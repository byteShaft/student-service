package com.byteshaft.studentservice;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.byteshaft.studentservice.fragment.ScheduleFragment;
import com.byteshaft.studentservice.fragment.CommunicateFragment;
import com.byteshaft.studentservice.fragment.ContactsFragment;
import com.byteshaft.studentservice.fragment.DeanFragment;
import com.byteshaft.studentservice.fragment.SocialMediaFragment;
import com.byteshaft.studentservice.fragment.TrackFragment;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private Class fragmentClass;
    private Fragment fragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (AppGlobals.isVirgin()) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
        android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.commit();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        setupDrawerContent(navigationView);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                selectDrawerItem(item);
                return true;
            }
        });
    }

    private void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.communicate_fragment:
                fragmentClass = CommunicateFragment.class;
                break;
            case R.id.track_fragment:
                fragmentClass = TrackFragment.class;
                break;
            case R.id.chat_fragment:
                fragmentClass = ScheduleFragment.class;
                break;
            case R.id.dean_fragment:
                fragmentClass = DeanFragment.class;
                break;
            case R.id.phonebook_fragment:
                fragmentClass = ContactsFragment.class;
                break;
            case R.id.social_media_fragment:
                fragmentClass = SocialMediaFragment.class;
                break;
            default:
                fragmentClass = CommunicateFragment.class;
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.container_main, fragment).commit();
            }
        }, 340);

        menuItem.setChecked(true);
        setTitle(menuItem.getTitle());
        drawer.closeDrawers();
    }
}
