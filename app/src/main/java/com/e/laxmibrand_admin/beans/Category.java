package com.e.laxmibrand_admin.beans;

public class Category {

    String catimage,catname;
    String id;
    String imageurl;

    public Category(String id, String imageurl,String catName) {
        this.id = id;
        this.imageurl = imageurl;
        this.catname = catName;
    }
    public Category() {
        }


    public void setCatImage(String catimage) {
        this.catimage = catimage;
    }

    public String getCatImage(){return this.catimage;}

    public void setCatName(String catname) {
        this.catname = catname;
    }

    public String getCatName(){return this.catname;}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

}
