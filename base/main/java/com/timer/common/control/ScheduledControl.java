package com.timer.common.control;

import com.timer.banjian.entity.SysExternalLog;
import com.timer.banjian.service.BjAcceptService;
import com.timer.banjian.service.BjResultsService;
import com.timer.banjian.service.SpResultsService;
import com.timer.banjian.service.SysExternalLogService;
import com.timer.common.utils.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Date;

/**
 * 定时任务
 */
@Component
@Configuration
@EnableScheduling
public class ScheduledControl {

    private static final Logger log = LoggerFactory.getLogger(ScheduledControl.class);

    @Value("${httpUrl.accept}")
    private String acceptUrl;

    @Value("${httpUrl.results}")
    private String resultsUrl;

    @Autowired
    private BjResultsService bjResultsService;

    @Autowired
    private SysExternalLogService logService;

    @Autowired
    private SpResultsService spResultsService;

    /**
     * 同步学生教师办件受理、结果、结果处理
     */
    @Scheduled(cron = "${config.cron}")
    public void syncHandle(){
        log.info("start sync handle");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        String isSynJob = spResultsService.getIsSynJob();
        if (StringUtils.isNotEmpty(isSynJob) && isSynJob.equals("1")) {
            SysExternalLog logger = new SysExternalLog(null,null,1,null,
                    null,0, new Date(),"1003");
            try{
                spResultsService.handleDataAddTime(logger);
            }catch (Exception e){
                logger.setStatus(1);// 系统异常
                logger.setMessage(e.getMessage());
                log.warn("系统异常："+ e.getMessage());
                e.printStackTrace();
            }finally {
                logService.insertItem(logger);
            }
        }
        stopWatch.stop();
        log.info("end sync handle");
        log.info("消耗时间：" + stopWatch.getTotalTimeMillis() + "ms");
    }


    /**
     * 同步学生教师办件结果
     */
//    @Scheduled(cron = "0 0 0 1 * ?")
    public void syncBjResults(){
        log.info("start sync bjResults");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        SysExternalLog logger = new SysExternalLog(null,null,1,null,
                null,0, new Date(),"1004");
        try{
            String year = spResultsService.getApplyYear();

            if (StringUtils.isEmpty(year))
                year = DateUtil.format("yyyy", new Date());
            String start = DateUtil.format("yyyy-MM-dd HH:mm:ss", DateUtil.getStartDate(DateUtil.add(new Date(),5,-1)));
            String end = DateUtil.format("yyyy-MM-dd HH:mm:ss", DateUtil.getEndDate(DateUtil.add(new Date(),5,-1)));
            bjResultsService.syncBjResults(resultsUrl, 1, 20,  0, start, end, logger, year);
        }catch (Exception e){
            logger.setStatus(1);// 系统异常
            logger.setMessage(e.getMessage());
            log.warn("系统异常："+ e.getMessage());
            e.printStackTrace();
        }finally {
            logService.insertItem(logger);
        }
        stopWatch.stop();
        log.info("end sync bjResults");
        log.info("消耗时间：" + stopWatch.getTotalTimeMillis() + "ms");
    }


    /**
     * 处理审批结果
     */
//    @Scheduled(cron = "0 0 0 1 * ?")
    public void handleSpResults(){
        log.info("start handle spResults");
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
//        bjResultsService.handleResults();
        stopWatch.stop();
        log.info("end handle spResults");
        log.info("消耗时间：" + stopWatch.getTotalTimeMillis() + "ms");
    }
}
