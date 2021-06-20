package com.hj.homecleanproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FamilyAdapter extends RecyclerView.Adapter<FamilyAdapter.MyViewHolder> {

    ArrayList<FamilyItem> familyItemArrayList;

    public FamilyAdapter(ArrayList<FamilyItem> familyItemArrayList) {
        this.familyItemArrayList = familyItemArrayList;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.family_item,parent,false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.txt_Name.setText(familyItemArrayList.get(position).getName());
        holder.txt_group.setText(familyItemArrayList.get(position).getFamily_group());
        holder.txt_phone.setText(familyItemArrayList.get(position).getPhone_Number());
        holder.iv_photo.setImageResource(familyItemArrayList.get(position).getImage());

    }

    @Override
    public int getItemCount() {
        return familyItemArrayList.size();
    }

    class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView txt_Name;
        TextView txt_group;
        TextView txt_phone;
        ImageView iv_photo;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txt_Name =itemView.findViewById(R.id.name);
            this.txt_group =itemView.findViewById(R.id.family_group);
            this.txt_phone =itemView.findViewById(R.id.phone_Number);
            iv_photo= itemView.findViewById(R.id.iv_photo);
        }
    }
}


