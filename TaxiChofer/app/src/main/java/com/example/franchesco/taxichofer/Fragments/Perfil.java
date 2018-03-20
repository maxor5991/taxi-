package com.example.franchesco.taxichofer.Fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.franchesco.taxichofer.Home;
import com.example.franchesco.taxichofer.Login;
import com.example.franchesco.taxichofer.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Perfil extends Fragment {

    int Dni = 0;
    TextView txtNombre,txtApellido,txtDNI,txtCelular,txtCorreo,txtPlaca,txtMarca,txtModelo,txtColor;
    CircleImageView ImgPerfil;
    Button btnCerrarSesion;

    public Perfil() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);


        //Rucuperando DNI por SharedPreference
        RecuperaDniChofer();

        ImgPerfil = (CircleImageView)view.findViewById(R.id.ImgPerfil);
        txtNombre = view.findViewById(R.id.txtNombre);
        txtApellido = view.findViewById(R.id.txtApellido);
        txtDNI = view.findViewById(R.id.txtDNI);
        txtCelular = view.findViewById(R.id.txtCelular);
        txtCorreo = view.findViewById(R.id.txtCorreo);

        txtPlaca = view.findViewById(R.id.txtPlaca);
        txtMarca = view.findViewById(R.id.txtMarca);
        txtModelo = view.findViewById(R.id.txtModelo);
        txtColor = view.findViewById(R.id.txtColor);
        btnCerrarSesion = view.findViewById(R.id.btnCerrarSesion);

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login.cambiarEstado(getActivity(),false);
                Intent intent = new Intent(getActivity(),Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        ImprimirDatosChofer();
        ImprimirDatosVehiculo();
        return view;
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

                Picasso.with(getActivity()).load("http://katso.com.pe/rapitaxi/ServicioChofer/Imagenes/"+
                        json.getJSONObject(i).getString("Imagen")).into(ImgPerfil);

                txtNombre.setText(json.getJSONObject(i).getString("Nombre"));
                txtApellido.setText(json.getJSONObject(i).getString("Apellido"));
                txtDNI.setText(json.getJSONObject(i).getString("DNI"));
                txtCelular.setText(json.getJSONObject(i).getString("Celular"));
                txtCorreo.setText(json.getJSONObject(i).getString("Correo"));
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

                getActivity().runOnUiThread(new Runnable() {
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

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MostrarDatosVehiculo(res);
                    }
                });
            }
        };
        tr.start();
    }

    //Preferencias
    private void RecuperaDniChofer()
    {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Chofer", Context.MODE_PRIVATE);
        Dni = preferences.getInt("Dni",0);
    }
}
