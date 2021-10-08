package com.e.laxmibrand_admin.admin.category;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.admin.product.ProductList;
import com.e.laxmibrand_admin.beans.AdminCategory;
import com.e.laxmibrand_admin.beans.BaseResponse;
import com.e.laxmibrand_admin.utils.Utility;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryItemViewHolder> {

    Context context;
    ArrayList<AdminCategory> catList;
    JsonObject categoryDetailsUpdate;
    AlertDialog.Builder builder;
    JsonObject categoryDetailsDelete;
    int catidtodel=0;
    public static Dialog editCategoryDialog;
    private static final int PICK_IMAGE = 100;
    RequestBody requestFile,catname,isactive;
    ImageView catImage;
    public CategoryListAdapter(Context context, ArrayList<AdminCategory> catList) {
        this.context = context;
        this.catList = catList;
        categoryDetailsUpdate = new JsonObject();
        categoryDetailsDelete = new JsonObject();

    }
    @NonNull
    @Override
    public CategoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.category_list_item, viewGroup, false);
        return new CategoryItemViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final CategoryItemViewHolder viewHolder, final int i) {

        try {
            String images = catList.get(i).getCatImage();
            if (!images.equals("") && (images.contains("https")||images.contains("http"))) {
                //String[] imagesList = images.split(",");
                String S1 = images;
                if (S1.contains("https"))
                    S1 = S1.replace("https", "http");
                Log.i("image image :", S1);
                Glide.with(context)
                        .load(S1)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.watermark_icon)
                        .error(R.drawable.watermark_icon)
                        .into(viewHolder.catImage);
            }

            else {
                Glide.with(context)
                        .load(R.drawable.watermark_icon)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.watermark_icon)
                        .error(R.drawable.watermark_icon)
                        .into(viewHolder.catImage);

            }
        } catch (Exception e) {
            Log.e("exc", e.getMessage());
        }

        String cname=catList.get(i).getCatName();
        cname =cname.replace("_"," ");
        viewHolder.catText.setText(cname);

        viewHolder.catDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                catidtodel=i;
                builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure?");
                builder.setPositiveButton("Yes", dialogClickListener);
                builder.setNegativeButton("No", dialogClickListener).show();



            }
        });

        viewHolder.catEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CategoryList)context).openEditCategoryDialog(catList.get(i).getCatImage(),catList.get(i).getCatid(),catList.get(i).getCatName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return catList.size();
    }
        public class CategoryItemViewHolder extends RecyclerView.ViewHolder {
        ImageView catImage,catEdit,catDelete;TextView catText;

        public CategoryItemViewHolder(@NonNull View itemView) {
            super(itemView);
            catImage = itemView.findViewById(R.id.catImage);
            catEdit = itemView.findViewById(R.id.editBtn);
            catDelete = itemView.findViewById(R.id.deleteBtn);
            catText = itemView.findViewById(R.id.catText); }
    }



    private void deleteCategory(JsonObject categoryIdForDelete) {
        final Dialog dialog = Utility.showProgress(context);
        //  ArrayList<MultipartBody.Part> images = prepareFilePart("image[]");
        Call<ResponseBody> deleteCat = Utility.retroInterface().deleteCategory(categoryIdForDelete);
        deleteCat.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, " Category Deleted", Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(context, CategoryList.class);
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

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    categoryDetailsDelete = new JsonObject();
                    categoryDetailsDelete.addProperty("id", catList.get(catidtodel).getCatid());
                    deleteCategory(categoryDetailsDelete);
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    dialog.dismiss();
                    catidtodel=0;
                    break;
            }
        }
    };


}
