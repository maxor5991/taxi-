package com.franchesco.taxicliente.Fragments.Natural;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.franchesco.taxicliente.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class PedidoChofer extends AppCompatActivity {

    int Dni = 0;
    TextView txtNombre,txtDni,txtPlaca,txtMarca,txtModelo,txtColor;
    CircleImageView ImgPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_chofer);

        //Recuperando Dni
        Bundle recupera = getIntent().getExtras();

        if (recupera!=null)
            Dni = recupera.getInt("Dni");

        ImgPerfil = (CircleImageView)findViewById(R.id.ImgPerfil);
        txtNombre = findViewById(R.id.txtNombre);
        txtDni = findViewById(R.id.txtDni);

        txtPlaca = findViewById(R.id.txtPlaca);
        txtMarca = findViewById(R.id.txtMarca);
        txtModelo = findViewById(R.id.txtModelo);
        txtColor = findViewById(R.id.txtColor);
        ImprimirDatosChofer();
        ImprimirDatosVehiculo();
    }


    //Chofer
    public String consumeJSONChofer(int Dni)
    {
        URL urlPerfil = null;
        String lineaPerfil = "";
        int respuestaPerfil = 0;
        StringBuilder resul = null;

        try {
            urlPerfil = new URL ("http://katso.com.pe/rapitaxi/ServicioChofer/perfilch.php?dni=" + Dni);
            HttpURLConnection connectionPerfil = (HttpURLConnection) urlPerfil.openConnection();
            respuestaPerfil = connectionPerfil.getResponseCode();
            resul = new StringBuilder();

            if (respuestaPerfil == HttpURLConnection.HTTP_OK) {
                InputStream inPerfil = new BufferedInputStream(connectionPerfil.getInputStream());
                BufferedReader readerPerfil = new BufferedReader(new InputStreamReader(inPerfil));
                while ((lineaPerfil = readerPerfil.readLine()) != null) {
                    resul.append(lineaPerfil);
                }
            }
        }
        catch (Exception e) {

        }
        return  resul.toString();
    }

    public void MostrarDatosChofer(String rspta)
    {
        try
        {
            JSONArray json = new JSONArray(rspta);
            for (int i = 0 ; i<json.length();i++) {

                Picasso.with(this).load("http://katso.com.pe/rapitaxi/ServicioChofer/Imagenes/"+
                        json.getJSONObject(i).getString("Imagen")).into(ImgPerfil);

                txtNombre.setText(json.getJSONObject(i).getString("Nombre"));
                txtDni.setText(json.getJSONObject(i).getString("DNI"));
            }
        }
        catch (Exception e) {

        }
    }

    public void ImprimirDatosChofer()
    {
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String res = consumeJSONChofer(Dni);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MostrarDatosChofer(res);
                    }
                });
            }
        };
        tr.start();
    }

    //Auto
    public String consumeJSONVehiculo(int Dni)
    {
        URL urlVehiculo = null;
        String lineaVehiculo = "";
        int respuestaVehiculo = 0;
        StringBuilder resul = null;

        try {
            urlVehiculo = new URL ("http://katso.com.pe/rapitaxi/ServicioChofer/vehiculoch.php?dni=" + Dni);
            HttpURLConnection connectionPerfil = (HttpURLConnection) urlVehiculo.openConnection();
            respuestaVehiculo = connectionPerfil.getResponseCode();
            resul = new StringBuilder();

            if (respuestaVehiculo == HttpURLConnection.HTTP_OK) {
                InputStream inPerfil = new BufferedInputStream(connectionPerfil.getInputStream());
                BufferedReader readerPerfil = new BufferedReader(new InputStreamReader(inPerfil));
                while ((lineaVehiculo = readerPerfil.readLine()) != null) {
                    resul.append(lineaVehiculo);
                }
            }
        }
        catch (Exception e) {

        }
        return  resul.toString();
    }

    public void MostrarDatosVehiculo(String rspta)
    {
        try
        {
            JSONArray json = new JSONArray(rspta);
            for (int i = 0 ; i<json.length();i++)
            {
                txtPlaca.setText(json.getJSONObject(i).getString("Placa"));
                txtMarca.setText(json.getJSONObject(i).getString("Marca"));
                txtModelo.setText(json.getJSONObject(i).getString("Modelo"));
                txtColor.setText(json.getJSONObject(i).getString("Color"));
            }
        }
        catch (Exception e) {

        }
    }

    public void ImprimirDatosVehiculo()
    {
        Thread tr = new Thread() {
            @Override
            public void run() {
                final String res = consumeJSONVehiculo(Dni);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MostrarDatosVehiculo(res);
                    }
                });
            }
        };
        tr.start();
    }
}
