package com.xinlan.imsdk.core;

import android.app.job.JobParameters;
import android.app.job.JobService;

import com.xinlan.imsdk.IMClient;
import com.xinlan.imsdk.util.LogUtil;
import com.xinlan.imsdk.util.ProcessUtil;

public class KeepJobService extends JobService {
    public static final int JOB_ID = 1;

    @Override
    public boolean onStartJob(JobParameters params) {
        LogUtil.log("received kill jobschedule");
        if(!ProcessUtil.isServiceRunning(this , CoreService.NAME)){
            LogUtil.log("core service has been killed");
            IMClient.getInstance().startCoreService(this);
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}//end class
