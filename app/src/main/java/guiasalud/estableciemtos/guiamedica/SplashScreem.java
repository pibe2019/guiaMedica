package guiasalud.estableciemtos.guiamedica;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreem extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    verificacionInternet aInternt;
    //Context cont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screem);
        //cont=getApplication();
        /*if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }*/
        try {
            if (android.os.Build.VERSION.SDK_INT != Build.VERSION_CODES.O){
                if (this.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
                    this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        aInternt=new verificacionInternet(this);
        //ANIMACIONES
        Animation animation1= AnimationUtils.loadAnimation(this,R.anim.desplazamiento_arriba);//android.view.aniamtion
        Animation animation2=AnimationUtils.loadAnimation(this, R.anim.desplazamiento_abajo);
        ImageView logoImagen=findViewById(R.id.imageViewDoctor);
        TextView Guia=findViewById(R.id.textGuia);
        //TextView de=findViewById(R.id.textGuia);
        TextView salud=findViewById(R.id.textMedica);
        logoImagen.setAnimation(animation1);
        //de.setAnimation(animation2);
        Guia.setAnimation(animation2);
        salud.setAnimation(animation2);
        //permiso();
        activarMyLocation();

    }
    private void splashScreen(){
        //android.os
        new Handler().postDelayed(() -> {
            if (aInternt.HayInternet()){
                Intent inte= new Intent(SplashScreem.this, MainActivity.class);
                startActivity(inte);
                finish();
            }else{
                mostrarDialog();
            }
        },3000);
    }

    private void mostrarDialog() {//dialogo basico
        new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_general)
                .setCancelable(false)
                .setMessage(R.string.dialog_splashScreem)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> finish()).show();
    }

    private void activarMyLocation() {
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //mMap.setMyLocationEnabled(true);
            //mMap.getUiSettings().setMyLocationButtonEnabled(true);//q se vea el boton de mi ubicacion
            //mMap.setOnMyLocationButtonClickListener(MapsFragment.this);//tocas el boton de mi ubicacion
            splashScreen();
        } else {
            avisoPermiss(this);
            //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
              //      LOCATION_PERMISSION_REQUEST_CODE);
        }
        //mMap.setMyLocationEnabled(true);//FALTA UBICAR ESTA LINEA DE CODIGO
        //Toast.makeText(getContext(),"no se corta..",Toast.LENGTH_LONG).show();
        //mMap.getUiSettings().setMyLocationButtonEnabled(true);//q se vea el boton de mi ubicacion
        //mMap.setOnMyLocationButtonClickListener(MapsFragment.this);//tocas el boton de mi ubicacion
    }

    @SuppressLint("SetTextI18n")
    private void avisoPermiss(Context c){
        AlertDialog.Builder builder=new AlertDialog.Builder(c);
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog,null);
        //m_llMain=view.findViewById(R.id.cadllMain);
        //m_llMain.setBackgroundResource(R.drawable.dialogredondeado);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
        TextView txt=view.findViewById(R.id.text_dialog);
        txt.setTextSize(14);
        txt.setText(R.string.dialog_splashScreem_permiso_ubicacion);
        Button btn=view.findViewById(R.id.btn_dialogo_ok);
        btn.setOnClickListener(v -> {
            //requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
            //mMap.setMyLocationEnabled(true);
            //Toast.makeText(getActivity(),"los servicios de esta app necesitan de Internet ",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
            //runOnUiThread(() -> {
                ActivityCompat.requestPermissions(SplashScreem.this, new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
                ultimoPermiss(SplashScreem.this);
            //});
            //VOLVER A HACER LA CONDICION DEL PERMISO
            /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                splashScreen();
            } else {
                ActivityCompat.requestPermissions(this, new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
                splashScreen();
                //ultimoPermiss(this);
                //mostrarDialogPersonalizadoPermiss(this);
                //poner aviso de la respuesta si es no: no tendraacceso a la ruta y splashscren
                //si es si: splas scremm
            }*/
        });
    }

    @SuppressLint("SetTextI18n")
    private void ultimoPermiss(Context c){
        AlertDialog.Builder builder=new AlertDialog.Builder(c);
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog,null);
        //m_llMain=view.findViewById(R.id.cadllMain);
        //m_llMain.setBackgroundResource(R.drawable.dialogredondeado);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        dialog.setCancelable(false);
        TextView txt=view.findViewById(R.id.text_dialog);
        txt.setTextSize(14);
        txt.setText("Entrar...");
        Button btn=view.findViewById(R.id.btn_dialogo_ok);
        btn.setOnClickListener(v -> {
            dialog.dismiss();
            splashScreen();
            //VOLVER A HACER LA CONDICION DEL PERMISO
            /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                splashScreen();
            } else {
               ActivityCompat.requestPermissions(this, new String[]
                                {Manifest.permission.ACCESS_FINE_LOCATION},
                        LOCATION_PERMISSION_REQUEST_CODE);
                splashScreen();
            }*/
        });
    }
/*
    public void permiso(){
        //modificar x q si no se le da el permiso, aparece el mapa vacio
        //intentar cambiar este permiso al inten donde se ejecuta el SplashScreem
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
            //requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }
    */

}