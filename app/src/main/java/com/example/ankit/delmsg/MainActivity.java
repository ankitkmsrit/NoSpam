package com.example.ankit.delmsg;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.service.notification.NotificationListenerService;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    static List<String> company = new ArrayList<>();
    SQLiteDatabase db;
    Button show;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db=openOrCreateDatabase("Company",MODE_PRIVATE,null);
        db.execSQL("create table if not exists spam (name text)");
        Cursor dbcursor=db.rawQuery("select * from spam",null);

        if(dbcursor.getCount()!=0) {
            dbcursor.moveToFirst();
            String temp;
            do {
                temp = dbcursor.getString(0);
                company.add(temp);
            } while (dbcursor.moveToNext());
        }

    }

    /*public void Send(View view) {
        String number = "917204370020";
        String msg = "This is a test message";
        SmsManager smsManager = SmsManager.getDefault();
        try {
            smsManager.sendTextMessage(number, null, msg, null, null);
            Log.i("Msg", "Sent successfully");
            Toast.makeText(MainActivity.this,"Message sent successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error in sending message", Toast.LENGTH_SHORT).show();
        }

    }*/


    public void deleteMsg(View view) {
        String temp;

        try {
            //grantUriPermission("com.android.message",Uri.parse("content://sms/inbox"),0);
            Cursor cursor = getContentResolver().query(Uri.parse("content://sms"), null, null, null, null);

            int flag=0;

            if (cursor.moveToFirst()) {
                do {

                    String id = cursor.getString(0);
                    String add = cursor.getString(2);
                    //Log.i("logmsg", id+" "+add);
                    for(int i=0;i<company.size();i++){

                        if(company.get(i).equals(add)){
                            try {
                                getContentResolver().delete(Uri.parse("content://sms/" + id), null, null);
                                Toast.makeText(MainActivity.this,"Spam deleted successfully",Toast.LENGTH_SHORT).show();
                                flag=1;
                            }

                            catch (Exception e){
                                Toast.makeText(MainActivity.this,"Error in deleting spam",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } while (cursor.moveToNext());
            }

            if(flag==0){
                Toast.makeText(this,"No message to delete",Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.add_spam){
            Intent intent = new Intent(MainActivity.this,AddSpamSender.class);
            startActivity(intent);

        }

        else if (id == R.id.show_spam){

                Intent intent = new Intent(MainActivity.this,ShowSpammer.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}

