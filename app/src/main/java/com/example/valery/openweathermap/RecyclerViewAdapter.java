package com.example.valery.openweathermap;

import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.example.valery.openweathermap.entities.WeatherList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valery Garaev on June 09, 2018.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView date;
        TextView temperature;
        TextView wind_speed;
        TextView pressure;
        TextView humidity;
        TextView weather_condition;
        ProgressBar progressBar;
        ImageView condition_picture;

        ConstraintLayout parent_layout;

        public ViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            temperature = itemView.findViewById(R.id.temperature);
            wind_speed = itemView.findViewById(R.id.wind_speed);
            pressure = itemView.findViewById(R.id.pressure);
            humidity = itemView.findViewById(R.id.humidity);
            weather_condition = itemView.findViewById(R.id.weather_condition);
            progressBar = itemView.findViewById(R.id.progress_bar);
            condition_picture = itemView.findViewById(R.id.condition_picture);
            parent_layout = itemView.findViewById(R.id.parent_layout);
        }
    }

    private List<WeatherList> integers = new ArrayList<>();

    public void setIntegers(List<WeatherList> integers) {
        this.integers = integers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View forecastView = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_layout, parent, false);
        return new ViewHolder(forecastView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        WeatherList currentValue = integers.get(position);

        holder.date.setText(String.valueOf(currentValue.getDtTxt()));
    }

    @Override
    public int getItemCount() {
        return integers.size();
    }
}
