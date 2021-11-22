package com.example.assignment2todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;


public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();  // array list of string data type
    static ArrayAdapter arrayAdapter; // to connect array list and list view together because LV cannot talk to arraylist directly

    // setting up the menu bar and menu item
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();  // pop up menu
        menuInflater.inflate(R.menu.main_menu, menu); // get menu from the res folder, main_menu.xml

        return super.onCreateOptionsMenu(menu);  // return menu
    }

    // what happen if the user select the menu item - add button
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.add_note) {

            Intent intent = new Intent(getApplicationContext(),
                    note_editor.class);  // open the link and page, bring the user to another page

            startActivity(intent); // start another page, we have note_editor.java stored in the intent object

            return true; // user select the item

        }

        return false; // user do not select the item

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView listView = (ListView) findViewById(R.id.listView);  // get me the LV controls

        // setup the shared preferences object , file to stored data , in this application, for private use only
        // change the package name
        SharedPreferences sharedPreferences =
                getApplicationContext().getSharedPreferences
                        ("com.example.assignment2todolist", Context.MODE_PRIVATE);

        // get the array list and convert them to become set collections
        // later we need to save all the data into shared preferences
        // permanently

        HashSet<String> set = (HashSet<String>)
                sharedPreferences.getStringSet("notes", null);

        // i used a hash set collections here
        // because the array list called notes, cannot direclty talk to shared preferences
        // we need to use hash set as a middle man

        if (set == null) {  // if the set is empty, display

            notes.add("Example note");

        } else {  // if the set is not empty, display all the contents in the array list

            notes = new ArrayList(set);

        }

        // because an array list cannot talk to LV controls, we need to use an array adapter as a middle man here
        // get all the items stored in the array list, and used the adapter to transfer all the items to LV controls
        arrayAdapter = new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, notes);

        listView.setAdapter(arrayAdapter);  // display all the items in the LV ui controls

        // if the user wish to edit or update the items
        // a single click on the item, just click on the item, we used item click listener here to listen to the user click

        // what happen if the user click the items in the listview
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            // override the abstract method
            public void onItemClick
            (AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(),
                        note_editor.class);  // bring the user to another page
                intent.putExtra("noteId", i); // save
                startActivity(intent);

                // open the new page
                // transfer the data note id to another page
                // start the new page aka activity


            }  // event handler

        });  // event listener interface


        // what happen if the user long press click on the items
        // delete the items from the files or SP
        // pop out alert dialog box , ask the user do they confirm want to delete
        // or not
        // once they confirmed they want to delete,
        // delete the item inside the array list first, and update the arraylist
        // finally update the files again , aka shared preferences


        // event listener interface , long press click, liste to long press click events
        listView.setOnItemLongClickListener
                (new AdapterView.OnItemLongClickListener() {

                    public boolean onItemLongClick  // event handler
                            (AdapterView<?> adapterView, View view, int i, long l) {

                                final int itemToDelete = i;  // which item to delete

                                new AlertDialog.Builder(MainActivity.this)   // create a new pop up dialog
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .setTitle("Are you sure?")
                                        .setMessage("Do you want to delete this note?")
                                        .setPositiveButton("Yes",
                                                new DialogInterface.OnClickListener() {  // if the user click the button

                                                    public void onClick(DialogInterface dialogInterface, int i) {

                                                        notes.remove(itemToDelete);  // update the array list
                                                        arrayAdapter.notifyDataSetChanged();  // send notification

                                                        // update the shared preferences , file
                                                        SharedPreferences sharedPreferences =
                                                                getApplicationContext().getSharedPreferences
                                                                        ("com.example.assignment2todolist",
                                                                                Context.MODE_PRIVATE);

                                                        HashSet<String> set = new HashSet(MainActivity.notes);

                                                        sharedPreferences.edit().putStringSet("notes", set).apply(); // update, save

                                                    }
                                                }
                                        )

                                        .setNegativeButton("No", null)  // if the user click no button
                                        .show();


                                return true;
                            }  // event handler


                });  // item listener interface



    }  // end of onCreate()
} // end of the class
