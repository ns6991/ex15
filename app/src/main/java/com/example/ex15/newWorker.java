package com.example.ex15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class newWorker extends AppCompatActivity {

    SQLiteDatabase db;
    HelperDB hlp;
    Intent si;
    EditText idET, fnameET, lnameET, companyET, phoneET;
    String id, a;
    ContentValues cv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_worker);
        hlp = new HelperDB(this);

        idET = (EditText) findViewById(R.id._wid);
        fnameET = (EditText) findViewById(R.id.fn);
        lnameET = (EditText) findViewById(R.id.ln);
        companyET = (EditText) findViewById(R.id.comp);
        phoneET = (EditText) findViewById(R.id.pn);

        cv = new ContentValues();
    }


    public void cencelbk(View view) {
        si =new Intent(this,WorkersList.class);
        startActivity(si);
    }

    public void newWorOC(View view) {
        if (checkValid()) {

            cv.put(Worker1.WORKER_ID, idET.getText().toString());
            cv.put(Worker1.FIRST_NAME, fnameET.getText().toString());
            cv.put(Worker1.LAST_NAME, lnameET.getText().toString());
            cv.put(Worker1.COMPANY_NAME, companyET.getText().toString());
            cv.put(Worker1.PHONE_NUMBER, phoneET.getText().toString());
            cv.put(Worker1.ACTIVE, "1");

            db = hlp.getWritableDatabase();
            db.insert(Worker1.WORKERS_TABLE, null, cv);
            db.close();

            Toast.makeText(this, "Saved Successfully!", Toast.LENGTH_LONG).show();
            si =new Intent(this,WorkersList.class);
            startActivity(si);
        }
    }
    private boolean checkValid(){
        int no =0;
        if (!isValidId(idET.toString())) {
            idET.setError("enter valid id");
            no++;
        }
        if (!(phoneET.getText().length() == 10)) {
            phoneET.setError("Must Be 10 Digits Long");
            no++;
        }
        if (fnameET.getText().length() == 0) {
            fnameET.setError("can't be empty");
            no++;
        }
        if (lnameET.getText().length() == 0) {
            lnameET.setError("can't be empty");
            no++;
        }
        if (companyET.getText().length() == 0) {
            companyET.setError("can't be empty");
            no++;
        }


        if(no!=0){
            Toast.makeText(this, "Error, please enter again..", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }














    private boolean isValidId(String id){
        if(id.length()>9)return false;
        else{
            for(int i=0; i < 9-id.length() ;i++){
                id = "0"+id;
            }
        }
        int sum =0;
        int temp =0;
        for(int i =0;i<id.length();i++){
            temp =0;
            if(i%2 ==0){
                temp = Integer.parseInt(id.charAt(i)+"");
                if(temp>9) sum+= ((temp/10 )+ (temp%10));
                else sum+= temp;
            }
            else{
                temp = (Integer.parseInt(id.charAt(i)+""))*2;
                if(temp>9) sum+= ((temp/10 )+ (temp%10));
                else sum+= temp;
            }
        }
        if(sum%10==0)return true;
        return false;
    }


}