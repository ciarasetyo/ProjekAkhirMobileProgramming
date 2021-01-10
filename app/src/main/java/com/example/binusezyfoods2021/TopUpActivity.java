package com.example.binusezyfoods2021;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class TopUpActivity extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_up);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.bar_layout);

        TextView myTitleText = (TextView) findViewById(R.id.homeTitle);
        myTitleText.setText("Binus EzyFoody: Top Up");

        ImageButton backBtn = (ImageButton) findViewById(R.id.backBtn);
        backBtn.setVisibility(View.VISIBLE);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainMenu();
            }
        });

        //        Fungsi button pada toolbar
        Button barBtn = (Button) findViewById(R.id.homeBtn);
        barBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMyOrder();
            }
        });

        Button topUpBtn = (Button) findViewById(R.id.saveTopUp);
        topUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateTopUp();
            }
        });
    }

    public void validateTopUp(){
        EditText topUpQty = (EditText) findViewById(R.id.TopUpQty);
        String input = topUpQty.getText().toString();

        if(input.isEmpty() || Integer.parseInt(input)==0){
            toastMessage("Input must be filled and more than 0");
        }
        else if(Integer.parseInt(input)>2000000){
            toastMessage("Top Up can't be more than 2.000.000");
        }
        else{
            mDatabaseHelper = new DatabaseHelper(this);
            Cursor data = mDatabaseHelper.getData("user");
            data.moveToFirst();

            mDatabaseHelper.updateName("user", "cash",data.getInt(2)+Integer.parseInt(input), 1);

            openMainMenu();
        }

    }

    public void openMainMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void openMyOrder(){
        Intent intent = new Intent(this, MyOrderActivity.class);
        startActivity(intent);
    }
}