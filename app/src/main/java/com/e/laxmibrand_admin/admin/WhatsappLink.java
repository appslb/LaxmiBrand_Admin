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


public class WhatsappLink extends AppCompatActivity {

    Button saveBTN;
    ImageView logoutBTN,backPressed,editBtn;
    TextInputEditText descET;
    String id = "0";
    String whatsapp_link;
    JsonObject saveLinkDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wa_link);

        saveBTN = findViewById(R.id.saveBTN);
        logoutBTN = findViewById(R.id.logoutBTN);
        backPressed = findViewById(R.id.backPressed);
        descET = findViewById(R.id.waText);
        editBtn =findViewById(R.id.editBtn);
       descET.setEnabled(false);
        descET.setText(whatsapp_link);
        saveBTN.setEnabled(false);
        saveBTN.setAlpha(0.5f);
        saveLinkDetails = new JsonObject();

        getWALink();


editBtn.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        descET.setEnabled(true);
        descET.setFocusable(true);
    }
});

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
                    if (Utility.isOnline(WhatsappLink.this)) {


                        saveLinkDetails.addProperty("new_whatsapp_grp_link", descET.getText().toString().trim());
saveLinkAPI();
                    } else {
                        // Utility.noInternetError(getActivity());
                        Toast.makeText(WhatsappLink.this, "No Internet.", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(WhatsappLink.this, "Please enter Link details.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    private void getWALink() {
        final Dialog dialog = Utility.showProgress(WhatsappLink.this);
        Call<BaseResponse> get = Utility.retroInterface().getWhatsappGroupLink();
        get.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        if (response.body().getStatus().equalsIgnoreCase("success")) {
                            whatsapp_link = response.body().getResponseData().getWhatsapp_grp_redirect_link();
                            descET.setText(whatsapp_link);

                        }
                    }
                }
                catch (Exception e) {
                  //  Toast.makeText(WhatsappLink.this, "Could Not get link"+e.getMessage(), Toast.LENGTH_SHORT).show();

                    Utility.somethingWentWrong(WhatsappLink.this);
                }
            }
            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utility.dismissDialog(dialog);

                Utility.somethingWentWrong(WhatsappLink.this);
            }
        });


    }



    public void saveLinkAPI()
    {
        final Dialog dialog = Utility.showProgress(WhatsappLink.this);
        Call<BaseResponse> addContact = Utility.retroInterface().addwhatsapplink(saveLinkDetails);
        addContact.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        if (response.body().getStatus().equalsIgnoreCase("success")) {
                            Toast.makeText(WhatsappLink.this, "Whatsapp Link Added.", Toast.LENGTH_SHORT).show();
                                descET.setEnabled(false);
                        } else {
                            Toast.makeText(WhatsappLink.this, "Whatsapp Linknot added.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(WhatsappLink.this, "Whatsapp Linknot added.", Toast.LENGTH_SHORT).show();


                    }
                } catch (Exception e) {
                    Utility.somethingWentWrong(WhatsappLink.this);

                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                Utility.dismissDialog(dialog);
                Utility.somethingWentWrong(WhatsappLink.this);
            }
        });


    }

}