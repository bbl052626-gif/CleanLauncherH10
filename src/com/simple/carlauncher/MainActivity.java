package com.example.cleanlauncher; // Asegúrate de que este sea tu paquete correcto

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textTime;
    private TextView textSpeed;
    private ProgressBar progressOil;
    private TextView textOilStatus;
    private Handler clockHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. Inicializar vistas
        textTime = findViewById(R.id.text_time);
        textSpeed = findViewById(R.id.text_speed);
        progressOil = findViewById(R.id.progress_oil);
        textOilStatus = findViewById(R.id.text_oil_status);

        // 2. Iniciar Reloj en tiempo real
        startClock();

        // 3. Iniciar Velocímetro por GPS
        startGpsSpeedometer();

        // 4. Cargar Recordatorio de Aceite (Hardcoded por ahora)
        setupOilReminder();
    }

    private void startClock() {
        Runnable clockRunnable = new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                textTime.setText(sdf.format(new Date()));
                clockHandler.postDelayed(this, 1000); // Actualizar cada segundo
            }
        };
        clockHandler.post(clockRunnable);
    }

    private void startGpsSpeedometer() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                if (location != null) {
                    // location.getSpeed() viene en m/s. Pasarlo a km/h (* 3.6)
                    float speedKmh = location.getSpeed() * 3.6f;
                    // Limitar a entero y evitar valores negativos por fluctuación GPS
                    int speedInt = Math.max(0, (int) speedKmh);
                    textSpeed.setText(String.valueOf(speedInt));
                }
            }
            @Override public void onStatusChanged(String p, int s, Bundle b) {}
            @Override public void onProviderEnabled(String p) {}
            @Override public void onProviderDisabled(String p) {}
        };

        // Solicitar permisos de ubicación para el GPS
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
 
