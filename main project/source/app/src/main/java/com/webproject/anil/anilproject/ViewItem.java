package com.webproject.anil.anilproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ViewItem extends AppCompatActivity implements AdapterCallback{
    String url;
    List<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;

    @Override
    public void onClickCallback(int itemModel) {
        //Toast.makeText(this, itemModel, Toast.LENGTH_SHORT).show();
        Intent i=new Intent(ViewItem.this, ViewItem.class);
        Log.d("id","hello");
        Log.d("id",String.valueOf(itemModel));
        i.putExtra("id",String.valueOf(itemModel));
        startActivity(i);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_view);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the productlist
        productList = new ArrayList<>();

        Bundle bundle = getIntent().getExtras();
        String text = bundle.getString("id");

        String url = "http://api.walmartlabs.com/v1/items/"+text+"?format=json&apiKey=ygbtydqxg9d7ehfv488deu4p";
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                System.out.println(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final JSONObject jsonResult;

                final String result = response.body().string();
                try {
                    Log.d("query", url);
                    Log.d("out", result);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            setData(result);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setData(String Data) {
        JSONObject c;
        JSONArray items;
        TextView textViewTitle, textViewShortDesc, textViewRating, textViewPrice;
        ImageView imageView;Button btnBuy;
        textViewTitle =(TextView) findViewById(R.id.textViewTitle);
        textViewShortDesc =(TextView) findViewById(R.id.textViewShortDesc);
        textViewRating = (TextView)findViewById(R.id.textViewRating);
        textViewPrice = (TextView)findViewById(R.id.textViewPrice);
        imageView = (ImageView)findViewById(R.id.imageView);
        btnBuy=(Button)findViewById(R.id.btnView);
        try {
            c = new JSONObject(Data.toString());

            textViewTitle.setText(c.getString("name"));
            textViewShortDesc.setText(c.getString("shortDescription"));
            textViewRating.setText(c.has("customerRating") ? c.getString("customerRating") : "0.00");
            textViewPrice.setText((Double.toString(c.getDouble("salePrice"))));
            Glide.with(ViewItem.this)
                    .load(c.getString("mediumImage"))
                    .into(imageView);
            url=c.getString("productUrl");

            String url = "http://api.walmartlabs.com/v1/nbp?apiKey=ygbtydqxg9d7ehfv488deu4p&itemId="+c.getString("itemId");
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(url)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println(e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final JSONObject jsonResult;

                    final String result = response.body().string();
                    try {
                        Log.d("query", url);
                        Log.d("out", result);
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                ViewRecommend(result);
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Throwable t) {
            Log.e("My App", t.getMessage());

        }
    }
    public void ViewRecommend(String result){
        JSONArray js;
        JSONArray items;
        try {
            js = new JSONArray(result.toString());
            items=js;
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            for(int i=0;i<items.length();i++) {
                JSONObject c = items.getJSONObject(i);
                productList.add(new Product(
                        c.getInt("itemId"),
                        c.getString("name"),
                        c.getString("shortDescription"),
                        c.has("customerRating")?Double.parseDouble(c.getString("customerRating")):0.00,
                        c.getDouble("salePrice"),
                        c.getString("mediumImage"),
                        c.has("msrp")?c.getDouble("msrp"):0.00
                ));
            }

            ProductsAdapter adapter = new ProductsAdapter(ViewItem.this, productList,this);
            recyclerView.setAdapter(adapter);
        }
        catch (Throwable t) {
            Log.e("My App1", t.getMessage());
        }
    }
    public void buyProduct(View v)
    {
        Intent i2=new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(i2);
    }
}