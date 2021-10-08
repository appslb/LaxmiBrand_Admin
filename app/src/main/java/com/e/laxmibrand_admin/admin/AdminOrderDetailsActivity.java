package com.e.laxmibrand_admin.admin;

import android.app.Dialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.beans.OrderItem;
import com.e.laxmibrand_admin.beans.Orders;
import com.e.laxmibrand_admin.beans.Products;
import com.e.laxmibrand_admin.beans.Var;
import com.e.laxmibrand_admin.pdf_d.PdfUtility;
import com.e.laxmibrand_admin.utils.Utility;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.e.laxmibrand_admin.MyApplication.TAG;


public class AdminOrderDetailsActivity extends AppCompatActivity implements PdfUtility.OnDocumentClose{
     Dialog dialog;
    LinearLayout saveCompleteLL;
    ImageView backBTN, editBTN;
    RecyclerView catalogueRV;
    RecyclerView itemRV;
    String orderid;
    LinearLayout not_available;
    PdfUtility pdfU;
    ArrayList<Orders> orderList;
    ArrayList<OrderItem> orderItemList;
    ArrayList<Var> varList;
    ArrayList<Products> productList;
    ArrayList<Products> orderedProductsList;
    int noOfPages,noOfPagesO;
    AdminOrderDetailsTitleAdapter adminOrderDetailsTitleAdapter;
    Button saveBTN, completeBTN,pdfBtn;
    AdminOrderDetailsActivity context;
    TextView totalTV,userDetailsTV,totalQtyTV;
    TextView customerName,customerAddress,customerMobile,dateValue,orderStatusValue,grandTotalValue,discountAmount,grandTotalValue2,deliveryChargesText,deliveryChargeAmount;
    String userid,deviceid,phonenumber,add,total,status,date,mob,path;
    int totalwdis=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_order_details2);
        context = this;
        noOfPages=1;noOfPagesO=1;
        orderid = getIntent().getStringExtra("orderid");
        itemRV = findViewById(R.id.orderItemsList);
        grandTotalValue=findViewById(R.id.grandTotalValue);
        grandTotalValue2=findViewById(R.id.grandTotalValue2);
        discountAmount=findViewById(R.id.discountAmount);
        deliveryChargesText=findViewById(R.id.deliveryChargesText);
        deliveryChargeAmount=findViewById(R.id.deliveryChargeAmt);
        customerName = findViewById(R.id.customerNameTV2);
        customerAddress= findViewById(R.id.customerAddress);
        customerMobile=findViewById(R.id.customerMobile);
        dateValue=findViewById(R.id.dateValue);
        grandTotalValue=findViewById(R.id.grandTotalValue);
        orderStatusValue=findViewById(R.id.orderStatusValue);
        not_available = findViewById(R.id.not_available);
        pdfBtn = findViewById(R.id.pdfBtn);

        itemRV.setLayoutManager(new LinearLayoutManager(AdminOrderDetailsActivity.this));
        orderList = new ArrayList<Orders>();
        orderItemList = new ArrayList<OrderItem>();
        productList = new ArrayList<Products>();
        orderedProductsList = new ArrayList<Products>();



        pdfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createPdf("Text to pdf");

                try {
                    dialog = Utility.showProgress(context);
                    createOrderPDF();
                }
                catch(Exception e){
                    Utility.dismissDialog(dialog);

                }
                }
        });


        if(Utility.isOnline(context)) {
            getAllOrder(noOfPagesO);
        }
        else
        {
            Utility.noInternetError(context);
        }

    }


