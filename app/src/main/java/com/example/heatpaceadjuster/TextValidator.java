package com.example.heatpaceadjuster;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import java.util.regex.Pattern;

/*
Not mine. Credit: https://stackoverflow.com/questions/2763022/android-how-can-i-validate-edittext-input/11838715#11838715
 */

public abstract class TextValidator implements TextWatcher {
    private final TextView textView;

    private static String regexValidPaceExpression = "^([0-9]?[0-9]?:[0-5][0-9]?)|([0-9][0-9]?)|(:[0-9][0-9]?)|(:)|([0-9][0-9]?:)$";

    public TextValidator(TextView textView) {
        this.textView = textView;
    }

    public abstract void validate(TextView textView, String text);

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();
        int length = text.length();

        if(length > 0 && !Pattern.matches(regexValidPaceExpression, text)) {
            s.delete(length - 1, length);
        }
    }

    @Override
    final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

    @Override
    final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
}