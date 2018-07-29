package com.example.hp.colorlock;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Add_network_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ArrayList<String> network_id_list=new ArrayList<>();
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText net_id,pass,net_name;
    private String network_id,password,network_name;
    private Button add_button;
    //Firebase parameter
    private FirebaseApp firebaseApp;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private SharedPreferences sharedPreferences;
    private Map<String,Object> mp;
    private ArrayList<String> arrayList=new ArrayList<>();
    public Add_network_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Add_network_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Add_network_fragment newInstance(String param1, String param2) {
        Add_network_fragment fragment = new Add_network_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.add_network, container, false);
        sharedPreferences=getActivity().getSharedPreferences("user_credentials",Context.MODE_PRIVATE);
        final String master_id=sharedPreferences.getString("user_id","");
        firebaseApp=FirebaseApp.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance(firebaseApp);
        databaseReference=firebaseDatabase.getReference("users");

        net_id=rootView.findViewById(R.id.network_id);
        pass=rootView.findViewById(R.id.password);
        net_name=rootView.findViewById(R.id.name);
        //net_id=net_id.getText().toString();
        add_button=rootView.findViewById(R.id.add);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(net_id.getText().toString().equals("")){
                    net_id.setError("This field is empty!");
                }
                if(net_name.getText().toString().equals("")){
                    net_name.setError("Name is mandatory!");
                }else{
                    network_id=net_id.getText().toString();
                    network_name=net_name.getText().toString();
                    databaseReference.child("user_id").equalTo(master_id).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                            arrayList=(ArrayList<String>)dataSnapshot.getValue();
                        }

                        @Override
                        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onChildRemoved(DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    arrayList.add(network_id);
                    databaseReference.push().setValue(arrayList);
                    Toast.makeText(getActivity(),"Network added successfully!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }





    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
