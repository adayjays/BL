package com.example.lendme.ui.messages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.lendme.R;
import com.example.lendme.models.Message;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {

    private boolean previousMessages = false;
    private TextView messageText;
    private ListView listView;
    private List<Message> messagesList = new ArrayList<>();

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

        listView = view.findViewById(R.id.msg_list);

        getMessages();
        getMessagesAsLender();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle bundle = new Bundle();
                bundle.putString("chatId", messagesList.get(i).getObjectId());
                ChatFragment fragment = new ChatFragment();
                fragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.nav_host_fragment_activity_main, fragment, "ChatFragment");
                fragmentTransaction.commit();

                 }
        });


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
        for (ParseObject object : list) {

            Message msg = new Message(
                    object.getObjectId(),object.getString("seller"),object.getString("buyer"),object.getString("item"),
                    object.getString("possible_buyer"),object.getString("createdAt"),object.getString("sender_name")
            );
            messagesList.add(msg);

        }
        if (messagesList.size()>0){
            CustomMessageListAdapter adapter = new CustomMessageListAdapter(messagesList,getContext(),R.layout.message_item);
            listView.setAdapter(adapter);

        }
    }

}