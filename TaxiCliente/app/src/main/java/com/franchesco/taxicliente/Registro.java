package com.franchesco.taxicliente;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Registro extends AppCompatActivity {

    EditText txtDniR,txtCorreoR,txtContrasenaR;
    Button btnRegistraseR;

    int Dni = 0;

    AwesomeValidation awesomeValidation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //para eliminar la barra fondo completo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        txtDniR = (EditText)findViewById(R.id.txtDniR);
        txtCorreoR = (EditText)findViewById(R.id.txtCorreoR);
        txtContrasenaR = (EditText)findViewById(R.id.txtContrasenaR);

        btnRegistraseR = (Button)findViewById(R.id.btnRegistraseR);

        awesomeValidation.addValidation(this, R.id.txtDniR, "^[0-9]{8}$", R.string.dniR);
        awesomeValidation.addValidation(this, R.id.txtCorreoR, Patterns.EMAIL_ADDRESS, R.string.correoR);
        awesomeValidation.addValidation(this, R.id.txtContrasenaR, "^(?=.*[a-zA-Z\\d].*)[a-zA-Z\\d!@#$%&-_*]{6,6}$", R.string.claveR);

        btnRegistraseR.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (awesomeValidation.validate())
                {
                    RegistrarUsuario();
                }
            }
        });

    }

    public String EnviarPostRCN(String Dni,String correo,String contrasena)
    {

        String parametros = "dni="+Dni+"&correo="+correo+"&contrasena="+contrasena;
        HttpURLConnection connection = null;
        String respuesta = "";

        try
        {
            URL url = new URL("http://katso.com.pe/rapitaxi/ServicioCliente/registrocn.php");
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

    private void RegistrarUsuario()
    {
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {
                final String res = EnviarPostRCN(txtDniR.getText().toString(),txtCorreoR.getText().toString(),txtContrasenaR.getText().toString());


                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {

                        String rspta = "Exitoso";

                        if (rspta.equals(res))
                        {
                            Intent intent = new Intent(Registro.this,HomeCN.class);
                            intent.putExtra("Dni",Integer.parseInt(txtDniR.getText().toString()));//Enviando Dni
                            startActivity(intent);
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
