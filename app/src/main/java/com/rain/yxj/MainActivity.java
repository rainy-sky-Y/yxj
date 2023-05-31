package com.rain.yxj;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
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

public class MainActivity extends AppCompatActivity {

    private ListView ls;
    private Button search;
    private View bottom;
    private TextView tv;
    private ProgressBar pb;
    List<Product> products = new ArrayList<>();
    ProductAdapter pa;

    int pageIndex = 1, pageNum = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();    //初始化控件
        initEvent();    //控件事件

        initProduct();

        pa = new ProductAdapter(MainActivity.this,R.layout.product_item,products);
        ls.addFooterView(bottom);
        ls.setAdapter(pa);
    }

    private void initView() {

        ls = findViewById(R.id.ls);
        search = findViewById(R.id.search);
        bottom = getLayoutInflater().inflate(R.layout.bottom, null);
        pb = (ProgressBar) bottom.findViewById(R.id.progressBar1);
        tv = (TextView) bottom.findViewById(R.id.textView);

    }
    //初始化事件
    private void initEvent() {
        //搜索按钮点击事件
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        //列表item点击事件
        ls.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                int product_id = products.get(i).getProductId();
                Intent intent = new Intent(MainActivity.this,ProductDetailActivity.class);
                intent.putExtra("product_id",product_id);
                startActivity(intent);
                Log.d("Item", "onItemClick: ");

            }
        });

        //为listview设置滑动监听
        ls.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
                // 判断滚动是否停止
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //判断界面上显示的最后一项item的position，是否等于item的总个数减1（item的position从0开始），如果是，说明滑动到了底部。
                    if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                        /*
                         * 如果程序运行到这里，说明滑动到了底部，下面显示加载效果，通过线程加载数据
                         */
                        pageIndex++;
                        initProduct();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

            }
        });
    }
    //初始化数据
    private void initProduct(){

        String url = "http://goods.yuanxiaojiang.com/api/product/getproductList?pageIndex="+pageIndex+"&pageNum="+pageNum;
        OkhttpUtils.getInstance().doGet(url, new CallBack() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray pro_list = new JSONObject(result).getJSONArray("Data");
                    if (pro_list.length()==0){
                        tv.setVisibility(View.VISIBLE);
                        pb.setVisibility(View.GONE);
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
                Toast.makeText(MainActivity.this,e.toString(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}