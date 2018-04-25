package com.example.dinopc.atsimink;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Function {


    private static final String OPEN_WEATHER_MAP_URL =
            "http://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&units=metric";

    private static final String OPEN_WEATHER_MAP_API = "28352cd6a33346c0b8aa305813460433";

    public static String setWeatherIcon(int actualId, long sunrise, long sunset){
        int id = actualId / 100;
        String icon = "";
        if(actualId == 800){
            long currentTime = new Date().getTime();
            if(currentTime>=sunrise && currentTime<sunset) {
                icon = "&#xf00d;";
            } else {
                icon = "&#xf02e;";
            }
        } else {
            switch(id) {
                case 2 : icon = "&#xf01e;";
                    break;
                case 3 : icon = "&#xf01c;";
                    break;
                case 7 : icon = "&#xf014;";
                    break;
                case 8 : icon = "&#xf013;";
                    break;
                case 6 : icon = "&#xf01b;";
                    break;
                case 5 : icon = "&#xf019;";
                    break;
            }
        }
        return icon;
    }



    public interface AsyncResponse {

        void processFinish(String output1, String output2, String output3, String output4, String output5, String output6, String output7, String output8, String output9, String output10, String output11);
    }





    public static class placeIdTask extends AsyncTask<String, Void, JSONObject> {

        public AsyncResponse delegate = null;//Call back interface

        public placeIdTask(AsyncResponse asyncResponse) {
            delegate = asyncResponse;//Assigning call back interfacethrough constructor
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            JSONObject jsonWeather = null;
            try {
                jsonWeather = getWeatherJSON(params[0], params[1]);
            } catch (Exception e) {
                Log.d("Error", "Cannot process JSON results", e);
            }


            return jsonWeather;
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            try {
                if(json != null){
                    JSONObject detailsObj = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject mainObj = json.getJSONObject("main");
                    JSONObject windObj = json.getJSONObject("wind");
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");
                    JSONObject sysObj = json.getJSONObject("sys");
                    long dr = Long.valueOf(sysObj.getInt("sunrise"))*1000;
                    long ds = Long.valueOf(sysObj.getInt("sunset"))*1000;


                    String city = json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country");
                    String description = detailsObj.getString("description").toUpperCase(Locale.US);
                    String temperature = mainObj.getInt("temp")+ "°C";  //Shows integer temp
                    //String temperature = String.format("%.2f", main.getDouble("temp"))+ "°C";  //Shows float temp
                    String wind = windObj.getDouble("speed") + " m/s";
                    String humidity = mainObj.getInt("humidity") + "%";
                    String pressure = mainObj.getInt("pressure") + " hPa";
                    String updatedOn = "Last update: " + df.format(new Time(json.getLong("dt")*1000));
                    String sunrise = new SimpleDateFormat("HH:mm").format(dr);
                    String sunset = new SimpleDateFormat("HH:mm").format(ds);
                    String iconText = setWeatherIcon(detailsObj.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000);


                    delegate.processFinish(city, description, temperature, wind, humidity, pressure, sunrise, sunset, updatedOn, iconText, ""+ (json.getJSONObject("sys").getLong("sunrise") * 1000));
                }
            } catch (JSONException e) {
                //Log.e(LOG_TAG, "Cannot process JSON results", e);
            }



        }
    }






    public static JSONObject getWeatherJSON(String lat, String lon){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_URL, lat, lon));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key", OPEN_WEATHER_MAP_API);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }




}
