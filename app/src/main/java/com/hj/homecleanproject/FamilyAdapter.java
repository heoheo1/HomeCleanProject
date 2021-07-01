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
        holder.txt_groupName.setText(familyItemArrayList.get(position).getFamily_group());
        holder.txt_Email.setText(familyItemArrayList.get(position).getEmail());
        holder.txt_Position.setText(familyItemArrayList.get(position).getPosition());
        holder.iv_photo.setImageBitmap(familyItemArrayList.get(position).getBitmap());

    }

    @Override
    public int getItemCount() {
        return familyItemArrayList.size();
    }

    class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView txt_Name;
        TextView txt_groupName;
        TextView txt_Email;
        TextView txt_Position;
        ImageView iv_photo;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.txt_Name =itemView.findViewById(R.id.txt_name);
            this.txt_groupName =itemView.findViewById(R.id.txt_GroupName);
            this.txt_Position =itemView.findViewById(R.id.txt_Position);
            iv_photo= itemView.findViewById(R.id.iv_photo);
        }
    }
}


