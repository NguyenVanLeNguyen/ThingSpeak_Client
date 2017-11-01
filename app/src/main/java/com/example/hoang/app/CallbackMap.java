package com.example.hoang.app;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by hoang on 21/10/2017.
 */

public class CallbackMap implements OnMapReadyCallback {
    private GoogleMap myMap;
    private Double lat;
    private Double lng;
    private  String content;
    private  String lastUpdate;

    public CallbackMap(Double lat, Double lng, String content, String lastUpdate) {
        this.lat = lat;
        this.lng = lng;
        this.content = content;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myMap = googleMap;
        LatLng location = new LatLng(lat, lng);

        //mMap.setMyLocationEnabled(true);

        myMap.addMarker(new MarkerOptions()
                .title(content)
                .snippet(lastUpdate)
                .position(location));
        myMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 13));
    }


}
