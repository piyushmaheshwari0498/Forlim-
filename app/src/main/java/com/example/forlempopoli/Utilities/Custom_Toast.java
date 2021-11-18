package com.example.forlempopoli.Utilities;

import android.content.Context;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.widget.Toast;

import com.example.forlempopoli.R;

import es.dmoral.toasty.Toasty;

public class Custom_Toast {

    public static void warning(Context context, String message){
        Toasty.warning(context,message, Toast.LENGTH_SHORT,true).show();
    }

    public static void long_warning(Context context, String message){
        Toasty.warning(context,message, Toast.LENGTH_LONG,true).show();
    }
    public static void error(Context context, String message){
        Toasty.error(context, message, Toast.LENGTH_SHORT,true).show();
    }

    public static void custom_error(Context context, String message){
        Toast toast = Toasty.error(context, message, Toast.LENGTH_SHORT,true);
        toast.setGravity(Gravity.BOTTOM, 1, 1);
        toast.show();
    }

    public static void add_cart_toast(Context context, String message){
        SpannableString spannableString = new SpannableString(message);
        spannableString.setSpan(
                new ForegroundColorSpan(context.getResources().getColor(android.R.color.white)),
                0,
                spannableString.length(),
                0);
        Toast toast = Toasty.success(context, spannableString,Toast.LENGTH_SHORT,true);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
    public static void success(Context context, String message){
        Toasty.success(context,message, Toast.LENGTH_SHORT,true).show();
    }

    public static void custom_success(Context context, String message){
        Toast toast = Toasty.success(context, message, Toast.LENGTH_SHORT,true);
        toast.setGravity(Gravity.TOP, 1, 1);
        toast.show();
        //Toasty.success(context,message, android.widget.Custom_Toast.LENGTH_LONG,true).show();
    }

    public static void info(Context context, String message){
        Toasty.info(context,message, Toast.LENGTH_SHORT,true).show();
    }

}
