package guiasalud.estableciemtos.guiamedica.ui.maps;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import guiasalud.estableciemtos.guiamedica.R;
import guiasalud.estableciemtos.guiamedica.dbSqlite.ServicesUsuario;
import guiasalud.estableciemtos.guiamedica.dbSqlite.Usuario;
import guiasalud.estableciemtos.guiamedica.fragment.establecimientoFragment;
import guiasalud.estableciemtos.guiamedica.modelsEntities.especialidadMedica;
import guiasalud.estableciemtos.guiamedica.modelsEntities.establecimientoMapa;
import guiasalud.estableciemtos.guiamedica.services.IEspecialidadesMedicas;
import guiasalud.estableciemtos.guiamedica.services.IRecepcionEstablecimientoCcordenads;
import guiasalud.estableciemtos.guiamedica.services.ServicioEspecialidadesMedicas;
import guiasalud.estableciemtos.guiamedica.services.ServicioEstablecimientoCcordenads;
import guiasalud.estableciemtos.guiamedica.verificacionInternet;

public class MapsFragment extends Fragment implements IRecepcionEstablecimientoCcordenads,
        IEspecialidadesMedicas, GoogleMap.OnMarkerClickListener, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnInfoWindowCloseListener, GoogleMap.OnMyLocationButtonClickListener/*,
        AdapterView.OnItemSelectedListener*/ {
    NavController navController;
    Fragment estabecimientoFragmenttt;
    private verificacionInternet aInternt;
    private Usuario usuario = new Usuario();
    private boolean pressBotonMiHubication = false;
    private WeakReference<Context> contex;

    //private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    ServicioEstablecimientoCcordenads sec = new ServicioEstablecimientoCcordenads();
    ServicioEspecialidadesMedicas seem = new ServicioEspecialidadesMedicas();
    //
    private Spinner especialiddmedica;
    private static ArrayList<especialidadMedica> listaEspecialdades = new ArrayList<>();
    ArrayList<String> listaStringEspecialidades;
    //
    static ArrayList<establecimientoMapa> li;
    private GoogleMap mMap;
    ArrayList<Marker> lista_marker;//almaceno los marcadores resultantes detodo los establecimientos traidos de la BD
    //
    Marker markerGlobal = null;
    //private int posicionGuardada=-2;
    private double longitudeEstable = 0, latitudeEstable = 0;
    //progres bar
    private ProgressDialog progressDialogGlobal;

    //////////////////////
    //private static final int PETICION_PERMISO_LOCATION = 101;
    String mensaje1;
    double lat = 0.0;
    double lng = 0.0;
    private Marker marcador;
    //
    protected View mView = null;
    //
    //private boolean tocoMarket = false;
    LocationManager locationManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.contex = new WeakReference<>(getContext());
        estabecimientoFragmenttt = new establecimientoFragment();
        aInternt = new verificacionInternet(this.getActivity());
    }

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(@NotNull GoogleMap googleMap) {
            mMap = googleMap;
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            LatLng puntoZonn = new LatLng(-8.105843, -79.035873);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puntoZonn, 12));
            mMap.setMaxZoomPreference(19.3f);
            //mMap.setBuildingsEnabled(true); abilita capa de edificio en 3d
            //UiSettings uiSettings = googleMap.getUiSettings();//abilitamos la opcion de boton en el mapa de zonn
            //uiSettings.setZoomControlsEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(false);//abilitamos u  deshabilitala opcion de boton en el mapa de zonn
            //googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            //uiSettings.setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(false);//barra de herramientas del mapa
            //googleMap.setOnMyLocationButtonClickListener(MapsFragment.this::onMyLocationButtonClick);
            enableMyLocation2();
            selectSpinner(googleMap);
        }

    };

    private void enableMyLocation2() {
        if (ContextCompat.checkSelfPermission(contex.get(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contex.get(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);//q se vea el boton de mi ubicacion
            mMap.setOnMyLocationButtonClickListener(MapsFragment.this);//tocas el boton de mi ubicacion
        } /*else {
            ActivityCompat.requestPermissions((Activity) contex.get(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            mostrarDialogPersonalizadoPermiss(contex.get());
        }*/

    }

    public void selectSpinner(GoogleMap googleMap) {
        especialiddmedica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int posicioReal = position - 1;
                mMap.clear();//elimina todo los marcadores y limpia el mapa
                sec.borrarPolilyne();
                markerGlobal = null;
                latitudeEstable = 0.0;
                longitudeEstable = 0.0;
                if (aInternt.HayInternet()) {
                    if (position != 0) {
                        progressDialogGlobal = new ProgressDialog(getContext());
                        progressDialogGlobal.setMessage(getString(R.string.cargar));
                        progressDialogGlobal.show();
                        int codEspecialidad;
                        codEspecialidad = listaEspecialdades.get(posicioReal).getCodigoEspecialidad();
                        new Thread(() -> {
                            //Aquí ejecutamos nuestras tareas costosas
                            sec.establecimientosPorEspecialidad(getContext(), codEspecialidad, MapsFragment.this, googleMap);
                        }).start();

                    } else /*if (position==0)*/ {
                        //markertodos=mMap.addMarker(new MarkerOptions().position(trujillo3).title("Ciudad de Trujillo2").snippet("cerca a la laza de armas").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
                        //if (posicionGuardada==-1) posicionGuardada=-2;
                        new Thread(() -> {
                            //Aquí ejecutamos nuestras tareas costosas
                            sec.coordenadas(contex.get(), MapsFragment.this, googleMap);
                        }).start();
                    }
                } else {
                    mostrarDialogPersonalizado(contex.get());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //Toast.makeText(getContext(),"onNothuSelected:"+parent,Toast.LENGTH_LONG).show();
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        this.mView = rootView;
        miUbicacion(false);
        especialiddmedica = rootView.findViewById(R.id.spseleccionar);
        verificacionInternet aInternt = new verificacionInternet(getActivity());
        if (aInternt.HayInternet()) {
            progressDialogGlobal = new ProgressDialog(getContext());
            progressDialogGlobal.setMessage(getString(R.string.cargar));
            progressDialogGlobal.show();
            //hilos
            //new Handler().postDelayed(() -> progressDialogGlobal.dismiss(),5000);
            seem.traerEspecialidadesMedicas(contex.get(), this);
        } else {
            mostrarDialogPersonalizado(contex.get());
            //Toast.makeText(this,"no puede acceder a este cervicio,sin Internet",Toast.LENGTH_LONG).show();
            /*if(!aInternt.verifiConexion()){
                Log.i("no hay INTERNET","");//provaraaa
                Toast.makeText(contex.get(),"sin internet",Toast.LENGTH_LONG).show();
            }else Log.i("SI hay INTERNET","");//provaraaa*/
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map1);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        navController = Navigation.findNavController(view);
        ServicesUsuario servicesUsuario = new ServicesUsuario(contex.get());
        usuario = servicesUsuario.mostrarUsuario();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //Log.i("onDestroyxtocoMarket: ", ""+ tocoMarket);
    }

    @Override
    public void onResume() {
        super.onResume();
        //sec.borrarPolilyne();
        //Log.i("onResumen markert globl",""+markerGlobal);//provaraaa
        miUbicacion(false);

        //SharedPreferences prefs = contex.get().getSharedPreferences("shared_preferenc",   Context.MODE_PRIVATE);
        //int posicionGuarda=prefs.getInt("posicion",-1);
        //prefs.edit().clear().apply();
        //if (posicionGuarda!=-1) posicionGuardada=posicionGuarda;

        //Log.i("onResumen posicions",""+posicionGuardada);//provaraaa
        //cuando el fragment puede verse en la actividad
        //tocoMarket = true;
        //Log.i("onResumentxtocoMarket: ", ""+ tocoMarket);
        if (getActivity() != null)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public void onPause() {
        super.onPause();
        //tocoMarket = false;
        //Log.i("onPausextocoMarket: ", ""+ tocoMarket);
        try {
            detenerServiceLocation();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //si el fragment es removido o reemplazado
    }

    @Override
    public void exitoCorde(ArrayList<establecimientoMapa> x, GoogleMap googleMap) {
        //liTodos=x;
        if (x != null) {
            li = x;
            //Toast.makeText(getContext(),"dato del resultado:"+liTodos.get(0).getResultado(),Toast.LENGTH_LONG).show();
            if (li.get(0).getCodigoEst() > 0) addMarkersToMap(googleMap);
            //addMarkersToMap(googleMap);
        } else
            Toast.makeText(contex.get(), R.string.error_alcargar_establecimientos, Toast.LENGTH_SHORT).show();
        //else{
        //sec.coordenadas(contex.get(),MapsFragment.this,googleMap);
        //}
    }

    @Override
    public void exitoImagen(ArrayList<establecimientoMapa> x) {

    }

    @Override
    public void errorCorde(String x) {
        if (x.equals("0"))
            Toast.makeText(contex.get(), R.string.null_establecimientos, Toast.LENGTH_SHORT).show();

    }

    @SuppressLint("PotentialBehaviorOverride")
    private void addMarkersToMap(GoogleMap googleMap) {
        lista_marker = new ArrayList<>();
        //runOnUiThread -->es para activity
        //Thread es para fragment tambien pero no permite modificar la interfaz ni pasar parametros con el hilo principal
        //Handler()--> perfecto para fragmento y permite interactuar con variables del hilo principal mediante el" .pos() "
        /*getActivity().runOnUiThread(() -> {
        });
        new Thread(() -> {
        }).start();*/
        new Handler().post(() -> {
            for (int i = 0; i < li.size(); i++) {
                //LatLng sydney = new LatLng(-8.105843, -79.035873);
                LatLng estabSald = new LatLng(li.get(i).getLatitudEst(), li.get(i).getLongitudEst());
                Marker actual;
                if (li.get(i).getTipoEst().equals("publico")) {
                    actual = googleMap.addMarker(new MarkerOptions().position(estabSald).title(li.get(i).getNombreEst()).snippet(li.get(i).getTipoEst()).icon(BitmapDescriptorFactory.fromResource(R.drawable.azulmarcador52)).rotation(6));
                } else {
                    actual = googleMap.addMarker(new MarkerOptions().position(estabSald).title(li.get(i).getNombreEst()).snippet(li.get(i).getTipoEst()).icon(BitmapDescriptorFactory.fromResource(R.drawable.turquesamarcador52)).alpha(1));
                }
                lista_marker.add(i, actual);
                /*  if (i==0) markerEstab = googleMap.addMarker(new MarkerOptions().position(estabSald).title(li.get(i).getNombreEst()).snippet(li.get(i).getTipoEst()));*/
            }
            progressDialogGlobal.dismiss();
        });

        //FALTA VALIDAR QUE MI UBICACION STE ACTIVADA.<-- ya esta
        googleMap.setOnMarkerClickListener(marker -> {
            markerGlobal = marker;
            int posicion = lista_marker.indexOf(markerGlobal);
            try {
                latitudeEstable = li.get(posicion).getLatitudEst();//del establecimiento
                longitudeEstable = li.get(posicion).getLongitudEst();//del establecimiento
                if (lat != 0 && lng != 0) {
                    //tocoMarket = true;
                    sec.ruta(contex.get(), googleMap, lat, lng, latitudeEstable, longitudeEstable);
                    //tutaMarcada=true;
                } else
                    Toast.makeText(contex.get(), R.string.aviso_de_ubicación, Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //Toast.makeText(getContext(), "Seleccione otro punto.", Toast.LENGTH_SHORT).show();
            }
            return false;
        });
        //evento cuando tocas el marker
        //googleMap.setOnMarkerClickListener(this);
        //evento cuando tocas la ventana de informacion
        googleMap.setOnInfoWindowClickListener(this);
        //googleMap.setOnInfoWindowClickListener(this);//cierra la ventana de informacion
    }

    @Override
    public void onInfoWindowClick(@NonNull @NotNull Marker marker) {
        int posicion = lista_marker.indexOf(marker);
        /*SharedPreferences prefs = contex.get().getSharedPreferences("shared_preferenc",   Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("posicion", posicion);
        editor.apply();*/
        markerGlobal = null;
        try {
            Bundle bundle = new Bundle();
            bundle.putString("nombre", li.get(posicion).getNombreEst());
            bundle.putString("tipo", li.get(posicion).getTipoEst());
            bundle.putString("direccion", li.get(posicion).getDireccionEst());
            bundle.putDouble("longitud", li.get(posicion).getLongitudEst());
            bundle.putDouble("latitud", li.get(posicion).getLatitudEst());
            bundle.putString("paginaWeb", li.get(posicion).getAginawebEst());
            bundle.putString("facebook", li.get(posicion).getFacebookEst());
            bundle.putString("numeroAtencion", li.get(posicion).getNumeroAtencionEst());
            bundle.putString("whatssap", li.get(posicion).getWhatssapEst());
            bundle.putString("correo", li.get(posicion).getCorreoInstitucionalEst());
            bundle.putString("numeroEmergencia", li.get(posicion).getNumeroEmergenciaEst());
            bundle.putString("licencia", li.get(posicion).getLicenciaFuncionamientoEst());
            bundle.putString("descripcion", li.get(posicion).getDescripcionServiciosEst());
            bundle.putInt("codigoEstablecimiento", li.get(posicion).getCodigoEst());
            //bundle.putParcelable("imagen",li.get(posicion).getImagen());
            //bundle.putString("tipoImagen",li.get(posicion).getTipo());
            Navigation.findNavController(mView).navigate(R.id.establecimientoFragment2, bundle);
        } catch (Exception ignored) {
        }
    }


    @Override
    public boolean onMarkerClick(@NonNull @NotNull Marker marker) {
        return false;
    }

    @Override
    public void onInfoWindowClose(@NonNull @NotNull Marker marker) {
    }

    @Override
    public boolean onMyLocationButtonClick() {
        // " Se hizo clic en el botón Mi ubicación "
        pressBotonMiHubication = true;
        miUbicacion(true);
        return false;
    }

    private void miUbicacion(boolean pb) {
        locationManager = (LocationManager) contex.get().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(contex.get(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contex.get(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager = (LocationManager) contex.get().getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            //Log.i("entro a principal else","");//provaraaa
            // Log.i("entro a if. para actu u","");//provaraaa
            ActualizarUbicacion(location,pb);
            new Handler().post(() -> {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);//tiempo q demora en actualizar la posicion
            });
        }
            /* if (ActivityCompat.checkSelfPermission(contex.get(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(contex.get(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) contex.get(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            PETICION_PERMISO_LOCATION);
                    //return;
                    //Log.i("entro a IF","");
                }
                else {
                    locationManager = (LocationManager) contex.get().getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    //Log.i("entro a principal else","");//provaraaa
                       // Log.i("entro a if. para actu u","");//provaraaa
                    ActualizarUbicacion(location,pb);
                    new Handler().post(() -> {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);//tiempo q demora en actualizar la posicion
                    });

                }*/
    }

    private void  detenerServiceLocation(){
        locationManager.removeUpdates(locationListener);
    }

    private void ActualizarUbicacion(Location location,boolean pb) {
        try {
            if (location != null) {
                if (location.getLatitude() !=0.0 && location.getLongitude() !=0.0) {
                    lat = location.getLatitude();
                    lng = location.getLongitude();
                    agregarMarcador(lat, lng, pb);
                }
            }
        }catch (Exception e){
            //Toast.makeText(contex,"error gps->: "+e.toString(),Toast.LENGTH_LONG).show();
        }

    }
    // agregar marcador de mi ubicacion en el mapa
    private void agregarMarcador(double lat, double lng, boolean pressButon) {
        //usuario;
        LatLng coordenadas = new LatLng(lat, lng);
        if (marcador != null) marcador.remove();
        if (usuario!=null){
            String alias=usuario.getAlias().trim();
            marcador = mMap.addMarker(new MarkerOptions()
                    .position(coordenadas)
                    .title(" "+alias+" ")//cuando tocas el icino aparecera
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.caminando30)));//icono x defecto
        }else {
            marcador = mMap.addMarker(new MarkerOptions()
                    .position(coordenadas)
                    .title(contex.get().getString(R.string.dato_yo))//cuando tocas el icino aparecera
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.caminando30)));//icono x defecto
        }
        if (pressButon){
            pressBotonMiHubication=false;
            CameraUpdate MiUbicacion = CameraUpdateFactory.newLatLng(coordenadas);//MAYOR CANTIDAD MAS CERCA ZONN DEL MAPA
            mMap.animateCamera(MiUbicacion);
        }
    }
    //control del mapa
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //entra cuando la ubicacion a cambiado
            //int posicion;
            ActualizarUbicacion(location,false);
            if(markerGlobal!=null){
                Log.i("marker global dentro",""+markerGlobal);//provaraaa
               // Log.i("estro markertGlobal","");//provaraaa
               // posicionGuardada=-2;
                //posicion=lista_marker.indexOf(markerGlobal);
                try {
                    pintar(latitudeEstable,longitudeEstable, location);
                }catch (Exception ignored){
                }
            }
            /*else if (posicionGuardada>=-1){
                Log.i("posicion guardada >=-1",""+posicionGuardada);//provaraaa
                try {
                    posicion=posicionGuardada;
                    if(li!=null){
                        Double latitud=li.get(posicion).getLatitudEst();
                        Double longitud=li.get(posicion).getLongitudEst();
                        pintar(latitud,longitud, location);
                    }

                }catch (Exception ignored){
                }
            }*/

        }
        private void pintar(double latitudd, double longitudd, Location location){

            try {
                if (location.getLatitude()!=0 && location.getLongitude()!=0){
                    Log.i("dato locatimarket dentr",""+location.getLatitude());//provaraaa
                    pressBotonMiHubication=false;
                    //if (tocoMarket){
                    //new Thread(() -> sec.ruta(contex.get(),mMap,location.getLatitude(),location.getLongitude(),latitud,longitud)).start();
                    sec.ruta(contex.get(),mMap,location.getLatitude(),location.getLongitude(),latitudd,longitudd);
                    //tutaMarcada=true;
                    //}
                }
            }catch (Exception e){
                Log.i("exepxion-->",""+e);//provaraaa
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            //quedo obsoleto en el nivel de api 29

        }

        @Override
        public void onProviderEnabled(String provider) {
            pressBotonMiHubication=false;
            mensaje1 = contex.get().getString(R.string.aviso_toas_activado);
            //gps activado
            Mensaje();
        }

        @Override
        public void onProviderDisabled(String provider) {
            mensaje1 = contex.get().getString(R.string.aviso_toas_desactivado);
            //se ejecuta cuando el gps sta desactivado
            if (pressBotonMiHubication) locationStart();
            Mensaje();
            lat=0.0;
            lng=0.0;
        }

    };
    public void Mensaje(){
        Toast toast = Toast.makeText(contex.get(),mensaje1,Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
        toast.show();
    }

    private void locationStart() {//activar los servicion cuando stan apagados
        try {
            if (contex.get() !=null){
                LocationManager mlocManag = (LocationManager) contex.get().getSystemService(Context.LOCATION_SERVICE);
                final boolean gpsEnable = mlocManag.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if (!gpsEnable) {
                    Intent settingInt = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(settingInt);// ACTION_APPLICATION_DETAILS_SETTINGS
                    pressBotonMiHubication=false;
                }
            }
        }catch (Exception e){
            //Toast.makeText(getContext(),"error: "+e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void exitoTraerEspecialidades(ArrayList<especialidadMedica> x) {
        if (x==null) seem.traerEspecialidadesMedicas(contex.get(),this);
        else{
            listaEspecialdades=x;
            listaStringEspecialidades=new ArrayList<>();
            listaStringEspecialidades.add(getString(R.string.spiner_cabecera));
            for (int i=0;i < x.size();i++){
                listaStringEspecialidades.add(listaEspecialdades.get(i).getNombreEspecialidad()/*+"-"+listaEspecialdades.get(i).getCodigoEspecialidad()*/);
            }
            llenarSpiner(listaStringEspecialidades);
        }
    }

    public void llenarSpiner(ArrayList<String> lista)
    {
         ArrayAdapter<String> adapter=new ArrayAdapter<>(contex.get(), android.R.layout.simple_spinner_item,lista);
        especialiddmedica.setAdapter(adapter);
    }

    @Override
    public void errorTraerEspecialidades(String y) {}


    private void mostrarDialogPersonalizado(Context c){
        AlertDialog.Builder builder=new AlertDialog.Builder(c);
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog,null);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        TextView txt=view.findViewById(R.id.text_dialog);
        txt.setText(R.string.fallo_de_conexion);
        Button btn=view.findViewById(R.id.btn_dialogo_ok);
        btn.setOnClickListener(v -> {
            //Toast.makeText(getActivity(),"los servicios de esta app necesitan de Internet ",Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });
    }
 /*
    private void mostrarDialogPersonalizadoPermiss(Context c){
        AlertDialog.Builder builder=new AlertDialog.Builder(c);
        LayoutInflater inflater=getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog,null);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        TextView txt=view.findViewById(R.id.text_dialog);
        txt.setTextSize(14);
        txt.setText(R.string.dialog_splashScreem_permiso_ubicacion);
        Button btn=view.findViewById(R.id.btn_dialogo_ok);
        btn.setOnClickListener(v -> {
            dialog.dismiss();
            //VOLVER A HACER LA CONDICION DEL PERMISO
            if (ContextCompat.checkSelfPermission(contex.get(), Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(contex.get(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);//q se vea el boton de mi ubicacion
                mMap.setOnMyLocationButtonClickListener(MapsFragment.this);//tocas el boton de mi ubicacion
            }
        });
    }
    */
}