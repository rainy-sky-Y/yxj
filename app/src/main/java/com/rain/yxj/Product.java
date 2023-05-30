package com.rain.yxj;

public class Product {
    private int productId;
    private String productName;
    private float productPrice;
    private String productImgURL;

    public Product(int productId,String productName,float productPrice,String productImgURL){
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImgURL = productImgURL;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductPrice() {
        return String.valueOf(productPrice);
    }

    public String getProductImgURL() {
        return productImgURL;
    }
}
