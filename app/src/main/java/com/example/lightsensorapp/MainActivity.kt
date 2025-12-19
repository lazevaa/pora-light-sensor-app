package com.example.lightsensorapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var tvStatus: TextView
    private lateinit var tvValue: TextView
    private lateinit var etMin: EditText
    private lateinit var etMax: EditText
    private lateinit var etInterval: EditText
    private lateinit var btnStart: Button
    private lateinit var btnStop: Button

    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private var running = false

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var mqttHelper: MqttHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvStatus = findViewById(R.id.tvStatus)
        tvValue = findViewById(R.id.tvValue)
        etMin = findViewById(R.id.etMin)
        etMax = findViewById(R.id.etMax)
        etInterval = findViewById(R.id.etInterval)
        btnStart = findViewById(R.id.btnStart)
        btnStop = findViewById(R.id.btnStop)

        handler = Handler(Looper.getMainLooper())
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mqttHelper = MqttHelper(applicationContext)
        mqttHelper.connect()

        btnStart.setOnClickListener { startSampling() }
        btnStop.setOnClickListener { stopSampling() }
    }

    private fun startSampling() {
        val min = etMin.text.toString().toIntOrNull() ?: 0
        val max = etMax.text.toString().toIntOrNull() ?: 1000
        val interval = (etInterval.text.toString().toLongOrNull() ?: 10L) * 1000

        if (running) return
        running = true
        tvStatus.text = "Status: Running"

        runnable = object : Runnable {
            override fun run() {
                // Simulacija svetlosti
                val lux = (min..max).random()

                // Čas
                val time = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(Date())

                // Lokacija
                if (ActivityCompat.checkSelfPermission(this@MainActivity,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Če ni dovoljenja, pošljemo fiktivno lokacijo
                    sendData(lux, time, 46.056, 14.505)
                } else {
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        val lat = location?.latitude ?: 46.056
                        val lon = location?.longitude ?: 14.505
                        sendData(lux, time, lat, lon)
                    }
                }

                tvValue.text = "Svetlost: $lux lux"
                handler.postDelayed(this, interval)
            }
        }
        handler.post(runnable)
    }

    private fun stopSampling() {
        if (!running) return
        running = false
        handler.removeCallbacks(runnable)
        tvStatus.text = "Status: Stopped"
    }

    private fun sendData(lux: Int, time: String, lat: Double, lon: Double) {
        val json = """{"lux":$lux,"time":"$time","lat":$lat,"lon":$lon}"""
        mqttHelper.publish("myapp/light", json)
    }
}
