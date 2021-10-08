package com.e.laxmibrand_admin.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class BaseResponse implements Serializable {

    @SerializedName("status")
    @Expose
    private String status;


    @SerializedName("data")
    @Expose
    private ResponseData data;


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public ResponseData getResponseData() {
        return data;
    }

    public void setResponseData(ResponseData data) {
        this.data = data;
    }

    public class ResponseData implements Serializable {
        @SerializedName("message")
        @Expose
        private String message;

        @SerializedName("user_id")
        @Expose
        private String user_id;

        @SerializedName("whatsapp_grp_redirect_link")
        @Expose
        private String whatsapp_grp_redirect_link;

        @SerializedName("order_id")
        @Expose
        private int order_id;
        public String getWhatsapp_grp_redirect_link() {
            return whatsapp_grp_redirect_link;
        }

        public void setWhatsapp_grp_redirect_link(String whatsapp_grp_redirect_link) {
            this.whatsapp_grp_redirect_link = whatsapp_grp_redirect_link;
        }

        public String getuser_id() {
            return user_id;
        }

        public void setuser_id(String user_id) {
            this.user_id = user_id;
        }

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }


        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }


    }

    }
