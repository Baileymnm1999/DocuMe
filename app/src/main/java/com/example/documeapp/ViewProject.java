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
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.net.URI;
import java.util.ArrayList;


class ViewProject extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content view
        setContentView(R.layout.view_project);

        // Get the project data
        // TODO: some error checking to make sure we have the project
        JSONObject project = new JSONObject(getIntent().getString("project"));

        // Set up the header
        EditText p_name = (EditText)findViewById(R.id.p_name);
        EditText p_genre = (EditText)findViewById(R.id.p_genre);
        EditText p_description = (EditText)findViewById(R.id.p_description);

        p_name.setText(project.getString("title"));
        p_genre.setText(project.getString("genre"));
        p_description.setText(project.getString("description"));

        // Set up the project steps
        ListView step_list = (ListView)findViewById(R.id.p_steps);

        JSONArray p_steps = project.getJSONArray("Steps");
        for(int index = 0; index < p_steps.length(); index++)
        {
            JSONObject step = p_steps.getJSONObject(index);

        }



    }




}