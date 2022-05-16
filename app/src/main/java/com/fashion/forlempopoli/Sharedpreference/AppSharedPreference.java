package com.fashion.forlempopoli.Sharedpreference;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.fashion.forlempopoli.LoginActivity;
import com.fashion.forlempopoli.Utilities.Constants;


public class AppSharedPreference {
  private  static SharedPreferences mSharedPreference;
  private static  AppSharedPreference mAppSharedPreference;
  private static SharedPreferences.Editor mEditor;

    public static synchronized AppSharedPreference getAppSharedPreference(Context argContext)
  {
      if (mSharedPreference == null) {
          mSharedPreference = argContext.getApplicationContext().getSharedPreferences(Constants.SHARED_PREFRENCE_NAME, Context.MODE_PRIVATE);
          mAppSharedPreference = new AppSharedPreference();
          mEditor = mSharedPreference.edit();
      }
      return mAppSharedPreference;
  }
    public void putIntValue(String argKey, int argValue) {
        mEditor.putInt(argKey, argValue).apply();
    }

    public int getIntValue(String argKey)
    {
        return mSharedPreference.getInt(argKey,-1);
    }

    public void putStringValue(String argKey,String argValue)
    {
        mEditor.putString(argKey,argValue).apply();
    }
  public String getString(String argKey)
  {
    return mSharedPreference.getString(argKey,"");
  }
    public void putBooleanValue(String argKey,boolean argValue)
    {
        mEditor.putBoolean(argKey, argValue).apply();
    }

    public boolean getBooleanValue(String argKey)
    {
        return mSharedPreference.getBoolean(argKey,false);
    }


    public void clearAllValues()
    {
        mSharedPreference.edit().clear().apply();
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void checkLogin(Context _context){
        // Check login status

        if(!this.getBooleanValue(Constants.IS_LOGGED_IN)){
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, LoginActivity.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }

    }

    /**
     * Clear session details
     * */
    public void logoutUser(Context _context){
        // Clearing all data from Shared Preferences
        mEditor.clear();
        mEditor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

}
