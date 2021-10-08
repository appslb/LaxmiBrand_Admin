package com.e.laxmibrand_admin.admin;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.beans.BaseResponse;
import com.e.laxmibrand_admin.utils.Utility;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AdminAboutUsActivity extends AppCompatActivity {

    Button saveBTN;
    ImageView logoutBTN,backPressed;
    TextInputEditText descET;
    String id = "0";
    JsonObject aboutUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_about_us);
        // Inflate the layout for this fragment

        saveBTN = findViewById(R.id.saveBTN);
        logoutBTN = findViewById(R.id.logoutBTN);
        backPressed = findViewById(R.id.backPressed);
        descET = findViewById(R.id.descET);
        descET.setText("Write About Your CompanyHere............");

        saveBTN.setEnabled(false);
        saveBTN.setAlpha(0.5f);

        descET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveBTN.setEnabled(true);
                saveBTN.setAlpha(1f);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), Login.class));

            }
        });

        backPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!descET.getText().toString().trim().isEmpty()) {
                    if (Utility.isOnline(AdminAboutUsActivity.this)) {

                        aboutUs = new JsonObject();
                        aboutUs.addProperty("about_us_desc", descET.getText().toString());

                        saveAboutUs(aboutUs);
                        Toast.makeText(AdminAboutUsActivity.this, "About Us Details added.", Toast.LENGTH_SHORT).show();

                    } else {
                       // Utility.noInternetError(getActivity());
                        Toast.makeText(AdminAboutUsActivity.this, "No Internet.", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(AdminAboutUsActivity.this, "Please enter about us details.", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        getAboutUsAPI();

    }

    private void saveAboutUs(JsonObject aboutUs) {
        final Dialog dialog = Utility.showProgress(AdminAboutUsActivity.this);
        Call<BaseResponse> add = Utility.retroInterface().about_us(aboutUs);
        add.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        if (response.body().getStatus().equalsIgnoreCase("success")) {
                            Toast.makeText(AdminAboutUsActivity.this, "About Us Details added.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(AdminAboutUsActivity.this, AdminAboutUsActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else {
                            Toast.makeText(AdminAboutUsActivity.this, "Could Not update about Us", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(AdminAboutUsActivity.this, "Could Not add about us", Toast.LENGTH_SHORT).show();


                    }
                } catch (Exception e) {
                    Utility.somethingWentWrong(AdminAboutUsActivity.this);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utility.dismissDialog(dialog);
                Utility.somethingWentWrong(AdminAboutUsActivity.this);
            }
        });
    }

}