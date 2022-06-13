package com.example.ex15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class newRestaurant extends AppCompatActivity {

    SQLiteDatabase db;
    HelperDB hlp;

    EditText nameET, taxET, mPhoneET, sPhoneET;
    ContentValues cv;

    Intent si;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_restaurant);

        hlp = new HelperDB(this);
        db = hlp.getWritableDatabase();
        db.close();

        nameET = (EditText) findViewById(R.id.nameR);
        mPhoneET = (EditText) findViewById(R.id.fp);
        sPhoneET = (EditText) findViewById(R.id.sp);

        cv = new ContentValues();
    }

    public void newResOC(View view) {
        if (checkValid()) {

            cv.put(Restaurant1.NAME, nameET.getText().toString());
            cv.put(Restaurant1.MAIN_PHONE, mPhoneET.getText().toString());
            cv.put(Restaurant1.SECONDARY_PHONE, sPhoneET.getText().toString());
            cv.put(Restaurant1.ACTIVE, "1");

            db = hlp.getWritableDatabase();
            db.insert(Restaurant1.TABLE_RESTAURANT, null, cv);
            db.close();

            Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_LONG).show();
            si =new Intent(this,Restaurant.class);
            startActivity(si);
        }

    }

    private boolean checkValid(){
        int no =0;
        if (nameET.getText().length() == 0) {
            nameET.setError("Can't Be Empty");
            no++;
        }
        if (!(mPhoneET.getText().length() == 10)  || mPhoneET.getText().toString().charAt(0) != '0' || !(mPhoneET.getText().toString().matches("[0-9]+") && mPhoneET.getText().toString().length() > 2)) {

            mPhoneET.setError("Must Be 10 Digits Long");
            no++;
        }
        if (sPhoneET.getText().toString() != "" ) {
            if((sPhoneET.getText().length() <9 || sPhoneET.getText().toString().charAt(0)!='0'|| !(mPhoneET.getText().toString().matches("[0-9]+") && mPhoneET.getText().toString().length() > 2))){
                sPhoneET.setError("Must Be 10 or 9 Digits Long");
                no++;
            }

        }

        if(no!=0){
            Toast.makeText(this, "Error, please enter again..", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    public void cancelBK(View view) {
        si =new Intent(this,Restaurant.class);
        startActivity(si);
    }
}