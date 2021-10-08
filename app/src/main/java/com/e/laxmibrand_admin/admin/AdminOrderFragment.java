package com.e.laxmibrand_admin.admin;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/*import com.app.sradesign.adapter.AdminOrderAdapter;
import com.app.sradesign.model.response.GetAllOrderDetailResponse;
import com.app.sradesign.utils.Utility;*/

import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.admin.category.CategoryList;
import com.e.laxmibrand_admin.admin.category.CategoryListAdapter;
import com.e.laxmibrand_admin.beans.AdminCategory;
import com.e.laxmibrand_admin.beans.OrderItem;
import com.e.laxmibrand_admin.beans.Orders;
import com.e.laxmibrand_admin.beans.Var;
import com.e.laxmibrand_admin.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminOrderFragment extends AppCompatActivity {

    Context context;
    RecyclerView orderRV;
   public static ArrayList<Orders> orderList;
    LinearLayout emptyOrderArea;
    AdminOrderAdapter adminOrderAdapter;
    SearchView searchET;
    ImageView logoutBTN,backPressed;
    TextView noDataTV, fromDateTV, toDateTV;
    LinearLayout fromDateLL, toDateLL, findBTN;
    int startYear, startMonth, startDay, endYear, endMonth, endDay;
    Calendar calendar;
    String startDateToPassInAPI = "", endDateToPassInAPI = "";
    Date startDate, endDate;
    int orderid = 0;
    static int noOfPages;
    ArrayList<Integer> othDis;
    int othdis=0;

    // Inflate the layout for this fragment
   @Override
   protected void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       setContentView(R.layout.fragment_admin_order);

       emptyOrderArea = findViewById(R.id.empty_order);
       logoutBTN = findViewById(R.id.logoutBTN);
       backPressed = findViewById(R.id.backPressed);
        orderRV = findViewById(R.id.orderRV);
        searchET = findViewById(R.id.searchET);
        logoutBTN = findViewById(R.id.logoutBTN);
        fromDateTV = findViewById(R.id.fromDateTV);
        toDateTV = findViewById(R.id.toDateTV);
        fromDateLL = findViewById(R.id.fromDateLL);
        toDateLL = findViewById(R.id.toDateLL);
        findBTN = findViewById(R.id.findBTN);
       SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
       searchET.setSearchableInfo(searchManager
               .getSearchableInfo(getComponentName()));
       searchET.setMaxWidth(Integer.MAX_VALUE);

       searchET.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String s) {
               adminOrderAdapter.getFilter().filter(s);
               return false;
           }

           @Override
           public boolean onQueryTextChange(String s) {
               String text = s;
               adminOrderAdapter.getFilter().filter(text);
               return false;
           }
       });
