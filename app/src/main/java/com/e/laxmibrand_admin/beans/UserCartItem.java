package com.e.laxmibrand_admin.beans;

public class UserCartItem {

     String pdt_id,var_id,category,productname,productImage,variant,actual_price,savedAmount,total_price;
int qty;

    public UserCartItem(String category,String var_id,String pdt_id,String productname,String productImage,int qty,String variant,String actual_price,String savedAmount,String total_price){
         this.pdt_id = pdt_id;
         this.category=category;
         this.var_id=var_id;
         this.productname=productname;
         this.productImage=productImage;
         this.qty=qty;
         this.variant = variant;
         this.actual_price=actual_price;
         this.savedAmount = savedAmount;
         this.total_price=total_price;

     }


    public void setPdt_id(String pdt_id){this.pdt_id=pdt_id;}
    public String getPdt_id(){return pdt_id;}

    public void setVar_id(String var_id){this.var_id=var_id;}
    public String getVar_id(){return var_id;}

     public void setCatgory(String category){this.category=category;}
     public String getCategory(){return this.category;}

    public void setProductName(String productName){this.productname=productname;}
    public String getProductname(){return this.productname;}

    public void setProductImage(String productImage){this.productImage=productImage;}
    public String getProductImage(){return this.productImage;}

    public void setQty(int qty){this.qty=qty;}
    public int getQty(){return this.qty;}

    public void setVariant(String variant){this.variant=variant;}
    public String getVariant(){return this.variant;}

    public void setActual_price(String actual_price){this.actual_price=actual_price;}
    public String getActual_price(){return this.actual_price;}

    public void setTotalItemPrice(String total_price){this.total_price=total_price;}
    public String getTotalItemPrice(){return this.total_price;}

    public void setsavedAmount(String savedAmount){this.savedAmount=savedAmount;}
    public String getsavedAmount(){return this.savedAmount;
     }






}
