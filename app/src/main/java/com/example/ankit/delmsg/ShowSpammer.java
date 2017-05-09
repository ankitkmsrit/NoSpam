package com.example.ankit.delmsg;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShowSpammer extends AppCompatActivity {

    SQLiteDatabase db;
    ListView listView;
    List<String> company = new ArrayList<>();
    ArrayAdapter<String> adapter;
    String itemName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_spammer);
        db=openOrCreateDatabase("Company",MODE_PRIVATE,null);

        fetchData();
        showListView();

        registerForContextMenu(listView);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                itemName = adapter.getItem(position);
                return false;
            }
        });
    }

    public void fetchData() {
        String l;
        Cursor cursor = db.rawQuery("select * from spam", null);
        if (cursor.getCount() != 0) {
            cursor.moveToFirst();
            do {
                l = cursor.getString(0);
                company.add(l);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(getApplicationContext(), "No labels set for now", Toast.LENGTH_SHORT).show();
        }
    }


    public void showListView(){
        listView=(ListView)findViewById(R.id.list_item);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,company);
        listView.setAdapter(adapter);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, v.getId(), 0, "Delete");
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //return super.onContextItemSelected(item);
        if(item.getTitle() =="Delete"){
            confirmDelete();
        }
        else {
            return false;
        }
        return true;
    }

    public void confirmDelete(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Are you sure to delete ?");
        alertDialog.setPositiveButton("Yes",  new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.remove(itemName);
                adapter.notifyDataSetChanged();
                //String[] labelparts = itemName.split(" ");
                //String labelName = labelparts[0];
                db.execSQL("delete from spam where name='" + itemName + "'");
                Toast.makeText(getApplicationContext(), "Deleted Succesfully", Toast.LENGTH_SHORT).show();
            }
        });

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();

    }
}
