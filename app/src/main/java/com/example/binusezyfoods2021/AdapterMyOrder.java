package com.example.binusezyfoods2021;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterMyOrder extends RecyclerView.Adapter<AdapterMyOrder.MyViewHolder> {

    int[] data1, data3, data4, data5;
    String[] data2;
    Boolean complete;

    Context context;

    DatabaseHelper mDatabaseHelper;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public AdapterMyOrder(Context ct, int[] gambar, String[] nama, int[] jumlah, int[] harga, int[] ID, Boolean selesai){
        context = ct;
        data1 = gambar;
        data2= nama;
        data3 = jumlah;
        data4 = harga;
        data5 = ID;
        complete = selesai;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_my_order, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        int total = data3[position] * data4[position];
        holder.myText1.setText(data2[position]);
        holder.myText2.setText(data3[position] + " x " + data4[position] + " = " + total);
        holder.myImage.setImageResource(data1[position]);

//        Fungsi ketika tombol hapus ditekan
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(context, "posisi"+data5[position], Toast.LENGTH_LONG);
                removeData(position);
            }
        });

//        kalau activity myOrder maka ada tombol hapus kalau activity complete tidak ada
        if(complete==false){
            holder.removeBtn.setVisibility(View.VISIBLE);
        }
        else{
            holder.removeBtn.setVisibility(View.GONE);
        }
    }

    //    Hapus semua data ketika sudah tekan tombol pay now
    public void removeData(int position){

        mDatabaseHelper = new DatabaseHelper(context);
        mDatabaseHelper.deleteName("cart", data5[position]);
        Toast.makeText(context, "posisi"+data5[position], Toast.LENGTH_LONG);

        Intent intent = new Intent(context, MyOrderActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView myText1, myText2;
        ImageView myImage, tes;
        Button removeBtn;

        public MyViewHolder (@NonNull View itemView){
            super(itemView);
            myText1 = itemView.findViewById(R.id.textMenu);
            myText2 = itemView.findViewById(R.id.textMenuDetail);
            myImage = itemView.findViewById(R.id.imageMenu);
            removeBtn = itemView.findViewById(R.id.removeBtn);
        }
    }
}
