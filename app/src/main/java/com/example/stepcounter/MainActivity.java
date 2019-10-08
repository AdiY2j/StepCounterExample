package com.example.stepcounter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements SensorEventListener{

    private static final String TAG = "MainActivity";
    @BindView(R.id.tvSteps)
    TextView tvSteps;
    @BindView(R.id.btnStart)
    Button btnStart;
    @BindView(R.id.btnStop)
    Button btnStop;
    @BindView(R.id.counterLayout)
    LinearLayout counterLayout;
    @BindView(R.id.imgSteps)
    ImageView imgSteps;

    private SensorManager sensorManager;
    private Sensor sensor;
    private int flag = 0;
    private long initialValue = 0, steps = 0;
    private boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupCounterService();
    }

    private void setupCounterService() {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(sensor != null){
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST);
        }else{
            Toast.makeText(this, "No Sensor Detected !!", Toast.LENGTH_SHORT).show();
            btnStart.setEnabled(false);
        }

    }


    @OnClick(R.id.btnStart)
    public void startCounter(){
        running = true;
        showStartLayout();
    }

    private void showStartLayout() {
        steps = 0;
        tvSteps.setText(String.valueOf(steps));
        if(btnStart.getVisibility() == View.VISIBLE){
            btnStart.setVisibility(View.GONE);
        }
        if(counterLayout.getVisibility() == View.GONE){
            counterLayout.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btnStop)
    public void stopCounter(){
        running = false;
        showStopLayout();
        sensorManager.unregisterListener(this);
    }

    private void showStopLayout() {
        flag = 0;
        if(btnStart.getVisibility() == View.GONE){
            btnStart.setVisibility(View.VISIBLE);
        }
        if(counterLayout.getVisibility() == View.VISIBLE){
            counterLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if(running) {
            if (flag == 0) {
                flag = 1;
                initialValue = (long) sensorEvent.values[0];
            }
            steps = (long) (sensorEvent.values[0] - initialValue);
            tvSteps.setText(String.valueOf(steps));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

}
