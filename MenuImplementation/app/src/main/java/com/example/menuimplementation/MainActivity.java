package com.example.menuimplementation;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

//    Attributes
    ListView listView;
    ArrayAdapter<String> adapter;
    String[] contacts = {"Sineka", "Hemant", "Uma", "Guna"};
    int position;
    //    this creates a mutable list
    ArrayList<String> contactList = new ArrayList<>(Arrays.asList(contacts));

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Configuring ListView
        listView = findViewById(R.id.listView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, contactList);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE); // Enables user to select multiple values
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            position = i; //Set this so that we can manipulate using index in the later part of code
            return false;
        });
        registerForContextMenu(listView);

    }

//    Lets Create ContextMenu and OptionsMenu using MenuInflater's inflate method
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_main,menu);
        menu.setHeaderTitle("Edit Menu");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu,menu);
        return true;
    }

//    CORE LOGIC: Cut and Copy
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.cut) {
            contactList.remove(position);
            Toast.makeText(this, "Cut success!", Toast.LENGTH_SHORT).show();
            adapter.notifyDataSetChanged();
        } else if(itemId == R.id.copy) {
            String value = contactList.get(position);
            Toast.makeText(this, "Copied: " + value, Toast.LENGTH_SHORT).show();
        } else {
            return false;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.edit) {
            SparseBooleanArray chosenItem = listView.getCheckedItemPositions();
            if (chosenItem.size() == 0) {
                Toast.makeText(this, "Nothing Chosen to Edit!!", Toast.LENGTH_SHORT).show();
            } else if (chosenItem.size() > 1) {
                Toast.makeText(this, "Kindly choose one time at a time to edit", Toast.LENGTH_SHORT).show();
            } else {
                EditText input = new EditText(this);
                new AlertDialog.Builder(this)
                        .setTitle("Edit")
                        .setMessage("Enter details to update: ")
                        .setView(input)
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            contactList.set(position, String.valueOf(input.getText()));
                            adapter.notifyDataSetChanged();
                        })
                        .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                        .show();
                chosenItem.clear();
            }
        } else if (item.getItemId() == R.id.delete) {
            SparseBooleanArray chosenItem = listView.getCheckedItemPositions();
            if (chosenItem.size() == 0) {
                Toast.makeText(this, "Choose something to delete!!", Toast.LENGTH_SHORT).show();
            }else {
                for (int i = chosenItem.size(); i >= 0; i--) {
                    if(chosenItem.valueAt(i)) {
                        contactList.remove(chosenItem.keyAt(i));
                    }
                }
                adapter.notifyDataSetChanged();
                chosenItem.clear();
            }
        }
        return true;
    }
}