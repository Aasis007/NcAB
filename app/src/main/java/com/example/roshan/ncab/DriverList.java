package com.example.roshan.ncab;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DriverList extends Fragment {
private View view;
    private ListView listview;
    DatabaseReference databaseReference;

    public DriverList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_driver_list, container, false);
        listview=(ListView)view.findViewById(R.id.listView);
        final List<Post> itemList = new ArrayList<>();
        databaseReference= FirebaseDatabase.getInstance().getReference("driver");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {
                    Post post = dataSnapshot2.getValue(Post.class);
//                    String username1 = post.getUsername();
//                    final long contact1 = post.getContact();
                    itemList.add(post);
                    ItemAdapter adapter = new ItemAdapter(getActivity(),itemList);
                    listview.setAdapter(adapter);

//                            Intent callIntent = new Intent(Intent.ACTION_CALL);
//                            callIntent.setData(Uri.parse("tel:" + contact1));
//                            startActivity(callIntent);
//
//                            Toast.makeText(getActivity(), "" + contact1, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       

        return view;
    }
    public class ItemAdapter extends ArrayAdapter {
        public List<Post> itemList;
        private int resource;
        private LayoutInflater inflater;


        public ItemAdapter(Context context, List<Post> itemList) {
            super(context,R.layout.list_item, itemList);
            this.itemList = itemList;
            this.resource = resource;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();

                convertView = inflater.inflate(R.layout.list_item, null);

                holder.name = (TextView) convertView.findViewById(R.id.driver_name);

                holder.phone = (TextView) convertView.findViewById(R.id.phone);

                holder.email = (TextView) convertView.findViewById(R.id.email);
                holder.lat = (TextView) convertView.findViewById(R.id.lat);
                holder.lng = (TextView) convertView.findViewById(R.id.lon);
                holder.id = (TextView) convertView.findViewById(R.id.txt_id);
                holder.licence = (TextView) convertView.findViewById(R.id.licence);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            //data insert


            Log.d("MainActivity", "Name = " + itemList.get(position).getUsername());
            holder.name.setText(itemList.get(position).getUsername());
            holder.email.setText(itemList.get(position).getEmail());
            holder.lat.setText(String.format(String.valueOf(itemList.get(position).getLatitude())));
            holder.lng.setText(String.format(String.valueOf(itemList.get(position).getLongitude())));
            holder.id.setText(itemList.get(position).getId());
            holder.licence.setText(itemList.get(position).getLicence());

            holder.phone.setText(String.format(String.valueOf(itemList.get(position).getContact())));
            Log.d("MainActivity", "Phone = " + itemList.get(position).getContact());

            return convertView;
        }

        class ViewHolder {
            private TextView name,phone,email,licence,lat,lng,id;
        }
    }
}
