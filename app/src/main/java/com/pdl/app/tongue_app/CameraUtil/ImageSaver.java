package com.pdl.app.tongue_app.CameraUtil;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;

//保存图像
    public  class ImageSaver implements Runnable {

        private Image mImage;
        private String imagePath="";
        private Handler handler=null;
        public ImageSaver(Image image,Handler handler) {
            this.mImage = image;
            this.handler=handler;
        }
        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            Bitmap bit= BitmapFactory.decodeByteArray(data,0,data.length);
            //图片存储的路径
            String path = Environment.getExternalStorageDirectory() + "/DCIM/CameraV2/";
            File mImageFile = new File(path);
            if (!mImageFile.exists()) {
                mImageFile.mkdir();
            }
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = path + "IMG_" + timeStamp + ".jpg";
            imagePath=fileName;
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(fileName);
                fos.write(data);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            //发送消息给主线程就行图片的显示
            Message message = new Message();//发送消息
            message.what = 1;
            message.obj=imagePath;
            handler.sendMessage(message);
        }
    }