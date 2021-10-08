package com.e.laxmibrand_admin.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.e.laxmibrand_admin.MyApplication;
import com.e.laxmibrand_admin.PreferenceSettings;
import com.e.laxmibrand_admin.R;
import com.e.laxmibrand_admin.retrofit.RetroInterface;
import com.e.laxmibrand_admin.retrofit.RetrofitAdapter;

import java.io.ByteArrayOutputStream;
import java.util.regex.Pattern;



public class Utility {

    public static void disableAllView(ViewGroup layout) {
        layout.setEnabled(false);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                disableAllView((ViewGroup) child);
            } else {
                child.setEnabled(false);
            }
        }
    }

/*
    public static void logout(Context context) {
        Utility.mPreferenceSettings().setIsLogin(false);
        Utility.mPreferenceSettings().setUserDetails(null);
        context.startActivity(new Intent(context, LoginActivity.class)
                .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
    }
*/

    public static void scanGallery(Context context, String path) {
        try {
            MediaScannerConnection.scanFile(context, new String[]{path}, null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "There was an issue scanning gallery.");
        }
    }


    public static Dialog showProgress(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.transparent));

        dialog.show();
        return dialog;
    }

    public static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null) {
            return false;
        } else {
            return pat.matcher(email).matches();
        }
    }

    public static void dismissDialog(Dialog dialog) {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public static void generateKeyHash() {


//        sha = "F6:AE:11:2B:EE:46:B4:C6:58:98:35:F6:8D:B6:9B:84:39:CC:EF:93";

        byte[] sha1 = {
                (byte) 0xF6, (byte) 0xAE, 0x11, 0x2B, (byte) 0xEE, 0x46, (byte) 0xB4, (byte) 0xC6, 0x58, (byte) 0x98, 0x35,
                (byte) 0xF6, (byte) 0x8D, (byte) 0xB6, (byte) 0x9B, (byte) 0x84, 0x39, (byte) 0xCC, (byte) 0xEF, (byte) 0x93
        };


        Log.e("keyhashGooglePlaySignIn", Base64.encodeToString(sha1, Base64.NO_WRAP));

    }

    public static void enableAllView(ViewGroup layout) {
        layout.setEnabled(true);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof ViewGroup) {
                enableAllView((ViewGroup) child);
            } else {
                child.setEnabled(true);
            }
        }
    }


 public static boolean isOnline(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    public static void somethingWentWrong(Context context) {
        Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
    }


    public static void noInternetError(final Context context) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);//, R.style.AlertDialogTheme);
        builder1.setTitle("Error");
        builder1.setIcon(R.drawable.finallogo);
        builder1.setMessage("Internet connection not available!");
        builder1.setCancelable(true);
        builder1.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (isOnline(context)) {
                            dialog.dismiss();
                        }
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
  /*  public static void showUnauthorisedDialog(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.unauthorised_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.AlertDialogTheme;
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.transparent));

        Button okBTN = dialog.findViewById(R.id.okBTN);

        okBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPreferenceSettings().setUserDetails(null);
                mPreferenceSettings().setIsLogin(false);
                mPreferenceSettings().setUserId(0);
                context.startActivity(new Intent(context, LoginActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        dialog.show();
    }
*/
    public static String getBase64(Bitmap bm) {
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] b = baos.toByteArray();
            String encImage = Base64.encodeToString(b, Base64.DEFAULT);

            return encImage;
        } else
            return "";
    }


    public static PreferenceSettings mPreferenceSettings() {
       PreferenceSettings setting = MyApplication.getInstance().getPreferenceSettings();
        return setting;
    }
    public static RetroInterface retroInterface() {
        RetroInterface retroInterface = RetrofitAdapter.retrofit.create(RetroInterface.class);
        return retroInterface;
    }

}
