package com.pdl.app.tongue_app;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;


import com.pdl.app.tongue_app.ImagesProcessing.ImagesProc;

/**
 * Created by pc on 2018/3/12.
 */

public class MyHandler extends Handler{
    private ImageView imageView=null;
    private TextView textView=null;
    public MyHandler(ImageView imageView,TextView textView){
        this.imageView=imageView;
        this.textView=textView;
    }
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        int msgId = msg.what;
        switch (msgId) {
            case 1:
                //获取Bitmap对象
                //接下来的部分是图像的处理部分
                BitmapFactory.Options options=new BitmapFactory.Options();
                options.inJustDecodeBounds=true;//(设为true 图片不加入内存效率高)
                BitmapFactory.decodeFile((String) msg.obj,options);
                int outWidth = options.outWidth;
                int outHeight = options.outHeight;
                Bitmap bm= BitmapFactory.decodeFile((String) msg.obj);
                bm =  ThumbnailUtils.extractThumbnail(bm,outWidth,outHeight);

                //此处为简单的判断，判断照片是都大于要切割的大小
                int width=bm.getWidth();
                int height=bm.getHeight();
                if(width<240||height<300){
                    //Toast.makeText(CameraActivity.this, "照片的大小不符合规则", Toast.LENGTH_SHORT).show();
                    return ;
                }

                //图片处理部分
                ImagesProc imgPro=new ImagesProc(bm);
                if(imgPro==null){
                    Log.i("pdl_handler","imgPro is null");
                }
                //显示切割后的图片
                if(imageView==null){
                    Log.i("pdl_handler","imageView is null");
                }
                imageView.setImageBitmap(imgPro.createImage(imgPro.getSlicedPixArray()));
                String info="检测结果：\n";
                int G=imgPro.getAverG();
                int H=imgPro.getAverH();
                //暂时未粗略的判断
                if(H<110) {
                    info+="     黄苔（H<110）！\n";
                    info+="平均H值为："+H+"      "+"平均G值为："+G;
                }
                if(H<260&&H>=110) {
                    info+="     白苔（260>H>=110）！\n";
                    info+="平均H值为："+H+"      "+"平均G值为："+G;
                }
                if(H>=260) {
                    info+="     正常苔（H>=260）！\n";
                    info+="平均H值为："+H+"      "+"平均G值为："+G;
                }
                textView.setText(info);
                break;
            default:
                break;
        }
    }

}
