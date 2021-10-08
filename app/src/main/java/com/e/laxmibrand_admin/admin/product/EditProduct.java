package com.e.laxmibrand_admin.admin.product;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.utils.PermissionUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;
import com.sangcomz.fishbun.define.Define;

import java.util.ArrayList;


    public class EditProduct extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

        TextInputEditText productNameET, variantET,  priceET,discountPrice;
        CheckBox discountCB;
        VariantListAdapter variantListAdapter;
        TextInputLayout priceTIL;
        String selectedCategory="";
        Button uploadImgBTN;
        ImageView backBTN;
        ImageListAdapter imageListAdapter;
        public static ArrayList<Uri> imgList;
        RecyclerView imgRV,variantList;
        ArrayList<String> list;
        ArrayList<String> vList;
        public  Button nextBTN, cancelBTN, submitBTN;
        Switch displayToUserSwitch;
        int createCatalogueID, single_design;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.edit_product);
            single_design = getIntent().getIntExtra("single_design", 0);
            productNameET = findViewById(R.id.productNameET);
            variantET = findViewById(R.id.variant);
            priceET = findViewById(R.id.priceET);
            uploadImgBTN = findViewById(R.id.uploadImgBTN);
            imgRV = findViewById(R.id.imgRV);
            nextBTN = findViewById(R.id.nextBTN);
            cancelBTN = findViewById(R.id.cancelBTN);
            backBTN = findViewById(R.id.backBTN);
            submitBTN = findViewById(R.id.submitBTN);
            displayToUserSwitch = findViewById(R.id.displayswitch);




            displayToUserSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
