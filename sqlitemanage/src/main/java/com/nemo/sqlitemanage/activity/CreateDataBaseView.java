package com.nemo.sqlitemanage.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nemo.sqlitemanage.R;

/**
 * Created by nemo on 2016/7/22 0022.
 */
public class CreateDataBaseView extends AppCompatActivity{

    private EditText db_name;
    private Button db_ok;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_db_main);
        initView();
    }

    private void initView() {
        db_name = (EditText) findViewById(R.id.create_db_text);
        db_ok  = (Button) findViewById(R.id.create_db_btn);

        db_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = db_name.getText().toString();

            }
        });
    }
}
