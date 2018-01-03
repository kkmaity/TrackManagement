package com.demo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.demo.Enum.AppMenu;
import com.demo.fragments.RootFragment;
import com.demo.services.LocationUpdateService;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static int ACTIVE_TAB_POSITION = 1;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startService(new Intent(MainActivity.this,LocationUpdateService.class));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                 if (id == R.id.bottom_menu_home) {
                     onMenuItemSelect(AppMenu.HOME);
                }else if (id == R.id.bottom_menu_attendence) {
                     onMenuItemSelect(AppMenu.ATTENDENCE);
                }else if (id == R.id.bottom_menu_work_entry) {
                     onMenuItemSelect(AppMenu.WORk_ENTRY);
                }else if (id == R.id.bottom_menu_leaves) {
                     onMenuItemSelect(AppMenu.LEAVES);
                }
                return true;
            }
        });

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (fragment == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.frame_container, getRootFragment(AppMenu.HOME),AppMenu.HOME.name()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame_container);
            if (fragment != null) {
                if (!fragment.getChildFragmentManager().popBackStackImmediate())
                    exitApplication();
            } else {
                exitApplication();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onMenuItemSelect(AppMenu appMenu){

        Fragment fragment = null;
        String tag = null;

        switch (appMenu){
            case HOME:
                tag = AppMenu.HOME.name();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (ACTIVE_TAB_POSITION == 1) {
                    fragment = null;
                }
                ACTIVE_TAB_POSITION = 1;
                if (fragment == null)
                    fragment = getRootFragment(AppMenu.HOME);
                break;
            case ATTENDENCE:
                tag = AppMenu.ATTENDENCE.name();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (ACTIVE_TAB_POSITION == 2) {
                    fragment = null;
                }
                ACTIVE_TAB_POSITION = 2;
                if (fragment == null)
                    fragment = getRootFragment(AppMenu.ATTENDENCE);
                break;
            case WORk_ENTRY:
                tag = AppMenu.WORk_ENTRY.name();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (ACTIVE_TAB_POSITION == 3) {
                    fragment = null;
                }
                ACTIVE_TAB_POSITION = 3;
                if (fragment == null)
                    fragment = getRootFragment(AppMenu.WORk_ENTRY);
                break;
            case LEAVES: tag = AppMenu.LEAVES.name();
                fragment = getSupportFragmentManager().findFragmentByTag(tag);
                if (ACTIVE_TAB_POSITION == 4) {
                    fragment = null;
                }
                ACTIVE_TAB_POSITION = 4;
                if (fragment == null)
                    fragment = getRootFragment(AppMenu.LEAVES);
                break;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment, tag).commit();
    }

    public Fragment getRootFragment(AppMenu appMenu){
        Fragment fragment = new RootFragment();
        Bundle bundle = new Bundle();
        switch (appMenu){
            case HOME:
                bundle.putString("appMenu",AppMenu.HOME.name());
                break;
            case ATTENDENCE:
                bundle.putString("appMenu",AppMenu.ATTENDENCE.name());
                break;
            case WORk_ENTRY:
                bundle.putString("appMenu",AppMenu.WORk_ENTRY.name());
                break;
            case LEAVES:
                bundle.putString("appMenu",AppMenu.LEAVES.name());
                break;
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    private void exitApplication() {
        if (ACTIVE_TAB_POSITION != 1) {
            onMenuItemSelect(AppMenu.HOME);
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.double_tap_to_exit, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }



}
