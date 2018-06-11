package com.example.valery.openweathermap;


import com.example.valery.openweathermap.entities.ReceivedWeather;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Valery Garaev on April 07, 2018.
 */

class WeatherService {

    private JSONObject fetchData(String city, boolean check_activity) throws IOException, JSONException {

        String query;

        if (!check_activity) {
            query = String.format
                    ("http://api.openweathermap.org/data/2.5/weather?q=%s&APPID=9f1c6da33186cff18e6aa50ac4532cc0&units=metric", city);
        } else {
            query = String.format
                    ("http://api.openweathermap.org/data/2.5/forecast?q=%s&APPID=9f1c6da33186cff18e6aa50ac4532cc0&units=metric", city);
        }

        URL url = new URL(query);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) connection.disconnect();

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return new JSONObject(response.toString());

    }

    private Object parseData(JSONObject json, boolean check_activity) {

        if (!check_activity) {

            WeatherData weather = new WeatherData();

            try {
                weather.city = json.getString("name");
                weather.temperature = json.getJSONObject("main").getDouble("temp");
                weather.pressure = json.getJSONObject("main").getInt("pressure");
                weather.humidity = json.getJSONObject("main").getInt("humidity");
                weather.weather_condition = json.getJSONArray("weather").getJSONObject(Integer.parseInt("0")).getString("description");
                weather.condition_picture_id = json.getJSONArray("weather").getJSONObject(Integer.parseInt("0")).getString("icon");
                weather.wind_speed = json.getJSONObject("wind").getInt("speed");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return weather;
        } else {
            ReceivedWeather receivedWeather = new Gson().fromJson(json.toString(), ReceivedWeather.class);
            return receivedWeather;
        }
    }

    public Object getWeather(String city, boolean is_weekly) throws IOException, JSONException {

        JSONObject json = fetchData(city, is_weekly);
        return parseData(json, is_weekly);

    }
}