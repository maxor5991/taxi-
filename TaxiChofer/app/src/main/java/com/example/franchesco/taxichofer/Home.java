package com.example.franchesco.taxichofer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.franchesco.taxichofer.Fragments.Carrera;
import com.example.franchesco.taxichofer.Fragments.Pedido;
import com.example.franchesco.taxichofer.Fragments.Perfil;

public class Home extends AppCompatActivity {

    int Dni = 0;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch (item.getItemId()) {
                case R.id.navigation_pedido:
                    transaction.replace(R.id.Contenedor,new Pedido()).commit();
                    return true;
                case R.id.navigation_home:
                    transaction.replace(R.id.Contenedor,new Carrera()).commit();
                    return true;
                case R.id.navigation_perfil:
                    transaction.replace(R.id.Contenedor,new Perfil()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Rucuperando DNI por SharedPreference
        RecuperaDniChofer();

        //Mostrar Pedidos al inicar la App
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.Contenedor,new Pedido()).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    //Preferencias
    private void RecuperaDniChofer()
    {
        SharedPreferences preferences = getSharedPreferences("Chofer", Context.MODE_PRIVATE);
        Dni = preferences.getInt("Dni",0);
    }


    @Override
    public void onBackPressed()
    {
    }
}
