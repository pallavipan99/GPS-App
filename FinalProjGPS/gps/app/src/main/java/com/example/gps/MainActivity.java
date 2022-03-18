package com.example.gps;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static android.location.Location.convert;

import static java.util.Locale.US;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity{
    private static final int REQUEST_LOCATION = 1;
    TextView lat;
    TextView longit;
    TextView location;
    TextView dist;
    TextView favLocation;
    double lati = 0.0;
    double lon = 0.0;
    double distance = 0.0;
    int i = -1;
    Location newLoc = null;
    String longitude ;
    String latitude;
    LocationManager locationManager;
    Context mainContext;
    List<String> addressCollec = new ArrayList<String>();
    List<Double>latitudeList = new ArrayList<Double>();
    List<Double>longitudeList = new ArrayList<Double>();
    List<Double>timeList = new ArrayList<Double>();
    private long mLastClickTime = 0;
    private long theUserCannotClickTime = 1000;

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
        locationManager = (LocationManager) mainContext.getSystemService(Context.LOCATION_SERVICE);
       /* long currentTime = SystemClock.elapsedRealtime();
        long elapsedTime = currentTime - mLastClickTime;
        if (elapsedTime < theUserCannotClickTime)
            return;
        mLastClickTime = currentTime;*/

        if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,  Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.INTERNET}, REQUEST_LOCATION);

            }

        }
        //https://stackoverflow.com/questions/31574615/android-how-to-convert-from-systemclock-elapsedrealtime-to-a-human-readable-cha 

        Location crntLocation=new Location("crntlocation");
        Location newLocation=new Location("newlocation");

        LocationListener locationListener = new LocationListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onLocationChanged(@NonNull Location locationchange) {
                long currentTime = SystemClock.elapsedRealtime();
                //Duration d = Duration.ofMillis( currentTime ) ;
                Duration d = Duration.ofMillis( 441_000L ) ;
//long millis = d.toMillis() ;  // Not the part, but the entire span of time in terms of milliseconds.

                longitude = String.valueOf(locationchange.getLongitude());
                latitude = String.valueOf(locationchange.getLatitude());
                lati =  new Double(latitude).doubleValue();
                lon = new  Double(longitude).doubleValue();
                lat.setText("Latitude: "+lati );
                longit.setText("Longitude: "+lon);
                String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
                List<Address>addressList;
                String loc = "";
                try {
                    addressList = address.getFromLocation(lati,lon,1);
                    if(addressList.size()>0)
                        loc = addressList.get(0).getAddressLine(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                location.setText("Address: " + loc);
                addressCollec.add(loc);
                latitudeList.add(lati);
                longitudeList.add(lon);
                i++;
                if(latitudeList.size()>1 && longitudeList.size()>1 ){
                    if((latitudeList.get(i).equals(latitudeList.get(i-1))) && (longitudeList.get(i).equals(longitudeList.get(i-1)))) {
                        latitudeList.remove(i);
                        longitudeList.remove(i);
                        i--;
                    }
                    else{
                        if(latitudeList.size()>1 && longitudeList.size()>1){
                            crntLocation.setLatitude(latitudeList.get(i-1));
                            crntLocation.setLongitude(longitudeList.get(i-1));
                            newLocation.setLatitude(latitudeList.get(i));
                            newLocation.setLongitude(longitudeList.get(i));
                            distance += crntLocation.distanceTo(newLocation);
                            dist.setText("Distance: "+distance + " meters");

                        }

                    }
                }

            }
            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

        };

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        }, REQUEST_LOCATION);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, locationListener);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    }
                }
                return;
            }
        }
    }

}