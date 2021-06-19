package com.hj.homecleanproject;

public class MyWork {

    int resID; //CardView에 사용될 Image Resource
    String content; // CardView에 사용될 TextView

    public MyWork(int resID, String content){
        this.resID = resID;
        this.content = content;
    }

    public int getResID() {
        return resID;
    }

    public void setResID(int resID) {
        this.resID = resID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
