package com.e.laxmibrand_admin.beans;

import java.util.ArrayList;

public class Products {

     String pdt_id, pdt_name, category_id,pdt_discount_display, pdt_about, pdt_storage_uses,pdt_other_info,is_active,
             prdt_images, created_date,updated_at,deleted_at,is_deleted,populairty,product_id,varient_isactive,rowid,
             var_id, varient_status,pdt_price_actual_500gm,pdt_price_discounted_500gm,pdt_price_enable_500gm
    ,pdt_price_actual_1kg, pdt_price_discounted_1kg,pdt_price_enable_1kg,pdt_price_actual_2kg,pdt_price_discounted_2kg,pdt_price_enable_2kg
             ,pdt_price_actual_3kg, pdt_price_discounted_3kg,pdt_price_enable_3kg
    ,pdt_price_actual_5kg, pdt_price_discounted_5kg, pdt_price_enable_5kg;

    ArrayList<Var> varList;
     String[] product_images;

     public void setImages(String[] product_images){this.product_images=product_images;}
    public String[] getImages(){return this.product_images;}

public void setVarientItems(ArrayList<Var> varList){this.varList = varList;}
public ArrayList<Var> getVarientItems(){return varList;}

    public void setPdt_id(String pdt_id){this.pdt_id=pdt_id;}
     public String getPdt_id(){return pdt_id;}

    public void setPdt_name(String pdt_name){this.pdt_name=pdt_name;}
    public String getPdt_name(){return pdt_name;}

    public void setPdt_about(String pdt_about){this.pdt_about=pdt_about;}
    public String getPdt_about(){return pdt_about;}

    public void setPdt_discount_display(String pdt_discount_display){this.pdt_discount_display=pdt_discount_display;}
    public String getPdt_discount_display(){return pdt_discount_display;}

    public void setPrdt_images(String prdt_images){this.prdt_images=prdt_images;}
    public String getPrdt_images(){return prdt_images;}

    public void setCategory_id(String category_id){this.category_id=category_id;}
    public String getCategory_id(){return category_id;}

    public void setIs_active(String is_active){this.is_active=is_active;}
    public String getIs_active(){return is_active;}

    public void setPopulairty(String populairty){this.populairty=populairty;}
    public String getPopulairty(){return populairty;}

    public void setVarient_isactive(String varient_isactive){this.varient_isactive=varient_isactive;}
    public String getVarient_isactive(){return varient_isactive;}

    public void setVarient_status(String varient_status){this.varient_status = varient_status;}
    public String getVarient_status(){return this.varient_status;}

    public void setPdt_price_actual_500gm(String pdt_price_actual_500gm){this.pdt_price_actual_500gm = pdt_price_actual_500gm;}
    public String getPdt_price_actual_500gm(){return this.pdt_price_actual_500gm;}

    public void setPdt_price_discounted_500gm(String pdt_price_discounted_500gm){this.pdt_price_discounted_500gm = pdt_price_discounted_500gm;}
    public String getPdt_price_discounted_500gm(){return this.pdt_price_discounted_500gm;}

    public void setPdt_price_enable_500gm(String pdt_price_enable_500gm){this.pdt_price_enable_500gm = pdt_price_enable_500gm;}
    public String getPdt_price_enable_500gm(){return this.pdt_price_enable_500gm;}

    public void setPdt_price_actual_1kg(String pdt_price_actual_1kg){this.pdt_price_actual_1kg = pdt_price_actual_1kg;}
    public String getPdt_price_actual_1kg(){return this.pdt_price_actual_1kg;}

    public void setPdt_price_discounted_1kg(String pdt_price_discounted_1kg){this.pdt_price_discounted_1kg = pdt_price_discounted_1kg;}
    public String getPdt_price_discounted_1kg(){return this.pdt_price_discounted_1kg;}

    public void setPdt_price_enable_1kg(String pdt_price_enable_1kg){this.pdt_price_enable_1kg = pdt_price_enable_1kg;}
    public String getPdt_price_enable_1kg(){return this.pdt_price_enable_1kg;}

    public void setPdt_price_actual_2kg(String pdt_price_actual_2kg){this.pdt_price_actual_2kg = pdt_price_actual_2kg;}
    public String getPdt_price_actual_2kg(){return this.pdt_price_actual_2kg;}

    public void setPdt_price_discounted_2kg(String pdt_price_discounted_2kg){this.pdt_price_discounted_2kg = pdt_price_discounted_2kg;}
    public String getPdt_price_discounted_2kg(){return this.pdt_price_discounted_2kg;}

    public void setPdt_price_enable_2kg(String pdt_price_enable_2kg){this.pdt_price_enable_2kg = pdt_price_enable_2kg;}
    public String getPdt_price_enable_2kg(){return this.pdt_price_enable_2kg;}

    public void setPdt_price_actual_3kg(String pdt_price_actual_3kg){this.pdt_price_actual_3kg = pdt_price_actual_3kg;}
    public String getPdt_price_actual_3kg(){return this.pdt_price_actual_3kg;}

    public void setPdt_price_discounted_3kg(String pdt_price_discounted_3kg){this.pdt_price_discounted_3kg = pdt_price_discounted_3kg;}
    public String getPdt_price_discounted_3kg(){return this.pdt_price_discounted_3kg;}

    public void setPdt_price_enable_3kg(String pdt_price_enable_3kg){this.pdt_price_enable_3kg = pdt_price_enable_3kg;}
    public String getPdt_price_enable_3kg(){return this.pdt_price_enable_3kg;}

    public void setPdt_price_actual_5kg(String pdt_price_actual_5kg){this.pdt_price_actual_5kg = pdt_price_actual_5kg;}
    public String getPdt_price_actual_5kg(){return this.pdt_price_actual_5kg;}

    public void setPdt_price_discounted_5kg(String pdt_price_discounted_5kg){this.pdt_price_discounted_5kg = pdt_price_discounted_5kg;}
    public String getPdt_price_discounted_5kg(){return this.pdt_price_discounted_5kg;}

    public void setPdt_price_enable_5kg(String pdt_price_enable_5kg){this.pdt_price_enable_5kg = pdt_price_enable_5kg;}
    public String getPdt_price_enable_5kg(){return this.pdt_price_enable_5kg;}


}
