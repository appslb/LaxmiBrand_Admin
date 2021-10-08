package com.e.laxmibrand_admin.admin.category;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.admin.Login;
import com.e.laxmibrand_admin.admin.MainActivity;
import com.e.laxmibrand_admin.admin.PageNoAdapter;
import com.e.laxmibrand_admin.admin.product.AddProduct;
import com.e.laxmibrand_admin.admin.product.ProductList;
import com.e.laxmibrand_admin.beans.AdminCategory;
import com.e.laxmibrand_admin.beans.BaseResponse;
import com.e.laxmibrand_admin.beans.GetAllCategoryResponse;
import com.e.laxmibrand_admin.beans.LoginResponse;
import com.e.laxmibrand_admin.utils.ImageFilePath;
import com.e.laxmibrand_admin.utils.Utility;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryList extends AppCompatActivity {
    ImageView addCategory;
   public static Dialog addCategoryDialog;
    RecyclerView categoryList,pagenorv;
    static int noOfPages;
    static int[] pages;
    File imgFile;
    static PageNoAdapter pagenoAdapter;
    LinearLayout empty_category;
    TextView addCategoryBtn;
    JsonObject catObject;
    private static final int PICK_IMAGE = 100;
    Uri imageUri=null;
    static Context context;
    static ImageView catImage;
    ImageView backPressedImage,logoutBTN;
    CategoryListAdapter categoryListAdapter;
    ArrayList<AdminCategory> catList;
    GetAllCategoryResponse.ResponseData allCatData;
    RequestBody requestFile,catname,isactive;
    MultipartBody.Part catimage=null;
    ByteArrayOutputStream byteBuff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_list_page);
        pagenorv = (RecyclerView) findViewById(R.id.pagenoRV);

        addCategoryBtn = (TextView) findViewById(R.id.addCategoryBtn);
        empty_category = (LinearLayout) findViewById(R.id.empty_category);
        addCategory = (ImageView) findViewById(R.id.addCategory);
        categoryList = (RecyclerView) findViewById(R.id.categoryList);
        backPressedImage = (ImageView) findViewById(R.id.back_pressed);
        logoutBTN = (ImageView) findViewById(R.id.logoutBTN);
        catList = new ArrayList<AdminCategory>();
        context = CategoryList.this;
        categoryList.setLayoutManager(new LinearLayoutManager(CategoryList.this));
        noOfPages = 1;
        if (Utility.isOnline(context)) {
            getAllCategoryDetails(noOfPages);
        }
        else {
            Utility.noInternetError(context);

        }


        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    openAddCategoryDialog();
            }
        });

        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(v.getContext(), Login.class));

            }
        });

        backPressedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*addCategoryDialog = new Dialog(CategoryList.this);
                addCategoryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                addCategoryDialog.setContentView(R.layout.add_category);
                addCategoryDialog.show();

                EditText catName = (EditText) addCategoryDialog.findViewById(R.id.categoryName);
                catImage = (ImageView) addCategoryDialog.findViewById(R.id.catImage);
                ImageButton addImageBtn = (ImageButton) addCategoryDialog.findViewById(R.id.addImageBtn);
                Button addCategoryBtn = (Button) addCategoryDialog.findViewById(R.id.addCategoryBtn);

                addImageBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGallery();
                    }
                });


                addCategoryBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AdminCategory cat = new AdminCategory();
                        cat.setCatName(catName.getText().toString());
                        Log.i("CATEGORY NAME : ", catName.getText().toString().trim());
                        if (!catName.getText().toString().trim().isEmpty()) {
                            if (Utility.isOnline(v.getContext())) {

                                String cname=catName.getText().toString().trim();
                                cname =cname.replace(" ","_");
                                catname = RequestBody.create(MediaType.parse("multipart/form-data"), cname);
                                isactive = RequestBody.create(MediaType.parse("multipart/form-data"), "1");

                                addNewCategory();

                            } else {
                                Utility.noInternetError(v.getContext());
                            }
                        } else {
                            Toast.makeText(context, "Please Enter Category Name", Toast.LENGTH_SHORT).show();
                        }

                    }
                });*/
