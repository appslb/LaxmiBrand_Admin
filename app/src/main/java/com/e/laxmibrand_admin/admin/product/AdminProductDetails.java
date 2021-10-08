package com.e.laxmibrand_admin.admin.product;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.admin.Login;
import com.e.laxmibrand_admin.admin.category.CategoryList;
import com.e.laxmibrand_admin.admin.category.CategoryListAdapter;
import com.e.laxmibrand_admin.beans.AdminCategory;
import com.e.laxmibrand_admin.beans.BaseResponse;
import com.e.laxmibrand_admin.beans.Products;
import com.e.laxmibrand_admin.beans.UploadImageResponse;
import com.e.laxmibrand_admin.beans.Var;
import com.e.laxmibrand_admin.beans.Variant;
import com.e.laxmibrand_admin.utils.ImageFilePath;
import com.e.laxmibrand_admin.utils.PermissionUtils;
import com.e.laxmibrand_admin.utils.Utility;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;



public class AdminProductDetails extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextInputEditText productNameET,aboutProductET,storageUsesET,otherInfoET;
    EditText price30gm,discount30gm,price60gm,discount60gm,price100gm,discount100gm,price250gm,discount250gm,price500gm,discount500gm,price1kg,discount1kg,price2kg,discount2kg,price3kg,discount3kg,price5kg,discount5kg;
    ArrayList<AdminCategory> catList;
    AlertDialog.Builder builder;
    int pidtodel=0;
    //Spinner categorySpinner;
    String selectedCategory="",selectedCategoryId="";
    Button uploadImgBTN;
    String[] imagesList;
    TextView productCategory;
    ImageView backBTN,logoutBtn,editBtn,deleteBtn;
    ImageListAdapter imageListAdapter;
    ImageDetailsAdapter imageDetailsAdapter;
    public static ArrayList<Uri> imgList;
    RecyclerView imgRV,variantList;
    Context context;
    ArrayList<MultipartBody.Part> varItems = new ArrayList<>();
    ArrayList<MultipartBody.Part> varActive = new ArrayList<>();
    ArrayList<MultipartBody.Part> varDisAmt = new ArrayList<>();
    ArrayList<MultipartBody.Part> varActAmt = new ArrayList<>();
    JsonObject productid,updateProductDetails;
    String selectedProductId;
    public  Button nextBTN, cancelBTN, submitBTN;
    SwitchCompat displayToUserSwitch;
    SwitchCompat active30gm,active60gm,active100gm,active250gm,active500gm,active1kg,active2kg,active3kg,active5kg;
    public  AddProduct createProductActivity;
    RequestBody requestFile, pdt_id,pdt_name, category_id,pdt_discount_display, pdt_about, pdt_storage_uses, pdt_other_info, is_active, pdt_price_actual_500gm, pdt_price_discounted_500gm, pdt_price_enable_500gm,pdt_price_actual_30gm, pdt_price_discounted_30gm, pdt_price_enable_30gm,pdt_price_actual_60gm, pdt_price_discounted_60gm, pdt_price_enable_60gm,pdt_price_actual_100gm, pdt_price_discounted_100gm, pdt_price_enable_100gm,pdt_price_actual_250gm, pdt_price_discounted_250gm, pdt_price_enable_250gm,pdt_price_actual_1kg, pdt_price_discounted_1kg, pdt_price_enable_1kg,pdt_price_actual_2kg, pdt_price_discounted_2kg, pdt_price_enable_2kg,pdt_price_actual_3kg, pdt_price_discounted_3kg, pdt_price_enable_3kg,pdt_price_actual_5kg, pdt_price_discounted_5kg, pdt_price_enable_5kg, var_id;
    private static final int PERMISSIONS_WRITE_EXTERNAL_STORAGE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        context = AdminProductDetails.this;
        catList = new ArrayList<AdminCategory>();
      //  categorySpinner = (Spinner) findViewById(R.id.spinnerCategory);
      //  categorySpinner.setEnabled(false);
        productCategory=findViewById(R.id.productCategory);
        productNameET = findViewById(R.id.productNameET);
        productNameET.setEnabled(false);
        storageUsesET = findViewById(R.id.storageUsesET);
        storageUsesET.setEnabled(false);
        otherInfoET = findViewById(R.id.otherInfoET);
        otherInfoET.setEnabled(false);
        aboutProductET = findViewById(R.id.aboutProductET);
        aboutProductET.setEnabled(false);
        uploadImgBTN = findViewById(R.id.uploadImgBTN);
        uploadImgBTN.setVisibility(View.GONE);
        imgRV = findViewById(R.id.imgRV);
        nextBTN = findViewById(R.id.nextBTN);
        cancelBTN = findViewById(R.id.cancelBTN);
        backBTN = findViewById(R.id.backBTN);
        logoutBtn = findViewById(R.id.logoutBTN);
        editBtn = findViewById(R.id.editBtn);
        editBtn.setVisibility(View.VISIBLE);
        deleteBtn = findViewById(R.id.deleteBtn);
        deleteBtn.setVisibility(View.VISIBLE);
        price30gm = findViewById(R.id.price30gm);
        price30gm.setEnabled(false);
        discount30gm = findViewById(R.id.discount30gm);
        discount30gm.setEnabled(false);
        price60gm = findViewById(R.id.price60gm);
        price60gm.setEnabled(false);
        discount60gm = findViewById(R.id.discount60gm);
        discount60gm.setEnabled(false);
        price100gm = findViewById(R.id.price100gm);
        price100gm.setEnabled(false);
        discount100gm = findViewById(R.id.discount100gm);
        discount100gm.setEnabled(false);
        price250gm = findViewById(R.id.price250gm);
        price250gm.setEnabled(false);
        discount250gm = findViewById(R.id.discount250gm);
        discount250gm.setEnabled(false);
        price500gm = findViewById(R.id.price500gm);
        price500gm.setEnabled(false);
        discount500gm = findViewById(R.id.discount500gm);
        discount500gm.setEnabled(false);
        price1kg = findViewById(R.id.price1kg);
        price1kg.setEnabled(false);
        discount1kg = findViewById(R.id.discount1kg);
        discount1kg.setEnabled(false);
        price2kg = findViewById(R.id.price2kg);
        price2kg.setEnabled(false);
        discount2kg = findViewById(R.id.discount2kg);
        discount2kg.setEnabled(false);
        price3kg = findViewById(R.id.price3kg);
        price3kg.setEnabled(false);
        discount3kg = findViewById(R.id.discount3kg);
        discount3kg.setEnabled(false);
        price5kg = findViewById(R.id.price5kg);
        price5kg.setEnabled(false);
        discount5kg = findViewById(R.id.discount5kg);
        discount5kg.setEnabled(false);

        submitBTN = findViewById(R.id.submitBTN);
        submitBTN.setVisibility(View.GONE);
        displayToUserSwitch = findViewById(R.id.displayswitch);
        displayToUserSwitch.setEnabled(false);
        active30gm = findViewById(R.id.active30gm);
        active30gm.setEnabled(false);
        active60gm = findViewById(R.id.active60gm);
        active60gm.setEnabled(false);
        active100gm = findViewById(R.id.active100gm);
        active100gm.setEnabled(false);
        active250gm = findViewById(R.id.active250gm);
        active250gm.setEnabled(false);
        active500gm = findViewById(R.id.active500gm);
        active500gm.setEnabled(false);
        active1kg = findViewById(R.id.active1kg);
        active1kg.setEnabled(false);
        active2kg = findViewById(R.id.active2kg);
        active2kg.setEnabled(false);
        active3kg = findViewById(R.id.active3kg);
        active3kg.setEnabled(false);
        active5kg = findViewById(R.id.active5kg);
        active5kg.setEnabled(false);
        imgList = new ArrayList<>();
        selectedProductId=getIntent().getStringExtra("productid");
        productid = new JsonObject();
        productid.addProperty("id", selectedProductId);
        getProductDetails(productid);


        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //categorySpinner.setEnabled(true);
                productNameET.setEnabled(true);
                productNameET.setFocusable(true);
                storageUsesET.setEnabled(true);
                otherInfoET.setEnabled(true);
                aboutProductET.setEnabled(true);
                uploadImgBTN.setVisibility(View.VISIBLE);
                price30gm.setEnabled(true);
                discount30gm.setEnabled(true);
                price60gm.setEnabled(true);
                discount60gm.setEnabled(true);
                price100gm.setEnabled(true);
                discount100gm.setEnabled(true);
                price250gm.setEnabled(true);
                discount250gm.setEnabled(true);
                price500gm.setEnabled(true);
                discount500gm.setEnabled(true);
                price1kg.setEnabled(true);
                discount1kg.setEnabled(true);
                price2kg.setEnabled(true);
                discount2kg.setEnabled(true);
                price3kg.setEnabled(true);
                discount3kg.setEnabled(true);
                price5kg.setEnabled(true);
                discount5kg.setEnabled(true);
                submitBTN.setVisibility(View.VISIBLE);
                displayToUserSwitch.setEnabled(true);
                active30gm.setEnabled(true);
                active60gm.setEnabled(true);
                active100gm.setEnabled(true);
                active250gm.setEnabled(true);
                active500gm.setEnabled(true);
                active1kg.setEnabled(true);
                active2kg.setEnabled(true);
                active3kg.setEnabled(true);
                active5kg.setEnabled(true);
               editBtn.setVisibility(View.GONE);
               deleteBtn.setVisibility(View.GONE);
            }
        });


        //categorySpinner.setOnItemSelectedListener(this);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), Login.class));
            }
        });
        displayToUserSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    is_active= RequestBody.create(MediaType.parse("multipart/form-data"), "1");

                }
                else {
                    is_active= RequestBody.create(MediaType.parse("multipart/form-data"), "0");

                }
            }
        });

        /*imgRV.setLayoutManager(new LinearLayoutManager(context,  RecyclerView.HORIZONTAL, false));
        imageListAdapter = new ImageListAdapter(context, imgList, true);
        imgRV.setAdapter(imageListAdapter);
        */uploadImgBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imgList.clear();
                checkPermission();
                if (PermissionUtils.requestPermission(AdminProductDetails.this, 1, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    FishBun.with(AdminProductDetails.this)
                            .setImageAdapter(new GlideAdapter())
                            .setMaxCount(25)
                            .setSelectedImages(imgList)
                            .setMinCount(1)
                            .startAlbum();
                }
            }
        });
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    builder = new AlertDialog.Builder(context);
                    builder.setMessage("Are you sure?");
                    builder.setPositiveButton("Yes", dialogClickListener);
                    builder.setNegativeButton("No", dialogClickListener).show();
               }

            });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!productNameET.getText().toString().trim().isEmpty()) {
                    if (!aboutProductET.getText().toString().trim().isEmpty()) {
                        if (!storageUsesET.getText().toString().trim().isEmpty()) {
                            if (!otherInfoET.getText().toString().trim().isEmpty()) {
                                if(Utility.isOnline(v.getContext())) {
                                    String pname=productNameET.getText().toString().trim();
                                    pname =pname.replace(" ","_");
                                    pdt_id = RequestBody.create(MediaType.parse("multipart/form-data"), selectedProductId);
                                    pdt_name = RequestBody.create(MediaType.parse("multipart/form-data"), pname);
                                    category_id = RequestBody.create(MediaType.parse("multipart/form-data"), selectedCategoryId);
                                    pdt_discount_display = RequestBody.create(MediaType.parse("multipart/form-data"), "20");
                                    pdt_about = RequestBody.create(MediaType.parse("multipart/form-data"), aboutProductET.getText().toString().trim());
                                    pdt_storage_uses = RequestBody.create(MediaType.parse("multipart/form-data"), storageUsesET.getText().toString().trim());
                                    pdt_other_info = RequestBody.create(MediaType.parse("multipart/form-data"), otherInfoET.getText().toString().trim());
                                    var_id = RequestBody.create(MediaType.parse("multipart/form-data"), "1");
                                    int k = 0;


                                    if (active30gm.isChecked()) {
                                        varItems.add(k, MultipartBody.Part.createFormData("var_type[]", "30gms"));
                                        varActive.add(k, MultipartBody.Part.createFormData("var_is_active[]", "1"));
                                        varDisAmt.add(k, MultipartBody.Part.createFormData("var_discount_price[]", discount30gm.getText().toString().trim()));
                                        varActAmt.add(k, MultipartBody.Part.createFormData("var_actual_price[]", price30gm.getText().toString().trim()));

                                        k++;
                                    }
                                   /* else
                                    {

                                    }*/

                                    if (active60gm.isChecked()) {
                                        varItems.add(k, MultipartBody.Part.createFormData("var_type[]", "60gms"));
                                        varActive.add(k, MultipartBody.Part.createFormData("var_is_active[]", "1"));
                                        varDisAmt.add(k, MultipartBody.Part.createFormData("var_discount_price[]", discount60gm.getText().toString().trim()));
                                        varActAmt.add(k, MultipartBody.Part.createFormData("var_actual_price[]", price60gm.getText().toString().trim()));
                                        k++;
                                    }

                                    if (active100gm.isChecked()) {
                                        varItems.add(k, MultipartBody.Part.createFormData("var_type[]", "100gms"));
                                        varActive.add(k, MultipartBody.Part.createFormData("var_is_active[]", "1"));
                                        varDisAmt.add(k, MultipartBody.Part.createFormData("var_discount_price[]", discount100gm.getText().toString().trim()));
                                        varActAmt.add(k, MultipartBody.Part.createFormData("var_actual_price[]", price100gm.getText().toString().trim()));
                                        k++;
                                    }
                                    if (active250gm.isChecked()) {
                                        varItems.add(k, MultipartBody.Part.createFormData("var_type[]", "250gms"));
                                        varActive.add(k, MultipartBody.Part.createFormData("var_is_active[]", "1"));
                                        varDisAmt.add(k, MultipartBody.Part.createFormData("var_discount_price[]", discount250gm.getText().toString().trim()));
                                        varActAmt.add(k, MultipartBody.Part.createFormData("var_actual_price[]", price250gm.getText().toString().trim()));
                                        k++;
                                    }
                                    if (active500gm.isChecked()) {
                                        varItems.add(k, MultipartBody.Part.createFormData("var_type[]", "500gms"));
                                        varActive.add(k, MultipartBody.Part.createFormData("var_is_active[]", "1"));
                                        varDisAmt.add(k, MultipartBody.Part.createFormData("var_discount_price[]", discount500gm.getText().toString().trim()));
                                        varActAmt.add(k, MultipartBody.Part.createFormData("var_actual_price[]", price500gm.getText().toString().trim()));
                                        k++;
                                    }
                                    if (active1kg.isChecked()) {
                                        varItems.add(k, MultipartBody.Part.createFormData("var_type[]", "1kg"));
                                        varActive.add(k, MultipartBody.Part.createFormData("var_is_active[]", "1"));
                                        varDisAmt.add(k, MultipartBody.Part.createFormData("var_discount_price[]", discount1kg.getText().toString().trim()));
                                        varActAmt.add(k, MultipartBody.Part.createFormData("var_actual_price[]", price1kg.getText().toString().trim()));

                                        k++;
                                    }
                                    if (active2kg.isChecked()) {
                                        varItems.add(k, MultipartBody.Part.createFormData("var_type[]", "2kg"));
                                        varActive.add(k, MultipartBody.Part.createFormData("var_is_active[]", "1"));
                                        varDisAmt.add(k, MultipartBody.Part.createFormData("var_discount_price[]", discount2kg.getText().toString().trim()));
                                        varActAmt.add(k, MultipartBody.Part.createFormData("var_actual_price[]", price2kg.getText().toString().trim()));
                                        k++;
                                    }
                                    if (active3kg.isChecked()) {
                                        varItems.add(k, MultipartBody.Part.createFormData("var_type[]", "3kg"));
                                        varActive.add(k, MultipartBody.Part.createFormData("var_is_active[]", "1"));
                                        varDisAmt.add(k, MultipartBody.Part.createFormData("var_discount_price[]", discount3kg.getText().toString().trim()));
                                        varActAmt.add(k, MultipartBody.Part.createFormData("var_actual_price[]", price3kg.getText().toString().trim()));

                                        k++;
                                    }
                                    if (active5kg.isChecked()) {
                                        varItems.add(k, MultipartBody.Part.createFormData("var_type[]", "5kg"));
                                        varActive.add(k, MultipartBody.Part.createFormData("var_is_active[]", "1"));
                                        varDisAmt.add(k, MultipartBody.Part.createFormData("var_discount_price[]", discount5kg.getText().toString().trim()));
                                        varActAmt.add(k, MultipartBody.Part.createFormData("var_actual_price[]", price5kg.getText().toString().trim()));

                                        k++;
                                    }
                                    updateProduct();
                                }else
                                {
                                    Utility.noInternetError(v.getContext());

                                }
/*
                                if(imgList.size()>0) {
                                }
                                else
                                {
                                    Toast.makeText(v.getContext(),"Please select at least one Image",Toast.LENGTH_SHORT).show();
                                }
*/
                            }
                            else
                            {
                                otherInfoET.setError("Enter Other Info");
                            }
                        }
                        else
                        {
                            storageUsesET.setError("Enter Storage USes");
                        }
                    }
                    else
                    {
                        aboutProductET.setError("Enter About Product");

                    }
                }
                else
                {
                    productNameET.setError("Enter Product Name");

                }
            }
        });


        nextBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int showPrice, isRecommended, showDesignNo;


            }
        });

        cancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        backBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
       /* Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();*/
        if(pos>0) {

            selectedCategory=catList.get(pos).getCatName();
            selectedCategoryId = catList.get(pos).getCatid();
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }




    public void checkPermission()
    {

        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )  {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        } else {
            // Android version is lesser than 6.0 or the permission is already granted.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {

            }
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == PERMISSIONS_WRITE_EXTERNAL_STORAGE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                //  showContacts();

            } else {
                Toast.makeText(this, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Define.ALBUM_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ArrayList<Uri> list = data.getParcelableArrayListExtra(Define.INTENT_PATH);

                imgList = list;

                String path = getRealPathFromURI(list.get(0).normalizeScheme());
                imageListAdapter = new ImageListAdapter(context, imgList, true);
                imgRV.setAdapter(imageListAdapter);
                imageListAdapter.notifyDataSetChanged();

            }
        }
    }

    private ArrayList<MultipartBody.Part> prepareFilePart(String partName) {
        ArrayList<MultipartBody.Part> imageFile = new ArrayList<>();
        requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), "");
        imageFile.add(MultipartBody.Part.createFormData(partName,  "",requestFile));

        try {
            if (imgList != null) {
                for (int i = 0; i < imgList.size(); i++) {
                    File imgFile = new File(ImageFilePath.getPath(context, imgList.get(i)));
                    // File imgFile = new File(getRealPathFromURI(imgList.get(i)));
                    // Toast.makeText(AddProduct.this,"Prep File Obj : " +ImageFilePath.getPath(AddProduct.this, imgList.get(0)),Toast.LENGTH_LONG).show();
                    try {
                        float fileSizeKB = Float.parseFloat(String.valueOf(imgFile.length() / 1024));

                        if (fileSizeKB > 500) {
                            int quality = (int) (50000 / fileSizeKB);
                            imgFile = new Compressor(context).setQuality(quality).compressToFile(imgFile);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
                    imageFile.add(MultipartBody.Part.createFormData(partName, imgFile.getName(), requestFile));
                }
            }
        }
        catch(Exception e){Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();}
        return imageFile;
    }

    public void addItemsOnSpinner1() {
        getAllCategoryDetails();
    }



    private void getAllCategoryDetails() {
        final Dialog dialog = Utility.showProgress(context);
        Call<ResponseBody> get = Utility.retroInterface().getAllCategory();
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

                            // JSONObject objR = jsonElements.getJSONObject(0);
                            JSONArray jA = objRO.getJSONArray("result");
                            for (int i = 0; i < jA.length(); i++) {
                                JSONObject obj = jA.getJSONObject(i);
                                String name = obj.getString("category_name");
                                AdminCategory cat = new AdminCategory();
                                cat.setCatName(name);
                                cat.setCatId(obj.getString("category_id"));
                                catList.add(cat);

                            }
                            //SpinnerAdapter spinnerAdapter = new SpinnerAdapter(context,catList);
                            //categorySpinner.setAdapter(spinnerAdapter);


                        }catch(JSONException je){
                            Toast.makeText(context, "JSONEXCE : " + je.getMessage(), Toast.LENGTH_LONG).show();
                        }
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


    private void getProductDetails(JsonObject productId) {

        final Dialog dialog = Utility.showProgress(context);
        Call<ResponseBody> get = Utility.retroInterface().getProductDetail(productId);
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
                         //   JSONObject objRO = object.getJSONObject("data");
                            JSONArray jA = object.getJSONArray("data");
                            JSONObject objRO = jA.getJSONObject(0);
                            JSONObject objP = objRO.getJSONObject("product");
                            String id = objP.getString("pdt_id");
                            String pname = objP.getString("pdt_name");
                            pname =pname.replace("_"," ");
                            productNameET.setText(pname);
                            aboutProductET.setText(objP.getString("pdt_about"));
                            storageUsesET.setText(objP.getString("pdt_storage_uses"));
                            otherInfoET.setText(objP.getString("pdt_other_info"));

                             if(objP.getString("is_active").equals("1"))
                             displayToUserSwitch.setChecked(true);
                             else
                                 displayToUserSwitch.setChecked(false);
                            String catid =objP.getString("category_id");
                            selectedCategoryId = catid;
                            addItemsOnSpinner1();
                            for(int i=0;i<catList.size();i++) {
                             if(catid.equals(catList.get(i).getCatid()))
                                productCategory.setText(catList.get(i).getCatName());
                            }
                            String images = objP.getString("prdt_images");
                            if ((images.contains("https")||images.contains("http"))) {
                                 imagesList = images.split(",");
                            //     for(int i =0 ;i<imagesList.length;i++)
                                // imgList.add(Uri.parse(imagesList[i]));
                                imgRV.setLayoutManager(new LinearLayoutManager(context,  RecyclerView.HORIZONTAL, false));
                                imageDetailsAdapter = new ImageDetailsAdapter(context, imagesList, true);
                                imgRV.setAdapter(imageDetailsAdapter);
                            }
                            JSONArray jVar = objRO.getJSONArray("varient");
                            for(int j=0;j<jVar.length();j++)
                            {
                                JSONObject objVar = jVar.getJSONObject(j);
                                switch(objVar.getString("var_type"))
                                {
                                    case "30gms" :
                                {
                                    price30gm.setText(objVar.getString("var_actual_price"));
                                    discount30gm.setText(objVar.getString("var_discount_price"));
                                    if(objVar.getString("is_active").equals("1"))
                                    {
                                        active30gm.setChecked(true);
                                    }
                                    else
                                    {
                                        active30gm.setChecked(false);
                                    }

                                }
                                break;
                                    case "60gms" :
                                    {
                                        price60gm.setText(objVar.getString("var_actual_price"));
                                        discount60gm.setText(objVar.getString("var_discount_price"));
                                        if(objVar.getString("is_active").equals("1"))
                                        {
                                            active60gm.setChecked(true);
                                        }
                                        else
                                        {
                                            active60gm.setChecked(false);
                                        }

                                    }
                                    break;
                                    case "100gms" :
                                    {
                                        price100gm.setText(objVar.getString("var_actual_price"));
                                        discount100gm.setText(objVar.getString("var_discount_price"));
                                        if(objVar.getString("is_active").equals("1"))
                                        {
                                            active100gm.setChecked(true);
                                        }
                                        else
                                        {
                                            active100gm.setChecked(false);
                                        }

                                    }
                                    break;
                                    case "250gms" :
                                    {
                                        price250gm.setText(objVar.getString("var_actual_price"));
                                        discount250gm.setText(objVar.getString("var_discount_price"));
                                        if(objVar.getString("is_active").equals("1"))
                                        {
                                            active250gm.setChecked(true);
                                        }
                                        else
                                        {
                                            active250gm.setChecked(false);
                                        }

                                    }
                                    break;
                                    case "500gms" :
                                    {
                                        price500gm.setText(objVar.getString("var_actual_price"));
                                        discount500gm.setText(objVar.getString("var_discount_price"));
                                        if(objVar.getString("is_active").equals("1"))
                                        {
                                            active500gm.setChecked(true);
                                        }
                                        else
                                        {
                                            active500gm.setChecked(false);
                                        }

                                    }
                                    break;

                                    case "1kg" :
                                    {
                                        price1kg.setText(objVar.getString("var_actual_price"));
                                        discount1kg.setText(objVar.getString("var_discount_price"));
                                        if(objVar.getString("is_active").equals("1"))
                                        {
                                            active1kg.setChecked(true);
                                        }
                                        else
                                        {
                                            active1kg.setChecked(false);
                                        }

                                    }
                                    break;
                                    case "2kg" :
                                    {
                                        price2kg.setText(objVar.getString("var_actual_price"));
                                        discount2kg.setText(objVar.getString("var_discount_price"));
                                        if(objVar.getString("is_active").equals("1"))
                                        {
                                            active2kg.setChecked(true);
                                        }
                                        else
                                        {
                                            active2kg.setChecked(false);
                                        }

                                    }
                                    break;

                                    case "3kg" :
                                    {
                                        price3kg.setText(objVar.getString("var_actual_price"));
                                        discount3kg.setText(objVar.getString("var_discount_price"));
                                        if(objVar.getString("is_active").equals("1"))
                                        {
                                            active3kg.setChecked(true);
                                        }
                                        else
                                        {
                                            active3kg.setChecked(false);
                                        }

                                    }
                                    break;
                                    case "5kg" :
                                    {
                                        price5kg.setText(objVar.getString("var_actual_price"));
                                        discount5kg.setText(objVar.getString("var_discount_price"));
                                        if(objVar.getString("is_active").equals("1"))
                                        {
                                            active5kg.setChecked(true);
                                        }
                                        else
                                        {
                                            active5kg.setChecked(false);
                                        }

                                    }
                                    break;


                                }
                            }

                        }catch(JSONException je){
                            Toast.makeText(context, "JSONEXCE : " + je.getMessage(), Toast.LENGTH_LONG).show();
                        }
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



    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        return result;
    }



    private void updateProduct() {
        final Dialog dialog = Utility.showProgress(context);
        ArrayList<MultipartBody.Part> images = prepareFilePart("image[]");
            Call<BaseResponse> updateProductDetail = Utility.retroInterface().updateProductDetail(images,pdt_id,pdt_name,category_id,pdt_discount_display,pdt_about,pdt_storage_uses,pdt_other_info,is_active,var_id,varItems,varActive,varDisAmt,varActAmt);

        updateProductDetail.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utility.dismissDialog(dialog);
                try {
                   // if (response.code() == 200) {
                        if(response.isSuccessful()){

                     //       if (response.body().getStatus().equalsIgnoreCase("success")) {
                            Toast.makeText(context,"Product Updated Successfully.", Toast.LENGTH_SHORT).show();
                            Intent i=new Intent(context, ProductList.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else {
                            Toast.makeText(context, response.message().toString(), Toast.LENGTH_SHORT).show();
                        }

                } catch (Exception e) {
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();

                    Utility.somethingWentWrong(context);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utility.dismissDialog(dialog);
               Toast.makeText(context,"exception here"+t.getMessage(),Toast.LENGTH_LONG).show();

                Utility.somethingWentWrong(context);
            }
        });


    }
    private void deleteProduct(JsonObject productIdForDelete) {
        final Dialog dialog = Utility.showProgress(context);
        //  ArrayList<MultipartBody.Part> images = prepareFilePart("image[]");
        Call<ResponseBody> updateProductDetail = Utility.retroInterface().deleteProductDetail(productIdForDelete);
        updateProductDetail.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, " Product Deleted", Toast.LENGTH_SHORT).show();

                        Intent i=new Intent(context, ProductList.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
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


    @Override
    public void onBackPressed() {

        super.onBackPressed();
        // implement your override logic here
        return;
    }


    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    deleteProduct(productid);

                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    dialog.dismiss();
                    break;
            }
        }
    };

}
