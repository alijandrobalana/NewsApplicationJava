package com.example.newsappusingjava;

// This page is the one that gets the data and pass it to the application tv news category

import java.util.*;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{

    private ArrayList<CategoryModel> categoryModels;
    private Context context;
    private CategoryClickInterface categoryClickInterface;

    public CategoryAdapter(ArrayList<CategoryModel> categoryModel, Context context, CategoryClickInterface categoryClickInterface) {
        this.categoryModels = categoryModel;
        this.context = context;
        this.categoryClickInterface = categoryClickInterface;
    }

    @NonNull
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categories_item, parent,false);
        return new CategoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) { //Passing the data to the CategoryModel
        CategoryModel categoryModel = categoryModels.get(position); //Array
        holder.categoryTV.setText(categoryModel.getCategory()); //Text
        Picasso.get().load(categoryModel.getCategoryImageUrl()).into(holder.categoryIV);//Image
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                categoryClickInterface.onCategoryClick(position);

            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }

    public interface CategoryClickInterface{
        void onCategoryClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{ //The one that is passing the data to the app id

        private TextView categoryTV;
        private ImageView categoryIV;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryTV = itemView.findViewById(R.id.idTVCategory);
            categoryIV = itemView.findViewById(R.id.idIVCategory);
        }
    }
}
