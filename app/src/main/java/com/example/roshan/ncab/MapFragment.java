package com.example.roshan.ncab;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MapFragment extends Fragment implements OnMapReadyCallback{
    private View view;
    private MapView mMapView;
    private GoogleMap mMap;
    Marker marker;
    private Button btn_search,btn_share;
    FloatingActionButton fab, fab1, fab2, fab3, fab4;
    private FirebaseAuth firebaseauth;
    private Firebase mref;
    private DatabaseReference refrence;
    LatLng loc;
    private String Email;
    List<Address> addressList = new ArrayList<>();
    private DatabaseReference Location;
    private DatabaseReference mDatabase;
    private DatabaseReference userdatabase;
    EditText locationSearch;
    double lat;
    double lon;
    String latitude;
    LatLng latLng;
    LatLng longitude;
    SharedPreferences sharedpreferences;
    long contact;
    String licence;
    String username;
    String id;

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
        locationSearch = (EditText) view.findViewById(R.id.editText);
        firebaseauth= FirebaseAuth.getInstance();
    //   databaseReference= FirebaseDatabase.getInstance().getReference("driver");
          mDatabase = FirebaseDatabase.getInstance().getReference("driver");
          userdatabase = FirebaseDatabase.getInstance().getReference("user");
//        Location=mDatabase.child("Email");
        Firebase.setAndroidContext(getActivity());
        mref =new Firebase("https://ncab-80692.firebaseio.com/");
        btn_search = (Button) view.findViewById(R.id.search_button);
        btn_share=(Button)view.findViewById(R.id.btn_share_location);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }


        sharedpreferences = getActivity().getSharedPreferences("LONGITUDE", Context.MODE_PRIVATE);
        contact = sharedpreferences.getLong("contact", 0);
        licence=sharedpreferences.getString("licence",null);
        username=sharedpreferences.getString("username",null);
        id = sharedpreferences.getString("id",null);

        System.out.println("ID :"+id);
        System.out.println("Username: "+username);
        System.out.println("Licence :"+licence);