noOfPages=1;
       context = AdminOrderFragment.this;
       orderRV.setLayoutManager(new LinearLayoutManager(AdminOrderFragment.this));
       orderList = new ArrayList<>();
       calendar = Calendar.getInstance();
       endYear = calendar.get(Calendar.YEAR);
       endMonth = calendar.get(Calendar.MONTH);
       endDay = calendar.get(Calendar.DAY_OF_MONTH);
       SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US),
                apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        endDateToPassInAPI = apiDateFormat.format(calendar.getTime());
        toDateTV.setText(dateFormat.format(calendar.getTime()));
        endDate = calendar.getTime();

        calendar.add(Calendar.DAY_OF_MONTH, -6);
        startYear = calendar.get(Calendar.YEAR);
        startMonth = calendar.get(Calendar.MONTH);
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        startDateToPassInAPI = apiDateFormat.format(calendar.getTime());
        fromDateTV.setText(dateFormat.format(calendar.getTime()));
        startDate = calendar.getTime();

        if(Utility.isOnline(context)) {
            getAllOrder(noOfPages);
        }
        else
        {
            Utility.noInternetError(context);
        }


       logoutBTN.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(v.getContext(), Login.class));

           }
       });

       backPressed.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onBackPressed();
           }
       });


        fromDateLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int selectYear, int selectMonth, int selectDay) {

                                calendar.set(Calendar.YEAR, selectYear);
                                calendar.set(Calendar.MONTH, selectMonth);
                                calendar.set(Calendar.DAY_OF_MONTH, selectDay);

                                if (!calendar.getTime().after(endDate)) {
                                    startDate = calendar.getTime();
                                    startYear = selectYear;
                                    startMonth = selectMonth;
                                    startDay = selectDay;
                                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US),
                                            apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                                    startDateToPassInAPI = apiDateFormat.format(calendar.getTime());
                                    Toast.makeText(context,startDateToPassInAPI,Toast.LENGTH_LONG).show();
                                    fromDateTV.setText(dateFormat.format(calendar.getTime()));
                                } else {
                                    Toast.makeText(context, "From date must be before to date.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, startYear, startMonth, startDay);
                datePickerDialog.getDatePicker().setBackgroundColor(getResources().getColor(R.color.white));
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }
        });

        toDateLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectYear, int selectMonth, int selectDay) {

                        calendar.set(Calendar.YEAR, selectYear);
                        calendar.set(Calendar.MONTH, selectMonth);
                        calendar.set(Calendar.DAY_OF_MONTH, selectDay);
                        if (!calendar.getTime().before(startDate)) {
                            endDate = calendar.getTime();
                            endYear = selectYear;
                            endMonth = selectMonth;
                            endDay = selectDay;
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US),
                                    apiDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                            endDateToPassInAPI = apiDateFormat.format(calendar.getTime());
                            toDateTV.setText(dateFormat.format(calendar.getTime()));
                        } else {
                            Toast.makeText(AdminOrderFragment.this, "To date must be after from date.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }, endYear, endMonth, endDay);
                datePickerDialog.getDatePicker().setBackgroundColor(getResources().getColor(R.color.white));
                datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
                datePickerDialog.show();
            }
        });

        findBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Utility.isOnline(context)) {
                 //   getAllOrderDetailAPI();
                } else {
                    Utility.noInternetError(context);
                }
            }
        });

