package com.example.ankit.delmsg;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddSpamSender extends AppCompatActivity {

    SQLiteDatabase db;
    MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_spam_sender);
        db=openOrCreateDatabase("Company",MODE_PRIVATE,null);
    }


    public void addtoSpam(View view){

        EditText editText =(EditText)findViewById(R.id.box);
        String spamSender = editText.getText().toString().trim();
        if(spamSender.length()==0){
            Toast.makeText(this,"Please enter the spam sender",Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                db.execSQL("insert into spam values('"+spamSender+"')");
                MainActivity.company.add(spamSender);
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "Sender added to spam", Toast.LENGTH_SHORT).show();


        }

    }
}
