package com.example.documeapp;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileDescriptor;
import java.io.IOException;
import java.net.URI;
import java.text.Format;
import java.util.jar.JarInputStream;

import static com.example.documeapp.NewProject.rotateImage;

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

            if (step.getString("stepPicture") != null){


                Uri pictureUri = Uri.parse(step.getString("stepPicture"));
                ImageView stepImageView = (ImageView) stepView.findViewById(R.id.step_view_photo_container);
                //stepImageView.setImageURI(null);
                //stepImageView.setImageURI(pictureUri);

                /*try{
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(m_viewLocation.getContentResolver(), pictureUri);
                    stepImageView.setImageBitmap(bitmap);
                    //stepImageView.getLayoutParams().height = 500;

                }catch (IOException e){
                    e.printStackTrace();
                }*/
             /*   try{

                    ParcelFileDescriptor parcelFileDescriptor = m_viewLocation.getContentResolver().openFileDescriptor(pictureUri, "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    parcelFileDescriptor.close();

                    //Bitmap bitmap = MediaStore.Images.Media.getBitmap(m_viewLocation.getContentResolver(), pictureUri);
                    //stepImageView.setImageBitmap(bitmap);
                }catch (IOException e) {
                   e.printStackTrace();
                }
*/

            }
            //ImageView stepImageView = (ImageView) stepView.findViewById(R.id.step_view_photo_container);
            //stepImageView.setImageURI(pictureUri);




        } catch (JSONException e) {
            e.printStackTrace();
        }


        return stepView;
    }


}
