package com.timer.banjian.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.timer.banjian.entity.*;
import com.timer.banjian.repository.BjAcceptRepository;
import com.timer.common.utils.HttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swallow.framework.service.BaseService;

import java.util.Map;

@Service
@Transactional
public class BjAcceptService extends BaseService<BjAcceptRepository, BjAccept> {

    private static final Logger logger = LoggerFactory.getLogger(BjAcceptService.class);

    @Value("${params.STU_ITEMCODE}")
    private String STU_ITEMCODE;//深圳市在园儿童健康成长补贴

    @Value("${params.TEA_ITEMCODE}")
    private String TEA_ITEMCODE;//幼儿园保教人员长期从教津贴

    @Value("${params.apiType}")
    private String apiType;

    @Value("${params.userName}")
    private String userName;

    @Value("${params.psd}")
    private String psd;

    @Value("${params.code1}")
    public String code1;

    @Value("${params.apiId1}")
    public String apiId1;

    @Transactional
    public synchronized void syncBjAccept(String url, int page, int pageSize, int totalPage, String startDate, String endDate, SysExternalLog log, String year) throws Exception {
        try {
            JSONObject json = this.getJsonParams(page, pageSize, startDate, endDate,
                    "F_CD_TIME_gte","F_CD_TIME_lte", code1, apiId1);
            log.setReqDesc(json.toJSONString());
            JSONObject jsonObject = syncBjAccept(url, json);
            if (jsonObject == null) {
                log.setStatus(3);
                log.setMessage("请求异常：请求超时或失败！！！");
                throw new Exception("请求异常：请求超时或失败！！！");
            }
            if ("200".equals(jsonObject.getString("status"))) {
                int total = jsonObject.getIntValue("total");
                totalPage = total % pageSize == 0 ? total / pageSize : Math.floorDiv(total, pageSize) + 1;
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                if (jsonArray != null){
                    for (int i=0;i<jsonArray.size();i++) {
                        BjAccept isExists = this.getItems(jsonArray.getJSONObject(i).getString("F_PROJECTNO"));
                        BjAccept bjAccept = this.parseObject(jsonArray.getJSONObject(i));
                        if (bjAccept != null && isExists == null) {
                            bjAccept.setYear(year);
                            super.insertItem(bjAccept);
                        }
                    }
                }
                if (page < totalPage) {
                    syncBjAccept(url, ++page, pageSize, totalPage, startDate, endDate, log, year);
                }
            }else{// 请求数据失败
                log.setStatus(2);// 请求数据异常
                log.setMessage(jsonObject.toJSONString());
            }
        }catch (Exception e){
            logger.warn("系统异常："+ e.getMessage());
            e.printStackTrace();
            throw new Exception(e.getMessage());
        }
    }

    private JSONObject syncBjAccept(String url, Map<String, Object> map){
        return HttpClientUtils.httpPost(url, map);
    }

    private JSONObject syncBjAccept(String url, JSONObject json) throws Exception{
        return HttpClientUtils.doPost(url, json);
    }

    public BjAccept getItems(String projectNo){
        QBjAccept qbj = QBjAccept.bjAccept;
        return super.getItem(query -> {
                query.where(qbj.projectNo.eq(projectNo));
            return query;
        });
    }

    private BjAccept parseObject(JSONObject json){
        BjAccept bjAccept = new BjAccept();
        bjAccept.setRowGuid(json.getString("F_ROWGUID"));
        bjAccept.setCdTime(json.getDate("F_CD_TIME"));
        bjAccept.setAcceptDate(json.getDate("F_ACCEPTDATE"));
        bjAccept.setLocalTaskCode(json.getString("F_LOCALTASKCODE"));
        bjAccept.setTaskHandleItem(json.getString("F_TASKHANDLEITEM"));
        bjAccept.setItemCode(json.getString("F_ITEMCODE"));
        bjAccept.setSblshShort(json.getString("F_SBLSHSHORT"));
        bjAccept.setApplyerPageType(json.getString("F_APPLYERPAGETYPE"));
        bjAccept.setLocalCatalogCode(json.getString("F_LOCALCATALOGCODE"));
        bjAccept.setOrgName(json.getString("F_ORGNAME"));
        bjAccept.setApplyerName(json.getString("F_APPLYERNAME"));
        bjAccept.setTaskName(json.getString("F_TASKNAME"));
        bjAccept.setTaskCode(json.getString("F_TASKCODE"));
        bjAccept.setApplyerPageCode(json.getString("F_APPLYERPAGECODE"));
        bjAccept.setApplyerType(json.getString("F_APPLYERTYPE"));
        bjAccept.setApplyType(json.getString("F_APPLYTYPE"));
        bjAccept.setCdBatch(json.getString("F_CD_BATCH"));
        bjAccept.setSlztdm(json.getString("F_SLZTDM"));
        bjAccept.setProjectNo(json.getString("F_PROJECTNO"));
        bjAccept.setOrgCode(json.getString("F_ORGCODE"));
        bjAccept.setCdOperation(json.getString("F_CD_OPERATION"));
        bjAccept.setHandleusername(json.getString("F_HANDLEUSERNAME"));
        bjAccept.setCatalogCode(json.getString("F_CATALOGCODE"));
        bjAccept.setPromisedate(json.getDate("F_PROMISEDATE"));
        bjAccept.setSblsh(json.getString("F_SBLSH"));
        bjAccept.setApplyDate(json.getDate("F_APPLYDATE"));
        bjAccept.setApplyForm(json.getString("F_APPLYFORM"));
        return bjAccept;
    }

    public JSONObject getJsonParams(int page, int pageSize, String startDate, String endDate,
                                    String param1, String param2, String code, String apiId){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("code", code);
        jsonObject.put("isPage", true);
        jsonObject.put("index", page);
        jsonObject.put("size", pageSize);
        jsonObject.put("apiType", apiType);
        jsonObject.put("userName", userName);
        jsonObject.put("psd", psd);
        jsonObject.put("apiId", apiId);
        JSONArray jsonArray = new JSONArray();
        JSONObject json = new JSONObject();
        json.put("param", param1);
        json.put("type","String");
        json.put("val", startDate);
        jsonArray.add(json);
        JSONObject json1 = new JSONObject();
        json1.put("param", param2);
        json1.put("type","String");
        json1.put("val", endDate);
        jsonArray.add(json1);
        JSONObject json2 = new JSONObject();
        json2.put("param","F_ITEMCODE");
        json2.put("type","String");
        json2.put("val","'"+STU_ITEMCODE+"','"+TEA_ITEMCODE+"'");
        jsonArray.add(json2);
        jsonObject.put("search", jsonArray);
        return jsonObject;
    }
}
