package com.e.laxmibrand_admin.admin.product;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.beans.Category;
import com.e.laxmibrand_admin.beans.Products;

import java.io.IOException;
import java.util.ArrayList;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ProductItemViewHolder> {
    int pos;

    Context context;
    ArrayList<Products> productList;
    public ProductListAdapter(Context context, ArrayList<Products> productList) {
        this.context = context;
        this.productList = productList;
    }
    @NonNull
    @Override
    public ProductItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.product_list_item, viewGroup, false);
        return new ProductItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final ProductItemViewHolder viewHolder, final int i) {
        pos = i;
        String variantStr = "",varPrice="";
/*

        if (productList.get(i).getPdt_price_enable_500gm().equals("1")) {
            if (variantStr.equals("")){
                variantStr = variantStr + " 500gm";
        } else {
            variantStr = variantStr + " , 500gm";
        }
    }
        if(productList.get(i).getPdt_price_enable_1kg().equals("1")) {
            if (variantStr.equals(""))
                variantStr = variantStr + " 1kg";
            else
                variantStr = variantStr + " , 1kg";
        }
            if(productList.get(i).getPdt_price_enable_2kg().equals("1")) {
             if(variantStr.equals(""))
                 variantStr = variantStr + " 2kg";
                else
                variantStr = variantStr + " , 2kg";
            }
                if(productList.get(i).getPdt_price_enable_3kg().equals("1"))
                if(variantStr.equals(""))
                    variantStr = variantStr + " 3kg";
                else
                    variantStr=variantStr+" , 3kg";
        if(productList.get(i).getPdt_price_enable_5kg().equals("1"))
            if(variantStr.equals(""))
                variantStr = variantStr + "5kg";
            else
                    variantStr=variantStr+" , 5kg";

            viewHolder.variantText.setText(variantStr);
*/

        for(int n=0;n<productList.get(i).getVarientItems().size();n++) {
            Log.i("varsize", "" + productList.get(i).getVarientItems().size());

            Log.i("vartype", productList.get(i).getVarientItems().get(n).getVarType());
            if (variantStr.equals("")){
                variantStr = productList.get(i).getVarientItems().get(n).getVarType();
        }
                    else {
                variantStr=variantStr + "," + productList.get(i).getVarientItems().get(n).getVarType();

            }
            varPrice = varPrice + "\u20B9" + productList.get(i).getVarientItems().get(n).getVarActualAmt() + " per " + productList.get(i).getVarientItems().get(n).getVarType() + "\n";

        }

        viewHolder.variantText.setText(variantStr);
        viewHolder.priceText.setText(varPrice);

        variantStr="";
        varPrice="";


        try {
            String images = productList.get(i).getPrdt_images();
            if (!images.equals("") && (images.contains("https")||images.contains("http"))) {
                String[] imagesList = images.split(",");
                String S1 = imagesList[0];
                if (S1.contains("https"))
                    S1 = S1.replace("https", "http");

                Log.i("image image :", S1);

                Glide.with(context)
                        .load(S1)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.watermark_icon)
                        .error(R.drawable.watermark_icon)
                       .into(viewHolder.productImage);

            }

        } catch (Exception e) {
            Log.e("exc", e.getMessage());
        }




        if(productList.get(i).getIs_active().equals("1")) {
            viewHolder.displayToUser.setImageDrawable(context.getDrawable(R.drawable.ic_check_24));
        }
        else
            {
                viewHolder.displayToUser.setImageDrawable(context.getDrawable(R.drawable.ic_cross));
        }

        String pname=productList.get(i).getPdt_name();
        pname =pname.replace("_"," ");
        viewHolder.productName.setText(pname);

        viewHolder.productDescription.setText(productList.get(i).getPdt_about());

        viewHolder.editItem.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            context.startActivity(new Intent(v.getContext(),EditProduct.class));
        }
    });

        viewHolder.productCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startProductDetail =new Intent(v.getContext(),AdminProductDetails.class);
                startProductDetail.putExtra("productid",productList.get(i).getPdt_id());
                v.getContext().startActivity(startProductDetail);

            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }


    public class ProductItemViewHolder extends RecyclerView.ViewHolder {
        TextView productName,productDescription,variantText,priceText;
        CardView productCard;
            ImageView editItem,productImage,displayToUser;
        public ProductItemViewHolder(@NonNull View itemView) {
            super(itemView);
          /*  catImage = itemView.findViewById(R.id.catImage);
            catText = itemView.findViewById(R.id.catText); */
            editItem = itemView.findViewById(R.id.editIcon);
            productName = itemView.findViewById(R.id.productName);
            productDescription = itemView.findViewById(R.id.productDescription);
            displayToUser = itemView.findViewById(R.id.showToUserIcon);
            priceText = itemView.findViewById(R.id.priceText);
            productImage=itemView.findViewById(R.id.productImage);
            productCard = itemView.findViewById(R.id.productCard);
            variantText = itemView.findViewById(R.id.variantText);
           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent startProductDetail =new Intent(v.getContext(),AdminProductDetails.class);
                    startProductDetail.putExtra("productid",productList.get(pos).getPdt_id());
                    Toast.makeText(v.getContext(),"productid -"+productList.get(pos).getPdt_id(),Toast.LENGTH_LONG).show();
                    v.getContext().startActivity(startProductDetail);
                }
            });*/
        }
    }

}
