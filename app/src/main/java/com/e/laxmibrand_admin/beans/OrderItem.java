package com.e.laxmibrand_admin.beans;

import java.lang.reflect.Array;
import java.util.ArrayList;

public  class OrderItem{

    String  itemid,itemname,item_qty,item_weight,item_rate,amount;
    ArrayList<Var> order_varients;

   /* OrderItem(String itemid,String itemname,String item_qty,String item_weight,String item_rate,String amount){
        this.itemid = itemid;
        this.itemname=itemname;
        this.item_qty=item_qty;
        this.item_weight=item_weight;
        this.item_rate=item_rate;
        this.amount=amount;
    }*/

    public void setOrder_varients(ArrayList<Var> order_varients) {
        this.order_varients = order_varients;
    }

    public ArrayList<Var> getOrder_varients(){return this.order_varients;}

    public void setItemId(String itemid) {
        this.itemid = itemid;
    }

    public String getItemid(){return this.itemid;}

    public void setItemName(String itemname) {
        this.itemname = itemname;
    }

    public String getItemName(){return this.itemname;}

    public void setItemQty(String item_qty) {
        this.item_qty = item_qty;
    }

    public String getItemQty(){return this.item_qty;}

    public void setItemWeight(String item_weight) {
        this.item_weight = item_weight;
    }

    public String getItemWeight(){return this.item_weight;}

    public void setItemRate(String item_rate) {
        this.item_rate = item_rate;
    }

    public String getItemRate(){return this.item_rate;}


    public void setItemAmount(String amount) {
        this.amount = amount;
    }

    public String getItemAmount(){return this.amount;}}

