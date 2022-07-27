package com.example.lendme.ui.messages;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;
public class MessageAdapter extends RecyclerView.Adapter<MessageHolder> {
    private List<ParseObject> list;
    private Context context;
    Activity activity;

    public MessageAdapter(List<ParseObject> list, Context context,Activity activity) {
        this.list = list;
        this.context = context;
        this.activity = activity;

    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        ParseObject object = list.get(position);
        ParseUser currentUser = ParseUser.getCurrentUser();
        String userObjectId =currentUser.getObjectId();
        String person = "Lender";
        if(userObjectId == object.getString("sender")){
            person = "Me";
        }
        holder.sender.setText(person+"  : "+object.getString("message"));
        holder.time.setText(object.getString("createdAt"));
//        holder.description.setText(object.getString("description"));
        holder.sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dataToSend = object.getString("item")+","+object.getString("item");
                MessageFragment messageFragment  = new MessageFragment();
                messageFragment.goToChatFragment(dataToSend);


            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
class  MessageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView sender;
    public TextView time;
    public MessageHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void onClick(View view) {

    }
}
