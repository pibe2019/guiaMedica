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

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import java.util.List;
import guiasalud.estableciemtos.guiamedica.R;
import guiasalud.estableciemtos.guiamedica.modelsEntities.doctor;
import guiasalud.estableciemtos.guiamedica.services.OnItemDoctorClick;
import guiasalud.estableciemtos.guiamedica.verificacionInternet;
import pl.droidsonroids.gif.GifImageButton;

import static guiasalud.estableciemtos.guiamedica.R.*;

public class listDocAdapter extends RecyclerView.Adapter<listDocAdapter.ViewHolderDoc> {
    //relaciona los datos con el cardview que hicimos "doclist.xml y especialiddlista.xml"
    private List<doctor>mData;
    private final LayoutInflater mInflater;//ayuda q describrir de que layaut, de q archivo proviene...infla
    private final Context context;//para saber de q clase se esta llamando a ste adaptador
    private verificacionInternet aInternt;
    private Vibrator vb;
    //private final int positionMarcada=-1;
    private final OnItemDoctorClick OOnItemDoctorClick;
    //private static final int DURATION = 700;//250

    /*public interface OnItemDoctorClick{
        static void onItemClick(View view, int position, RelativeLayout relativeLayout, RelativeLayout click){};
    }*/

    public listDocAdapter(List<doctor> itemsList, Context context, OnItemDoctorClick onItemClickListener){
        this.mInflater=LayoutInflater.from(context);
        this.context=context;
        this.mData=itemsList;
        OOnItemDoctorClick=onItemClickListener;
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
        View view=mInflater.inflate(layout.doclista,parent,false);
        aInternt=new verificacionInternet(context);
        return new listDocAdapter.ViewHolderDoc(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull @NotNull listDocAdapter.ViewHolderDoc holder, int position) {
//sus vistas se completan durante el desplazamiento
        //if (positionMarcada!=position) holder.cardDoctor.setAnimation(AnimationUtils.loadAnimation(context, anim.slide));
        //super.onBindViewHolder(holder, position, payloads);
        /*if ( mData.get(position).isEstadoCardV()) {
            holder.expand.setVisibility(View.VISIBLE);
        } else holder.expand.setVisibility(View.GONE);
        //holder.itemView.setOnClickListener(v -> {}
        holder.click.setOnClickListener(v -> {
            positionMarcada=position;
            mData.get(position).setSelecciono(true);
            for (int i=0;i<getItemCount();i++){
                if (i!=position){
                    mData.get(i).setSelecciono(false);
                }
            }
            notifyDataSetChanged();
        });
        if (positionMarcada==position){
            positionMarcada=-1;
            //posicionPosterior=0;
            if (mData.get(position).isSelecciono()){
                if (holder.expand.getVisibility()==View.GONE && !mData.get(position).isEstadoCardV()) {
                    mData.get(position).setEstadoCardV(true);
                    new Handler().post(() -> {
                        ExpandAndCollapseViewUtil.expand(holder.expand, DURATION);
                    });
                }else {
                    mData.get(position).setEstadoCardV(false);
                    new Handler().post(() -> {
                                ExpandAndCollapseViewUtil.collapse(holder.expand, DURATION);
                            });
                    mData.get(position).setSelecciono(false);
                }
            }
        }else {
            if (mData.get(position).isEstadoCardV() && mData.get(position).isSelecciono()){
                holder.expand.setVisibility(View.VISIBLE);
            }else{
                holder.expand.setVisibility(View.GONE);
                mData.get(position).setEstadoCardV(false);
            }
        }*/
        holder.bindData(mData.get(position));
    }

    //puede servir para hacer un listado nuevo,vuelves a definirtodo los items de esta lista
    public void setItems(List<doctor> items){mData=items;}

    public class ViewHolderDoc extends RecyclerView.ViewHolder implements View.OnClickListener {
        //
        private final TextView nombreCompleto,rne,precio,especialidadDoc,NumColegiatura1;
        private final TextInputEditText tietdescripTipoAtencion,tietNumColegiatura,tietflecha;
        private final TextInputLayout TILverificarColegiatura,TILcolegiatura;
        //private String especialdd;
        private final ImageView iconDoctors;
        //private Boolean expande;
        private final RelativeLayout expand,click;
        //private ExpandableTextView descTextVieww;
        //private final Button botonMostrarDialog;
        private String numeroTelef,enlaceFaceb,enlaceInstag;
        private String nombreDoc;
        //String dirface="fb://profile/";//1037901420
        //private ViewGroup linearLayoutDetails;
        CardView cardDoctor;
        private final LinearLayout ll,llcbda;
        private final Button botonMostrarDialog;

        ViewHolderDoc(View itemView){
            super(itemView);
            //descTextVieww=itemView.findViewById(R.id.descTextView);
            nombreCompleto=itemView.findViewById(id.textNombreYApellidoDocPorEstablecimiento);
            rne=itemView.findViewById(id.textRNEDocPorEstablecimiento);
            NumColegiatura1=itemView.findViewById(id.textNumColegiaturaDocPorEstablecimiento);

            //NumColegiatura=itemView.findViewById(id.textNumColegiatura);
            tietNumColegiatura=itemView.findViewById(id.TIEDNumColegiaturaDocPorEstablecimiento);


            //descripTipoAtencion=itemView.findViewById(id.textDescripcionAtencion);
            tietdescripTipoAtencion=itemView.findViewById(id.TIEDescripcionAtencionDocPorEstablecimiento);

            precio=itemView.findViewById(id.textPrecioDocPorEstablecimiento);
            especialidadDoc=itemView.findViewById(id.textEspecialidadDocPorEstablecimiento);

            TILcolegiatura=itemView.findViewById(id.idColegDocEstablecimiento);
            TILverificarColegiatura=itemView.findViewById(id.idVerifiColegDocEstablecimiento);
            //flecha=itemView.findViewById(id.flechaIconoColegiatura);
            tietflecha=itemView.findViewById(id.TIDConImgFlechaDocPorEstablecimiento);

            iconDoctors=itemView.findViewById(id.iconEspecialiddoc);

            //linearLayoutDetails=itemView.findViewById(R.id.RLexpandibleAzul);//
            botonMostrarDialog=itemView.findViewById(id.btnMostrarHorarioDocPorEstablecimiento);
            //botonOcultarHorario=itemView.findViewById(R.id.btnQuitarHorario);
            ll=itemView.findViewById(id.LLDescripTarjetaDocPoEstablecimiento);
            llcbda=itemView.findViewById(id.LLcardBuscarDocPorEstableimiento);

            cardDoctor=itemView.findViewById(id.docListaCardview);
            expand=itemView.findViewById(id.RLexpandibleDocPorEstabelcimeinto);
            click=itemView.findViewById(id.RLClickDocPorEstablecimiento);

            vb=(Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            tietflecha.setOnClickListener(v -> OOnItemDoctorClick.onItemClick(v, getAdapterPosition(),click,expand));
            //provando
            /**/
            click.setOnClickListener(v -> {
                doctor doc =mData.get(getAdapterPosition());
                doc.setEstadoCardV(!doc.isEstadoCardV());
                notifyItemChanged(getAdapterPosition());
            });
        }
        @SuppressLint("SetTextI18n")
        void bindData(final doctor item){
            boolean isVisible= item.isEstadoCardV();
            expand.setVisibility(isVisible ? View.VISIBLE : View.GONE);
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
            String nuColegiatura=item.getNumColegiatura();
            nombreCompleto.setText(item.getApellido()+" "+item.getNombre());
            nombreDoc=item.getApellido()+" "+item.getNombre();
            if (item.getRne().equals("")){
                rne.setVisibility(View.GONE);
                if(!nuColegiatura.equals("")){
                    NumColegiatura1.setText(context.getString(R.string.iniciales_cmp)+" "+nuColegiatura);
                    NumColegiatura1.setVisibility(View.VISIBLE);
                    TILcolegiatura.setVisibility(View.VISIBLE);
                    TILverificarColegiatura.setVisibility(View.VISIBLE);
                }else {
                    NumColegiatura1.setVisibility(View.GONE);
                    TILcolegiatura.setVisibility(View.GONE);
                    TILverificarColegiatura.setVisibility(View.GONE);
                }
            } else {
                NumColegiatura1.setVisibility(View.GONE);
                rne.setText(context.getString(R.string.iniciales_rne)+" "+(item.getRne()));
                rne.setVisibility(View.VISIBLE);
                TILcolegiatura.setVisibility(View.VISIBLE);
                TILverificarColegiatura.setVisibility(View.VISIBLE);
            }
            //NumColegiatura.setText(item.getNumColegiatura());
            tietNumColegiatura.setText(item.getNumColegiatura());
            //NumColegiatura.setOnClickListener(this);
            tietNumColegiatura.setOnClickListener(this);

            //descripTipoAtencion.setText(item.getDescripTipoAtencioDoc());
            tietdescripTipoAtencion.setText(item.getDescripTipoAtencioDoc());

            if (item.getPrecio()<1) precio.setVisibility(View.GONE);
            else {
                precio.setText(context.getString(R.string.precio_consulta)+" "+item.getPrecio());
                precio.setVisibility(View.VISIBLE);
            }
            //horario.setText(item.getDirFacebook());
            String especialdd=item.getEspecialidad();
            if (especialdd!=null)especialidadDoc.setText(item.getEspecialidad());
            else {
                especialidadDoc.setVisibility(View.GONE);
                nombreCompleto.setPadding(1,15,10,0);
            }
            Picasso.get()
                    .load("https://img.icons8.com/dusk/112/000000/stethoscope.png")
                    .into(iconDoctors);

            tietNumColegiatura.setOnLongClickListener(v -> {
                onCopyAddressClick(context,nuColegiatura);
                return true;
            });
            llcbda.setBackgroundColor(Color.argb(16,0,0,12));
            ///
            ///////
            if(numeroTelef.equals("") && enlaceFaceb.equals("") && enlaceInstag.equals("")){
                botonMostrarDialog.setVisibility(View.GONE);
            }else botonMostrarDialog.setVisibility(View.VISIBLE);

            //llcbda.setBackgroundColor(Color.argb(100,21,23,28));
            rne.setOnClickListener(v -> Toast.makeText(context,R.string.rne_todo,Toast.LENGTH_SHORT).show());
            botonMostrarDialog.setOnClickListener(v -> avisoPermissContactame(context));
            tietflecha.setOnClickListener(v -> {
                if (aInternt.HayInternet()){
                    //String x = "<script>document.getElementById().value='maria'</script>";
                    Uri uri=Uri.parse("https://www.cmp-trujillo.org.pe/conoce-tu-medico");
                    Intent intent=new Intent(Intent.ACTION_VIEW, uri);
                    context.startActivity(intent);
                }else mostrarDialog(context);
            });
            NumColegiatura1.setOnClickListener(v -> Toast.makeText(context,R.string.cmp_todo,Toast.LENGTH_SHORT).show());
        }

        //////////
        @SuppressLint("SetTextI18n")
        private void avisoPermissContactame(Context c){
            final GifImageButton iconllamarDialog;
            final ImageButton iconInstagramDialog,iconFacebookDialog;
            AlertDialog.Builder builder=new AlertDialog.Builder(c);
            //LayoutInflater inflater=getLayoutInflater();
            View view=mInflater.inflate(layout.dialogo_contactame,null);
            builder.setView(view);
            AlertDialog dialog=builder.create();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
            TextView txt=view.findViewById(id.text_dialog);
            txt.setTextSize(14);
            txt.setText(R.string.subTitle_comunicasion);
            Button btn=view.findViewById(id.btn_dialogo_ok);
            TextInputEditText tietnombre=view.findViewById(id.TIETdialog_docNombre);

            tietnombre.setText(nombreDoc);
            iconllamarDialog=view.findViewById(id.iconLlamarDocDialog);
            iconInstagramDialog=view.findViewById(id.iconInstagramDocDialog);
            iconFacebookDialog=view.findViewById(id.iconFacebookDocDialog);
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
            //evento de botones
            btn.setOnClickListener(v -> dialog.dismiss());
            iconllamarDialog.setOnClickListener(this);
            iconInstagramDialog.setOnClickListener(this);
            iconFacebookDialog.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId()== id.iconLlamarDocDialog){
                vb.vibrate(38);
                int permiso= ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE);
                if (permiso != PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(context,R.string.permiso_llamada,Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE},255);
                }else {
                    String numero="tel:"+numeroTelef;
                    Intent i=new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse(numero));
                    context.startActivity(i);
                }
            }else if(v.getId()== id.iconFacebookDocDialog){
                vb.vibrate(38);
                if (aInternt.HayInternet()) {
                    try {
                        Uri uriWfb;
                        Intent intent;
                        uriWfb = Uri.parse(enlaceFaceb);
                        intent = new Intent(Intent.ACTION_VIEW, uriWfb);
                        //Toast.makeText(context, "No tiene descargada la aplicacion, abriendo Navegador", Toast.LENGTH_SHORT).show();
                        //}
                        context.startActivity(intent);
                        //Toast.makeText(context, "Mostrando Facebook", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(context, R.string.facebook_error, Toast.LENGTH_SHORT).show();
                    }
                }else Toast.makeText(context,R.string.no_internet,Toast.LENGTH_SHORT).show();
            }else if(v.getId()== id.iconInstagramDocDialog){
                vb.vibrate(38);
                if (aInternt.HayInternet()) {
                    try {
                        Uri uri = Uri.parse(enlaceInstag);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        context.startActivity(intent);
                        Toast.makeText(context, R.string.instagran_ver, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(context, R.string.instagran_error, Toast.LENGTH_SHORT).show();
                    }
                }else Toast.makeText(context,R.string.no_internet,Toast.LENGTH_SHORT).show();
            }else if (v.getId()== id.TIEDNumColegiaturaDocPorEstablecimiento){
                vb.vibrate(38);
                Toast.makeText(context, R.string.toas_presione, Toast.LENGTH_SHORT).show();
            }
        }

    }
    private void onCopyAddressClick(Context context, String phoneUrl) {
        final ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText(context.getPackageName(), phoneUrl);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, R.string.toas_confir_copy, Toast.LENGTH_SHORT).show();
    }
    private void mostrarDialog(Context c) {//dialogo basico
        new AlertDialog.Builder(c)
                .setTitle(R.string.dialog_general)
                .setMessage(R.string.dialog_internet_colegiatura)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    //getActivity().finish();
                    dialog.dismiss();
                }).show();
    }

}
