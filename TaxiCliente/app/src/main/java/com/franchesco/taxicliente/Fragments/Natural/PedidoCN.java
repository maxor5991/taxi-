package com.franchesco.taxicliente.Fragments.Natural;


import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.franchesco.taxicliente.HomeCN;
import com.franchesco.taxicliente.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

public class PedidoCN extends Fragment {

    String Fecha;
    int Hora,Minuto;
    double Latitud,Longitud;
    private String[] arraySpinner;
    int Dni = 0;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    EditText txtReferencia,txtLocacionLat,txtLocacionLong,txtMostarHora,txtMiReferencia;
    Button btnVerDireccion,btnVerMiDireccion,btnHora,btnRealizarPedido;
    TextView txtDireccion,txtLatitud,txtLongitud,txtVerMiDireccion,txtFechaHoy;
    Spinner SpinnerNumPersonas;

    /*AwesomeValidation awesomeValidation;*/

    public PedidoCN() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pedido_cn, container, false);

        //Capturamos la Latitud y la Longitud
        HomeCN Activity = (HomeCN) getActivity();
        Latitud = Activity.getLatitud();
        Longitud = Activity.getLongitud();

        //Rucuperando DNI por SharedPreference
        RecuperaDniClienteN();

        //Parseamos LA LATITUD Y LONGITUD
        String ResulLat = Double.toString(Latitud);
        String ResulLong = Double.toString(Longitud);


        //Declaramos el array y  el Spiner y lo llenamos
        this.arraySpinner = new String[]{"1","2","3","4"};
        SpinnerNumPersonas = (Spinner)view.findViewById(R.id.spinnerNumPersonas);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),R.layout.item_numpersonas,R.id.txtItemNumPersonas,arraySpinner);
        SpinnerNumPersonas.setAdapter(adapter);

        btnVerDireccion = (Button)view.findViewById(R.id.btnVerDireccion);
        btnVerMiDireccion = (Button)view.findViewById(R.id.btnVerMiDireccion);
        btnHora = (Button)view.findViewById(R.id.btnHora);
        btnRealizarPedido = (Button)view.findViewById(R.id.btnRealizarPedido);

        txtFechaHoy = (TextView)view.findViewById(R.id.txtFechaHoy);
        txtDireccion = (TextView)view.findViewById(R.id.txtDireccion);
        txtLatitud = (TextView)view.findViewById(R.id.txtLatitud);
        txtLongitud = (TextView)view.findViewById(R.id.txtLongitud);
        txtVerMiDireccion = (TextView)view.findViewById(R.id.txtVerMiDireccion);

        txtMostarHora = (EditText)view.findViewById(R.id.txtMostarHora);
        txtLocacionLat = (EditText)view.findViewById(R.id.txtLocacionLat);
        txtLocacionLong = (EditText)view.findViewById(R.id.txtLocacionLong);
        txtReferencia = (EditText)view.findViewById(R.id.txtReferencia);
        txtMiReferencia = (EditText)view.findViewById(R.id.txtMiReferencia);


        //Para Mostrar la fecha de hoy
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Fecha = simpleDateFormat.format(calendar.getTime());
        txtFechaHoy.setText(Fecha);


        //Imprimimos La Latitud y Longitud
        txtLocacionLat.setText(ResulLat);
        txtLocacionLong.setText(ResulLong);


        btnVerDireccion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ReferenciaLatLong();
            }
        });
        btnVerMiDireccion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MiDireccionLatLong();
            }
        });
        btnHora.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                MostrarHora();
            }
        });

        btnRealizarPedido.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if("".equals(txtReferencia.getText().toString()) || "".equals(txtMiReferencia.getText().toString())
                        || "".equals(txtMostarHora.getText().toString() ))
                {
                    Toast.makeText(getActivity(),"Opps! tiene algun campo vacio",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    ProcesarPedido();
                }
            }
        });

        return view;
    }

    //Para Mostrar LAT,LONG y  DIRECCION DE UNA Referencia
    public String consumeJsonGet(String Direccion)
    {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;
        try
        {
            url = new URL ("https://maps.googleapis.com/maps/api/geocode/json?address="+Direccion);
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
            JSONObject jsonObject = new JSONObject(rspta);

            String Latitud = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                    .getJSONObject("location").get("lat").toString();

            String Longitud = ((JSONArray)jsonObject.get("results")).getJSONObject(0).getJSONObject("geometry")
                    .getJSONObject("location").get("lng").toString();

            String Direccion = ((JSONArray)jsonObject.get("results")).getJSONObject(0).get("formatted_address").toString();

            txtLatitud.setText(Latitud);
            txtLongitud.setText(Longitud);
            txtDireccion.setText(Direccion);
        }
        catch (JSONException e)
        {

        }
    }

    private void ReferenciaLatLong()
    {
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {
                final String res = consumeJsonGet(txtReferencia.getText().toString().replace(" ","%20"));

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

    //Para Mostrar Direccion de mi Lat y Long
    public String consumeJsonGetLatLong(String Lat,String Long)
    {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;
        try
        {
            url = new URL ("https://maps.googleapis.com/maps/api/geocode/json?latlng="+Lat+","+Long);
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

    public void MostrarMiUbi(String rspta)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(rspta);

            String MiDireccion = ((JSONArray)jsonObject.get("results")).getJSONObject(0).get("formatted_address").toString();

            txtVerMiDireccion.setText(MiDireccion);
        }
        catch (JSONException e)
        {

        }
    }

    private void MiDireccionLatLong()
    {
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {
                final String res = consumeJsonGetLatLong(txtLocacionLat.getText().toString(),txtLocacionLong.getText().toString());

                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        MostrarMiUbi(res);
                    }
                });
            }
        };
        tr.start();
    }

    //Para Mostrar La Hora en un dialog
    private void MostrarHora()
    {
        Hora = calendar.get(Calendar.HOUR_OF_DAY);
        Minuto = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int HoraDia, int MinutoDia) {
                txtMostarHora.setText(HoraDia+":"+MinutoDia);
            }
        },Hora,Minuto,false);
        timePickerDialog.show();
    }

    //Para Realizar el Pedido

    public String InsertaData(String RefOrigen,String DirOrigen,String LatOrigen,String LongOrigen,String NumPers,
                              String RefDestino,String DirDestino,String LatDestino,String LongDestino,String Hora,
                              String Fecha,int IdCli)
    {
        String parametros = "reforigen="+RefOrigen+"&dirorigen="+DirOrigen+"&latorigen="+LatOrigen+"&longorigen="+LongOrigen+
                "&numpers="+NumPers+"&refdestino="+RefDestino+"&dirdestino="+DirDestino+"&latdestino="+LatDestino+
                "&longdestino="+LongDestino+"&hora="+Hora+"&fecha="+Fecha+"&idcli="+IdCli;


        HttpURLConnection connection = null;
        String respuesta = "";

        try
        {
            URL url = new URL("http://katso.com.pe/rapitaxi/ServicioCliente/procesarpedidocn.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            //connection.setRequestProperty("Content-Length",""+Integer.toString(parametros.getBytes().length));

            connection.setDoInput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            wr.writeBytes(parametros);
            wr.close();

            Scanner inStream = new Scanner(connection.getInputStream());

            while (inStream.hasNextLine())
                respuesta+=(inStream.nextLine());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        return respuesta.toString();

    }

    public void MostrarRespuesta(String rspta)
    {
        try
        {
            Toast.makeText(getActivity(),rspta,Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {

        }
    }


    private void ProcesarPedido()
    {
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {
                final String res =InsertaData(
                        txtMiReferencia.getText().toString().replaceAll(" ","%20"),
                        txtVerMiDireccion.getText().toString().replaceAll(" ","%20"),
                        txtLocacionLat.getText().toString(),
                        txtLocacionLong.getText().toString(),
                        SpinnerNumPersonas.getSelectedItem().toString(),
                        txtReferencia.getText().toString().replaceAll(" ","%20"),
                        txtDireccion.getText().toString().replaceAll(" ","%20"),
                        txtLatitud.getText().toString(),
                        txtLongitud.getText().toString(),
                        txtMostarHora.getText().toString(),
                        txtFechaHoy.getText().toString().replaceAll(" ","%20"),
                        Dni);

                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        MostrarRespuesta(res);
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
