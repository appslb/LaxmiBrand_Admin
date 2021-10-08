package com.e.laxmibrand_admin.admin.product;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/*
import com.app.sradesign.model.response.GetAllOrderDetailResponse;
import com.app.sradesign.utils.Utility;
*/

import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.beans.Orders;
import com.e.laxmibrand_admin.beans.Variant;

import java.util.ArrayList;

public class VariantListAdapter extends RecyclerView.Adapter<VariantListAdapter.MyViewHolder> {
    Context context;
ArrayList<String> vList;
    public VariantListAdapter(Context context,ArrayList<String> vList) {
        this.context = context;

           this.vList = vList;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.variant_list_item, viewGroup, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder viewHolder, final int i) {
        Variant var = new Variant();

        viewHolder.checkVariant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    var.setVariantName(viewHolder.variantName.getText().toString());

                    viewHolder.variantName.setText(vList.get(i));
                    viewHolder.itemRate.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            //  AddProduct.variantItemRate=viewHolder.itemRate.getText().toString().trim();
                            var.setItemRate(viewHolder.itemRate.getText().toString().trim());

                        }
                    });

                    viewHolder.discountRate.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            //AddProduct.variantDiscountRate=viewHolder.discountRate.getText().toString().trim();
                            var.setDiscountAmount(viewHolder.discountRate.getText().toString().trim());

                        }
                    });

                    viewHolder.itemAmount.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            //AddProduct.variantItemAmount=viewHolder.itemAmount.getText().toString().trim();
                            var.setItemAmount(viewHolder.itemAmount.getText().toString().trim());

                        }
                    });
               }
                else
                {

                }

            }
        });

    }


    @Override
    public int getItemCount() {
        return vList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView variantName;
        EditText itemRate, discountRate, itemAmount;
        CheckBox checkVariant;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkVariant = itemView.findViewById(R.id.selectVariant);
            variantName = itemView.findViewById(R.id.variantName);
            itemRate = itemView.findViewById(R.id.ItemRate);
            discountRate = itemView.findViewById(R.id.DiscountRate);
            itemAmount = itemView.findViewById(R.id.ItemAmount);
        }
    }

}
