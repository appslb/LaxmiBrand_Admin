package com.e.laxmibrand_admin.beans;

import java.util.ArrayList;
import java.util.Comparator;

public class Orders {

    String orderDetail;
    String deviceid,phonenumber,OrderID,orderDate,disamount,OrderedByUserId,OrderStatus,totalOrderItem,address,city,pincode,landmark;

    ArrayList<OrderItem> OrderedItemsArray;

    public void setOrderDetail(String orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getOrderDetail(){return this.orderDetail;}

    public void setDisAmt(String disAmt) {
        this.disamount = disAmt;
    }

    public String getDisAmt(){return this.disamount;}
    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getDeviceid(){return this.deviceid;}

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity(){return this.city;}


    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress(){return this.address;}

    public void setPinCode(String pincode) {
        this.pincode = pincode;
    }

    public String getPinCode(){return this. pincode;}

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getLandmark(){return this.landmark;}


    public void setPhoneNumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPhoneNumber(){return this.phonenumber;}

    public void setOrderItems(ArrayList<OrderItem> OrderedItemsArray) {
        this.OrderedItemsArray = OrderedItemsArray;
    }

    public ArrayList<OrderItem> getOrderItems(){return this.OrderedItemsArray;}
    public void setOrderId(String OrderID) {
        this.OrderID = OrderID;
    }

    public String getOrderID(){return this.OrderID;}

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderDate(){return this.orderDate;}


    public void setOrderByUserId(String OrderedByUserId) {
        this.OrderedByUserId = OrderedByUserId;
    }

    public String getOrdereByUserId(){return this.OrderedByUserId;}

    public void setOrderStatus(String OrderStatus) {
        this.OrderStatus = OrderStatus;
    }

    public String getOrderStatus(){return this.OrderStatus;}

    public void setTotalOrderItem(String totalOrderItem) {
        this.totalOrderItem = totalOrderItem;
    }

    public String getTotalOrderItem(){return this.totalOrderItem;}

}
 class SortbyOrderId implements Comparator<Orders>
{
    // Used for sorting in ascending order of
    // roll number
    public int compare(Orders a, Orders b)
    {
        return Integer.parseInt(a.OrderID) - Integer.parseInt(b.OrderID);
    }
}