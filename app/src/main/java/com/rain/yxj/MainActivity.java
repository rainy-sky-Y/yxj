package com.rain.yxj;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.rain.yxj.net.OkhttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView ls;
    List<Product> products = new ArrayList<>();
    ProductAdapter pa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();

        initProduct();
        pa = new ProductAdapter(MainActivity.this,R.layout.product_item,products);
        ls.setAdapter(pa);
    }

    //初始化控件
    private void initView() {
        ls = findViewById(R.id.ls);
    }
    //初始化事件
    private void initEvent() {

    }
    //初始化数据
    private void initProduct(){
        for(int i = 0;i<50;i++){
            String url = "http://goods.yuanxiaojiang.com/api/Product/"+i;
            int finalI = i;
            OkhttpUtils.getInstance().doGet(url, new CallBack() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject object = new JSONObject(result);
                        String product_name = object.getJSONObject("Data").getString("product_name");
                        String product_img_url = object.getJSONObject("Data").getString("product_img_url");
                        float product_price = (float) object.getJSONObject("Data").getDouble("product_price");

                        Product p = new Product(1,product_name,product_price,product_img_url);
                        products.add(p);
                        pa.notifyDataSetChanged();
                        //日志
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this,"第"+ finalI +"个item无法加载",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Exception e) {
                    Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}