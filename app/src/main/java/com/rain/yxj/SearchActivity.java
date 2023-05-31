package com.rain.yxj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.rain.yxj.net.CallBack;
import com.rain.yxj.net.OkhttpUtils;
import com.rain.yxj.product.Product;
import com.rain.yxj.product.ProductAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private ListView ls;
    private Button search;
    private EditText search_edit;
    private TextView no_result;
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
        no_result = findViewById(R.id.noresult);

    }
    //初始化事件
    private void initEvent() {

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String key = search_edit.getText().toString();
                initProduct(key);
            }
        });
        //item点击事件
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int product_id = products.get(i).getProductId();
                Intent intent = new Intent(SearchActivity.this,ProductDetailActivity.class);
                intent.putExtra("product_id",product_id);
                startActivity(intent);
                Log.d("Item", "onItemClick: ");

            }
        });
    }
    //初始化数据
    private void initProduct(String key){
        products.clear();
        String url = "http://goods.yuanxiaojiang.com/api/product/search?key="+key;
        OkhttpUtils.getInstance().doGet(url, new CallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray pro_list = new JSONObject(result).getJSONArray("Data");
                    if(pro_list.length() == 0){
                        no_result.setVisibility(View.VISIBLE);

                    }
                    Product product;
                    for (int i = 0; i < pro_list.length(); i++) {
                        JSONObject object = (JSONObject) pro_list.opt(i);
                        int product_id = object.getInt("product_id");
                        String product_name = object.getString("product_name");
                        String product_img_url = object.getString("product_img_url");
                        float product_price = (float) object.getDouble("product_price");

                        product = new Product(product_id, product_name, product_price, product_img_url);
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