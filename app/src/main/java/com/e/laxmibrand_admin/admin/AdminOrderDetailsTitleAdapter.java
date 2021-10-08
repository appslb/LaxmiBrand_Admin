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

public class AdminOrderDetailsTitleAdapter extends RecyclerView.Adapter<AdminOrderDetailsTitleAdapter.MyViewHolder>{
    Context context;
    ArrayList<OrderItem> orderItemList;
    ArrayList<Var> varItemList;
AdminOrderDetailsItemAdapter adminOrderDetailsItemsAdapter;
    ArrayList<Products> orderedProductList;

    int noOfPages;
    public AdminOrderDetailsTitleAdapter(Context context, ArrayList<OrderItem> orderItemList,ArrayList<Products> orderedProductList) {
        this.context = context;
        noOfPages=1;
        this.orderItemList=orderItemList;
       this.orderedProductList =orderedProductList;
        varItemList = new ArrayList<Var>();
       }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.orderitem_row_list_title, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int i) {

    /*   // Log.i("in adapter itemname",orderedProductList.get(i).getPdt_name());
    //   viewHolder.ItemName.setText(orderedProductList.get(i).getPdt_name());

        varItemList=orderItemList.get(i).getOrder_varients();
        Log.i("in adapter varsize",""+varItemList.size());
        viewHolder.varItem.setLayoutManager(new LinearLayoutManager(context));
        adminOrderDetailsItemsAdapter = new AdminOrderDetailsItemAdapter(context,varItemList);
        viewHolder.varItem.setAdapter(adminOrderDetailsItemsAdapter);
        adminOrderDetailsItemsAdapter.notifyDataSetChanged();*/

        viewHolder.ItemName.setText(orderItemList.get(i).getItemName());
        varItemList=new ArrayList<Var>();
        viewHolder.varItem.setLayoutManager(new LinearLayoutManager(context));
        adminOrderDetailsItemsAdapter = new AdminOrderDetailsItemAdapter(context,orderItemList.get(i).getOrder_varients());
        viewHolder.varItem.setAdapter(adminOrderDetailsItemsAdapter);
        adminOrderDetailsItemsAdapter.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return orderItemList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ItemName;
        RecyclerView varItem;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ItemName = itemView.findViewById(R.id.ItemName);
varItem=itemView.findViewById(R.id.orderItemsVarList);

        }
    }


}



