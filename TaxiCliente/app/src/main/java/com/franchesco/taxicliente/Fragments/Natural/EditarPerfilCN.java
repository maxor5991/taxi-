package com.franchesco.taxicliente.Fragments.Natural;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.franchesco.taxicliente.R;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class EditarPerfilCN extends AppCompatActivity {

    int Dni = 0;
    String Celular,Correo,Nombre,Apellido;
    EditText txtEdiDni,txtEdiCel,txtEdiCorreo,txtEdiNom,txtEdiApe;
    Button btnEdiGuardar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_cn);

        //Recuperando Dni
        Bundle recupera = getIntent().getExtras();

        if (recupera!=null)
            Dni = recupera.getInt("Dni");
            Celular = recupera.getString("Celular");
            Correo = recupera.getString("Correo");
            Nombre = recupera.getString("Nombre");
            Apellido = recupera.getString("Apellido");

        txtEdiDni = (EditText)findViewById(R.id.txtEdiDni);
        txtEdiCel = (EditText)findViewById(R.id.txtEdiCel);
        txtEdiNom = (EditText)findViewById(R.id.txtEdiNom);
        txtEdiApe = (EditText)findViewById(R.id.txtEdiApe);
        txtEdiCorreo = (EditText)findViewById(R.id.txtEdiCorreo);


        txtEdiDni.setText(Integer.toString(Dni));
        txtEdiCel.setText(Celular);
        txtEdiNom.setText(Nombre);
        txtEdiApe.setText(Apellido);
        txtEdiCorreo.setText(Correo);

        btnEdiGuardar = (Button)findViewById(R.id.btnEdiGuardar);

        btnEdiGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditarPerfil();
            }
        });

    }

    public String EnviarPostECN(int Dni,String correo,String nombre,String apellido,int celular)
    {

        String parametros = "dni="+Dni+"&correo="+correo+"&nombre="+nombre+"&apellido="+apellido+"&celular="+celular;
        HttpURLConnection connection = null;
        String respuesta = "";

        try
        {
            URL url = new URL("http://katso.com.pe/rapitaxi/ServicioCliente/actualizarcn.php");
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

    public void MostrarRespuesta(String rspta)
    {
        try
        {
            Toast.makeText(getApplicationContext(),rspta,Toast.LENGTH_SHORT).show();
        }
        catch (Exception e)
        {

        }
    }

    private void EditarPerfil()
    {
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {
                final String res = EnviarPostECN(Dni,txtEdiCorreo.getText().toString(),txtEdiNom.getText().toString(),
                        txtEdiApe.getText().toString(),Integer.parseInt(txtEdiCel.getText().toString()));


                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        String rspta = "Actualizados";

                        if (rspta.equals(res))
                        {

                            Toast.makeText(getApplication(),"Datos Actualizados",Toast.LENGTH_LONG).show();
                            //Intent intent = new Intent(EditarPerfilCN.this,HomeCN.class);
                            //intent.putExtra("Dni",Dni);//Enviando Dni
                            //startActivity(intent);
                            EditarPerfilCN.this.finish();
                            overridePendingTransition(R.anim.alpha_ingreso,R.anim.alpha_salida);
                        }
                        else
                        {
                            MostrarRespuesta(res);
                        }
                    }
                });
            }
        };
        tr.start();
    }

}