/*

    public void setOrderItems()
    {
        for(int i=0;i<orderList.size();i++){
            Log.i("order id:",String.valueOf(orderid));
            Log.i("order id ll:",String.valueOf(orderList.get(i).getOrderID()));
            if(orderid.equals(orderList.get(i).getOrderID())){
                for(int p=0;p<orderList.get(i).getOrderItems().size();p++) {
                    Log.i("product id lln:",orderList.get(i).getOrderItems().get(p).getItemid());
                    orderItemList = orderList.get(i).getOrderItems();
                    getProductName(orderList.get(i).getOrderItems().get(p).getItemid());
                }

                customerAddress.setText(orderList.get(i).getAddress()+"\n"+orderList.get(i).getLandmark()+"\n"+orderList.get(i).getCity()+" "+orderList.get(i).getPinCode());
                customerMobile.setText(orderList.get(i).getPhoneNumber());
                dateValue.setText(orderList.get(i).getOrderDate());
                orderStatusValue.setText(orderList.get(i).getOrderStatus());
                grandTotalValue.setText(orderList.get(i).getTotalOrderItem());
            }

        }

    }
*/
public void setOrderItems22()
{
    if (orderid.equals(orderList.get(0).getOrderID())) {
        for (int p = 0; p < orderList.get(0).getOrderItems().size(); p++) {
            ArrayList<Var> varItemList = new ArrayList<Var>();
            Log.i("product id lln:", orderList.get(0).getOrderItems().get(p).getItemid());
            String pname = getProductName(orderList.get(0).getOrderItems().get(p).getItemid(), orderList.get(0).getOrderItems().get(p).getOrder_varients());
            OrderItem oi = new OrderItem();
            oi.setItemName(pname);
            oi.setOrder_varients(orderList.get(0).getOrderItems().get(p).getOrder_varients());
            orderItemList.add(oi);
        }

        add=(orderList.get(0).getAddress() + "\n" + orderList.get(0).getLandmark() + "\n" + orderList.get(0).getCity() + " " + orderList.get(0).getPinCode());
        mob=(orderList.get(0).getPhoneNumber());
        date=(orderList.get(0).getOrderDate());
        status=(orderList.get(0).getOrderStatus());
        total=(orderList.get(0).getTotalOrderItem());

    }
    customerName.setText(orderList.get(0).getOrderID());

    customerAddress.setText(add);
    customerMobile.setText(mob);
    dateValue.setText(date);
    orderStatusValue.setText(status);
    grandTotalValue.setText(total);


    int chkTotal = Integer.valueOf(total);
    int tdis=chkTotal;
    if(orderList.get(0).getDisAmt().equals(null) || orderList.get(0).getDisAmt().equals(""))
    {

    }
    else
    {
        tdis = tdis-Integer.valueOf(orderList.get(0).getDisAmt());
    }


    if(chkTotal <= 500)
    {
        // totalwdis = chkTotal+50;
        totalwdis = tdis+50;
        deliveryChargeAmount.setText("50.0");
    }
    else
    {
        deliveryChargeAmount.setText("0.0");
        //totalwdis=chkTotal;
        totalwdis=tdis;
    }

    discountAmount.setText(orderList.get(0).getDisAmt());
    grandTotalValue.setText(String.valueOf(totalwdis));
    grandTotalValue2.setText(String.valueOf(chkTotal));
    itemRV.setLayoutManager(new LinearLayoutManager(
            AdminOrderDetailsActivity.this));


    if (orderItemList.size() > 0) {
        pdfBtn.setVisibility(View.VISIBLE);
        itemRV.setVisibility(View.VISIBLE);
        not_available.setVisibility(View.GONE);
        adminOrderDetailsTitleAdapter = new AdminOrderDetailsTitleAdapter(AdminOrderDetailsActivity.this, orderItemList, orderedProductsList);
        itemRV.setAdapter(adminOrderDetailsTitleAdapter);
        adminOrderDetailsTitleAdapter.notifyDataSetChanged();
    }
    else
        {
        pdfBtn.setVisibility(View.GONE);

        itemRV.setVisibility(View.GONE);
        not_available.setVisibility(View.VISIBLE);
    }

}

    public void getAllProducts(int pageno) {
        final Dialog dialog = Utility.showProgress(context);
        JsonObject prdctObject;
        prdctObject = new JsonObject();

        prdctObject.addProperty("popularity", "0");
        prdctObject.addProperty("price", "0");
        prdctObject.addProperty("sort", "0");

        Call<ResponseBody> get = Utility.retroInterface().getProductsByPage("http://dev.polymerbazaar.com/laxmibrand/admin/product/list/"+pageno,prdctObject);
        get.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        try {
                            ResponseBody responseBody = response.body();
                            String s = responseBody.string();
                            Log.i("Response : ",s);
                            JSONObject object = new JSONObject(s);
                            // JSONArray jsonElements = object.getJSONArray("data");
                            JSONObject objRO = object.getJSONObject("data");
                            // JSONObject objR = jsonElements.getJSONObject(0);
                            int noOfPages2 = objRO.getInt("total_pages");
                            JSONArray jA = objRO.getJSONArray("result");
                            for (int i = 0; i < jA.length(); i++) {
                                JSONObject obj = jA.getJSONObject(i);
                                JSONObject objP = obj.getJSONObject("product");
                                Products product = new Products();
                                product.setPdt_id(objP.getString("pdt_id"));
                                product.setPdt_name(objP.getString("pdt_name"));
                                productList.add(product);
                            }

                            if(noOfPages2>1 && noOfPages<noOfPages2) {
                                noOfPages = noOfPages + 1;
                                getAllProducts(noOfPages);
                            }
                            else
                            {
                                setOrderItems22();
                            }

                        }catch(JSONException je){
                            Toast.makeText(context, "JSONEXCE : " + je.getMessage(), Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        Toast.makeText(context, "other than 200", Toast.LENGTH_LONG).show();

                    }
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();

                    Utility.somethingWentWrong(context);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utility.dismissDialog(dialog);
                Utility.somethingWentWrong(context);
            }
        });

    }

    private String getProductName(String itemid, ArrayList<Var> order_varients) {

        for(int p=0;p<productList.size();p++)
        {

            if(productList.get(p).getPdt_id().equals(itemid)) {
                Products pr= new Products();
                pr.setPdt_id(productList.get(p).getPdt_id());
                pr.setPdt_name(productList.get(p).getPdt_name());


                return productList.get(p).getPdt_name();
            }
        }

        return "";
    }

    private void getAllOrder(int pageno) {
        final Dialog dialog = Utility.showProgress(context);
     //   Call<ResponseBody> get = Utility.retroInterface().getOrderList();
        Call<ResponseBody> get = Utility.retroInterface().getOrderNew("http://dev.polymerbazaar.com/laxmibrand/admin/order/list/"+pageno);

        get.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        try {
                            ResponseBody responseBody = response.body();
                            String s = responseBody.string();
                            JSONObject object = new JSONObject(s);
                            JSONObject objRO = object.getJSONObject("data");
                            JSONArray jA = objRO.getJSONArray("result");
                            int noOfPages2O = objRO.getInt("total_pages");
                            for (int i = 0; i < jA.length(); i++) {
                                JSONObject obj = jA.getJSONObject(i);
                                ArrayList<OrderItem> productsList = new ArrayList<OrderItem>();
                                Orders order = new Orders();
                                if (orderid.equals(obj.getString("order_id"))) {

                                    order.setOrderId(obj.getString("order_id"));
                                    order.setPhoneNumber(obj.getString("user_mobile"));
                                    order.setDeviceid(obj.getString("unique_deviceid"));
                                    order.setOrderDate(obj.getString("order_created_date"));
                                    order.setAddress(obj.getString("address"));
                                    order.setCity(obj.getString("city"));
                                    order.setPinCode(obj.getString("pincode"));
                                    order.setLandmark(obj.getString("landmark"));
                                    order.setTotalOrderItem(obj.getString("amount"));
                                    order.setOrderStatus(obj.getString("order_status"));
                                    order.setDisAmt(obj.getString("discount_amount"));
                                    JSONArray jP = obj.getJSONArray("products");
                                    for (int j = 0; j < jP.length(); j++) {
                                        JSONObject objP = jP.getJSONObject(j);
                                        OrderItem oi = new OrderItem();
                                        oi.setItemId(objP.getString("pdt_id"));
                                        JSONArray jV = objP.getJSONArray("variant");
                                        ArrayList<Var> varientList = new ArrayList<Var>();

                                        for (int k = 0; k < jV.length(); k++) {
                                            JSONObject objV = jV.getJSONObject(k);
                                            Var v = new Var();
                                            v.setVarType(objV.getString("var_type"));
                                            v.setVarActualAmt(objV.getString("var_actual_price"));
                                            v.setVarDisAmt(objV.getString("var_discount_price"));
                                            v.setVarQty(objV.getString("var_qty"));
                                            varientList.add(v);
                                        }
                                        oi.setOrder_varients(varientList);
                                        productsList.add(oi);
                                    }
                                    order.setOrderItems(productsList);

                                    orderList.add(order);
                                }
                            }

                            if(noOfPages2O>1 && noOfPagesO<noOfPages2O) {
                                noOfPagesO = noOfPagesO + 1;
                                Log.i("noofpagesO==",noOfPages2O+"");
                                getAllOrder(noOfPagesO);
                              }
                            else {
                                getAllProducts(noOfPages);
                            }


                        }catch(JSONException je){
                            Toast.makeText(context, "JSONEXCE : " + je.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        //recyclerView.setAdapter(new AdminSalesmanUserAdapter(getActivity(),salesmanDetails));
                        // }
                        //  }
                    }
                    else {
                        Toast.makeText(context, "other than 200"+response.errorBody(), Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    Utility.somethingWentWrong(context);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utility.somethingWentWrong(context);
            }
        });
    }


    private void createOrderPDF()throws Exception
    {
        Toast.makeText(this,"Creating Pdf , Please Wait!!",Toast.LENGTH_LONG).show();

        path = Environment.getExternalStorageDirectory().toString() + "/LB_Order_"+orderid+".pdf";
        try
        {
            PdfUtility.createPdf(context,AdminOrderDetailsActivity.this,orderItemList,path,true,orderid);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            Log.e(TAG,"Error Creating Pdf"+e.getMessage());
            Toast.makeText(context,"Error Creating Pdf. Try Again Later!!",Toast.LENGTH_SHORT).show();
        }
    }

    private void createPdf(String sometext){
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(50, 50, 30, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText(sometext, 80, 50, paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page
        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(100, 100, 100, paint);
        document.finishPage(page);
        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"LB_Order_"+orderid+".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "File saved to : "+directory_path, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }

    private void createOrderPdf(String sometext){
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawCircle(50, 50, 30, paint);
        paint.setColor(Color.BLACK);
        canvas.drawText(sometext, 80, 50, paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page
        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.BLUE);
        canvas.drawCircle(100, 100, 100, paint);
        document.finishPage(page);
        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"LB_Order_"+orderid+".pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(this, "File saved to : "+directory_path, Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }


    @Override
    public void onPDFDocumentClose(File file)
    {

        Toast.makeText(this,"Pdf Created at : " + path,Toast.LENGTH_SHORT).show();
    }
}