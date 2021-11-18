package com.example.forlempopoli.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.forlempopoli.Entity.response.PdfResponse;
import com.example.forlempopoli.Interface.ApiInterface;
import com.example.forlempopoli.MainActivity;
import com.example.forlempopoli.R;
import com.example.forlempopoli.Sharedpreference.AppSharedPreference;
import com.example.forlempopoli.Utilities.Constants;
import com.example.forlempopoli.Utilities.RetrofitBuilder;
import com.github.barteksc.pdfviewer.PDFView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PdfFragment extends Fragment {
    int pos;
    WebView webView;
    String resulturl;
    private AppSharedPreference mAppSharedPrefernce;
    String sm_id;
    String url;
    PDFView pdfView;
    TextView tv_loading;
    String dest_file_path = "invoice.pdf";
    int downloadedSize = 0, totalsize;
    String download_file_url = "http://ilabs.uw.edu/sites/default/files/sample_0.pdf";
    float per = 0;
    public MainActivity activity;
    Uri path;
    File file;
    private static final int STORAGE_PERMISSION_CODE = 12;
    @SuppressLint("MissingSuperCall")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
      //  this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pdf, container, false);
        mAppSharedPrefernce = AppSharedPreference.getAppSharedPreference(getActivity());
       //webView = view.findViewById(R.id.WV);
       pdfView = view.findViewById(R.id.pdfView);
        tv_loading = new TextView(getActivity());
        getActivity().setContentView(tv_loading);
        tv_loading.setGravity(Gravity.CENTER);
        tv_loading.setTypeface(null, Typeface.BOLD);

//        webView.getSettings().setJavaScriptEnabled(true);
//        webView.setWebChromeClient(new WebChromeClient());
        Bundle bundle = this.getArguments();
        pos = bundle.getInt("pos", pos);
        System.out.println("position" + pos);

        Bundle bundle1 = this.getArguments();
        sm_id = bundle1.getString(Constants.INTENT_KEYS.KEY_SM_ID);
        System.out.println("PdfFragment smid" + sm_id);

//        Bundle bundle4 = this.getArguments();
//        url = bundle4.getString("request_pdf");
//        System.out.println("PdfFragment url" + url);
//        pdfView.fromUri(Uri.parse(url));
        requestStoragePermission();
        pdfDetails();
        return view;
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getActivity(), "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(getActivity(), "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void pdfDetails() {
        final ApiInterface apiInterface = RetrofitBuilder.getRetrofitInstance().create(ApiInterface.class);
        Call<PdfResponse> call = apiInterface.printPdf(getHashMap());
        call.enqueue(new Callback<PdfResponse>() {
            @Override
            public void onResponse(Call<PdfResponse> call, Response<PdfResponse> response) {
                if (response.code() == 200 && response.message().equals("OK")) {
                    if (response.body().getStatusCode() == 1 && response.body().getStatusMessage().equals("Success")) {
                        // Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        resulturl = response.body().getResultUrl();
                        url = resulturl + pos;
                        System.out.println("url"+url);
                        path = Uri.fromFile(new File(url));
                     //   downloadFile(String.valueOf(path));
                        downloadFile(url);
                       // Build.VERSION_CODES.ICE_CREAM_SANDWICH
                       // targetSdkVersion >= 24
                        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.M) {
                            //this code will be executed on devices running ICS or later
                            new Thread(new Runnable() {
                                public void run() {
                                    try {

                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                    //    intent.setDataAndType(path, "application/pdf");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        startActivity(intent);
                                    //     getActivity().finish();
                                    //    downloadFile();
                                    //    pdfView.fromUri(path);
                                    } catch (ActivityNotFoundException e) {
                                        tv_loading.setError("PDF Reader application is not installed in your device");
                                    }
                                }
                            }).start();
                        }
                        else {
                            new Thread(new Runnable() {
                                public void run() {
                                  //Uri path = Uri.fromFile(new File(url));
                                    try {
                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                      //intent.setDataAndType(path, "application/pdf");
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        startActivity(intent);
                                      //getActivity().finish();
                                      //pdfView.fromUri(path);
                                    } catch (ActivityNotFoundException e) {
                                        tv_loading.setError("PDF Reader application is not installed in your device");
                                    }
                                }
                            }).start();
                        }
                        //webView.loadUrl("http://docs.google.com/gview?embedded=true&url="+url);
                        //"http://maven.apache.org/maven-1.x/maven.pdf"
                       // startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    }
                } else {
                    if (response.code() == 404 && response.message().equals("Not Found")) {
                        try {
                            JSONObject jsonObject = new JSONObject(response.errorBody().string());
                            String message = jsonObject.getString("message");
                             Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
                            System.out.println("404" + message);
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("Some Thing Went Wrong");
                        Toast.makeText(getActivity(), "Some Thing Went Wrong !!", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<PdfResponse> call, Throwable t) {
                //ringProgressDialog.dismiss();
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
                System.out.println("onFailure getMessage" + t.getMessage());
            }
        });
    }
        private HashMap<String, Object> getHashMap() {
            HashMap<String, Object> map = new HashMap<>();
            map.put("API_ACCESS_KEY", "ZkC6BDUzxz");
            map.put("acc_id", mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
            // System.out.println("acc_id"+mAppSharedPrefernce.getString(Constants.INTENT_KEYS.KEY_ACCOUNT_ID));
            map.put("sm_id", sm_id);
            //  System.out.println("sm_id"+sm_id);
            return map;
        }
        File downloadFile(String dwnload_file_path) {
            File file = null;
            try {

           URL url = new URL(dwnload_file_path);
          //  URL url = new URL(null, dwnload_file_path, new Handler());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

  //          HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
//            urlConnection.setRequestMethod("POST");
//            urlConnection.setDoOutput(true);

                // connect
                urlConnection.connect();

                // set the path where we want to save the file
                File SDCardRoot = Environment.getExternalStorageDirectory();
                // create a new file, to save the downloaded file
                file = new File(SDCardRoot, dest_file_path);

                FileOutputStream fileOutput = new FileOutputStream(file);

                // Stream used for reading the data from the internet
                InputStream inputStream = urlConnection.getInputStream();

                // this is the total size of the file which we are
                // downloading
                totalsize = urlConnection.getContentLength();
                setText("Starting PDF download...");

                // create a buffer...
                byte[] buffer = new byte[1024 * 1024];
                int bufferLength = 0;

                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    per = ((float) downloadedSize / totalsize) * 100;
                    setText("Total PDF File size  : "
                            + (totalsize / 1024)
                            + " KB\n\nDownloading PDF " + (int) per
                            + "% complete");
                }
                // close the output stream when complete //
                fileOutput.close();
                setText("Download Complete. Open PDF Application installed in the device.");

            } catch (final MalformedURLException e) {
                System.out.println("MalformedURLException" + e);
                setTextError("Some error. Press back and try again.", Color.RED);
            } catch (final IOException e) {
                System.out.println("IOException" + e);
                setTextError("Some error occured. Press back and try again.", Color.RED);
            } catch (final Exception e) {
                System.out.println("Exception" + e);
                setTextError("Failed to download image. Please check your internet connection.", Color.RED);
            }
        return file;
    }

    void setTextError(final String message, final int color) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() { tv_loading.setTextColor(color);
            tv_loading.setText(message);
                System.out.println("message error"+message);
            }
        });

    }

    void setText(final String txt) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                tv_loading.setText(txt);
                System.out.println("setText"+txt);
            }
        });
    }
}

