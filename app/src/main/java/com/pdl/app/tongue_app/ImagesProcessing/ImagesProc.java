package com.pdl.app.tongue_app.ImagesProcessing;

import android.graphics.Bitmap;

import java.util.Random;

/**
 * Created by asus-pc on 2017/10/4.
 */

public class ImagesProc {

    private Bitmap bitmap;
    private int height, width;//图片的高和宽
    private int w,h;//切割后的图片的高和宽
    private int[][] pixGreenRGBArray;//RGB Green值矩阵
    private int[][] pixHHSVArray;//HSV H值矩阵

    private int sampleW,sampleH;//选取的样本矩阵的宽和高，此处选的是3*3的矩阵点
    private int sampleNum;//取样的样本数量


    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void setSampleW(int sampleW) {
        this.sampleW = sampleW;
    }

    public void setSampleH(int sampleH) {
        this.sampleH = sampleH;
    }

    public void setSampleNum(int sampleNum) {
        this.sampleNum = sampleNum;
    }

    public ImagesProc(Bitmap bitmap) {
        this.bitmap = bitmap;
        ImagesProcinit();
    }

    private int getWidth() {
        return bitmap.getWidth();
    }

    private int getHeight() {
        return bitmap.getHeight();
    }


    //此处的初始化函数可以利用重载构成传餐改变属性值设定值
    private void ImagesProcinit() {
        this.width = getWidth();
        this.height = getHeight();
        this.w=240;//切割图片的宽
        this.h=300;//切割图片的宽
        this.sampleH=3;//取样本的时候选取3*3的矩阵
        this.sampleW=3;
        sampleNum=100;//取样的次数
        this.pixGreenRGBArray = new int[w][h];
        this.pixHHSVArray=new int[w][h];
        pixGreenRGBArray=this.getGreenPixArray();//得到Green值的矩阵
        pixHHSVArray=this.getHHSVValueArray();//得到H值值的矩阵
    }

    /**
     *在矩阵matrix中随机取sampleNum个sampleW*sampleH矩阵
     * 求其平均值
     * @return
     */
    private int getAverValue(int[][]matrix){
        //此处的a,b要根据取样方式做确定
        int a=sampleW/2;
        int b=sampleH/2;
        int w=matrix.length;
        int h=matrix[0].length;
        /*
            采取的随机取样方式  每个样品九个像素点
             (x-1,y-1)  (x,y-1)  (x+1,y-1)
             (x-1,y)    (x,y)    (x+1,y)
             (x-1,y+1)  (x,y+1)  (x+1,y+1)
            其中（x,y）为随机生成的取样点
        */

        int sumOfGreenValue=0;//100存绿色的RGB值的总值
        Random random=new Random();
        //sampleNum取样的总数
        for (int i = 0; i < sampleNum; i++) {
            int x= random.nextInt(w-2*a)+1;//可以取样的宽为w-2*a
            int y= random.nextInt(h-2*b)+1;//可以取样的高h-2*b
            /*
                求
                  G(x-1,y-1)  G(x,y-1)  G(x+1,y-1)
                 G(x-1,y)    G(x,y)    G(x+1,y)
                 G(x-1,y+1)  G(x,y+1)  G(x+1,y+1)
                 各点值和的平均值
                aver= [G(x-1,y-1)+G(x,y-1)+G(x+1,y-1)+G(x-1,y)+G(x,y)
                            +G(x+1,y)+G(x-1,y+1)+G(x,y+1)+G(x+1,y+1)]/9
             */
            int aver=(matrix[x-1][y-1]+matrix[x][y-1]+matrix[x+1][y-1]+
                      matrix[x-1][y]  +matrix[x][y]  +matrix[x+1][y]+
                      matrix[x-1][y+1]+matrix[x][y+1]+matrix[x+1][y+1])/9;
            sumOfGreenValue+=aver;
        }
        return sumOfGreenValue/sampleNum;
    }



    //得到切割后的图像像素矩阵
    public int[][] getSlicedPixArray() {

        int[][] matrix=new int[w][h];
        int startX=width/2-w/2;
        int startY=height/2-h/2;
        int endX=width/2+w/2;
        int endY=height/2+h/2;

        for (int i =startY; i < endY; i++)
            for (int j=startX ; j < endX; j++) {
                int argb = bitmap.getPixel(j, i);
                matrix[j-startX][i-startY] =argb;
            }
        return  matrix;
    }
    //选取的区域为图片的中央w*h的范围
    //取得选取区域内的像素的Green RGB值存入矩阵
    /*选取的矩行区域左上角坐标：x=width/2-w/2  y=height/2-h/2
      选取的矩行区域右下角坐标：x=width/2+w/2  y=height/2+h/2
     */
    private int[][] getGreenPixArray() {

        int[][] greenArray=new int[w][h];
        int startX=width/2-w/2;
        int startY=height/2-h/2;
        int endX=width/2+w/2;
        int endY=height/2+h/2;
        //此循环选出图片最中央的w*h区
        for (int i =startY; i < endY; i++)
            for (int j=startX ; j < endX; j++) {
                int argb = bitmap.getPixel(j, i);
               // int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
               // int b = (argb >> 0) & 0xFF;
               // int gray = (int) (r + g + b) / 3;
                greenArray[j-startX][i-startY] = g;
            }
        return  greenArray;
    }

    //得到切割区域HSV中的H值的矩阵
    //
    private int[][] getHHSVValueArray() {
        int[][] greenArray=new int[w][h];
        int startX=width/2-w/2;
        int startY=height/2-h/2;
        int endX=width/2+w/2;
        int endY=height/2+h/2;

        //此循环选出图片最中央的w*h区域
        for (int i =startY; i < endY; i++)
            for (int j=startX ; j < endX; j++) {
                int argb = bitmap.getPixel(j, i);
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = (argb >> 0) & 0xFF;

                //取三个值中的最大值  //1.max=max(R,G,B)
                double max=r>g?r:g;
                max=max>b?max:b;
                //取三个值中的最小值   //1.min=min(R,G,B)
                double min=r<g?r:g;
                min=min<b?min:b;
                //需要考虑max-min=0的情况
                double H=0;//HSV中的H值
                if(max!=min) {
                    if (r == max) H = (g - b) / (max - min);
                    if (g == max) H = 2 + (b - r) / (max - min);
                    if (b == max) H = 4 + (r - g) / (max - min);
                }else{
                    H=0;
                }
                H*=60;
                if(H<0) H+=360;
                //int V=max;
                //int S=(max-min)/max;
                greenArray[j-startX][i-startY] = (int)H;
            }
        return  greenArray;
    }


    //取得样本得出的平均H值
    public int getAverH(){
        return getAverValue(pixHHSVArray);
    }

    //取得样本得出的平均值
    public int getAverG(){
        return getAverValue(pixGreenRGBArray);
    }


    //由像素矩阵生成处理后的Bitmap
    public Bitmap createImage(int[][]grayMatrix)
    {
        int w=grayMatrix.length;
        int h = grayMatrix[0].length;
        Bitmap bt=Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        for(int i=0;i<h;i++)
            for(int j=0;j<w;j++) {
                bt.setPixel(j, i, grayMatrix[j][i]);
            }
        return bt;
    }


}
