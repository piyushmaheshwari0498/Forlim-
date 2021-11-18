package com.example.forlempopoli.Utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;

public class AppUtils {
    public boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected= activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }
    public static String getFilePathFromContentUri(Uri selectedVideoUri,
                                                   Context mContext) {
        ContentResolver contentResolver = mContext.getContentResolver();
        String filePath;
        String[] filePathColumn = {MediaStore.MediaColumns.DATA};

        Cursor cursor = contentResolver.query(selectedVideoUri, filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    public static String getFilePath(Uri argUri)
    {
        String path = argUri.getPath(); // "/mnt/sdcard/FileName.mp3"
        return path;
    }
}
