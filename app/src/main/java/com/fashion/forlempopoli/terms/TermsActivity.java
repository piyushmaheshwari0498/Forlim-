package com.fashion.forlempopoli.terms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.fashion.forlempopoli.Activity.CartActivity;
import com.fashion.forlempopoli.Activity.ProductActivity;
import com.fashion.forlempopoli.Activity.search.SearchProductActivity;
import com.fashion.forlempopoli.Adapter.InvoiceAdapter;
import com.fashion.forlempopoli.Adapter.TermsAdapter;
import com.fashion.forlempopoli.Model.Terms;
import com.fashion.forlempopoli.R;

public class TermsActivity extends AppCompatActivity {

    TermsAdapter adapter;
    RecyclerView recyclerView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Policy");
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.logo_1);
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }

        recyclerView = findViewById(R.id.recycler_view_terms);
        adapter = new TermsAdapter(new Terms().getTermList(this),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
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