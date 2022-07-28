package com.example.lendme.ui.messages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lendme.R;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class MessageFragment extends Fragment {

    private boolean previousMessages = false;
    private TextView messageText;
    private RecyclerView recyclerView;

    String userId;

    public MessageFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        userId = currentUser.getObjectId();
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        messageText = view.findViewById(R.id.no_msgs);
        recyclerView = view.findViewById(R.id.msg_list);

        getMessages();
        getMessagesAsLender();
        if (previousMessages) {
            messageText.setText("");
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
                previousMessages = true;
                initMessageList(objects);
            } else {
                Toast.makeText(getActivity(), "No Messages Yet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getMessagesAsLender(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("messages");
        //We use this code to fetch data from newest to oldest.
        query.whereEqualTo("seller", userId);
        query.orderByDescending("createdAt");
        query.findInBackground((objects, e) -> {
            if (e == null) {
                previousMessages = true;
                initMessageList(objects);
            } else {
                Toast.makeText(getActivity(),"No Messages Yet",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initMessageList(List<ParseObject> list) {
        if (list == null || list.isEmpty()) {
            return;
        }

        MessageAdapter adapter = new MessageAdapter(list, getContext());

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

}