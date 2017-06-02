package com.example.roshan.ncab;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.roshan.ncab.R.id.fab;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    private View view;
    private MapView mMapView;
    private GoogleMap mMap;
    Marker marker;
    FloatingActionButton fab, fab1, fab2, fab3, fab4;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView = (MapView) view.findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(this);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab_map);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab_hybrid);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab_hybrid);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab4 = (FloatingActionButton) view.findViewById(R.id.fab_statilte);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                if (marker != null) {
                    marker.remove();
                }
                marker= mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_navigation_black_24dp)).flat(true)
                        .rotation(270)
                        .title(String.valueOf(loc)).snippet("This is kathfoard international college").position(loc));

                CameraPosition cameraPosition = CameraPosition.builder()
                        .target(loc)
                        .zoom(13)
                        .bearing(90)
                        .build();

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fab4.setVisibility(View.VISIBLE);
                        fab3.setVisibility(View.VISIBLE);
                        fab2.setVisibility(View.VISIBLE);
                        fab1.setVisibility(View.VISIBLE);
                        fab.setVisibility(View.GONE);
                    }
                });

fab1.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
       fab4.setVisibility(View.GONE);
        fab3.setVisibility(View.GONE);
        fab1.setVisibility(View.GONE);
        fab2.setVisibility(View.GONE);
        fab.setVisibility(View.VISIBLE);
    }
});
                fab2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                        fab4.setVisibility(View.GONE);
                        fab3.setVisibility(View.GONE);
                        fab1.setVisibility(View.GONE);
                        fab2.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                    }
                });

                fab3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        fab4.setVisibility(View.GONE);
                        fab3.setVisibility(View.GONE);
                        fab1.setVisibility(View.GONE);
                        fab2.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                    }
                });

                fab4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        fab4.setVisibility(View.GONE);
                        fab3.setVisibility(View.GONE);
                        fab1.setVisibility(View.GONE);
                        fab2.setVisibility(View.GONE);
                        fab.setVisibility(View.VISIBLE);
                    }
                });
                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                        2000, null);


            }
        };
        mMap.setOnMyLocationChangeListener(myLocationChangeListener);
    }

}
