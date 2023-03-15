package com.example.loginapp;

import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Favourite_Activity extends BaseActivity {


    Favourite_Iteam_RecyclerAdapter adapter;
    RecyclerView recyclerView;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        token();

        recyclerView =(RecyclerView) findViewById(R.id.favouriteMenuItems);

    }
    public boolean onSupportNavigateUp() {

        Intent i =new Intent(Favourite_Activity.this, Dashboard_Activity_p9.class);
        startActivity(i);
        finish();
        return super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i =new Intent(Favourite_Activity.this,Dashboard_Activity_p9.class);
        startActivity(i);
        finish();
    }
    private void getFavouriteItemsData(String access_token)
    {
        try
        {
            String url = "https://admin.p9bistro.com/index.php/getFavoriteProducts";
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        if (response.getBoolean("status")) {
                            String message = response.getString("message");

                            JSONArray resarray = response.getJSONArray("data");

                            List<Favourite_Iteam_Data> list = new ArrayList<>();

                            for (int i = 0; i < resarray.length(); i++) {
                                JSONObject resobj = resarray.getJSONObject(i);


                                String f_id = resobj.getString("f_id");
                                String id = resobj.getString("id");
                                String product_id = resobj.getString("product_id");
                                String product_name = resobj.getString("product_name");
                                String Description = resobj.getString("Description");
                                String product_rank = resobj.getString("product_rank");

                                list.add(new Favourite_Iteam_Data(product_id, product_name));
                            }
                            adapter = new Favourite_Iteam_RecyclerAdapter(list, getApplication());
                            recyclerView.setLayoutManager(new LinearLayoutManager(Favourite_Activity.this));
                            recyclerView.setAdapter(adapter);
                            if(resarray.length() == 0)
                            {
                                showToast("No favourite iteams to show");
                            }
                            else
                            {
                                showToast("Favourite Iteams fetched");
                            }
                        } else {
                            showToast("Response false");
                        }
                    } catch (JSONException ex) {
                        showToast("Error : " + ex);
                        ex.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    showToast("Fail to get Response : "+error);
                }
            }){
                @Override
                public Map<String,String> getHeaders()throws AuthFailureError
                {
                    SharedPreferences sh = getApplicationContext().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
                    String api = sh.getString("apiKey","");

                    Map<String,String>params = new HashMap<String ,String>();
                    params.put("authorization",access_token);
                    params.put("api-key",api);
                    params.put("Content-Type", "application/json");
                    return params;
                }
            };
            RequestQueue requestQueue =Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(request);
        }
        catch (Exception ex)
        {
            Toast.makeText(Favourite_Activity.this, "Error 3 : "+ex, Toast.LENGTH_SHORT).show();
        }
    }
    private void token() {
        String url = "https://admin.p9bistro.com/index.php/generate_auth_token";
        Log.e("checklog", url + "");

        StringRequest stringRequest =new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("checklog", response + "");
                JSONObject jsonObject = null;
                try
                {
                    jsonObject = new JSONObject(response);
                    String access_token = jsonObject.getString("access_token");
                    Log.e("ACCESSTOKEN", access_token);
                    getFavouriteItemsData(access_token);

                } catch (JSONException je) {
                    Toast.makeText(Favourite_Activity.this, "Error 2 : " + je, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("checklog",error + "");
                Toast.makeText(getApplicationContext(), "Timeout Error", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("x-api-key","XABRTYUX@123YTUFGB");
                return headers;
            }
        };
        RequestQueue requestquese = Volley.newRequestQueue(getApplicationContext());
        requestquese.add(stringRequest);
    }

}