//        getAllOrderDetailAPI();
  //      return view;
    }
 /*   private void getAllOrder() {
        final Dialog dialog = Utility.showProgress(context);
        Call<ResponseBody> get = Utility.retroInterface().getOrderList();
 */
 private  void getAllOrder(int pageno) {
     final Dialog dialog = Utility.showProgress(context);
     //   Call<ResponseBody> get = Utility.retroInterface().getAllACategory("2");
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
                            int noOfPages2 = objRO.getInt("total_pages");
                            for (int i = 0; i < jA.length(); i++) {
                                JSONObject obj = jA.getJSONObject(i);
                                ArrayList<OrderItem> productsList = new ArrayList<OrderItem>();
                                ArrayList<Var> varientList = new ArrayList<Var>();
                                Orders order = new Orders();
                                order.setOrderId(obj.getString("order_id"));
                                order.setPhoneNumber(obj.getString("user_mobile"));
                                order.setDeviceid(obj.getString("unique_deviceid"));
                                String date = obj.getString("order_created_date");
                                date = date.substring(0,10);
                                order.setOrderDate(date);
                                order.setAddress(obj.getString("address"));
                                order.setDisAmt(obj.getString("discount_amount"));
                                order.setCity(obj.getString("city"));
                                order.setPinCode(obj.getString("pincode"));
                                order.setLandmark(obj.getString("landmark"));
                                order.setTotalOrderItem(obj.getString("amount"));
                                order.setOrderStatus(obj.getString("order_status"));
                                JSONArray jP = obj.getJSONArray("products");
                                for(int j=0;j< jP.length();j++) {
                                    JSONObject objP = jP.getJSONObject(j);
                                    OrderItem oi = new OrderItem();
                                    oi.setItemId(objP.getString("pdt_id"));
                                    JSONArray jV = objP.getJSONArray("variant");
                                    for(int k=0;k<jV.length();k++) {
                                        JSONObject objV = jV.getJSONObject(k);
                                        Var v = new Var();
                                        v.setVarType(objV.getString("var_type"));
                                        v.setVarActualAmt(objV.getString("var_actual_price"));
                                        v.setVarDisAmt(objV.getString("var_discount_price"));
                                        v.setVarQty(objV.getString("var_qty"));
                                        varientList.add(v);
                                        //othdis=othdis+Integer.parseInt(v.getVarDisAmt());
                                       // othDis.add(othdis);
                                    }
                                    oi.setOrder_varients(varientList);
                                    productsList.add(oi);
                                }
                                order.setOrderItems(productsList);

                                orderList.add(order);
                           }


                            if(noOfPages2>1 && noOfPages<noOfPages2) {
                                noOfPages = noOfPages + 1;
                                getAllOrder(noOfPages);
                                Log.i("noOfPages : ", "" + noOfPages2);
                            }
                            else {
                                if (orderList.size() > 0) {
                                    Date sdate=new SimpleDateFormat("yyyy-MM-dd").parse(startDateToPassInAPI);
                                    Date edate=new SimpleDateFormat("yyyy-MM-dd").parse(endDateToPassInAPI);

                                    ArrayList<Orders> orderList2 = new ArrayList<>();
                                    ArrayList<Var> varList2 = new ArrayList<>();
                                    ArrayList<OrderItem> productsList2 = new ArrayList<OrderItem>();

                                    for(int d=0;d<orderList.size();d++){
                                        Date date1=new SimpleDateFormat("yyyy-MM-dd").parse(orderList.get(d).getOrderDate());
                                        if(date1.after(sdate)&& date1.before(edate)){
                                            Orders order = new Orders();
                                            order.setOrderId(orderList.get(d).getOrderID());
                                            order.setPhoneNumber(orderList.get(d).getPhoneNumber());
                                            order.setDeviceid(orderList.get(d).getDeviceid());
                                            order.setOrderDate(orderList.get(d).getOrderDate());
                                            order.setAddress(orderList.get(d).getAddress());
                                            order.setDisAmt(orderList.get(d).getDisAmt());
                                            order.setCity(orderList.get(d).getCity());
                                            order.setPinCode(orderList.get(d).getPinCode());
                                            order.setLandmark(orderList.get(d).getLandmark());
                                            order.setTotalOrderItem(orderList.get(d).getTotalOrderItem());
                                            order.setOrderStatus(orderList.get(d).getOrderStatus());
                                            order.setOrderItems(orderList.get(d).getOrderItems());

                                            for(int j=0;j< orderList.get(d).getOrderItems().size();j++) {

                                                OrderItem oi = new OrderItem();
                                                oi.setItemId(orderList.get(d).getOrderItems().get(j).getItemid());
                                                for(int k=0;k<orderList.get(d).getOrderItems().get(j).getOrder_varients().size();k++) {

                                                    Var v = new Var();
                                                    v.setVarType(orderList.get(d).getOrderItems().get(j).getOrder_varients().get(k).getVarType());
                                                    v.setVarActualAmt(orderList.get(d).getOrderItems().get(j).getOrder_varients().get(k).getVarActualAmt());
                                                    v.setVarDisAmt(orderList.get(d).getOrderItems().get(j).getOrder_varients().get(k).getVarDisAmt());
                                                    v.setVarQty(orderList.get(d).getOrderItems().get(j).getOrder_varients().get(k).getVar_qty());
                                                    varList2.add(v);
                                                    //othdis=othdis+Integer.parseInt(v.getVarDisAmt());
                                                    // othDis.add(othdis);
                                                }
                                                oi.setOrder_varients(varList2);
                                                productsList2.add(oi);
                                            }
                                            order.setOrderItems(productsList2);
                                            orderList2.add(order);
                                        }
                                    }

                                  //  Collections.sort(orderList2,);
                                    emptyOrderArea.setVisibility(View.GONE);
                                    orderRV.setVisibility(View.VISIBLE);
                                    adminOrderAdapter = new AdminOrderAdapter(AdminOrderFragment.this, orderList2);
                                    orderRV.setAdapter(adminOrderAdapter);
                                    adminOrderAdapter.notifyDataSetChanged();
                                } else {
                                    emptyOrderArea.setVisibility(View.VISIBLE);
                                    orderRV.setVisibility(View.GONE);
                                }
                            }

                        /*    adminOrderAdapter = new AdminOrderAdapter(AdminOrderFragment.this, orderList);
                            orderRV.setAdapter(adminOrderAdapter);
                            adminOrderAdapter.notifyDataSetChanged();
*/

                        }catch(JSONException je){
                            Toast.makeText(context, "JSONEXCE : " + je.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        //recyclerView.setAdapter(new AdminSalesmanUserAdapter(getActivity(),salesmanDetails));
                        // }
                        //  }
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

}