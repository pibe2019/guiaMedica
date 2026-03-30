package guiasalud.estableciemtos.guiamedica.fragment;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.os.Vibrator;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.material.textfield.TextInputLayout;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;
import guiasalud.estableciemtos.guiamedica.modelsEntities.establecimientoMapa;
import guiasalud.estableciemtos.guiamedica.R;
import guiasalud.estableciemtos.guiamedica.services.IRecepcionEstablecimientoCcordenads;
import guiasalud.estableciemtos.guiamedica.services.ServicioEstablecimientoCcordenads;
import guiasalud.estableciemtos.guiamedica.verificacionInternet;
import me.saket.bettermovementmethod.BetterLinkMovementMethod;
import pl.droidsonroids.gif.GifImageButton;

public class establecimientoFragment extends Fragment implements View.OnClickListener, IRecepcionEstablecimientoCcordenads {
    private String nombre,paginaWebe,facebooke;
    //private Bitmap imagen;
    private int codigoEstablecimiento;
    //private EditText edtnombre,edttipo,edtnumeroAtencion,edtnumeroEmergencia,edtwhatssap,edtcorreo,edtlicencia,edtdireccion,edtdescripcion;
    private ImageView imgEstablecimiento;
    //NavController navController;
    Fragment especialiddsYDoctoresFragmento;
    ServicioEstablecimientoCcordenads sec=new ServicioEstablecimientoCcordenads();
    private ProgressBar progressBar;
    private verificacionInternet aInternt;
    //private LinearLayout lltAtencion,llWhatss,llCorreo,llLicencia;
    //private GifImageButton flecha;
    ImageButton pageWeb,imgfacebook;
    private Vibrator vb;
    TextInputLayout tilNumEmergencia;
    Context context;
    protected View mView;

    public establecimientoFragment() {
        // Required empty public constructor
    }

    public static establecimientoFragment createNewInstance(ArrayList<establecimientoMapa> especi){
        return new establecimientoFragment();
    }


