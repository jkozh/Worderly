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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import timber.log.Timber;

import static com.julia.android.worderly.utils.Constants.ACCEPT_PARAM;
import static com.julia.android.worderly.utils.Constants.HAS_DEFINITION;
import static com.julia.android.worderly.utils.Constants.HAS_DETAILS_PARAM;
import static com.julia.android.worderly.utils.Constants.LETTERS_PARAM;
import static com.julia.android.worderly.utils.Constants.LETTERS_PATTERN_PARAM;
import static com.julia.android.worderly.utils.Constants.MASHAPE_KEY_PARAM;
import static com.julia.android.worderly.utils.Constants.MESSAGE;
import static com.julia.android.worderly.utils.Constants.NUMBER_OF_LETTERS;
import static com.julia.android.worderly.utils.Constants.PART_OF_SPEECH;
import static com.julia.android.worderly.utils.Constants.PART_OF_SPEECH_PARAM;
import static com.julia.android.worderly.utils.Constants.RANDOM;
import static com.julia.android.worderly.utils.Constants.RANDOM_PARAM;
import static com.julia.android.worderly.utils.Constants.TEXT_PLAIN;
import static com.julia.android.worderly.utils.Constants.WORDS_API_BASE_URL;
import static com.julia.android.worderly.utils.Constants.lettersPattern;


public class WordRequest {


    public WordRequest(RequestQueue requestQueue, final WordCallback callback) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        final Gson gson = gsonBuilder.create();

        String uri = getUri();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, uri,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        Timber.d("response: %s", response);
                        Word word = gson.fromJson(response, Word.class);
                        List<Result> results = word.getResults();
                        Timber.d("word: %s", word.getWord());
                        Timber.d("definition: %s", results.get(0).getDefinition());
                        callback.onSuccess(word.getWord(), results.get(0).getDefinition());
                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                if (networkResponse != null && networkResponse.data != null) {
                    String json = new String(networkResponse.data);
                    try{
                        JSONObject obj = new JSONObject(json);
                        json = obj.getString(MESSAGE);
                        Timber.e("Status code: %s", Integer.toString(networkResponse.statusCode));
                        Timber.e("Message: %s", json);
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
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
                Timber.e("Bad Request - Often missing a required parameter, "
                        + "or a parameter was not the right type");
                break;
            case 401:
            case 403:
                Timber.e("Unauthorized - No access token, or it isn't valid");
                break;
            case 404:
                Timber.e("Not Found - The requested item doesn't exist");
                break;
            case 429:
                Timber.e("Too Many Requests - Rate limit exceeded");
                break;
            case 500:
                Timber.e("Server Error - Something went wrong with Words API");
                break;
            default:
                Timber.e("Unknown error, status code: %s", statusCode);
        }
    }

    private String getUri() {
        return Uri.parse(WORDS_API_BASE_URL).buildUpon()
                .appendQueryParameter(RANDOM_PARAM, RANDOM)
                .appendQueryParameter(LETTERS_PARAM, Integer.toString(NUMBER_OF_LETTERS))
                .appendQueryParameter(PART_OF_SPEECH_PARAM, PART_OF_SPEECH)
                .appendQueryParameter(HAS_DETAILS_PARAM, HAS_DEFINITION)
                .appendQueryParameter(LETTERS_PATTERN_PARAM, lettersPattern)
                .build().toString();
    }
}