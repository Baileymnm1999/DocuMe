package com.example.documeapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.Normalizer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.widget.ViewFlipper;
import android.view.ViewGroup.LayoutParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by user on 12/13/16.
 */
public class NewProject extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Setting up onClick popup

    private String mProjectTitle;
    private String mProjectGenre;
    private String mProjectDescription;
    public ArrayList<ProjectStep> mSteps;

    private Context mContext;
    private Button saveProjectButton;
    private Activity mActivity;
    private LinearLayout mLinearLayout;
    private Button mButton;
    private Button takePictureButton;
    private PopupWindow mPopupWindow;
    private ImageView mImageView;
    private Button selectPictureButton;
    public Uri uri;



    //Done setting up onClick

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSteps = new ArrayList<ProjectStep>();
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.new_project);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //SET POPUP FORM AND CLICK LISTENER

        // Get the application context
        mContext = getApplicationContext();

        // Get the activity
        mActivity = NewProject.this;

        // Get the widgets reference from XML layout
        mLinearLayout = (LinearLayout) findViewById(R.id.new_project_ll_form);
        mButton = (Button) findViewById(R.id.new_step_button);
        saveProjectButton = (Button) findViewById(R.id.save_project_button);

        final AlertDialog.Builder emptyTitleAlert = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Save Failed")
                .setMessage("You must include a title")
                .setNegativeButton("Okay", null);


        saveProjectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                mProjectTitle = ((EditText) findViewById(R.id.project_name)).getText().toString();
                mProjectGenre = ((Spinner) findViewById(R.id.genre_spinner)).getSelectedItem().toString();
                mProjectDescription = ((EditText) findViewById(R.id.project_description)).getText().toString();

                    if (mProjectTitle.isEmpty()){
                        emptyTitleAlert.show();
                        return;
                    }


                    String projectsFilename = getResources().getText(R.string.projects_file).toString();
                    FileInputStream inputStream;
                    StringBuffer fileContent = new StringBuffer("");
                    byte[] buffer = new byte[1024];
                    Log.d("SavedProject", "Attempting to read from " + projectsFilename);
                    int n = -1;
                    try {
                        inputStream = openFileInput(projectsFilename);
                        while ((n = inputStream.read(buffer)) != -1) {
                            fileContent.append(new String(buffer, 0, n));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("File Output", fileContent.toString());

                    JSONArray allProjects = new JSONArray();
                    if (!fileContent.toString().isEmpty()) {
                        try {
                            Log.d("{POPULATING ARRAY", allProjects.toString());
                            allProjects = new JSONArray(fileContent.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                    JSONObject JSONProject = new JSONObject();
                    try {
                        JSONProject.put("title", mProjectTitle);
                        JSONProject.put("genre", mProjectGenre);
                        JSONProject.put("description", mProjectDescription);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    JSONArray JSONStep = new JSONArray();
                    for (ProjectStep step : mSteps) {
                        JSONObject stepObj = new JSONObject();
                        try {
                            stepObj.put("title", step.getTitle());
                            stepObj.put("description", step.getDescription());
                            stepObj.put("stepPicture", step.getStepPicture());
                            JSONStep.put(stepObj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    try {
                        JSONProject.put("Steps", JSONStep);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d("JSON OUTPUT", JSONProject.toString());
                    FileOutputStream outputStream;
                    allProjects.put(JSONProject);
                    Log.d("SavedProject", "Attempting to write to " + projectsFilename);
                    try {
                        outputStream = openFileOutput(projectsFilename, Context.MODE_PRIVATE);
                        outputStream.write(allProjects.toString().getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("ARRAY OUTPUT", allProjects.toString());
                    Log.d("FILE OUTPUT", fileContent.toString());
                    try {
                        inputStream = openFileInput(projectsFilename);
                        while ((n = inputStream.read(buffer)) != -1) {
                            fileContent.append(new String(buffer, 0, n));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.d("ARRAY OUTPUT", allProjects.toString());
                    Log.d("FILE OUTPUT", fileContent.toString());
                    finish();





            }
        });



        // Set a click listener for the text view
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize a new instance of LayoutInflater service
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);

                // Inflate the custom layout/view
                View customView = inflater.inflate(R.layout.new_step_popup,null);

                /*
                    public PopupWindow (View contentView, int width, int height)
                        Create a new non focusable popup window which can display the contentView.
                        The dimension of the window must be passed to this constructor.

                        The popup does not provide any background. This should be handled by
                        the content view.

                    Parameters
                        contentView : the popup's content
                        width : the popup's width
                        height : the popup's height
                */
                // Initialize a new instance of popup window
                mPopupWindow = new PopupWindow(
                        customView,
                        LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT
                );

                // Set an elevation value for popup window
                // Call requires API level 21
                if(Build.VERSION.SDK_INT>=21){
                    mPopupWindow.setElevation(5.0f);
                }

                takePictureButton = (Button) customView.findViewById(R.id.take_picture_button);
                selectPictureButton = (Button) customView.findViewById(R.id.choose_picture_button);
                mImageView = (ImageView) customView.findViewById(R.id.step_photo_container);

                takePictureButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dispatchTakePictureIntent();

                    }
                });
                selectPictureButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dispatchSelectPictureIntent();

                    }
                });

                // Get a reference for the custom view close button
                Button save_button = (Button) customView.findViewById(R.id.save_step_button);

                // Set a click listener for the popup window close button
                save_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Dismiss the popup window

                        Uri stepPicture = null;
                        String title = "";

                        title =  ((EditText) mPopupWindow.getContentView().findViewById(R.id.step_title)).getText().toString();
                        String description =  ((EditText) mPopupWindow.getContentView().findViewById(R.id.step_description)).getText().toString();
                        stepPicture = uri;

                        if (!title.isEmpty()){
                            Log.d("TITLE", title);
                        }else{
                            Log.d("TITLE", "Empty Title");
                            emptyTitleAlert.show();
                            return;
                        }
                        if (description != null){
                            Log.d("DESCRIPTION", description);
                        }
                        if (stepPicture != null) {
                            Log.d("IMAGE URI", stepPicture.toString());
                        }

                        ProjectStep step = new ProjectStep(title, description, stepPicture);
                        mSteps.add(step);

                        mPopupWindow.dismiss();
                    }
                });

                /*
                    public void showAtLocation (View parent, int gravity, int x, int y)
                        Display the content view in a popup window at the specified location. If the
                        popup window cannot fit on screen, it will be clipped.
                        Learn WindowManager.LayoutParams for more information on how gravity and the x
                        and y parameters are related. Specifying a gravity of NO_GRAVITY is similar
                        to specifying Gravity.LEFT | Gravity.TOP.

                    Parameters
                        parent : a parent view to get the getWindowToken() token from
                        gravity : the gravity which controls the placement of the popup window
                        x : the popup's x location offset
                        y : the popup's y location offset
                */
                // Finally, show the popup window at the center location of root relative layout
                mPopupWindow.setFocusable(true);
                mPopupWindow.showAtLocation(mLinearLayout, Gravity.CENTER_HORIZONTAL,0,0);
            }
        });

    }

    //Create int to request camera to take a picture
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
    static final int PICK_IMAGE_REQUEST = 2;
    private void dispatchSelectPictureIntent() {
        Intent intent = new Intent();
        // Show only images, no videos or anything else
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // Always show the chooser (if there are multiple options available)
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    //Retrieve thumbnail bitmap
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mImageView.setImageBitmap(imageBitmap);
            uri =data.getData();
        }else if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            uri =data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                mImageView.setImageBitmap(rotateImage(bitmap, 90));
            }catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Invalid File",Toast.LENGTH_SHORT);
            }
        }else{
            Toast.makeText(getApplicationContext(),"An error occurred",Toast.LENGTH_SHORT);
        }
    }

    //Rotate an image 90 degrees clockwise
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Log.d("docume", Integer.toString(source.getHeight()));
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    //Function to create a file documenting photo filename and location
//    String mCurrentPhotoPath;
//
//    private File createImageFile() throws IOException {
//        // Create an image file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(
//                imageFileName,  /* prefix */
//                ".jpg",         /* suffix */
//                storageDir      /* directory */
//        );
//
//        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = image.getAbsolutePath();
//        return image;
//    }

/*    static final int REQUEST_TAKE_PHOTO = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                //...
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }*/

    @Override
    public void onBackPressed() {
//        View displayedChild = newProjectViewFlipper.getCurrentView();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
            else {
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Quit Project?")
                        .setMessage("Are you sure you want to quit? If you leave, this project will not be saved.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();
                //super.onBackPressed();
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

        } else if (id == R.id.settings) {

        } else if (id == R.id.logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}