/*
                if (imgList.size() > 0) {
                    if (isChecked) {
                        submitBTN.setVisibility(View.GONE);
                        nextBTN.setVisibility(View.VISIBLE);
                    } else {
                        submitBTN.setVisibility(View.VISIBLE);
                        nextBTN.setVisibility(View.GONE);
                    }
                } else {
                    submitBTN.setVisibility(View.GONE);
                    nextBTN.setVisibility(View.GONE);
                }*/
                }

            });

            imgRV.setLayoutManager(new GridLayoutManager(EditProduct.this, 3, RecyclerView.VERTICAL, false));
            imgList = new ArrayList<>();
            imageListAdapter = new ImageListAdapter(EditProduct.this, imgList, true);
            imgRV.setAdapter(imageListAdapter);
            uploadImgBTN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (PermissionUtils.requestPermission(EditProduct.this, 1, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        FishBun.with(EditProduct.this)
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

                    int showPrice, isRecommended, showDesignNo;

              /*  if (priceSwitch.isChecked()) {
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
                                if (showPrice == 0 ||
                                        (!priceET.getText().toString().trim().isEmpty() && Integer.parseInt(priceET.getText().toString().trim()) > 0)) {

                                    String price;
                                    if (priceET.getText().toString().trim().isEmpty()) {
                                        price = "0";
                                    } else {
                                        price = priceET.getText().toString().trim();
                                    }

                                    CreateCatalogueModel model = new CreateCatalogueModel(catalogueNameET.getText().toString().trim(),
                                            materialET.getText().toString().trim(), workET.getText().toString().trim(),
                                            occasionET.getText().toString().trim(), price, showPrice, isRecommended,
                                            showDesignNo,Integer.parseInt(pcsPerSetET.getText().toString()));

                                    if (single_design == 0) {
                                        if (Utility.isOnline(createCatalogueActivity)) {
                                            createCatalogueAPI(model.getCatalogueName(), model.getMaterial(),
                                                    model.getOccasion(), model.getWork(), model.getShowPrice(),
                                                    model.getPrice(), model.getIsRecommended(), model.getIsShowDesignNo(), 0,model.getNoofsets());
                                        } else {
                                            Utility.noInternetError(createCatalogueActivity);
                                        }
                                    } else {
                                        showDiscountDialog(catalogueNameET.getText().toString().trim(), model, true);
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
                }*/
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
                    finish();
                }
            });
        }


        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
       /* Toast.makeText(parent.getContext(),
                "OnItemSelectedListener : " + parent.getItemAtPosition(pos).toString(),
                Toast.LENGTH_SHORT).show();*/
            if(pos>0) {

                selectedCategory=list.get(pos);
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

     */   @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == Define.ALBUM_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    ArrayList<Uri> list = data.getParcelableArrayListExtra(Define.INTENT_PATH);
                    imgList = list;
                    imageListAdapter = new ImageListAdapter(EditProduct.this, imgList, true);
                    imgRV.setAdapter(imageListAdapter);
                    imageListAdapter.notifyDataSetChanged();
             /*   if (imgList.size() >= 5) {
                    if (displayToUserSwitch.isChecked()) {
                        submitBTN.setVisibility(View.GONE);
                        nextBTN.setVisibility(View.VISIBLE);
                    } else {
                        submitBTN.setVisibility(View.VISIBLE);
                        nextBTN.setVisibility(View.GONE);
                    }
                } else {
                    submitBTN.setVisibility(View.GONE);
                    nextBTN.setVisibility(View.GONE);
                }*/
                    // you can get an image path(ArrayList<Uri>) on 0.6.2 and later
                }
            }
        }

 /*   private void createCatalogueAPI(String catalogueName, String material, String occasion, String work, int showPrice,
                                    String price, int isRecommended, int isRequiredDesignNo, int discount,int noofset) {

        final Dialog dialog = Utility.showProgress(createCatalogueActivity);
        int uid = Utility.mPreferenceSettings().getUserId();
        String authkey = Utility.mPreferenceSettings().getFirebaseToken();
        Call<BaseResponse> create = Utility.retroInterface().upsertCatalogue(0,
                catalogueName, material, occasion, work, "", showPrice, price, isRecommended,
                isRequiredDesignNo, 0, "", 0, single_design,discount,noofset, uid, authkey);
        create.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {

                try {
                    if (response.code() == 200) {
                        if (response.body().getResponseCode().equalsIgnoreCase("SUCCESS")) {
                            createCatalogueID = response.body().getResponseID();
                            ArrayList<UpsertCatalogueDesignRequest> upsertDesignList = new ArrayList<>();
                            for (int i = 0; i < imgList.size(); i++) {
                                UpsertCatalogueDesignRequest upsertCatalogueDesignRequest =
                                        new UpsertCatalogueDesignRequest(createCatalogueID, "", 1);
                                upsertDesignList.add(upsertCatalogueDesignRequest);
                            }

//                        Gson gson = new Gson();
//                        String designListJSON = gson.toJson(upsertDesignList);
//                        upsertCatalogueDesignAPI(dialog, designListJSON);

                            uploadFiles(upsertDesignList);
                        } else {
                            Utility.dismissDialog(dialog);
                            if (response.body().getResponseMessage().equals("Authentication Key is wrong.")) {
                                Utility.showUnauthorisedDialog(AdminCreateCatalogueActivity.this);
                            } else {
                                Toast.makeText(AdminCreateCatalogueActivity.this, response.body().getResponseMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        Utility.somethingWentWrong(createCatalogueActivity);
                        Utility.dismissDialog(dialog);
                    }
                } catch (Exception e) {
                    Utility.somethingWentWrong(AdminCreateCatalogueActivity.this);
                    Utility.dismissDialog(dialog);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utility.dismissDialog(dialog);
                Utility.somethingWentWrong(createCatalogueActivity);
            }
        });

    }
*/
   /* public void uploadFiles(ArrayList<UpsertCatalogueDesignRequest> upsertDesignList) {

        pDialog = new ProgressDialog(this);
        showProgress("Uploading media ...");
        FileUploader fileUploader = new FileUploader();
        fileUploader.uploadFiles(createCatalogueActivity, imgList, upsertDesignList, new FileUploader.FileUploaderCallback() {
            @Override
            public void onError() {
                hideProgress();
            }

            @Override
            public void onFinish(String[] responses) {
                hideProgress();
                Toast.makeText(AdminCreateCatalogueActivity.this, "Catalogue created successfully.", Toast.LENGTH_SHORT).show();
                if (single_design == 0) {
                    AdminCatalogueViewFragment.createCatalogue = true;
                } else {
                    AdminSingleDesignCatalogueViewFragment.createCatalogue = true;
                }
                finish();
            }

            @Override
            public void onProgressUpdate(int currentpercent, int totalpercent, int filenumber) {
                updateProgress(totalpercent, "Uploading file " + filenumber, "");
            }
        });
    }

    private ProgressDialog pDialog;

    public void updateProgress(int val, String title, String msg) {
        pDialog.setTitle(title);
        pDialog.setMessage(msg);
        pDialog.setProgress(val);
    }

    public void showProgress(String str) {
        try {
            pDialog.setCancelable(false);
            pDialog.setTitle("Please wait");
//            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            pDialog.setMax(100); // Progress Dialog Max Value
            pDialog.setMessage(str);
            if (pDialog.isShowing())
                pDialog.dismiss();
            pDialog.show();
        } catch (Exception e) {

        }
    }

    public void hideProgress() {
        try {
            if (pDialog.isShowing())
                pDialog.dismiss();
        } catch (Exception e) {

        }
    }
*/
    /*private ArrayList<MultipartBody.Part> prepareFilePart(String partName) {

        ArrayList<MultipartBody.Part> imageFile = new ArrayList<>();

        if (imgList != null) {

            for (int i = 0; i < imgList.size(); i++) {
                File imgFile = new File(ImageFilePath.getPath(AdminCreateCatalogueActivity.this, imgList.get(i)));
                try {
                    float fileSizeKB = Float.parseFloat(String.valueOf(imgFile.length() / 1024));

                    if (fileSizeKB > 500) {
                        int quality = (int) (50000 / fileSizeKB);
                        imgFile = new Compressor(AdminCreateCatalogueActivity.this).setQuality(quality).compressToFile(imgFile);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);

                // MultipartBody.Part is used to send also the actual file name
//                imageFileArray.add(MultipartBody.Part.createFormData(partName, imgFile.getName(), requestFile));
                imageFile.add(MultipartBody.Part.createFormData(partName, imgFile.getName(), requestFile));
            }

        }

        return imageFile;
    }
*/




    }

