package com.brit.britaniaapp.base;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.brit.britaniaapp.R;
import com.brit.britaniaapp.ReportsFilterActivity;
import com.brit.britaniaapp.UserFilterActivity;
import com.google.android.material.navigation.NavigationView;

public class BaseNavActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private NavigationView navigationView;
    ContentFrameLayout contentFrameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Toolbar initialization
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Nav Drawer initialization
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        // Nav item click listener init
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        View header = navigationView.getHeaderView(0);
//        TextView tvUserName = header.findViewById(R.id.baseNavActivity_tvUserName);
//        TextView tvEmail = header.findViewById(R.id.baseNavActivity_tvEmail);
    }

    /**
     * On back pressed, check whether the drawer layout is open
     * If open, close drawer else go back normally
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        finish();
    }

    /**
     * Inflate all the menu inside navigation drawer
     *
     * @param menu Menu Menu object which contains all the menu
     * @return Boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Top bar action selection
     *
     * @param item MenuItem Item which got selected
     * @return Boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.action_loads) {
            navigateAssignedLoads();
            return true;
        } */

        return super.onOptionsItemSelected(item);
    }


    @SuppressLint("RtlHardcoded")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        int id = item.getItemId();
        if (id == R.id.nav_reports) {
            drawer.closeDrawer(Gravity.LEFT);
            startActivity(new Intent(getApplicationContext(), ReportsFilterActivity.class));
        }else if (id == R.id.nav_user_reports) {
            drawer.closeDrawer(Gravity.LEFT);
            startActivity(new Intent(getApplicationContext(), UserFilterActivity.class));
        }
        return true;
    }



}
