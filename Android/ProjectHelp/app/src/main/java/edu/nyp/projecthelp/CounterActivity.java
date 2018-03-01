package edu.nyp.projecthelp;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class CounterActivity extends AppCompatActivity implements SensorEventListener{

    TextView tv_Steps;

    SensorManager sensorManager;

    boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        running = true;

        tv_Steps = findViewById(R.id.tvSteps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void onResume(){
        super.onResume();

        running = true;

        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        }
        else {
            Toast.makeText(this, "No sensor detected!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onPause(){
        super.onPause();

        running = false;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {

        if (running) {
            tv_Steps.setText(String.valueOf(event.values[0]));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
