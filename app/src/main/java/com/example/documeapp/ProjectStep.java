package com.example.documeapp;

import android.net.Uri;

import java.net.URI;

/**
 * Created by Bailey on 2/14/2017.
 */

public class ProjectStep {

    private String mTitle;
    private String mDescription;
    private Uri mStepPicture;

    public ProjectStep(String title, String description, Uri stepPicture) {
        this.mTitle = title;
        this.mDescription = description;
        this.mStepPicture = stepPicture;
    }
    public ProjectStep(){

    }

    public String getTitle(){
        return this.mTitle;
    }
    public String getDescription() {
        return this.mDescription;
    }
    public Uri getStepPicture() {
        return this.mStepPicture;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }
    public void setDescription(String description) {
        this.mDescription = description;
    }
    public void setStepPicture(Uri stepPicture) {
        this.mStepPicture = stepPicture;
    }
}
