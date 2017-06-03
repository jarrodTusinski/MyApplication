package com.example.jarek.gps;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LocationListener{

    TextView dostawca;
    TextView dlugosc;
    TextView szerokosc;
    Criteria cr;
    Location loc;
    String mojDostawca;
    LocationManager mylm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dostawca=(TextView) findViewById(R.id.textView1);
        dlugosc=(TextView) findViewById(R.id.textView2);
        szerokosc=(TextView) findViewById(R.id.textView3);
        cr = new Criteria();
        cr.setAccuracy(Criteria.NO_REQUIREMENT);
        cr.setSpeedAccuracy(Criteria.NO_REQUIREMENT);
        cr.setPowerRequirement(Criteria.POWER_MEDIUM);
        mylm = (LocationManager) getSystemService(LOCATION_SERVICE);
        mojDostawca = mylm.getBestProvider(cr,true);
        setGPSMessage();
    }

    private void setGPSMessage() {
        loc = mylm.getLastKnownLocation(mojDostawca);
        dostawca.setText("dostawca: " + mojDostawca);
        dlugosc.setText("dlugosc: " + loc.getLongitude());
        szerokosc.setText("szerokosc: " + loc.getLatitude());
    }

    @Override
    public void onLocationChanged(Location location) {
        setGPSMessage();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        mylm.requestLocationUpdates(mojDostawca,400,1,this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mylm.removeUpdates(this);
    }

    public void goToDropbox(View v) {
        Context context = getApplicationContext();
        Intent intent = new Intent(context,DropboxApi.class);
        startActivity(intent);
    }
}