package guiasalud.estableciemtos.guiamedica.fragment;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import guiasalud.estableciemtos.guiamedica.modelsEntities.especialidadMedica;
import guiasalud.estableciemtos.guiamedica.R;
import guiasalud.estableciemtos.guiamedica.adacter.listEspecialidAdapter;
import guiasalud.estableciemtos.guiamedica.services.IEspecialidadesMedicas;
import guiasalud.estableciemtos.guiamedica.services.ServicioEspecialiddsSegunEstableci;

public class especialiddsFragment extends Fragment implements IEspecialidadesMedicas{
    private static int codigoEstab;
    RecyclerView recyclerView;
    listEspecialidAdapter listEspeciAdapt;
    ServicioEspecialiddsSegunEstableci sesgEstablecimient=new ServicioEspecialiddsSegunEstableci();
    private ProgressDialog progressDialogGlobal;

    public especialiddsFragment() {
        // Required empty public constructor
    }
    public static especialiddsFragment createNewInstance(int codEstablecimient){
        codigoEstab=codEstablecimient;
        return new especialiddsFragment();
    }

    public static especialiddsFragment newInstance() {
        especialiddsFragment fragment = new especialiddsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialogGlobal=new ProgressDialog(getContext());
        progressDialogGlobal.setMessage(getString(R.string.cargar));
        progressDialogGlobal.show();
        //Activity a= getActivity();
        //if (a!=null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rooView=inflater.inflate(R.layout.fragment_especialidds, container, false);
        //recyclerView.setAdapter();
        ArrayList<especialidadMedica> xx=new ArrayList<>();
        recyclerView= rooView.findViewById(R.id.listRecyclerViewEspecialidades);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listEspeciAdapt = new listEspecialidAdapter(xx,getContext());
        recyclerView.setAdapter(listEspeciAdapt);
        sesgEstablecimient.especialiddsSegunEstableci(getContext(),codigoEstab,this);
        return rooView;
    }
    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //recyclerView=(RecyclerView) view.findViewById(R.id.listRecyclerViewEspecialidades);
    }

    @Override
    public void exitoTraerEspecialidades(ArrayList<especialidadMedica> x) {
        listEspeciAdapt = new listEspecialidAdapter(x,getContext());
        //RecyclerView recyclerView=find;
        //recyclerView.setHasFixedSize(true);
        //recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(listEspeciAdapt);
        progressDialogGlobal.dismiss();
    }

    @Override
    public void errorTraerEspecialidades(String y) {

    }
}