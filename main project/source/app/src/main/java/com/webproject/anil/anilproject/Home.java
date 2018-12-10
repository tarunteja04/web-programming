package com.webproject.anil.anilproject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Home extends AppCompatActivity implements AdapterCallback {
    Button cam;
    Button search_stores;
    Bitmap image;
    String imagepath;

    Single<ClassifiedImages> observable;
    private TableLayout mTableLayout;
    TextView addBtn;
    List<Product> productList;

    //the recyclerview
    RecyclerView recyclerView;

    @Override
    public void onClickCallback(int itemModel) {
        //Toast.makeText(this, itemModel, Toast.LENGTH_SHORT).show();
        Intent i=new Intent(Home.this, ViewItem.class);
        Log.d("id","hello");
        Log.d("id",String.valueOf(itemModel));
        i.putExtra("id",String.valueOf(itemModel));
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //initializing the productlist
        productList = new ArrayList<>();
        cam=(Button) findViewById(R.id.cam);
        search_stores=(Button) findViewById(R.id.search_stores);

        observable = Single.create((SingleOnSubscribe<ClassifiedImages>) emitter -> {
            InputStream imageStream = null;
            try {
                imageStream = new FileInputStream(imagepath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream finalImageStream = imageStream;
            IamOptions options = new IamOptions.Builder()
                    .apiKey("dCKQttzpGtH2GqsuWo3wR3dXP9lUmQOIhSx2lBc5XDXc")
                    .build();

            VisualRecognition visualRecognition = new VisualRecognition("2018-03-19", options);
            ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                    .imagesFile(finalImageStream)
                    .imagesFilename("fruitbowl.jpg")
                    .classifierIds(Collections.singletonList("default"))
                    .threshold((float) 0.6)
                    .owners(Collections.singletonList("me"))
                    .build();
            ClassifiedImages classifiedImages = visualRecognition.classify(classifyOptions).execute();
            Log.d("tag","images classified");
            emitter.onSuccess(classifiedImages);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
    public void viewData(String data){
        JSONObject js;
        JSONArray items;
        try {
            js = new JSONObject(data.toString());
            items=js.getJSONArray("items");
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

            ProductsAdapter adapter = new ProductsAdapter(Home.this, productList,this);
            recyclerView.setAdapter(adapter);
        }
        catch (Throwable t) {
            Log.e("My App", t.getMessage());
        }
    }
    public void getResult() {
        observable.subscribe(new SingleObserver<ClassifiedImages>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onSuccess(ClassifiedImages classifiedImages) {
                // System.out.println(classifiedImages.toString());
                //Log.d("result",classifiedImages.toString());
                String res=classifiedImages.toString();
                //parse json objects
                Log.d("output",res);

                try {
                    JSONObject reader = new JSONObject(res);

                    JSONArray images = reader.getJSONArray("images");

                    JSONObject classifier = new JSONObject(images.get(0).toString());
                    JSONArray classifiers1 = classifier.getJSONArray("classifiers");
                    JSONObject classes = new JSONObject(classifiers1.get(0).toString());

                    JSONArray x = classes.getJSONArray("classes");

                    JSONObject y = new JSONObject(x.get(0).toString());

                    String classname = y.getString("class");
                    Log.d("x", classname.toString());

                    String url = "http://api.walmartlabs.com/v1/search?apiKey=ygbtydqxg9d7ehfv488deu4p&query=" + classname;
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
                                Log.d("out", result);
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {
                                        viewData(result);
                                        // Stuff that updates the UI

                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("out", "asd");
            }
            @Override
            public void onError(Throwable e) {
                System.out.println(e.getMessage());
            }
        });
    }
    public void opencam(View v){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, 100);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    if(requestCode==100){
        image=(Bitmap)data.getExtras().get("data");
        imagepath=saveimage();
        getResult();
    }
    }
    public String saveimage(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String filename = "sample" + timeStamp + ".jpg";
        File file = new File(getFilesDir(), filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException io) {
            io.printStackTrace();
        }
        return file.getAbsolutePath();
    }

    public void searchstores(View v){
        Intent register= new Intent(Home.this,WalmartLocation.class);
        startActivity(register);
    }
}
