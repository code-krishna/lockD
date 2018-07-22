package com.example.hp.colorlock;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class MyNetworksFragment extends Fragment {


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
        final View rootView = inflater.inflate(R.layout.fragment_blank, container, false);
        ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("Numbers");
        arrayList.add("Family Members");
        arrayList.add("Colors");
        arrayList.add("Phrases");
        ListView listView=rootView.findViewById(R.id.listview);
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_2,android.R.id.text1,arrayList);
        listView.setAdapter(arrayAdapter);
        return rootView;
    }

}
