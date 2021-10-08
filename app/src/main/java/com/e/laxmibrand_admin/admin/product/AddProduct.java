package com.e.laxmibrand_admin.admin.product;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.admin.Login;
import com.e.laxmibrand_admin.beans.AdminCategory;
import com.e.laxmibrand_admin.beans.BaseResponse;
import com.e.laxmibrand_admin.utils.ImageFilePath;
import com.e.laxmibrand_admin.utils.PermissionUtils;
import com.e.laxmibrand_admin.utils.Utility;
import com.google.android.material.textfield.TextInputEditText;
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

public class AddProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextInputEditText productNameET,aboutProductET,storageUsesET,otherInfoET;
    EditText price30gm,discount30gm,price60gm,discount60gm,price100gm,discount100gm,price250gm,discount250gm,price500gm,discount500gm,price1kg,discount1kg,price2kg,discount2kg,price3kg,discount3kg,price5kg,discount5kg;
    ArrayList<AdminCategory> catList;
    Spinner categorySpinner;
    String selectedCategory="",selectedCategoryId="";
    Button uploadImgBTN;
    ImageView backBTN,logoutBtn;
    ImageListAdapter imageListAdapter;
    public static ArrayList<Uri> imgList;
    RecyclerView imgRV,variantList;
    Context context;
    static ArrayList<String> newCList;

    static String[] cList;
    RequestBody rb,rba,rbd;
    ArrayList<MultipartBody.Part> varItems = new ArrayList<>();
    ArrayList<MultipartBody.Part> varActive = new ArrayList<>();
    ArrayList<MultipartBody.Part> varDisAmt = new ArrayList<>();
    ArrayList<MultipartBody.Part> varActAmt = new ArrayList<>();
    static int noOfPages;

    public  Button nextBTN, cancelBTN, submitBTN;
    SwitchCompat displayToUserSwitch;
    SwitchCompat active30gm,active60gm,active100gm,active250gm,active500gm,active1kg,active2kg,active3kg,active5kg;
    public  AddProduct createProductActivity;
    RequestBody requestFile, pdt_name, category_id,pdt_discount_display, pdt_about, pdt_storage_uses, pdt_other_info, is_active, pdt_price_actual_500gm, pdt_price_discounted_500gm, pdt_price_enable_500gm,pdt_price_actual_30gm, pdt_price_discounted_30gm, pdt_price_enable_30gm,pdt_price_actual_60gm, pdt_price_discounted_60gm, pdt_price_enable_60gm,pdt_price_actual_100gm, pdt_price_discounted_100gm, pdt_price_enable_100gm,pdt_price_actual_250gm, pdt_price_discounted_250gm, pdt_price_enable_250gm,pdt_price_actual_1kg, pdt_price_discounted_1kg, pdt_price_enable_1kg,pdt_price_actual_2kg, pdt_price_discounted_2kg, pdt_price_enable_2kg,pdt_price_actual_3kg, pdt_price_discounted_3kg, pdt_price_enable_3kg,pdt_price_actual_5kg, pdt_price_discounted_5kg, pdt_price_enable_5kg, var_id;
    private static final int PERMISSIONS_WRITE_EXTERNAL_STORAGE = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        createProductActivity = this;
        context = AddProduct.this;
        catList = new ArrayList<AdminCategory>();
        newCList = new ArrayList<String>();

        categorySpinner = (Spinner) findViewById(R.id.spinnerCategory);
        productNameET = findViewById(R.id.productNameET);
        storageUsesET = findViewById(R.id.storageUsesET);
        otherInfoET = findViewById(R.id.otherInfoET);

        aboutProductET = findViewById(R.id.aboutProductET);
        uploadImgBTN = findViewById(R.id.uploadImgBTN);
        imgRV = findViewById(R.id.imgRV);
        nextBTN = findViewById(R.id.nextBTN);
        cancelBTN = findViewById(R.id.cancelBTN);
        backBTN = findViewById(R.id.backBTN);
        logoutBtn = findViewById(R.id.logoutBTN);


        price30gm = findViewById(R.id.price30gm);
        discount30gm = findViewById(R.id.discount30gm);
        price60gm = findViewById(R.id.price60gm);
        discount60gm = findViewById(R.id.discount60gm);
        price100gm = findViewById(R.id.price100gm);
        discount100gm = findViewById(R.id.discount100gm);
        price250gm = findViewById(R.id.price250gm);
        discount250gm = findViewById(R.id.discount250gm);
        price500gm = findViewById(R.id.price500gm);
        discount500gm = findViewById(R.id.discount500gm);
        price1kg = findViewById(R.id.price1kg);
        discount1kg = findViewById(R.id.discount1kg);
        price2kg = findViewById(R.id.price2kg);
        discount2kg = findViewById(R.id.discount2kg);
        price3kg = findViewById(R.id.price3kg);
        discount3kg = findViewById(R.id.discount3kg);
        price5kg = findViewById(R.id.price5kg);
        discount5kg = findViewById(R.id.discount5kg);


        submitBTN = findViewById(R.id.submitBTN);
        displayToUserSwitch = findViewById(R.id.displayswitch);
      displayToUserSwitch.setChecked(false);
        //  is_active= RequestBody.create(MediaType.parse("multipart/form-data"), "1");

        active30gm = findViewById(R.id.active30gm);
        active60gm = findViewById(R.id.active60gm);
        active100gm = findViewById(R.id.active100gm);
        active250gm = findViewById(R.id.active250gm);
        active500gm = findViewById(R.id.active500gm);
        active1kg = findViewById(R.id.active1kg);
        active2kg = findViewById(R.id.active2kg);
        active3kg = findViewById(R.id.active3kg);
        active5kg = findViewById(R.id.active5kg);

    //    is_active= RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        pdt_price_enable_30gm= RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        pdt_price_enable_60gm= RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        pdt_price_enable_100gm= RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        pdt_price_enable_250gm= RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        pdt_price_enable_500gm= RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        pdt_price_enable_1kg= RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        pdt_price_enable_2kg= RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        pdt_price_enable_3kg= RequestBody.create(MediaType.parse("multipart/form-data"), "0");
        pdt_price_enable_5kg= RequestBody.create(MediaType.parse("multipart/form-data"), "0");


  /*      price30gm.setEnabled(false);
        price60gm.setEnabled(false);
        price100gm.setEnabled(false);
        price250gm.setEnabled(false);
        price500gm.setEnabled(false);
        price1kg.setEnabled(false);
        price2kg.setEnabled(false);
        price3kg.setEnabled(false);
        price5kg.setEnabled(false);*/

        imgList = new ArrayList<>();

        noOfPages = 1;
        addItemsOnSpinner1();

        categorySpinner.setOnItemSelectedListener(this);

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

        active30gm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                }
                else {
                }

            }
        });

        active60gm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                }
                else {
                }

            }
        });

        active100gm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                }
                else {

                }

            }
        });

        active250gm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                }
                else {

                }

            }
        });

        active500gm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                }
                else {

                }

            }
        });

        active1kg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                }
                else {

                }

            }
        });


        active2kg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                }
                else {

                }

            }
        });

        active3kg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                }
                else {

                }
            }
        });


        active5kg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                }
                else {

                }

            }
        });

        imgRV.setLayoutManager(new GridLayoutManager(AddProduct.this, 3, RecyclerView.VERTICAL, false));
        imageListAdapter = new ImageListAdapter(AddProduct.this, imgList, true);
        imgRV.setAdapter(imageListAdapter);
        uploadImgBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                if (PermissionUtils.requestPermission(AddProduct.this, 1, Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    FishBun.with(AddProduct.this)
                            .setImageAdapter(new GlideAdapter())
                            .setMaxCount(25)
                            .setSelectedImages(imgList)
                            .setMinCount(1)
                            .startAlbum();
                }
            }
        });



        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!productNameET.getText().toString().trim().isEmpty()) {
                    if (!aboutProductET.getText().toString().trim().isEmpty()) {
                        if (!storageUsesET.getText().toString().trim().isEmpty()) {
                            if (!otherInfoET.getText().toString().trim().isEmpty()) {
                                if(imgList.size()>0) {
                                    if(Utility.isOnline(v.getContext())) {
                                        String pname=productNameET.getText().toString().trim();
                                        pname =pname.replace(" ","_");

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


                                    /*    pdt_price_actual_30gm = RequestBody.create(MediaType.parse("multipart/form-data"), price30gm.getText().toString().trim());
                                        pdt_price_discounted_30gm = RequestBody.create(MediaType.parse("multipart/form-data"), discount30gm.getText().toString().trim());
*/
                     /*                   pdt_price_actual_60gm = RequestBody.create(MediaType.parse("multipart/form-data"), price60gm.getText().toString().trim());
                                        pdt_price_discounted_60gm = RequestBody.create(MediaType.parse("multipart/form-data"), discount60gm.getText().toString().trim());

                                            pdt_price_actual_100gm = RequestBody.create(MediaType.parse("multipart/form-data"), price100gm.getText().toString().trim());
                                            pdt_price_discounted_100gm = RequestBody.create(MediaType.parse("multipart/form-data"), discount100gm.getText().toString().trim());

                                        pdt_price_actual_250gm = RequestBody.create(MediaType.parse("multipart/form-data"), price250gm.getText().toString().trim());
                                        pdt_price_discounted_250gm = RequestBody.create(MediaType.parse("multipart/form-data"), discount250gm.getText().toString().trim());

                                            pdt_price_actual_500gm = RequestBody.create(MediaType.parse("multipart/form-data"), price500gm.getText().toString().trim());
                                            pdt_price_discounted_500gm = RequestBody.create(MediaType.parse("multipart/form-data"), discount500gm.getText().toString().trim());

                                            pdt_price_actual_1kg = RequestBody.create(MediaType.parse("multipart/form-data"), price1kg.getText().toString().trim());
                                            pdt_price_discounted_1kg = RequestBody.create(MediaType.parse("multipart/form-data"), discount1kg.getText().toString().trim());

                                            pdt_price_actual_2kg = RequestBody.create(MediaType.parse("multipart/form-data"), price2kg.getText().toString().trim());
                                            pdt_price_discounted_2kg = RequestBody.create(MediaType.parse("multipart/form-data"), discount2kg.getText().toString().trim());

                                            pdt_price_actual_3kg = RequestBody.create(MediaType.parse("multipart/form-data"), price3kg.getText().toString().trim());
                                            pdt_price_discounted_3kg = RequestBody.create(MediaType.parse("multipart/form-data"), discount3kg.getText().toString().trim());

                                            pdt_price_actual_5kg = RequestBody.create(MediaType.parse("multipart/form-data"), price5kg.getText().toString().trim());
                                            pdt_price_discounted_5kg = RequestBody.create(MediaType.parse("multipart/form-data"), discount5kg.getText().toString().trim());
*/

                                        addNewProduct();
                                    }else
                                    {
                                        Utility.noInternetError(v.getContext());

                                    }
                                }
                                else
                                {
                                    Toast.makeText(v.getContext(),"Please select at least one Image",Toast.LENGTH_SHORT).show();
                                }
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

             /*   if (priceSwitch.isChecked()) {
                    showPrice = 1;
                } else {
                    showPrice = 0;
                }

                if (recommendedCB.isChecked()) {
                    isRecommended = 1;
                } else {
                    isRecommended = 0;
                }

                if (designNoSwitch.isChecked()) {
                    showDesignNo = 1;
                } else {
                    showDesignNo = 0;
                }

                if (!catalogueNameET.getText().toString().trim().isEmpty()) {
                    if (!materialET.getText().toString().trim().isEmpty()) {
                        if (!workET.getText().toString().trim().isEmpty()) {
                            if (!occasionET.getText().toString().trim().isEmpty()) {
                                if (showPrice == 0 || !priceET.getText().toString().trim().isEmpty()) {

                                    String price;
                                    if (priceET.getText().toString().trim().isEmpty()) {
                                        price = "0";
                                    } else {
                                        price = priceET.getText().toString().trim();
                                    }

                                    CreateCatalogueModel createCatalogueModel = new CreateCatalogueModel(catalogueNameET.getText().toString().trim(),
                                            materialET.getText().toString().trim(), workET.getText().toString().trim(),
                                            occasionET.getText().toString().trim(), price, showPrice, isRecommended,
                                            showDesignNo,Integer.parseInt(pcsPerSetET.getText().toString()));

                                    if (single_design == 0) {
                                        startActivity(new Intent(AdminCreateCatalogueActivity.this, ImageDetailsSaveActivity.class)
                                                .putExtra("single_design", single_design)
                                                .putExtra("discount", 0)
                                                .putExtra("catalogueDetails", createCatalogueModel)
                                                .putExtra("imgList", imgList));
                                    } else {
                                        showDiscountDialog(catalogueNameET.getText().toString().trim(), createCatalogueModel, false);
                                    }
                                } else {
                                    Toast.makeText(AdminCreateCatalogueActivity.this, "Please enter price.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(AdminCreateCatalogueActivity.this, "Please enter occasion.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(AdminCreateCatalogueActivity.this, "Please enter work.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AdminCreateCatalogueActivity.this, "Please enter material.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AdminCreateCatalogueActivity.this, "Please enter catalogue name.", Toast.LENGTH_SHORT).show();
                }

//                startActivity(new Intent(CreateCatalogueActivity.this, ImageDetailsSaveActivity.class)
////                        .putExtra("catalogueDetails", createCatalogueModel)
//                        .putExtra("imgList", imgList));
*/
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



    /*   private void showDiscountDialog(String name, final CreateCatalogueModel model, final boolean isSubmit) {

        final Dialog dialog = new Dialog(AdminCreateCatalogueActivity.this);
        dialog.setContentView(R.layout.add_discount_dialog);
        dialog.setCancelable(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.AlertDialogTheme;
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.transparent);

        Button submitBTN = dialog.findViewById(R.id.submitBTN);
        final TextInputEditText nameET = dialog.findViewById(R.id.nameET),
                discountET = dialog.findViewById(R.id.discountET);

        nameET.setText(name);
        nameET.setEnabled(false);

        discountET.setText("0");

        discountET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (discountET.getText().toString().trim().length() > 0) {
                    if (Long.parseLong(discountET.getText().toString()) > 100) {
                        String text = discountET.getText().toString();
                        discountET.setText(text.substring(0, text.length() - 1));
                        discountET.setSelection(discountET.getText().length());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        submitBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!discountET.getText().toString().trim().isEmpty()) {
                    dialog.dismiss();
                    if (isSubmit) {
                        if (Utility.isOnline(createCatalogueActivity)) {
                            createCatalogueAPI(model.getCatalogueName(), model.getMaterial(),
                                    model.getOccasion(), model.getWork(), model.getShowPrice(),
                                    model.getPrice(), model.getIsRecommended(), model.getIsShowDesignNo(),
                                    Integer.parseInt(discountET.getText().toString().trim()),Integer.parseInt(pcsPerSetET.getText().toString().trim()));
                        } else {
                            Utility.noInternetError(createCatalogueActivity);
                        }
                    } else {
                        startActivity(new Intent(AdminCreateCatalogueActivity.this, ImageDetailsSaveActivity.class)
                                .putExtra("single_design", single_design)
                                .putExtra("discount", Integer.parseInt(discountET.getText().toString().trim()))
                                .putExtra("catalogueDetails", model)
                                .putExtra("imgList", imgList));
                    }
                } else {
                    Toast.makeText(AdminCreateCatalogueActivity.this, "Discount field can't be blank.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();

    }

 */
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
                imageListAdapter = new ImageListAdapter(AddProduct.this, imgList, true);
                imgRV.setAdapter(imageListAdapter);
                imageListAdapter.notifyDataSetChanged();

            }
        }
    }

   private ArrayList<MultipartBody.Part> prepareFilePart(String partName) {

       ArrayList<MultipartBody.Part> imageFile = new ArrayList<>();
     if (imgList != null) {
            for (int i = 0; i < imgList.size(); i++) {
                File imgFile = new File(ImageFilePath.getPath(AddProduct.this, imgList.get(i)));
                try {
                    float fileSizeKB = Float.parseFloat(String.valueOf(imgFile.length() / 1024));

                    if (fileSizeKB > 500) {
                        int quality = (int) (5000 / fileSizeKB);
                        imgFile = new Compressor(AddProduct.this).setQuality(quality).compressToFile(imgFile);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                 requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
                imageFile.add(MultipartBody.Part.createFormData(partName, imgFile.getName(), requestFile));
            }
        }
     return imageFile;
    }




    public void addItemsOnSpinner1() {
        getAllCategoryDetails(noOfPages);
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
                            cList = new String[jA.length()];
                            for (int i = 0; i < jA.length(); i++) {
                                JSONObject obj = jA.getJSONObject(i);
                                String name = obj.getString("category_name");
                                AdminCategory cat = new AdminCategory();
                                cat.setCatName(name);
                                cat.setCatId(obj.getString("category_id"));
                                catList.add(cat);
                                cList[i]=name;
                                newCList.add(name);
                            }
                                selectedCategoryId=catList.get(0).getCatid();
                            if(noOfPages2>1 && noOfPages<noOfPages2) {
                                noOfPages = noOfPages + 1;
                                getAllCategoryDetails(noOfPages);
                                Log.i("noOfPages : ", "" + noOfPages2);
                            }
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_spinner_item, newCList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            categorySpinner.setAdapter(dataAdapter);

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


    private void addNewProduct() {
        final Dialog dialog = Utility.showProgress(context);
        ArrayList<MultipartBody.Part> images = prepareFilePart("image[]");
       // getVariantList();
        Call<BaseResponse> addCategory = Utility.retroInterface().addNewProduct(images,pdt_name,category_id,pdt_discount_display,pdt_about,pdt_storage_uses,pdt_other_info,is_active,var_id,varItems,varActive,varDisAmt,varActAmt);//,pdt_price_actual_30gm,pdt_price_discounted_30gm,pdt_price_enable_30gm,pdt_price_actual_60gm,pdt_price_discounted_60gm,pdt_price_enable_60gm,pdt_price_actual_100gm,pdt_price_discounted_100gm,pdt_price_enable_100gm,pdt_price_actual_250gm,pdt_price_discounted_250gm,pdt_price_enable_250gm,pdt_price_actual_1kg,pdt_price_discounted_1kg,pdt_price_enable_1kg,pdt_price_actual_2kg,pdt_price_discounted_2kg,pdt_price_enable_2kg,pdt_price_actual_3kg,pdt_price_discounted_3kg,pdt_price_enable_3kg,pdt_price_actual_5kg,pdt_price_discounted_5kg,pdt_price_enable_5kg);
              addCategory.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utility.dismissDialog(dialog);
                try {
                    //if (response.code() == 200) {
                        if(response.isSuccessful()){
                        //if (response.body().toString().getStatus().equalsIgnoreCase("success")) {
                           // Toast.makeText(context, response.body().toString(), Toast.LENGTH_SHORT).show();
                          //  Toast.makeText(context, "Product ADDED - "+response.code(), Toast.LENGTH_LONG).show();
                            Intent i=new Intent(context, ProductList.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                        } else {
                            Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                        }
                    /*} else {
                        Toast.makeText(context, "othtn200 Could Not Insert New Product:"+response.code(), Toast.LENGTH_SHORT).show();

//                        Toast.makeText(context, "Wrong Email or Password.", Toast.LENGTH_SHORT).show();

                    }*/
                } catch (Exception e) {
                    Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();

                   // Utility.somethingWentWrong(context);
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




    private void uploadImageFile(Uri fileUri, String desc) {

        //creating a file
        File file = new File(getRealPathFromURI(fileUri));

        //creating request body for file
         requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);
        RequestBody descBody = RequestBody.create(MediaType.parse("text/plain"), desc);

        //The gson builder
       /* Gson gson = new GsonBuilder()
                .setLenient()
                .create();*/

/*
        //creating retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        //creating our api
        Api api = retrofit.create(Api.class);

        //creating a call and calling the upload image method
        Call<MyResponse> call = api.uploadImage(requestFile, descBody);

        //finally performing the call
        call.enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (!response.body().error) {
                    Toast.makeText(getApplicationContext(), "File Uploaded Successfully...", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Some error occurred...", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });*/
    }
    @Override
    public void onBackPressed() {

        super.onBackPressed();
        // implement your override logic here
        return;
    }
}