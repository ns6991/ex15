package com.example.ex15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class newOrder extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    Intent si;

    SQLiteDatabase db;
    HelperDB hlp;
    ContentValues cv;
    Spinner sp;
    EditText first_mealET ,main_mealET, extraET, dessertET, drinkET, worker_numberET;
    ArrayAdapter<String> adp;
    ArrayList<String> tbl;
    ArrayList<String> restIDlist;
    Cursor crsr;
    int ind1, ind2;

    String orderID, orderName, resID ,resName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        first_mealET = (EditText) findViewById(R.id.fm);
        main_mealET = (EditText) findViewById(R.id.mm);
        extraET = (EditText) findViewById(R.id.em);
        dessertET = (EditText) findViewById(R.id.dm);
        drinkET = (EditText) findViewById(R.id.dr);
        worker_numberET = (EditText) findViewById(R.id.wid);

        sp = (Spinner) findViewById(R.id.options);
        sp.setOnItemSelectedListener(this);


        restIDlist = new ArrayList<>();
        restIDlist.add("0");

        db = hlp.getWritableDatabase();
        tbl = new ArrayList<>();
        tbl.add("Choose Restaurant:");
        crsr = db.query(Restaurant1.TABLE_RESTAURANT, null, Restaurant1.ACTIVE + "=?", new String[]{"1"}, null, null, null);

        ind1 = crsr.getColumnIndex(Restaurant1.NAME);
        ind2 = crsr.getColumnIndex(Restaurant1.KEY_ID);

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            int key = crsr.getInt(ind2);
            String name = crsr.getString(ind1);
            tbl.add(name);
            restIDlist.add(key+"");
            crsr.moveToNext();
        }

        crsr.close();
        db.close();
        adp = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, tbl);
        sp.setAdapter(adp);

        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();
        cv = new ContentValues();

    }

    public void submitOrder(View view) {
        if(checkValid() && checkValidRes()){
            cv.put(Meal1.FIRST_MEAL, first_mealET.getText().toString());
            cv.put(Meal1.MAIN_MEAL, main_mealET.getText().toString());
            cv.put(Meal1.EXTRA, extraET.getText().toString());
            cv.put(Meal1.DESSERT, dessertET.getText().toString());
            cv.put(Meal1.DRINK,drinkET.getText().toString());

            db = hlp.getWritableDatabase();
            db.insert(Meal1.MEALS_TABLE, null, cv);
            db.close();

            crsr = db.query(Worker1.WORKERS_TABLE, null, Worker1.KEY_ID + "=?", new String[]{worker_numberET.getText().toString()}, null, null, null);
            int col1 = crsr.getColumnIndex(Worker1.FIRST_NAME);
            crsr.moveToFirst();
            orderName = crsr.getString(col1);
            crsr.close();

            db = hlp.getWritableDatabase();
            cv.put(Order1.USER_ID, orderID);
            cv.put(Order1.RESTAURANT_ID, resID);
            cv.put(Order1.RESTAURANT_NAME,resName);
            cv.put(Order1.USER_NAME,orderName);



            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy HH:mm");
            cv.put(Order1.DATE,formatter.format(calendar.getTime()));


            db = hlp.getWritableDatabase();
            db.insert(Order1.TABLE_ORDERS, null, cv);
            db.close();


            si =new Intent(this,MainActivity.class);
            startActivity(si);
        }
        else{
            Toast.makeText(this, "ERROR WITH SUBMIT ORDER", Toast.LENGTH_LONG).show();
        }

    }



    private boolean checkValidRes(){
        db = hlp.getWritableDatabase();
        crsr = db.query(Worker1.WORKERS_TABLE, null, Worker1.KEY_ID + "=?", new String[]{worker_numberET.getText().toString()}, null, null, null);
        int col1 = crsr.getColumnIndex(Worker1.ACTIVE);
        crsr.moveToFirst();
        try {
            if (crsr.getString(col1).equals("0")){
                Toast.makeText(this,"User Don't work", Toast.LENGTH_LONG).show();
            }
            else{
                orderID = worker_numberET.getText().toString();
                crsr.close();
                db.close();
                return true;
            }
        }catch (Exception e) {
            Toast.makeText(this, "User Don't exists", Toast.LENGTH_LONG).show();
        }
        crsr.close();
        db.close();
        return false;

    }


    private boolean checkValid(){
        int no =0;
        if (first_mealET.getText().length() == 0) {
            first_mealET.setText("NO");
            no++;
        }
        if (main_mealET.getText().length() == 0) {
            main_mealET.setText("NO");
            no++;
        }
        if (extraET.getText().length() == 0) {
            extraET.setText("NO");
            no++;
        }
        if (dessertET.getText().length() == 0) {
            dessertET.setText("NO");
            no++;
        }
        if (drinkET.getText().length() == 0) {
            drinkET.setText("NO");
            no++;
        }
        if (worker_numberET.getText().length() == 0) {
            worker_numberET.setError("Can't Be Empty.");
            return false;
        }
        if(no==5){
            Toast.makeText(this, "you have to order at least one thing..", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position==0){
            resID =null;
        }else{
            resID = restIDlist.get(position);
            resName = tbl.get(position);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    public void cancelBK(View view) {
        si =new Intent(this,PreviousOrders.class);
        startActivity(si);
    }
}