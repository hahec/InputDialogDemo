package com.example.goddragonfish.inputdialogdemo;

/**
 * Created by GodDragonFish on 2018/1/24.
 */

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by GuangzhouRD on 2018/1/24.
 */

public class KeyboardMonitor implements ViewTreeObserver.OnGlobalLayoutListener{


    private View mContentView;
    private KeyBoardShowListener keyBoardShowListener;
    private int screenHeight;
    private int excVisableHeight; //前一状态的可见区域高度

    public KeyboardMonitor(Activity activity) {
        mContentView=activity.findViewById(android.R.id.content);//获取contentView
    }

    /**
     * Activity->PhoneWindow->DecorView(不包括状态栏)->(TitleView+ContentView)
     */
    @Override
    public void onGlobalLayout() {
        try {
            Rect rect = new Rect();
            //以手机屏幕左上角为原点（包括状态栏），获取除了状态栏的可见矩形
            mContentView.getWindowVisibleDisplayFrame(rect);
            screenHeight=mContentView.getRootView().getHeight();//decorView的高度
            int keyboardHeight = mContentView.getRootView().getHeight() - rect.bottom;
            //防止不必要的keyBoardShowListener监听（比如关屏幕后开启屏幕）
            if(excVisableHeight==keyboardHeight)return;
            excVisableHeight=keyboardHeight;

            if (keyboardHeight > 0) {
                //软键盘显示中
                keyBoardShowListener.onKeyboardShow(true,screenHeight,keyboardHeight);
            } else {
                ////软键盘没有显示
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
            if (mContentView != null) {
                mContentView.getViewTreeObserver().addOnGlobalLayoutListener(this);
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
            if (mContentView != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mContentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface KeyBoardShowListener{
        void onKeyboardShow(boolean isShowing,int screenHeight,int keyboardHeight);
    }
}