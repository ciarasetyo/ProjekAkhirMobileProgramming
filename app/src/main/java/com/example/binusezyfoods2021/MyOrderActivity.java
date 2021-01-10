package com.example.binusezyfoods2021;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MyOrderActivity extends AppCompatActivity {
    DatabaseHelper mDatabaseHelper;
    RecyclerView recyclerView;
    int total = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.bar_layout);

        recyclerView = findViewById(R.id.recyclerView2);

//        Mengubah title toolbar
        TextView myTitleText = (TextView) findViewById(R.id.homeTitle);
        myTitleText.setText("Binus EzyFoody: My Order");

//        Mengubah tulisan button pada pada toolbar
        Button barBtn = (Button) findViewById(R.id.homeBtn);
        barBtn.setText("Pay Now");

        mDatabaseHelper = new DatabaseHelper(this);
        Cursor dataCart = mDatabaseHelper.getData("cart");
        int count = dataCart.getCount();

//        Kalau blom ada yang diorder tombol tidak muncul
        if(count==0){
            barBtn.setVisibility(View.GONE);
        }
        else{
            barBtn.setVisibility(View.VISIBLE);
        }



//        Untuk menampilkan back button dan memberikan fungsinya
        ImageButton myImgBtn = (ImageButton) findViewById(R.id.backBtn);
        myImgBtn.setVisibility(View.VISIBLE);

        myImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        int[] dataImage = new int[count];
        String[] dataName = new String[count];
        int[] dataQty = new int[count];
        int[] dataPrice = new int[count];
        int[] dataID = new int[count];

        for(int i=0; i<count; i++){
            dataCart.moveToNext();

            Cursor dataMenu = mDatabaseHelper.getItemByID("menu", dataCart.getInt(1));
            dataMenu.moveToFirst();

            dataName[i] = dataMenu.getString(2);
            dataPrice[i] = dataMenu.getInt(4);
            dataQty[i] = dataCart.getInt(2);
            dataImage[i] = dataMenu.getInt(6);
            dataID[i] = dataCart.getInt(0);

            int temp = dataQty[i] * dataPrice[i];
            total = total + temp;

        }

        TextView totalText = (TextView) findViewById(R.id.totalText);
        totalText.setText(String.valueOf(total));

        barBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPay(total);
            }
        });

//        Menggunakan adapter untuk recycler view
        AdapterMyOrder adapterMyOrder = new AdapterMyOrder(this, dataImage, dataName, dataQty, dataPrice, dataID, false);
        recyclerView.setAdapter(adapterMyOrder);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void back(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openPay(int total){
        Cursor dataUser = mDatabaseHelper.getData("user");
        dataUser.moveToFirst();

        if(dataUser.getInt(2) < total){
            toastMessage("Your money not enough, Please top up");
            return;
        }
        else{
            mDatabaseHelper.updateName("user", "cash", dataUser.getInt(2)-total, 1);

            Cursor dataBranch = mDatabaseHelper.getItemByID("branch", dataUser.getInt(3));
            dataBranch.moveToFirst();

            mDatabaseHelper.addDataTransaction(new SimpleDateFormat("EEE, dd MMM yyyy   HH:mm:ss").format(Calendar.getInstance().getTime()), dataBranch.getString(1));

            Cursor dataTrans = mDatabaseHelper.getData("transactions");
            dataTrans.moveToLast();

            Cursor dataCart = mDatabaseHelper.getData("cart");

            while (dataCart.moveToNext()){
                Cursor dataMenu = mDatabaseHelper.getItemByID("menu", dataCart.getInt(1));
                dataMenu.moveToFirst();

                mDatabaseHelper.updateName("menu", "stock", dataMenu.getInt(5)-dataCart.getInt(2), dataMenu.getInt(0));
                mDatabaseHelper.addDataDetail(dataTrans.getInt(0), dataCart.getInt(1), dataCart.getInt(2));
                mDatabaseHelper.deleteName("cart", dataCart.getInt(0));
            }

        }

        Intent intent = new Intent(this, CompleteActivity.class);
        startActivity(intent);
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        back();
    }
}