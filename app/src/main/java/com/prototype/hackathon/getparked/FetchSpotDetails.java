package com.prototype.hackathon.getparked;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by LUCIFER on 14-03-2018.
 */

public class FetchSpotDetails {
    private final static String TAG = "FetchResults";
    private static List<SpotDetails> spotDetails;

    public static List<SpotDetails> fetchData(String url_string) {
        Log.v(TAG,"fetchData() called");
        spotDetails = new ArrayList<>();
        URL url = createURL(url_string);
        String json = makeHTTPConnection(url);
        extractFromJson(json);
        return spotDetails;
    }

    private static void extractFromJson(String json) {
        if (json == null) {
            Log.d(TAG, "Empty Json File");
            return;
        }
        try {
            JSONObject root = new JSONObject(json);
            JSONObject manager = root.optJSONObject("manager");
            JSONObject gujarat = manager.optJSONObject("gujarat");
            JSONObject rajkot = gujarat.optJSONObject("rajkot");
            for(int i=1;;i++) {
                if(rajkot.optJSONObject("loc"+i)==null)
                    break;
                else {
                    JSONObject loc = rajkot.optJSONObject("loc"+i);
                    SpotDetails spotDetail = new SpotDetails();
                    spotDetail.setLatitude(loc.optDouble("lat"));
                    spotDetail.setLongitude(loc.optDouble("long"));
                    spotDetail.setAddress(loc.optString("name"));

                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception caught");
        }
    }

    private static URL createURL(String string) {
        URL url = null;
        try {
            url = new URL(string);
        } catch (MalformedURLException e) {
            Log.e(TAG, "Unable to create URL object", e);
        } finally {
            return url;
        }
    }

    private static String makeHTTPConnection(URL url) {
        String json = new String();
        if (url == null)
            return null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(10000);
            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            json = parseIntoJson(inputStream);
        } catch (IOException e) {

        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();
            if (inputStream != null)
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error in closing the inputStream", e);
                }
            return json;
        }
    }

    private static String parseIntoJson(InputStream inputStream) {
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader reader = new BufferedReader(inputStreamReader);
        try {
            line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            Log.e(TAG, "Unable to read from the Buffered Reader ", e);
        } finally {
            return stringBuilder.toString();
        }
    }
}
