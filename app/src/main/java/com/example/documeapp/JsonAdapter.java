package com.example.documeapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.Format;
import java.util.jar.JarInputStream;

/**
 * Created by Bailey on 4/11/2017.
 */

public class JsonAdapter extends BaseAdapter {

    private Context m_viewLocation;
    private JSONArray m_projects;
    private static LayoutInflater inflater = null;


    public JsonAdapter(JSONArray allProjects, Context viewLocation){
        m_projects = allProjects;
        m_viewLocation = viewLocation;
        inflater = (LayoutInflater) m_viewLocation
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return m_projects.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return m_projects.get(position);
        }catch (JSONException e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View tile = null;

//      if (project.getString("title").is)
//        if (){
//                tile = inflater.inflate(R.layout.plus_button, null);
//            TextView tileTitle = (TextView) tile.findViewById(R.id.plus_tile);
//            tileTitle.setText("+");
//
//        }else{
            try {
                JSONObject project = m_projects.getJSONObject(position);
                JSONObject dummyPlusButton = m_projects.getJSONObject(m_projects.length()-1);

//                if(project.getString("title").isEmpty()){
                if(project.getString("title").isEmpty()){
                    tile = inflater.inflate(R.layout.plus_button, null);
                    TextView tileTitle = (TextView) tile.findViewById(R.id.plus_tile);
                    tileTitle.setText("+");
                }else{
                    tile = convertView;
                    if (convertView == null)
                        tile = inflater.inflate(R.layout.tile, null);
                    TextView tileTitle = (TextView) tile.findViewById(R.id.tileTitle);
                    if (tileTitle != null) {
                        tileTitle.setText(project.getString("title"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
//        }

        return tile;
    }
}
