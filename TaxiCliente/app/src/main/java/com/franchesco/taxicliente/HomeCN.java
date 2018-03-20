package com.franchesco.taxicliente;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.franchesco.taxicliente.Fragments.Natural.InicioCN;
import com.franchesco.taxicliente.Fragments.Natural.PedidoCN;
import com.franchesco.taxicliente.Fragments.Natural.PerfilCN;

public class HomeCN extends AppCompatActivity {

    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    int Dni = 0;
    double Latitud,Longitud;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {

                case R.id.navigation_pedido:

                    transaction.replace(R.id.CoontenedorCN,new PedidoCN()).commit();
                    return true;
                case R.id.navigation_home:

                    transaction.replace(R.id.CoontenedorCN,new InicioCN()).commit();
                    return true;

                case R.id.navigation_Perfil:

                    transaction.replace(R.id.CoontenedorCN,new PerfilCN()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cn);

        //Rucuperando DNI por SharedPreference
        RecuperaDniClienteN();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        getLocation();
    }


    //en esta clase Capturo La Latitud
    public double getLatitud()
    {
        return Latitud;
    }

    //en esta clase Capturo La Longitud
    public double getLongitud()
    {
        return Longitud;
    }


    void getLocation()
    {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)
        {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        }
        else
        {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null)
            {
                Latitud = location.getLatitude();
                Longitud = location.getLongitude();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case REQUEST_LOCATION:
                getLocation();
                break;
        }
    }

    //Preferencias
    private void RecuperaDniClienteN()
    {
        SharedPreferences preferences = getSharedPreferences("ClienteN", Context.MODE_PRIVATE);
        Dni = preferences.getInt("Dni",0);
    }

    @Override
    public void onBackPressed()
    {
    }
}
