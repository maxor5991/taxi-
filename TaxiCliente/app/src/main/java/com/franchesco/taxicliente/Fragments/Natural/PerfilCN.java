package com.franchesco.taxicliente.Fragments.Natural;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.franchesco.taxicliente.HomeCN;
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


public class PerfilCN extends Fragment {

    int Dni = 0;
    TextView txtNombre,txtDNI,txtCelular,txtCorreo,txtApellido;
    CircleImageView ImgPerfil;
    ImageButton ImgPerfilSolo;
    Button btnEditarCN;
    Button btnCerrarSesion;

    public PerfilCN() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil_cn, container, false);

        //Rucuperando DNI por SharedPreference
        RecuperaDniClienteN();

        ImgPerfil = (CircleImageView)view.findViewById(R.id.ImgPerfil);
        txtNombre = (TextView)view.findViewById(R.id.txtNombre);
        txtApellido = (TextView)view.findViewById(R.id.txtApellido);
        txtDNI = (TextView)view.findViewById(R.id.txtDNI);
        txtCelular = (TextView)view.findViewById(R.id.txtCelular);
        txtCorreo = (TextView)view.findViewById(R.id.txtCorreo);

        btnEditarCN = (Button)view.findViewById(R.id.btnEditarCN);

        ImgPerfilSolo = (ImageButton)view.findViewById(R.id.ImgPerfilSolo);

        ImgPerfilSolo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),ImgPerfilCN.class);
                intent.putExtra("Dni",Dni);//Enviando Dni
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.alpha_ingreso,R.anim.alpha_salida);
            }
        });


        btnEditarCN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),EditarPerfilCN.class);
                intent.putExtra("Dni",Dni);//Enviando Dni
                intent.putExtra("Celular",txtCelular.getText().toString());//Enviando Celular
                intent.putExtra("Nombre",txtNombre.getText().toString());//Enviando Nombre
                intent.putExtra("Apellido",txtApellido.getText().toString());//Enviando Apellido
                intent.putExtra("Correo",txtCorreo.getText().toString());//Enviando Correo
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.alpha_ingreso,R.anim.alpha_salida);
            }
        });

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

    public String consumeJSONget(int Dni)
    {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;
        try
        {
            url = new URL ("http://katso.com.pe/rapitaxi/ServicioCliente/perfiln.php?dni="+Dni);
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
                txtNombre.setText(json.getJSONObject(i).getString("Nombre"));
                txtApellido.setText(json.getJSONObject(i).getString("Apellido"));
                txtDNI.setText(json.getJSONObject(i).getString("DNI"));
                txtCelular.setText(json.getJSONObject(i).getString("Celular"));
                txtCorreo.setText(json.getJSONObject(i).getString("Correo"));
                Picasso.with(getActivity()).load("http://katso.com.pe/rapitaxi/ServicioCliente/Imagenes/PerfilN/"+
                        json.getJSONObject(i).getString("Imagen")).into(ImgPerfil);
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
                final String res = consumeJSONget(Dni);

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
    private void RecuperaDniClienteN()
    {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("ClienteN", Context.MODE_PRIVATE);
        Dni = preferences.getInt("Dni",0);
    }

}
