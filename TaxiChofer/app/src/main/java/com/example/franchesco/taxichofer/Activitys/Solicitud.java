package com.example.franchesco.taxichofer.Activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.franchesco.taxichofer.R;

import org.json.JSONArray;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Solicitud extends AppCompatActivity {

    int IdPedido = 0;
    int Aceptar = 1,Rechazar = 2;

    TextView txtHoraSol,txtNumPerSol,txtRefereSol,txtMensaje,txtDirSol,txtDir,txtDirOrSol;
    Button btnAceptar,btnRechazar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitud);


        //Recuperando IdPedido
        Bundle recupera = getIntent().getExtras();

        if (recupera!=null)
            IdPedido = recupera.getInt("IdPedido");

        txtRefereSol = (TextView)findViewById(R.id.txtRefereSol);
        txtHoraSol = (TextView)findViewById(R.id.txtHoraSol);
        txtNumPerSol = (TextView)findViewById(R.id.txtNumPerSol);
        txtMensaje = (TextView)findViewById(R.id.txtMensaje);
        txtDirSol = (TextView)findViewById(R.id.txtDirSol);
        txtDir = (TextView)findViewById(R.id.txtDir);
        txtDirOrSol = (TextView)findViewById(R.id.txtDirOrSol);

        btnAceptar = (Button)findViewById(R.id.btnAceptar);
        btnRechazar = (Button)findViewById(R.id.btnRechazar);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Aceptar();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        btnRechazar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Rechazar();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        ImprimirDatos();
    }



    public String consumeDniJSON(int IdPedido)
    {
        URL url = null;
        String linea = "";
        int respuesta = 0;
        StringBuilder resul = null;
        try
        {
            url = new URL ("http://katso.com.pe/rapitaxi/ServicioChofer/unicosolicitud.php?idpedido="+IdPedido);
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
                txtRefereSol.setText(json.getJSONObject(i).getString("ReferenciaDestino"));
                txtNumPerSol.setText(json.getJSONObject(i).getString("NumPersonas"));
                txtHoraSol.setText(json.getJSONObject(i).getString("Hora"));

                String EstadoPedido = json.getJSONObject(i).getString("EstadoPedido");

                if (EstadoPedido.equals("0"))
                {
                    btnAceptar.setVisibility(View.VISIBLE);
                    btnRechazar.setVisibility(View.VISIBLE);
                }
                else if (EstadoPedido.equals("1"))
                {
                    btnAceptar.setVisibility(View.INVISIBLE);
                    btnRechazar.setVisibility(View.INVISIBLE);
                    txtDir.setVisibility(View.VISIBLE);
                    txtDirSol.setText(json.getJSONObject(i).getString("DireccionDestino"));
                    txtDirOrSol.setText("("+json.getJSONObject(i).getString("ReferenciaOrigen") +")"+ " " +
                            json.getJSONObject(i).getString("DireccionOrigen"));
                }
                else if (EstadoPedido.equals("2"))
                {
                    txtMensaje.setVisibility(View.VISIBLE);
                }
            }

        }
        catch (Exception e)
        {
            e.getMessage();
        }
    }

    private void ImprimirDatos()
    {
        Thread tr = new Thread(){
            @Override
            public void run()
            {

                final String res = consumeDniJSON(IdPedido);

                runOnUiThread(new Runnable()
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

    //Estados Aceptar

    public String ActualizarAceptar(int EstadoPedido,int IdPedido)
    {
        String parametros = "estadopedido="+EstadoPedido+"&idpedido="+IdPedido;
        HttpURLConnection connection = null;
        String respuesta = "";

        try
        {
            URL url = new URL("http://katso.com.pe/rapitaxi/ServicioChofer/actualizarsolep.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length",""+Integer.toString(parametros.getBytes().length));

            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            wr.writeBytes(parametros);
            wr.close();

            Scanner inStream = new Scanner(connection.getInputStream());

            while (inStream.hasNextLine())
                respuesta+=(inStream.nextLine());
        }
        catch (Exception e)
        {

        }
        return respuesta.toString();
    }

    private void Aceptar()
    {
        Thread tr = new Thread(){
            @Override
            public void run()
            {
                ActualizarAceptar(Aceptar,IdPedido);
            }
        };
        tr.start();
    }

    //Estados Rechazar
    public String ActualizarRechazar(int EstadoPedido,int IdPedido)
    {
        String parametros = "estadopedido="+EstadoPedido+"&idpedido="+IdPedido;
        HttpURLConnection connection = null;
        String respuesta = "";

        try
        {
            URL url = new URL("http://katso.com.pe/rapitaxi/ServicioChofer/actualizarsolep.php");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Length",""+Integer.toString(parametros.getBytes().length));

            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());

            wr.writeBytes(parametros);
            wr.close();

            Scanner inStream = new Scanner(connection.getInputStream());

            while (inStream.hasNextLine())
                respuesta+=(inStream.nextLine());
        }
        catch (Exception e)
        {

        }
        return respuesta.toString();
    }

    private void Rechazar()
    {
        Thread tr = new Thread(){
            @Override
            public void run()
            {
                ActualizarRechazar(Rechazar,IdPedido);
            }
        };
        tr.start();
    }
}
