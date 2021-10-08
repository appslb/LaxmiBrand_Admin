package com.e.laxmibrand_admin.admin;

import android.content.Context;
import android.graphics.pdf.PdfDocument;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.admin.product.DemoObjectFragment;
import com.e.laxmibrand_admin.admin.product.ProductList;
import com.e.laxmibrand_admin.beans.DiscountedProducts;

import java.util.List;

public class PageNoAdapter extends RecyclerView.Adapter<PageNoAdapter.PageNoViewHolder> {

    Context context;
    static int[] pages;
static int pos=0;

    public PageNoAdapter(Context context, int[] pages) {
        this.context = context;
        this.pages=pages;
    }

    @NonNull
    @Override
    public PageNoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.page_no_list_item, parent, false);
        return new PageNoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageNoViewHolder holder, int position) {
            holder.pageNoText.setText(String.valueOf(pages[position]));
        pos=position;
                holder.pageNoText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DemoObjectFragment.getAllProducts(Integer.parseInt(holder.pageNoText.getText().toString()));
                    }
                });
    }

    @Override
    public int getItemCount() {
        return pages.length;
    }

    public static class PageNoViewHolder extends  RecyclerView.ViewHolder{
        TextView pageNoText;
        public PageNoViewHolder(@NonNull View itemView) {
            super(itemView);

            pageNoText = itemView.findViewById(R.id.pagenoText);


        }
    }
}
