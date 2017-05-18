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

public class StepAdapter extends BaseAdapter {

    private Context m_viewLocation;
    private JSONArray m_steps;
    private static LayoutInflater inflater = null;


    public StepAdapter(JSONArray p_steps, Context viewLocation){
        m_steps = p_steps;
        m_viewLocation = viewLocation;
        inflater = (LayoutInflater) m_viewLocation
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return m_steps.length();
    }

    @Override
    public Object getItem(int position) {
        try {
            return m_steps.get(position);
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
        View stepView = null;
        try {
            JSONObject step = (JSONObject) m_steps.get(position);
            String title = step.getString("title");
            String desciption = step.getString("description");

            stepView = inflater.inflate(R.layout.step_text_view, null);
            TextView titleView = (TextView) stepView.findViewById(R.id.title_text_view);
            titleView.setText(title);
            TextView descriptionView = (TextView) stepView.findViewById(R.id.description_text_view);
            descriptionView.setText(desciption);



        } catch (JSONException e) {
            e.printStackTrace();
        }


        return stepView;
    }
}
