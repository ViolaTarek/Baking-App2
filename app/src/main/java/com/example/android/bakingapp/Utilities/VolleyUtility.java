package com.example.android.bakingapp.Utilities;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;



public class VolleyUtility {
    private static Context mContext;

    private static VolleyUtility mVolleyUtility;
    private RequestQueue mRequestQue;

    private VolleyUtility(Context context){
        mContext = context;
        mRequestQue = getRequestQue();
    }

    public static synchronized VolleyUtility getInstance (Context context){
        if(mVolleyUtility == null){
            mVolleyUtility = new VolleyUtility(context);
        }
        return mVolleyUtility;
    }

    public RequestQueue getRequestQue(){
        if (mRequestQue == null){
            mRequestQue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQue;
    }

    public <T> void addToRequestQueue(Request<T> req){
        getRequestQue().add(req);
    }

    public void cancelPendingRequests (Object tag){
        if (mRequestQue != null){
            mRequestQue.cancelAll(tag);
        }
    }
}
