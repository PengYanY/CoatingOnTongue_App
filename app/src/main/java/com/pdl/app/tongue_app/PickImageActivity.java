package com.pdl.app.tongue_app;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.pdl.app.tongue_app.ImagesProcessing.ImagesProc;

import java.io.File;



public class PickImageActivity extends AppCompatActivity{

    /**request Code 从相册选择照片不裁切**/
    private final static int IMAGE=126;
    private Uri imageUri;

    private ImageView imageView=null;
    private Button button=null;
    private TextView textView=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("pdl_pick","onCreate");
        setContentView(R.layout.activity_pick_image);
        imageView = (ImageView) findViewById(R.id.photo);
        button = (Button) findViewById(R.id.picButton);
        textView=(TextView)findViewById(R.id.textview1);
        imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "test.jpg"));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Log.i("pdl_pick","onClick");
                intent.setAction(Intent.ACTION_PICK);//Pick an item from the data
                intent.setType("image/*");//从所有图片中进行选择
                startActivityForResult(intent, IMAGE);//requestCode
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            Log.i("pdl_pick","onActivityResult");
            Uri selectedImage = data.getData();
            String[] filePathColumns = {MediaStore.Images.Media.DATA};
            Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
            c.moveToFirst();
            int columnIndex = c.getColumnIndex(filePathColumns[0]);
            String imagePath = c.getString(columnIndex);
            showImage(imagePath);
            c.close();
        }

    }
    //加载图片
    private void showImage(String imagePath){
        Log.i("pdl_pick","imagePath:"+imagePath);
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        //此处为简单的判断，判断照片是都大于要切割的大小

        if(bm==null){
            Log.i("pdl_pick","bm is null");
            return ;
        }
        int width=bm.getWidth();
        int height=bm.getHeight();

        if(width<240||height<300){
            Toast.makeText(this, "照片的大小不符合规则", Toast.LENGTH_SHORT).show();
            return ;
        }

        ImagesProc imgPro=new ImagesProc(bm);
        Log.i("pdl_pick","new ImagesProc(bm)");


        //显示切割后的图片
        imageView.setImageBitmap(imgPro.createImage(imgPro.getSlicedPixArray()));
        String info="检测结果：\n";
        int G=imgPro.getAverG();
        int H=imgPro.getAverH();
        //暂时未粗略的判断
        if(H<110) {
            info+="     黄苔（H<110）！\n";
            info+="     平均H值为："+H+"      "+"平均G值为："+G;
        }
        if(H<260&&H>=110) {
            info+="     白苔（260>H>=110）！\n";
            info+="     平均H值为："+H+"      "+"平均G值为："+G;
        }
        if(H>=260) {
            info+="     正常苔（H>=260）！\n";
            info+="     平均H值为："+H+"      "+"平均G值为："+G;
        }
        textView.setText(info);
    }
}
