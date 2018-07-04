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

import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import jack.services.LocationService;
import jack.utils.Constant;
import jack.utils.SystemShare;

import static jack.hive.R.id.iv_weather;

public class MainActivity extends AppCompatActivity implements WeatherSearch.OnWeatherSearchListener {

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
    private int userImageId;
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
                userImageId = R.drawable.man;
            }
            iv_touxiang.setImageResource(userImageId);
            tv_school.setText(schoolName);
            tv_grade.setText(gradeName+className);
            tv_name.setText(userName);
        }
    };


    /**
    天气
     */
    private WeatherSearchQuery mquery;
    private WeatherSearch mweathersearch;
    private LocalWeatherLive weatherlive;
    private TextView tv_address,tv_weather,tv_temp;
    private String address;
    private ImageView iv_weather;

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



        Intent service = new Intent(context, LocationService.class);
        context.startService(service);


        //天气
        //检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST
        mweathersearch=new WeatherSearch(this);
        mweathersearch.setOnWeatherSearchListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        updateWeather();
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
        iv_touxiang = (ImageView) findViewById(R.id.iv_touxiang);
        userImageId = R.drawable.man;
		
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_weather = (TextView) findViewById(R.id.tv_weather);
        tv_temp = (TextView) findViewById(R.id.tv_temp);
        iv_weather = (ImageView) findViewById(R.id.iv_weather);
        
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
    //定时器更新天气
    Timer timer = new Timer();
    private static int period = 60 * 60 * 1000; //60分钟一次
    private void updateWeather(){
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                address = SystemShare.getSettingString(context, Constant.Pad_address);
                if (address!=null && !"".equals(address)) {
                    //天气
                    //检索参数为城市和天气类型，实况天气为WEATHER_TYPE_LIVE、天气预报为WEATHER_TYPE_FORECAST
                    mquery = new WeatherSearchQuery(address, WeatherSearchQuery.WEATHER_TYPE_LIVE);
                    mweathersearch.setQuery(mquery);
                    mweathersearch.searchWeatherAsyn(); //异步搜索
                }
            }
        },1000,period);
    }
    // 停止定时器
    private void stopTimer(){
        if(timer != null){
            timer.cancel();
            // 一定设置为null，否则定时器不会被回收
            timer = null;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        stopTimer();
    }
    @Override
    public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
        if (rCode == 1000) {
            if (weatherLiveResult != null&&weatherLiveResult.getLiveResult() != null) {
                weatherlive = weatherLiveResult.getLiveResult();
                /*reporttime1.setText(weatherlive.getReportTime()+"发布");
                weather.setText(weatherlive.getWeather());
                Temperature.setText(weatherlive.getTemperature()+"°");
                wind.setText(weatherlive.getWindDirection()+"风     "+weatherlive.getWindPower()+"级");
                humidity.setText("湿度         "+weatherlive.getHumidity()+"%");*/
                tv_address.setText(address);
                tv_weather.setText(weatherlive.getWeather());
                tv_temp.setText(weatherlive.getTemperature()+"°");
                setImg(weatherlive.getWeather());
                Toast.makeText(context, weatherlive.getTemperature()+"°",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(context, "weatherLiveResult is null",Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(context, "code = "+rCode,Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {

    }

    private  void setImg(String weather){
        //iv_weather.setBackgroundResource(R.mipmap.iv_weather_qing);
        if (weather != null && "".equals(weather)){
            if (weather.contains(getString(R.string.daxue))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weaher_daxue);
            }else if (weather.contains(getString(R.string.baoxue))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_baoxue);
            }else if (weather.contains(getString(R.string.baoyu))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_baoyu);
            }else if (weather.contains(getString(R.string.bingbao))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_bingbao);
            }else if (weather.contains(getString(R.string.dabaoyu))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_dabaoyu);
            }else if (weather.contains(getString(R.string.dayu))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_dayu);
            }else if (weather.contains(getString(R.string.dongyu))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_dongyu);
            }else if (weather.contains(getString(R.string.duoyun))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_duoyun);
            }else if (weather.contains(getString(R.string.fuchen))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_fuchen);
            }else if (weather.contains(getString(R.string.leizhenyu))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_leizhenyu);
            }else if (weather.contains(getString(R.string.mai))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_mai);
            }else if (weather.contains(getString(R.string.qiangshachengbao))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_qiangshachengbao);
            }else if (weather.contains(getString(R.string.qing))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_qing);
            }else if (weather.contains(getString(R.string.shachenbao))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_shachenbao);
            }else if (weather.contains(getString(R.string.tedabaoyu))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_tedabaoyu);
            }else if (weather.contains(getString(R.string.wu))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_wu);
            }else if (weather.contains(getString(R.string.xiaoxue))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_xiaoxue);
            }else if (weather.contains(getString(R.string.xiaoyu))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_xiaoyu);
            }else if (weather.contains(getString(R.string.yangsha))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_yangsha);
            }else if (weather.contains(getString(R.string.ying))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_ying);
            }else if (weather.contains(getString(R.string.yujiaxue))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_yujiaxue);
            }else if (weather.contains(getString(R.string.zhenxue))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_zhenxue);
            }else if (weather.contains(getString(R.string.zhenyu))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_zhenyu);
            }else if (weather.contains(getString(R.string.zhongxue))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_zhongxue);
            }else if (weather.contains(getString(R.string.zhongyu))){
                iv_weather.setBackgroundResource(R.mipmap.iv_weather_zhongyu);
            }
        }
    }
}
