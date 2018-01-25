package com.example.goddragonfish.inputdialogdemo;

import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by GodDragonFish on 2018/1/25.
 */

public class EditTextListenerUtil {
    public static TextWatcher EdTextListener(final EditText ed_text, final ImageView delete) {
        ed_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    delete.setVisibility(View.VISIBLE);
                } else {
                    delete.setVisibility(View.GONE);
                }

            }
        });

        TextWatcher mTextWatcher = new TextWatcher() {
            private CharSequence temp;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
//				Logger.i("aaaa", "长度:" + temp.length());
                if (temp.length() > 0) {
                    delete.setVisibility(View.VISIBLE);
                } else {
                    delete.setVisibility(View.GONE);
                }
            }
        };
        return mTextWatcher;
    }




    public static TextWatcher EdTextListener(final EditText ed_text, final ImageView delete, final CheckBox cb) {
        ed_text.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                try {
                    if(hasFocus) {
                        cb.setVisibility(View.VISIBLE);
                    }else {
                        delete.setVisibility(View.GONE);
                        cb.setVisibility(View.GONE);
                    }
                    if (hasFocus && ed_text.getText().toString().trim().length()>0) {
                        delete.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        TextWatcher mTextWatcher = new TextWatcher() {
            private CharSequence temp;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                temp = s;
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // Log.i("aaaa", "长度:" + temp.length());
                if (temp == null) {
                    return;
                }
                if (temp.length() > 0) {
                    delete.setVisibility(View.VISIBLE);
                    cb.setVisibility(View.VISIBLE);
                } else {
                    delete.setVisibility(View.GONE);
                    cb.setVisibility(View.GONE);
                }
            }
        };
        return mTextWatcher;
    }


    //设置密码是否可见，且定位光标焦点在最后，即使输入框为空但只要获取到焦点也要显示cb控件
    public static void isEnableVisible(CheckBox cb,EditText edt){
        if (cb.isChecked()) {
            //设置EditText的密码为可见的
            edt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            if(edt.isFocusable()){
                cb.setVisibility(View.VISIBLE);
            }
            String str = edt.getText().toString().trim();
            //调整光标到最后一行
            edt.setSelection(str.length());
        } else {
            //设置密码为隐藏的
            edt.setTransformationMethod(PasswordTransformationMethod.getInstance());
            if(edt.isFocusable()){
                cb.setVisibility(View.VISIBLE);
            }
            String str = edt.getText().toString().trim();
            edt.setSelection(str.length());
        }
    }

    //删除密码框中的内容，且显示是否可见的图标
    public static void onDelete(EditText edt,CheckBox cb){
        edt.setText("");
        cb.setVisibility(View.VISIBLE);
    }
}
