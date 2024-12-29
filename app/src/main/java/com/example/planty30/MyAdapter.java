package com.example.planty30;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private Context context;
    private List<DataClass5> dataList;
    public MyAdapter(Context context, List<DataClass5> dataList) {
        this.context = context;
        this.dataList = dataList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);
        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(dataList.get(position).getDataImg()).into(holder.photoinrc);
        holder.nameinrc.setText(dataList.get(position).getDataName());
        holder.priceinrc.setText("₹"+dataList.get(position).getDataPrice());
        holder.tempinrc.setText(dataList.get(position).getDataTemp());
        holder.waterinrc.setText(dataList.get(position).getDataWater());
        holder.descinrc.setText(dataList.get(position).getDataDesc());

        holder.photoinrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,DetailActivity.class);
                intent.putExtra("Image",dataList.get(holder.getAdapterPosition()).getDataImg());
                intent.putExtra("Name",dataList.get(holder.getAdapterPosition()).getDataName());
                intent.putExtra("Price",dataList.get(holder.getAdapterPosition()).getDataPrice());
                intent.putExtra("Temp",dataList.get(holder.getAdapterPosition()).getDataTemp());
                intent.putExtra("Water",dataList.get(holder.getAdapterPosition()).getDataWater());
                intent.putExtra("Desc",dataList.get(holder.getAdapterPosition()).getDataDesc());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void searchDataList(ArrayList<DataClass5> searchList){

        dataList = searchList;
        notifyDataSetChanged();

    }
}

class MyViewHolder extends RecyclerView.ViewHolder{

    ImageView photoinrc;
    TextView nameinrc,priceinrc,tempinrc,waterinrc,descinrc;
    CardView recCard;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);

        photoinrc = itemView.findViewById(R.id.photoinrc);
        nameinrc = itemView.findViewById(R.id.nameinrc);
        priceinrc = itemView.findViewById(R.id.priceinrc);
        tempinrc = itemView.findViewById(R.id.tempinrc);
        waterinrc = itemView.findViewById(R.id.waterinrc);
        descinrc = itemView.findViewById(R.id.descinrc);

    }
}
