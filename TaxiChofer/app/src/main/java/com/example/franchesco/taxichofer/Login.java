package com.example.franchesco.taxichofer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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

    int Dni = 0;

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

        //Si el estado es falso no aparecera el login
        if (ObtenerEstado())
        {
            Intent intent = new Intent(Login.this,Home.class);
            startActivity(intent);
            finish();
        }

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        txtCorreo = (EditText)findViewById(R.id.txtCorreo);
        txtClave = (EditText)findViewById(R.id.txtClave);
        btnIniSesion = (Button)findViewById(R.id.btnIniSesion);

        awesomeValidation.addValidation(this, R.id.txtCorreo, Patterns.EMAIL_ADDRESS, R.string.correoError);
        //awesomeValidation.addValidation(this, R.id.txtClave, "^(?=.*[a-zA-Z\\d].*)[a-zA-Z\\d!@#$%&-_*]{6,6}$", R.string.claveError);

        btnIniSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awesomeValidation.validate()) {
                    Validar();
                }
            }
        });
    }

    @Override
    public void onBackPressed()
    {
    }

    //Login Chofer

    private void Validar()
    {
        Thread tr = new Thread()
        {
            @Override
            public void run()
            {

                final String res = EnviarPost(txtCorreo.getText().toString(),txtClave.getText().toString());

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        int r = objJSON(res);

                        int getData = r;

                        GuardarEstado();

                        if (getData > 0)
                        {
                            Dni = getData;//Capturando DNI

                            GuardarDniChofer();

                            Intent intent = new Intent(Login.this,Home.class);
                            startActivity(intent);
                            finish();//para destruir la aplicacion
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

    public String EnviarPost(String correo,String contrasena)
    {

        String parametros = "correo="+correo+"&contrasena="+contrasena;
        HttpURLConnection connection = null;
        String respuesta = "";

        try
        {
            URL url = new URL("http://katso.com.pe/rapitaxi/ServicioChofer/validacionch.php");
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

    public int objJSON(String rspta)
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

    //Preferencias

    public void GuardarDniChofer()
    {
        //Guardar Id del Chofer Con SP
        SharedPreferences preferences = getSharedPreferences("Chofer", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("Dni",Dni);
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
}
