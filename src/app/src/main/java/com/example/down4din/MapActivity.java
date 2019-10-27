package com.example.down4din;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private String address;
    private String name;
    private Bundle bundle;
    private Context context;
    private SearchView searchView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        this.context = this;


        searchView = findViewById(R.id.sv_location);


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if(location != null || !location.equals("")){
                    Geocoder geocoder = new Geocoder(MapActivity.this);
                    try{
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {

                    }

                    Address address = addressList.get(0);
                    LatLng latLong = new LatLng(address.getLatitude(), address.getLongitude());



                    map.addMarker(new MarkerOptions().position(latLong).title(query));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 10));

                    Bundle toStausDialog = new Bundle();
                    StatusDialog sd = new StatusDialog();
                    String placeCoords = latLong.toString();
                    toStausDialog.putString("place", query);
                    toStausDialog.putString("placeLat", placeCoords.substring(placeCoords.indexOf("(") + 1, placeCoords.indexOf(",")));
                    toStausDialog.putString("placeLong", placeCoords.substring(placeCoords.indexOf(",") + 1, placeCoords.indexOf(")")));
                    sd.setArguments(toStausDialog);


                    finish();


                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        try {
            mapFragment.getMapAsync( this);
        } catch(NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        if(getIntent().hasExtra("address")) {
            bundle = getIntent().getExtras();
            try {
                address = bundle.getString("address");
                name = bundle.getString("name");
            } catch (NullPointerException e) {
                e.printStackTrace();
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }



    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    public LatLng getLocFromAdd(Context context, String add){
        Geocoder gc = new Geocoder(context);
        List<Address> address;
        LatLng latLng = null;
        try {
            // May throw an IOException
            address = gc.getFromLocationName(add, 5);
            if (address == null || address.size() == 0) {
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


        if(getIntent().hasExtra("address")) {
            LatLng location = getLocFromAdd(this, address);
            map.addMarker(new MarkerOptions().position(location).title(String.format("%s is here", name)));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16.0f));

        }
    }

}
