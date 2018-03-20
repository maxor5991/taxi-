package com.example.franchesco.taxichofer.Fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.example.franchesco.taxichofer.Adaptadores.ListaCarreraAdaptador;
import com.example.franchesco.taxichofer.Clases.Carreras;
import com.example.franchesco.taxichofer.Home;
import com.example.franchesco.taxichofer.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Carrera extends Fragment {

    int Dni = 0;
    int IdPedido = 0;

    List arraylist;
    RecyclerView ListaCarrera;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;


    public Carrera() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_carrera, container, false);


        //Rucuperando DNI por SharedPreference
        RecuperaDniChofer();

        arraylist = new ArrayList<>();
        ListaCarrera = (RecyclerView) view.findViewById(R.id.ListaCarrera);

        ImprimirDatos();

        return view;
    }


    //listarDatos
    private void ImprimirDatos()
    {
        Thread tr = new Thread(){
            @Override
            public void run()
            {

                final String res = consumeDniJSON(Dni);

                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        MostrarDatos(res);
                    }
                });
            }
        };
        tr.start();
    }

    public String consumeDniJSON(int Dni)
    {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;
        try
        {
            url = new URL ("http://katso.com.pe/rapitaxi/ServicioChofer/listarcarrera.php?dni="+Dni);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            respuesta = connection.getResponseCode();

            resul = new StringBuilder();
            if (respuesta == HttpURLConnection.HTTP_OK)
            {
                InputStream in = new BufferedInputStream(connection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                while ((linea = reader.readLine())!=null)
                {
                    resul.append(linea);
                }
            }
        }
        catch (Exception e)
        {

        }
        return  resul.toString();
    }

    public void MostrarDatos(String rspta)
    {
        try
        {
            JSONArray json = new JSONArray(rspta);
            for (int i = 0 ; i<json.length();i++)
            {
                JSONObject jsonObject = json.getJSONObject(i);
                arraylist.add(new Carreras(
                        jsonObject.getString("IdPedido"),
                        jsonObject.getString("ReferenciaDestino"),
                        jsonObject.getString("NumPersonas"),
                        jsonObject.getString("Hora"),
                        "("+ jsonObject.getString("ReferenciaOrigen") +")" +" " +jsonObject.getString("DireccionOrigen"),
                        jsonObject.getString("DireccionDestino")
                ));
            }

        }
        catch (Exception e)
        {
            e.getMessage();
        }
        ListaCarrera = ListaCarrera.findViewById(R.id.ListaCarrera);
        ListaCarrera.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        ListaCarrera.setLayoutManager(layoutManager);

        adapter = new ListaCarreraAdaptador(arraylist);
        ListaCarrera.setAdapter(adapter);
    }

    //Preferencias
    private void RecuperaDniChofer()
    {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Chofer", Context.MODE_PRIVATE);
        Dni = preferences.getInt("Dni",0);
    }
}
