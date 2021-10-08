package com.e.laxmibrand_admin.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class GetAllCategoryResponse implements Serializable {


    @SerializedName("status")
    @Expose
    private String responseStatus;

    @SerializedName("responseData")
    @Expose
    private ResponseData responseData;

    public String getStatus() {
        return responseStatus;
    }

    public void setStatus(String status) {
        this.responseStatus = responseStatus;
    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }


    public class ResponseData implements Serializable {

        @SerializedName("result")
        @Expose
        private ArrayList<CategoryList> categoryDetails;

        public ArrayList<CategoryList> getCategoryDetails() {
            return categoryDetails;
        }

        public void setCategoryDetails(ArrayList<CategoryList> categoryDetails) {
            this.categoryDetails = categoryDetails;
        }

        public class CategoryList implements Serializable {

            @SerializedName("category_id")
            @Expose
            private String categoryid;

            @SerializedName("category_name")
            @Expose
            private String categoryname;

            @SerializedName("is_active")
            @Expose
            private String is_active;


            public void setCategoryId(String categoryId) {
                this.categoryid = categoryId;
            }

            public String getCategoryId() {
                return this.categoryid ;
            }

            public void setCategoryName(String categoryname) {
                this.categoryname = categoryname;
            }

            public String getCategoryName() {
                return this.categoryname ;
            }

            public void setIsActive(String is_active) {
                this.is_active = is_active;
            }

            public String getIsActive() {
                return this.is_active ;
            }

        }
    }
}
