package com.e.laxmibrand_admin.admin.product;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand_admin.admin.MainActivity;
import com.e.laxmibrand_admin.admin.PageNoAdapter;
import com.e.laxmibrand_admin.retrofit.RetroInterface;
import com.squareup.picasso.Picasso;


import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.beans.AdminCategory;
import com.e.laxmibrand_admin.beans.Products;
import com.e.laxmibrand_admin.utils.Utility;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
   // private ViewPager mViewPager;
    ImageView addProduct,backPressed;
    static String[] cList;
    static ArrayList<String> newCList;
    static int k=0;
    boolean isf=false;
    static RecyclerView pagenorv;
    static PageNoAdapter pagenoAdapter;
    TabLayout tabLayout;
    FrameLayout productListArea;
    DemoObjectFragment productListPage;
    FragmentTransaction transaction;
    static Spinner categorySpinner;
    ArrayList<String> list;
    static ArrayList<AdminCategory> catList;
    static int noOfPages;
    static int[] pages;
    static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list);
        context=ProductList.this;
       // mViewPager = (ViewPager) findViewById(R.id.pager);
        addProduct = (ImageView) findViewById(R.id.addProduct);
     //   tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        productListArea = (FrameLayout) findViewById(R.id.productListArea);
        categorySpinner = (Spinner) findViewById(R.id.spinnerCategory);
        backPressed = (ImageView) findViewById(R.id.back_pressed);
        catList = new ArrayList<AdminCategory>();
        newCList = new ArrayList<String>();
        pagenorv =(RecyclerView) findViewById(R.id.pagenoRV);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        pagenorv.setLayoutManager(layoutManager);
        noOfPages=1;
        addItemsOnSpinner1(noOfPages);

        backPressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        categorySpinner.setOnItemSelectedListener(this);

addProduct.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(ProductList.this, AddProduct.class));
    }
});
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        isf=true;
            productListPage = new DemoObjectFragment();
            Bundle bundle = new Bundle();
            bundle.putString("selected_cat_id", catList.get(pos).getCatid());
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            productListPage.setArguments(bundle);
            transaction.replace(R.id.productListArea, productListPage);
            transaction.addToBackStack(null);
            transaction.commit();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public static void addItemsOnSpinner1(int pageno) {
        if (Utility.isOnline(context)) {

            getAllCategoryDetails(pageno);
        }
        else
        {
            Utility.noInternetError(context);
        }
    }

    private static void getAllCategoryDetails(int pageno) {
        final Dialog dialog = Utility.showProgress(context);
     //   Call<ResponseBody> get = Utility.retroInterface().getAllACategory("2");
        Call<ResponseBody> get = Utility.retroInterface().getCatNew("http://dev.polymerbazaar.com/laxmibrand/admin/category/list/"+pageno);
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
                            int noOfPages2 = objRO.getInt("total_pages");
                            JSONArray jA = objRO.getJSONArray("result");
                            for (int i = 0; i < jA.length(); i++) {
                                JSONObject obj = jA.getJSONObject(i);
                                String name = obj.getString("category_name");
                                AdminCategory cat = new AdminCategory();
                                cat.setCatName(name);
                                cat.setCatId(obj.getString("category_id"));
                                catList.add(cat);
                                newCList.add(name);
                                Log.i("catname",name);
                             //   cList[k]=name;
                             //   k++;
                            }

                            if(noOfPages2>1 && noOfPages<noOfPages2) {
                                noOfPages = noOfPages + 1;
                                getAllCategoryDetails(noOfPages);
                                Log.i("noOfPages : " ,""+noOfPages2);

                      /*   pagenorv.setVisibility(View.VISIBLE);
                         pages = new int[noOfPages2];
                         for (int i = 0; i < noOfPages2; i++) {
                             pages[i] = i+1;
                             Log.i("cur Value : " ,pages[i]+"");

                         }
                         RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                         pagenorv.setLayoutManager(layoutManager);
                      */   //  pagenoAdapter = new PageNoAdapter(context, pages);

                                //pagenorv.setAdapter(pagenoAdapter);
                            }
                            else
                            {
                                // pagenorv.setVisibility(View.GONE);
                            }

                            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(context,
                                    android.R.layout.simple_spinner_item, newCList);
                            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            categorySpinner.setAdapter(dataAdapter);



                        }catch(JSONException je){
                            //Toast.makeText(context, "JSONEXCE : " + je.getMessage(), Toast.LENGTH_LONG).show();
                        }

                        // }
                        //  }
                    }
                    else {
                     //   Toast.makeText(context, "other than 200", Toast.LENGTH_LONG).show();

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


    }


}