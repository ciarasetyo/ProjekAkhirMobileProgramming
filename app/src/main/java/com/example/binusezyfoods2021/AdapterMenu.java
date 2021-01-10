package com.example.binusezyfoods2021;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterMenu extends RecyclerView.Adapter<AdapterMenu.MyViewHolder> {

    String data1[], valueName;
    int images[], data2[], data3[];
    Context context;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public AdapterMenu(Context ct, String nama[], int harga[], int[] stock, int img[], String value){
        context = ct;
        data1 = nama;
        data2= harga;
        data3 = stock;
        images = img;
        valueName = value;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.row_category, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.myText1.setText((data1[position]) + "\n(" + data3[position] +")");
        holder.myText2.setText("Rp " + data2[position]);
        holder.myImage.setImageResource((images[position]));

//        Untuk pindah ke orderActivity
        holder.myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openOrder(data1[position]);
            }
        });
    }

    public void openOrder(String menu){
        Intent intent = new Intent(context, OrderActivity.class);
        intent.putExtra("menu", menu);
//        intent.putExtra("value", valueName);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return data1.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView myText1, myText2;
        ImageView myImage;

        public MyViewHolder (@NonNull View itemView){
            super(itemView);
            myText1 = itemView.findViewById(R.id.textMenu);
            myText2 = itemView.findViewById(R.id.textMenuPrice);
            myImage = itemView.findViewById(R.id.imageMenu);
        }
    }
}
