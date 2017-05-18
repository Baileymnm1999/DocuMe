package com.example.documeapp;

import android.content.ClipData;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.net.URI;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    protected void onResume() {
        super.onResume();
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
        loadUi();
    }


    //Parsing file for project data
    public void loadUi(){
        FileInputStream inputStream;
        String projectsFilename = getResources().getText(R.string.projects_file).toString();
        StringBuffer fileContent = new StringBuffer("");
        byte[] buffer = new byte[1024];
        Log.d("SavedProject", "Attempting to read from " + projectsFilename);
        int n = 0;
        try {
            inputStream = openFileInput(projectsFilename);
            while ((n = inputStream.read(buffer)) != -1) {
                fileContent.append(new String(buffer, 0, n));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("File Output", fileContent.toString());
        JSONArray allProjects = null ;

        if (!fileContent.toString().isEmpty()) {

            ViewStub stub = (ViewStub) findViewById(R.id.menu_stub);
            stub.setLayoutResource(R.layout.project_grid_layout);
            View inflated = stub.inflate();
            final GridView gridView = (GridView) findViewById(R.id.project_grid_view);
            //Button plusButton = (Button) findViewById(R.id.plus_button);

            try {
                allProjects = new JSONArray(fileContent.toString());
                JSONObject dummyPlusObject = new JSONObject();
                dummyPlusObject.put("title", "");
                allProjects.put(dummyPlusObject);
                Log.d("JSONPROJECT: ", allProjects.toString());
                //Log.d("JSONTitle!!", JSONProject.getString("title"));
                //Log.d("Project Title", JSONProject.getJSONObject("title").toString());
                //Log.d("Steps Array", JSONProject.getJSONArray("Steps").toString());
                JSONObject project;
                JsonAdapter projectAdapter = new JsonAdapter(allProjects, getApplicationContext());
                gridView.setAdapter(projectAdapter);
                
//		gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
  //                  @Override
    //                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      //                  if( position == gridView.getCount()-1){
        //                    Intent i = new Intent(MainActivity.this, NewProject.class);
     //                       startActivity(i);
     //                   }
      //              }
      //          });
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        // Check if this was the + button

                        if( position == gridView.getCount() - 1)
                        {
                            Intent i = new Intent(MainActivity.this, NewProject.class);
                            startActivity(i);
                        }
                        else {
                            // Start new activity from NewProject\
                            Object project = ((JsonAdapter)gridView.getAdapter()).getItem(position);
                            Intent project_activity = new Intent(MainActivity.this, ViewProject.class);
                            project_activity.putExtra("project", project.toString());
                            startActivity(project_activity);
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            ViewStub stub = (ViewStub) findViewById(R.id.menu_stub);
            stub.setLayoutResource(R.layout.content_main);
            View inflated = stub.inflate();
            Button plusBtn = (Button)findViewById(R.id.button);
            plusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(MainActivity.this, NewProject.class);
                    startActivity(i);
                }
            });


        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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

        } else if (id == R.id.settings) {

        } else if (id == R.id.logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
