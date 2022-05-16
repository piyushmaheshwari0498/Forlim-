package com.fashion.forlempopoli.Model;

import android.content.Context;

import com.fashion.forlempopoli.R;

import java.util.ArrayList;
import java.util.List;

public class Terms {
    String term_name;
    String term_id;
    String term_header;
    String term_content;

    public Terms() {
    }

    public Terms(String term_name, String term_id, String term_header, String term_content) {
        this.term_name = term_name;
        this.term_id = term_id;
        this.term_header = term_header;
        this.term_content = term_content;
    }

    public String getTerm_name() {
        return term_name;
    }

    public void setTerm_name(String term_name) {
        this.term_name = term_name;
    }

    public String getTerm_id() {
        return term_id;
    }

    public void setTerm_id(String term_id) {
        this.term_id = term_id;
    }

    public String getTerm_header() {
        return term_header;
    }

    public void setTerm_header(String term_header) {
        this.term_header = term_header;
    }

    public String getTerm_content() {
        return term_content;
    }

    public void setTerm_content(String term_content) {
        this.term_content = term_content;
    }

    public List<Terms> getTermList(Context context){
        List<Terms> termsList = new ArrayList<>();
        termsList.add(new Terms("About Us","1", context.getString(R.string.about_header),context.getString(R.string.about_content)));
        termsList.add(new Terms("Contact Us","2", context.getString(R.string.contact_header),context.getString(R.string.contact_content)));
        termsList.add(new Terms("Disclaimer","3", context.getString(R.string.disclaimer_header),context.getString(R.string.disclaimer_content)));
        termsList.add(new Terms("Privacy Policy","4", context.getString(R.string.privacy_header),context.getString(R.string.privacy_content)));
        termsList.add(new Terms("Terms and Condition","5", context.getString(R.string.terms_header),context.getString(R.string.terms_content)));
        termsList.add(new Terms("Shipping, Cancellation and Refund","6", context.getString(R.string.shipping_header),context.getString(R.string.shipping_content)));


        return termsList;
    }
}
