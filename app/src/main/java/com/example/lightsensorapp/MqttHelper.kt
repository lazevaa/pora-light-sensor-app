package com.example.lightsensorapp

import android.content.Context
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.util.*

class MqttHelper(context: Context) {

    private val serverUri = "tcp://broker.emqx.io:1883"
    private val clientId = "AndroidClient_${UUID.randomUUID()}"
    private val client = MqttAndroidClient(context, serverUri, clientId)

    fun connect() {
        val options = MqttConnectOptions()
        options.isCleanSession = true

        try {
            client.connect(options, null, null)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }

    fun publish(topic: String, payload: String) {
        try {
            val message = MqttMessage()
            message.payload = payload.toByteArray()
            client.publish(topic, message)
        } catch (e: MqttException) {
            e.printStackTrace()
        }
    }
}
