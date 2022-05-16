package com.fashion.forlempopoli.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fashion.forlempopoli.R;
import com.fashion.forlempopoli.Utilities.Constants;

public class FilterActivity extends AppCompatActivity {
    private RadioGroup radioArticleNoGroup,radioPriceGroup;
    private RadioButton radioArticleNo1,radioArticleNo2,radioArticleNo3,radioArticleNo4,radioArticleNo5,radioArticleNo6,
            radioPrice1,radioPrice2,radioPrice3;
    int selectedId;
    Button clearButton,applyButton;
    private int selectedPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        setTitle(Html.fromHtml("<font color='#FFFFFF'><small>Filters</small> </font>"));
        final Drawable upArrow = getResources().getDrawable(R.drawable.ic_arrow_back_24dp);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(), R.color.colorPrimary));
        }
        radioArticleNoGroup=findViewById(R.id.radioArticleNoGroup);
        radioArticleNo1=findViewById(R.id.radioArticleNo1);
        radioArticleNo2=findViewById(R.id.radioArticleNo2);
        radioArticleNo3=findViewById(R.id.radioArticleNo3);
        radioArticleNo4=findViewById(R.id.radioArticleNo4);
        radioArticleNo5=findViewById(R.id.radioArticleNo5);
        radioArticleNo6=findViewById(R.id.radioArticleNo6);

        radioPriceGroup=findViewById(R.id.radioPriceGroup);
        radioPrice1=findViewById(R.id.radioPrice1);
        radioPrice2=findViewById(R.id.radioPrice2);
        radioPrice3=findViewById(R.id.radioPrice3);

        clearButton=findViewById(R.id.clearButton);
        applyButton=findViewById(R.id.applyButton);

         applyButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 selectedId=radioArticleNoGroup.getCheckedRadioButtonId();
                 selectedPrice=radioPriceGroup.getCheckedRadioButtonId();
                 String selectedArticle=getSelectedArticleGroup(selectedId);
                 String selectedPriceObtained=getSelectedPrice(selectedPrice);
                 System.out.println("Selected Id "+selectedId);
                 Intent resultIntent=new Intent();
                 resultIntent.putExtra(Constants.INTENT_KEYS.KEY_FILTER_ARTICLE_NO,selectedArticle);
                 resultIntent.putExtra(Constants.INTENT_KEYS.KEY_FILTER_PRICE,selectedPriceObtained);
                 setResult(RESULT_OK,resultIntent);
                 finish();
             }
         });
         clearButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

             }
         });
    }

    private String getSelectedArticleGroup(int selectedId) {
         String selectedArticle="-1";
         switch (selectedId)
         {
             case R.id.radioArticleNo1:
                  selectedArticle="SH1";
                  break;
             case R.id.radioArticleNo2:
                 selectedArticle="SH2";
                 break;
             case R.id.radioArticleNo3:
                 selectedArticle="SH3";
                 break;
             case R.id.radioArticleNo4:
                 selectedArticle="SH4";
                 break;
             case R.id.radioArticleNo5:
                 selectedArticle="SH5";
                 break;
             case R.id.radioArticleNo6:
                 selectedArticle="SH6";
                 break;
         }
         return selectedArticle;
    }

    private String getSelectedPrice(int selectedId) {
        String selectedArticle="-1";
        switch (selectedId)
        {
            case R.id.radioPrice1:
                selectedArticle="100-200";
                break;
        }
        return selectedArticle;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
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
