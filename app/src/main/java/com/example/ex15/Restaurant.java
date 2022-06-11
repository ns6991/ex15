package com.example.ex15;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class Restaurant extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener {
    Intent si;
    Spinner spin;
    ListView lv;
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;

    TextView  tax;
    EditText name, ph1 , ph2 ;
    Button update;
    Switch active, filter;

    ArrayList<String> tbl;
    ArrayList<String> cardIDs;
    ArrayAdapter<String> adp;

    String userKeyId = "";
    final String[] sortAD = {"Card-ID", "Name A→Z","Name Z→A"};
    String sort , filterPar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        name = (EditText) findViewById(R.id.nR);
        ph1 = (EditText) findViewById(R.id.p1);
        ph2 = (EditText) findViewById(R.id.p2);
        tax = (TextView) findViewById(R.id.tax);

        update = (Button) findViewById(R.id.updateRes);
        active = (Switch) findViewById(R.id.active);
        filter = (Switch) findViewById(R.id.filter1);

        hlp = new HelperDB(this);

        lv = (ListView) findViewById(R.id.rl);
        lv.setOnItemClickListener(this);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        update_comps( sort);

        spin = (Spinner) findViewById(R.id.option1);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, sortAD);
        spin.setAdapter(adp);
    }


    @Override
    protected void onResume() {
        super.onResume();
        update_comps(sort);
    }

    private void update_comps( String sort) {

        name.setEnabled(false);
        ph1.setEnabled(false);
        ph2.setEnabled(false);
        tax.setEnabled(false);
        active.setEnabled(false);

        db = hlp.getWritableDatabase();
        tbl = new ArrayList<>();
        cardIDs = new ArrayList<>();

        if (filterPar != null)
            crsr = db.query(Restaurant1.TABLE_RESTAURANT, null, Restaurant1.ACTIVE + "=?", new String[]{filterPar}, null, null, sort);
        else
            crsr = db.query(Restaurant1.TABLE_RESTAURANT, null, null, null, null, null, sort);

        int col1 = crsr.getColumnIndex(Restaurant1.KEY_ID);
        int col2 = crsr.getColumnIndex(Restaurant1.NAME);

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            int key = crsr.getInt(col1);
            String name = crsr.getString(col2);
            String tmp = "" + key + ". " + name;
            tbl.add(tmp);
            cardIDs.add(key + "");
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        adp = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, tbl);
        lv.setAdapter(adp);
    }

    public void addRes(View view) {
        si =new Intent(this,newRestaurant.class);
        startActivity(si);
    }

    public void updateButton(View view) {

        if (update.getText().toString().equals("UPDATE")) {
            Toast.makeText(this, "please change the info above as you want", Toast.LENGTH_LONG).show();

            update.setText("SAVE");
            tax.setEnabled(false);
            name.setEnabled(true);
            name.setHint("name");
            ph1.setEnabled(true);
            ph1.setHint("first phone number");
            ph2.setEnabled(true);
            ph2.setHint("second phone number");
            active.setEnabled(true);

        } else if (update.getText().toString().equals("SAVE")) {
            ContentValues cv = new ContentValues();
            db = hlp.getWritableDatabase();
            cv.put(Restaurant1.NAME, name.getText().toString());

            cv.put(Restaurant1.MAIN_PHONE, ph1.getText().toString());
            cv.put(Restaurant1.SECONDARY_PHONE, ph2.getText().toString());
            db.update(Restaurant1.TABLE_RESTAURANT, cv, Worker1.KEY_ID + "=?",  new String[]{userKeyId});

            db.close();

            Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_LONG).show();
            update.setText("UPDATE");
            name.setEnabled(false);
            ph1.setEnabled(false);
            ph2.setEnabled(false);
            active.setEnabled(false);

        }


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

        name.setEnabled(false);
        ph1.setEnabled(false);
        ph2.setEnabled(false);
        tax.setEnabled(false);
        active.setEnabled(false);

        userKeyId = cardIDs.get(position);
        String[] selectionArgs = {userKeyId};

        db = hlp.getReadableDatabase();
        crsr = db.query(Restaurant1.TABLE_RESTAURANT, null, Restaurant1.KEY_ID + "=?", selectionArgs, null, null, null);
        int col0 = crsr.getColumnIndex(Restaurant1.KEY_ID);
        int col1 = crsr.getColumnIndex(Restaurant1.NAME);
        int col2 = crsr.getColumnIndex(Restaurant1.MAIN_PHONE);
        int col3 = crsr.getColumnIndex(Restaurant1.SECONDARY_PHONE);
        int col4 = crsr.getColumnIndex(Restaurant1.ACTIVE);

        crsr.moveToFirst();

        name.setText(crsr.getString(col1));
        ph1.setText(crsr.getString(col2));
        ph2.setText(crsr.getString(col3));
        tax.setText("tax number: " +crsr.getString(col0));

        active.setChecked(crsr.getString(col4).equals("1"));

        db.close();
        crsr.close();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (pos == 0) {
            sort = Restaurant1.KEY_ID;
        } else if (pos == 1) {
            sort = Restaurant1.NAME;
        } else if (pos == 2) {
            sort = Restaurant1.NAME+ " DESC";
        }
        update_comps(sort);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    public void isClicked1(View view) {
        if (filter.isChecked()) {
            filterPar ="1";
        }
        else
            filterPar = "0";
        update_comps(sort);
    }
}