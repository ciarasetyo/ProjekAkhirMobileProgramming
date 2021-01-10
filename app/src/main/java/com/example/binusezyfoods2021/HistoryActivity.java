package com.example.binusezyfoods2021;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    ListView transList;
    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.bar_layout);

//        Mengubah title toolbar
        TextView myTitleText = (TextView) findViewById(R.id.homeTitle);
        myTitleText.setText("Binus EzyFoody: History");

        ImageButton myImgBtn = (ImageButton) findViewById(R.id.backBtn);
        myImgBtn.setVisibility(View.VISIBLE);

        myImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainMenu();
            }
        });

//        Mengubah tulisan button pada pada toolbar
        Button barBtn = (Button) findViewById(R.id.homeBtn);
        barBtn.setVisibility(View.GONE);

        transList = (ListView) findViewById(R.id.nearestList);

        mDatabaseHelper = new DatabaseHelper(this);
        final Cursor dataTrans = mDatabaseHelper.getData("transactions");

        final ArrayList<Integer> dataID = new ArrayList<>();

        final ArrayList<String> dataFinal = new ArrayList<>();

        while(dataTrans.moveToNext()){

            Cursor dataDetail = mDatabaseHelper.getItemDetail(dataTrans.getInt(0));
            int count = dataDetail.getCount();

            dataID.add(dataTrans.getInt(0));

            String input = "\n" + dataTrans.getString(1) + " at " + dataTrans.getString(2) + "\n" + count + " Item(s)\n\nCLICK FOR DETAIL\n";
            dataFinal.add(input);
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, dataFinal);
        transList.setAdapter(arrayAdapter);

        transList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openDetail(dataID.get(position));
            }
        });

    }

    public void openMainMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openDetail(int trans_id){
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("trans_id", trans_id);
        startActivity(intent);
    }
}