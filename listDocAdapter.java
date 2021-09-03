package guiasalud.estableciemtos.guiamedica.adacter;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import java.util.List;

import guiasalud.estableciemtos.guiamedica.ExpandAndCollapseViewUtil;
import guiasalud.estableciemtos.guiamedica.ModelsEntities.doctor;
import guiasalud.estableciemtos.guiamedica.R;
import guiasalud.estableciemtos.guiamedica.verificacionInternet;
import pl.droidsonroids.gif.GifImageButton;

public class listDocAdapter extends RecyclerView.Adapter<listDocAdapter.ViewHolderDoc> {
    //relaciona los datos con el cardview que hicimos "doclist.xml y especialiddlista.xml"
    private List<doctor>mData;
    private final LayoutInflater mInflater;//ayuda q describrir de que layaut, de q archivo proviene...infla
    private final Context context;//para saber de q clase se esta llamando a ste adaptador
    private verificacionInternet aInternt;
    private Vibrator vb;
    private int posicionMarcada=-1;
    private int contador =0;

    public listDocAdapter(List<doctor> itemsList, Context context){
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.mData=itemsList;
    }
    @Override
    public int getItemCount(){
        try{
            return mData.size();
        }catch (Exception e){ return 0;}
        }

    @NotNull
    @Override
    public listDocAdapter.ViewHolderDoc onCreateViewHolder(@NotNull ViewGroup parent, int viewType){
        View view=mInflater.inflate(R.layout.doclista,null);
        aInternt=new verificacionInternet(context);
        return new listDocAdapter.ViewHolderDoc(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull listDocAdapter.ViewHolderDoc holder, int position) {

        //super.onBindViewHolder(holder, position, payloads);
        holder.cardDoctor.setAnimation(AnimationUtils.loadAnimation(context,R.anim.slide));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Recycle Click" + position, Toast.LENGTH_SHORT).show();
            }
        });
        holder.bindData(mData.get(position));
    }

    //puede servir para hacer un listado nuevo,vuelves a definir todo los items de esta lista
    public void setItems(List<doctor> items){mData=items;}

    public class ViewHolderDoc extends RecyclerView.ViewHolder implements View.OnClickListener {
        //
        private TextView nombreCompleto,edad,NumColegiatura,descripTipoAtencion,precio,especialidadDoc,NumColegiatura1;
        private String especialdd;
        private ImageView flecha,iconDoctors;
        //private final ImageButton iconInstagram,iconFacebook;
        //private final GifImageButton iconllamar;
        RelativeLayout expand,click;
        //private ExpandableTextView descTextVieww;
        private final Button botonMostrarDialog;
        private String numeroTelef,enlaceFaceb,enlaceInstag;
        private String nombreDoc;
        String dirface="fb://profile/";//1037901420
        private static final int DURATION = 250;
        private ViewGroup linearLayoutDetails,linearLayoutContactame;
        CardView cardDoctor;
        LinearLayout ll,llc;
        ViewHolderDoc(View itemView){
            super(itemView);
            //descTextVieww=itemView.findViewById(R.id.descTextView);
            nombreCompleto=itemView.findViewById(R.id.textNombreYApellido);
            edad=itemView.findViewById(R.id.textRNE);
            NumColegiatura1=itemView.findViewById(R.id.textNumColegiatura1);
            NumColegiatura=itemView.findViewById(R.id.textNumColegiatura);
            descripTipoAtencion=itemView.findViewById(R.id.textDescripcionAtencion);
            precio=itemView.findViewById(R.id.textPrecio);
            especialidadDoc=itemView.findViewById(R.id.textEspecialidadDoc);
            flecha=itemView.findViewById(R.id.flechaIconoColegiatura);
            iconDoctors=itemView.findViewById(R.id.iconEspecialiddoc);

            linearLayoutDetails=itemView.findViewById(R.id.RLexpandible);//
            botonMostrarDialog=itemView.findViewById(R.id.btnMostrarHorario);
            //botonOcultarHorario=itemView.findViewById(R.id.btnQuitarHorario);
            ll=itemView.findViewById(R.id.LLDescripHorario);
            edad.setOnClickListener(v -> Toast.makeText(context,"Registro Nacional de Especialista",Toast.LENGTH_SHORT).show());
            botonMostrarDialog.setOnClickListener(v -> {
                avisoPermissContactame(context);
            });
                flecha.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (aInternt.HayInternet()){
                            Uri uri=Uri.parse("https://www.cmp-trujillo.org.pe/conoce-tu-medico");
                            Intent intent=new Intent(Intent.ACTION_VIEW, uri);
                            context.startActivity(intent);
                        }else mostrarDialog(context);
                    }
                });

            cardDoctor=itemView.findViewById(R.id.docListaCardview);
            expand=itemView.findViewById(R.id.RLexpandible);
            click=itemView.findViewById(R.id.RLClick);
            NumColegiatura1.setOnClickListener(v -> Toast.makeText(context,"Colegio Médico del Perú",Toast.LENGTH_SHORT).show());
            vb=(Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        }
        @SuppressLint("SetTextI18n")
        void bindData(final doctor item){
            numeroTelef=item.getNuContacto();
            /*if (numeroTelef.equals("")){
                iconllamar.setVisibility(View.GONE);
            }*/
            enlaceFaceb=item.getDirFacebook();
            //Toast.makeText(context, "dato facebook:"+enlaceFaceb,Toast.LENGTH_LONG).show();
            /*if (enlaceFaceb.equals("")){
                iconFacebook.setVisibility(View.GONE);
            }*/
            enlaceInstag=item.getDirInstagram();
           /* if (enlaceInstag.equals("")){
                iconInstagram.setVisibility(View.GONE);
            }*/
            nombreCompleto.setText(item.getApellido()+" "+item.getNombre());
            nombreDoc=item.getApellido()+" "+item.getNombre();
            if (item.getRne().equals("") || item.getRne()==null){
                NumColegiatura1.setText("CMP: "+item.getNumColegiatura());
                NumColegiatura1.setVisibility(View.VISIBLE);
                edad.setVisibility(View.GONE);
            } else {
                edad.setText("RNE: "+(item.getRne()));
                edad.setVisibility(View.VISIBLE);
                NumColegiatura1.setVisibility(View.GONE);
            }
            NumColegiatura.setText(item.getNumColegiatura());
            NumColegiatura.setOnClickListener(this);
            descripTipoAtencion.setText(item.getDescripTipoAtencioDoc());
            if (String.valueOf(item.getPrecio()).length()<1) precio.setVisibility(View.GONE);
            else precio.setText("Precio de consulta: "+item.getPrecio()+" S/");
            //horario.setText(item.getDirFacebook());
            especialdd=item.getEspecialidad();
            if (especialdd!=null)especialidadDoc.setText(item.getEspecialidad());
            else {
                especialidadDoc.setVisibility(View.GONE);
                nombreCompleto.setPadding(1,15,10,0);
            }
            Picasso.get()
                    .load("https://img.icons8.com/material-two-tone/58/000000/circled-right-2.png")
                    .into(flecha);
            Picasso.get()
                    .load("https://img.icons8.com/dusk/112/000000/stethoscope.png")
                    .into(iconDoctors);
            //EVENTO onClick PARA QUE SE EXPANDA EL cardView
             click.setOnClickListener(v -> {
                if (expand.getVisibility()==View.GONE) {
                    ExpandAndCollapseViewUtil.expand(linearLayoutDetails,DURATION);
                }
                else {
                    ExpandAndCollapseViewUtil.collapse(linearLayoutDetails, DURATION);
                    botonMostrarDialog.setVisibility(View.VISIBLE);
                }
            });

        }
        //////////
        @SuppressLint("SetTextI18n")
        private void avisoPermissContactame(Context c){
            final GifImageButton iconllamarDialog;
            final ImageButton iconInstagramDialog,iconFacebookDialog;
            AlertDialog.Builder builder=new AlertDialog.Builder(c);
            //LayoutInflater inflater=getLayoutInflater();
            View view=mInflater.inflate(R.layout.dialogo_contactame,null);
            //m_llMain=view.findViewById(R.id.cadllMain);
            //m_llMain.setBackgroundResource(R.drawable.dialogredondeado);
            builder.setView(view);
            AlertDialog dialog=builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            TextView txt=view.findViewById(R.id.text_dialog);
            txt.setTextSize(14);
            txt.setText("Contactame ahora...");
            Button btn=view.findViewById(R.id.btn_dialogo_ok);
            TextView nombre=view.findViewById(R.id.dialog_docNombre);
            nombre.setText("Doctor/a."+nombreDoc);
            iconllamarDialog=view.findViewById(R.id.iconLlamarDocDialog);
            iconInstagramDialog=view.findViewById(R.id.iconInstagramDocDialog);
            iconFacebookDialog=view.findViewById(R.id.iconFacebookDocDialog);
            //logica iconos
            if (numeroTelef.equals("")){
                iconllamarDialog.setVisibility(View.GONE);
            }
            if (enlaceFaceb.equals("")){
                iconFacebookDialog.setVisibility(View.GONE);
            }
            if (enlaceInstag.equals("")){
                iconInstagramDialog.setVisibility(View.GONE);
            }
            Picasso.get()
                    .load("https://img.icons8.com/fluency/79/000000/instagram-new.png")
                    .into(iconInstagramDialog);
            Picasso.get()
                    .load("https://img.icons8.com/ios-filled/71/4a90e2/facebook--v1.png")
                    .into(iconFacebookDialog);//+8
            //evento de botones
            btn.setOnClickListener(v -> {
                dialog.dismiss();
            });
            iconllamarDialog.setOnClickListener(this);
            iconInstagramDialog.setOnClickListener(this);
            iconFacebookDialog.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId()==R.id.iconLlamarDocDialog){
                vb.vibrate(38);
                int permiso= ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
                if (permiso != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context,"No tiene permisos de llamada",Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE},255);
                }else {
                    String numero="tel:"+numeroTelef;
                    Intent i=new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse(numero));
                    context.startActivity(i);
                }
            }else if(v.getId()==R.id.iconFacebookDocDialog){
                vb.vibrate(38);
                if (aInternt.HayInternet()) {
                    try {
                        Uri uriAfb,uriWfb;
                        Intent intent;
                        //String[] copi=enlaceFaceb.split(";");
                        //Toast.makeText(context, "posicion 0: "+copi[0]+"posicion 1: "+copi[1],Toast.LENGTH_LONG).show();
                        //copi[0];//enlace de fb web
                        //copi[1];//codigo ID
                        /*if (copi[1].trim() != null){
                            String enlaceAplicacion=dirface+copi[1].trim();
                            uriAfb = Uri.parse(enlaceAplicacion);
                            intent = new Intent(Intent.ACTION_VIEW, uriAfb);
                            Toast.makeText(context, "Mostrando Facebook", Toast.LENGTH_SHORT).show();
                        }else*/ //{
                        //String enlaceWeb=copi[0].trim();
                        uriWfb = Uri.parse(enlaceFaceb);
                        intent = new Intent(Intent.ACTION_VIEW, uriWfb);
                        //Toast.makeText(context, "No tiene descargada la aplicacion, abriendo Navegador", Toast.LENGTH_SHORT).show();
                        //}
                        context.startActivity(intent);
                        //Toast.makeText(context, "Mostrando Facebook", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "problemas con la direccion de facebook, consulte con el Admin", Toast.LENGTH_SHORT).show();
                    }
                }else Toast.makeText(context,"no cuenta con internet",Toast.LENGTH_SHORT).show();
            }else if(v.getId()==R.id.iconInstagramDocDialog){
                vb.vibrate(38);
                if (aInternt.HayInternet()) {
                    try {
                        Uri uri = Uri.parse(enlaceInstag);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                        Toast.makeText(context, "Mostrando Instagran", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(context, "problemas con la direccion de instagram, consulte con el Admin", Toast.LENGTH_SHORT).show();
                    }
                }else Toast.makeText(context,"no cuenta con internet",Toast.LENGTH_SHORT).show();
            }else if (v.getId()==R.id.textNumColegiatura){
                vb.vibrate(38);
                Toast.makeText(context, "presiona durante 2 segundos", Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void onCopyAddressClick(Context context, String phoneUrl) {
        final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText(context.getPackageName(), phoneUrl);
        //noinspection ConstantConditions
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Copiado al portapapeles", Toast.LENGTH_SHORT).show();
    }
    private void mostrarDialog(Context c) {//dialogo basico
        new AlertDialog.Builder(c)
                .setTitle("Problemas Con Internet")
                .setMessage("se necesita Internet para verificar la colegiatura")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    //getActivity().finish();
                    dialog.dismiss();
                }).show();
    }

}
