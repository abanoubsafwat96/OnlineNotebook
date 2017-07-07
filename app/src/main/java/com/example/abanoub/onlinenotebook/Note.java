package com.example.abanoub.onlinenotebook;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Abanoub on 2017-06-27.
 */

public class Note implements Serializable{
    public String title;
    public String note;
    public String pushId;

    public Note() {
        // Default constructor required to be empty for calls to DataSnapshot.getValue(User.class) (for using firebase)
    }

    public Note(String title,String note,String pushId){
        this.title=title;
        this.note=note;
        this.pushId=pushId;
    }

    public Map<String,Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("title", title);
        result.put("note", note);
        result.put("pushId", pushId);

        return result;
    }
}