openAddCategoryDialog();
            }
        });


    }


    public  void openAddCategoryDialog()
    {
        addCategoryDialog = new Dialog(context);
        addCategoryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addCategoryDialog.setContentView(R.layout.add_category);
        addCategoryDialog.show();
        TextView title=(TextView) addCategoryDialog.findViewById(R.id.title);
        title.setText("ADD CATEGORY");
        EditText catName = (EditText) addCategoryDialog.findViewById(R.id.categoryName);
        catImage = (ImageView) addCategoryDialog.findViewById(R.id.catImage);
        ImageButton addImageBtn = (ImageButton) addCategoryDialog.findViewById(R.id.addImageBtn);
        Button addCategoryBtn = (Button) addCategoryDialog.findViewById(R.id.addCategoryBtn);
        addCategoryBtn.setText("Add Category");
        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminCategory cat = new AdminCategory();
                cat.setCatName(catName.getText().toString());
                catimage = prepareFilePart("image[]");

                Log.i("CATEGORY NAME : ", catName.getText().toString().trim());
                if (!catName.getText().toString().trim().isEmpty()) {
if(imageUri!=(null)) {
    if (Utility.isOnline(v.getContext())) {
        catname = RequestBody.create(MediaType.parse("multipart/form-data"), catName.getText().toString().trim());
        isactive = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
        addNewCategory();

    } else {
        Utility.noInternetError(v.getContext());
    }
}
else
{
    Toast.makeText(context, "Please upload an image for category.", Toast.LENGTH_SHORT).show();

}
                } else {
                    Toast.makeText(context, "Please Enter Category Name", Toast.LENGTH_SHORT).show();
                }

           /*     catList.add(cat);
                categoryListAdapter.notifyDataSetChanged();
                addCategoryDialog.dismiss();*/
                //Refresh Category List to show new category added
            }
        });

    }

    public void openEditCategoryDialog(String catimg,String categoryId,String categoryName)
    {
        addCategoryDialog = new Dialog(context);
        addCategoryDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        addCategoryDialog.setContentView(R.layout.add_category);
        addCategoryDialog.show();
        TextView title=(TextView) addCategoryDialog.findViewById(R.id.title);
        title.setText("EDIT CATEGORY");

        EditText catName = (EditText) addCategoryDialog.findViewById(R.id.categoryName);
        catName.setText(categoryName);
        catImage = (ImageView) addCategoryDialog.findViewById(R.id.catImage);
        try {
            String images = catimg;
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
                        .into(catImage);

            }
            else {
                Glide.with(context)
                        .load(R.drawable.watermark_icon)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.watermark_icon)
                        .error(R.drawable.watermark_icon)
                        .into(catImage);

            }

        } catch (Exception e) {
            Log.e("exc_catImg", e.getMessage());
        }

        ImageButton addImageBtn = (ImageButton) addCategoryDialog.findViewById(R.id.addImageBtn);
        addImageBtn.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_edit_24));
        Button addCategoryBtn = (Button) addCategoryDialog.findViewById(R.id.addCategoryBtn);
        addCategoryBtn.setText("UPDATE");

        addImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });


        addCategoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AdminCategory cat = new AdminCategory();
                cat.setCatName(catName.getText().toString());

                if (!catName.getText().toString().trim().isEmpty()) {
                    if(imageUri!=(null) || catImage.getDrawable()!=null) {
                        if (Utility.isOnline(v.getContext())) {
                            RequestBody catid = RequestBody.create(MediaType.parse("multipart/form-data"), categoryId);

                            catname = RequestBody.create(MediaType.parse("multipart/form-data"), catName.getText().toString().trim());
                            isactive = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
                           editNewCategory(catid);

                        } else {
                            Utility.noInternetError(v.getContext());
                        }
                    }
                    else {


                        Toast.makeText(context, "Please upload an image for category.", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(context, "Please Enter Category Name", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    public void addCategoryListItems() {
        for (int i = 0; i < 5; i++) {
            AdminCategory cat = new AdminCategory();
            cat.setCatName("Category" + (i + 1));
            catList.add(cat);
        }

    }


    private  void openGallery() {
        catImage.setBackground(null);
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }


    private MultipartBody.Part prepareFilePart(String partName) {
        MultipartBody.Part catimageFile;
        requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        catimageFile = MultipartBody.Part.createFormData(partName, "", requestFile);
        if(imageUri!=null) {
            try {
                imgFile = new File(ImageFilePath.getPath(CategoryList.this, imageUri));
                float fileSizeKB = Float.parseFloat(String.valueOf(imgFile.length() / 1024));
                if (fileSizeKB > 500) {
                    int quality = (int) (50000 / fileSizeKB);
                    imgFile = new Compressor(CategoryList.this).setQuality(quality).compressToFile(imgFile);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("imguri exc",e.getMessage());
            }
            requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
            catimageFile = MultipartBody.Part.createFormData(partName, imgFile.getName(), requestFile);
        }
        return catimageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            catImage.setImageURI(imageUri);
        }
    }

    private void addNewCategory() {
        final Dialog dialog = Utility.showProgress(context);
      catimage = prepareFilePart("image[]");
        Call<BaseResponse> addCategory = Utility.retroInterface().insert_category(catname,isactive,catimage);
        addCategory.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        if (response.body().getStatus().equalsIgnoreCase("success")) {
                            Toast.makeText(context, "Successfully added new Category.", Toast.LENGTH_SHORT).show();
                            addCategoryDialog.dismiss();
                            Intent i=new Intent(context, CategoryList.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else {
                            Toast.makeText(context, "Could Not Add New Category", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Could Not add New Category", Toast.LENGTH_SHORT).show();

//                        Toast.makeText(context, "Wrong Email or Password.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Utility.somethingWentWrong(context);
                }
            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utility.dismissDialog(dialog);
                Utility.somethingWentWrong(context);
            }
        });


    }

    private void editNewCategory(RequestBody cat_id) {
        final Dialog dialog = Utility.showProgress(context);
       catimage = prepareFilePart("image[]");
        Call<BaseResponse> addCategory = Utility.retroInterface().update_category(cat_id,catname,isactive,catimage);

        addCategory.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        if (response.body().getStatus().equalsIgnoreCase("success")) {
                            Toast.makeText(context, "Successfully updated category.", Toast.LENGTH_SHORT).show();
                            addCategoryDialog.dismiss();
                            Intent i=new Intent(context, CategoryList.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else {
                            Toast.makeText(context, "Could Not Add New Category", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Could Not add New Category", Toast.LENGTH_SHORT).show();

//                        Toast.makeText(context, "Wrong Email or Password.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Utility.somethingWentWrong(context);

                }
            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utility.dismissDialog(dialog);
                Utility.somethingWentWrong(context);
            //    Toast.makeText(context, "Throwable"+t.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }


    private void getAllCategoryDetails(int pageno) {
        final Dialog dialog = Utility.showProgress(context);
    //    Call<ResponseBody> get = Utility.retroInterface().getAllCategory();
        Call<ResponseBody> get = Utility.retroInterface().getCategoryByPage("http://dev.polymerbazaar.com/laxmibrand/admin/category/list/"+pageno);

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

                    // JSONArray jsonElements = object.getJSONArray("data");
                     JSONObject objRO = object.getJSONObject("data");
                     int noOfPages2 = objRO.getInt("total_pages");

                    // JSONObject objR = jsonElements.getJSONObject(0);
                     JSONArray jA = objRO.getJSONArray("result");

                     for (int i = 0; i < jA.length(); i++) {
                         JSONObject obj = jA.getJSONObject(i);
                         String name = obj.getString("category_name");
                         AdminCategory cat = new AdminCategory();
                         cat.setCatName(name);
                         cat.setCatId(obj.getString("category_id"));
                         cat.setCatImage(obj.getString("image"));
                         catList.add(cat);

                     }

                     if(noOfPages2>1 && noOfPages<noOfPages2) {
                         noOfPages = noOfPages + 1;
                         getAllCategoryDetails(noOfPages);

                     }else {

                         if (catList.size() > 0) {
                             categoryList.setVisibility(View.VISIBLE);
                             empty_category.setVisibility(View.GONE);
                             categoryListAdapter = new CategoryListAdapter(CategoryList.this, catList);
                             categoryList.setAdapter(categoryListAdapter);
                             categoryListAdapter.notifyDataSetChanged();

                         } else {
                             categoryList.setVisibility(View.GONE);
                             empty_category.setVisibility(View.VISIBLE);

                         }
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


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        // implement your override logic here
        return;
    }
}