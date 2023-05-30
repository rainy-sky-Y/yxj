package com.rain.yxj;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rain.yxj.net.OkhttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ListView ls;
    private Button search;
    private EditText search_edit;
    List<Product> products = new ArrayList<>();
    ProductAdapter pa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();    //初始化控件
        initEvent();    //控件事件

        pa = new ProductAdapter(SearchActivity.this,R.layout.product_item,products);
        ls.setAdapter(pa);
    }

    //初始化控件
    private void initView() {

        ls = findViewById(R.id.ls);
        search = findViewById(R.id.search);
        search_edit = findViewById(R.id.search_edit);

    }
    //初始化事件
    private void initEvent() {

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = search_edit.getText().toString();
                Log.d("哈哈", "onClick: "+key);
                initProduct(key);
            }
        });
    }
    //初始化数据
    private void initProduct(String key){
        String url = "http://goods.yuanxiaojiang.com/api/product/search?key="+key;
        OkhttpUtils.getInstance().doGet(url, new CallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray pro_list = new JSONObject(result).getJSONArray("Data");
                    Product product;
                    for (int i = 0; i < pro_list.length(); i++) {
                        JSONObject object = (JSONObject) pro_list.opt(i);
                        String product_name = object.getString("product_name");
                        String product_img_url = object.getString("product_img_url");
                        float product_price = (float) object.getDouble("product_price");

                        product = new Product(1, product_name, product_price, product_img_url);
                        products.add(product);
                    }

                    pa.notifyDataSetChanged();
                    //日志
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(SearchActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }

}