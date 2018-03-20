package com.franchesco.taxicliente.Fragments.Natural;

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

import com.franchesco.taxicliente.Adaptadores.PedidoListaCNAdapter;
import com.franchesco.taxicliente.Clases.PedidoListaCN;
import com.franchesco.taxicliente.HomeCN;
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

public class InicioCN extends Fragment {

    int Dni = 0;
    ArrayList<PedidoListaCN> arrayList;
    ListView ListaPedidoCN;
    PedidoListaCNAdapter adapter;

    public InicioCN() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio_cn, container, false);

        //Rucuperando DNI por SharedPreference
        RecuperaDniClienteN();

        arrayList = new ArrayList<>();
        ListaPedidoCN = (ListView)view.findViewById(R.id.ListaPedidoCN);

        ListaPedidoCN.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                //String IdPedido = adapter.getIdPedido(i);
                //Toast.makeText(getActivity(),IdPedido,Toast.LENGTH_LONG).show();

                Intent intent = new Intent(getActivity(), PedidoChofer.class);
                intent.putExtra("Dni",Integer.parseInt(adapter.getDni(i)));
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

                final String res = consumeDniJSONget(Dni);

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

    public String consumeDniJSONget(int IdCliente)
    {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;
        try
        {
            url = new URL ("http://katso.com.pe/rapitaxi/ServicioCliente/listarpedidocn.php?idcliente="+IdCliente);
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
                arrayList.add(new PedidoListaCN(
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
        adapter = new PedidoListaCNAdapter(getActivity(),R.layout.item_lista_pedido_cn,arrayList);
        ListaPedidoCN.setAdapter(adapter);
    }

    //Preferencias
    private void RecuperaDniClienteN()
    {
        SharedPreferences preferences = this.getActivity().getSharedPreferences("ClienteN", Context.MODE_PRIVATE);
        Dni = preferences.getInt("Dni",0);
    }

}
