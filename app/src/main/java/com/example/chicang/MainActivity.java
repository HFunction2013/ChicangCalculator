package com.example.chicang;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.Editable;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.google.android.material.textfield.TextInputEditText;
import android.widget.TextView;
import com.google.android.material.card.MaterialCardView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private TextInputEditText cc_chengben, cc_shuliang, bc_jiage, bc_shuliang;
    private TextView chenbenjia, yingkuibiA, zongchicang, yingkuibiB, zongshizhi, kuisun, yingkuibiA2, yingkuibiB2;
    private MaterialCardView rescard;
    private View layout;
    private int colorRed, colorBlack, colorGreen;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initColor();
        setupTextWatchers();
        setupFocusChangeListeners();
        setupRootLayoutClickListener();
        setupFunctionButtons();
        yingkuibiA2.setText("");
        yingkuibiB2.setText("");
        rescard.setVisibility(View.GONE);
    }

    private void setupFunctionButtons() {
        findViewById(R.id.btn_clear).setOnClickListener(v -> {
            cc_chengben.setText("");
            cc_shuliang.setText("");
            bc_jiage.setText("");
            bc_shuliang.setText("");
            yingkuibiA2.setText("");
            yingkuibiB2.setText("");
            rescard.setVisibility(View.GONE);
        });
    }

    private void initViews() {
        cc_chengben = findViewById(R.id.chengben);
        cc_shuliang = findViewById(R.id.chishuliang);
        bc_jiage = findViewById(R.id.bucang);
        bc_shuliang = findViewById(R.id.bushuliang);

        chenbenjia = findViewById(R.id.chengbenjia);
        yingkuibiA = findViewById(R.id.yingkuibiA);
        zongchicang = findViewById(R.id.zongchicang);
        yingkuibiB = findViewById(R.id.yingkuibiB);
        zongshizhi = findViewById(R.id.zongshizhi);
        kuisun = findViewById(R.id.kuisun);
        yingkuibiA2 = findViewById(R.id.yingkuibiA2);
        yingkuibiB2 = findViewById(R.id.yingkuibiB2);

        rescard = findViewById(R.id.cardResult);
        layout = findViewById(android.R.id.content);
    }
    private void initColor() {
        colorRed = ContextCompat.getColor(this, R.color.red);
        colorBlack = ContextCompat.getColor(this, R.color.black);
        colorGreen = ContextCompat.getColor(this, R.color.green);
    }
    private void setupTextWatchers() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                calculate();
            }
        };
        cc_chengben.addTextChangedListener(textWatcher);
        cc_shuliang.addTextChangedListener(textWatcher);
        bc_jiage.addTextChangedListener(textWatcher);
        bc_shuliang.addTextChangedListener(textWatcher);
    }
    private void setupFocusChangeListeners() {
        View.OnFocusChangeListener focusChangeListener = (v, hasFocus) -> {
            if (!hasFocus) {
                hideKeyboard(v);
            }
        };
        cc_chengben.setOnFocusChangeListener(focusChangeListener);
        cc_shuliang.setOnFocusChangeListener(focusChangeListener);
        bc_jiage.setOnFocusChangeListener(focusChangeListener);
        bc_shuliang.setOnFocusChangeListener(focusChangeListener);
    }
    private void setupRootLayoutClickListener() {
        layout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                v.performClick();
            }
            return true;
        });
    }
    private void setStockStyleText(TextView textView, String label, double value, String format, int col) {

        String valueStr = String.format(format, value);
        String fullText = label + valueStr;

        SpannableString spannable = new SpannableString(fullText);
        int valueStart = fullText.indexOf(valueStr);
        int valueEnd = valueStart + valueStr.length();

        spannable.setSpan(new ForegroundColorSpan(col), valueStart, valueEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        textView.setText(spannable);
    }
@SuppressLint("DefaultLocale")
    private void calculate(){
        try {
            double v1 = getDoubleFromEditText(cc_chengben);
            double v2 = getDoubleFromEditText(cc_shuliang);
            double v3 = getDoubleFromEditText(bc_jiage);
            double v4 = getDoubleFromEditText(bc_shuliang);
            if (
                    Double.isNaN(v1) ||
                    Double.isNaN(v2) ||
                    Double.isNaN(v3) ||
                    Double.isNaN(v4)
            ){
                yingkuibiA2.setText("");
                yingkuibiB2.setText("");
                rescard.setVisibility(View.GONE);
                return;
            }
            if (v1 > 0 || v2 > 0 || v3 > 0 || v4 > 0){
                rescard.setVisibility(View.VISIBLE);
                // chenbenjia, yingkuibiA, zongchicang, yingkuibiB, zongshizhi, kuisun;
                double zsz = v3 * (v2 + v4);
                double cbj = (v1 * v2 + v3 * v4) / (v2 + v4);
                double ykbA = (v3 - v1) / v1 * 100;
                double zcc = v2 + v4;
                double ks = v3 * v2 - v1 * v2;
                double ykbB = ks / (v1 * v2 + v3 * v4) * 100;

                double ykbA2 = (v1 - v3) / v3 * 100;
                double ykbB2 = (cbj - v3) / v3 * 100;

                setStockStyleText(chenbenjia, getString(R.string.chengbenjia) + ": ", cbj, "%.2f", colorBlack);
                setStockStyleText(yingkuibiA, getString(R.string.yingkuibiA) + ": ", ykbA, "%.2f%%", (ykbA < 0 ? colorGreen : colorRed));
                setStockStyleText(zongchicang, getString(R.string.zongchicang) + ": ", zcc, "%.0f", colorBlack);
                setStockStyleText(yingkuibiB, getString(R.string.yingkuibiB) + ": ", ykbB, "%.2f%%", (ykbB < 0 ? colorGreen : colorRed));
                setStockStyleText(zongshizhi, getString(R.string.zongshizhi) + ": ", zsz, "%.2f", colorBlack);
                setStockStyleText(kuisun, getString(R.string.kuisun) + ": ", ks, "%.2f", (ks < 0 ? colorGreen : colorRed));
                setStockStyleText(yingkuibiA2, "", ykbA2, "%.2f%%", (ykbA2 < 0 ? colorGreen : colorRed));
                setStockStyleText(yingkuibiB2, "", ykbB2, "%.2f%%", (ykbA2 < 0 ? colorGreen : colorRed));
            }
            else {
                yingkuibiA2.setText("");
                yingkuibiB2.setText("");
                rescard.setVisibility(View.GONE);
            }

        } catch (Exception e) {
            e.printStackTrace();
            yingkuibiA2.setText("");
            yingkuibiB2.setText("");
            rescard.setVisibility(View.GONE);
        }
    }
    private double getDoubleFromEditText(TextInputEditText editText) {
        String text = Objects.requireNonNull(editText.getText()).toString();
        if (TextUtils.isEmpty(text)) {
            return Double.NaN;
        }
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void hideKeyboardAndClearFocus() {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            currentFocus.clearFocus();
            hideKeyboard(currentFocus);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View currentFocus = getCurrentFocus();
            if (currentFocus instanceof TextInputEditText) {
                if (!isPointInsideView(ev.getRawX(), ev.getRawY(), currentFocus)) {
                    hideKeyboardAndClearFocus();
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private boolean isPointInsideView(float x, float y, View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int viewX = location[0];
        int viewY = location[1];
        return (x >= viewX && x <= (viewX + view.getWidth()) &&
                y >= viewY && y <= (viewY + view.getHeight()));
    }
}