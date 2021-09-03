package guiasalud.estableciemtos.guiamedica.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

import guiasalud.estableciemtos.guiamedica.ModelsEntities.doctor;
import guiasalud.estableciemtos.guiamedica.ModelsEntities.especialidadMedica;
import guiasalud.estableciemtos.guiamedica.R;
import guiasalud.estableciemtos.guiamedica.adacter.listDocAdapter;
import guiasalud.estableciemtos.guiamedica.services.IDoctor;
import guiasalud.estableciemtos.guiamedica.services.IEspecialidadesMedicas;
import guiasalud.estableciemtos.guiamedica.services.ServicioDocSegunEstableci;
import guiasalud.estableciemtos.guiamedica.verificacionInternet;

public class docFragment extends Fragment implements IEspecialidadesMedicas, IDoctor {
    private static int codigoEstab;
    RecyclerView recyclerView;
    listDocAdapter listDoctorAdapt;
    ServicioDocSegunEstableci sdsEsta=new ServicioDocSegunEstableci();
    private static ArrayList<especialidadMedica> listaEspecialdades=new ArrayList<>();
    ArrayList<String> listaStringEspecialidades;
    private Spinner especialiddmedica;
    private verificacionInternet aInternt;

    public docFragment() {
        // Required empty public constructor
    }
    public static docFragment createNewInstance(int codEstablecimient){
        codigoEstab=codEstablecimient;
        return new docFragment();
    }

    public static docFragment newInstance() {
        docFragment fragment = new docFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Activity a= getActivity();
        //if (a!=null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        aInternt=new verificacionInternet(getContext());
        sdsEsta.traerEspecialidadesConMiniUnDocSegunEstableci(getContext(),codigoEstab,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root=inflater.inflate(R.layout.fragment_doc, container, false);
        ArrayList<doctor> xx=new ArrayList<>();
        especialiddmedica=root.findViewById(R.id.spinnerEspecConMiniUnDoc);
        recyclerView= root.findViewById(R.id.listaRecyclerViewDoctores);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        listDoctorAdapt = new listDocAdapter(xx,getContext());
        recyclerView.setAdapter(listDoctorAdapt);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Toast.makeText(getContext(),"codigo en docFragrment:"+codigoEstab,Toast.LENGTH_LONG).show();
        selectSpinnerEspecialiddFrag(codigoEstab);
    }
    public void selectSpinnerEspecialiddFrag(int codEstablecimiento){
        especialiddmedica.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int posicioReal=position-1;
                //Toast.makeText(getContext(),"posicion spinner:"+posicioReal,Toast.LENGTH_LONG).show();
                if (aInternt.HayInternet()){
                    if (position!=0){
                        int codEspecialidad;
                        codEspecialidad=listaEspecialdades.get(posicioReal).getCodigoEspecialidad();
                        sdsEsta.traerDoctorsSegunEspecialiddEstableci(getContext(),codEstablecimiento,codEspecialidad,docFragment.this);

                    }else if (position==0){
                        sdsEsta.traerTodosLosDoctoresDelEstablecimiento(getContext(),codEstablecimiento,docFragment.this);
                    }
                }else mostrarDialog(getContext());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
// de la interface especialidades medicas
    @Override
    public void exitoTraerEspecialidades(ArrayList<especialidadMedica> x) {
        listaEspecialdades=x;
        listaStringEspecialidades=new ArrayList<>();
        listaStringEspecialidades.add("Todos");
        for (int i=0;i < x.size();i++){
            listaStringEspecialidades.add(listaEspecialdades.get(i).getNombreEspecialidad());
        }
        llenarSpinerEspecialidds(listaStringEspecialidades);
    }
    public void llenarSpinerEspecialidds(ArrayList<String> lista)
    {
        ArrayAdapter<CharSequence> adapter=new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item,lista);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        especialiddmedica.setAdapter(adapter);
        //especialiddmedica.setOnItemSelectedListener(this);
    }

    @Override
    public void errorTraerEspecialidades(String y) {

    }
//interfas doctores, LLENA EL ADAPTADOR PARA Q PINTE EL recyclerView
    @Override
    public void exitoTraerDoctores(ArrayList<doctor> x) {
        listDoctorAdapt = new listDocAdapter(x,getContext());
        recyclerView.setAdapter(listDoctorAdapt);
    }

    @Override
    public void errorTraerDoctores(String y) {

    }
    private void mostrarDialog(Context c) {//dialogo basico
        new AlertDialog.Builder(c)
                .setTitle("Problemas Con Internet")
                .setMessage("no cuenta con Internet para acceder a este cervicio")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //getActivity().finish();
                        dialog.dismiss();
                    }
                }).show();
    }
}