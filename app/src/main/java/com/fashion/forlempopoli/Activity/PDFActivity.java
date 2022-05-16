package com.fashion.forlempopoli.Activity;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Environment.DIRECTORY_DOCUMENTS;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.appcompat.widget.ShareActionProvider;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.downloader.Error;
import com.downloader.OnDownloadListener;
import com.downloader.PRDownloader;
import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Utilities.Constants;
import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.channels.FileChannel;

public class PDFActivity extends AppCompatActivity {
    PDFView pdfView;
    ProgressBar progressBar;
    int position;
    String smId, request_pdf;
    String fileName;
    File downloadedFile;
    String directoryPath;
    String filecreatedname;
    Toolbar toolbar;
    String rootDirectoryPath;
    File destinationFileDirectory;
    private ShareActionProvider mShareActionProvider;

    public static String getRootDirPath(Context context) {
        String folderName = "Forlimpopoli_Files";
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

            if (!rootFolder.exists()) {
                boolean isFolderCreated = rootFolder.mkdir();
                System.out.println("Is Folder Created in Internal Storage" + isFolderCreated);
            }
            return rootFolder.getAbsolutePath();
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f);
        pdfView = findViewById(R.id.pdfView);
        progressBar = findViewById(R.id.progressBar);
        toolbar = findViewById(R.id.toolbar);
//        toolbar.setTitle("Forlem Popoli-Invoice");
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.logo_1);
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
        fileName = "invoice_" + smId + ".pdf";
        rootDirectoryPath = getRootDirPath(this);
        System.out.println("Root_Directory_Path" + rootDirectoryPath);
        downloadPdfFromInternet(request_pdf, getRootDirPath(this), fileName);
    }

    private void downloadPdfFromInternet(String url, final String dirPath, final String fileName) {
        PRDownloader.download(url, dirPath, fileName).build().start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                // Toast.makeText(getApplicationContext(), "downloadComplete", Toast.LENGTH_LONG).show();
                directoryPath = dirPath;
                filecreatedname = fileName;
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
        pdfView.fromFile(file)
                .password(null)
                .defaultPage(0)
                .enableSwipe(true)
                .swipeHorizontal(false)
                .enableDoubletap(true)
                .load();
    }

    public void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            System.out.println("Has_File_Sucessfully_Moved " + src.renameTo(dst));
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
        getMenuInflater().inflate(R.menu.menu_share, menu);

        getMenuInflater().inflate(R.menu.menu_download, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.menu_item_share:
                File fileToShare = new File(rootDirectoryPath, fileName);
                if (fileToShare == null || !fileToShare.exists()) {
                    Toast.makeText(this, R.string.text_generated_file_error, Toast.LENGTH_SHORT).show();
                    return super.onOptionsItemSelected(item);
                }

                Intent intentShareFile = new Intent(Intent.ACTION_SEND);

                Uri apkURI = FileProvider.getUriForFile(getApplicationContext(),
                        getApplicationContext()
                                .getPackageName() + ".provider", fileToShare);
                intentShareFile.setDataAndType(apkURI, URLConnection.guessContentTypeFromName(fileToShare.getName()));
                intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                intentShareFile.putExtra(Intent.EXTRA_STREAM, apkURI);

                startActivity(Intent.createChooser(intentShareFile, "Share File"));
//        }
                break;
            case R.id.action_download:
                if (Build.VERSION_CODES.R > Build.VERSION.SDK_INT) {
                    destinationFileDirectory = new File(Environment.getExternalStorageDirectory().getPath()
                            + "//ForlimDocument//Invoices");
                } else {
                    destinationFileDirectory = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS).getPath()
                            + "//ForlimDocument//Invoices");
                }
                if (!destinationFileDirectory.exists()) {
                    destinationFileDirectory.mkdir();
                }
                fileName = "invoice_" + smId + ".pdf";
                rootDirectoryPath = getRootDirPath(this);
                System.out.println("Root_Directory_Path" + rootDirectoryPath);

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(request_pdf));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                }
                request.setDestinationInExternalFilesDir(this, destinationFileDirectory.getAbsolutePath(), fileName);
                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                request.setMimeType("application/pdf");
                request.allowScanningByMediaScanner();
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
                downloadManager.enqueue(request);

                PRDownloader.download(request_pdf, destinationFileDirectory.getAbsolutePath(), fileName).build().start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        directoryPath = destinationFileDirectory.getAbsolutePath();
                        filecreatedname = fileName;
                        downloadedFile = new File(destinationFileDirectory, fileName);
                        System.out.println("ondownloadedFile" + downloadedFile);
                        progressBar.setVisibility(View.GONE);
//                        Toast.makeText(getApplicationContext(), "PDF Downloaded Successfully\n" + destinationFileDirectory.getAbsolutePath() + "/" + fileName, Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onError(Error error) {
                        Toast.makeText(getApplicationContext(), "Error in downloading file : $error", Toast.LENGTH_LONG).show();
                    }
                });


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


