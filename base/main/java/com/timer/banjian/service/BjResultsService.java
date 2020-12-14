package com.timer.banjian.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.timer.banjian.entity.*;
import com.timer.banjian.repository.BjResultsRepository;
import com.timer.common.service.JDBCDaoImp;
import com.timer.common.utils.DateUtil;
import com.timer.common.utils.HttpClientUtils;
import com.timer.common.utils.TypeNameUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import swallow.framework.service.BaseService;

import java.util.*;

@Service
@Transactional
public class BjResultsService extends BaseService<BjResultsRepository, BjResults> {

    private static final Logger logger = LoggerFactory.getLogger(BjResultsService.class);

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

    @Value("${params.code2}")
    public String code2;

    @Value("${params.apiId2}")
    public String apiId2;

    @Autowired
    private JDBCDaoImp jdbcDaoImp;

    @Autowired
    private SpResultsService spResultsService;

    @Autowired
    private BjAcceptService bjAcceptService;

    @Transactional(rollbackFor = Exception.class)
    public synchronized void syncBjResults(String url,int page, int pageSize, int totalPage, String startDate, String endDate, SysExternalLog log, String year) throws Exception {
        try {
            JSONObject json = this.getJsonParams(page, pageSize, startDate, endDate,
                    "F_CD_TIME_gte","F_CD_TIME_lte", code2, apiId2);
            log.setReqDesc(json.toJSONString());
            JSONObject jsonObject = syncBjResults(url, json);
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
                        BjResults isExists = this.getItems(jsonArray.getJSONObject(i).getString("F_PROJECTNO"));
                        BjResults bjResults = this.parseObject(jsonArray.getJSONObject(i));
                        if (bjResults != null && isExists == null) {
                            bjResults.setYear(year);
                            super.insertItem(bjResults);
                        }
                    }
                }
                if (page < totalPage) {
                    syncBjResults(url, ++page, pageSize, totalPage, startDate, endDate, log, year);
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

    private JSONObject syncBjResults(String url, Map<String, Object> map){
        return HttpClientUtils.httpPost(url, map);
    }

    private JSONObject syncBjResults(String url, JSONObject json) throws Exception{
        return HttpClientUtils.doPost(url, json);
    }

    public BjResults getItems(String projectNo){
        QBjResults qbj = QBjResults.bjResults;
        return super.getItem(query -> {
                query.where(qbj.projectNo.eq(projectNo));
            return query;
        });
    }

    private BjResults parseObject(JSONObject json){
        BjResults bjResults = new BjResults();
        bjResults.setResultDate(json.getString("F_RESULTDATE"));
        bjResults.setOrgName(json.getString("F_ORGNAME"));
        bjResults.setProjectNo(json.getString("F_PROJECTNO"));
        bjResults.setCdBatch(json.getString("F_CD_BATCH"));
        bjResults.setSblsh(json.getString("F_SBLSH"));
        bjResults.setUserName(json.getString("F_USERNAME"));
        bjResults.setResultType(json.getString("F_RESULTTYPE"));
        bjResults.setRegionCode(json.getString("F_REGIONCODE"));
        bjResults.setRowGuid(json.getString("F_ROWGUID"));
        bjResults.setTaskHandleItem(json.getString("F_TASKHANDLEITEM"));
        bjResults.setCdTime(json.getDate("F_CD_TIME"));
        bjResults.setSblshShort(json.getString("F_SBLSHSHORT"));
        bjResults.setItemCode(json.getString("F_ITEMCODE"));
        bjResults.setResultExplain(json.getString("F_RESULTEXPLAIN"));
        bjResults.setCdOperation(json.getString("F_CD_OPERATION"));
        bjResults.setOrgCode(json.getString("F_ORGCODE"));
        return bjResults;
    }

    @Transactional(rollbackFor = Exception.class)
    public synchronized void handleResults() throws Exception {
        try{
            // 未处理办件结果
            List<BjResults> bjResultsList = getAllItems(query -> query.where(QBjResults.bjResults.status.eq(0))
                    .orderBy(QBjResults.bjResults.resultDate.asc()));
            for (BjResults bjResults : bjResultsList) {
                handleResults(bjResults);
            }
        }catch (Exception e){
            throw new Exception("更新异常：" + e.getMessage());
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void handleResults(BjResults bjResults) throws Exception {
        try {
            BjAccept bjAccept = bjAcceptService.getItem(query ->
                    query.where(QBjAccept.bjAccept.projectNo.eq(bjResults.getProjectNo())));
            if (bjAccept == null){
                bjResults.setStatus(2);// 标记异常
                super.updateItem(bjResults);
                return;
            }
            JSONObject jsonObject = JSONObject.parseObject(bjAccept.getApplyForm().replace("\\u0027","'"));
            String certNo = "";
            if (STU_ITEMCODE.equals(bjAccept.getItemCode())){
                certNo = jsonObject.getString("ZJHM");
            }else {
                certNo = jsonObject.getString("ZhengJianHaoMa");
            }
            String year = spResultsService.getApplyYear();

            if (StringUtils.isEmpty(year))
                year = DateUtil.format("yyyy", new Date());

            SpResults spResults = spResultsService.getItems(certNo, bjAccept.getItemCode(), year);
            // 审批结果
            if (spResults == null) {
                spResults = new SpResults();
                spResults.setBizType(bjAccept.getItemCode());
                spResults.setCertNo(certNo);
                spResults.setYear(year);
                spResults.setSpStatus(bjResults.getResultType());
                spResultsService.insertItem(spResults);//审批结果
                this.updStuOrTeaInfo(bjAccept, jsonObject, certNo);//更新教师学生数据
            }else if (spResults.getSpStatus().equals("0")) {
                spResults.setSpStatus(bjResults.getResultType());
                spResults.setLastmodi(new Date());
                spResultsService.updateItem(spResults);
                this.updStuOrTeaInfo(bjAccept, jsonObject, certNo);
            }else{
                if (bjResults.getResultType().equals("1")) {// 办件审批通过
                    spResults.setSpStatus(bjResults.getResultType());
                    spResults.setLastmodi(new Date());
                    spResultsService.updateItem(spResults);
                    this.updStuOrTeaInfo(bjAccept, jsonObject, certNo);
                }
            }
            bjResults.setStatus(1);// 标记已处理
            super.updateItem(bjResults);
        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("更新异常：" + e.getMessage());
        }

    }

    private void updStuOrTeaInfo(BjAccept bjAccept, JSONObject jsonObject, String certNo){
        if (STU_ITEMCODE.equals(bjAccept.getItemCode())) {// 学生
            this.updateStudentInfo(jsonObject);
        }else if (TEA_ITEMCODE.equals(bjAccept.getItemCode())) {// 教师
            boolean flag = this.updateTeacherInfo(jsonObject);// 教师信息
            if (flag){// 合同
                this.deleteHeTong(certNo);
                JSONArray jsonArray = jsonObject.getJSONArray("drow2022201011283916666");
                if (jsonArray == null)
                    jsonArray = jsonObject.getJSONArray("drow2020101011283916666");
                this.insertHeTong(certNo, jsonArray);
            }
        }
    }

    private boolean updateStudentInfo(JSONObject jsonObject) {
        List<Object> obj = new ArrayList<>();
        StringBuffer sql = new StringBuffer("update sys_student set sex = ?,cert_type=?,");
        obj.add(TypeNameUtil.getTypeKeyByValue("xb", jsonObject.getString("XINGBIE")));
        obj.add(jsonObject.getString("ZJLX"));
        if(jsonObject.get("CSRQ") != null){
            sql.append("birthday_time = ?, ");
            obj.add(jsonObject.getString("CSRQ"));
        }
        if(jsonObject.get("XJZDZ") != null){
            sql.append("current_address = ?, ");
            obj.add(jsonObject.getString("XJZDZ"));
        }
        if(jsonObject.get("JHREXM") != null){
            sql.append("guardian_name2 = ?, ");
            obj.add(jsonObject.getString("JHREXM"));
        }
        if(jsonObject.get("JHREGZDW") != null){
            sql.append("guardian_work_unit2 = ?, ");
            obj.add(jsonObject.getString("JHREGZDW"));
        }
        if(jsonObject.get("YinXingKaHao") != null){
            sql.append("card_no = ?, ");
            obj.add(jsonObject.getString("YinXingKaHao"));
        }
        if(jsonObject.get("JHRYGZDW") != null){
            sql.append("guardian_work_unit = ?, ");
            obj.add(jsonObject.getString("JHRYGZDW"));
        }
        if(jsonObject.get("JHREYETGX") != null){
            sql.append("guardian2 = ?, ");
            obj.add(TypeNameUtil.getTypeKeyByValue("yetgx", jsonObject.getString("JHREYETGX")));
        }
        if(jsonObject.get("YinXingHuZhu") != null){
            sql.append("account_holder = ?, ");
            obj.add(jsonObject.getString("YinXingHuZhu"));
        }
        if(jsonObject.get("JHREHJ") != null){
            sql.append("guardian_census2 = ?, ");
            obj.add(jsonObject.getString("JHREHJ"));
        }
        if(jsonObject.get("ETHJ") != null){
            sql.append("census = ?, ");
            obj.add(TypeNameUtil.getTypeKeyByValue("ethj", jsonObject.getString("ETHJ")));
        }
        if(jsonObject.get("JHRYZJLX") != null){
            sql.append("guardian_cert_type = ?, ");
            obj.add(jsonObject.getString("JHRYZJLX"));
        }
        if(jsonObject.get("CSD") != null){
            sql.append("birthplace = ?, ");
            obj.add(jsonObject.getString("CSD"));
        }
        if(jsonObject.get("JHRYYETGX") != null){
            sql.append("guardian = ?, ");
            obj.add(TypeNameUtil.getTypeKeyByValue("yetgx", jsonObject.getString("JHRYYETGX")));
        }
        if(jsonObject.get("JHREZJLX") != null){
            sql.append("guardian_cert_type2 = ?, ");
            obj.add(jsonObject.getString("JHREZJLX"));
        }
        if(jsonObject.get("SFGR") != null){
            sql.append("is_orphan = ?, ");
            obj.add(jsonObject.getString("SFGR"));
        }
        if(jsonObject.get("JHRYXM") != null){
            sql.append("guardian_name = ?, ");
            obj.add(jsonObject.getString("JHRYXM"));
        }
        if(jsonObject.get("NIANJI") != null){
            sql.append("grade = ?, ");
            obj.add(TypeNameUtil.getTypeKeyByValue("nj", jsonObject.getString("NIANJI")));
        }
        if(jsonObject.get("JHRYLXDH") != null){
            sql.append("guardian_phone = ?, ");
            obj.add(jsonObject.getString("JHRYLXDH"));
        }
        if(jsonObject.get("JHREZJHM") != null){
            sql.append("guardian_cert_no2 = ?, ");
            obj.add(jsonObject.getString("JHREZJHM"));
        }
        if(jsonObject.get("RYRQ") != null){
            sql.append("enrollment_time = ?, ");
            obj.add(jsonObject.getString("RYRQ"));
        }
        if(jsonObject.get("HKXZ") != null){
            sql.append("residents = ?, ");
            obj.add(TypeNameUtil.getTypeKeyByValue("hkxz", jsonObject.getString("HKXZ")));
        }
        if(jsonObject.get("JHRYHJ") != null){
            sql.append("guardian_census = ?, ");
            obj.add(jsonObject.getString("JHRYHJ"));
        }
        if(jsonObject.get("JHRYZJHM") != null){
            sql.append("guardian_cert_no = ?, ");
            obj.add(jsonObject.getString("JHRYZJHM"));
        }
        if(jsonObject.get("CSRQ") != null){
            sql.append("birthday_time = ?, ");
            obj.add(jsonObject.getString("CSRQ"));
        }
        if(jsonObject.get("SFLSHYFZN") != null){
            sql.append("is_martyrs_children = ?, ");
            obj.add(jsonObject.getString("SFLSHYFZN"));
        }
        if(jsonObject.get("JHRELXDH") != null){
            sql.append("guardian_phone2 = ?, ");
            obj.add(jsonObject.getString("JHRELXDH"));
        }
        if(jsonObject.get("SFCJET") != null){
            sql.append("is_disability = ?, ");
            obj.add(jsonObject.getString("SFCJET"));
        }
        if(jsonObject.get("SFDQJT") != null){
            sql.append("is_one_parent_family = ?, ");
            obj.add(jsonObject.getString("SFDQJT"));
        }
        if(jsonObject.get("JIGUAN") != null){
            sql.append("hometown = ?, ");
            obj.add(jsonObject.getString("JIGUAN"));
        }
        if(jsonObject.get("JZSJH") != null){
            sql.append("telephone = ?, ");
            obj.add(jsonObject.getString("JZSJH"));
        }
        if(jsonObject.get("ZHIHANGMINGCHENG") != null){
            sql.append("sub_branch = ?, ");
            obj.add(jsonObject.getString("ZHIHANGMINGCHENG"));
        }
        if(jsonObject.get("BANHAO") != null){
            sql.append("clazz = ?, ");
            obj.add(jsonObject.getString("BANHAO"));
        }
        if(jsonObject.get("YinXingKaKaiHuXing") != null){
            sql.append("bank_name = ?, ");
            obj.add(jsonObject.getString("YinXingKaKaiHuXing"));
        }
        sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(",")+1,"");
        sql.append(" where cert_no = ? and status = 0 ");
        obj.add(jsonObject.getString("ZJHM"));
        return jdbcDaoImp.executeSql(sql.toString(), obj.toArray());
    }

    private boolean updateTeacherInfo(JSONObject jsonObject) {
        List<Object> obj = new ArrayList<>();
        StringBuffer sql = new StringBuffer("update sys_teacher_staff set sex = ?,cert_type=?,");
        obj.add(jsonObject.getString("XINGBIE"));
        obj.add(jsonObject.getString("ZHENGJIANLEIXING"));
        if(jsonObject.get("JINBENYUANNIANYUE") != null){
            sql.append("now_work_time = ?, ");
            obj.add(jsonObject.getString("JINBENYUANNIANYUE"));
        }
        if(jsonObject.get("BanJi") != null){
            sql.append("ban_hao = ?, ");
            obj.add(jsonObject.getString("BanJi"));
        }
        if(jsonObject.get("ZAISHENSHEBAOKADIANNAOHAO") != null){
            sql.append("she_bao_num = ?, ");
            obj.add(jsonObject.getString("ZAISHENSHEBAOKADIANNAOHAO"));
        }
        if(jsonObject.get("YiLingQuBJRYCJJTNX") != null){
            sql.append("jin_tie_year = ?, ");
            obj.add(jsonObject.getString("YiLingQuBJRYCJJTNX"));
        }
        if(jsonObject.get("XueLi") != null){
            sql.append("full_time_education = ?, ");
            obj.add(TypeNameUtil.getTypeKeyByValue("xl",jsonObject.getString("XueLi")));
        }
        if(jsonObject.get("GONGZUOGANGWEI") != null){
            sql.append("job = ?, ");
            obj.add(TypeNameUtil.getTypeKeyByValue("zyzw",jsonObject.getString("GONGZUOGANGWEI")));
        }
        if(jsonObject.get("ZIGEZHENGBIANHAO") != null){
            sql.append("teacher_cert_num = ?, ");
            obj.add(jsonObject.getString("ZIGEZHENGBIANHAO"));
        }
        if(jsonObject.get("HuMing") != null){
            sql.append("bank_holder = ?, ");
            obj.add(jsonObject.getString("HuMing"));
        }
        if(jsonObject.get("KaHaoZhangHao") != null){
            sql.append("bank_num = ?, ");
            obj.add(jsonObject.getString("KaHaoZhangHao"));
        }
        if(jsonObject.get("ShiFouDaDaoTuiXiuNianL") != null){
            sql.append("sf_tui_xiu = ?, ");
            obj.add(TypeNameUtil.getTypeKeyByValue("ll_0001",jsonObject.getString("ShiFouDaDaoTuiXiuNianL")));
        }
        if(jsonObject.get("FeiQuanRiZhiXueLi") != null){
            sql.append("part_time_education = ?, ");
            obj.add(TypeNameUtil.getTypeKeyByValue("xl",jsonObject.getString("FeiQuanRiZhiXueLi")));
        }
        if(jsonObject.get("ZiGeZhengMingChen") != null){
            sql.append("teacher_cert_name = ?, ");
            obj.add(TypeNameUtil.getTypeKeyByValue("dd_0037",jsonObject.getString("ZiGeZhengMingChen")));
        }
        if(jsonObject.get("CANJIAGONGZUORIQI") != null){
            sql.append("total_work_time = ?, ");
            obj.add(jsonObject.getString("CANJIAGONGZUORIQI"));
        }
        if(jsonObject.get("ShouJiHaoMa") != null){
            sql.append("contact_way = ?, ");
            obj.add(jsonObject.getString("ShouJiHaoMa"));
        }
        if(jsonObject.get("FeiQuanRiZhiXueWei") != null){
            sql.append("part_time_degree = ?, ");
            obj.add(TypeNameUtil.getTypeKeyByValue("xw",jsonObject.getString("FeiQuanRiZhiXueWei")));
        }
        if(jsonObject.get("CSNY") != null){
            sql.append("birthay = ?, ");
            obj.add(jsonObject.getString("CSNY"));
        }
        if(jsonObject.get("SHIFOUYIBANLIJUZHUZHENG") != null){
            sql.append("ju_zhu_zheng = ?, ");
            obj.add(jsonObject.getString("SHIFOUYIBANLIJUZHUZHENG"));
        }
        if(jsonObject.get("KaiHuXingMingChen") != null){
            sql.append("bank_name = ?, ");
            obj.add(jsonObject.getString("KaiHuXingMingChen"));
        }
        if(jsonObject.get("FaZhengRiQi") != null){
            sql.append("teacher_cert_date = ?, ");
            obj.add(jsonObject.getString("FaZhengRiQi"));
        }
        if(jsonObject.get("NianJi") != null){
            sql.append("nianji = ?, ");
            obj.add(jsonObject.getString("NianJi"));
        }
        if(jsonObject.get("XUEWEI") != null){
            sql.append("full_time_degree = ?, ");
            obj.add(TypeNameUtil.getTypeKeyByValue("xw",jsonObject.getString("XUEWEI")));
        }
        if(jsonObject.get("XIANJUZHUDIZHI") != null){
            sql.append("address = ?, ");
            obj.add(jsonObject.getString("XIANJUZHUDIZHI"));
        }
        if(jsonObject.get("YUANZHANGZIGEZHENGSHUBIANHAO") != null){
            sql.append("yuan_zhang_cert_num = ?, ");
            obj.add(jsonObject.getString("YUANZHANGZIGEZHENGSHUBIANHAO"));
        }
        if(jsonObject.get("FAZHENGDANWEI") != null){
            sql.append("teacher_cert_unit = ?, ");
            obj.add(jsonObject.getString("FAZHENGDANWEI"));
        }
        if(jsonObject.get("HUJIXINXI") != null){
            sql.append("place_info = ?, ");
            obj.add(jsonObject.getString("HUJIXINXI"));
        }
        if(jsonObject.get("ZhiXing") != null){
            sql.append("bank_site = ?, ");
            obj.add(jsonObject.getString("ZhiXing"));
        }
        sql.replace(sql.lastIndexOf(","), sql.lastIndexOf(",")+1,"");
        sql.append(" where cert_num = ? and status = 0 ");
        obj.add(jsonObject.getString("ZhengJianHaoMa"));
        return jdbcDaoImp.executeSql(sql.toString(), obj.toArray());
    }

    private boolean deleteHeTong(String certNum){
        StringBuffer sql = new StringBuffer("delete from sys_teacher_he_tong where cert_num = ? ");
        return jdbcDaoImp.executeSql(sql.toString(), new Object[]{certNum});
    }

    private boolean insertHeTong(String certNum,JSONArray jsonArray){
        List<String> batchSql = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject j = jsonArray.getJSONObject(i);
            StringBuffer sql = new StringBuffer("insert into sys_teacher_he_tong ");
            sql.append(" (version, created, creator, lastmodi,modifier, cert_num, he_tong_end, he_tong_start, he_tong_unit,shebao_end_date, shebao_start_date) ");
            sql.append(" VALUES ( 0, now(), 0, now(), 0, '"+certNum+"', '"+j.getString("HeTongYouXiaoQiJieShu")+"', '"+j.getString("HeTongYouXiaoQiKaiShi")+"', '"+j.getString("HeTongQianDingDanWei")+"', '"+j.getString("SheBaoJieShuShiJian")+"', '"+j.getString("SheBaoKaiShiShiJian")+"')");
            batchSql.add(sql.toString());
        }
        return jdbcDaoImp.batchExecuteSql(batchSql);
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
