package com.julia.android.worderly.network;


import android.net.Uri;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.julia.android.worderly.BuildConfig;
import com.julia.android.worderly.model.Result;
import com.julia.android.worderly.model.Word;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.julia.android.worderly.utils.Constants.ACCEPT_PARAM;
import static com.julia.android.worderly.utils.Constants.MASHAPE_KEY_PARAM;
import static com.julia.android.worderly.utils.Constants.TEXT_PLAIN;
import static com.julia.android.worderly.utils.Constants.WORDS_API_BASE_URL;

public class CheckWordRequest {

    private final String TAG = CheckWordRequest.class.getSimpleName();

    public CheckWordRequest(RequestQueue requestQueue, String word, final CheckWordCallback callback) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        String uri = Uri.parse(WORDS_API_BASE_URL).buildUpon().appendPath(word).build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Log.d(TAG, "response: " + response);
                        Word word = gson.fromJson(response, Word.class);
                        List<Result> results = word.getResults();
                        if (results != null) {
                            Log.i(TAG, "word: " + word.getWord());
                            if (results.get(0) != null) {
                                Log.i(TAG, "definition: " + results.get(0).getDefinition());
                            } else {
                                callback.onFail();
                            }
                            callback.onSuccess(results.get(0).getDefinition());
                        } else {
                            callback.onFail();
                        }
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String json = new String(networkResponse.data);
                    if (networkResponse.statusCode == 404) {
                        callback.onFail();
                    }
//                    try{
//                        JSONObject obj = new JSONObject(json);
//                        json = obj.getString(MESSAGE);
//                        Log.e(TAG, "Status code: " + Integer.toString(networkResponse.statusCode));
//                        Log.e(TAG, "Message: " + json);
//                    } catch(JSONException e){
//                        e.printStackTrace();
//                    }
                    getNetworkResponseApi(networkResponse.statusCode);
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(MASHAPE_KEY_PARAM, BuildConfig.WORDS_API_KEY);
                params.put(ACCEPT_PARAM, TEXT_PLAIN);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void getNetworkResponseApi(int statusCode) {
        switch (statusCode) {
            case 400:
                Log.e(TAG, "Bad Request - Often missing a required parameter, "
                        + "or a parameter was not the right type");
                break;
            case 401:
            case 403:
                Log.e(TAG, "Unauthorized - No access token, or it isn't valid");
                break;
            case 404:
                Log.e(TAG, "Not Found - The requested item doesn't exist");
                break;
            case 429:
                Log.e(TAG, "Too Many Requests - Rate limit exceeded");
                break;
            case 500:
                Log.e(TAG, "Server Error - Something went wrong with Words API");
                break;
            default:
                Log.e(TAG, "Unknown error, status code: " + statusCode);
        }
    }

//    private String getUri() {
//        return Uri.parse(WORDS_API_BASE_URL).buildUpon()
//                .appendQueryParameter(RANDOM_PARAM, RANDOM)
//                .appendQueryParameter(LETTERS_PARAM, Integer.toString(NUMBER_OF_LETTERS))
//                .appendQueryParameter(PART_OF_SPEECH_PARAM, PART_OF_SPEECH)
//                .appendQueryParameter(HAS_DETAILS_PARAM, HAS_DEFINITION)
                //.appendQueryParameter(LETTERS_PATTERN_PARAM, lettersPattern)
//                .build().toString();
//    }

}
