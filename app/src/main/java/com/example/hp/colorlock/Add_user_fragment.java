package com.example.hp.colorlock;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Add_user_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private SharedPreferences sharedPreferences;
    private FirebaseApp firebaseApp;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private EditText user_id,pass,uid;
    private Button create;
    String userid,passwrd,net_id,network_name;
    public Add_user_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Add_user_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Add_user_fragment newInstance(String param1, String param2) {
        Add_user_fragment fragment = new Add_user_fragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View rootView=inflater.inflate(R.layout.add_users,container,false);
       firebaseApp=FirebaseApp.getInstance();
       firebaseDatabase=FirebaseDatabase.getInstance(firebaseApp);
       create=rootView.findViewById(R.id.create);
       user_id=rootView.findViewById(R.id.user_id);
       pass=rootView.findViewById(R.id.password);
       uid=rootView.findViewById(R.id.network_id);
       sharedPreferences=getActivity().getSharedPreferences("Network_info",Context.MODE_PRIVATE);
       network_name=sharedPreferences.getString("Network_name","");
       create.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if(user_id.getText().toString().equals("")){
                   user_id.setError("User Id is mandatory!");
               }if(pass.getText().toString().equals("")){
                   pass.setError("Enter the password!");
               }
               if(uid.getText().toString().equals("")){
                   uid.setError("Unique Network Id is mandatory!");
               }
               else{
                   userid=user_id.getText().toString();
                   passwrd=pass.getText().toString();
                   net_id=uid.getText().toString();
                   databaseReference=firebaseDatabase.getReference().child(net_id);
                   databaseReference=firebaseDatabase.getReferenceFromUrl("https://lockd-1469b.firebaseio.com/networks/-"+net_id+"/users");
                   //User user=new User(userid,passwrd,network_name);
                   //databaseReference.push().setValue(user);
                   //Toast.makeText(getActivity(),"User added successfully!",Toast.LENGTH_SHORT).show();
               }
           }
       });

       return rootView;
    }
}

