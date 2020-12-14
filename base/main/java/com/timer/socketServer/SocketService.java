package com.timer.socketServer;


import com.timer.banjian.entity.SysExternalLog;
import com.timer.banjian.service.SpResultsService;
import com.timer.banjian.service.SysExternalLogService;
import com.timer.common.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class SocketService {

    private static final Logger log = LoggerFactory.getLogger(SocketService.class);

    @Autowired
    private SysExternalLogService logService;

    @Autowired
    private SpResultsService spResultsService;

    public String initData(Date startTime, Date endTime){
        List<String> listTime = DateUtil.getDayList(startTime, endTime, "yyyy-MM-dd");
        for (String time : listTime) {
            SysExternalLog logger = new SysExternalLog(null,null,1,null,
                    null,0, new Date(),"1003");
            try{
                Date date = DateUtil.parseDate(time);
                spResultsService.handleSpData(date, date, logger);
            }catch (Exception e){
                logger.setStatus(1);// 系统异常
                logger.setMessage(e.getMessage());
                log.warn("系统异常："+ e.getMessage());
                e.printStackTrace();
                return "{\"Code\":\"1\",\"Msg\":\"失败！\"}";
            }finally {
                logService.insertItem(logger);
            }
        }
        return "{\"Code\":\"0\",\"Msg\":\"成功！\"}";
    }

}
