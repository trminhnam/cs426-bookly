package com.example.bookly.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookly.Model.NotificationModel;
import com.example.bookly.Model.RequestModel;
import com.example.bookly.R;

import java.util.ArrayList;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.viewHolder>{
    ArrayList<RequestModel> requestList;
    Context context;

    public RequestAdapter(ArrayList<RequestModel> requestList, Context context) {
        this.requestList = requestList;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.rv_item_request, parent, false);

        return new RequestAdapter.viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestAdapter.viewHolder holder, int position) {
        RequestModel requestModel = requestList.get(position);

        holder.profileIv.setImageResource(requestModel.getProfile());
        holder.categoriesTv.setText(requestModel.getCategories());
        holder.timeTv.setText(requestModel.getTime());
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        ImageView profileIv;
        TextView categoriesTv, timeTv;
        public viewHolder(@NonNull View itemView){
            super(itemView);
            profileIv = itemView.findViewById(R.id.profile_image_request);
            categoriesTv = itemView.findViewById(R.id.categoriesTv);
            timeTv = itemView.findViewById(R.id.timeRequestTv);
        }
    }
}
