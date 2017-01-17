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
import com.julia.android.worderly.ui.game.presenter.GamePresenter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordRequest {

    public WordRequest(RequestQueue requestQueue, final GamePresenter presenter) {

        final String LOG_TAG = WordRequest.class.getSimpleName();

        GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        String random = "true";
        int letters = 7;
        String partOfSpeech = "noun";
        String hasDefinition = "hasDefinition";
        //String lettersPattern = "^[^\\d\\s]+$\"";

        // Construct the URL for the WordsApi query
        // Possible parameters are available at WordsApi page, at
        // https://www.wordsapi.com/docs#search
        final String WORDS_API_BASE_URL = "https://wordsapiv1.p.mashape.com/words";
        final String RANDOM_PARAM = "random";
        final String LETTERS_PARAM = "letters";
        final String PART_OF_SPEECH_PARAM = "partOfSpeech";
        final String HAS_DETAILS_PARAM = "hasDetails";
        //final String LETTERS_PATTERN_PARAM = "lettersPattern";

        String uri = Uri.parse(WORDS_API_BASE_URL).buildUpon()
                .appendQueryParameter(RANDOM_PARAM, random)
                .appendQueryParameter(LETTERS_PARAM, Integer.toString(letters))
                .appendQueryParameter(PART_OF_SPEECH_PARAM, partOfSpeech)
                .appendQueryParameter(HAS_DETAILS_PARAM, hasDefinition)
                //.appendQueryParameter(LETTERS_PATTERN_PARAM, lettersPattern)
                .build().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("VolleyResponse", "response: " + response);

                Word word = gson.fromJson(response, Word.class);

                List<Result> results = word.getResults();

                Log.i(LOG_TAG, "word: " + word.getWord());

                for (Result result : results) {
                    Log.i(LOG_TAG, "definition: " + result.getDefinition());
                }

                presenter.setWord(word.getWord());

            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {

                    String json = new String(networkResponse.data);

                    try{
                        JSONObject obj = new JSONObject(json);
                        json = obj.getString("message");
                        Log.e("VolleyError",
                                "Status code: " + Integer.toString(networkResponse.statusCode)
                                        + "; " + "Message: " + json);

                    } catch(JSONException e){
                        e.printStackTrace();
                    }

                    switch (networkResponse.statusCode) {
                        case 400:
                            Log.e("VolleyError", "Message: "
                                    + "Bad Request - Often missing a required parameter, " +
                                    "or a parameter was not the right type");
                            break;
                        case 401:
                        case 403:
                            Log.e("VolleyError", "Message: "
                                    + "Unauthorized - No access token, or it isn't valid");
                            break;
                        case 404:
                            Log.e("VolleyError", "Message: "
                                    + "Not Found - The requested item doesn't exist");
                            break;
                        case 429:
                            Log.e("VolleyError", "Message: "
                                    + "Too Many Requests - Rate limit exceeded");
                            break;
                        case 500:
                            Log.e("VolleyError", "Message: "
                                    + "Server Error - Something went wrong with Words API");
                            break;
                    }
                }
            }

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("X-Mashape-Key", BuildConfig.WORDS_API_KEY);
                params.put("Accept", "text/plain");
                return params;
            }

        };

        requestQueue.add(stringRequest);
    }

}
