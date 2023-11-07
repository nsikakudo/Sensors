package com.example.sensors

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sensors.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var sensorHelper: DeviceSensorHelper
    private lateinit var binding: ActivityMainBinding

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var lightSensorListener: SensorEventListener
    private lateinit var proximitySensorListener: SensorEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorHelper = DeviceSensorHelper(this)
        val allSensors = sensorHelper.getAllSensors()

        val sensorListAdapter = DeviceSensorAdapter(this, allSensors)
        binding.sensorListView.adapter = sensorListAdapter

        lightAndProximitySensors()

//        TODO("Remove code")
        for (sensor in allSensors) {
            println("Sensor Name: ${sensor.name}")
            println("Sensor Type: ${sensor.type}")
        }

    }

    private fun lightAndProximitySensors() {

        lightSensorListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            override fun onSensorChanged(event: SensorEvent) {
                binding.apply {
                    lightSensorAccuracy.text = checkAccuracy(event.accuracy)
                    lightSensorValue.text = getString(R.string.lx, event.values[0])
                }
            }
        }

        proximitySensorListener = object : SensorEventListener {
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
            override fun onSensorChanged(event: SensorEvent) {
                binding.apply {
                    proximitySensorAccuracy.text = checkAccuracy(event.accuracy)
                    proximitySensorValue.text = if (event.values[0] == 0.0f) {
                        getString(R.string.close, event.values[0])
                    } else {
                        getString(R.string.far, event.values[0])
                    }
                }
            }
        }

        val lightSensor = sensorHelper.find(Sensor.TYPE_LIGHT)
        if (lightSensor != null) {
            sensorHelper.addSensorListener(lightSensor, lightSensorListener)
        } else {
            Log.e(TAG, "lightAndProximitySensors: Light sensor is not available")
        }

        val proximitySensor = sensorHelper.find(Sensor.TYPE_PROXIMITY)
        if (proximitySensor != null) {
            sensorHelper.addSensorListener(proximitySensor, proximitySensorListener)
        } else {
            Log.e(TAG, "lightAndProximitySensors: proximity sensor is not available")
        }
    }

    private fun checkAccuracy(accuracy: Int): String {
        return when (accuracy) {
            0 -> getString(R.string.accuracy_unreliable)
            1 -> getString(R.string.accuracy_low)
            2 -> getString(R.string.accuracy_medium)
            3 -> getString(R.string.accuracy_high)
            else -> getString(R.string.unknown_accuracy)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        sensorHelper.removeSensorListener(lightSensorListener)
        sensorHelper.removeSensorListener(proximitySensorListener)
    }
}