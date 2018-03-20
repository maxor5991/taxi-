package com.franchesco.taxicliente.Fragments.Juridico;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.franchesco.taxicliente.Adaptadores.PedidoListaCJAdapter;
import com.franchesco.taxicliente.Clases.PedidoListaCJ;
import com.franchesco.taxicliente.Fragments.Natural.PedidoChofer;
import com.franchesco.taxicliente.HomeCJ;
import com.franchesco.taxicliente.R;

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
public class InicioCJ extends Fragment {

    long Ruc = 0;
    ArrayList<PedidoListaCJ> arrayList;
    ListView ListaPedidoCJ;
    PedidoListaCJAdapter adapter;

    public InicioCJ() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_inicio_cj, container, false);

        //Rucuperando DNI por SharedPreference
        RecuperaDniClienteJ();

        arrayList = new ArrayList<>();
        ListaPedidoCJ = (ListView)view.findViewById(R.id.ListaPedidoCJ);

        ListaPedidoCJ.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //String IdPedido = adapter.getIdPedido(i);
                //Toast.makeText(getActivity(),IdPedido,Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), PedidoChofer.class);
                intent.putExtra("Dni",Integer.parseInt(adapter.getDniChofer(i)));
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

                final String res = consumeDniJSONget(Ruc);

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

    public String consumeDniJSONget(long IdCliente)
    {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;
        try
        {
            url = new URL ("http://katso.com.pe/rapitaxi/ServicioCliente/listarpedidocj.php?idcliente="+IdCliente);
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
                arrayList.add(new PedidoListaCJ(
                        jsonObject.getString("ReferenciaDestino"),
                        jsonObject.getString("NumPersonas"),
                        jsonObject.getString("Hora"),
                        jsonObject.getString("DniChofer")
                ));
            }

        }
        catch (Exception e)
        {
            e.getMessage();
        }
        adapter = new PedidoListaCJAdapter(getActivity(),R.layout.item_lista_pedido_cj,arrayList);
        ListaPedidoCJ.setAdapter(adapter);
    }

    //Preferencias
    private void RecuperaDniClienteJ()
    {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("ClienteJ", Context.MODE_PRIVATE);
        Ruc = preferences.getLong("Ruc",0);
    }
}
