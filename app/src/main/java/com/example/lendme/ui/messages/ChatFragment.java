package com.example.lendme.ui.messages;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.lendme.R;
import com.example.lendme.models.Chat;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    Button send;
    EditText msg;
    String itemId, chatId, userId, sellerId,userName,chatIdFromFragment;
    ListView listView;

    public ChatFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        msg = view.findViewById(R.id.msg_input);
        send = view.findViewById(R.id.send_btn);
        listView = view.findViewById(R.id.list_view_chat);
        chatIdFromFragment = null;

        itemId = "";
        chatId = "";
        getChatId();
        userId = "";
        sellerId = "";
        ParseUser currentUser = ParseUser.getCurrentUser();
        userId = currentUser.getObjectId();
        userName = currentUser.getUsername();

        Bundle extras = this.getArguments();

        if (extras != null) {
            chatIdFromFragment = extras.getString("chatId");
            if(chatIdFromFragment == null) {
                String value = extras.getString("key");
                String[] value_array = value.split(",");
                itemId = value_array[0];
                sellerId = value_array[1];
            }
        } else {
            showToast("Empty key");
        }
        if(chatIdFromFragment != null){
            getChats();
        }

        boolean emptyIds = itemId.equals("") && sellerId.equals("") && userId.equals("");
        if (!emptyIds ){
            getChatId();
            getChats();
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showToast("sending...");
                sendMessage();
                getChats();
            }
        });

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getChats();
            }
        }, 5000);

        return view;
    }



    private void getChats() {
        readChat();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("chats");
        if(chatIdFromFragment != null){
            query.whereEqualTo("chat_id", chatIdFromFragment);

        }else {
            query.whereEqualTo("seller", sellerId);
            query.whereEqualTo("item", itemId);
            query.whereEqualTo("possible_buyer", userId);

        }
        query.findInBackground((objects, e) -> {
            if (e == null) {
                initChatList(objects);
            } else {
                showToast("Error !" + e.getMessage());
            }
        });
    }

    private void initChatList(List<ParseObject> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        List<Chat> chatList = new ArrayList<>();
        for (ParseObject object : list) {

            String sender_person = "";

            ParseUser currentUser = ParseUser.getCurrentUser();
            String userObjectId = currentUser.getObjectId();
            String person = "Lender";
            if (userObjectId == object.getString("sender")) {
                person = "Me";
            }
            sender_person = person + "  : " + object.getString("message");
            if (chatIdFromFragment != null) {
                itemId = object.getString("item");
                sellerId = object.getString("seller");
            }

            Chat chat = new Chat(
              object.getObjectId(),
                    object.getString("seller"),
                    object.getString("item"),
                    object.getString("possible_buyer"),
                    object.getString("chat_id"),
                    object.getString("is_read"),
                    object.getString("sender"),
                    object.getString("sender_name"),
                    sender_person
                    );
            chatList.add(chat);
        }
        Toast.makeText(getContext(), "size "+chatList.size(), Toast.LENGTH_SHORT).show();
        CustomChatListAdapter adapter = new CustomChatListAdapter(chatList,getContext(),R.layout.chat_item);
        listView.setAdapter(adapter);

//        ChatAdapter adapter = new ChatAdapter(list, getContext());
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
    }

    public void sendMessage(){
        if (msg.getText().toString().trim().length() > 0){
            if (chatId.trim().length() == 0){
                createMessageItem();
            }else{
                saveChat();
            }
        }else{
            showToast("Empty Text");
        }
    }

    public void createMessageItem(){
        ParseObject msgItem =new ParseObject("messages");
        if (chatId.trim().length() == 0 && chatIdFromFragment == null){
            msgItem.put("seller", sellerId);
            msgItem.put("item",itemId);
            msgItem.put("possible_buyer", userId);
            msgItem.put("sender_name", userName);
            msgItem.saveInBackground(e -> {
                if (e == null){
                    getChatId();
                }else {
                    showToast("Error ! : "+ e.getMessage());
                }
            });
        }else{
            chatId = chatIdFromFragment;
        }

    }

    public void saveChat(){
        ParseObject chat = new ParseObject("chats");
        chat.put("message", msg.getText().toString());
        chat.put("seller", sellerId);
        chat.put("item", itemId);
        chat.put("possible_buyer", userId);
        chat.put("chat_id", chatId);
        chat.put("is_read","sent");
        chat.put("sender", userId);
        chat.put("sender_name", userName);

        chat.saveInBackground(e -> {
            if (e == null) {
                //We saved the object and fetching data again
                showToast("Sent");
                msg.setText("");
                getChats();
            } else {
                //We have an error.We are showing error message here.
                showToast("Error !"+ e.getMessage());
            }
        });
    }
    public void getChatId(){
        final String[] newChatId = {""};
        ParseQuery<ParseObject> query = ParseQuery.getQuery("messages");
        query.whereEqualTo("seller", sellerId);
        query.whereEqualTo("item", itemId);
        query.whereEqualTo("possible_buyer", userId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject player, ParseException e) {
                if (e == null) {
                    chatId = player.getObjectId();
                } else {
                    // Something is wrong
                    tryAsSeller();
                }
            }
        });

    }
    public void tryAsSeller(){
//        createMessageItem
        ParseQuery<ParseObject> query = ParseQuery.getQuery("messages");
        query.whereEqualTo("possible_buyer", sellerId);
        query.whereEqualTo("item", itemId);
        query.whereEqualTo("seller", userId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject player, ParseException e) {
                if (e == null) {
                    chatId = player.getObjectId();
                } else {
                    // Something is wrong
                    createMessageItem();
                }
            }
        });
    }

    private void showToast(String msg) {
        Toast toast = new Toast(getContext());
//        toast.setText(msg);
        toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    //change chat status to read if the current user_id is not same as the sender
    public void readChat() {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("chats");

// The query will resolve only after calling this method
        query.whereEqualTo("chat_id", chatId);
//        query.whereEqualTo("item", itemId);
//        query.whereEqualTo("possible_buyer", userId);
//        query.whereEqualTo("is_read", "sent");
//        query.whereNotEqualTo("sender", userId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // if the error is null then we are getting
                    // our object id in below line.
                    String objectID = object.getObjectId().toString();
                    query.getInBackground(objectID, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject object, ParseException e) {
                            if (e == null) {
                                object.put("is_read", "read");
                                object.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {}
                                });
                            }
                        }
                    });
                }
            }
        });
    }

}