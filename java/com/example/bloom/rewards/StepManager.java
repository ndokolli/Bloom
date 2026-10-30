package com.example.bloom.rewards;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class StepManager implements SensorEventListener {

    private static StepManager instance;
    private final Context context;
    private final RewardManager rewards;
    private SensorManager sensorManager;
    private Handler autoStepHandler;
    private Runnable autoStepRunnable;
    private boolean isListening = false;

    private StepManager(Context context) {
        this.context = context.getApplicationContext();
        this.rewards = new RewardManager(this.context);
        this.sensorManager = (SensorManager) this.context.getSystemService(Context.SENSOR_SERVICE);
    }

    public static synchronized StepManager getInstance(Context context) {
        if (instance == null) {
            instance = new StepManager(context);
        }
        return instance;
    }

    public void start(Activity activity) {
        checkPermissionAndStart(activity);
        startAutoSimulation();
    }

    public void checkPermissionAndStart(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                if (activity != null) {
                    ActivityCompat.requestPermissions(
                            activity,
                            new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                            101
                    );
                }
            } else {
                registerSensors();
            }
        } else {
            registerSensors();
        }
    }

    public void registerSensors() {
        if (isListening || sensorManager == null) return;

        Sensor stepDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if (stepDetector != null) {
            sensorManager.registerListener(this, stepDetector, SensorManager.SENSOR_DELAY_NORMAL);
            isListening = true;
        }

        Sensor stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (stepCounter != null) {
            sensorManager.registerListener(this, stepCounter, SensorManager.SENSOR_DELAY_NORMAL);
            isListening = true;
        }
    }

    private void startAutoSimulation() {
        if (autoStepHandler != null) return;
        autoStepHandler = new Handler(Looper.getMainLooper());
        autoStepRunnable = new Runnable() {
            @Override
            public void run() {
                int extraSteps = 1 + (int) (Math.random() * 3);
                rewards.addSteps(extraSteps);
                autoStepHandler.postDelayed(this, 4000);
            }
        };
        autoStepHandler.postDelayed(autoStepRunnable, 4000);
    }

    public void stop() {
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            isListening = false;
        }
        if (autoStepHandler != null && autoStepRunnable != null) {
            autoStepHandler.removeCallbacks(autoStepRunnable);
            autoStepHandler = null;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            rewards.addSteps(1);
        } else if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (event.values.length > 0 && event.values[0] > 0) {
                rewards.addSteps(1);
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}
}
