package com.example.airbagController;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Color;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.SoundPool;

import android.os.Bundle;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private static final String TAG = "MainActivity";
// Defines an element from the SensorManager group.
    private SensorManager sensorManager;
// Defines an element from the Sensor group.
    Sensor accelerometer;
// Defines the tone.
    private SoundPool soundPool;
    private int beep;

// Defines the variables we will use to store the data.
    private TextView readingX, readingY, readingZ, absoluteValueText, thresholdTextView;
    private SeekBar thresholdSeekBar;
    private Button forceSignalButton;
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
 // Declares soundPool.
        soundPool = new SoundPool(1, AudioManager.STREAM_MUSIC,0);

// Declare the beep's location.
        beep = soundPool.load(this, R.raw.beep, 1);

// Define the strings that we will store the data in.
        readingX = findViewById(R.id.axisX);
        readingY = findViewById(R.id.axisY);
        readingZ = findViewById(R.id.axisZ);
        absoluteValueText = findViewById(R.id.absoluteValueText);
        thresholdSeekBar = findViewById(R.id.threshlodSeekBar);
        forceSignalButton = findViewById(R.id.forceSignalButton);
        thresholdTextView = findViewById(R.id.threshlodTextView);

        forceSignalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                playSound(1500, 441000);
                soundPool.play(beep, 1, 1, 1, 0, 1);
            }
        });
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

    public void playSound(double frequency, int duration) {
        // AudioTrack definition
        int mBufferSize = AudioTrack.getMinBufferSize(44100,
                AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_8BIT);

        AudioTrack mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, 44100,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT,
                mBufferSize, AudioTrack.MODE_STREAM);

        // Sine wave
        double[] mSound = new double[4410];
        short[] mBuffer = new short[duration];
        for (int i = 0; i < mSound.length; i++) {
            mSound[i] = 1;//Math.sin((2.0*Math.PI * i/(44100/frequency)));
            mBuffer[i] = (short) (mSound[i]*Short.MAX_VALUE);
        }

        mAudioTrack.setStereoVolume(AudioTrack.getMaxVolume(), AudioTrack.getMaxVolume());
        mAudioTrack.play();

        mAudioTrack.write(mBuffer, 0, mSound.length);
        mAudioTrack.stop();
        mAudioTrack.release();

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

        absoluteValueText.setText(String.format("Absolute Value: %.3f", absoluteValue));

        Button mButton = findViewById(R.id.colorButton);

       if(absoluteValue <= threshold){
           mButton.setBackgroundColor(Color.RED);
           soundPool.play(beep, 1, 1, 1, 0, 1);
//           playSound(1500, 441000);
       }
       else{
           mButton.setBackgroundColor(Color.GREEN);
       }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
        soundPool = null;
    }
}
