package com.e.laxmibrand_admin.admin;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.admin.category.CategoryList;
import com.e.laxmibrand_admin.beans.AdminCategory;
import com.e.laxmibrand_admin.beans.AdminFAQ;
import com.e.laxmibrand_admin.utils.Utility;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FAQListAdapter extends RecyclerView.Adapter<FAQListAdapter.FAQItemViewHolder> {

    Context context;
    ArrayList<AdminFAQ> faqList;
    AlertDialog.Builder builder;
    JsonObject faqDetailsDelete;
    int faqidtodel=0;
    public FAQListAdapter(Context context,     ArrayList<AdminFAQ> faqList) {
        this.context = context;
        this.faqList = faqList;
    }
    @NonNull
    @Override
    public FAQItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.faq_list_item, viewGroup, false);
        return new FAQItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final FAQItemViewHolder viewHolder, final int i) {

        viewHolder.faqQues.setText(faqList.get(i).getFaqQues());
        viewHolder.faqAns.setText(faqList.get(i).getFaqAns());

        viewHolder.editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((AdminFAQs)context).editFAQDetails(faqList.get(i).getFaqid(),"e");
            }
        });

        viewHolder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faqidtodel=i;
                builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", dialogClickListener);
                builder.setNegativeButton("No", dialogClickListener).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return faqList.size();
    }


    public class FAQItemViewHolder extends RecyclerView.ViewHolder {
       TextView faqAns,faqQues;
       ImageView editIcon,deleteIcon;
        public FAQItemViewHolder(@NonNull View itemView) {
            super(itemView);
            faqAns = itemView.findViewById(R.id.faqAns);
            faqQues = itemView.findViewById(R.id.faqQues);
            editIcon =itemView.findViewById(R.id.editIcon);
            deleteIcon =itemView.findViewById(R.id.deleteIcon);
        }
    }


    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    faqDetailsDelete = new JsonObject();
                    faqDetailsDelete.addProperty("id", faqList.get(faqidtodel).getFaqid());
                    deleteFAQ(faqDetailsDelete);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    dialog.dismiss();
                    faqidtodel=0;
                    break;
            }
        }
    };
    private void deleteFAQ(JsonObject faqIdToDelete) {
        final Dialog dialog = Utility.showProgress(context);
        Call<ResponseBody> deleteCat = Utility.retroInterface().deleteFAQ(faqIdToDelete);
        deleteCat.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, " FAQ details removed.", Toast.LENGTH_SHORT).show();

                        Intent i=new Intent(context, AdminFAQs.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    } else {
                        Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();

                    Utility.somethingWentWrong(context);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Utility.dismissDialog(dialog);
                Toast.makeText(context,"exception here"+t.getMessage(),Toast.LENGTH_LONG).show();
                Utility.somethingWentWrong(context);
            }
        });


    }

}
