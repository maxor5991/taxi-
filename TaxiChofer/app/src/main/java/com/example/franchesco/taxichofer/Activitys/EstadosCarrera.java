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

public class EstadosCarrera extends AppCompatActivity {

    int IdPedido = 0;
    int Inicio = 1;
    int Fin = 2;
    TextView txtRefereEs,txtHoraEs,txtNumPersoEs,txtMensaje,txtDirSol,txtDir,txtDirOrSol;
    Button btnIniCarrera,btnFinCarrera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estados_carrera);

        //Recuperando IdPedido
        Bundle recupera = getIntent().getExtras();

        if (recupera!=null)
            IdPedido = recupera.getInt("IdPedido");

        txtRefereEs = (TextView)findViewById(R.id.txtRefereEs);
        txtHoraEs = (TextView)findViewById(R.id.txtHoraEs);
        txtNumPersoEs = (TextView)findViewById(R.id.txtNumPersoEs);
        txtMensaje = (TextView)findViewById(R.id.txtMensaje);
        txtDirSol = (TextView)findViewById(R.id.txtDirSol);
        txtDir = (TextView)findViewById(R.id.txtDir);
        txtDirOrSol = (TextView)findViewById(R.id.txtDirOrSol);

        btnIniCarrera = (Button)findViewById(R.id.btnIniCarrera);
        btnFinCarrera = (Button)findViewById(R.id.btnFinCarrera);

        btnIniCarrera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Iniciar();
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
            }
        });

        btnFinCarrera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Finalizar();
                finish();
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
                txtRefereEs.setText(json.getJSONObject(i).getString("ReferenciaDestino"));
                txtNumPersoEs.setText(json.getJSONObject(i).getString("NumPersonas"));
                txtHoraEs.setText(json.getJSONObject(i).getString("Hora"));

                int EstadoCarrera = Integer.parseInt(json.getJSONObject(i).getString("EstadoCarrera"));


                if (EstadoCarrera == 0)
                {
                    txtDir.setVisibility(View.VISIBLE);
                    txtDirSol.setText(json.getJSONObject(i).getString("DireccionDestino"));
                    txtDirOrSol.setText("("+json.getJSONObject(i).getString("ReferenciaOrigen") +")"+ " " +
                            json.getJSONObject(i).getString("DireccionOrigen"));
                    btnIniCarrera.setVisibility(View.VISIBLE);
                }
                else if (EstadoCarrera == 1)
                {
                    txtDir.setVisibility(View.VISIBLE);
                    txtDirSol.setText(json.getJSONObject(i).getString("DireccionDestino"));
                    txtDirOrSol.setText("("+json.getJSONObject(i).getString("ReferenciaOrigen") +")"+ " " +
                            json.getJSONObject(i).getString("DireccionOrigen"));
                    btnFinCarrera.setVisibility(View.VISIBLE);
                }
                else if (EstadoCarrera == 2)
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


    //Actualizar Estado Carrera

    //Iniciar Carrera
    public String ActualizarIniciar(int EstadoCarrera,int IdPedido)
    {
        String parametros = "estadocarrera="+EstadoCarrera+"&idpedido="+IdPedido;
        HttpURLConnection connection = null;
        String respuesta = "";

        try
        {
            URL url = new URL("http://katso.com.pe/rapitaxi/ServicioChofer/actualizarsolec.php");
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

    private void Iniciar()
    {
        Thread tr = new Thread(){
            @Override
            public void run()
            {
                ActualizarIniciar(Inicio,IdPedido);
            }
        };
        tr.start();
    }

    //Fin Carrera
    public String ActualizarFinalizar(int EstadoCarrera,int IdPedido)
    {
        String parametros = "estadocarrera="+EstadoCarrera+"&idpedido="+IdPedido;
        HttpURLConnection connection = null;
        String respuesta = "";

        try
        {
            URL url = new URL("http://katso.com.pe/rapitaxi/ServicioChofer/actualizarsolec.php");
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

    private void Finalizar()
    {
        Thread tr = new Thread(){
            @Override
            public void run()
            {
                ActualizarFinalizar(Fin,IdPedido);
            }
        };
        tr.start();
    }


}
