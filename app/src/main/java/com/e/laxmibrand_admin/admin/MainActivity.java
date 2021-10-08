package com.e.laxmibrand_admin.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.e.laxmibrand_admin.MyService;
import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.admin.category.CategoryList;
import com.e.laxmibrand_admin.admin.product.ProductList;
import com.e.laxmibrand_admin.beans.OrderItem;
import com.e.laxmibrand_admin.beans.Orders;
import com.e.laxmibrand_admin.beans.Var;
import com.e.laxmibrand_admin.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
        ImageView orders,products,category;
        CardView card9_wal,card1_order,card2_products,card3_category,card4_aboutus,card5_contactus,card6_faqs,card7_promo,card8_market;
    JSONArray jA;
    int pl,noOfPages,totalno;
    SharedPreferences sharedPref;
Timer myTimer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        orders= (ImageView) findViewById(R.id.order);
        category= (ImageView) findViewById(R.id.category);
        products = (ImageView) findViewById(R.id.product);
        card1_order = (CardView) findViewById(R.id.card1);
        card2_products = (CardView) findViewById(R.id.card2);
        card3_category = (CardView) findViewById(R.id.card3);
        card4_aboutus = (CardView) findViewById(R.id.card4);
        card5_contactus = (CardView) findViewById(R.id.card5);
        card6_faqs = (CardView) findViewById(R.id.card6);
        card7_promo = (CardView) findViewById(R.id.card7);
        card8_market = (CardView) findViewById(R.id.card8);
        card9_wal = (CardView) findViewById(R.id.card9);
        sharedPref = getSharedPreferences("laxmi_brand", Context.MODE_PRIVATE);
        myTimer = new Timer();
        noOfPages=1;
        totalno=0;

     //   getAllOrder(noOfPages);
//  startService();
autoStartService();

        card1_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //  myTimer.cancel();

               startActivity(new Intent(MainActivity.this,AdminOrderFragment.class));

            }
        });

        card7_promo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                myTimer.cancel();

                startActivity(new Intent(MainActivity.this,AdminPromotional.class));
            //stopService();
            }
        });

        card8_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
   //             myTimer.cancel();

                startActivity(new Intent(MainActivity.this,AdminMarketing.class));
            //startService();

            }
        });

        card5_contactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
     //           myTimer.cancel();

                startActivity(new Intent(MainActivity.this,ContactUsActivity.class));
            }
        });

        card4_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //        myTimer.cancel();
                startActivity(new Intent(MainActivity.this,AdminAboutUsActivity.class));
            }
        });

        card3_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(MainActivity.this, CategoryList.class));
                Intent i=new Intent(MainActivity.this, CategoryList.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });

        card6_faqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(MainActivity.this,AdminFAQs.class));
            }
        });


        card9_wal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,WhatsappLink.class));
            }
        });



        card2_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // startActivity(new Intent(MainActivity.this, ProductList.class));
                Intent i=new Intent(MainActivity.this, ProductList.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(i);
            }
        });
    }



    public void autoStartService() {

            myTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    //TimerMethod();
                        noOfPages = 1;
                        totalno = 0;
                        getAllOrder(noOfPages);
                   }

            }, 2000, 7000);

    }
    public void startService(){
        String input = "You got a new Order!!";


        Intent serviceIntent = new Intent(MainActivity.this, MyService.class);
        serviceIntent.putExtra("inputExtra",input);
        ContextCompat.startForegroundService(MainActivity.this, serviceIntent);
      //  startService(serviceIntent);

    }


    public void stopService(){
        Intent serviceIntent = new Intent(MainActivity.this,MyService.class);

        stopService(serviceIntent);
    }




    private  void getAllOrder(int pageno) {
      //  final Dialog dialog = Utility.showProgress(MainActivity.this);
        Call<ResponseBody> get = Utility.retroInterface().getOrderNew("http://dev.polymerbazaar.com/laxmibrand/admin/order/list/"+pageno);

        get.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                    //      Utility.dismissDialog(dialog);
                    try {
                        if (response.code() == 200) {
                            try {
                                ResponseBody responseBody = response.body();
                                String s = responseBody.string();
                                JSONObject object = new JSONObject(s);
                                JSONObject objRO = object.getJSONObject("data");
                                int noOfPages2 = objRO.getInt("total_pages");
                                int records = objRO.getInt("total_record");

                                jA = objRO.getJSONArray("result");
                                totalno = records;
                                Log.i("totalno","Totalo=="+totalno);

/*
                                if(noOfPages2>1 && noOfPages<noOfPages2) {
                                    noOfPages = noOfPages + 1;
                                    getAllOrder(noOfPages);
                                }
                                else {
*/

                                    pl = sharedPref.getInt("lasttotal", 0);
                                    Log.i("PL",""+pl);

                                    if (totalno > pl && pl > 0) {
                                        Log.i("PL",""+pl);

                                        Log.i("Starting service Msg","STARTING.......");
                                        myTimer.cancel();
                                 //   myTimer=null;
                                     startService();
                                       /* String input = "Welcome to Laxmi brand namkeen";

                                        Log.i("in start service",input);

                                        Intent serviceIntent = new Intent(MainActivity.this, MyService.class);
                                        serviceIntent.putExtra("inputExtra",input);
                                        ContextCompat.startForegroundService(MainActivity.this, serviceIntent);*/

                                        //sharedPref.edit().putInt("lasttotal",totalno).apply();
                                      //  generateNotification(MainActivity.this,"New Message");

                                    }
                                    else
                                    {
//                                        sharedPref.edit().putInt("lasttotal",totalno).apply();
                                      //  myTimer = new Timer();
                                       // autoStartService();
                                        //stopService();
                                    }
                                sharedPref.edit().putInt("lasttotal",totalno).apply();

//                                }

                            } catch (JSONException je) {
                              //  Toast.makeText(MainActivity.this, "JSONEXCE : " + je.getMessage(), Toast.LENGTH_LONG).show();
                            }

                        } else {
                           // Toast.makeText(MainActivity.this, "other than 200", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        //Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                        //Utility.somethingWentWrong(MainActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //  Utility.dismissDialog(dialog);
                    //Utility.somethingWentWrong(MainActivity.this);
                }
            });

        }

    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.logo;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);

        String title = context.getString(R.string.app_name);

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.putExtra("ms", message);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
       //notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notification.defaults |= Notification.DEFAULT_SOUND;

        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notificationManager.notify(0, notification);
    }


    @Override
    public void  onBackPressed(){
        Intent i=new Intent(MainActivity.this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }
    }