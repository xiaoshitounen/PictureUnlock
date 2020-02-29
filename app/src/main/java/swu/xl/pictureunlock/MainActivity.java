package swu.xl.pictureunlock;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //合法的路线
    public static int[] legalLines = new int[]{12,14,15,23,24,25,26,35,36,45,47,48,56,57,58,59,68,69,78,89};

    //保存点的List
    private List<XLImageView> dotsView = new ArrayList<>();

    //保存线条的List
    private List<XLImageView> linesView = new ArrayList<>();

    //保存点亮的点的List
    private List<XLImageView> lightDotsView = new ArrayList<>();

    //保存点亮的线的List
    private List<XLImageView> lightLinesView = new ArrayList<>();

    //上一个被点亮的点
    private XLImageView lastLightDotView;

    //密码
    private String password = null;

    //第一次设置的密码
    private String firPassword = "";

    //临时保存密码
    private StringBuilder tempPassword = new StringBuilder();

    //SharedPreferences 的名字
    private final String SHARE_NAME = "PictureUnlock";

    //保存密码的键
    private final String PASSWORD_KEY = "password";

    //提示框
    private TextView textView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SharedPreferences
        final SharedPreferences sharedPreferences = getSharedPreferences(SHARE_NAME, Context.MODE_PRIVATE);

        //Editor
        final SharedPreferences.Editor edit = sharedPreferences.edit();

        //获取布局
        final RelativeLayout rootLayout = (RelativeLayout) findViewById(R.id.root_layout);

        //获取操作的背景视图
        final ImageView pointBg = (ImageView) findViewById(R.id.point_bg);

        //post方式得到控件坐标
        pointBg.post(new Runnable() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void run() {
                //获取屏幕的密度
                float density = getResources().getDisplayMetrics().density;

                //获取操作的视图的原点坐标
                int x = pointBg.getLeft();
                int y = pointBg.getTop();

                //创建提示框
                textView = new TextView(getApplicationContext());

                //设置文本
                password = sharedPreferences.getString(PASSWORD_KEY, null);
                textView.setText(password == null ? "请设置密码" : "请输入密码");

                //设置文本颜色
                textView.setTextColor(Color.WHITE);

                //设置文本字体大小
                textView.setTextSize(Utils.spToPx(8, density));

                //设置文本居中
                textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                //视图的布局参数 初始化的时候赋予宽度和高度
                RelativeLayout.LayoutParams myRelativeParams = new RelativeLayout.LayoutParams(
                        pointBg.getWidth(),
                        Utils.dpToPx(30, density)
                );

                //添加坐标
                myRelativeParams.leftMargin = pointBg.getLeft();
                myRelativeParams.topMargin = pointBg.getTop() + Utils.dpToPx(80, density);

                //添加到布局中
                rootLayout.addView(textView, myRelativeParams);

                //创建六个横线
                for (int i = 0; i < 6; i++) {
                    //所在第几行
                    int row = i / 2;
                    //所在第几列
                    int col = i % 2;

                    //创建显示横线的视图
                    XLImageView horLine = new XLImageView(getApplicationContext());

                    //设置两种资源id
                    horLine.setRightImageResource(R.drawable.normal_highlight1);
                    horLine.setWrongImageResource(R.drawable.wrong_highlight1);

                    //设置显示的图片
                    horLine.setImageResource(R.drawable.normal_highlight1);

                    //视图的隐藏
                    horLine.setVisibility(View.INVISIBLE);

                    //设置 id
                    horLine.setId((12 + row*33 +col*11));

                    //视图的布局参数 初始化的时候赋予宽度和高度
                    RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    //添加坐标
                    relativeParams.leftMargin = (x + Utils.dpToPx(46.6f, density)) +
                            (col * Utils.dpToPx(99, density));
                    relativeParams.topMargin = (y + Utils.dpToPx(170, density)) +
                            (row * Utils.dpToPx(99, density));

                    //添加到布局中
                    rootLayout.addView(horLine, relativeParams);

                    //保存到 List 中
                    linesView.add(horLine);
                }

                //创建六个竖线
                for (int i = 0; i < 6; i++) {
                    //所在第几行
                    int row = i / 3;
                    //所在第几列
                    int col = i % 3;

                    //创建显示竖线的视图
                    XLImageView verLine = new XLImageView(getApplicationContext());

                    //设置两种资源id
                    verLine.setRightImageResource(R.drawable.normal_highlight2);
                    verLine.setWrongImageResource(R.drawable.wrong_highlight2);

                    //设置显示的图片
                    verLine.setImageResource(R.drawable.normal_highlight2);

                    //视图的隐藏
                    verLine.setVisibility(View.INVISIBLE);

                    //设置 id
                    verLine.setId(14 + row*33 + col*11);

                    //视图的布局参数 初始化的时候赋予宽度和高度
                    RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    //添加坐标
                    relativeParams.leftMargin = (x + Utils.dpToPx(42, density)) +
                            (col * Utils.dpToPx(99, density));
                    relativeParams.topMargin = (y + Utils.dpToPx(170, density)) +
                            (row * Utils.dpToPx(99, density));

                    //添加到布局中
                    rootLayout.addView(verLine, relativeParams);

                    //保存到 List 中
                    linesView.add(verLine);
                }

                //创建八个竖线
                for (int i = 0; i < 4; i++) {
                    //所在第几行
                    int row = i / 2;
                    //所在第几列
                    int col = i % 2;

                    //创建显示左斜线的视图
                    XLImageView oblLineL = new XLImageView(getApplicationContext());

                    //设置两种资源id
                    oblLineL.setRightImageResource(R.drawable.normal_highlight3);
                    oblLineL.setWrongImageResource(R.drawable.wrong_highlight3);

                    //设置显示的图片
                    oblLineL.setImageResource(R.drawable.normal_highlight3);

                    //视图的隐藏
                    oblLineL.setVisibility(View.INVISIBLE);

                    //设置 id
                    oblLineL.setId(15 + row*33 + col*11);

                    //视图的布局参数 初始化的时候赋予宽度和高度
                    RelativeLayout.LayoutParams relativeParamsL = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    //添加坐标
                    relativeParamsL.leftMargin = (x + Utils.dpToPx(42, density)) +
                            (col * Utils.dpToPx(99, density));
                    relativeParamsL.topMargin = (y + Utils.dpToPx(170, density)) +
                            (row * Utils.dpToPx(99, density));

                    //添加到布局中
                    rootLayout.addView(oblLineL, relativeParamsL);

                    //添加到 List 中
                    linesView.add(oblLineL);

                    //创建显示左斜线的视图
                    XLImageView oblLineR = new XLImageView(getApplicationContext());

                    //设置两种资源id
                    oblLineR.setRightImageResource(R.drawable.normal_highlight4);
                    oblLineR.setWrongImageResource(R.drawable.wrong_highlight4);

                    //设置显示的图片
                    oblLineR.setImageResource(R.drawable.normal_highlight4);

                    //视图的隐藏
                    oblLineR.setVisibility(View.INVISIBLE);

                    //设置 id
                    oblLineR.setId(24 + row*33 + col*11);

                    //视图的布局参数 初始化的时候赋予宽度和高度
                    RelativeLayout.LayoutParams relativeParamsR = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    //添加坐标
                    relativeParamsR.leftMargin = (x + Utils.dpToPx(53.3f, density)) +
                            (col * Utils.dpToPx(99, density));
                    relativeParamsR.topMargin = (y + Utils.dpToPx(170, density)) +
                            (row * Utils.dpToPx(99, density));

                    //添加到布局中
                    rootLayout.addView(oblLineR, relativeParamsR);

                    //添加到 List 中
                    linesView.add(oblLineR);
                }

                //创建九个点的坐标
                for (int i = 0; i < 9; i++) {
                    //所在第几行
                    int row = i / 3;
                    //所在第几列
                    int col = i % 3;

                    //创建显示点的视图
                    XLImageView dotImgView = new XLImageView(getApplicationContext());

                    //设置两种资源id
                    dotImgView.setRightImageResource(R.drawable.selected_dot);
                    dotImgView.setWrongImageResource(R.drawable.wrong_dot);

                    //设置显示的图片
                    dotImgView.setImageResource(R.drawable.selected_dot);

                    //视图的隐藏
                    dotImgView.setVisibility(View.INVISIBLE);

                    //设置 id
                    dotImgView.setId(i+1);

                    //视图的布局参数 初始化的时候赋予宽度和高度
                    RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );

                    //添加坐标
                    relativeParams.leftMargin = (x + Utils.dpToPx(35, density)) +
                            (col * Utils.dpToPx(99, density));
                    relativeParams.topMargin = (y + Utils.dpToPx(162, density)) +
                            (row * Utils.dpToPx(99, density));

                    //添加到布局中
                    rootLayout.addView(dotImgView,relativeParams);

                    //保存到 List 中
                    dotsView.add(dotImgView);
                }

                //监听触摸事件
                pointBg.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        //触摸点的坐标 （相对于 pointBg视图 左上角）
                        float x = event.getX();
                        float y = event.getY();

                        //触摸点的坐标 （相对于 屏幕  左上角）
                        //float rawX = event.getRawX();
                        //float rawY = event.getRawY();
                        //System.out.println("X:"+x+"---"+"Y:"+y);
                        //System.out.println("RAWX:"+rawX+"---"+"RAWY:"+rawY);

                        //获取触摸的事件
                        int action = event.getAction();

                        //判断事件类型
                        if (action == MotionEvent.ACTION_DOWN){
                            //手指按下

                        }else if (action == MotionEvent.ACTION_MOVE){
                            //手指移动

                            //遍历每一个点
                            for (XLImageView dotView : dotsView) {
                                //找到每个点的范围
                                RectF rectF = new RectF();
                                rectF.set(dotView.getLeft()-pointBg.getLeft(),
                                        dotView.getTop()-pointBg.getTop(),
                                        dotView.getRight()-pointBg.getLeft(),
                                        dotView.getBottom()-pointBg.getTop()
                                );

                                //判断触摸点是否在该点范围内
                                if (rectF.contains(x, y)){

                                    //第一次点亮
                                    if (lightDotsView.size() == 0){
                                        //1.点亮
                                        dotView.setVisibility(View.VISIBLE);

                                        //2.保存到 List
                                        lightDotsView.add(dotView);

                                        //3.设置为上一个点亮的点
                                        lastLightDotView = dotView;
                                    }else{

                                        //不是第一次点亮，需要判断两个点之间是否可以连线

                                        //获取线条的id
                                        int lineId = doLineLight(lastLightDotView.getId(), dotView.getId());

                                        //判断线条是否可以被点亮
                                        if(lineId != -1){
                                            //1.点亮
                                            dotView.setVisibility(View.VISIBLE);

                                            //2.保存到 List
                                            lightDotsView.add(dotView);

                                            //3.设置为上一个点亮的点
                                            lastLightDotView = dotView;

                                            //4.点亮线条
                                            for (XLImageView lineView : linesView) {
                                                if(lineView.getId() == lineId){
                                                    //点亮
                                                    lineView.setVisibility(View.VISIBLE);

                                                    //加入到 List
                                                    lightLinesView.add(lineView);

                                                    //跳出循环
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                        }else if(action == MotionEvent.ACTION_UP){
                            //手指移开

                            //1.获得图片密码
                            for (int i = 0; i < lightDotsView.size(); i++) {
                                //记录绘制的密码
                                tempPassword.append(lightDotsView.get(i).getId());
                            }

                            //2.逻辑判断
                            if (password == null){
                                //设置密码

                                if(firPassword.length() == 0){
                                    //第一次输入密码
                                    firPassword = tempPassword.toString();

                                    textView.setText("请再次输入密码");

                                    //正确的操作
                                    rightOperation();
                                }else{
                                    //第二次输入密码
                                    if(firPassword.equals(tempPassword.toString())){
                                        //设置成功
                                        textView.setText("密码设置成功");

                                        //持久化保存
                                        edit.putString(PASSWORD_KEY, firPassword);
                                        edit.apply();

                                        //清空 第一次设置的密码
                                        firPassword = "";

                                        //System.out.println("--------"+tempPassword.toString());

                                        //正确的操作
                                        rightOperation();

                                        //跳转界面
                                        jumpNextActivity();
                                    }else{
                                        //设置失败
                                        textView.setText("密码设置失败");

                                        //错误展示
                                        wrongOperation();
                                    }
                                }
                            }else{
                                //输入密码
                                if (password.equals(tempPassword.toString())){
                                    //密码输入正确
                                    textView.setText("密码输入正确");

                                    //正确的操作
                                    rightOperation();

                                    //跳转界面
                                    jumpNextActivity();
                                }else{
                                    //密码输入失败
                                    textView.setText("密码输入失败");

                                    //System.out.println(password+"--------"+tempPassword.toString());

                                    //错误展示
                                    wrongOperation();
                                }
                            }
                        }

                        return true;
                    }
                });
            }

            /**
             * 判断两个点之间是否可以点亮
             * @param dot1
             * @param dot2
             * @return
             */
            private int doLineLight(int dot1, int dot2){
                //得到两个点连线的id
                int id = dot1>dot2 ? dot1+dot2*10 : dot1*10+dot2;

                //遍历合法的线条id
                for (int legalLine : legalLines) {
                    if(legalLine == id){
                        return id;
                    }
                }

                return -1;
            }

            /**
             * 正确的操作
             */
            private void rightOperation(){
                //0.延迟一会
                SystemClock.sleep(500);

                //1.所有的点熄灭
                for (ImageView dotView : lightDotsView) {
                    dotView.setVisibility(View.INVISIBLE);
                }
                //所有的线条熄灭
                for (ImageView lineView : linesView) {
                    lineView.setVisibility(View.INVISIBLE);
                }

                //2.清空 临时密码
                tempPassword.delete(0,tempPassword.length());

                //3.清空点亮的点的数组
                lightDotsView.clear();
                //清空点亮的线条的数组
                lightLinesView.clear();
            }

            /**
             * 错误的操作
             */
            private void wrongOperation(){
                //1.所有的点变成红色
                for (XLImageView dotView : lightDotsView) {
                    dotView.changeImage(dotView.getWrongImageResource());
                }
                //所有的线条变成红色
                for (XLImageView lineView : lightLinesView) {
                    lineView.changeImage(lineView.getWrongImageResource());
                }

                //2.延迟一会
                //SystemClock.sleep(500);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //3.所有的点熄灭
                        for (XLImageView dotView : lightDotsView) {
                            dotView.setVisibility(View.INVISIBLE);
                        }
                        //所有的线条熄灭
                        for (XLImageView lineView : lightLinesView) {
                            lineView.setVisibility(View.INVISIBLE);
                        }

                        //4.所有的点变成绿色
                        for (XLImageView dotView : lightDotsView) {
                            dotView.changeImage(dotView.getRightImageResource());
                        }
                        //所有的线条变成绿色
                        for (XLImageView lineView : lightLinesView) {
                            lineView.changeImage(lineView.getRightImageResource());
                        }

                        //5.清空 临时密码
                        tempPassword.delete(0,tempPassword.length());

                        //6.清空点亮的点的数组
                        lightDotsView.clear();
                        //清空点亮的线条的数组
                        lightLinesView.clear();
                    }
                }, 1000);
            }

            /**
             * 跳转到下一个界面
             */
            private void jumpNextActivity(){
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SecondActivity.class);
                startActivity(intent);
            }
        });

        //获取重置按钮
        Button resetBtn = (Button) findViewById(R.id.reset);

        //点击事件
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重置密码
                password = null;
                edit.putString(PASSWORD_KEY, null);
                textView.setText("请重新设置密码");
            }
        });


    }
}

