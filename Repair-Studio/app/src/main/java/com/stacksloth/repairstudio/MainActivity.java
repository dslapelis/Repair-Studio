package com.stacksloth.repairstudio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private View mHView;
    private SharedPreferences mSharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            onNavigationItemSelected(navigationView.getMenu().findItem(0));
        }

        mSharedPref = getSharedPreferences("Preferences", Context.MODE_PRIVATE);
        populateNav();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            this.moveTaskToBack(true);
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
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_dashboard) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentLayout,new DashboardFragment())
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_customers) {
            fragmentManager.beginTransaction()
                    .replace(R.id.fragmentLayout, new CustomersFragment())
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.nav_inventory) {

        } else if (id == R.id.nav_invoice) {

        } else if (id == R.id.nav_contact) {

        } else if (id == R.id.nav_logout) {

            SharedPreferences sharedPref = this.getSharedPreferences("Preferences", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            Toast.makeText(this, "You were successfully logged out.", Toast.LENGTH_SHORT).show();
            this.finish();

        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentLayout,new DashboardFragment())
                    .addToBackStack(null)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void populateNav()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mHView =  navigationView.getHeaderView(0);
        TextView hdNameView = (TextView) mHView.findViewById(R.id.nameView);
        TextView hdEmailView = (TextView) mHView.findViewById(R.id.emailView);
        TextView hdMembView = (TextView) mHView.findViewById(R.id.membershipView);
        hdNameView.setText(mSharedPref.getString("name", "DNE"));
        hdEmailView.setText(mSharedPref.getString("email", "DNE"));
        int membershipLevel = Integer.parseInt(mSharedPref.getString("membership", "-1"));
        if(membershipLevel == 0)
        {
            hdMembView.setText("Membership Level: BETA");
        }
        else if (membershipLevel == 1)
        {
            hdMembView.setText("Membership Level: Limited");
        }
        else if (membershipLevel == 2)
        {
            hdMembView.setText("Membership Level: Unlimited");
        }
    }
}
