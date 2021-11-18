package com.example.forlempopoli.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Utilities.Constants;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PdfSamplingDataActivity extends AppCompatActivity {
    PDFView sampling_pdfView;
    ProgressBar progressBar;
    int position;
    String smId, request_pdf;
    File downloadedFile;
    String fileName;
    String rootDirectoryPath;
    Toolbar toolbar1;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_sampling_data);
        sampling_pdfView = findViewById(R.id.sampling_pdfView);
        progressBar = findViewById(R.id.progressBar);
        toolbar1 = findViewById(R.id.toolbar1);
        toolbar1.setTitle("Forlem Popoli");
        setSupportActionBar(toolbar1);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        System.out.println("position" + position);

        Intent intent1 = getIntent();
        smId = intent1.getStringExtra(Constants.INTENT_KEYS.KEY_SM_ID);
        System.out.println("smId" + smId);

        Intent intent2 = getIntent();
        request_pdf = intent2.getStringExtra("request_pdf");
        System.out.println("request_pdf" + request_pdf);

        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        PRDownloader.initialize(getApplicationContext());
        checkPdfAction();
    }
    private void checkPdfAction() {
        progressBar.setVisibility(View.VISIBLE);
        fileName = "samplingdata.pdf";
        rootDirectoryPath=getRootDirPath(this);
        System.out.println("Root Directory  Path"+rootDirectoryPath);
        downloadPdfFromInternet(request_pdf, getRootDirPath(this), fileName);
    }

    private void downloadPdfFromInternet(String url, final String dirPath, final String fileName) {
        PRDownloader.download(url, dirPath, fileName).build().start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
               // Toast.makeText(getApplicationContext(), "downloadComplete", Toast.LENGTH_LONG).show();
                downloadedFile = new File(dirPath, fileName);

                System.out.println("downloadedFile" + downloadedFile);
                progressBar.setVisibility(View.GONE);
                showPdfFromFile(downloadedFile);
            }

            @Override
            public void onError(Error error) {
                Toast.makeText(getApplicationContext(), "Error in downloading file : $error", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void showPdfFromFile(File file) {
        sampling_pdfView.fromFile(file)
                .password(null)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .load();
    }

    public static String getRootDirPath(Context context) {
        String folderName = "SamplingPdf_Files";
        if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            System.out.println("Sd Card is Present");
            File rootFolder = new File(Environment.getExternalStorageDirectory(), folderName);
            if (!rootFolder.exists()) {
                boolean isFolderCreated = rootFolder.mkdir();
                System.out.println("Is Folder Created in Sd Card" + isFolderCreated);
            }
            return rootFolder.getAbsolutePath();
        } else {
            System.out.println("Sd Card is Not Present");
            File rootFolder = new File(context.getExternalFilesDir(null), folderName);
            //File rootFolder=new File(context.getApplicationContext().getFilesDir(),folderName);
            if (!rootFolder.exists()) {
                boolean isFolderCreated = rootFolder.mkdir();
                System.out.println("Is Folder Created in Internal Storage" + isFolderCreated);
            }
            return rootFolder.getAbsolutePath();
        }
    }
    public void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            System.out.println("Has File Sucessfully Moved "+src.renameTo(dst));
            //inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // return super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_samplingdatashare, menu);

        getMenuInflater().inflate(R.menu.menu_samplingdownload, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_samplingdata_share:
                System.out.println("share_menu");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("application/pdf");
                    PackageManager pm = getPackageManager();
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    Intent openInChooser = Intent.createChooser(shareIntent, "Share the file ...");
                    List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
                    List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
                    for (int i = 0; i < resInfo.size(); i++) {
                        // Extract the label, append it, and repackage it in a LabeledIntent
                        ResolveInfo ri = resInfo.get(i);
                        String packageName = ri.activityInfo.packageName;
                        if (packageName.contains("android.gm")|| packageName.contains("com.whatsapp")) {
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, "");
                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+rootDirectoryPath+"/"+fileName));
                            System.out.println("Marshmellow above gmail"+Uri.parse(rootDirectoryPath+"/"+fileName));
                        }
                        intentList.add(new LabeledIntent(shareIntent, packageName, ri.loadLabel(pm), ri.icon));
                    }
                    // convert intentList to array
                    LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);
                    openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
                    startActivity(openInChooser);
                }
                else {
                    System.out.println("share_else");
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("application/pdf");
                    PackageManager pm = getPackageManager();
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    Intent openInChooser = Intent.createChooser(shareIntent, "Share the file ...");
                    List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
                    List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
                    for (int i = 0; i < resInfo.size(); i++) {
                        // Extract the label, append it, and repackage it in a LabeledIntent
                        ResolveInfo ri = resInfo.get(i);
                        String packageName = ri.activityInfo.packageName;
                        if (packageName.contains("android.gm")|| packageName.contains("com.whatsapp")) {
                            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                            shareIntent.putExtra(Intent.EXTRA_TEXT, "");
                            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://"+rootDirectoryPath+"/"+fileName));
                            System.out.println("Marshmellow above gmail"+Uri.parse(rootDirectoryPath+"/"+fileName));
                        }
                        intentList.add(new LabeledIntent(shareIntent, packageName, ri.loadLabel(pm), ri.icon));
                    }
                    // convert intentList to array
                    LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);
                    openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
                    startActivity(openInChooser);
                }
                break;
            case R.id.action_samplingdownload:
                try {
                    File destinationFileDirectory=new File(String.valueOf(getExternalFilesDirs(Environment.DIRECTORY_DOWNLOADS)));
                    if(!destinationFileDirectory.exists()){
                        destinationFileDirectory.mkdir();
                    }
                    //System.out.println("Check Download Directory Exists "+getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());
                    System.out.println("Check Download Directory Exists "+Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS));
                    String downloadDrectoryPath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
                    //copyFile(new File(rootDirectoryPath+"/"+fileName), new File((getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+ "/Downloads/")));
                    copyFile(new File(rootDirectoryPath+"/"+fileName),new File(downloadDrectoryPath+"/"+fileName));
                    Toast.makeText(getApplicationContext(),"PDF Downloaded Successfully"+downloadDrectoryPath+"/"+fileName,Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("IOException"+e);
                }
                break;
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // System.out.println("Landscape");
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            //   System.out.println("Potrait");
        }
    }
}