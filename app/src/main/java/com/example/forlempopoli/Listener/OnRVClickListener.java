package com.example.forlempopoli.Listener;

import android.widget.RadioGroup;

public interface OnRVClickListener {
    void onRadioChange(RadioGroup radioGroup, int checkedId);

    void onClickData(String value);

    public void onItemClick();
}
