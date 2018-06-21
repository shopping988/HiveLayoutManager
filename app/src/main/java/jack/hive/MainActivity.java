package jack.hive;

import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    public static final int[] resIds = new int[]{
            R.drawable.img_0
            , R.drawable.img_1
            , R.drawable.img_2
            , R.drawable.img_3
            , R.drawable.img_4
            , R.drawable.img_5
            , R.drawable.img_6
            , R.drawable.img_7
            , R.drawable.img_8
            , R.drawable.img_9
            , R.drawable.img_10
            , R.drawable.img_11
            , R.drawable.img_12
    };
    private static final String TAG = MainActivity.class.getSimpleName();

    RecyclerView recyclerView;
    HiveLayoutManager layoutManager;
    HiveAdapter adapter;
    int index = 17;
    int timeWidgetID = 66;
    int dateWidgetID = 88;

    private FrameLayout timeFrameLayout;
    private FrameLayout dateFrameLayout;
    private AppWidgetManager appWidgetManager;
    private AppWidgetHost appWidgetHost;

    private static final int APPWIDGET_HOST_ID = 0x100;

    String timePackageName = "com.android.deskclock";
    //String timePackageName = "com.hy.digitclock";
    String timeWidgetClassName = "com.android.alarmclock.DigitalAppWidgetProvider";
    //String timeWidgetClassName = "com.hy.digitclock.DigitClock";
    String timeAppClassName = "com.android.deskclock.DeskClock";
    ComponentName timeWidgetComponentName = new ComponentName(timePackageName, timeWidgetClassName);
    ComponentName timeAppComponentName = new ComponentName(timePackageName, timeAppClassName);

    String datePackageName = "com.android.calendar";
    String dateWidgetClassName = "com.android.calendar.widget.CalendarAppWidgetProvider";
    String dateAppClassName = "com.android.calendar.AllInOneActivity";
    ComponentName dateWidgetComponentName = new ComponentName(datePackageName, dateWidgetClassName);
    ComponentName dateAppComponentName = new ComponentName(datePackageName, dateAppClassName);

    Context context;
    private HashMap<ComponentName, AppWidgetProviderInfo> mWidgetMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);
        appWidgetManager = AppWidgetManager.getInstance(context);
        AppWidgetProviderInfo timeAppWidgetInfo = findAppWidgetProviderInfo(timeWidgetComponentName);
        AppWidgetProviderInfo dateAppWidgetInfo = findAppWidgetProviderInfo(dateWidgetComponentName);

        appWidgetHost = new AppWidgetHost(this, APPWIDGET_HOST_ID);
        appWidgetHost.startListening();

        initObjects();
        initViews();
        afterViews();

        if (timeWidgetID == 0)
            timeWidgetID = appWidgetHost.allocateAppWidgetId();
        if (dateWidgetID == 0)
            dateWidgetID = appWidgetHost.allocateAppWidgetId();

        Log.v("shopping", "timeWidgetID = " + timeWidgetID);
        Log.v("shopping", "dateWidgetID = " + dateWidgetID);

        View timeHostView = appWidgetHost.createView(this, timeWidgetID, timeAppWidgetInfo);
        timeFrameLayout = (FrameLayout) findViewById(R.id.time_layout);
        timeFrameLayout.addView(timeHostView);
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

        View dateHostView = appWidgetHost.createView(this, dateWidgetID, dateAppWidgetInfo);
        dateFrameLayout = (FrameLayout) findViewById(R.id.date_layout);
        dateFrameLayout.addView(dateHostView);
        dateFrameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setComponent(dateAppComponentName);
                intent.setAction("android.intent.action.MAIN");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void initObjects() {
        BitmapCache.INSTANCE.init(this, 200 * 200 * 4 * 13);
        adapter = new HiveAdapter(this);
    }

    private void initViews() {
        recyclerView = (RecyclerView) findViewById(R.id.list);
        /*findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int r = getRandomPosition();
                Log.d(TAG, "onClick: r" + r);
                adapter.addData(resIds[index % resIds.length], r);
                adapter.notifyItemInserted(r);
                index++;
            }
        });
        findViewById(R.id.remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getItemCount() != 0) {
                    int r = getRandomPosition();
                    Log.d(TAG, "onClick: r" + r);
                    adapter.remove(r);
                    adapter.notifyItemRemoved(r);
                    index--;
                }
            }
        });
        findViewById(R.id.move).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int r = getRandomPosition();
                int r2 = getRandomPosition();
                Log.d(TAG, "onClick: r" + r);
                adapter.move(r, r2);
                adapter.notifyItemMoved(r, r2);
            }
        });*/

//        recyclerView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int position = recyclerView.getChildAdapterPosition(view);
//                Log.v("shopping", "position = " + position);
//                if (position == 0) {
//                    Intent intent = new Intent();
//                    ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings");
//                    intent.setComponent(comp);
//                    intent.setAction("android.intent.action.MAIN");
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                }
//            }
//        });
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
}
