package com.advertisement.online.oadv.Model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Post {
    public String userID;
    public String category;
    public String region;
    public String desc;

    public Post(String userID, String category, String region, String desc){
        this.userID = userID;
        this.category = category;
        this.region = region;
        this.desc = desc;
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


}
