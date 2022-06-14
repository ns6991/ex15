package com.example.ex15;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

public class PreviousOrders extends AppCompatActivity implements  AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener{
    Intent si;
    Spinner spin;
    ListView lv;
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;

    TextView wkrN, resN, fm,mm,em,dr,dess,date,time;

    ArrayList<String> tbl;
    ArrayList<String> cardIDs;
    ArrayAdapter<String> adp;

    String userKeyId = "";
    final String[] sortAD = {"Date", "Name A→Z", "Restaurant A→Z"};
    String sort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_previous_orders);

        lv = (ListView) findViewById(R.id.ol);
        wkrN = (TextView)findViewById(R.id.wrkO);
        resN = (TextView)findViewById(R.id.resO);
        fm = (TextView)findViewById(R.id.fmO);
        mm = (TextView)findViewById(R.id.mmO);
        em= (TextView)findViewById(R.id.exmO);
        dr = (TextView)findViewById(R.id.dO);
        dess = (TextView)findViewById(R.id.dsmO);
        date = (TextView)findViewById(R.id.dateO);
        time = (TextView)findViewById(R.id.time);

        hlp = new HelperDB(this);

        lv = (ListView) findViewById(R.id.ol);
        lv.setOnItemClickListener(this);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        update_orders(sort);

        spin = (Spinner) findViewById(R.id.options);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, sortAD);
        spin.setAdapter(adp);
    }

    public void newOrder(View view) {
        si =new Intent(this,newOrder.class);
        startActivity(si);
    }

    public void update_orders(String sortPar) {
        db = hlp.getWritableDatabase();
        tbl = new ArrayList<>();
        cardIDs = new ArrayList<>();
        crsr = db.query(Order1.TABLE_ORDERS, null, null, null, null, null, sortPar);

        int col1 = crsr.getColumnIndex(Order1.KEY_ID);
        int col2 = crsr.getColumnIndex(Order1.RESTAURANT_NAME);
        int col3 = crsr.getColumnIndex(Order1.USER_NAME);
        int col4 = crsr.getColumnIndex(Order1.DATE);

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            int key = crsr.getInt(col1);
            String rName = crsr.getString(col2);
            String uName = crsr.getString(col3);
            String date = crsr.getString(col4);
            String tmp = "" + key + ". " + uName + ", Shop: " + rName + ", At " + date;
            tbl.add(tmp);
            cardIDs.add(key + "");
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        adp = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, tbl);
        lv.setAdapter(adp);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.workersList){
            si =new Intent(this,WorkersList.class);
            startActivity(si);
        }
        else if (item.getItemId() == R.id.credits){
            si = new Intent(this,Credits.class);
            startActivity(si);
        }
        else if (item.getItemId() == R.id.restaurants){
            si = new Intent(this,Restaurant.class);
            startActivity(si);
        }
        if (item.getItemId() == R.id.prevOrder){
            si = new Intent(this,PreviousOrders.class);
            startActivity(si);
        }
        if (item.getItemId() == R.id.home){
            si = new Intent(this,MainActivity.class);
            startActivity(si);
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        userKeyId = cardIDs.get(position);
        String[] selectionArgs = {userKeyId};

        String selection = Meal1.KEY_ID + "=?";
        db = hlp.getReadableDatabase();
        crsr = db.query(Meal1.MEALS_TABLE, null, selection, selectionArgs, null, null, null);
        int col1 = crsr.getColumnIndex(Meal1.FIRST_MEAL);
        int col2 = crsr.getColumnIndex(Meal1.MAIN_MEAL);
        int col3 = crsr.getColumnIndex(Meal1.EXTRA);
        int col4 = crsr.getColumnIndex(Meal1.DESSERT);
        int col5 = crsr.getColumnIndex(Meal1.DRINK);


        crsr.moveToFirst();
        fm.setText("First Meal: \n" + crsr.getString(col1));
        mm.setText("Main Meal: \n" + crsr.getString(col2));
        em.setText("Extra Meal: \n" + crsr.getString(col3));
        dess.setText("Dessert: \n" + crsr.getString(col4));
        dr.setText("Drinks: \n" + crsr.getString(col5));

        db.close();
        crsr.close();

        selection = Order1.KEY_ID + "=?";
        db = hlp.getReadableDatabase();
        crsr = db.query(Order1.TABLE_ORDERS, null, selection, selectionArgs, null, null, null);
        col1 = crsr.getColumnIndex(Order1.USER_NAME);
        col3 = crsr.getColumnIndex(Order1.RESTAURANT_NAME);
        col5 = crsr.getColumnIndex(Order1.DATE);

        crsr.moveToFirst();
        wkrN.setText("Worker: \n" + crsr.getString(col1));
        resN.setText("Restaurant: \n" + crsr.getString(col3));
        date.setText(crsr.getString(col5));


        db.close();
        crsr.close();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (pos == 0) {
            sort = Order1.KEY_ID;
        } else if (pos == 1) {
            sort = Order1.USER_NAME;
        } else if (pos == 2) {
            sort = Order1.RESTAURANT_NAME;
        }
        update_orders(sort);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}