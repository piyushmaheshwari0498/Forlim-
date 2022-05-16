package com.fashion.forlempopoli.terms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.fashion.forlempopoli.R;

public class TermsDetailsActivity extends AppCompatActivity {

    String name;
    String header;
    String content;
    TextView tv_header;
    TextView tv_content;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_details);
        tv_header = findViewById(R.id.tv_header);
        tv_content = findViewById(R.id.tv_content);


        name = getIntent().getStringExtra("terms");
        header = getIntent().getStringExtra("header");
        content = getIntent().getStringExtra("content");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.logo_1);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }

        tv_header.setText(header);
        tv_content.setText(content);
    }

    @SuppressLint("NewApi")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}