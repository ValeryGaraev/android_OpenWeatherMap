package com.example.valery.openweathermap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;

/**
 * Created by Valery Garaev on April 07, 2018.
 */

public class MainActivity extends AppCompatActivity {

    private EditText city;
    private TextView temperature;
    private TextView wind_speed;
    private TextView pressure;
    private TextView humidity;
    private TextView weather_condition;

    private ImageView condition_picture;

    private ProgressBar progress_bar;
    private Button get_button;

    public Toast toast;

    @SuppressLint("ShowToast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        city = findViewById(R.id.city_field);
        temperature = findViewById(R.id.temperature);
        wind_speed = findViewById(R.id.wind_speed);
        pressure = findViewById(R.id.pressure);
        humidity = findViewById(R.id.humidity);
        weather_condition = findViewById(R.id.weather_condition);

        condition_picture = findViewById(R.id.condition_picture);

        progress_bar = findViewById(R.id.progressBar);
        get_button = findViewById(R.id.get_button);

        toast = Toast.makeText(getBaseContext(), "", Toast.LENGTH_SHORT);

        get_button.setClickable(true);
    }

    public void buttonClicked(View view) {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;

        boolean connected;

        connected = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;

        if (connected) {
            String city_string = city.getText().toString();
            new RequestWeather().execute(city_string);
        } else {
            toast.setText("Check your connection");
            toast.show();
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class RequestWeather extends AsyncTask<String, Void, WeatherData> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            get_button.setEnabled(false);
            get_button.setClickable(false);

            progress_bar.setVisibility(View.VISIBLE);
            condition_picture.setVisibility(View.INVISIBLE);

            toast.setText("Requesting weather");
            toast.show();
        }

        @Override
        protected WeatherData doInBackground(String... strings) {
            WeatherService service = new WeatherService();
            try {
                return service.getWeather(strings[0]);
            } catch (IOException e) {
                Log.i("REQUEST_WEATHER", e.getMessage());
            } catch (JSONException e) {
                Log.i("REQUEST_WEATHER", e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(WeatherData data) {
            super.onPostExecute(data);

            progress_bar.setVisibility(View.INVISIBLE);

            get_button.setEnabled(true);
            get_button.setClickable(true);

            if (data == null) {
                condition_picture.setVisibility(View.INVISIBLE);

                toast.setText("City not found");
                toast.show();

                temperature.setText(String.format("Temperature: %s", "null"));
                wind_speed.setText(String.format("Wind speed: %s", "null"));
                pressure.setText(String.format("Pressure: %s", "null"));
                weather_condition.setText(String.format("Weather condition: %s", "null"));
                humidity.setText(String.format("Humidity: %s", "null"));
            } else {
                toast.setText("Weather received");
                toast.show();

                condition_picture.setVisibility(View.VISIBLE);

                // String url = "http://openweathermap.org/img/w/" + data.condition_picture_id + ".png";
                // Picasso.get().load(url).into(condition_picture);

                String name = "p" + data.condition_picture_id;
                int image = getResources().getIdentifier(name, "drawable", getPackageName());
                condition_picture.setImageResource(image);

                temperature.setText(String.format("Temperature: %s °C", String.valueOf(data.temperature)));
                wind_speed.setText(String.format("Wind speed: %s meter/sec", String.valueOf(data.wind_speed)));
                pressure.setText(String.format("Pressure: %s mm Hg", String.valueOf(Math.round(data.pressure / 1.33322387415))));
                weather_condition.setText(String.format("Weather condition: %s", data.weather_condition));
                humidity.setText(String.format("Humidity: %s%%", String.valueOf(data.humidity)));
            }
        }
    }
}