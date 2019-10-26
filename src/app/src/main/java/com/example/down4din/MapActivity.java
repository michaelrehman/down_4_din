package com.example.down4din;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private String address;
    private String name;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        bundle = getIntent().getExtras();
        address = bundle.getString("address");
        name = bundle.getString("name");

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
    }





    public LatLng getLocFromAdd(Context context, String add){
        Geocoder gc = new Geocoder(context);
        List<Address> address;
        LatLng latLng = null;
        try {
            // May throw an IOException
            address = gc.getFromLocationName(add, 5);
            if (address == null) {
                return null;
            }

            if (address.size() == 0) {
                return null;
            }

            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            latLng = new LatLng(location.getLatitude(), location.getLongitude());

        } catch (IOException ex) {

            ex.printStackTrace();
            Toast.makeText(context, ex.getMessage(), Toast.LENGTH_LONG).show();
        }

        return latLng;

    }






    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng temp = getLocFromAdd(this, address);

        LatLng location = new LatLng(temp.latitude, temp.longitude);
        map.addMarker(new MarkerOptions().position(location).title(address));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16.0f));

//        LatLng boltonHall = new LatLng(33.9510 ,  83.3775);
//        map.addMarker(new MarkerOptions().position(boltonHall).title("Bolton Dining Hall"));
//        map.moveCamera(CameraUpdateFactory.newLatLng(boltonHall));
    }
}
