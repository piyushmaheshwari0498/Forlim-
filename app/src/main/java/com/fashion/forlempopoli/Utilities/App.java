package com.fashion.forlempopoli.Utilities;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;


public class App extends Application {
    public static  final String CHANNEL_ID ="notification_channel";
    private static App mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        /*Bugfender.init(this, "YxHsMsV1Pt9F3T5e5UDkJWLUaFd4bBcs", BuildConfig.DEBUG);
        Bugfender.enableCrashReporting();
        Bugfender.enableUIEventLogging(this);
        Bugfender.enableLogcatLogging(); // optional, if you want logs automatically collected from logcat
*/
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotication();
        }
        mInstance = this;

    }

        @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotication() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel serviceChannel = new NotificationChannel(CHANNEL_ID,
                    "Forlim App",
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public static synchronized App getInstance() {
        return mInstance;
    }


}
