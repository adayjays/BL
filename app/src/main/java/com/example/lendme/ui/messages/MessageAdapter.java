package com.example.lendme.ui.messages;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lendme.MainActivity;
import com.example.lendme.R;
import com.example.lendme.ui.borrow.ConfirmationFragment;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageHolder> {
    private List<ParseObject> list;
    private Context context;

    public MessageAdapter(List<ParseObject> list, Context context) {
        this.list = list;
        this.context = context;
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
        String userObjectId = currentUser.getObjectId();
        String person = userObjectId == object.getString("sender") ? "me" : "Lender";

        holder.sender.setText(person + "  : "  + object.getString("message"));
        holder.time.setText(object.getString("createdAt"));
//        holder.description.setText(object.getString("description"));
        holder.sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("key", object.getString("item"));
                ChatFragment fragment = new ChatFragment();
                fragment.setArguments(bundle);
                FragmentManager manager = ((MainActivity) context).getSupportFragmentManager();
                manager.beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.nav_host_fragment_activity_main, fragment, null).commit();
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
    public void onClick(View view) {}
}
