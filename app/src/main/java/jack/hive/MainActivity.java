package jack.hive;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final int[] resIds = new int[]{
            R.drawable.img_smartclass
            , R.drawable.img_1
            , R.drawable.img_k12cloud
            , R.drawable.img_dict
            , R.drawable.img_4
            , R.drawable.img_apps
            , R.drawable.img_classschedule
            , R.drawable.img_7
            , R.drawable.img_8
            , R.drawable.img_9
            , R.drawable.img_protecteyes
            , R.drawable.img_11
            , R.drawable.img_12
            , R.drawable.img_12
            , R.drawable.img_12
            , R.drawable.img_12
            , R.drawable.img_settings
    };
    private static final String TAG = MainActivity.class.getSimpleName();

    RecyclerView recyclerView;
    HiveLayoutManager layoutManager;
    HiveAdapter adapter;
    int index = 17;
    int timeWidgetID = 66;
    //int dateWidgetID = 88;

    private FrameLayout timeFrameLayout;
   // private FrameLayout dateFrameLayout;
  //  private AppWidgetManager appWidgetManager;
    private AppWidgetHost appWidgetHost;

    private static final int APPWIDGET_HOST_ID = 0x100;

    String timePackageName = "com.android.deskclock";
    String timeWidgetClassName = "com.android.alarmclock.DigitalAppWidgetProvider";
    String timeAppClassName = "com.android.deskclock.DeskClock";

//    String timeWidgetClassName = "com.lu.weather.receiver.AppWidgetProvider_5x2";
//    String timePackageName = "com.lu.ashionweather";
//    String timeAppClassName = "com.lu.weather.activity.StartActivity";
    //private WidgetLayout mWidgetLayout;
    ComponentName timeWidgetComponentName = new ComponentName(timePackageName, timeWidgetClassName);
    ComponentName timeAppComponentName = new ComponentName(timePackageName, timeAppClassName);

    String datePackageName = "com.android.calendar";
    String dateWidgetClassName = "com.android.calendar.widget.CalendarAppWidgetProvider";
    String dateAppClassName = "com.android.calendar.AllInOneActivity";
    ComponentName dateWidgetComponentName = new ComponentName(datePackageName, dateWidgetClassName);
    ComponentName dateAppComponentName = new ComponentName(datePackageName, dateAppClassName);

    Context context;
    private HashMap<ComponentName, AppWidgetProviderInfo> mWidgetMap;

    private static final String LOGIN_ACTION = "com.clovsoft.huayinghealth.student.LOGIN";
    private static final String LOGOUT_ACTION = "com.clovsoft.huayinghealth.student.LOGOUT";
    private String schoolName = null;
    private String gradeName  = null;
    private String className  = null;
    private String userName   = null;
    private String userSex    = null;

    private ImageView iv_touxiang;
    private TextView tv_name, tv_school, tv_grade;
    private BroadcastReceiver logStateChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (LOGIN_ACTION.equals(intent.getAction())) {
                schoolName = intent.getStringExtra("school_name");
                gradeName  = intent.getStringExtra("grade_name");
                className  = intent.getStringExtra("class_name");
                userName   = intent.getStringExtra("user_name");
                userSex    = intent.getStringExtra("user_sex");
            } else if (LOGOUT_ACTION.equals(intent.getAction())) {
                schoolName = "";
                gradeName  = "";
                className  = "";
                userName   = "";
                userSex    = "";
            }
            tv_school.setText(schoolName);
            tv_grade.setText(gradeName+className);
            tv_name.setText(userName);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
       // appWidgetManager = AppWidgetManager.getInstance(context);
        AppWidgetProviderInfo timeAppWidgetInfo = findAppWidgetProviderInfo(timeWidgetComponentName);
        AppWidgetProviderInfo dateAppWidgetInfo = findAppWidgetProviderInfo(dateWidgetComponentName);

        appWidgetHost = new AppWidgetHost(this, APPWIDGET_HOST_ID);
        appWidgetHost.startListening();

        initObjects();
        initViews();
        afterViews();

        if (timeWidgetID == 0)
            timeWidgetID = appWidgetHost.allocateAppWidgetId();
