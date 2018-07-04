package jack.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeoutException;


/**
 * 网络操作类.
 *
 * @author xcl
 * @Description 提供网络操作的相关方法.
 * @date 2013-5-29
 */
public class NetUtil {

    /**
     * 响应头信息
     */
    private HashMap<String, String> responseHearders = new HashMap<String, String>();
    /**
     * 返回结果
     */
    private String result;
    /**
     * 输入流
     */
    private BufferedInputStream input;
    /**
     * http链接
     */
    private HttpURLConnection conn;
    /**
     * 输出流
     */
    private DataOutputStream output;

    /**
     * 获取当前网络链接状态.
     *
     * @param context 上下文环境.
     * @return 有任意网络通畅时返回true, 否则返回false.
     * @version 1.0
     * @createTime 2013-3-5,下午4:21:27
     * @updateTime 2013-3-5,下午4:21:27
     * @updateInfo
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE); // 获取网络服务
        if (connectivity == null) { // 判断网络服务是否为空
            return false;
        } else { // 判断当前是否有任意网络服务开启
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    /**
     * 像指定地址发送post请求提交数据.
     *
     * @param path      数据提交路径.
     * @param timeout   超时时间(毫秒).
     * @param attribute 发送请求参数,key为属性名,value为属性值.
     * @return 服务器的响应信息, 当发生错误时返回响应码.
     * @throws IOException      网络连接错误时抛出IOException.
     * @throws TimeoutException 网络连接超时时抛出TimeoutException.
     * @version 1.1
     * @createTime 2013-3-5,下午4:33:20
     * @updateTime 2013-4-17,下午3:21:56
     * @updateInfo 捕获非致命异常SocketTimeoutException同时抛出致命异常TimeoutException.
     */
    public String sendPost(String path, int timeout, HashMap<String, String> attribute) throws IOException, TimeoutException, SocketTimeoutException {
        URL url = new URL(path);
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true); // 设置输出,post请求必须设置.
        conn.setDoInput(true); // 设置输入,post请求必须设置.
        conn.setUseCaches(false); // 设置是否启用缓存,post请求不能使用缓存.
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        conn.setRequestMethod("POST");
        //设置请求体的长度
        String data = getParams(attribute);
  //      conn.setRequestProperty("Content-Length", String.valueOf(data.length()));
        conn.connect(); // 打开网络链接.
        output = new DataOutputStream(conn.getOutputStream());
        output.writeBytes(data); // 将请求参数写入网络链接.
        output.flush();
        return readResponse();
    }

    /**
     * 向指定地址发送post请求（无参数）
     *
     * @param path    post请求地址
     * @param timeout 超时时长
     * @return
     * @throws IOException            网络连接错误时抛出IOException.
     * @throws TimeoutException       网络连接错误时抛出TimeoutException.
     * @throws SocketTimeoutException
     * @version 1.0
     * @createTime 2015年6月19日, 下午2:44:36
     * @updateTime 2015年6月19日, 下午2:44:36
     * @updateInfo 捕获非致命异常SocketTimeoutException同时抛出致命异常TimeoutException.
     */
    public String sendPost(String path, int timeout) throws IOException, TimeoutException, SocketTimeoutException {
        URL url = new URL(path);
        conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true); // 设置输出,post请求必须设置.
        conn.setDoInput(true); // 设置输入,post请求必须设置.
        conn.setUseCaches(false); // 设置是否启用缓存,post请求不能使用缓存.
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        conn.setRequestMethod("POST");
        conn.connect(); // 打开网络链接.
        return readResponse();
    }

    /**
     * 像指定地址发送get请求.
     *
     * @param path    数据提交路径.
     * @param timeout 超时时间,单位为毫秒.
     * @return 服务器的响应信息, 当发生错误时返回响应码.
     * @throws IOException      网络连接错误时抛出IOException.
     * @throws TimeoutException 网络连接超时时抛出TimeoutException.
     * @version 1.1
     * @createTime 2013-3-5,下午4:40:14
     * @updateTime 2013-4-17,下午3:28:56
     * @updateInfo 捕获非致命异常SocketTimeoutException同时抛出致命异常TimeoutException.
     */
    public String sendGet(String path, int timeout) throws IOException, TimeoutException, SocketTimeoutException {
        URL url = new URL(path);
        conn = (HttpURLConnection) url.openConnection();
        conn.setUseCaches(false); // 设置是否启用缓存,post请求不能使用缓存.
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        conn.setRequestMethod("GET");
        conn.connect(); // 打开网络链接.
        return readResponse();
    }

    /**
     * 读取服务器响应信息.
     *
     * @return 服务器的响应信息, 当发生错误时返回响应码.
     * @throws IOException 读取信息发生错误时抛出IOException.
     * @version 1.0
     * @createTime 2013-3-5,下午4:48:00
     * @updateInfo
     */
    private String readResponse() throws IOException {
        int code = conn.getResponseCode();
        if (code == HttpURLConnection.HTTP_OK) { // 响应成功.
            readResponseHeaders(); // 读取响应头信息.
            input = new BufferedInputStream(conn.getInputStream());
            StringBuffer sb=new StringBuffer();
            String readLine=new String();
            BufferedReader responseReader=new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            while((readLine=responseReader.readLine())!=null){
                sb.append(readLine).append("\n");
            }
            result = sb.toString();
            Log.d("readResponse=》",result);
        } else { // 响应失败.
            return "error";
        }
        closeConnection();
        return result;
    }

    /**
     * 读取响应头信息.
     *
     * @version 1.0
     * @createTime 2014年6月17日, 下午3:20:37
     * @updateTime 2014年6月17日, 下午3:20:37
     * @updateInfo
     */
    private void readResponseHeaders() {
        Map<String, List<String>> headers = conn.getHeaderFields();
        Set<String> keys = headers.keySet();
        for (String key : keys) {
            responseHearders.put(key, conn.getHeaderField(key));
        }
    }

    /**
     * 获取响应头信息.
     *
     * @return 响应头信息.
     * @version 1.0
     * @createTime 2014年6月17日, 下午3:24:15
     * @updateTime 2014年6月17日, 下午3:24:15
     * @updateInfo
     */
    public HashMap<String, String> getResponseHeaders() {
        return responseHearders;
    }

    /**
     * 将发送请求的参数构造为指定格式.
     *
     * @param attribute 发送请求的参数,key为属性名,value为属性值.
     * @return 指定格式的请求参数.
     * @version 1.0
     * @createTime 2013-3-5,下午4:49:45
     * @updateTime 2013-3-5,下午4:49:45
     * @updateInfo
     */
    private String getParams(HashMap<String, String> attribute) {
        Set<String> keys = attribute.keySet(); // 获取所有参数名
        Iterator<String> iterator = keys.iterator(); // 将所有参数名进行跌代
        StringBuffer params = new StringBuffer();
        // 取出所有参数进行构造
        while (iterator.hasNext()) {
            try {
                String key = iterator.next();
                String param = key + "=" + URLEncoder.encode(attribute.get(key)) + "&";
                params.append(param);
            } catch (Exception e) {
                Log.e("net", "net construction params error!");
            }

        }
        // 返回构造结果
        return params.toString().substring(0, params.toString().length() - 1);
    }

    /**
     * 关闭链接与所有从链接中获得的流.
     *
     * @throws IOException 关闭发生错误时抛出IOException.
     * @version 1.0
     * @createTime 2013-3-5,下午4:51:31
     * @updateTime 2013-3-5,下午4:51:31
     * @updateInfo
     */
    private void closeConnection() throws IOException {
        if (input != null) {
            input.close();
        }
        if (output != null) {
            output.close();
        }
        if (conn != null) {
            conn.disconnect();
        }
    }

    /**
     * 下载文件,下载文件存储至指定路径.
     *
     * @param path     下载路径.
     * @param savePath 存储路径.
     * @return 下载成功返回true, 若下载失败则返回false.
     * @throws MalformedURLException 建立连接发生错误抛出MalformedURLException.
     * @throws IOException           下载过程产生错误抛出IOException.
     * @version 1.2
     * @createTime 2013-3-6,下午4:10:13
     * @updateTime 2013-5-29,下午4:56:43
     * @updateInfo 取消图片的下载后缀, 取消文件下载（除.jpg文件外）的tmp流程.
     */
    public boolean downloadFile(String path, String savePath) throws MalformedURLException, IOException {
        File file = null;
        InputStream input = null;
        OutputStream output = null;
        boolean success = false;
        try {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                input = conn.getInputStream();
                file = new File(savePath);
                file.createNewFile(); // 创建文件
                output = new FileOutputStream(file);
                byte buffer[] = new byte[1024];
                int read = 0;
                while ((read = input.read(buffer)) != -1) { // 读取信息循环写入文件
                    output.write(buffer, 0, read);
                }
                output.flush();
                success = true;
            } else {
                success = false;
            }
        } catch (MalformedURLException e) {
            success = false;
            throw e;
        } catch (IOException e) {
            success = false;
            throw e;
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return success;
    }

   /* *//**
     * 上传文件
     *
     * @param path   文件路径
     * @param params 参数
     * @param files
     *//*
    public String sendMultyPartRequest(String path, HashMap<String, String> params, HashMap<String, File> files) throws IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(path);
        MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        if (params.size() > 0) {
            Set<String> paramKeys = params.keySet();
            Iterator<String> paramIterator = paramKeys.iterator();
            while (paramIterator.hasNext()) {
                String key = paramIterator.next();
                try {
                    StringBody stringBody = new StringBody(params.get(key), Charset.forName(HTTP.UTF_8));
                    multipartEntity.addPart(key, stringBody);
                } catch (UnsupportedEncodingException e) {
                    return "error";
                }
            }
        }
        if (files.size() > 0) {
            Set<String> fileKeys = files.keySet();
            Iterator<String> fileIterator = fileKeys.iterator();
            while (fileIterator.hasNext()) {
                String key = fileIterator.next();
                FileBody fileBody = new FileBody(files.get(key));
                multipartEntity.addPart(key, fileBody);
            }
        }
        httpPost.setEntity(multipartEntity);
        String result = null;
        try {
            HttpResponse response = httpClient.execute(httpPost);
            int statueCode = response.getStatusLine().getStatusCode();
            LogUtil.logDebugMessage("statueCode==>>" + statueCode);
            if (statueCode == HttpStatus.SC_OK) {
                result = EntityUtils.toString(response.getEntity(), "utf-8");
                LogUtil.logDebugMessage("result==>>" + result);
            } else {
                result = "error";
            }
        } catch (Exception e) {
            result = "error";
            e.printStackTrace();
        } finally {
            try {
                multipartEntity.consumeContent();
                multipartEntity = null;
                httpPost.abort();
            } catch (UnsupportedOperationException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }*/


    public String upLoadFile(String path, HashMap<String, String> params, HashMap<String, File> files) throws IOException {
        String end = "/r/n";
        String Hyphens = "--";
        /**边界标识*/
        String boundary = "*****";
        URL url = null;
        try {
            url = new URL(path);
            conn = (HttpURLConnection) url.openConnection();
             /* 允许Input、Output，不使用Cache */
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            /* 设定传送的method=POST */
            conn.setRequestMethod("POST");
                /* setRequestProperty */
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
            output = new DataOutputStream(conn.getOutputStream());
            output.writeBytes(getParams(params)); // 将请求参数写入网络链接.
            output.flush();
            if (files.size() > 0) {
                Set<String> fileKeys = files.keySet();
                Iterator<String> fileIterator = fileKeys.iterator();
                while (fileIterator.hasNext()) {
                    String key = fileIterator.next();
                    output.writeBytes(Hyphens + boundary + end);
                    output.writeBytes("Content-Disposition: form-data; "
                            + "name=\"" + key + "\";filename=\"" + files.get(key).getName() + "\"" + end);
                    output.writeBytes(end);
                    /* 取得文件的FileInputStream */
                    FileInputStream inputStream = new FileInputStream(files.get(key));
                    byte[] bytes = new byte[1024];
                    int len = 0;
                    while ((len = inputStream.read(bytes)) != -1) {
                        output.write(bytes, 0, len);
                    }
                    inputStream.close();
                    output.writeBytes(end);
                    output.writeBytes(Hyphens + boundary + Hyphens + end);
                    inputStream.close();
                    output.flush();
                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        return readResponse();
    }

    /**
     * 获取本机的物理地址
     *
     * @param context 上下文对象
     * @return
     * @version 1.0
     * @createTime 2014年12月10日, 下午3:49:22
     * @updateTime 2014年12月10日, 下午3:49:22
     * @updateInfo
     */
    public static String getLocalMacAddress(Context context) {
        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = wifi.getConnectionInfo();
        return info.getMacAddress();
    }

    /**
     * 获取设备ip地址
     *
     * @return
     * @version 1.0
     * @createTime 2015年6月3日, 上午10:23:39
     * @updateTime 2015年6月3日, 上午10:23:39
     * @updateInfo
     */

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("IpAddress==>>" , ex.toString());
        }
        return "";
    }
}
