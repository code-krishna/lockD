package com.example.hp.colorlock;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class Edit_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ProgressDialog progressDialog;
    // TODO: Rename and change types of parameters
    private String network_name;
    private Context context;
    public static final int DETAIL_REQUEST = 1;
    private String network_id;
    private EditText editText;
    private Button create;
    private String admin_id;
    private Dialog fbDialogue;
    private Button gen_seq,add_user,rem_user,show_users,change_pass;
    private  DatabaseReference databaseReference;
    private String c_id;
    private User client;
    public Edit_fragment() {
        // Required empty public constructor
    }

    public static Edit_fragment newInstance(String param1, String param2) {
        Edit_fragment fragment = new Edit_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        context=getActivity();
        View rootView=inflater.inflate(R.layout.network_operation,container,false);
        gen_seq=rootView.findViewById(R.id.gs);
        add_user=rootView.findViewById(R.id.au);
        rem_user=rootView.findViewById(R.id.ru);
        show_users=rootView.findViewById(R.id.su);
        change_pass=rootView.findViewById(R.id.cp);
        network_id=getArguments().getString("network_id");
        network_name=getArguments().getString("network_name");


        //remref=FirebaseDatabase.getInstance().getReference();
        //remref.addChildEventListener(valueEventListener3);
        gen_seq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(context,ColorStart.class);
                final Bundle extras=new Bundle();
                databaseReference=FirebaseDatabase.getInstance().getReference("networks/"+network_id);
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Network network=dataSnapshot.getValue(Network.class);
                        String network_password=network.getPassword();
                        Log.i("npaswrdedit_fragment",network_password);
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
        add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fbDialogue=setDialog(R.layout.add_users);
                editText=fbDialogue.findViewById(R.id.unique_id);
                create=fbDialogue.findViewById(R.id.create);
                create.setOnClickListener(createlistener);

//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, new Add_user_fragment()).addToBackStack("tag").commit();
            }
        });
        rem_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               getActivity().getSupportFragmentManager().beginTransaction().
//                        replace(R.id.fragment_container,new Remove_user_fragment()).addToBackStack("tag").commit();
                fbDialogue=setDialog(R.layout.fragment_remove_user_fragment);
                editText=fbDialogue.findViewById(R.id.uid);
                create=fbDialogue.findViewById(R.id.remove);
                create.setOnClickListener(removelistener);

            }
        });
        show_users.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Show_users_fragment fragment=new Show_users_fragment();
                Bundle bundle=new Bundle();
                bundle.putString("network_id",network_id);
                bundle.putString("network_name",network_name);
                fragment.setArguments(bundle);
                Log.i("new frgment====","called-======");
                getActivity().getSupportFragmentManager().beginTransaction().
                        replace(R.id.fragment_container,fragment).addToBackStack("tag").commit();
            }
        });

        return rootView;
    }
    View.OnClickListener createlistener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(editText.getText().toString().equals("")){
                editText.requestFocus();
                editText.setError("Enter the id!");
            }
            else{
                progressDialog=new ProgressDialog(getActivity());
                progressDialog.show();
                progressDialog.setMessage("Adding...");
                String client_id=editText.getText().toString();
                Log.i("client_id",client_id);
                upload_data(client_id);
                //databaseReference.removeEventListener(valueEventListener4);
               // databaseReference.removeEventListener(valueEventListener2);
            }
        }
    };
   // listener for the remove button
    //calls the remove_from_firebase method to remove the user from the selected admin network
    View .OnClickListener removelistener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(editText.getText().toString().equals("")){
                editText.requestFocus();
                editText.setError("Enter the id!");
            }else{
                progressDialog=new ProgressDialog(getActivity());
                progressDialog.setMessage("Removing...");
                progressDialog.show();
                String client_id=editText.getText().toString();
                Log.i("client_id_to_remove",client_id);
                remove_from_firebase(client_id);
               // databaseReference.removeEventListener(childEventListener);
            }

        }
    };
    private void upload_data(String client_id){
        Log.i("network_id",network_id);
        c_id=client_id;
        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference("users/").child(client_id);
        databaseReference1.addListenerForSingleValueEvent(valueEventListener4);
        databaseReference1.keepSynced(true);
    }
    ValueEventListener  valueEventListener4=new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //databaseReference.removeEventListener(this);
            Log.i("inside valistener4","onDataChanged");
            if (!dataSnapshot.exists()) {
                Toast.makeText(context, "User-id don't exist!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            } else {
                Log.i("key====",dataSnapshot.getKey());
                Log.i("values===",dataSnapshot.getValue().toString());
                dataSnapshot.getRef().child("joined_networks").child(network_id).setValue(new Network(network_name,network_id));
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    if(dataSnapshot1.getKey().equals("USER_DATA")){
                        client=dataSnapshot1.getValue(User.class);
                    }
                }
                progressDialog.dismiss();
                Toast.makeText(context, "Added to your network successfully!", Toast.LENGTH_SHORT).show();
                fbDialogue.dismiss();
                fbDialogue.cancel();
                upload_data_to_admin(client);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    private void upload_data_to_admin(User user){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("credentials",0);
        admin_id=sharedPreferences.getString("user_id","NULL");
        databaseReference=FirebaseDatabase.getInstance().getReference("users/"+admin_id+"/clients/"+network_id+"/"+user.getUid());
        databaseReference.setValue(user);
        databaseReference.keepSynced(true);
    }

    private  void remove_from_firebase(final String client_id){
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("credentials",0);
        admin_id=sharedPreferences.getString("user_id","NULL");
        databaseReference=FirebaseDatabase.getInstance().getReference().child("users").child(admin_id).child("clients").child(network_id).child(client_id);
        Log.i("admin_id==============",admin_id);
        Log.i("client_id=============",client_id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    databaseReference.removeValue();
                    Toast.makeText(context,"Removed successfully!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    fbDialogue.dismiss();
                    fbDialogue.cancel();
                    remove_data(client_id);
                }
                else{
                    Toast.makeText(context,"User doesn't exist on your network!",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    fbDialogue.dismiss();
                    fbDialogue.cancel();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        databaseReference.keepSynced(true);
    }

    private Dialog setDialog(int id){
        fbDialogue = new Dialog(getActivity(),android.R.style.Theme_Material_Light_Dialog_Alert);
        Window window=fbDialogue.getWindow();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        //  fbDialogue.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(100, 0, 0, 0)));
        fbDialogue.setContentView(id);
        fbDialogue.setCancelable(true);
        fbDialogue.show();
        return fbDialogue;
    }
    private void remove_data(String client_id){
        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("users/"+client_id+"/joined_networks/"+network_id);
        databaseReference.removeValue();
        databaseReference.keepSynced(true);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}


