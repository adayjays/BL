package com.example.lendme.ui.messages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lendme.MainActivity;
import com.example.lendme.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MessageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MessageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String val ="";
    private boolean isMsgs = false;
    private TextView msgMessage ;
    private RecyclerView recyclerView;

    String userId;

    public MessageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MessageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
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
//        ActionBar actionBar = getActivity().getActionBar();
//        actionBar.setTitle("Your title");
        ParseUser currentUser = ParseUser.getCurrentUser();
        userId =currentUser.getObjectId();
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        msgMessage = view.findViewById(R.id.no_msgs);
        recyclerView = view.findViewById(R.id.msg_list);

        getMessages();
        getMessagesAsLender();
        if(isMsgs){
            msgMessage.setText("");
        }
        // Inflate the layout for this fragment
        return view;
    }
    private void getMessages(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("messages");
        //We use this code to fetch data from newest to oldest.
        query.whereEqualTo("possible_buyer", userId);

        query.orderByDescending("createdAt");
        query.findInBackground((objects, e) -> {
            if (e == null) {
                isMsgs = true;
                initMessageList(objects);
            } else {
                Toast.makeText(getActivity(),"No Messages Yet",Toast.LENGTH_SHORT).show();
            }
        });
//        return n
    }



    private void getMessagesAsLender(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("messages");
        //We use this code to fetch data from newest to oldest.
        query.whereEqualTo("seller", userId);

        query.orderByDescending("createdAt");
        query.findInBackground((objects, e) -> {
            if (e == null) {
                isMsgs = true;
                initMessageList2(objects);
            } else {
                Toast.makeText(getActivity(),"No Messages Yet",Toast.LENGTH_SHORT).show();
            }
        });
//        return n
    }

    private void initMessageList2(List<ParseObject> list) {
        if (list == null || list.isEmpty()) {
//            empty_text.setVisibility(View.VISIBLE);
            return;
        }
//        empty_text.setVisibility(View.GONE);

        MessageAdapter adapter = new MessageAdapter(list, getContext(),getActivity());


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
    private void initMessageList(List<ParseObject> list) {
        if (list == null || list.isEmpty()) {
//            empty_text.setVisibility(View.VISIBLE);
            return;
        }
//        empty_text.setVisibility(View.GONE);

        MessageAdapter adapter = new MessageAdapter(list, getContext(),getActivity());


        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }
    public void goToChatFragment(String dataToSend){
        Bundle bundle = new Bundle();
        bundle.putString("key", dataToSend);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(bundle);
        FragmentManager manager = ((MainActivity) getContext()).getSupportFragmentManager();
        fragment.setArguments(bundle);
        manager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.nav_host_fragment_activity_main,fragment,null).commit();

    }

}