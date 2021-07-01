package com.hj.homecleanproject;

import android.graphics.Bitmap;
import android.net.Uri;

import java.net.URL;

public class FamilyItem {
    String name;
    String family_group;
    String email;
    String position;
    Uri uri;

    public FamilyItem(String name, String family_group, String email,String position, Uri uri) {
        this.name = name;
        this.family_group = family_group;
        this.email = email;
        this.position = position;
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFamily_group() {
        return family_group;
    }

    public void setFamily_group(String family_group) {
        this.family_group = family_group;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


}
