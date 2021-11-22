package com.example.assignment2todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.HashSet;



public class note_editor extends AppCompatActivity {


    int noteId;// pk, which record you stored

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText editText = (EditText) findViewById(R.id.editText);  // get me the text area controls from XML file

        Intent intent = getIntent();  // create an intent object
        noteId = intent.getIntExtra("noteId", -1); // set value for intents

        //intent object serves as a hyperlink, we can also use intent object to transfer data from one page to another page


        // note id variable transferred from main activity
        // if note id is not empty
        if (noteId != -1) {  // display the contents that you pull out from the file

            editText.setText(MainActivity.notes.get(noteId));  // display the contents , notes array list

        } else {  // noteID is empty

            MainActivity.notes.add("");  // display nothing
            noteId = MainActivity.notes.size() - 1; // arraylist item start 0
            MainActivity.arrayAdapter.notifyDataSetChanged();  // update arraylist

        }

        // event listener, if something change in the textbox or text area
        // means if you are typing something inside the textbox or textarea

        editText.addTextChangedListener(new TextWatcher() {

            // what happen before the changes in the textbox, nothing happen, when i open this page
            public void beforeTextChanged(CharSequence charSequence,
                                          int i, int i1, int i2) {

            }  // event handler 1

            // what happen during the changes in the textbox, when the user type inside the textbox

            public void onTextChanged(CharSequence charSequence, int i, int i1,
                                      int i2) {

                MainActivity.notes.set(noteId, String.valueOf(charSequence));  // update the array list , notes
                MainActivity.arrayAdapter.notifyDataSetChanged();  // send notification to SP or files

                // get the file and open the file, SP
                SharedPreferences sharedPreferences =
                        getApplicationContext().getSharedPreferences
                                ("com.example.assignment2todolist",
                                        Context.MODE_PRIVATE);

                HashSet<String> set = new HashSet(MainActivity.notes);   // serves a middle man between array list and SP

                // open the file in edit mode, put the contents in the file, save the changes
                sharedPreferences.edit().putStringSet("notes", set).apply();  // insert and save

                // update the notes array list that you get from main activity
                // open the files, convert the notes array list to set collections
                // save the entire set to the files, shared preferences only accept
                // set , cannot accept array list, so i need to convert the notes
                // array list into set first, because array list cannot talk to SP directly, they need middle man here, HashSet




            }  // event handler 2


            // what happen after the changes in the textbox, nothing happen
            public void afterTextChanged(Editable editable) {

            }  // event handler 3

        });  // event listener



        }
}
