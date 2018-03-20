package com.franchesco.taxicliente.Fragments.Juridico;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.franchesco.taxicliente.HomeCJ;
import com.franchesco.taxicliente.Login;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class PerfilCJ extends Fragment {

    long Ruc = 0;
    TextView txtNombreEmp,txtRuc,txtTelefono,txtRepresentante;
    CircleImageView ImgPerfilJ;
    Button btnCerrarSesion;

    public PerfilCJ() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil_cj, container, false);

        //Rucuperando DNI por SharedPreference
        RecuperaDniClienteJ();

        ImgPerfilJ = (CircleImageView)view.findViewById(R.id.ImgPerfilJ);
        txtNombreEmp = (TextView)view.findViewById(R.id.txtNombreEmp);
        txtRuc = (TextView)view.findViewById(R.id.txtRuc);
        txtTelefono = (TextView)view.findViewById(R.id.txtTelefono);
        txtRepresentante = (TextView)view.findViewById(R.id.txtRepresentante);

        ImprimirDatos();

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

        return view;
    }

    public String consumeJSONget(long Ruc)
    {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;
        try
        {
            url = new URL ("http://katso.com.pe/rapitaxi/ServicioCliente/perfilj.php?ruc="+Ruc);
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
                txtNombreEmp.setText(json.getJSONObject(i).getString("NombreEmpre"));
                txtRuc.setText(json.getJSONObject(i).getString("RUC"));
                txtTelefono.setText(json.getJSONObject(i).getString("Telefono"));
                txtRepresentante.setText(json.getJSONObject(i).getString("Representante"));
                Picasso.with(getActivity()).load("http://katso.com.pe/rapitaxi/ServicioCliente/Imagenes/PerfilJ/"+
                        json.getJSONObject(i).getString("Imagen")).into(ImgPerfilJ);
            }
        }
        catch (Exception e)
        {

        }
    }

    public void ImprimirDatos()
    {
        Thread tr = new Thread(){
            @Override
            public void run()
            {
                final String res = consumeJSONget(Ruc);

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

    //Preferencias
    private void RecuperaDniClienteJ()
    {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("ClienteJ", Context.MODE_PRIVATE);
        Ruc = preferences.getLong("Ruc",0);
    }
}
