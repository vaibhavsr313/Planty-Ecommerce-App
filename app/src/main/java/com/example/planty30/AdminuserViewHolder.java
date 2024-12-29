package com.example.planty30;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminuserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView nameadmininusers,phoneadmininusers,addressadmininusers,emailadmininusers;
    public ImageView photoadmininusers;
    private ItemClickListner itemClickListner;

    public AdminuserViewHolder(@NonNull View itemView) {
        super(itemView);

        nameadmininusers = itemView.findViewById(R.id.nameadmininusers);
        phoneadmininusers = itemView.findViewById(R.id.phoneadmininusers);
        addressadmininusers = itemView.findViewById(R.id.addressadmininusers);
        emailadmininusers = itemView.findViewById(R.id.emailadmininusers);
        photoadmininusers = itemView.findViewById(R.id.photoadmininusers);

    }


    @Override
    public void onClick(View view) {
        itemClickListner.onClick(view, getAdapterPosition(),false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner)
    {
        this.itemClickListner = itemClickListner;



    }
}
