package com.example.dinopc.atsimink;

import android.Manifest;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Typeface;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dinopc.atsimink.Settings.AboutUs;
import com.example.dinopc.atsimink.WeekDays.Current;
import com.example.dinopc.atsimink.WeekDays.Friday;
import com.example.dinopc.atsimink.WeekDays.Monday;
import com.example.dinopc.atsimink.WeekDays.Saturday;
import com.example.dinopc.atsimink.WeekDays.Sunday;
import com.example.dinopc.atsimink.WeekDays.Thursday;
import com.example.dinopc.atsimink.WeekDays.Tuesday;
import com.example.dinopc.atsimink.WeekDays.Wednesday;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    TextView cityField, detailsField, currentTemperatureField, wind_field, humidity_field, pressure_field, sunrise_field, sunset_field, weatherIcon, updatedField;

    Typeface weatherFont;

    SwipeRefreshLayout pullToRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer);
        mToggle=new ActionBarDrawerToggle(this, mDrawerLayout,R.string.open,R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView=(NavigationView)findViewById(R.id.Navigation_v);
        navigationView.setNavigationItemSelectedListener(this);

        Toast.makeText(getApplicationContext(), "This is the default location!", Toast.LENGTH_SHORT).show();

        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");

        cityField = (TextView)findViewById(R.id.city_field);
        updatedField = (TextView)findViewById(R.id.updated_field);
        detailsField = (TextView)findViewById(R.id.details_field);
        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        wind_field = (TextView) findViewById(R.id.wind_field);
        humidity_field = (TextView)findViewById(R.id.humidity_field);
        pressure_field = (TextView)findViewById(R.id.pressure_field);
        sunrise_field = (TextView) findViewById(R.id.sunrise_field);
        sunset_field = (TextView) findViewById(R.id.sunset_field);
        weatherIcon = (TextView)findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);


        Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String wind_speed, String weather_humidity, String weather_pressure, String weather_sunrise, String weather_sunset, String weather_updatedOn, String weather_iconText, String sun_rise) {

                cityField.setText(weather_city);
                updatedField.setText(weather_updatedOn);
                detailsField.setText(weather_description);
                currentTemperatureField.setText(weather_temperature);
                wind_field.setText("Wind Speed: "+wind_speed);
                humidity_field.setText("Humidity: "+weather_humidity);
                pressure_field.setText("Pressure: "+weather_pressure);
                sunrise_field.setText("Sunrise: "+weather_sunrise);
                sunset_field.setText("Sunset: "+weather_sunset);
                weatherIcon.setText(Html.fromHtml(weather_iconText));


            }
        });
        asyncTask.execute("47.0096", "28.8293");


    }

    @Override
    protected void onStart() {
        super.onStart();
        Button gps_button = (Button) findViewById(R.id.action_gps);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET },123);
        gps_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                GPS g = new GPS(getApplicationContext());
                Location location = g.getLocation();
                if (location != null){
                    double lat = location.getLatitude();
                    double lon = location.getLongitude();
                    Toast.makeText(getApplicationContext(), "Your coordinates are: "+"\n"+"Latitude: "+lat+"\n"+"Longitude: "+lon, Toast.LENGTH_SHORT).show();
                    String Latitude = String.valueOf(lat);
                    String Longitude = String.valueOf(lon);


                    weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");

                    cityField = (TextView)findViewById(R.id.city_field);
                    updatedField = (TextView)findViewById(R.id.updated_field);
                    detailsField = (TextView)findViewById(R.id.details_field);
                    currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
                    wind_field = (TextView) findViewById(R.id.wind_field);
                    humidity_field = (TextView)findViewById(R.id.humidity_field);
                    pressure_field = (TextView)findViewById(R.id.pressure_field);
                    sunrise_field = (TextView) findViewById(R.id.sunrise_field);
                    sunset_field = (TextView) findViewById(R.id.sunset_field);
                    weatherIcon = (TextView)findViewById(R.id.weather_icon);
                    weatherIcon.setTypeface(weatherFont);


                    Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
                        public void processFinish(String weather_city, String weather_description, String weather_temperature, String wind_speed, String weather_humidity, String weather_pressure, String weather_sunrise, String weather_sunset, String weather_updatedOn, String weather_iconText, String sun_rise) {

                            cityField.setText(weather_city);
                            updatedField.setText(weather_updatedOn);
                            detailsField.setText(weather_description);
                            currentTemperatureField.setText(weather_temperature);
                            wind_field.setText("Wind Speed: "+wind_speed);
                            humidity_field.setText("Humidity: "+weather_humidity);
                            pressure_field.setText("Pressure: "+weather_pressure);
                            sunrise_field.setText("Sunrise: "+weather_sunrise);
                            sunset_field.setText("Sunset: "+weather_sunset);
                            weatherIcon.setText(Html.fromHtml(weather_iconText));


                        }
                    });
                    asyncTask.execute(Latitude, Longitude); //  asyncTask.execute("Latitude", "Longitude")

                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //SEARCH AND SETTINGS MENU
        switch(item.getItemId()) {
            case R.id.action_search:

                Toast.makeText(this,"Item Search selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_settings:

                Toast.makeText(this,"Item Settings selected", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.action_about_us:

                AboutUs aboutUs = new AboutUs();
                android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, aboutUs);
                fragmentTransaction.commit();
        }
        //HAMBURGER
        if(mToggle.onOptionsItemSelected(item)){

            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();



        if(id == R.id.Current)
        {
            Current current = new Current();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, current);
            fragmentTransaction.commit();
            mDrawerLayout.closeDrawers();
        }
        else if(id == R.id.Monday)
        {
            Monday monday = new Monday();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, monday);
            fragmentTransaction.commit();
            mDrawerLayout.closeDrawers();
        }
        else if(id == R.id.Tuesday)
        {
            Tuesday tuesday = new Tuesday();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, tuesday);
            fragmentTransaction.commit();
            mDrawerLayout.closeDrawers();
        }
        else if(id == R.id.Wednesday)
        {
            Wednesday wednesday = new Wednesday();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, wednesday);
            fragmentTransaction.commit();
            mDrawerLayout.closeDrawers();
        }
        else if(id == R.id.Thursday)
        {
            Thursday thursday = new Thursday();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, thursday);
            fragmentTransaction.commit();
            mDrawerLayout.closeDrawers();
        }
        else if(id == R.id.Friday)
        {
            Friday friday = new Friday();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, friday);
            fragmentTransaction.commit();
            mDrawerLayout.closeDrawers();
        }
        else if(id == R.id.Saturday)
        {
            Saturday saturday = new Saturday();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, saturday);
            fragmentTransaction.commit();
            mDrawerLayout.closeDrawers();
        }
        else if(id == R.id.Sunday)
        {
            Sunday sunday = new Sunday();
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, sunday);
            fragmentTransaction.commit();
            mDrawerLayout.closeDrawers();
        }

        return true;
    }
}
