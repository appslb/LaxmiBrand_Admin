package com.e.laxmibrand_admin.admin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.beans.Orders;
import java.util.ArrayList;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.MyViewHolder> implements Filterable {
    Context context;
   ArrayList<Orders> dataList, dataListFiltered;

    TextView noDataTV;
 //   public AdminOrderAdapter(Context context, ArrayList<GetAllOrderDetailResponse.ResponseData> dataList, TextView noDataTV) {
 public AdminOrderAdapter(Context context, ArrayList<Orders> dataList) {
     this.context = context;
       this.dataList = dataList;
        this.dataListFiltered = dataList;
        this.noDataTV = noDataTV;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.admin_order_list_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int i) {
        try {

            viewHolder.dateTV.setText(dataListFiltered.get(i).getOrderDate());
            viewHolder.orderNoTV.setText("Order no - " + dataListFiltered.get(i).getOrderID() + ", ordered by - " +dataListFiltered.get(i).getPhoneNumber());
            viewHolder.orderstatus.setText("Order Status : " + dataListFiltered.get(i).getOrderStatus());
         //   viewHolder.totalTV.setText("Total Amount : " + dataListFiltered.get(i).getTotalOrderItem());
            int tot = Integer.parseInt(dataList.get(i).getTotalOrderItem());
            int totwithdel=0;
            int totwithdis=0;
            if(Integer.parseInt(dataList.get(i).getTotalOrderItem())<=500)
            {
                totwithdel = tot+50;
                // viewHolder.totalOrderAmount.setText("Total Amount : " + dataList.get(i).getTotalOrderItem());
            }
            else
            {
                totwithdel = tot;
                //  viewHolder.totalOrderAmount.setText("Total Amount : " + dataList.get(i).getTotalOrderItem());

            }
            if(Integer.parseInt(dataList.get(i).getDisAmt()) > 0)
                totwithdel = totwithdel-Integer.parseInt(dataList.get(i).getDisAmt());



            viewHolder.totalTV.setText("Total Amount : " + (totwithdel));





            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                                     Intent orderDetailActivity = new Intent(context,AdminOrderDetailsActivity.class);
                                   orderDetailActivity.putExtra("orderid",dataList.get(i).getOrderID());
                                     context.startActivity(orderDetailActivity);

                }
            });
        } catch (Exception e) {
            Log.e("exc", e.getMessage());
        }
    }


    @Override
    public int getItemCount() {
        return dataListFiltered.size();
    }

/*
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    dataListFiltered = dataList;
                } else {
//                    ArrayList<GetAllOrderDetailResponse.ResponseData> filteredList = new ArrayList<>();
                    ArrayList<Orders> filteredList = new ArrayList<>();

                    for (Orders row : dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                        if (row.getOrderStatus().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getOrderID().toLowerCase().contains(charString.toLowerCase()) ||
                                (row.getPhoneNumber()).toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }

                    }

                    dataListFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = dataListFiltered;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
             //   dataListFiltered = (ArrayList<GetAllOrderDetailResponse.ResponseData>) filterResults.values;
                dataListFiltered = (ArrayList<Orders>) filterResults.values;

                if (dataListFiltered != null && dataListFiltered.size() > 0) {
                    noDataTV.setVisibility(View.GONE);
                } else {
                    noDataTV.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            }
        };
    }
*/


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView orderNoTV, totalTV, dateTV,orderstatus;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNoTV = itemView.findViewById(R.id.orderid);
            dateTV = itemView.findViewById(R.id.orderdate);
            totalTV = itemView.findViewById(R.id.totalAmount);
            orderstatus=itemView.findViewById(R.id.orderstatus);

        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {

                    dataListFiltered = dataList;

                } else {
                    // filtered_salesmanDetailsList.clear();

                    ArrayList<Orders> filteredList = new ArrayList<Orders>();
                    for (Orders row : dataList) {
                        //change this to filter according to your case
                        if (row.getOrderStatus().toLowerCase().contains(charString.toLowerCase()) ||
                                row.getOrderID().toLowerCase().contains(charString.toLowerCase()) ||
                                (row.getPhoneNumber()).toLowerCase().contains(charString.toLowerCase())||(row.getAddress()).toLowerCase().contains(charString.toLowerCase()) || (row.getCity()).toLowerCase().contains(charString.toLowerCase())||(row.getLandmark()).toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }

                    }


                    dataListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = dataListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                dataListFiltered = (ArrayList) filterResults.values;
                notifyDataSetChanged();

            }
        };

    }


}
