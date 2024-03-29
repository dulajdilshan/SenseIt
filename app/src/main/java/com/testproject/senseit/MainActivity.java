package com.testproject.senseit;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;

    private float lastX = 0;
    private float lastY = 0;
    private float lastZ = 0;

    private TextView textX;
    private TextView textY;
    private TextView textZ;

    private float movementThreshold = 0.1f;

    DecimalFormat df = new DecimalFormat("###.###");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        textX = findViewById(R.id.txt_x);
        textY = findViewById(R.id.txt_y);
        textZ = findViewById(R.id.txt_z);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            // success! we have an accelerometer

            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            //sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
            System.out.println("initialized");

        } else {
            // fail we don't have an accelerometer!
            System.out.println("init failed");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        deltaX = Math.abs(lastX - sensorEvent.values[0]);
        deltaY = Math.abs(lastY - sensorEvent.values[1]);
        deltaZ = Math.abs(lastZ - sensorEvent.values[2]);

        // if the change is below 1, it is just plain noise
        if (deltaX < movementThreshold)
            deltaX = 0;
        if (deltaY < movementThreshold)
            deltaY = 0;
        if (deltaZ < movementThreshold)
            deltaZ = 0;


        // set the last know values of x,y,z
        lastX = sensorEvent.values[0];
        lastY = sensorEvent.values[1];
        lastZ = sensorEvent.values[2];


        textX.setText(Float.toString(deltaX));
        textY.setText(Float.toString(deltaY));
        textZ.setText(Float.toString(deltaZ));

//        textX.setText(df.format(lastX));
//        textY.setText(df.format(lastY));
//        textZ.setText(df.format(lastZ));

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        System.out.println("accuracy changed");
    }

    public void onClickStart(View view){
        System.out.println("starting");
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onClickStop(View view){
        System.out.println("stopping");
        sensorManager.unregisterListener(this);
    }
}
