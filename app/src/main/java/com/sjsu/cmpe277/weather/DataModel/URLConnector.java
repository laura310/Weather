package com.sjsu.cmpe277.weather.DataModel;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


/**
 * Created by laurazhou on 10/30/17.
 */

public class URLConnector {
    String urlStringBase;

    public URLConnector(String urlString) {
        urlStringBase = urlString;
    }

    public String getResponse(String addOnTail) {
        addOnTail = addOnTail.replaceAll("\\s+", "%20");
        String urlString = urlStringBase + addOnTail;

        URL url = null;
        BufferedReader br = null;
        String jsonText = "";
        try {
            url = new URL(urlString);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET"); // optional default is GET

            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            jsonText = readAll(br);

            Log.i("CONNECTION", "connected to url: " + urlString);

            br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            Log.i("Error", "Exception from con.setRequestMethod: " + e);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return jsonText;
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
