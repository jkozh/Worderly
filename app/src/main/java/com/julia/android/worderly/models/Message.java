package com.julia.android.worderly.models;

public class Message {

    private String id;
    private String text;
    private String name;
    private String photoUrl;
    private String timeStamp;

    public Message() {
    }

    public Message(String text, String name, String photoUrl, String timeStamp) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

}
