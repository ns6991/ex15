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

public class WorkersList extends AppCompatActivity implements AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener {
    Intent si;
    Spinner spin;
    ListView lv;
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    EditText fn, ln, phN, comp;
    TextView  idW, cardNum;
    Switch active, filter;
    Button update;

    ArrayList<String> tbl;
    ArrayList<String> cardIDs;
    ArrayAdapter<String> adp;

    String userKeyId = "";
    final String[] sortAD = {"Card-ID", "First Name A→Z", "Last Name A→Z", "Company A→Z"};
    String sort,filterPar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workers_list);

        fn = (EditText) findViewById(R.id.fnameW);
        ln = (EditText) findViewById(R.id.snameW);
        idW = (TextView) findViewById(R.id.idW);
        phN = (EditText) findViewById(R.id.phoneW);
        comp = (EditText) findViewById(R.id.compW);
        cardNum = (TextView) findViewById(R.id.cardNumW);

        update = (Button) findViewById(R.id.updateWo);
        active = (Switch) findViewById(R.id.switch1);
        filter = (Switch) findViewById(R.id.filter);

        hlp = new HelperDB(this);

        lv = (ListView) findViewById(R.id.wl);
        lv.setOnItemClickListener(this);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        update_users( sort);

        spin = (Spinner) findViewById(R.id.options2);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter<String> adp = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, sortAD);
        spin.setAdapter(adp);

    }

    @Override
    protected void onResume() {
        super.onResume();
        update_users(sort);
    }

    public void update_users( String sortPar) {
        fn.setEnabled(false);
        ln.setEnabled(false);
        idW.setEnabled(false);
        phN.setEnabled(false);
        comp.setEnabled(false);
        active.setEnabled(false);
        cardNum.setEnabled(false);

        db = hlp.getWritableDatabase();
        tbl = new ArrayList<>();
        cardIDs = new ArrayList<>();


        if (filterPar != null)
            crsr = db.query(Worker1.WORKERS_TABLE, null, Worker1.ACTIVE + "=?", new String[]{filterPar}, null, null, sort);
        else
            crsr = db.query(Worker1.WORKERS_TABLE, null, null, null, null, null, sort);

        int col1 = crsr.getColumnIndex(Worker1.KEY_ID);
        int col2 = crsr.getColumnIndex(Worker1.FIRST_NAME);
        int col3 = crsr.getColumnIndex(Worker1.LAST_NAME);

        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            int key = crsr.getInt(col1);
            String fname = crsr.getString(col2);
            String lname = crsr.getString(col3);
            String tmp = "" + key + ". " + fname + " " + lname;
            tbl.add(tmp);
            cardIDs.add(key + "");
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        adp = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, tbl);
        lv.setAdapter(adp);
    }




    public void updateButton(View view) {
        if (update.getText().toString().equals("UPDATE")) {
            update.setText("SAVE");
            Toast.makeText(this, "please change the info above as you want", Toast.LENGTH_LONG).show();
            fn.setEnabled(true);
            ln.setEnabled(true);
            idW.setEnabled(false);
            phN.setEnabled(true);
            comp.setEnabled(true);
            active.setEnabled(true);
            cardNum.setEnabled(false);
            fn.setHint("first name");
            ln.setHint("last name");
            phN.setHint("phone number");
            comp.setHint("company");

        } else if (update.getText().toString().equals("SAVE")) {
            if(checkValid()){
                ContentValues cv = new ContentValues();
                db = hlp.getWritableDatabase();
                cv.put(Worker1.FIRST_NAME, fn.getText().toString());
                cv.put(Worker1.LAST_NAME, ln.getText().toString());
                cv.put(Worker1.COMPANY_NAME, comp.getText().toString());
                cv.put(Worker1.WORKER_ID, idW.getText().toString());
                cv.put(Worker1.PHONE_NUMBER, phN.getText().toString());
                if(active.isChecked()) cv.put(Worker1.ACTIVE,"1");
                else cv.put(Worker1.ACTIVE,"0");
                db.update(Worker1.WORKERS_TABLE, cv, Worker1.KEY_ID + "=?", new String[]{userKeyId});

                db.close();

                Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_LONG).show();
                update.setText("UPDATE");
                fn.setEnabled(false);
                ln.setEnabled(false);
                phN.setEnabled(false);
                comp.setEnabled(false);
                active.setEnabled(false);
            }



        }
    }

    private boolean checkValid(){
        int no =0;
        Boolean b1 = phN.getText().length() == 10;
        Boolean b2 = phN.getText().toString().charAt(0)!='0';
        Boolean b3 = phN.getText().toString().matches("[0-9]+") && phN.getText().toString().length() > 2;
        if (!(phN.getText().length() == 10) || phN.getText().toString().charAt(0)!='0' || !(phN.getText().toString().matches("[0-9]+") && phN.getText().toString().length() > 2)) {
            phN.setError("Must Be 10 Digits Long and start with 0");
            no++;
        }
        if (fn.getText().length() == 0) {
            fn.setError("can't be empty");
            no++;
        }
        if (ln.getText().length() == 0) {
            ln.setError("can't be empty");
            no++;
        }
        if (comp.getText().length() == 0) {
            comp.setError("can't be empty");
            no++;
        }


        if(no!=0){
            Toast.makeText(this, "Error, please enter again..", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void newWorkerOC(View view) {
        si = new Intent(this, newWorker.class);
        startActivity(si);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.workersList) {
            si = new Intent(this, WorkersList.class);
            startActivity(si);
        } else if (item.getItemId() == R.id.credits) {
            si = new Intent(this, Credits.class);
            startActivity(si);
        } else if (item.getItemId() == R.id.restaurants) {
            si = new Intent(this, Restaurant.class);
            startActivity(si);
        }
        if (item.getItemId() == R.id.prevOrder) {
            si = new Intent(this, PreviousOrders.class);
            startActivity(si);
        }
        if (item.getItemId() == R.id.home) {
            si = new Intent(this, MainActivity.class);
            startActivity(si);
        }
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        fn.setEnabled(false);
        ln.setEnabled(false);
        idW.setEnabled(false);
        phN.setEnabled(false);
        comp.setEnabled(false);
        active.setEnabled(false);
        cardNum.setEnabled(false);


        userKeyId = cardIDs.get(position);
        String[] selectionArgs = {userKeyId};

        db = hlp.getReadableDatabase();
        crsr = db.query(Worker1.WORKERS_TABLE, null, Worker1.KEY_ID + "=?", selectionArgs, null, null, null);
        int col0 = crsr.getColumnIndex(Worker1.KEY_ID);
        int col1 = crsr.getColumnIndex(Worker1.FIRST_NAME);
        int col2 = crsr.getColumnIndex(Worker1.LAST_NAME);
        int col3 = crsr.getColumnIndex(Worker1.COMPANY_NAME);
        int col4 = crsr.getColumnIndex(Worker1.WORKER_ID);
        int col5 = crsr.getColumnIndex(Worker1.PHONE_NUMBER);
        int col6 = crsr.getColumnIndex(Worker1.ACTIVE);

        crsr.moveToFirst();

        fn.setText(crsr.getString(col1));
        ln.setText(crsr.getString(col2));
        idW.setText(crsr.getString(col4));
        phN.setText(crsr.getString(col5));
        comp.setText(crsr.getString(col3));
        cardNum.setText("card number: " + crsr.getString(col0));
        active.setChecked(crsr.getString(col6).equals("1"));

        db.close();
        crsr.close();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        if (pos == 0) {
            sort = Worker1.KEY_ID;
        } else if (pos == 1) {
            sort = Worker1.FIRST_NAME;
        } else if (pos == 2) {
            sort = Worker1.LAST_NAME;
        } else if (pos == 3) {
            sort = Worker1.COMPANY_NAME;
        }
        update_users( sort);

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
        update_users(sort);

    }


}