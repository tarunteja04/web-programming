package com.webproject.anil.anilproject;

import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WalmartLocation extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public Geocoder geocoder;
    private String JSON_Result;
    SupportMapFragment mapFragment;
    List<String> lstLatitude = new ArrayList<>();
    List<String> lstLongitude = new ArrayList<>();
    List<String> names = new ArrayList<>();
    private LatLng Kansas = new LatLng(39.0363000,-94.5822000);
    String PinCode;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.walmart);

         mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

       // new Random().execute();
        //GetPincode();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try

        {
            mMap = googleMap;


            mMap.setBuildingsEnabled(true);
       /* mMap.setMinZoomPreference(10.0f);
        mMap.setMaxZoomPreference(15.0f);*/
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Kansas, 6f));

            geocoder = new Geocoder(this);


            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Float.parseFloat(lstLongitude.get(0)),Float.parseFloat(lstLatitude.get(0)) ))
                    .title(names.get(0))
                    .zIndex(1.0f));
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Float.parseFloat(lstLongitude.get(1)), Float.parseFloat(lstLatitude.get(1))))
                    .title(names.get(1))
                    .zIndex(2.0f));
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Float.parseFloat(lstLongitude.get(2)), Float.parseFloat(lstLatitude.get(2))))
                    .title(names.get(2))
                    .zIndex(3.0f));
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Float.parseFloat(lstLongitude.get(3)), Float.parseFloat(lstLatitude.get(3))))
                    .title(names.get(3))
                    .zIndex(4.0f));
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(Float.parseFloat(lstLongitude.get(4)), Float.parseFloat(lstLatitude.get(4))))
                    .title(names.get(4))
                    .zIndex(5.0f));

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }





    }

    private class Random extends AsyncTask<String, Integer, Double> {

        private void RandomRecipe()
        {

            try
            {

                String URI = "http://api.walmartlabs.com/v1/stores?format=json&zip=64131&apiKey=vgncvgzdrgc7mwh87vwd554v";
                URL myURL = new URL(URI);
                HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                int ss = connection.getResponseCode();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder results = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    results.append(line);
                }
                JSON_Result = results.toString();
                connection.disconnect();

            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        @Override
        protected Double doInBackground(String... params) {
            RandomRecipe();
            return null;
        }

        @Override
        protected void onPostExecute(Double result)
        {
            try
            {
                if(JSON_Result!=null)
                {

                    parseforRandomRecipe();
                    SetData();

                }

            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }


        private void parseforRandomRecipe()
        {
            try
            {

                //JSONObject Obj = new JSONObject(JSON_Result);
                lstLatitude = new ArrayList<>();
                lstLongitude = new ArrayList<>();
                names = new ArrayList<>();

                JSONArray Array = new JSONArray(JSON_Result);
               for(int i=0;i<Array.length();i++)
               {
                   names.add(Array.getJSONObject(i).getString("name"));
                   JSONArray CoOrdinates =  Array.getJSONObject(i).getJSONArray("coordinates");
                   String Latitide =  CoOrdinates.getString(0);
                   String Longitude = CoOrdinates.getString(1);
                   lstLatitude.add(Latitide);
                   lstLongitude.add(Longitude);

               }





            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        private void SetData()
        {
            try
            {
                mapFragment.getMapAsync(WalmartLocation.this);
            }catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
    }

    private void GetPincode()
    {
        try
        {


            String UserName = "UserName";
            String urlEncodedName = URLEncoder.encode(UserName, "UTF-8");
            String URL = "https://api.mlab.com/api/1/databases/recpie/collections/userdetails?q={%22EMailID%22:%22"+urlEncodedName+"%22}&apiKey=WQdetFzPianTtgBryFsYkPkNE-osQ-Ue";
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(URL).build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                    final String respJson = response.body().string();
                    if(respJson!="")
                    {
                        try {
                            JSONArray responseJson = new JSONArray(respJson);
                            JSONObject jObj = (JSONObject) responseJson.get(0);
                            PinCode = jObj.getString("Pincode").toString();
                            new Random().execute();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }
            });

        }catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
