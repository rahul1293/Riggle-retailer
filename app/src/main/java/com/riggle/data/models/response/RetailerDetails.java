package com.riggle.data.models.response;

import java.util.List;

public class RetailerDetails {
    public int id;
    public String update_url;
    public String account_status;
    public int riggle_coins_balance;
    public String created_at;
    public String updated_at;
    public boolean is_deleted;
    public Object deleted_at;
    public String code;
    public String name;
    public String address;
    public String landmark;
    public String pincode;
    public String store_type;
    public String store_location;
    public String image;
    public String doc_id;
    public CartDetailsBean cart_data;
    public boolean is_active;
    public int sub_area;
}

class CartDetailsBean {
    public double amount;
    public List<ProductBean> products;
    public double final_amount;
    public int riggle_coins;
    public int redeemed_riggle_coins;
}

class ProductBean {
    public double rate;
    public double amount;
    public int product;
    public int quantity;
    public int riggle_coins;
    public double original_rate;
    public int product_combo;
}
