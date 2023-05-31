package com.rain.yxj.product;

public class Product {
    private int productId;
    private String productName;
    private float productPrice;
    private String productImgURL;
    private String productDetail;
    private int productNum;

    public Product(int productId,String productName,float productPrice,String productImgURL){
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImgURL = productImgURL;
    }
    public Product(int productId,String productName,float productPrice,String productImgURL,String productDetail,int productNum){
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImgURL = productImgURL;
        this.productDetail = productDetail;
        this.productNum = productNum;
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

    public int getProductId() {
        return productId;
    }
}
