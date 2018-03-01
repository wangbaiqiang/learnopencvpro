package com.hipad.firstopencvapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.FileNotFoundException;
import java.io.InputStream;

public class DisplayActivity extends AppCompatActivity {
    private final int SELECT_PHOTO = 1;
    private ImageView ivImage, ivImageProcessed;
    Mat src;
    static int ACTION_MODE=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispaly);
        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivImageProcessed=(ImageView)findViewById(R.id.ivImageProcessed);
        Intent intent = getIntent();
        if(intent.hasExtra("ACTION_MODE")){
            ACTION_MODE=intent.getIntExtra("ACTION_MODE",0);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.action_load_image){
            Intent photoPickerIntent=new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent,SELECT_PHOTO);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
             case SELECT_PHOTO:
                 if (resultCode== RESULT_OK){
                     try {
                         Uri imageUri = data.getData();
                         InputStream imageStream = getContentResolver().openInputStream(imageUri);
                         Bitmap selectImage = BitmapFactory.decodeStream(imageStream);
                         //在c代码中imread读取图片文件 参数二 flag 表示载入图片的类型，flags>0 3通道 flags=0返回灰度图像 小于0 返回包含alpha通道的图像
                         //cv_8uc4 8u表示8位无符号， c4 4个通道表示。
                         src=new Mat(selectImage.getHeight(),selectImage.getWidth(), CvType.CV_8UC4);
                         //将位图对象转成mat对象数据
                         Utils.bitmapToMat(selectImage, src);
                         switch (ACTION_MODE){
                             // TODO: 2018/2/28 根据不同的模式实现不同的操作
                             case MainActivity.MEAN_BLUR:
                                 Imgproc.blur(src,src,new Size(3,3));
                                 break;
                             case MainActivity.GAUSSIAN_BLUR:
                                 Imgproc.GaussianBlur(src,src,new Size(3,3),0);
                                 break;
                             case MainActivity.MEDIAN_BLUR:
                                 Imgproc.medianBlur(src,src,3);
                                 break;
                             case MainActivity.DILATE:
                                 Mat kernelDilate = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
                                 Imgproc.dilate(src,src,kernelDilate);
                                 break;
                             case MainActivity.ERODE:
                                 Mat kernelErode = Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(5, 5));
                                 Imgproc.erode(src,src,kernelErode);
                                 break;



                             //实现图像的操作后在将mat转成bitmap位图，在imageview中显示操作后的图像目标图像
                             //还要在imageview中载入原始图像
                         }
                         Bitmap processedImage = Bitmap.createBitmap(src.cols(), src.rows(), Bitmap.Config.ARGB_8888);
                         Utils.matToBitmap(src,processedImage);
                         ivImage.setImageBitmap(selectImage);
                         ivImageProcessed.setImageBitmap(processedImage);
                     } catch (FileNotFoundException e) {
                         e.printStackTrace();
                     }
                 }
             break;
             default:
             break;
        }
    }
}
