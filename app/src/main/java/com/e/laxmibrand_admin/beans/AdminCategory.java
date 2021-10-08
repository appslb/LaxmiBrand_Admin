package com.e.laxmibrand_admin.beans;

public class AdminCategory {

    String catid,catimage,catname,isactive;

       public  AdminCategory(String catname,String isactive){
            this.catname=catname;
            this.isactive=isactive;
        }
    public  AdminCategory(){

    }
    public void setIsactive(String isactive) {
        this.isactive = isactive;
    }

    public String getIsactive(){return this.isactive;}

    public void setCatImage(String catimage) {
        this.catimage = catimage;
    }

    public String getCatImage(){return this.catimage;}

    public void setCatName(String catname) {
        this.catname = catname;
    }

    public String getCatName(){return this.catname;}

    public void setCatId(String catid) {
        this.catid = catid;
    }

    public String getCatid(){return this.catid;}



}
