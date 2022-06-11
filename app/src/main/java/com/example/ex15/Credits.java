package com.example.ex15;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class Credits extends AppCompatActivity {

    Intent si;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
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
        else if (item.getItemId() == R.id.prevOrder){
            si = new Intent(this,PreviousOrders.class);
            startActivity(si);
        }
        else if (item.getItemId() == R.id.home){
            si = new Intent(this,MainActivity.class);
            startActivity(si);
        }
        return true;
    }
}