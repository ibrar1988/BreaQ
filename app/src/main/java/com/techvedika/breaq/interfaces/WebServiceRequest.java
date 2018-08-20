package com.techvedika.breaq.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by ibrar on 2/1/18.
 */

public interface WebServiceRequest {
    void onResposne(JSONObject response);
    void onErrorResponse(VolleyError error);
}
