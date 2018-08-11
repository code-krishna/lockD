package com.example.hp.colorlock;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MyNetworksFragment extends Fragment {

    private FloatingActionButton add_network;
    private FirebaseApp firebaseApp;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
//    private ArrayAdapter<String> arrayAdapter;
    private Myadapter myadapter;
    private ArrayList<Network> networks;
    private ListView listView;
    private EditText text_name,text_pass,text_id;
    private Button button_add;
    private Dialog dialog;
    private ProgressDialog progressDialog;
    private String network_id,network_name,network_pass;
    public MyNetworksFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static MyNetworksFragment newInstance() {
        MyNetworksFragment fragment = new MyNetworksFragment();
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
        add_network=rootView.findViewById(R.id.floating_action_button);
        setViews(rootView);
        firebaseApp= FirebaseApp.getInstance();
        progressDialog=new ProgressDialog(getActivity());
        firebaseDatabase= FirebaseDatabase.getInstance(firebaseApp);
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("credentials",0);
        Log.i("credentials======**",sharedPreferences.getString("user_id","NULL"));
        databaseReference=firebaseDatabase.getReference("users/" +sharedPreferences.getString("user_id","NULL")+"/my_networks");
        networks=new ArrayList<>();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Network network = dataSnapshot.getValue(Network.class);
                    Log.i("network_id",network.getNetwork_id());
                    Log.i("network_name",network.getNetwork_name());
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
        myadapter=new Myadapter(networks,getActivity());
        listView.setAdapter(myadapter);
        return rootView;
    }
    public void setViews(View rootView){//initializing and setting all views
        add_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(getActivity(), android.R.style.Theme_Material_Light_Dialog_Alert);
                Window window=dialog.getWindow();
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
                dialog.setContentView(R.layout.add_network);
                dialog.setCancelable(true);
                dialog.show();
                set_views_dialog(dialog);
                
            }
        });
        listView=rootView.findViewById(R.id.listview);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Network network=(Network)parent.getItemAtPosition(position);
                Log.i("tag","below see");
                Log.i(network.getNetwork_id(),network.getNetwork_name());
                //Passing the network_id and network_name to the fragment.
                Edit_fragment fragment=new Edit_fragment();
                Bundle bundle=new Bundle();
                bundle.putString("network_id",network.getNetwork_id());
                bundle.putString("network_name",network.getNetwork_name());
                fragment.setArguments(bundle);
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment).addToBackStack("tag").commit();
            }
        });
    }

    void set_views_dialog(Dialog dialog){
        text_id=dialog.findViewById(R.id.network_id);
        text_name=dialog.findViewById(R.id.name);
        text_pass=dialog.findViewById(R.id.password);
        button_add=dialog.findViewById(R.id.add_button);
        button_add.setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener =new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (text_id.getText().toString().equals("")) {
                text_id.setError("This field is empty!");
               // text_id.requestFocus();
            }if (text_name.getText().toString().equals("")) {
                text_name.setError("Name is mandatory!");
                //text_name.requestFocus();
            }if(text_pass.getText().toString().equals("")){
                text_pass.setError("Enter password!");
                //text_pass.requestFocus();
            }
            else {
                progressDialog.setMessage("Adding...");
                progressDialog.show();
                network_id = text_id.getText().toString();
                network_name = text_name.getText().toString();
                network_pass=text_pass.getText().toString();
                add_network();
            }

        }
    };
    void add_network(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        final SharedPreferences ret_cred = getActivity().getSharedPreferences("credentials", Context.MODE_PRIVATE);
        DatabaseReference networkref = databaseReference.child("networks").child(network_id);
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    progressDialog.dismiss();
                    databaseReference = firebaseDatabase
                            .getReference("networks/" +network_id);
                    Network network = new Network(network_id,network_pass, network_name,ret_cred.getString("user_name","NULL"));
                    //   Log.i("admin_name",ret_cred.getString("user_name","NULL"));
                    databaseReference.setValue(network);
                    databaseReference=firebaseDatabase.getReference("users/"+ret_cred.getString("user_id","NULL")+"/my_networks/"+network_id);
                    network=new Network(network_id,network_pass,network_name);
                    databaseReference.setValue(network);
                    Toast.makeText(getActivity(), "Network added successfully!", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(), "This id already exists!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        networkref.addListenerForSingleValueEvent(eventListener);

    }

}