//        new Timer().schedule(new TimerTask() {
//            @Override
//            public void run() {
//                if (isVisible()) {
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            DatabaseReference  mDatabase = FirebaseDatabase.getInstance().getReference("driver").child(id);
//                            mDatabase.setValue(new Post(id,firebaseauth.getCurrentUser().getEmail(),lat,lon,contact,licence,username));
//                            Toast.makeText(getActivity(), "Send data to server "+loc, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }    }
//
//
//        },0, 10000);




        mMapView.getMapAsync(this);
        fab1 = (FloatingActionButton) view.findViewById(R.id.fab_map);
        fab2 = (FloatingActionButton) view.findViewById(R.id.fab_hybrid);
        fab3 = (FloatingActionButton) view.findViewById(R.id.fab_hybrid);
        fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab4 = (FloatingActionButton) view.findViewById(R.id.fab_statilte);

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //search method

                String location = locationSearch.getText().toString();
                List<Address> addressList = new ArrayList<>();

                if (location != null || !location.equals("")) {
                    Geocoder geocoder = new Geocoder(getActivity());
                    try {
                        addressList = geocoder.getFromLocationName(location, 1);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (addressList != null && addressList.size() > 0) {
                        final Address address = addressList.get(0);
                        final String city=address.getLocality();

                        if (ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mMap.setMyLocationEnabled(true);
                        mMap.getUiSettings().setZoomGesturesEnabled(true);
                        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
                            @Override
                            public void onMyLocationChange(Location location) {
                                latLng = new LatLng(address.getLatitude(), address.getLongitude());

                                System.out.println("Latlang :"+latLng);
                                if (marker != null) {
                                    marker.remove();
                                }
                                marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_navigation_black_24dp)).flat(true)
                                        .rotation(270)
                                        .title(String.valueOf(latLng)).snippet(city).position(latLng));

                                CameraPosition cameraPosition = CameraPosition.builder()
                                        .target(latLng)
                                        .zoom(13)
                                        .bearing(90)
                                        .build();
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16.0f));
                                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                                        2000, null);
                            }
                        };
                        mMap.setOnMyLocationChangeListener(myLocationChangeListener);
                    }
                }

                }
        });

        //location share

        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if( licence.equals("1")){
                   DatabaseReference  userdatabase = FirebaseDatabase.getInstance().getReference("user").child(id);
                   userdatabase.setValue(new Post(id,firebaseauth.getCurrentUser().getEmail(),lat,lon,contact,licence,username));
                   Toast.makeText(getActivity(), "Send data to server "+loc, Toast.LENGTH_SHORT).show();
               }else
               {
                   DatabaseReference  mDatabase = FirebaseDatabase.getInstance().getReference("driver").child(id);
                   mDatabase.setValue(new Post(id,firebaseauth.getCurrentUser().getEmail(),lat,lon,contact,licence,username));
                   Toast.makeText(getActivity(), "Send data to server "+loc, Toast.LENGTH_SHORT).show();
               }

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    if (licence.equals("1")){

        mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                        Post post = dataSnapshot2.getValue(Post.class);
                        String username1 = post.getUsername();
                        final long contact1 = post.getContact();

                        sharedpreferences = getActivity().getSharedPreferences("Latitude", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putLong("Latitude", Double.doubleToLongBits(post.getLatitude()));
                        editor.putLong("Longitude", Double.doubleToLongBits(post.getLongitude()));
                        editor.apply();
                        final double lat1 = Double.longBitsToDouble(sharedpreferences.getLong("Latitude", 0));
                        final double lng1 = Double.longBitsToDouble(sharedpreferences.getLong("Longitude", 0));
                        longitude = new LatLng(lat1, lng1);
                        System.out.println("Longitude :" + longitude);
//                marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_navigation_black_24dp)).flat(true)
//                        .rotation(270)
//                        .title(String.valueOf(longitude)).position(longitude));
//
//                LatLng sydney = new LatLng(-33.852, 151.211);
                        mMap.addMarker(new MarkerOptions().position(longitude)
                                .title(String.valueOf(longitude)).snippet(username1));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(longitude));
                        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                            @Override
                            public void onInfoWindowClick(Marker arg0) {
                                // TODO Auto-generated method stub

                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + contact1));
                                startActivity(callIntent);

                                Toast.makeText(getActivity(), "" + contact1, Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    else {

        userdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    Post post = dataSnapshot2.getValue(Post.class);
                    sharedpreferences = getActivity().getSharedPreferences("Latitude", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putLong("Latitude", Double.doubleToLongBits(post.getLatitude()));
                    editor.putLong("Longitude", Double.doubleToLongBits(post.getLongitude()));
                    editor.apply();
                    final double lat1 = Double.longBitsToDouble(sharedpreferences.getLong("Latitude", 0));
                    final double lng1 = Double.longBitsToDouble(sharedpreferences.getLong("Longitude", 0));
                    longitude = new LatLng(lat1, lng1);
                    System.out.println("Longitude :" + longitude);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

   }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        GoogleMap.OnMyLocationChangeListener myLocationChangeListener = new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                loc = new LatLng(location.getLatitude(), location.getLongitude());
                lat=location.getLatitude();
                lon=location.getLongitude();
                System.out.println("lat :"+lat);
                System.out.println("lon :"+lon);

                if (marker != null) {
                    marker.remove();
                }
                        marker = mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_navigation_black_24dp)).flat(true)
                                .rotation(270)
                                .title(String.valueOf(loc)).position(loc));

                        CameraPosition cameraPosition = CameraPosition.builder()
                                .target(loc)
                                .zoom(13)
                                .bearing(90)
                                .build();
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                                5000, null);
            }

        };

        mMap.setOnMyLocationChangeListener(myLocationChangeListener);

    }
}