//        if (dateWidgetID == 0)
//            dateWidgetID = appWidgetHost.allocateAppWidgetId();

        Log.v("shopping", "timeWidgetID = " + timeWidgetID);
    //   Log.v("shopping", "dateWidgetID = " + dateWidgetID);

        View timeHostView = appWidgetHost.createView(this, timeWidgetID, timeAppWidgetInfo);
        timeFrameLayout = (FrameLayout) findViewById(R.id.time_layout);
       // mWidgetLayout = new WidgetLayout(this);
        timeFrameLayout.addView(timeHostView);
//        mWidgetLayout.addInScreen(timeHostView, timeAppWidgetInfo.minWidth,
//                timeAppWidgetInfo.minHeight);
        timeFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setComponent(timeAppComponentName);
                intent.setAction("android.intent.action.MAIN");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

//        View dateHostView = appWidgetHost.createView(this, dateWidgetID, dateAppWidgetInfo);
//        dateFrameLayout = (FrameLayout) findViewById(R.id.date_layout);
//        dateFrameLayout.addView(dateHostView);
//        dateFrameLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setComponent(dateAppComponentName);
//                intent.setAction("android.intent.action.MAIN");
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//            }
//        });

        IntentFilter logStateFilter = new IntentFilter();
        logStateFilter.addAction(LOGIN_ACTION);
        logStateFilter.addAction(LOGOUT_ACTION);
        context.registerReceiver(logStateChange, logStateFilter);

    }

    private void initObjects() {
        BitmapCache.INSTANCE.init(this, 200 * 200 * 4 * 13);
        adapter = new HiveAdapter(this);
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.list);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_school = (TextView) findViewById(R.id.tv_school);
        tv_grade = (TextView) findViewById(R.id.tv_grade);
    }

    private int getRandomPosition() {
        int count = adapter.getItemCount();
        if (count > 0) {
            return new Random().nextInt(count);
        } else {
            return 0;
        }
    }

    private void afterViews() {
        recyclerView.setLayoutManager(layoutManager = new HiveLayoutManager(HiveLayoutManager.HORIZONTAL));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        for (int i = 0; i < index; i++) {
            adapter.addData(resIds[i % resIds.length]);
        }
        layoutManager.setGravity(HiveLayoutManager.CENTER);
//        layoutManager.setGravity(HiveLayoutManager.ALIGN_LEFT);
//        layoutManager.setGravity(HiveLayoutManager.ALIGN_RIGHT);
//        layoutManager.setGravity(HiveLayoutManager.ALIGN_TOP);
//        layoutManager.setGravity(HiveLayoutManager.ALIGN_BOTTOM);
//        layoutManager.setGravity(HiveLayoutManager.ALIGN_LEFT | HiveLayoutManager.ALIGN_TOP);
//        layoutManager.setGravity(HiveLayoutManager.ALIGN_LEFT | HiveLayoutManager.ALIGN_BOTTOM);
//        layoutManager.setGravity(HiveLayoutManager.ALIGN_RIGHT | HiveLayoutManager.ALIGN_TOP);
//        layoutManager.setGravity(HiveLayoutManager.ALIGN_RIGHT | HiveLayoutManager.ALIGN_BOTTOM);
//        layoutManager.setPadding(300, 400, 500, 600);

    }

    private AppWidgetProviderInfo findAppWidgetProviderInfo(ComponentName component) {
        if (mWidgetMap == null) {
            List<AppWidgetProviderInfo> widgets =
                    AppWidgetManager.getInstance(this).getInstalledProviders();
            mWidgetMap = new HashMap<ComponentName, AppWidgetProviderInfo>(widgets.size());
            for (AppWidgetProviderInfo info : widgets) {
                Log.v("shopping", "info = " + info);
                mWidgetMap.put(info.provider, info);
            }
        }
        return mWidgetMap.get(component);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
