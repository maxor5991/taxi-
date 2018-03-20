package com.franchesco.taxicliente;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class Login extends AppCompatActivity {

    EditText txtCorreo,txtClave;
    Button btnIniSesion;
    CheckBox ckbClienteJuridico;
    Button btnRegistrate;

    int Dni = 0;
    long Ruc = 0;

    AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //para eliminar la barra fondo completo
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }


        DeclarandoElementos();
    }

    private void DeclarandoElementos() {
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        txtCorreo = findViewById(R.id.txtCorreo);
        txtClave = findViewById(R.id.txtClave);
        ckbClienteJuridico = findViewById(R.id.ckbClienteJuridico);
        btnIniSesion = findViewById(R.id.btnIniSesion);
        btnRegistrate = findViewById(R.id.btnRegistrate);

        awesomeValidation.addValidation(this, R.id.txtCorreo, Patterns.EMAIL_ADDRESS, R.string.correoError);
        //awesomeValidation.addValidation(this, R.id.txtClave, "{0,6}", R.string.claveError);

        btnIniSesion.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if (ckbClienteJuridico.isChecked() && awesomeValidation.validate())
                {
                    ValidarCJ();
                }
                else if (awesomeValidation.validate())
                {
                    ValidarCN();
                }

            }
        });

        btnRegistrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this,Registro.class);
                startActivity(intent);
                overridePendingTransition(R.anim.alpha_ingreso,R.anim.alpha_salida);
            }
        });
    }




    //Login Cliente Natural
    private void ValidarCN()
    {
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {

                final String res = EnviarPostCN(txtCorreo.getText().toString(),txtClave.getText().toString());

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int r = objJSONCN(res);

                        int getData = r;

                        GuardarEstado();

                        if (getData > 0)
                        {
                            Dni = getData;//Capturando DNI

                            GuardarDniClienteN();

                            Intent intent = new Intent(Login.this,HomeCN.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.alpha_ingreso,R.anim.alpha_salida);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Correo o Clave Incorrecto ",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        tr.start();
    }

    public String EnviarPostCN(String correo,String contrasena)
    {

        String parametros = "correo="+correo+"&contrasena="+contrasena;
        HttpURLConnection connection = null;
        String respuesta = "";

        try
        {
            URL url = new URL("http://katso.com.pe/rapitaxi/ServicioCliente/validacioncn.php");
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

    public int objJSONCN(String rspta)
    {
        int res = 0;

        try
        {
            JSONArray json = new JSONArray(rspta);
            for (int i = 0; i < json.length(); i++)
            {
                JSONObject c = json.getJSONObject(i);
                res = c.getInt("DNI");//Capturando json "Dni"
            }
        }
        catch (Exception e)
        {

        }
        return res;
    }



    //Login Cliente Juridico
    private void ValidarCJ()
    {
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {

                final String res = EnviarPostCJ(txtCorreo.getText().toString(),txtClave.getText().toString());

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        long r = objJSONCJ(res);

                        long getData = r;

                        GuardarEstado();

                        if (getData > 0)
                        {
                            Ruc = getData;//Capturando Ruc

                            GuardarDniClienteJ();

                            Intent intent = new Intent(Login.this,HomeCJ.class);
                            startActivity(intent);
                            finish();
                            overridePendingTransition(R.anim.alpha_ingreso,R.anim.alpha_salida);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Correo o Clave Incorrecto ",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        };
        tr.start();
    }

    public String EnviarPostCJ(String correo,String contrasena)
    {

        String parametros = "correo="+correo+"&contrasena="+contrasena;
        HttpURLConnection connection = null;
        String respuesta = "";

        try
        {
            URL url = new URL("http://katso.com.pe/rapitaxi/ServicioCliente/validacioncj.php");
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

    public long objJSONCJ(String rspta)
    {
        long res = 0;

        try
        {
            JSONArray json = new JSONArray(rspta);
            for (int i = 0; i < json.length(); i++)
            {
                JSONObject c = json.getJSONObject(i);
                res = c.getLong("RUC");//Capturando json "RUC"
            }
        }
        catch (Exception e)
        {

        }
        return res;
    }

    //Preferencias

    public void GuardarDniClienteN()
    {
        //Guardar Id del ClienteN Con SP
        SharedPreferences preferences = getSharedPreferences("ClienteN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Dni",Dni);
        editor.commit();
    }

    public void GuardarDniClienteJ()
    {
        //Guardar Id del ClienteN Con SP
        SharedPreferences preferences = getSharedPreferences("ClienteJ", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("Ruc",Ruc);
        editor.commit();
    }

    public void GuardarEstado()
    {
        //Guardar estado del Boton
        SharedPreferences preferences = getSharedPreferences("EstadoBoton", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("Estado",true).apply();
    }

    public boolean ObtenerEstado()
    {
        //Obtener estado del Boton
        SharedPreferences preferences = getSharedPreferences("EstadoBoton", Context.MODE_PRIVATE);
        return preferences.getBoolean("Estado",false);
    }

    public static void cambiarEstado(Context c,boolean b)
    {
        //Cambiar estado del Boton
        SharedPreferences preferences = c.getSharedPreferences("EstadoBoton", Context.MODE_PRIVATE);
        preferences.edit().putBoolean("Estado",b).apply();
    }

    @Override
    public void onBackPressed()
    {
    }
}
