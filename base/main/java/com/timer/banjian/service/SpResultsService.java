package com.timer.banjian.service;

import com.timer.banjian.entity.QSpResults;
import com.timer.banjian.entity.SpResults;
import com.timer.banjian.entity.SysExternalLog;
import com.timer.banjian.repository.SpResultsRepository;
import com.timer.common.service.JDBCDaoImp;
import com.timer.common.utils.DateUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swallow.framework.service.BaseService;

import java.util.Date;

@Service
@Transactional
public class SpResultsService extends BaseService<SpResultsRepository, SpResults> {

    @Autowired
    private JDBCDaoImp jdbcDaoImp;

    @Autowired
    private BjAcceptService bjAcceptService;

    @Autowired
    private BjResultsService bjResultsService;

    @Value("${httpUrl.accept}")
    private String acceptUrl;

    @Value("${httpUrl.results}")
    private String resultsUrl;

    public SpResults getItems(String certNo, String bizType, String year){
        QSpResults qsp = QSpResults.spResults;
        return super.getItem(query -> {
                    if (StringUtils.isNotEmpty(certNo)) {
                        query.where(qsp.certNo.eq(certNo));
                    }
                    if (StringUtils.isNotEmpty(bizType)) {
                        query.where(qsp.bizType.eq(bizType));
                    }
                    if (StringUtils.isNotEmpty(year)) {
                        query.where(qsp.year.eq(year));
                    }
                    return query;
                });
    }

    public String getApplyYear(){
        StringBuffer sql = new StringBuffer("select config_value from sys_config where config_key = ?");
        return jdbcDaoImp.queryForObject(String.class, sql.toString(),new Object[]{"applyYear"});
    }

    public String getOutDataDic(){
        StringBuffer sql = new StringBuffer("select config_value from sys_config where config_key = ?");
        return jdbcDaoImp.queryForObject(String.class, sql.toString(),new Object[]{"outDataDic"});
    }

    public String getIsSynJob(){
        StringBuffer sql = new StringBuffer("select config_value from sys_config where config_key = ?");
        return jdbcDaoImp.queryForObject(String.class, sql.toString(),new Object[]{"isSynJob"});
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleSpData(Date startTime, Date endTime, SysExternalLog logger) throws Exception {
        try {
            this.handleData(startTime, endTime, logger);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleData(Date startTime, Date endTime, SysExternalLog logger) throws Exception {
        try {
            String year = this.getApplyYear();

            if (StringUtils.isEmpty(year))
                year = DateUtil.format("yyyy", new Date());

            String startDate = DateUtil.format("yyyy-MM-dd HH:mm:ss", DateUtil.getStartDate(startTime));
            String endDate = DateUtil.format("yyyy-MM-dd HH:mm:ss", DateUtil.getEndDate(endTime));

            // 办件受理
            bjAcceptService.syncBjAccept(acceptUrl, 1, 20,  0, startDate, endDate, logger, year);

            // 办件结果
            bjResultsService.syncBjResults(resultsUrl, 1, 20,  0, startDate, endDate, logger, year);

            // 结果处理
            bjResultsService.handleResults();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleDataAddTime(SysExternalLog logger) throws Exception {
        try {
            this.handleData(logger);
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleData(SysExternalLog logger) throws Exception {
        try {
            Date now = new Date();
            String year = this.getApplyYear();

            if (StringUtils.isEmpty(year))
                year = DateUtil.format("yyyy", new Date());

            String startDate = DateUtil.format("yyyy-MM-dd HH:mm:ss", DateUtil.getStartDate(DateUtil.add(now,5,-1)));
            String endDate = DateUtil.format("yyyy-MM-dd HH:mm:ss", DateUtil.getEndDate(DateUtil.add(now,5,-1)));

            // 办件受理
            bjAcceptService.syncBjAccept(acceptUrl, 1, 20,  0, startDate, endDate, logger, year);

            // 办件结果
            bjResultsService.syncBjResults(resultsUrl, 1, 20,  0, startDate, endDate, logger, year);

            // 结果处理
            bjResultsService.handleResults();
        }catch (Exception e){
            throw new Exception(e.getMessage());
        }
    }
}
