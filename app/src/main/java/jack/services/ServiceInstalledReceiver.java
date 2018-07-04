package jack.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by ChanLin on 2018/6/11.
 * ProtectEyes
 * TODO:
 */
public class ServiceInstalledReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.media.AUDIO_BECOMING_NOISY")) {
            /* 服务开机自启动 */
            Intent service = new Intent(context, LocationService.class);
            context.startService(service);
        }
    }
}
