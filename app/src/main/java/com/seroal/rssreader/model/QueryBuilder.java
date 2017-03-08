package com.seroal.rssreader.model;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;
/**
 * Created by rouhalavi on 07/03/2017.
 */

public class QueryBuilder {
    public static Map<String,String> buildQuery(){
        return new HashMap<>();
    }
    public static Map<String,String> buildQuery(String id,String ids,String tags,String tagMode){
        HashMap<String,String> map = new HashMap<>();
        if (id.length()>0) map.put("id",id);
        if (ids.length()>0) map.put("ids",ids);
        if (tags.length()>0) map.put("tags",tags);
        if (tagMode.length()>0) map.put("tagmode",tagMode);

        Log.d("Query", map.toString());
        return map;
    }
}
