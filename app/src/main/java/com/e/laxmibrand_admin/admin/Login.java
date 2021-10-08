package com.e.laxmibrand_admin.admin;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.beans.LoginResponse;
import com.e.laxmibrand_admin.utils.Utility;
import com.google.android.material.textfield.TextInputEditText;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Login extends AppCompatActivity {
Button btnContinue;
TextInputEditText userid,password;
String str_userid,str_password;
Context context;
RequestBody email,pass;
    private static final int PERMISSIONS_WRITE_EXTERNAL_STORAGE = 100;
        SharedPreferences sharedPref;
        String useri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = Login.this;

        checkPermission();
        sharedPref = getSharedPreferences("laxmi_brand", Context.MODE_PRIVATE);

        useri=sharedPref.getString("userid","");
     if(!useri.equals("")){
    startActivity(new Intent(context, MainActivity.class)
            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));


}
else {

    setContentView(R.layout.activity_admin_login);
    btnContinue = (Button) findViewById(R.id.btn_continue);
    userid = (TextInputEditText) findViewById(R.id.userid);
    password = (TextInputEditText) findViewById(R.id.password);

    btnContinue.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            str_userid = userid.getText().toString();
            str_password = password.getText().toString();
            if (!userid.getText().toString().trim().isEmpty()) {
                if (!password.getText().toString().trim().isEmpty()) {
                    if (Utility.isOnline(v.getContext())) {
                        email = RequestBody.create(MediaType.parse("multipart/form-data"), userid.getText().toString().trim());
                        pass = RequestBody.create(MediaType.parse("multipart/form-data"), password.getText().toString().trim());
                        loginAPI(email,
                                pass);
                    } else {
                        Utility.noInternetError(v.getContext());
                    }
                } else {
                    Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Login.this, "Enter Username", Toast.LENGTH_SHORT).show();
            }
        }
    });
}
}
    private void loginAPI(RequestBody email, RequestBody password) {
        final Dialog dialog = Utility.showProgress(Login.this);
        Call<LoginResponse> login = Utility.retroInterface().verify_login(email, password);
        login.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                Utility.dismissDialog(dialog);
                try {
                    if (response.code() == 200) {
                        if (response.body().getStatus()) {
                            Toast.makeText(context, "Successfully Logged in.", Toast.LENGTH_SHORT).show();

                                    sharedPref.edit().putString("userid",userid.getText().toString()).apply();
                                startActivity(new Intent(context, MainActivity.class)
                                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));

                        }
                        else {
                            // Utility.somethingWentWrong(context);
                            Toast.makeText(context, "Wrong Email or Password.", Toast.LENGTH_SHORT).show();

                        }

                    }
                    else {
                       // Utility.somethingWentWrong(context);
                        Toast.makeText(context, "Wrong Email or Password.", Toast.LENGTH_SHORT).show();

                    }
                } catch (Exception e) {
                                Utility.somethingWentWrong(context);
                                Log.i("login error:",e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Utility.dismissDialog(dialog);
                Utility.somethingWentWrong(Login.this);
                Log.i("login failure:",t.getMessage());

            }
        });
    }

    public void checkPermission()
    {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED )  {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSIONS_WRITE_EXTERNAL_STORAGE);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method
        }
        else {
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



}
