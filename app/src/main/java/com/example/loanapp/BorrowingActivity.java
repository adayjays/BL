package com.example.loanapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class BorrowingActivity extends AppCompatActivity {
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowing);
        String[] itemsCategories = new String[]{"Books", "Outdoor supplies", "Technology", "Household Items", "clothing/Jewelry", "Miscellaneous"};

        listView = (ListView) findViewById(R.id.my_list_view);
<<<<<<< HEAD

        // Instanciating an array list (you don't need to do this,
        // you already have yours).
        List<String> your_array_list = new ArrayList<>();
        your_array_list.add("foo");
        your_array_list.add("bar");
=======
>>>>>>> eddbe32520172d3d6903a75cb30f55fb0158860d

        // This is the array adapter, it takes the context of the activity as a
        // first parameter, the type of list view as a second parameter and your
        // array as a third parameter.
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                itemsCategories);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Intent intent = new Intent(BorrowingActivity.this, ItemsActivity.class);
                intent.putExtra("cat",itemsCategories[position]);
                startActivity(intent);

            }

        });
    }
}