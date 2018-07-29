package com.example.hp.colorlock;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class MyNetworksFragment extends Fragment {

    private FloatingActionButton add_network;
    private FirebaseApp firebaseApp;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    private ArrayList<String> arrayList;
    public MyNetworksFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MyNetworksFragment newInstance() {
        MyNetworksFragment fragment = new MyNetworksFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("fragment","onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("fragment","onDestroyed");
        Main_menu.joinnet.setVisibility(View.VISIBLE);
        Main_menu.mynet.setVisibility(View.VISIBLE);
        Main_menu.seqButton.setVisibility(View.VISIBLE);
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.i("fragment","onResume");
    }
    @Override
    public void onStart() {
        super.onStart();
        Log.i("fragment","onStart");
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.i("fragment","onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("fragment","onPause");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.my_networks, container, false);
        firebaseApp= FirebaseApp.getInstance();
        firebaseDatabase= FirebaseDatabase.getInstance(firebaseApp);
        databaseReference=firebaseDatabase.getReference("networks");
        add_network=rootView.findViewById(R.id.floating_action_button);
        add_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Add_network_fragment()).addToBackStack("tag").commit();
            }
        });
        listView=rootView.findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String network_name="net";
                SharedPreferences loginData = getActivity().getSharedPreferences("Network_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = loginData.edit();
                editor.putString("Network_name",network_name);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new Edit_fragment()).addToBackStack("tag").commit();
            }
        });
        arrayList=new ArrayList<>();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Network network=dataSnapshot.getValue(Network.class);
                arrayList.add(network.getNetwork_name());
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Network network=dataSnapshot.getValue(Network.class);
                arrayList.remove(network.getNetwork_name());
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        arrayAdapter=new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_2,android.R.id.text1,arrayList);
        listView.setAdapter(arrayAdapter);
        return rootView;
    }


}

