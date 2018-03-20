package com.franchesco.taxicliente.Fragments.Natural;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.franchesco.taxicliente.R;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.PhotoLoader;

import java.util.HashMap;
import java.util.Map;

public class ImgPerfilCN extends AppCompatActivity {

    int Dni = 0;
    static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageView;
    private Button btnSubirImagen, btnSelecImagen;
    String rutaArchivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_perfil_cn);

        //Rucuperando DNI por SharedPreference
        RecuperaDniClienteN();


        imageView = (ImageView) findViewById(R.id.imageView);
        btnSubirImagen = (Button) findViewById(R.id.btnSubirImagen);
        btnSelecImagen = (Button) findViewById(R.id.btnSelecImagen);

        btnSelecImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeleccionarImagen();
            }
        });

        btnSubirImagen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (rutaArchivo != null)
                {
                    SubirImagen(rutaArchivo,Dni);
                    //Intent intent = new Intent(ImgPerfilCN.this,HomeCN.class);
                    //intent.putExtra("Dni",Dni);
                    //startActivity(intent);
                    ImgPerfilCN.this.finish();
                    overridePendingTransition(R.anim.alpha_ingreso,R.anim.alpha_salida);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Imagen no seleccionada!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void SeleccionarImagen()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if(requestCode == PICK_IMAGE_REQUEST){
                Uri imgUri = data.getData();

                rutaArchivo = getRuta(imgUri);

                Log.d("imgUri", imgUri.toString());
                Log.d("rutaArchivo", rutaArchivo);

                imageView.setImageURI(imgUri);

            }

        }

    }

    private String getRuta(Uri contentUri)
    {
        String[] proj = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(getApplicationContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }

    private void SubirImagen(final String imagenRuta, final int Dni)
    {

        try
        {
            Bitmap bitmap = PhotoLoader.init().from(imagenRuta).requestSize(112, 112).getBitmap();
            final String encodedString = ImageBase64.encode(bitmap);

            StringRequest smr = new StringRequest(Request.Method.POST,
                    "http://katso.com.pe/rapitaxi/ServicioCliente/subirimagencn.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("image", encodedString);
                    params.put("dni", String.valueOf(Dni));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(ImgPerfilCN.this);
            requestQueue.add(smr);
        }
        catch (Exception e)
        {}

    }

    //Preferencias
    private void RecuperaDniClienteN()
    {
        SharedPreferences preferences = getSharedPreferences("ClienteN", Context.MODE_PRIVATE);
        Dni = preferences.getInt("Dni",0);
    }
}
