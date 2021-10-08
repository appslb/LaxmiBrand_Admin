package com.e.laxmibrand_admin.admin;

import android.app.Dialog;
import android.content.Context;
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


public class ContactUsActivity extends AppCompatActivity {

    Button saveBTN;
    ImageView logoutBTN,backPressed;
    TextInputEditText descET, emailET, mobileNoET,whatsappLinkET;
    String srno = "0";
    Context context;
    JsonObject contactUs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        saveBTN = findViewById(R.id.saveBTN);
        logoutBTN = findViewById(R.id.logoutBTN);
        backPressed = findViewById(R.id.backPressed);
        descET = findViewById(R.id.descET);
        emailET = findViewById(R.id.emailET);
        whatsappLinkET = findViewById(R.id.whatsappGrpLink);
        mobileNoET = findViewById(R.id.mobileNoET);
        context = ContactUsActivity.this;
whatsappLinkET.setVisibility(View.GONE);

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


        descET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveBTN.setAlpha(1f);
                saveBTN.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveBTN.setAlpha(1f);
                saveBTN.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mobileNoET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                saveBTN.setAlpha(1f);
                saveBTN.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        saveBTN.setAlpha(0.5f);
        saveBTN.setEnabled(false);


        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!descET.getText().toString().trim().isEmpty()) {
                    if (!emailET.getText().toString().trim().isEmpty()) {
                        if (Utility.isValidEmail(emailET.getText().toString().trim())) {
                            if (!mobileNoET.getText().toString().trim().isEmpty()) {

                                    if (Utility.isOnline(ContactUsActivity.this)) {

                                        contactUs = new JsonObject();
                                        contactUs.addProperty("desc", descET.getText().toString());
                                        contactUs.addProperty("mobile_no", mobileNoET.getText().toString());
                                        contactUs.addProperty("email", emailET.getText().toString());
                                        saveContactUs(contactUs);

                                    }
                                    else {
                                        Utility.noInternetError(ContactUsActivity.this);
                                    }

                            } else {
                                Toast.makeText(ContactUsActivity.this, "Please enter phone no.", Toast.LENGTH_SHORT).show();
                            }
                        }
                         else {
                            Toast.makeText(ContactUsActivity.this, "Please enter valid email address.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ContactUsActivity.this, "Please enter email address.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ContactUsActivity.this, "Please enter description.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //getContactUsDetailsAPI();

    }
    private void saveContactUs(JsonObject contactObject) {
        final Dialog dialog = Utility.showProgress(context);
        Call<BaseResponse> add = Utility.retroInterface().contact_us(contactObject);
        add.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        if (response.body().getStatus().equalsIgnoreCase("success")) {
                            Toast.makeText(context, "Contact Details added.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(context, ContactUsActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else {
                            Toast.makeText(context, "Could Not add contact Details", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Could Not add contact Details", Toast.LENGTH_SHORT).show();


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

}
