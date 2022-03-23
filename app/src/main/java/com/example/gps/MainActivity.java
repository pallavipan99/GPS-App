package com.example.gps;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static java.lang.String.valueOf;
import static java.util.Locale.US;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION = 1;

    TextView lat, longit, location, dist, favLocation;
    double lati = 0.0, lon = 0.0, distance = 0.0;
    int i = -1;
    String longitude, latitude;
    LocationManager locationManager;
    Context mainContext;

    List<String> addressCollec = new ArrayList<>();
    List<Double> latitudeList = new ArrayList<>();
    List<Double> longitudeList = new ArrayList<>();
    List<Long> timeList = new ArrayList<>();
    List<Long> startTime = new ArrayList<>();
    List<Long> endTime = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainContext = this;
        lat = findViewById(R.id.lat);
        longit = findViewById(R.id.lon);
        location = findViewById(R.id.location);
        dist = findViewById(R.id.distance);
        favLocation = findViewById(R.id.favLocation);

        Geocoder address = new Geocoder(this, US);
        long start = SystemClock.elapsedRealtime();
        startTime.add(start);

        locationManager = (LocationManager) mainContext.getSystemService(Context.LOCATION_SERVICE);
        dist.setText("Distance: " + distance + " meters");

        // Request permissions
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.INTERNET
            }, REQUEST_LOCATION);
        }

        Location crntLocation = new Location("crntlocation");
        Location newLocation = new Location("newlocation");

        LocationListener locationListener = new LocationListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onLocationChanged(@NonNull Location locationchange) {
                longitude = valueOf(locationchange.getLongitude());
                latitude = valueOf(locationchange.getLatitude());
                lati = Double.parseDouble(latitude);
                lon = Double.parseDouble(longitude);

                lat.setText("Latitude: " + lati);
                longit.setText("Longitude: " + lon);

                List<Address> addressList;
                String loc = "";
                try {
                    addressList = address.getFromLocation(lati, lon, 1);
                    if (!addressList.isEmpty()) loc = addressList.get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                location.setText("Address: " + loc);
                addressCollec.add(loc);
                latitudeList.add(lati);
                longitudeList.add(lon);
                i++;

                if (latitudeList.size() > 1 && longitudeList.size() > 1) {
                    if (latitudeList.get(i).equals(latitudeList.get(i - 1)) &&
                            longitudeList.get(i).equals(longitudeList.get(i - 1))) {
                        latitudeList.remove(i);
                        longitudeList.remove(i);
                        addressCollec.remove(addressCollec.size() - 1);
                        i--;
                    } else {
                        crntLocation.setLatitude(latitudeList.get(i - 1));
                        crntLocation.setLongitude(longitudeList.get(i - 1));
                        newLocation.setLatitude(latitudeList.get(i));
                        newLocation.setLongitude(longitudeList.get(i));

                        distance += crntLocation.distanceTo(newLocation);
                        dist.setText("Distance: " + distance + " meters");

                        long stop = SystemClock.elapsedRealtime();
                        long tStart = startTime.get(startTime.size() - 1);
                        long elapsedMillis = stop - tStart;
                        endTime.add(stop);
                        startTime.add(SystemClock.elapsedRealtime());

                        long sec = TimeUnit.MILLISECONDS.toSeconds(elapsedMillis);
                        timeList.add(sec);

                        long temp = 0;
                        String favLoc = "";

                        for (int m = 0; m < addressCollec.size() - 1; m++) {
                            if (addressCollec.get(m).equals(loc)) {
                                long time = timeList.get(m);
                                favLoc = addressCollec.get(m);
                                addressCollec.remove(m);
                                timeList.remove(m);
                                sec += time;
                                timeList.set(timeList.size() - 1, sec);
                            }
                        }

                        for (int j = 0; j < timeList.size(); j++) {
                            if (timeList.get(j) > temp) {
                                temp = timeList.get(j);
                                favLoc = addressCollec.get(j);
                            }
                        }

                        favLocation.setText("FavLoc: " + favLoc + " sec: " + temp);
                    }
                }
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) { }
            @Override
            public void onProviderDisabled(@NonNull String provider) { }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) { }
        };

        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permissions granted
            return;
        }
    }
}
