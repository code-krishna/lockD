package com.example.hp.colorlock;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Joined_networks_fragment extends Fragment {

    private FloatingActionButton add_network;
    private FirebaseApp firebaseApp;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    //private ArrayAdapter<String> arrayAdapter;
    private ListView listView;
    private Button generate;
    private Myadapter myadapter;
    private Context context;
    private Dialog dialog;
    private String admin_id;
    public static final int DETAIL_REQUEST = 1;
    private Network selected_network;
    private ArrayList<Network> networks;
    public Joined_networks_fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static Joined_networks_fragment newInstance() {
        Joined_networks_fragment fragment = new Joined_networks_fragment();
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
        Log.i("fragment","onDestroyed");;
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
          View rootView=inflater.inflate(R.layout.my_joined_networks,container,false);
          Log.i("Joined_networks","launced");
          context=getActivity();
          listView=rootView.findViewById(R.id.list_view);
          setviews();
          SharedPreferences sharedPreferences=context.getSharedPreferences("credentials",0);
          admin_id=sharedPreferences.getString("user_id","NULL");
          databaseReference=FirebaseDatabase.getInstance().getReference("users/"+admin_id+"/joined_networks");
          Log.i("user_id",sharedPreferences.getString("user_id","NULL"));
          networks=new ArrayList<>();
          databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.i("key=========",dataSnapshot.getKey());
                Network network = dataSnapshot.getValue(Network.class);
                Log.i("network_id********",network.getNetwork_id());
                Log.i("network_name*******",network.getNetwork_name());
                networks.add(new Network(network.getNetwork_name(),network.getNetwork_id()));
                myadapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Network network=dataSnapshot.getValue(Network.class);
                networks.remove(new Network(network.getNetwork_name(),network.getNetwork_id()));
                myadapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myadapter=new Myadapter(networks,context);
        listView.setAdapter(myadapter);
        return rootView;
   }
   private void setviews(){
        progressDialog=new ProgressDialog(context);
        dialog = new Dialog(context, android.R.style.Theme_Material_Light_Dialog_Alert);
        Window window=dialog.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        dialog.setContentView(R.layout.edit_joined_networks);
        dialog.setCancelable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.show();
                selected_network=(Network)parent.getItemAtPosition(position);
                Log.i("network_id_sn=====",selected_network.getNetwork_id());
                Log.i("network_name_sn====",selected_network.getNetwork_name());
            }
        });
        generate=dialog.findViewById(R.id.generate);
        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String network_id=selected_network.getNetwork_id();
                databaseReference=FirebaseDatabase.getInstance().getReference("networks/"+network_id);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Network network=dataSnapshot.getValue(Network.class);
                        String network_password=network.getPassword();
                        Log.i("network_password===",network_password);
                        final Intent intent=new Intent(context,ColorStart.class);
                        Bundle extras=new Bundle();
                        extras.putString("Password",network_password);
                        intent.putExtras(extras);
                        Intent j = new Intent(context,RedStart.class);
                        startActivity(j);
                        Handler handler  = new Handler();
                        handler.postDelayed(new Runnable(){
                            @Override
                            public void run() {
                                startActivityForResult(intent,DETAIL_REQUEST);

                            }
                        },2000);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


    }

}

