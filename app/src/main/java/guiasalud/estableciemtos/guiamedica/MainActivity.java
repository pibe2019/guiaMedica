package guiasalud.estableciemtos.guiamedica;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;

import guiasalud.estableciemtos.guiamedica.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    //private ActivityMainBinding binding;
    verificacionInternet aInternt=new verificacionInternet(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (aInternt.HayInternet()){

            ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            setSupportActionBar(binding.appBarMain.toolbar);
        /*binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
            DrawerLayout drawer = binding.drawerLayout;
            NavigationView navigationView = binding.navView;
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_adminEstablecimiento/*,R.id.establecimientoFragment2*/)
                    .setOpenableLayout(drawer)
                    .build();
            //forma para corregir el problema de fragmentContainerView
            NavHostFragment navHostFragment=(NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_main) ;
            assert navHostFragment != null;
            NavController navController= navHostFragment.getNavController();

            //este solo funciona con el <fragment>
            //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

            //se implementa para poder tener el control de los botones del menu lateral
            //y se podra tener acceso a su metodo onNavigationItemSelected()
           //NavigationView navigationVieww = findViewById(R.id.nav_view);
            //navigationVieww.setNavigationItemSelectedListener(this);

       /* }else {
            Toast.makeText(this,"no puede acceder a este servicio,sin Internet",Toast.LENGTH_SHORT).show();
            this.finish();
        }*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    //se tiene acceso a controlar las opciones del tollbar, el icono de amburgueza del menu lateral
        //y los 3 puntos q es el boton de la derecha
        if (item.getItemId() == R.id.action_settings) {
            //Toast.makeText(this, "nonet", Toast.LENGTH_SHORT).show();
            this.logout();
        }
        return super.onOptionsItemSelected(item);
    }
    //metodo cerrar cesion
    private void logout() {
        //SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        //SharedPreferences.Editor editor=preferences.edit();
        //editor.remove("Usuario");
        //editor.apply();
        this.finish();

        /*Intent i=new Intent(Intent.ACTION_MAIN);
        i.addCategory(Intent.CATEGORY_HOME);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        //para tener acceso a este metodo, se tiene que implementar arriba en el MainActivity
        //implements NavigationView.OnNavigationItemSelectedListener
        return false;
    }
}