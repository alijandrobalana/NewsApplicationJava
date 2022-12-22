package com.example.newsappusingjava;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsappusingjava.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryAdapter.CategoryClickInterface {

    //API Key
    //5a793178c1f044b0b29c906b6cf94816

    private RecyclerView news, category;
    private ProgressBar loadingPB;
    private ArrayList<Articles> articlesArrayList;
    private ArrayList<CategoryModel> categoryModelArrayList;
    private CategoryAdapter categoryAdapter;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        news = findViewById(R.id.idRVNews);
        category = findViewById(R.id.idRVCategories);
        loadingPB = findViewById(R.id.idPBLoading);
        articlesArrayList = new ArrayList<>();
        categoryModelArrayList = new ArrayList<>();
        newsAdapter = new NewsAdapter(articlesArrayList, this);
        categoryAdapter = new CategoryAdapter(categoryModelArrayList,this,this::onCategoryClick);
        news.setLayoutManager(new LinearLayoutManager(this));
        news.setAdapter(newsAdapter);
        category.setAdapter(categoryAdapter);
        getCategories();//Getting the picked category
        getNews("All");//Fetching all the News
        newsAdapter.notifyDataSetChanged(); //Notify the Adapter that the Data has been changed



    }

    //Getting the data for the Category
    private void getCategories(){

        //Category
        categoryModelArrayList.add(new CategoryModel("All", "https://images.unsplash.com/photo-1530303263041-b5ca33678f04?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80"));
        categoryModelArrayList.add(new CategoryModel("Technology", "https://images.unsplash.com/photo-1488590528505-98d2b5aba04b?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80"));
        categoryModelArrayList.add(new CategoryModel("Science", "https://images.unsplash.com/photo-1507668077129-56e32842fceb?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=387&q=80"));
        categoryModelArrayList.add(new CategoryModel("Sports", "https://images.unsplash.com/photo-1461896836934-ffe607ba8211?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80"));
        categoryModelArrayList.add(new CategoryModel("General", "https://images.unsplash.com/photo-1512314889357-e157c22f938d?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=871&q=80"));
        categoryModelArrayList.add(new CategoryModel("Business", "https://images.unsplash.com/photo-1591696205602-2f950c417cb9?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80"));
        categoryModelArrayList.add(new CategoryModel("Entertainment", "https://images.unsplash.com/photo-1598899134739-24c46f58b8c0?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=856&q=80"));
        categoryModelArrayList.add(new CategoryModel("Health", "https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?ixlib=rb-4.0.3&ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&auto=format&fit=crop&w=870&q=80"));

        categoryAdapter.notifyDataSetChanged();//will notify the adapter that the data inside has been change
    }

    //Getting the data for the News and determining if the category is for specific category
    private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);//recall the display
        articlesArrayList.clear(); //Clear the ArrayList
        //Link of the specific Category API
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=ph&category="+ category + "&apikey=5a793178c1f044b0b29c906b6cf94816";
        //Link for all the news API
        String url = "https://newsapi.org/v2/top-headlines?country=ph&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apikey=5a793178c1f044b0b29c906b6cf94816";
        String BASE_URL = "https://newsapi.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GetAPI retrofitAPI = retrofit.create(GetAPI.class);
        Call<NewsModal> call;

        //Connecting to the Interface GetAPI and determining if the news is for all or specific category
        if(category.equals("All")){

            call = retrofitAPI.getAllNews(url);

        }else{

            call = retrofitAPI.getNewsByCategory(categoryURL);

        }

        call.enqueue(new Callback<NewsModal>() {
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) { //will be call if its a successful response
                //Storing it in the NewsModal Class
                NewsModal newsModal = response.body();
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModal.getArticles();

                for(int i = 0; i < articles.size(); i++){

                    //Pass all the articles data to the articles Array List
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(),
                                                       articles.get(i).getDescription(),
                                                       articles.get(i).getUrl(),
                                                       articles.get(i).getUrlToImage(),
                                                       articles.get(i).getContent()));

                }

                //Notify the Adapter
                newsAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(Call<NewsModal> call, Throwable t) { //will be call if there is any error in response or in connecting with API
                Toast.makeText(MainActivity.this,"Fail to get news", Toast.LENGTH_SHORT). show();
            }
        });

    }
    //When clicking a specific category
    @Override
    public void onCategoryClick(int position) {
        String category = categoryModelArrayList.get(position).getCategory();
        //The picked category
        getNews(category);
    }
}