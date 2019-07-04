package com.xinlan.imsdk.core.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.xinlan.imsdk.IMClient;
import com.xinlan.imsdk.core.CoreService;
import com.xinlan.imsdk.util.LogUtil;
import com.xinlan.imsdk.util.ProcessUtil;

/**
 * 以下事件依然能在 AndroidManifest.xml 中声明隐式广播接收器，可以正常使用。
 *
 * ACTION_LOCKED_BOOT_COMPLETED, ACTION_BOOT_COMPLETED
 * ACTION_USER_INITIALIZE
 * ACTION_LOCALE_CHANGED
 * ACTION_USB_ACCESSORY_ATTACHED, ACTION_USB_ACCESSORY_DETACHED, ACTION_USB_DEVICE_ATTACHED, ACTION_USB_DEVICE_DETACHED
 * ACTION_HEADSET_PLUG
 * ACTION_CONNECTION_STATE_CHANGED, ACTION_CONNECTION_STATE_CHANGED, ACTION_ACL_CONNECTED, ACTION_ACL_DISCONNECTED
 * ACTION_CARRIER_CONFIG_CHANGED
 * LOGIN_ACCOUNTS_CHANGED_ACTION
 * ACTION_PACKAGE_DATA_CLEARED
 * ACTION_PACKAGE_FULLY_REMOVED
 * ACTION_NEW_OUTGOING_CALL
 * ACTION_DEVICE_OWNER_CHANGED
 * ACTION_EVENT_REMINDER
 * ACTION_MEDIA_MOUNTED, ACTION_MEDIA_CHECKING, ACTION_MEDIA_UNMOUNTED, ACTION_MEDIA_EJECT, ACTION_MEDIA_UNMOUNTABLE
 * SMS_RECEIVED_ACTION, WAP_PUSH_RECEIVED_ACTION
 *
 *
 */
public class CoreServiceAuxBroadcast extends BroadcastReceiver {
    public static final String ACTION = "com.xinlan.imclient.receiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.log("received kill broadcast");
        if(!ProcessUtil.isServiceRunning(context , CoreService.NAME)){
            LogUtil.log("core service has been killed");
            IMClient.getInstance().startCoreService(context);
        }
    }
}// end class
