package com.example.studyprogram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
// Defines an element from the SensorManager group.
    private SensorManager sensorManager;
// Defines an element from the Sensor group.
    Sensor accelerometer;
// Defines the variables we will use to store the data.
    private TextView readingX, readingY, readingZ;
    public double X, Y, Z;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Initializing Sensor Services");
// Gets the sensor service in the sensorManager element.
        sensorManager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
// Gets the accelerometer data and puts it to the accelerometer.
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
// Sets the delay to zero.
        sensorManager.registerListener(MainActivity.this, accelerometer, sensorManager.SENSOR_DELAY_FASTEST);
        Log.d(TAG, "onCreate: Registered accelerometer listener");
// Define the strings that we will store the data in.
        readingX = (TextView)(findViewById(R.id.axisX));
        readingY = (TextView)(findViewById(R.id.axisY));
        readingZ = (TextView)(findViewById(R.id.axisZ));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
       readingX.setText("X: " + event.values[0]);
       readingY.setText("Y: " + event.values[1]);
       readingZ.setText("Z: " + event.values[2]);
       X = event.values[0];
       Y = event.values[1];
       Z = event.values[2];
       float absoluteValue = (float)Math.sqrt((Math.pow(X, 2) + Math.pow(Y, 2) + Math.pow(Z, 2)));
        ((TextView)findViewById(R.id.absoluteValueText)).setText(String.valueOf(absoluteValue));
       if(absoluteValue<=2){
           ((TextView)findViewById(R.id.errorMessage)).setText("Ниско ускорение.");
           Button mButton=(Button)findViewById(R.id.colorButton);
           mButton.setBackgroundColor(Color.RED);
       }
       else{
           Button mButton=(Button)findViewById(R.id.colorButton);
           mButton.setBackgroundColor(Color.GREEN);
       }
    }
}
