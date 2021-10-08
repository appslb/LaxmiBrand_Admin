package com.e.laxmibrand_admin.admin;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.admin.product.ProductListAdapter;
import com.e.laxmibrand_admin.beans.OrderItem;
import com.e.laxmibrand_admin.beans.Orders;
import com.e.laxmibrand_admin.beans.Products;
import com.e.laxmibrand_admin.beans.Var;
import com.e.laxmibrand_admin.utils.Utility;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminOrderDetailsItemAdapter extends RecyclerView.Adapter<AdminOrderDetailsItemAdapter.MyViewHolder>{
    Context context;
    ArrayList<OrderItem> orderItemList;
    ArrayList<Products> productList;
    ArrayList<Var> varItemList;

    int noOfPages;
    public AdminOrderDetailsItemAdapter(Context context, ArrayList<Var> varItemList) {
    this.context = context;
    noOfPages=1;
    this.varItemList=varItemList;
    productList =new ArrayList<Products>();
    getAllProducts(noOfPages);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.order_item_row_list_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int i) {

        viewHolder.ItemWeight.setText(varItemList.get(i).getVarType());
        viewHolder.ItemRate.setText(varItemList.get(i).getVarActualAmt());
        viewHolder.ItemQTY.setText(varItemList.get(i).getVar_qty());
        viewHolder.ItemAmount.setText(String.valueOf(Integer.parseInt(varItemList.get(i).getVarActualAmt())*Integer.parseInt(varItemList.get(i).getVar_qty())));

    }

    @Override
    public int getItemCount() {
        return varItemList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ItemWeight,ItemRate,ItemQTY,ItemAmount;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemWeight = itemView.findViewById(R.id.ItemWeight);
            ItemRate = itemView.findViewById(R.id.ItemRate);
            ItemQTY = itemView.findViewById(R.id.ItemQty);
            ItemAmount = itemView.findViewById(R.id.ItemAmount);

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



