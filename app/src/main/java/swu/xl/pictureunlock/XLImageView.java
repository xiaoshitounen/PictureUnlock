package swu.xl.pictureunlock;

import android.content.Context;

import androidx.appcompat.widget.AppCompatImageView;

public class XLImageView extends AppCompatImageView {
    //资源变量
    private int rightImageResource = 0;
    private int wrongImageResource = 0;

    //构造方法
    public XLImageView(Context context) {
        super(context);
    }

    //设置视图显示的图片
    public void changeImage(int resid){
        this.setImageResource(resid);
    }

    //getter/setter方法
    public int getRightImageResource() {
        return rightImageResource;
    }

    public void setRightImageResource(int rightImageResource) {
        this.rightImageResource = rightImageResource;
    }

    public int getWrongImageResource() {
        return wrongImageResource;
    }

    public void setWrongImageResource(int wrongImageResource) {
        this.wrongImageResource = wrongImageResource;
    }
}
