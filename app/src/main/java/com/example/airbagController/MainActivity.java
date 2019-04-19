package com.example.airbagController;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
// Defines an element from the SensorManager group.
    private SensorManager sensorManager;
// Defines an element from the Sensor group.
    Sensor accelerometer;

// Defines the variables we will use to store the data.
    private TextView readingX, readingY, readingZ, absoluteValueText, thresholdTextView;
    private SeekBar thresholdSeekBar;
    public double X, Y, Z, threshold, absoluteValue;

    public static final double THRESHOLD_MAX = 4;
    public static final double THRESHOLD_MIN = 0.1;

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
        readingX = findViewById(R.id.axisX);
        readingY = findViewById(R.id.axisY);
        readingZ = findViewById(R.id.axisZ);
        absoluteValueText = findViewById(R.id.absoluteValueText);
        thresholdSeekBar = findViewById(R.id.threshlodSeekBar);
        thresholdTextView = findViewById(R.id.threshlodTextView);

        thresholdSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                threshold = THRESHOLD_MIN + (THRESHOLD_MAX - THRESHOLD_MIN) * (progress / 100.);

                thresholdTextView.setText(String.format("Threshlod: %.2f", threshold));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // intentionally void
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        X = event.values[0];
        Y = event.values[1];
        Z = event.values[2];

        readingX.setText(String.format("X: %.3f", X));
        readingY.setText(String.format("X: %.3f", Y));
        readingZ.setText(String.format("X: %.3f", Z));

        absoluteValue = Math.sqrt((Math.pow(X, 2) + Math.pow(Y, 2) + Math.pow(Z, 2)));

        absoluteValueText.setText(String.format("Absolute Value: %.4f", absoluteValue));

       if(absoluteValue <= threshold){
           ((TextView)findViewById(R.id.errorMessage)).setText("Ниско ускорение.");
           Button mButton = findViewById(R.id.colorButton);
           mButton.setBackgroundColor(Color.RED);
       }
       else{
           Button mButton = findViewById(R.id.colorButton);
           mButton.setBackgroundColor(Color.GREEN);
       }
    }
}
