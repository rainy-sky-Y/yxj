package com.rain.yxj;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rain.yxj.net.CallBack;
import com.rain.yxj.net.OkhttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView product_img;
    private TextView product_name,product_price,product_detail,product_num;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        initView();    //初始化控件
        initEvent();    //控件事件

        int product_id = getIntent().getExtras().getInt("product_id");

        String url = "http://goods.yuanxiaojiang.com/api/Product/"+product_id;
        OkhttpUtils.getInstance().doGet(url, new CallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject productJSON = new JSONObject(result).getJSONObject("Data");
                    String productName = productJSON.getString("product_name");
                    String productImgUrl = productJSON.getString("product_img_url");
                    float productPrice = (float) productJSON.getDouble("product_uprice");
                    String productDetail = productJSON.getString("product_detail");
                    int productNum = productJSON.getInt("product_num");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(ProductDetailActivity.this)
                                    .load(productImgUrl) //加载地址
                                    //.placeholder(R.drawable.bitmap)//加载未完成时显示占位图
                                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                                    .into(product_img);//显示的位置
                            product_name.setText(productName);
                            product_price.setText(productPrice+"");
                            product_detail.setText(productDetail);
                            product_num.setText(productNum+"");
                        }
                    });

                    //日志
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Exception e) {
                Log.e("ProductDetailActivity", "onError: " + e.toString());
            }
        });
    }

    private void initView() {
        product_img = findViewById(R.id.product_img);
        product_name = findViewById(R.id.product_name);
        product_price = findViewById(R.id.product_price);
        product_detail = findViewById(R.id.product_detail);
        product_num = findViewById(R.id.product_num);
    }
    private void initEvent() {


    }

}