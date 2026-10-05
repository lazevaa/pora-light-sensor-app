# Light Sensor App

An Android app that reads the device's ambient light sensor and publishes timestamped, geotagged measurements to an MQTT topic in real time. It can also generate simulated values, so it works on devices and emulators without a light sensor.

## Screenshots

<p align="center">
  <img src="../screenshots/mqtt_ni_povezan.png" alt="MQTT ni povezan" width="30%">
  <img src="../screenshots/mqtt_povezan.png" alt="MQTT ni povezan" width="30%">
  <img src="../screenshots/nacin_delovanja.png" alt="Nacin delovanja" width="30%">
  <img src="../screenshots/res_sim.png" alt="Real/simulated" width="30%">
<img src="../screenshots/nastavitve.png" alt="Nastavitve" width="30%">
<img src="../screenshots/real_status.png" alt="Real status" width="30%">
<img src="../screenshots/simulated_status.png" alt="Simulated status" width="30%">
</p>

## Features
- Reads ambient light (lux) from the device sensor via `SensorManager`
- **Real** and **simulated** mode (random values within a user-defined min/max range)
- Configurable publishing interval: 1 s, 5 s, 10 s, 30 s, 1 min or 5 min
- Attaches GPS location and timestamp to every measurement
- Publishes JSON messages over MQTT (HiveMQ client, public HiveMQ broker)
- Live status on screen: connection state, sensor info, last published data, number of messages sent
- Runtime permission handling for location

## Message format
Each measurement is published as JSON to a randomly generated topic (`lightsensor/data/<id>`, shown in the app):

```json
{
  "timestamp": "2026-10-05 14:32:10",
  "lightLevel": 245.5,
  "latitude": 46.5547,
  "longitude": 15.6459,
  "mode": "real"
}
```

## Tech stack
- Kotlin
- Android SDK (SensorManager, ViewBinding)
- Kotlin Coroutines
- Google Play Services Fused Location Provider
- HiveMQ MQTT Client (MQTT 3)
- Gson

## Getting started
1. Clone the repository: `git clone https://github.com/lazevaa/pora-light-sensor-app`
2. Open it in **Android Studio** and wait for Gradle sync to finish
3. Run on a physical device (recommended, it has a real light sensor and GPS) or an emulator
4. Allow the location permission
5. Tap **Connect MQTT**, then **ZAČNI** (start)

## Viewing the data
Subscribe to the topic shown in the app using any MQTT client, for example the
[HiveMQ web client](https://www.hivemq.com/demos/websocket-client/) or MQTTX (broker `broker.hivemq.com`, port `1883`).

## Notes
- The app uses a **public test broker without authentication or encryption**, so it is intended for demonstration only. Do not send sensitive data.
- The interface is in Slovenian.

## Author
Lazeva, Computer Science student at FERI, University of Maribor
Course project: [Platformno odvisen razvoj aplikacij 2025/2026]
