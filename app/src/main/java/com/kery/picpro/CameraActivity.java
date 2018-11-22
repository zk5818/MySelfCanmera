package com.kery.picpro;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.io.BufferedOutputStream;

public class CameraActivity extends Activity {
    RectOnCamera rectOnCamera;
    CustomCameraPreview cameraPreview;
    ImageView take_light;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        rectOnCamera = (RectOnCamera) findViewById(R.id.rect);
        cameraPreview = (CustomCameraPreview) findViewById(R.id.cameprv);
        take_light = findViewById(R.id.take_light);
        rectOnCamera.setCamera(cameraPreview);

        take_light.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraPreview.changeLight();
            }
        });


        rectOnCamera.setPicListener(new RectOnCamera.TakePicLsn() {
            @Override
            public void takePicture() {
                cameraPreview.takePhoto(jpeg);
            }
        });

    }

    //创建jpeg图片回调数据对象
    private Camera.PictureCallback jpeg = new Camera.PictureCallback() {
        String filePath;

        @Override
        public void onPictureTaken(byte[] data, Camera Camera) {
            Intent intent = new Intent();
            intent.putExtra("data", data);
            setResult(100, intent);
            finish();


        }
//            BufferedOutputStream bos = null;
//            Bitmap bm = null;
//            try {
//                // 获得图片
//                bm = BitmapFactory.decodeByteArray(data, 0, data.length);
//                ((CameraActivity) mContext).rtPath(filePath);
//                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                    Log.i(TAG, "Environment.getExternalStorageDirectory()=" + Environment.getExternalStorageDirectory());
//                    filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + System.currentTimeMillis() + ".jpg";//照片保存路径
//                    File file = new File(filePath);
//                    if (!file.exists()) {
//                        file.createNewFile();
//                    }
//                    bos = new BufferedOutputStream(new FileOutputStream(file));
//                    bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩到流中
//                    ((CameraActivity) mContext).rtPath(filePath);
//                } else {
//                    Toast.makeText(mContext, "没有检测到内存卡", Toast.LENGTH_SHORT).show();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            finally {
//                try {
//                    bos.flush();//输出
//                    bos.close();//关闭
//                    bm.recycle();// 回收bitmap空间
//                    ((CameraActivity) mContext).rtPath(filePath);
////                    mCamera.stopPreview();// 关闭预览
////                    mCamera.startPreview();// 开启预览
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

//        }
    };

}
