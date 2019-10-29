package com.apps.pkl;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class UserActivity extends AppCompatActivity {
    DrawerLayout drawerLayout2;
    NavigationView navigationView2;
    FragmentManager FM2;
    FragmentTransaction FT2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        drawerLayout2 = (DrawerLayout) findViewById(R.id.drawerLayout2);
        navigationView2= (NavigationView) findViewById(R.id.shitstuff2);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Membutuhkan Izin Lokasi", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
        } else {
            Toast.makeText(this, "User Activity", Toast.LENGTH_SHORT).show();
        }
        FM2= getSupportFragmentManager();
        FT2= FM2.beginTransaction();
        FT2.replace(R.id.containerView2, new UserHomeFragment()).commit();

        navigationView2.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout2.closeDrawers();
                if (item.getItemId()== R.id.nav_item_karyawan2) {
                    FragmentTransaction fragmentTransaction= FM2.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView2, new DataKaryawanFragment()).commit();
                }
                if (item.getItemId()==R.id.nav_item_rating2){
                    FragmentTransaction fragmentTransaction1=FM2.beginTransaction();
                    fragmentTransaction1.replace(R.id.containerView2,new RatingFragment()).commit();
                }
                if (item.getItemId()==R.id.nav_item_home2){
                    FragmentTransaction fragmentTransaction1=FM2.beginTransaction();
                    fragmentTransaction1.replace(R.id.containerView2,new UserHomeFragment()).commit();
                }
                if (item.getItemId()==R.id.nav_item_primier2){
                    FragmentTransaction fragmentTransaction1=FM2.beginTransaction();
                    fragmentTransaction1.replace(R.id.containerView2,new DataPrimerFragment()).commit();
                }
                if (item.getItemId()== R.id.nav_item_histori2) {
                    FragmentTransaction fragmentTransaction= FM2.beginTransaction();
                    fragmentTransaction.replace(R.id.containerView2, new DataHistoryFragment()).commit();
                }
                if (item.getItemId()==R.id.nav_logout2){
                    startActivity(new Intent(UserActivity.this,MainActivity.class));
                }
                return false;
            }
        });
        Toolbar toolbar2 = (Toolbar)findViewById(R.id.toolbar2);
        ActionBarDrawerToggle toggle2= new ActionBarDrawerToggle(this,drawerLayout2,toolbar2,R.string.app_name,R.string.app_name);
        drawerLayout2.addDrawerListener(toggle2);
        toggle2.syncState();
    }
}
