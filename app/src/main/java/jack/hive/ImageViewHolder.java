package jack.hive;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by zjchai on 16/9/10.
 */
public class ImageViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView textView ;
    MainActivity context;


    String[][] appName = {
            {"com.huayinghealth.protecteyes", "com.huayinghealth.protecteyes.MainActivity"},
            {"com.hewei.DictORWordHD", "com.hewei.DictORWordHD.view.LoadingAcitivity"},
            {"cn.k12cloud.k12cloud2cv3.yibin", "cn.k12cloud.k12cloud2cv3.activity.LauncherActivity_"},
            {"com.clovsoft.huayinghealth.student", "com.clovsoft.smartclass.student.MainActivity"},
            {"com.hy.toolbox", "com.hy.toolbox.activity.LaunchAPPActivity"},
            {"com.application.classschedule", "com.application.classschedule.MainActivity"},
            {"com.android.settings", "com.android.settings.Settings"}
    };

    public ImageViewHolder(View itemView, MainActivity context) {
        super(itemView);
        imageView = (ImageView) itemView.findViewById(R.id.img_img);
        textView = (TextView) itemView.findViewById(R.id.number);
        this.context = context;
    }

    public void bind(Integer resId, final int position) {
        Bitmap bitmap = BitmapCache.INSTANCE.getBitmap(resId);
        HiveDrawable drawable = new HiveDrawable(HiveLayoutManager.HORIZONTAL,bitmap);
        imageView.setImageDrawable(drawable);
        textView.setText(String.valueOf(position));


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                String pkg = null;
                String cls = null;
                if (position == 10) {
                    pkg = appName[0][0];
                    cls = appName[0][1];
                } else if(position == 3) {
                    pkg = appName[1][0];
                    cls = appName[1][1];
                } else if(position == 2) {
                    pkg = appName[2][0];
                    cls = appName[2][1];
                } else if(position == 0) {
                    pkg = appName[3][0];
                    cls = appName[3][1];
                } else if(position == 5) {
                    pkg = appName[4][0];
                    cls = appName[4][1];
                } else if(position == 6) {
                    pkg = appName[5][0];
                    cls = appName[5][1];
                } else if(position == 16) {
                    pkg = appName[6][0];
                    cls = appName[6][1];
                }

                ComponentName comp = new ComponentName(pkg, cls);
                intent.setComponent(comp);
                intent.setAction("android.intent.action.MAIN");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                context.overridePendingTransition(R.anim.window_in,R.anim.window_out);
            }
        });

    }
}
