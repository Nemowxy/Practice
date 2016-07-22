package com.nemo.sqlitemanage;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.nemo.sqlitemanage.utils.DBHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DBHelper helper = new DBHelper(getApplicationContext(), "nemo");
        SQLiteDatabase sd = helper.getDatabase();
    }

    public void buttonClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.create_table:

                break;
            case R.id.delete_table:

                break;
            case R.id.insert:

                break;
            case R.id.delete:

                break;
            case R.id.update:

                break;
            case R.id.find:

                break;
        }
    }
}
