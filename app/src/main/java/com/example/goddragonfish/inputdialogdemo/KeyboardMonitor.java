package com.example.goddragonfish.inputdialogdemo;

/**
 * Created by GodDragonFish on 2018/1/24.
 */

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by GuangzhouRD on 2018/1/24.
 */

public class KeyboardMonitor implements ViewTreeObserver.OnGlobalLayoutListener{

    private View contentView;
    private KeyBoardShowListener keyBoardShowListener;
    private int oldInvisible;
    private int screenHeight;

    public KeyboardMonitor(Activity activity) {
        contentView=activity.findViewById(android.R.id.content);
    }

    @Override
    public void onGlobalLayout() {
        try {
            Rect rect = new Rect();
            contentView.getWindowVisibleDisplayFrame(rect);
            screenHeight=contentView.getRootView().getHeight();
            int keyboardHeight = contentView.getRootView().getHeight() - rect.bottom;
            if (oldInvisible == keyboardHeight) return;
            oldInvisible = keyboardHeight;
            if (keyboardHeight > 150) {
                keyBoardShowListener.onKeyboardShow(true,screenHeight,keyboardHeight);
            } else {
                keyBoardShowListener.onKeyboardShow(false,screenHeight,keyboardHeight);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setKeyBoardListener(KeyBoardShowListener listener) {
        this.keyBoardShowListener = listener;
        addGlobalListener();
    }

    /**
     * 添加OnGlobalLayoutListener监听
     */
    private void addGlobalListener() {
        try {
            if (contentView != null) {
                contentView.getViewTreeObserver().addOnGlobalLayoutListener(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 移除OnGlobalLayoutListener监听
     */
    public void removeGlobalListener() {
        try {
            if (contentView != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    contentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface KeyBoardShowListener{
        void onKeyboardShow(boolean isShow,int screenHeight,int keyboardHeight);
    }
}