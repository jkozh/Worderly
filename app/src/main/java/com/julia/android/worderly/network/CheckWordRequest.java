package com.julia.android.worderly.network;


import android.net.Uri;

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

import timber.log.Timber;

import static com.julia.android.worderly.utils.Constants.ACCEPT_PARAM;
import static com.julia.android.worderly.utils.Constants.MASHAPE_KEY_PARAM;
import static com.julia.android.worderly.utils.Constants.TEXT_PLAIN;
import static com.julia.android.worderly.utils.Constants.WORDS_API_BASE_URL;


public class CheckWordRequest {


    public CheckWordRequest(RequestQueue requestQueue, String word, final CheckWordCallback callback) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        String uri = Uri.parse(WORDS_API_BASE_URL).buildUpon().appendPath(word).build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {

                        Timber.d("response: %s", response);
                        Word word = gson.fromJson(response, Word.class);
                        List<Result> results = word.getResults();
                        if (results != null) {
                            Timber.d("word: %s", word.getWord());
                            if (results.get(0) != null) {
                                Timber.d("definition: %s", results.get(0).getDefinition());
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
                    if (networkResponse.statusCode == 404) {
                        callback.onFail();
                    }
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

}
