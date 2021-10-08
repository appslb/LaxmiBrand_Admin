package com.e.laxmibrand_admin;

import android.content.Context;
import android.content.SharedPreferences;

import com.e.laxmibrand_admin.beans.LoginResponse;
import com.e.laxmibrand_admin.beans.UserCartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;



public class PreferenceSettings {
    private static final String PREFERENCE_NAME = "laxmi_namkeen";

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private Context mContext;
    private int PRIVATE_MODE = 0;

    private String KEY_IS_FIRST = "first time";
    private String KEY_IS_LOGIN = "is login";
    private String KEY_TOKEN = "token";
    private String KEY_USER_ID = "user id";
    private String KEY_USER_DETAILS = "user details";
    private String KEY_UNIQUE_ID = "unique id";
    private String KEY_BASKET = "basket list";

    public PreferenceSettings(Context context) {
        mContext = context;
        mSharedPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
    }

    public void clearPreference() {
        mEditor.clear().commit();
    }

    public void setFirstTime(boolean isLogin) {
        mEditor.putBoolean(KEY_IS_FIRST, isLogin).commit();
    }

    public boolean getFirstTime() {
        boolean data = mSharedPreferences.getBoolean(KEY_IS_FIRST, true);
        return data;
    }

    public void setIsLogin(boolean isLogin) {
        mEditor.putBoolean(KEY_IS_LOGIN, isLogin).commit();
    }

    public boolean getIsLogin() {
        boolean data = mSharedPreferences.getBoolean(KEY_IS_LOGIN, false);
        return data;
    }

    public void setUniqueNotiId(int id) {
        mEditor.putInt(KEY_UNIQUE_ID, id).commit();
    }

    public int getUniqueNotiId() {
        int data = mSharedPreferences.getInt(KEY_UNIQUE_ID, 0);
        return data;
    }

    public void setUserDetails(LoginResponse.ResponseData data) {
        Gson gson = new Gson();
        mEditor.putString(KEY_USER_DETAILS, gson.toJson(data)).commit();
    }

/*
    public ApiVerifyLoginResponse.ResponseData getUserDetails() {
        Gson gson = new Gson();
        ApiVerifyLoginResponse.ResponseData data = gson.fromJson(mSharedPreferences.getString(KEY_USER_DETAILS, null), ApiVerifyLoginResponse.ResponseData.class);
        return data;
    }
*/

    public void setBasketList(ArrayList<UserCartItem> basketList) {
  /*      if (basketList != null && basketList.size() > 0) {
            if (UserCatalogueViewFragment.badgeIV != null) {
                UserCatalogueViewFragment.badgeIV.setVisibility(View.VISIBLE);
            }
            if (UserSingleDesignCatalogueViewFragment.badgeIV != null) {
                UserSingleDesignCatalogueViewFragment.badgeIV.setVisibility(View.VISIBLE);
            }
            if (UserCatalogueDetailsActivity.badgeIV != null) {
                UserCatalogueDetailsActivity.badgeIV.setVisibility(View.VISIBLE);
            }
        } else {
            if (UserCatalogueViewFragment.badgeIV != null) {
                UserCatalogueViewFragment.badgeIV.setVisibility(View.GONE);
            }
            if (UserSingleDesignCatalogueViewFragment.badgeIV != null) {
                UserSingleDesignCatalogueViewFragment.badgeIV.setVisibility(View.GONE);
            }
            if (UserCatalogueDetailsActivity.badgeIV != null) {
                UserCatalogueDetailsActivity.badgeIV.setVisibility(View.GONE);
            }
        }
  */      Gson gson = new Gson();
        mEditor.putString(KEY_BASKET, gson.toJson(basketList)).commit();
    }

    public ArrayList<UserCartItem> getBasketList() {
        Gson gson = new Gson();
        ArrayList<UserCartItem> data = gson.fromJson(mSharedPreferences.getString(KEY_BASKET, null), new TypeToken<ArrayList<UserCartItem>>() {
        }.getType());

        /*if (data != null && data.size() > 0) {
            if (UserCatalogueViewFragment.badgeIV != null) {
                UserCatalogueViewFragment.badgeIV.setVisibility(View.VISIBLE);
            }
            if (UserSingleDesignCatalogueViewFragment.badgeIV != null) {
                UserSingleDesignCatalogueViewFragment.badgeIV.setVisibility(View.VISIBLE);
            }
            if (UserCatalogueDetailsActivity.badgeIV != null) {
                UserCatalogueDetailsActivity.badgeIV.setVisibility(View.VISIBLE);
            }
        } else {
            if (UserCatalogueViewFragment.badgeIV != null) {
                UserCatalogueViewFragment.badgeIV.setVisibility(View.GONE);
            }
            if (UserSingleDesignCatalogueViewFragment.badgeIV != null) {
                UserSingleDesignCatalogueViewFragment.badgeIV.setVisibility(View.GONE);
            }
            if (UserCatalogueDetailsActivity.badgeIV != null) {
                UserCatalogueDetailsActivity.badgeIV.setVisibility(View.GONE);
            }
        }
*/
        return data;
    }

    public void setFirebaseToken(String token) {
        mEditor.putString(KEY_TOKEN, token).commit();
    }

    public String getFirebaseToken() {
        String data = mSharedPreferences.getString(KEY_TOKEN, null);
        return data;
    }

    public void setUserId(String uid) {
        mEditor.putString(KEY_USER_ID, uid).commit();
    }

    public int getUserId() {
        int data = mSharedPreferences.getInt(KEY_USER_ID, 0);
        return data;
    }


}
