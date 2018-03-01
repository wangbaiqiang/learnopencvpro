package com.hipad.firstopencvapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;

public class MainActivity extends AppCompatActivity {

    public static final int MEAN_BLUR = 1;
    public static final int GAUSSIAN_BLUR = 2;
    public static final int MEDIAN_BLUR = 3;
    public static final int DILATE = 4;
    public static final int ERODE = 5;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("opencv_java3");
        System.loadLibrary("native-lib");
    }
    private BaseLoaderCallback mOpenCVCallBack=new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch(status){
                 case LoaderCallbackInterface.SUCCESS:
                     // TODO: 2018/2/27 完成我们自己的操作
                 break;
                 default:
                     super.onManagerConnected(status);
                 break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bMean = (Button) findViewById(R.id.bMean);
        bMean.setOnClickListener((v)->handleClick(v));
    }

    /**
     * 处理bMean的点击事件
     * @param v
     */
    private void handleClick(View v) {
        Intent intent=new Intent(getApplicationContext(),DisplayActivity.class);
        //均值模糊
//        intent.putExtra("ACTION_MODE", MEAN_BLUR);
        //高斯模糊
//        intent.putExtra("ACTION_MODE", GAUSSIAN_BLUR);
        //中值模糊
//        intent.putExtra("ACTION_MODE", MEDIAN_BLUR);
        //膨胀
        intent.putExtra("ACTION_MODE", DILATE);
        //腐蚀
//        intent.putExtra("ACTION_MODE", DILATE);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
//        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0,this,mOpenCVCallBack);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();
}
