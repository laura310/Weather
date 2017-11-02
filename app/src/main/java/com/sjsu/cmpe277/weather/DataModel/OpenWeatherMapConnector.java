package com.sjsu.cmpe277.weather.DataModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by laurazhou on 10/30/17.
 */

public class OpenWeatherMapConnector {
    final static Logger logger = Logger.getLogger(OpenWeatherMapConnector.class);


    public String getCurWeatByCityName(String cityName) {
        String urlString = "http://api.openweathermap.org/data/2.5/weather?id=524901&APPID=b4631e5c54e1a3a9fdda89fca90d4114&q=" + cityName;

        URL url = null;
        BufferedReader br = null;
        String jsonText = "";
        try {
            url = new URL(urlString);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET"); // optional default is GET

            int responseCode = con.getResponseCode();
            logger.info("\nSending 'GET' request to URL : " + urlString);
            logger.info("Response Code : " + responseCode);

            br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            jsonText = readAll(br);

//            br.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            logger.error("Exception from con.setRequestMethod", e);
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

    public void getCurWeatByCityId() {

    }
}
