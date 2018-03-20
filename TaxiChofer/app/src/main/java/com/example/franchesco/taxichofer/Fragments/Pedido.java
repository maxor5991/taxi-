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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.franchesco.taxichofer.Activitys.Solicitud;
import com.example.franchesco.taxichofer.Adaptadores.ListaPedidoAdaptador;
import com.example.franchesco.taxichofer.Clases.Pedidos;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class Pedido extends Fragment {

    int Dni = 0;
    ArrayList<Pedidos> arrayList;
    ListView ListaPedido;
    ListaPedidoAdaptador adapter;

    TextView txtIdPedido;

    public Pedido() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pedido, container, false);


        //Rucuperando DNI por SharedPreference
        RecuperaDniChofer();

        txtIdPedido = (TextView)view.findViewById(R.id.txtIdPedido);

        arrayList = new ArrayList<>();
        ListaPedido = (ListView)view.findViewById(R.id.ListaPedido);

        ListaPedido.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //String IdPedido = adapter.getIdPedido(i);
                //Toast.makeText(getActivity(),IdPedido,Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), Solicitud.class);
                intent.putExtra("IdPedido",Integer.parseInt(adapter.getIdPedido(i)));
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.alpha_ingreso, R.anim.alpha_salida);
            }
        });

        ImprimirDatos();

        return view;
    }

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
            url = new URL ("http://katso.com.pe/rapitaxi/ServicioChofer/listarsolicitudch.php?dni="+Dni);
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
                arrayList.add(new Pedidos(
                        jsonObject.getString("IdPedido"),
                        jsonObject.getString("ReferenciaDestino"),
                        jsonObject.getString("NumPersonas"),
                        jsonObject.getString("Hora")
                ));
            }

        }
        catch (Exception e)
        {
            e.getMessage();
        }
        adapter = new ListaPedidoAdaptador(getActivity(),R.layout.item_pedido,arrayList);
        ListaPedido.setAdapter(adapter);
    }

    //Preferencias
    private void RecuperaDniChofer()
    {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("Chofer", Context.MODE_PRIVATE);
        Dni = preferences.getInt("Dni",0);
    }
}
