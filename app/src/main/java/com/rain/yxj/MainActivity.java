package com.rain.yxj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.rain.yxj.net.OkhttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView ls;
    private Button search;
    List<Product> products = new ArrayList<>();
    ProductAdapter pa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();    //初始化控件
        initEvent();    //控件事件

        initProduct();

        pa = new ProductAdapter(MainActivity.this,R.layout.product_item,products);
        ls.setAdapter(pa);
    }

    private void initView() {

        ls = findViewById(R.id.ls);
        search = findViewById(R.id.search);

    }
    //初始化事件
    private void initEvent() {

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });

    }
    //初始化数据
    private void initProduct(){
        int pageIndex = 1, pageNum = 20;
        String url = "http://goods.yuanxiaojiang.com/api/product/getproductList?pageIndex="+pageIndex+"&pageNum="+pageNum;
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
                Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}