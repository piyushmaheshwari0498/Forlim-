package com.example.forlempopoli.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.forlempopoli.R;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

public class ProductImageActivity extends AppCompatActivity {
    PhotoViewAttacher mAttacher;
    String filename;
    String image_URL;
    String url = "http://forlimpopoli.in/beta_mobile/public/uploads/articles/no-photo.jpg";
    private ProgressBar progressBar;
    private ImageView load_Image;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_image);
        load_Image = findViewById(R.id.load_Image);
        progressBar = findViewById(R.id.progressBar);
        getSupportActionBar().setLogo(R.drawable.logo_1);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }

        Intent intent = getIntent();
        filename = intent.getStringExtra("position");
        image_URL = "http://forlimpopoli.in/beta_mobile/public/uploads/articles/" + filename;

        if (url.equals(image_URL)) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getApplicationContext(), "Image does not exist", Toast.LENGTH_SHORT).show();
        } else {
            Glide.with(this)
                    .load(image_URL)
                    .centerCrop()
                    .placeholder(R.drawable.progress_animation)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.drawable.ic_baseline_error_outline_24)
                    .into(load_Image);

//            Picasso.get().load(image_URL).into(load_Image);
            mAttacher = new PhotoViewAttacher(load_Image);
            mAttacher.setZoomable(true);
            progressBar.setVisibility(View.GONE);
            System.out.println("load_Image " + image_URL);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
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
