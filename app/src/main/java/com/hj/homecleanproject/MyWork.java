package com.hj.homecleanproject;

public class MyWork {

    int resID; //CardView에 사용될 Image Resource
    String content; // CardView에 사용될 TextView

    byte[] encodeResID; //Bitmap을 통해 image Resource사용


    public MyWork(int resID, String content){
        this.resID = resID;
        this.content = content;
    }

    public MyWork(byte[] encodeResID, String content){
        this.encodeResID = encodeResID;
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

    public byte[] getEncodeResID() {
        return encodeResID;
    }

    public void setEncodeResID(byte[] encodeResID) {
        this.encodeResID = encodeResID;
    }
}
