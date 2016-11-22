package com.jonathanlieblich.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<String> mProducts;
    RecyclerView mRecyclerView;
    ProductViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cereal = (Button)findViewById(R.id.cereal);
        Button chocolate = (Button)findViewById(R.id.chocolate);
        Button tea = (Button)findViewById(R.id.tea);

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);

        mProducts = new ArrayList<>();

        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAdapter = new ProductViewAdapter(mProducts);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setAdapter(mAdapter);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()) {
                    case R.id.cereal:
                        getProductRequest("cereal");
                        break;
                    case R.id.chocolate:
                        getProductRequest("chocolate");
                        break;
                    case R.id.tea:
                        getProductRequest("tea");
                        break;
                }
            }
        };

        cereal.setOnClickListener(listener);
        chocolate.setOnClickListener(listener);
        tea.setOnClickListener(listener);
    }

    private void getProductRequest(String name) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://api.walmartlabs.com/v1/search?apiKey=s4x4jthz6j87wwmd5s3vuafd&query="+name;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    mProducts.clear();
                    JSONArray array = response.getJSONArray("items");
                    for(int i=0;i<array.length();i++) {
                        JSONObject item = array.getJSONObject(i);
                        String name = item.getString("name");
                        mProducts.add(name);
                    }
                } catch(JSONException e) {
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("tag", "onErrorResponse");
            }
        });
        queue.add(request);
    }
}
