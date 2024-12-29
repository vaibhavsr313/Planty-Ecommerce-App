package com.example.planty30;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public TextView txtProductName,txtProductPrice,txtProductQuantity;
    ImageView removebuttonincart;
    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        txtProductName = itemView.findViewById(R.id.nameinrc_cart);
        txtProductPrice = itemView.findViewById(R.id.priceinrc_cart);
        txtProductQuantity = itemView.findViewById(R.id.qtyinrc_cart);
        removebuttonincart = itemView.findViewById(R.id.removebuttonincart);

    }

    @Override
    public void onClick(View view)
    {
        itemClickListner.onClick(view, getAdapterPosition(),false);
    }

    public void setItemClickListner(ItemClickListner itemClickListner)
    {
        this.itemClickListner = itemClickListner;



    }
}
