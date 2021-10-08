package com.e.laxmibrand_admin.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginResponse implements Serializable{
    @SerializedName("status")
    @Expose
    private boolean status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private ResponseData data;

    @SerializedName("responseMessage")
    @Expose
    private String responseMessage;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }



    public ResponseData getResponseData() {
        return data;
    }

    public void setResponseData(ResponseData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public class ResponseData implements Serializable {

        @SerializedName("userid")
        @Expose
        private String userid;

        @SerializedName("username")
        @Expose
        private String username;


        @SerializedName("email")
        @Expose
        private String email;

        @SerializedName("password")
        @Expose
        private String password;

        @SerializedName("status")
        @Expose
        private String status;


        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUserName() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }


        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }


        public String getString() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
