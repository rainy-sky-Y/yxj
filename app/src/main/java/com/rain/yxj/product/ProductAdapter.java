package com.rain.yxj.product;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.rain.yxj.R;
import com.rain.yxj.product.Product;


public class ProductAdapter extends ArrayAdapter<Product> {
    private int resourceId;
    Bitmap bitmap = null;
    private Context mContext;

    public ProductAdapter(Context context, int resource, List<Product> objects) {
        super(context, resource, objects);
        resourceId = resource;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Product product = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.productImg = view.findViewById(R.id.product_img);
            viewHolder.productName = view.findViewById(R.id.product_name);
            viewHolder.productPrice = view.findViewById(R.id.product_price);

        }else {
            view = convertView;
            viewHolder =  (ViewHolder) view.getTag();
        }

        //使用glide加载图片
        Glide.with(mContext)
                .load(product.getProductImgURL()) //加载地址
                //.placeholder(R.drawable.bitmap)//加载未完成时显示占位图
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(viewHolder.productImg);//显示的位置


        viewHolder.productName.setText(product.getProductName());
        viewHolder.productPrice.setText(product.getProductPrice());
        view.setTag(viewHolder);
        return view;
    }


    class ViewHolder{
        ImageView productImg;
        TextView productName;
        TextView productPrice;
    }

}
