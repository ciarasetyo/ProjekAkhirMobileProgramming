package com.example.binusezyfoods2021;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class OrderActivity extends AppCompatActivity {

    DatabaseHelper mDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_order);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.bar_layout);

        TextView myTitleText = (TextView) findViewById(R.id.homeTitle);
        myTitleText.setText("Binus EzyFoody: Order");

//        Untuk menampilkan back button dan memberikan fungsinya
        ImageButton myImgBtn = (ImageButton) findViewById(R.id.backBtn);
        myImgBtn.setVisibility(View.VISIBLE);

        myImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMainMenu();
            }
        });

//        Untuk mengambil data makanan yang dipilih
        Bundle extras = getIntent().getExtras();
        final String menuSelected = extras.getString("menu");

        mDatabaseHelper = new DatabaseHelper(this);
        Cursor dataUser = mDatabaseHelper.getData("user");
        dataUser.moveToFirst();

        int branch_id = dataUser.getInt(3);
        final Cursor dataCategorySelected = mDatabaseHelper.getItemCategorySelected(branch_id,menuSelected);
        dataCategorySelected.moveToFirst();

        final String dataName = dataCategorySelected.getString(2);
        final int dataStock = dataCategorySelected.getInt(5);
        int dataPrice = dataCategorySelected.getInt(4);
        int dataImage = dataCategorySelected.getInt(6);

        final ImageView imgMenu = (ImageView) findViewById(R.id.imageMenu);
        imgMenu.setImageResource(dataImage);

        final TextView name = (TextView) findViewById(R.id.menuName);
        name.setText(dataName + " (" + dataStock + ")");

        final TextView price = (TextView) findViewById(R.id.menuPrice);
        price.setText("Rp " + dataPrice);

//        fungsi tambah kurang qty
        ImageButton add = (ImageButton) findViewById(R.id.plusBtn);
        ImageButton subtract = (ImageButton) findViewById(R.id.minusBtn);
        final TextView qty = (TextView) findViewById(R.id.qty);

        final Cursor dataCart = mDatabaseHelper.getItemCategorySelectedPreviousData(dataCategorySelected.getInt(0));
        dataCart.moveToFirst();
        if(dataCart.getCount()!=0){
            qty.setText(dataCart.getString(2));
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQty = Integer.parseInt(qty.getText().toString()) + 1;

                if (newQty>dataStock){
                    return;
                }

                qty.setText(String.valueOf(newQty));
            }
        });

        subtract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int newQty = Integer.parseInt(qty.getText().toString()) -1;

                if (newQty<0){
                    return;
                }
                qty.setText(String.valueOf(newQty));
            }
        });

        Button orderMore = (Button) findViewById(R.id.orderMoreBtn);

        orderMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = dataName;
                String jumlah = (String) qty.getText();
                String harga = (String) price.getText();

//                Kembali ke activity sebelumnya, kalau data sudah ada maka qty diupdate jika tidak maka data baru ditambahkan
                if(dataCart.getCount()==0) {
                    openMenuCategoryAdd(dataCategorySelected.getInt(0), Integer.parseInt(jumlah));
                }
                else{
                    openMenuCategoryUpdate(Integer.parseInt(jumlah), dataCart.getInt(0));
                }
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

    }

    public void openMainMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void openMenuCategoryAdd(int ID, int jumlah){

        if(jumlah < 1){
            toastMessage("Your quantity is 0, canceling add order to cart");
            finish();
            return;
        }

        mDatabaseHelper.addDataCart(ID, jumlah);

        finish();
    }

    public void openMenuCategoryUpdate(int jumlah, int cart_id){

        if(jumlah < 1){
            toastMessage("Your quantity is 0, canceling update order to cart");
            finish();
            return;
        }

        mDatabaseHelper.updateName("cart", "quantity", jumlah, cart_id);

        finish();
    }

    public void openMyOrder(){
        Intent intent = new Intent(this, MyOrderActivity.class);
        startActivity(intent);
    }

    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}