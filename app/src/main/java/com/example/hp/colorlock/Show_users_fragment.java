package com.example.hp.colorlock;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Show_users_fragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView listView;
    private String admin_id;
    private String network_id,network_name;
    private ArrayList<User> users;
    private ArrayList<String> arrayList;
    private Myadapter2 myadapter;
    public Show_users_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Show_users_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Show_users_fragment newInstance(String param1, String param2) {
        Show_users_fragment fragment = new Show_users_fragment();
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
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.show_users_fragment, container, false);
        Log.i("onCreateViewCalled=====","called-======");
        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("credentials",0);
        network_id=getArguments().getString("network_id");
        network_name=getArguments().getString("network_name");
        admin_id=sharedPreferences.getString("user_id","NULL");
        users=new ArrayList<>();
        listView=rootView.findViewById(R.id.user_view);
//        arrayList=new ArrayList<>();
//        arrayList.add("ayush");
//        arrayList.add("hii");
//        arrayList.add("yes");
//        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,arrayList);
//        listView.setAdapter(arrayAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(),"hii",Toast.LENGTH_SHORT).show();
//            }
//        });
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("users/"+admin_id+"/"+"clients").child(network_id);
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                User user=dataSnapshot.getValue(User.class);
                Log.i("user=======",user.toString());
                users.add(user);
                myadapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                Log.i("user=======",user.toString());
                users.remove(user);
                myadapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myadapter=new Myadapter2(users,getActivity());
        listView.setAdapter(myadapter);
        return rootView;
    }


}
