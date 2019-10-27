package com.example.down4din;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.List;

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
            public boolean onQueryTextSubmit(final String query) {
                map.clear();
                String location = searchView.getQuery().toString();
                List<Address> addressList = null;
                if(location != null && !location.equals("")){
                    final Geocoder geocoder = new Geocoder(MapActivity.this);
                    try {
                        addressList = geocoder.getFromLocationName(location, 5);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    int i = 0;
                    while (addressList != null && i < addressList.size() && addressList.get(i) != null) {
                        Address address = addressList.get(i);
                        String title = getStreetAddress(address);

                        LatLng latLong = new LatLng(address.getLatitude(), address.getLongitude());
                        map.addMarker(new MarkerOptions().position(latLong).title(title)).showInfoWindow();
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 10));
                        i++;
                    }

                    map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {
                            String placeCoords = marker.getPosition().toString();
                            Intent intent = new Intent();

                            intent.putExtra("address", marker.getTitle());
                            intent.putExtra("addressLat",
                                    placeCoords.substring(placeCoords.indexOf("(") + 1, placeCoords.indexOf(",")));
                            intent.putExtra("addressLong",
                                    placeCoords.substring(placeCoords.indexOf(",") + 1, placeCoords.indexOf(")")));

                            setResult(RESULT_OK, intent);
                            finish();
                            return true;
                        }
                    });
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) { return false; }
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
        setResult(RESULT_CANCELED);
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

    private String getStreetAddress(Address add) {
        String addressAsString = add.toString();
        int firstDQ = addressAsString.indexOf("\"");
        int secondDQ = addressAsString.indexOf("\"", firstDQ + 1);
        return addressAsString.substring(firstDQ + 1, secondDQ);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if(getIntent().hasExtra("address")) {
            LatLng location = getLocFromAdd(this, address);
            String title = "";
            try {
                title = getStreetAddress(new Geocoder(this).
                        getFromLocation(location.latitude, location.longitude, 1).get(0));
            } catch (IOException e) { e.printStackTrace(); }

            map.addMarker(new MarkerOptions().position(location).title(title)).showInfoWindow();
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 16.0f));
            searchView.setVisibility(View.GONE);
        }
    }
}
