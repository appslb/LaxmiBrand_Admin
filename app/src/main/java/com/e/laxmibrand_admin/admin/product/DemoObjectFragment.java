package com.e.laxmibrand_admin.admin.product;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.admin.PageNoAdapter;
import com.e.laxmibrand_admin.admin.category.CategoryList;
import com.e.laxmibrand_admin.admin.category.CategoryListAdapter;
import com.e.laxmibrand_admin.beans.AdminCategory;
import com.e.laxmibrand_admin.beans.DiscountedProducts;
import com.e.laxmibrand_admin.beans.Products;
import com.e.laxmibrand_admin.beans.Var;
import com.e.laxmibrand_admin.utils.Utility;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DemoObjectFragment extends Fragment {
    public static final String ARG_OBJECT = "object";
    static RecyclerView productList;
    TextView addProductBtn;
    static LinearLayout empty_product;
    static ProductListAdapter productListAdapter;
   static ArrayList<Products> productItemList;
    static ArrayList<Var> varItemList;

    static Context context;
    static String selectedCategory;
    static int noOfPages;
    static int[] pages;
    static RecyclerView pagenorv;
    static PageNoAdapter pagenoAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab_view_item, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        Bundle args = getArguments();
        selectedCategory = args.getString("selected_cat_id");
        productList = (RecyclerView) view.findViewById(R.id.productList);
        productItemList = new ArrayList<Products>();
        varItemList = new ArrayList<Var>();
        pagenorv = (RecyclerView) view.findViewById(R.id.pagenoRV);
        productList.setLayoutManager(new LinearLayoutManager(getActivity()));
        addProductBtn = (TextView) view.findViewById(R.id.addProductBtn);
        empty_product = (LinearLayout) view.findViewById(R.id.empty_product);
       noOfPages = 1;
        if (Utility.isOnline(context)) {
            getAllProducts(noOfPages);
        }
        else
        {
            Utility.noInternetError(context);
        }


        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(),AddProduct.class));
            }
        });


    }



    public  static void getAllProducts(int pageno) {
        final Dialog dialog = Utility.showProgress(context);
        JsonObject prdctObject;
        prdctObject = new JsonObject();

        prdctObject.addProperty("popularity", "0");
        prdctObject.addProperty("price", "0");
        prdctObject.addProperty("sort", "0");

        Call<ResponseBody> get = Utility.retroInterface().getProductsByPage("http://dev.polymerbazaar.com/laxmibrand/admin/product/list/"+pageno,prdctObject);
        get.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        try {
                            ResponseBody responseBody = response.body();
                            String s = responseBody.string();
                            Log.i("Response : ",s);
                            JSONObject object = new JSONObject(s);
                            // JSONArray jsonElements = object.getJSONArray("data");
                            JSONObject objRO = object.getJSONObject("data");
                            // JSONObject objR = jsonElements.getJSONObject(0);
                            int noOfPages2 = objRO.getInt("total_pages");
                            JSONArray jA = objRO.getJSONArray("result");
                            for (int i = 0; i < jA.length(); i++) {
                                varItemList = new ArrayList<Var>();

                                JSONObject obj = jA.getJSONObject(i);
                               JSONObject objP = obj.getJSONObject("product");
                                Products product = new Products();
                                product.setCategory_id(objP.getString("category_id"));
                                if(selectedCategory.equals( objP.getString("category_id"))) {
                                    product.setPdt_id(objP.getString("pdt_id"));
                                    product.setPdt_name(objP.getString("pdt_name"));
                                    product.setPdt_about(objP.getString("pdt_about"));
                                    product.setPrdt_images(objP.getString("prdt_images"));
                                    product.setIs_active(objP.getString("is_active"));
                                    JSONArray jVar = obj.getJSONArray("varient");
                                    for(int j=0;j<jVar.length();j++)
                                    {
                                        JSONObject objVar = jVar.getJSONObject(j);
                                        Var v = new Var();
                                        v.setVarType(objVar.getString("var_type"));
                                        v.setVarIsActive(objVar.getString("is_active"));
                                        v.setVarDisAmt(objVar.getString("var_discount_price"));
                                        v.setVarActualAmt(objVar.getString("var_actual_price"));
                                        varItemList.add(v);
                                    }
                                    product.setVarientItems(varItemList);

                                           productItemList.add(product);
                                }
                            }
                            if(noOfPages2>1 && noOfPages<noOfPages2) {
                                noOfPages = noOfPages + 1;
                                getAllProducts(noOfPages);
                                Log.i("noOfPages : " ,""+noOfPages2);


                                }

                            else
                            {

                            if(productItemList.size()>0)
                            {
                                productList.setVisibility(View.VISIBLE);
                                empty_product.setVisibility(View.GONE);
                                productListAdapter = new ProductListAdapter(context,productItemList);
                                productList.setAdapter(productListAdapter);
                                productListAdapter.notifyDataSetChanged();
                            }
                            else
                            {
                                productList.setVisibility(View.GONE);
                                empty_product.setVisibility(View.VISIBLE);
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

}