package com.souche.bulbous.excel;

import com.souche.optimus.common.config.OptimusConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author XadomGreen
 * @since 2018-08-21
 */
public class ExcelConstant {

    public final static ExecutorService EXECUTOR_POOL =
            new ThreadPoolExecutor(0, 50, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

    /**
     * Job前缀
     **/
    public final static String EXPROT_JOB_ = "bulbousinc_exprot_job_";
    /**
     * Job过期时间
     */
    public final static int EXPRIE_SECOND = 60 * 30;
    public final static int PAGE_SIZE = 50;
    public static String RESOURCE_HEAD = OptimusConfig.getValue("res_head");
}
