package com.timer.banjian.entity;

import com.timer.common.entity.BaseSimpleEntity;
import io.swagger.annotations.ApiModel;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import swallow.framework.jpaquery.repository.annotations.CnName;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.util.Date;

/**
 * 办件结果
 * @author zhengjc
 */
@ApiModel(value="办件结果")
@CnName("办件结果")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BjResults extends BaseSimpleEntity {

    @CnName("记录唯一标识")
    private String rowGuid;

    @CnName("办件编号")
    private String projectNo;

    @CnName("业务办理项编码")
    private String taskHandleItem;

    @CnName("办理部门所在地行政区域代码")
    private String regionCode;

    @CnName("办理部门统一社会信用代码")
    private String orgCode;

    @CnName("办理部门名称")
    private String orgName;

    @CnName("办结人员姓名")
    private String userName;

    @CnName("办结时间")
    private String resultDate;

    @CnName("办理结果")
    private String resultType;

    @CnName("办件结果描述")
    private String resultExplain;

    @CnName("同步类型")
    private String cdOperation;

    @CnName("同步时间")
    private Date cdTime;

    @CnName("批次号")
    private String cdBatch;

    @CnName("深圳市事项编码")
    private String itemCode;

    @CnName("申办流水号")
    private String sblsh;

    @CnName("短申办流水号")
    private String sblshShort;

    @CnName("是否处理 （0：否；1：是；2：异常）")
    private int status;

    @CnName("年份")
    private String year;

    public String getRowGuid() {
        return rowGuid;
    }

    public void setRowGuid(String rowGuid) {
        this.rowGuid = rowGuid;
    }

    public String getProjectNo() {
        return projectNo;
    }

    public void setProjectNo(String projectNo) {
        this.projectNo = projectNo;
    }

    public String getTaskHandleItem() {
        return taskHandleItem;
    }

    public void setTaskHandleItem(String taskHandleItem) {
        this.taskHandleItem = taskHandleItem;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getResultDate() {
        return resultDate;
    }

    public void setResultDate(String resultDate) {
        this.resultDate = resultDate;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultExplain() {
        return resultExplain;
    }

    public void setResultExplain(String resultExplain) {
        this.resultExplain = resultExplain;
    }

    public String getCdOperation() {
        return cdOperation;
    }

    public void setCdOperation(String cdOperation) {
        this.cdOperation = cdOperation;
    }

    public Date getCdTime() {
        return cdTime;
    }

    public void setCdTime(Date cdTime) {
        this.cdTime = cdTime;
    }

    public String getCdBatch() {
        return cdBatch;
    }

    public void setCdBatch(String cdBatch) {
        this.cdBatch = cdBatch;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getSblsh() {
        return sblsh;
    }

    public void setSblsh(String sblsh) {
        this.sblsh = sblsh;
    }

    public String getSblshShort() {
        return sblshShort;
    }

    public void setSblshShort(String sblshShort) {
        this.sblshShort = sblshShort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
