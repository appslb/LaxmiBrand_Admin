package com.e.laxmibrand_admin.admin;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.admin.category.CategoryList;
import com.e.laxmibrand_admin.admin.category.CategoryListAdapter;
import com.e.laxmibrand_admin.admin.product.ProductList;
import com.e.laxmibrand_admin.beans.AdminCategory;
import com.e.laxmibrand_admin.beans.AdminFAQ;
import com.e.laxmibrand_admin.beans.BaseResponse;
import com.e.laxmibrand_admin.utils.Utility;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminFAQs extends AppCompatActivity {
    Button saveBTN;
    ImageView logoutBTN, backPressed;
    TextInputEditText quesET, ansET;
    ArrayList<AdminFAQ> faqList;
    FAQListAdapter faqListAdapter;
    RecyclerView faqListView;
    Context context;
    JsonObject faqObject;
String flag;String updateFaqId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_faq);

        saveBTN = findViewById(R.id.saveBTN);
        logoutBTN = findViewById(R.id.logoutBTN);
        backPressed = findViewById(R.id.backPressed);

        quesET = findViewById(R.id.quesET);
        ansET = findViewById(R.id.ansET);
        faqListView = findViewById(R.id.faqsList);

        //  saveBTN.setEnabled(false);
        //  saveBTN.setAlpha(0.5f);
        saveBTN.setText("SAVE");
        flag="n";
        faqList = new ArrayList<AdminFAQ>();
        context = AdminFAQs.this;
        addFAQListItem();
        faqListView.setLayoutManager(new LinearLayoutManager(AdminFAQs.this));
        faqListAdapter = new FAQListAdapter(AdminFAQs.this, faqList);
        faqListView.setAdapter(faqListAdapter);
        faqListAdapter.notifyDataSetChanged();


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
            public void onClick(View v) {

                if (quesET.getText().toString() == "" || ansET.getText().toString() == "") {
                    Toast.makeText(v.getContext(), "Fields cannot be blank", Toast.LENGTH_SHORT).show();
                } else {
                    if (Utility.isOnline(v.getContext())) {
                        faqObject = new JsonObject();
                        faqObject.addProperty("faq_title", quesET.getText().toString());
                        faqObject.addProperty("faq_answer", ansET.getText().toString());
                        faqObject.addProperty("is_active", 1);

                        if(flag.equals("n")) {

                            addNewFAQ(faqObject);
                        }
                        else
                        {
                            faqObject.addProperty("id",updateFaqId );
                            editFAQ(faqObject);

                        }

                    } else {
                        Utility.noInternetError(v.getContext());
                    }


                }
            }
        });

    }

    public void addFAQListItem() {
        getAllFaqs();

    }

    public void editFAQDetails(String faqId,String flag ){
        updateFaqId = faqId;
        saveBTN.setText("Update");
        this.flag=flag;
        for(int f=0;f<faqList.size();f++){
            if(faqId.equals(faqList.get(f).getFaqid())){
                quesET.setText(faqList.get(f).getFaqQues());
                ansET.setText(faqList.get(f).getFaqAns());

            }
        }

    }
    private void getAllFaqs() {

        final Dialog dialog = Utility.showProgress(context);
        Call<ResponseBody> get = Utility.retroInterface().getFaqList();
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
                            JSONObject objRO = object.getJSONObject("data");
                            JSONArray jA = objRO.getJSONArray("result");

                            for (int i = 0; i < jA.length(); i++) {
                                JSONObject obj = jA.getJSONObject(i);
                                String faqT = obj.getString("faq_title");
                                String faqA = obj.getString("faq_answer");

                                AdminFAQ faq = new AdminFAQ();
                                faq.setFaqQues(faqT);
                                faq.setFaqid(obj.getString("id"));
                                faq.setFaqis_active(obj.getString("is_active"));
                                faq.setFaqAns(faqA);
                                faqList.add(faq);

                            }

                            faqListAdapter = new FAQListAdapter(context, faqList);
                            faqListView.setAdapter(faqListAdapter);
                            faqListAdapter.notifyDataSetChanged();


                        } catch (JSONException je) {
                            Toast.makeText(context, "JSONEXCE : " + je.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    } else {
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

    private void addNewFAQ(JsonObject addCatObject) {
        final Dialog dialog = Utility.showProgress(context);
        Call<BaseResponse> add = Utility.retroInterface().insert_faq(addCatObject);
        add.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        if (response.body().getStatus().equalsIgnoreCase("success")) {
                            Toast.makeText(context, "FAQ Details added.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(context, AdminFAQs.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else {
                            Toast.makeText(context, "Could Not add new Faq Details", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Could Not add New Faq Details", Toast.LENGTH_SHORT).show();


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


    private void editFAQ(JsonObject editFaqObject) {
        final Dialog dialog = Utility.showProgress(context);
        Call<BaseResponse> edit = Utility.retroInterface().update_faq(editFaqObject);
        edit.enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        if (response.body().getStatus().equalsIgnoreCase("success")) {
                            Toast.makeText(context, "FAQ Details Updated.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(context, AdminFAQs.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);

                        } else {
                            Toast.makeText(context, "Could Not update Faq Details", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Could not update FAQ Details", Toast.LENGTH_SHORT).show();


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



