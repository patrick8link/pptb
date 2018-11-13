package com.advertisement.online.oadv.Model;

import com.google.firebase.database.Exclude;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Post {
    public String userID;
    public String category;
    public String region;
    public String desc;
    public int viewCount;

    public Post(){

    }

    public Post(String userID, String category, String region, String desc, int viewCount){
        this.userID = userID;
        this.category = category;
        this.region = region;
        this.desc = desc;
        this.viewCount = viewCount;
    }

    @Exclude
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("userID",userID);
        result.put("category", category);
        result.put("region",region);
        result.put("desc",desc);
        return result;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


}