    public static establecimientoFragment newInstance() {
        establecimientoFragment fragment = new establecimientoFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        especialiddsYDoctoresFragmento=new especialiddsYDocFragment();
        context=getContext();
    }
    private void onCopyAddressClick(Context context, String phoneUrl) {
        final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText(context.getPackageName(), phoneUrl);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, R.string.toas_confir_copy, Toast.LENGTH_SHORT).show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert getArguments() != null;
        aInternt=new verificacionInternet(this.getActivity());
        codigoEstablecimiento= getArguments().getInt("codigoEstablecimiento");//--------
        return inflater.inflate(R.layout.fragment_establecimiento, container, false);
    }

    @SuppressLint("CutPasteId")
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (aInternt.HayInternet()) {
            sec.traerImagenDeEstabelcimeinto(getContext(),codigoEstablecimiento,this);
        }else mostrarDialog(this.getContext());
        //navController= Navigation.findNavController(view); navegar entre fragmentos
        //TextView facebook =view.findViewById(R.id.idPaginawebbutom);
        //TextView pagweb =view.findViewById(R.id.idFacebookbutom);
        this.mView = view;
        progressBar=view.findViewById(R.id.loadingProgresBar);
        LinearLayout lltAtencion=view.findViewById(R.id.llNuAtencion);
        LinearLayout llWhatss=view.findViewById(R.id.llWhatssap);
        LinearLayout llCorreo=view.findViewById(R.id.llEmail);
        //lltEmergenci=view.findViewById(R.id.llNuEmergenci);
        LinearLayout llLicencia=view.findViewById(R.id.llLicencia);
        //facebookGif=new GifImageButton(getContext());
        imgfacebook=view.findViewById(R.id.btnFacebook);
        pageWeb=view.findViewById(R.id.imgButtonWeb);
        assert getArguments() != null;
        //codigoEstablecimiento= getArguments().getInt("codigoEstablecimiento");//--------

        paginaWebe= getArguments().getString("paginaWeb");//--------
        facebooke= getArguments().getString("facebook");//--------

        EditText edtnombre=view.findViewById(R.id.edtNombre);
        //txtnombre=view.findViewById(R.id.textNombre);
        assert getArguments() != null;
        edtnombre.setText(getArguments().getString("nombre"));
        //txtnombre.setText(getArguments().getString("nombre"));
        nombre=getArguments().getString("nombre");

        //txttipo=view.findViewById(R.id.textTipo);
        //txttipo.setText(getArguments().getString("tipo"));
        EditText edttipo=view.findViewById(R.id.edtTipo);
        edttipo.setText(getArguments().getString("tipo"));


        //txtdireccion=view.findViewById(R.id.textDireccion);//-------
        EditText edtdireccion=view.findViewById(R.id.edtDireccion);//-------
        //txtdireccion.setText(getArguments().getString("direccion"));
        edtdireccion.setText(getArguments().getString("direccion"));


        //txtnumeroAtencion=view.findViewById(R.id.textNumeroAtencion);//-------
        EditText edtnumeroAtencion=view.findViewById(R.id.edtNumeroAtencion);//-------
        if (getArguments().getString("numeroAtencion").length()<1) lltAtencion.setVisibility(View.GONE);
        //else txtnumeroAtencion.setText(getArguments().getString("numeroAtencion"));
        else edtnumeroAtencion.setText(getArguments().getString("numeroAtencion"));

        //txtwhatssap=view.findViewById(R.id.textWhatssap);//-------
        EditText edtwhatssap=view.findViewById(R.id.edtWhatssap);//-------
        if(getArguments().getString("whatssap").length()<1) llWhatss.setVisibility(View.GONE);
        //else txtwhatssap.setText(getArguments().getString("whatssap"));
        else edtwhatssap.setText(getArguments().getString("whatssap"));

        //txtcorreo=view.findViewById(R.id.textCorreo);//-------
        EditText edtcorreo=view.findViewById(R.id.edtCorreo);//-------
        if (getArguments().getString("correo").length()<1) llCorreo.setVisibility(View.GONE);
        //else {txtcorreo.setText(getArguments().getString("correo"));
        else {
            edtcorreo.setText(getArguments().getString("correo"));
        }
        //txtcorreo.getPaint().setFlags(Paint.FAKE_BOLD_TEXT_FLAG);
        //txtnumeroEmergencia=view.findViewById(R.id.textNumEmergencia);//-------
        EditText edtnumeroEmergencia=view.findViewById(R.id.edtNumEmergencia);//-------
        tilNumEmergencia=view.findViewById(R.id.numEmergenciaTIL);

        if (getArguments().getString("numeroEmergencia").length()<1) tilNumEmergencia.setVisibility(View.GONE);
        //else txtnumeroEmergencia.setText(getArguments().getString("numeroEmergencia"));
        else edtnumeroEmergencia.setText(getArguments().getString("numeroEmergencia"));

        //txtlicencia=view.findViewById(R.id.textLicencia);//-------
        EditText edtlicencia=view.findViewById(R.id.edtLicencia);//-------
        if (getArguments().getString("licencia").length()<1) llLicencia.setVisibility(View.GONE);
        //else txtlicencia.setText(getArguments().getString("licencia"));
        else edtlicencia.setText(getArguments().getString("licencia"));

        //txtdescripcion=view.findViewById(R.id.textDescripcion);//-------
        EditText edtdescripcion=view.findViewById(R.id.edtDescripcion);//-------
        //txtdescripcion.setText(getArguments().getString("descripcion"));
        edtdescripcion.setText(getArguments().getString("descripcion"));

        imgEstablecimiento=view.findViewById(R.id.imageEstablecimiento);//--

        GifImageButton flecha=view.findViewById(R.id.flechaIcono);
        pageWeb.setOnClickListener(this);
        imgfacebook.setOnClickListener(this);
        //iconoMas=view.findViewById(R.id.flechaIcono);
        flecha.setOnClickListener(this);

        /*Picasso.get()
                .load("https://img.icons8.com/ios-filled/105/4a90e2/facebook--v1.png")
                .into(imgfacebook);*/
        //facebookGif.setImageResource(R.drawable.facebook);
        //if (imagen !=null)imgEstablecimiento.setImageBitmap(imagen);
        //else imgEstablecimiento.setImageResource(R.drawable.imagen);

        BetterLinkMovementMethod.linkify(Linkify.ALL, edtnumeroAtencion,edtwhatssap,edtcorreo,edtnumeroEmergencia)
                .setOnLinkClickListener((textView, url) -> false).setOnLinkLongClickListener(((textView, url) -> {
            String[] copi=url.split(":");
            onCopyAddressClick(context, copi[1]);
            return true;
        }));
        vb=(Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (getActivity()!=null)
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        //getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btnFacebook){
            vb.vibrate(38);
            if (facebooke.equals("")){
                Toast.makeText(getContext(), R.string.aviso_toas_fb_no, Toast.LENGTH_SHORT).show();
            }else{
                if (aInternt.HayInternet()) {
                    try {
                        Uri uri=Uri.parse(facebooke);
                        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                        Toast.makeText(getContext(), R.string.aviso_toas_fb_si, Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(getContext(), R.string.aviso_toas_fb_error, Toast.LENGTH_SHORT).show();
                    }
                }else Toast.makeText(getContext(),R.string.toas_internet_no,Toast.LENGTH_SHORT).show();
            }
        }else if (v.getId()==R.id.imgButtonWeb){
            vb.vibrate(38);
            if (paginaWebe.equals("")){
                Toast.makeText(getContext(), R.string.aviso_toas_web_no, Toast.LENGTH_SHORT).show();
            }else{
                if (aInternt.HayInternet()) {
                    try {
                        Uri uri=Uri.parse(paginaWebe);
                        Intent intent=new Intent(Intent.ACTION_VIEW,uri);
                        startActivity(intent);
                        Toast.makeText(getContext(), R.string.aviso_toas_web_si, Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toast.makeText(getContext(), R.string.aviso_toas_web_error, Toast.LENGTH_SHORT).show();
                    }
                }else Toast.makeText(getContext(),R.string.toas_internet_no,Toast.LENGTH_SHORT).show();
            }
        }else if (v.getId()==R.id.flechaIcono){
            //progressDialogGlobal= new ProgressDialog(context);
            //progressDialogGlobal.setMessage(context.getString(R.string.cargaWeb));
            //progressDialogGlobal.show();
            if (aInternt.HayInternet()) {
                Bundle bundle = new Bundle();
                bundle.putString("nombre", nombre);
                bundle.putInt("codigoEstablecimiento", codigoEstablecimiento);
                Navigation.findNavController(mView).navigate(R.id.especialiddsYDocFragment, bundle);
                //navController.navigate(R.id.especialiddsYDocFragment);//navegar entre fragmentos
            }else {
                mostrarDialogBtnFlecha(context);
            }
                //Toast.makeText(getContext(),"no cuenta con Internet para acceder a este servicio",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void exitoCorde(ArrayList<establecimientoMapa> x, GoogleMap googleMap) {

    }
    List<establecimientoMapa> emImagen=new ArrayList<>();
    @Override
    public void exitoImagen(ArrayList<establecimientoMapa> x) {
        emImagen=x;
        if (emImagen.get(0).getImagen() != null){
            imgEstablecimiento.setImageBitmap(emImagen.get(0).getImagen());
            /*progressBar.setVisibility(View.GONE);
            imgEstablecimiento.setVisibility(View.VISIBLE);*/
        }else {
            imgEstablecimiento.setImageResource(R.drawable.imagen);
            /*progressBar.setVisibility(View.GONE);
            imgEstablecimiento.setVisibility(View.VISIBLE);*/
        }
        progressBar.setVisibility(View.GONE);
        imgEstablecimiento.setVisibility(View.VISIBLE);
    }

    @Override
    public void errorCorde(String x) {

    }
    private void mostrarDialog(Context c) {
        new AlertDialog.Builder(c)
                .setTitle(R.string.dialog_general)
                .setMessage(R.string.dialog_imagen_internet_no)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> dialog.dismiss()).show();
    }
    private void mostrarDialogBtnFlecha(Context c) {
        new AlertDialog.Builder(c)
                .setTitle(R.string.dialog_general)
                .setMessage(R.string.dialog_btnflecha_internet_no)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> dialog.dismiss()).show();
    }
}