package com.example.goddragonfish.inputdialogdemo;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity {


    private RelativeLayout rlAcc;
    private RelativeLayout rlPsw;
    private InputMethodManager inputMethodManager;
    private KeyboardMonitor keyboardMonitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final RelativeLayout rlBind=findViewById(R.id.rl_bind);
        final InputMethodManager im=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        final ImageView iv1=findViewById(R.id.iv_del_acc_input);
        final ImageView iv2=findViewById(R.id.iv_del_psw_input);
        final EditText et1=findViewById(R.id.et_stu_account);
        final EditText et2=findViewById(R.id.et_stu_psw);
        final Button btn=findViewById(R.id.btn);

        rlAcc=findViewById(R.id.rl_acc);
        rlPsw=findViewById(R.id.rl_psw);
        inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et1.setText("");
            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et2.setText("");
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this,"click",Toast.LENGTH_SHORT).show();
            }
        });

        //防止软件软键盘遮住点击按钮
        keyboardMonitor = new KeyboardMonitor(this);
        keyboardMonitor.setKeyBoardListener(new KeyboardMonitor.KeyBoardShowListener(){
            @Override
            public void onKeyboardShow(boolean isShowing,int screenHeight,int keyboardHeight) {
                if (isShowing) {
                    //btn.getHitRect(rect);   //以decorView左上为原点
                    //btn.getLocalVisibleRect(rect);   //以decorView左上为原点
                    //btn.getGlobalVisibleRect(rect);   //以屏幕左上为原点
//                    int height=getResources().getDisplayMetrics().heightPixels;
//                    Log.e("h",Integer.toString(height));
                    int[] locs=new int[2];
                    //以屏幕左上为原点,getLocationInWindow效果一样，因为当前只有一个PhoneWindow，没有其他window（对话框）
                    btn.getLocationOnScreen(locs);
                    int btn2Bottom=screenHeight-locs[1]-btn.getHeight();//按钮底部到屏幕底部距离
                    if(keyboardHeight>btn2Bottom){
                        rlBind.scrollTo(0, keyboardHeight-btn2Bottom+dp2px(MainActivity.this,5));
                    }

                } else {
                    rlBind.scrollTo(0, 0);
                    Log.e("h","lalala");
//                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewPager.getLayoutParams();
//                    params.topMargin = DensityUtil.dip2px(mContext, 35);
//                    viewPager.setLayoutParams(params);
                }
            }
        });



        /*
        法3（最low）：当EditText获取焦点，点击其他任何地方都不会隐藏。
        所以，对顶层布局设置点击事件，点击就隐藏软键盘，点击旁边的删除输入imageView也不消失。
        缺点：点击titleBar不能隐藏，可能需要手动隐藏状态栏
         */
//        rlBind.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(im!=null){
//                    im.hideSoftInputFromWindow(rlBind.getWindowToken(),0);
//
//                }
//            }
//        });


    }

    @Override
    protected void onDestroy() {
        if(keyboardMonitor!=null){
            keyboardMonitor.removeGlobalListener();
        }
        super.onDestroy();
    }


    //法1、2实际是同一原理，通过重写事件分发dispatchTouchEvent()，动态的根据触摸的范围，按需隐藏
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        //法1：
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            if(isHideInputKeyBoard(ev,rlAcc,rlPsw)){
                if(inputMethodManager!=null){
                    inputMethodManager.hideSoftInputFromWindow(rlAcc.getWindowToken(),0);
                    inputMethodManager.hideSoftInputFromWindow(rlPsw.getWindowToken(),0);
                }
            }
        }
        //1、getWindow()->PhoneWindow（Window抽象类的唯一实现类）对象，
        //PhoneWindow.superDispatchTouchEvent(ev)->mDecor.dispatchTouchEvent(ev)
        //mDecor也就是view顶级容器，意思是真正的开始view的事件分发了；
        //2、onTouchEvent(ev)，当所有的事件都没有被拦截与消耗，最终交给Activity消耗
        return getWindow().superDispatchTouchEvent(ev)||onTouchEvent(ev);


        /*//法2：
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(isHideInputKeyBoard(ev,rlAcc,rlPsw)){
                    if(inputMethodManager!=null){
                        inputMethodManager.hideSoftInputFromWindow(rlAcc.getWindowToken(),0);
                        inputMethodManager.hideSoftInputFromWindow(rlPsw.getWindowToken(),0);
                    }
                }
                break;
        }
        return super.dispatchTouchEvent(ev);*/

    }


    public boolean isHideInputKeyBoard(MotionEvent motionEvent,View... vArray){
        if(vArray!=null&&vArray.length>0){
            for (View v:vArray) {
                int[] loc={0,0};
                //此时效果与v.getLocationOnScreen(loc);一样，getX()与getRawX()一样，
                //如果在onTouch中属于某个组件的OnTouchListener接口，点击组件获取的就不同，X相对，
                //RawX绝对坐标（左上角，不管titleBar）
                v.getLocationInWindow(loc);
                int left=loc[0];
                int top=loc[1];
                int bottom=top+v.getHeight();
                int right=left+v.getWidth();
                //判断当前触摸是否在View内，这里传入的View是EditText的父布局RelativeLayout
                if(motionEvent.getX()>left&&motionEvent.getX()<right
                        &&motionEvent.getY()>top&&motionEvent.getY()<bottom){
                    return false;
                }
            }
        }
        return true;
    }

    public int px2dp(Context context, int px) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density + 0.5f);
    }
    public int dp2px(Context context,int dp){
        float density =context.getResources().getDisplayMetrics().density;
        return (int)(dp*density+0.5);
    }


}
