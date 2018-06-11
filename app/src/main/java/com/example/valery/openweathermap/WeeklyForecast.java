package com.example.valery.openweathermap;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import com.example.valery.openweathermap.entities.ReceivedWeather;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Valery Garaev on June 04, 2018.
 */

public class WeeklyForecast extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_forecast);

        Intent city = getIntent();
        String city_name = city.getStringExtra("city");

        boolean is_weekly = true;

        new RequestWeather().execute(city_name);
    }

    public void updateList(ReceivedWeather receivedWeather){

        mRecyclerView = (RecyclerView) findViewById(R.id.forecast_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new RecyclerViewAdapter();

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter.setIntegers(receivedWeather.getWeatherList());

        mRecyclerView.setAdapter(mAdapter);
    }

    @SuppressLint("StaticFieldLeak")
    private class RequestWeather extends AsyncTask<String, Void, ReceivedWeather> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ReceivedWeather doInBackground(String... strings) {
            WeatherService service = new WeatherService();
            try {
                return (ReceivedWeather) service.getWeather(strings[0], true);
            } catch (IOException e) {
                Log.i("REQUEST_WEATHER", e.getMessage());
            } catch (JSONException e) {
                Log.i("REQUEST_WEATHER", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(ReceivedWeather data) {
            super.onPostExecute(data);
            updateList(data);
        }
    }
}
