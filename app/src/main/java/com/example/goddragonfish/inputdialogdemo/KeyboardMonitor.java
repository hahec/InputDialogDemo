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
    private int excVisableHeight; //前一状态的不可见区域高度
    private int huaWeiBottomBarHeight=-1; //华为底部的Bar高度

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
            //以手机屏幕左上角为原点（包括状态栏），获取除了状态栏的可见矩形，华为的rect.bottom不包括底部bar
            mContentView.getWindowVisibleDisplayFrame(rect);
            //PhoneWindow的高度,这构造函数里获取会为0,因为在onCreate()里获取压根还没绘制View,onResume()之后才绘制
            screenHeight=mContentView.getRootView().getHeight();
            //华为的heightPixels=screenHeight-huaweibottomBarHeight;  rect.bottom不包括底部bar
            if(huaWeiBottomBarHeight<0){
                huaWeiBottomBarHeight=screenHeight-rect.bottom;
            }
            int inVisableHeight = screenHeight - rect.bottom;
            //防止不必要的keyBoardShowListener监听（比如关屏幕后开启屏幕）
            if(excVisableHeight==inVisableHeight)return;
            excVisableHeight=inVisableHeight;
            if (inVisableHeight > huaWeiBottomBarHeight) {
                //软键盘显示中
                keyBoardShowListener.onKeyboardShow(true,screenHeight,inVisableHeight);
            } else {
                ////软键盘没有显示
                keyBoardShowListener.onKeyboardShow(false,screenHeight,inVisableHeight);
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
        void onKeyboardShow(boolean isShowing,int screenHeight,int inVisableHeight);
    }
}