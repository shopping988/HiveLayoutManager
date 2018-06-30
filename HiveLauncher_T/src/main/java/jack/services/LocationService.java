package jack.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Timer;
import java.util.TimerTask;

import jack.utils.DateUtil;
import jack.utils.NetUtil;

/**
 * Created by ChanLin on 2018/6/11.
 * ProtectEyes
 * TODO:
 */
public class LocationService extends Service implements AMapLocationListener {

    public static final long LOCATION_UPDATE_MIN_TIME = 5 *60 * 1000;
    public static final float LOCATION_UPDATE_MIN_DISTANCE = 5;


    public static int lactionCode = 0;
    public static String address = null;
    public static double longitude = 0;
    public static double latitude = 0;

    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mapLocationClient = null;
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                lactionCode = aMapLocation.getErrorCode();
                if (aMapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。

                    latitude = aMapLocation.getLatitude();
                    longitude = aMapLocation.getLongitude();
                    address = aMapLocation.getAddress();
                    Log.e("locationservice",longitude+"\t"+latitude+"\t"+address);
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError","location Error, ErrCode:"
                            + aMapLocation.getErrorCode() + ", errInfo:"
                            + aMapLocation.getErrorInfo());
                }
            }
        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mapLocationClient = new AMapLocationClient(getApplicationContext());
        mapLocationClient.setLocationListener(mLocationListener);
        mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        mLocationOption.setOnceLocation(false);//多次定位
        mLocationOption.setInterval(LOCATION_UPDATE_MIN_TIME);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mapLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mapLocationClient.startLocation();

        //TelephonyManager telephonyManager = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        imei = getIMEI(this);

        //创建定时器
        pushData();
    }

    public static final String getIMEI(Context context) {
        try {
            //实例化TelephonyManager对象
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //获取IMEI号
            String imei = telephonyManager.getDeviceId();
            //在次做个验证，也不是什么时候都能获取到的啊
            if (imei == null) {
                imei = "";
            }
            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }
    String imei;
    Timer timer = new Timer();
    private static int delay = 10000;
    private static String host = "http://paddms.huayingtek.com/padSocket/";
    public void pushData(){

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                Log.e("lzp", "timer excute");

                try {
                    if (!NetUtil.isNetworkAvailable(getApplicationContext())){
                        return;
                    }
                    URL postUrl = new URL(host + "/padController/updatePadAddress.do");
                    HttpURLConnection connection = (HttpURLConnection) postUrl.openConnection();
                    // 设置通用的请求属性
                    connection.setRequestProperty("accept", "*/*");
                     connection.setRequestProperty("connection", "Keep-Alive");
                    connection.setRequestProperty("user-agent",
                                       "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");
                    connection.setUseCaches(false);
                    connection.setInstanceFollowRedirects(true);
                    connection.connect();
                    DataOutputStream out = new DataOutputStream(connection
                            .getOutputStream());
                    String content = "imei="+imei
                            + "&longitude="+longitude
                            + "&latitude="+latitude
                            + "&address="+address
                            + "&date="+ DateUtil.getDate();
                    Log.i("content",content);

                    out.write(content.getBytes());

                    out.flush();
                    out.close();
                    //响应
                    int resultCode=connection.getResponseCode();
                    if(HttpURLConnection.HTTP_OK==resultCode){
                        StringBuffer sb=new StringBuffer();
                        String readLine=new String();
                        BufferedReader responseReader=new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
                        while((readLine=responseReader.readLine())!=null){
                            sb.append(readLine).append("\n");
                        }
                        responseReader.close();
                        Log.e("locationservice",sb.toString());
                    }
                    connection.disconnect();




                } catch (IOException e) {
                    e.printStackTrace();
                }

              /*  //发送POST请求
                try {
                    String content = URLEncoder.encode("imei="+imei
                               + "&longitude="+longitude
                               + "&latitude="+latitude
                                + "&address="+address, "UTF-8");
                    String s1 = sendPost(host + "/padController/updatePadAddress.do" ,
                            content);
                    System.out.println(s1);
                } catch (IOException e) {
                      e.printStackTrace();
                   }*/
                }
        }, delay, LOCATION_UPDATE_MIN_TIME);


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
        stopTimer();
    }
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

    }


    /**
     * 向指定URL发送POST方法的请求
     *
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是name1=value1&name2=value2的形式。
     * @return URL所代表远程资源的响应
     */
    public String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += "/n" + line;
            }
        } catch (Exception e) {
            System.out.println("发送POST请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
