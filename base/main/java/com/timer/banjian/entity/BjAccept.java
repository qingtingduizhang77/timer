package com.timer.banjian.entity;

import com.timer.common.entity.BaseSimpleEntity;
import io.swagger.annotations.ApiModel;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import swallow.framework.jpaquery.repository.annotations.CnName;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import java.util.Date;

/**
 * 办件受理
 * @author zhengjc
 */
@ApiModel(value="办件受理")
@CnName("办件受理")
@Entity
@EntityListeners(AuditingEntityListener.class)
public class BjAccept extends BaseSimpleEntity {

    @CnName("记录唯一标识")
    private String rowGuid;

    @CnName("办件编号")
    private String projectNo;

    @CnName("申办流水号")
    private String sblsh;

    @CnName("基本编码")
    private String catalogCode;

    @CnName("地方基本编码")
    private String localCatalogCode;

    @CnName("实施编码")
    private String taskCode;

    @CnName("地方实施编码")
    private String localTaskCode;

    @CnName("业务办理项编码")
    private String taskHandleItem;

    @CnName("事项名称")
    private String taskName;

    @CnName("申请人名称")
    private String applyerName;

    @CnName("申请人类型")
    private String applyerType;

    @CnName("申请人证件类型")
    private String applyerPageType;

    @CnName("申请人证件号码")
    private String applyerPageCode;

    @CnName("申请时间")
    private Date applyDate;

    @CnName("申请类型")
    private String applyType;

    @CnName("受理部门")
    private String orgName;

    @CnName("受理部门编码")
    private String orgCode;

    @CnName("受理状态代码")
    private String slztdm;

    @CnName("受理人员")
    private String handleusername;

    @CnName("受理时间")
    private Date acceptDate;

    @CnName("承诺办结时间")
    private Date promisedate;

    @CnName("申办表单数据")
    private String applyForm;

    @CnName("同步类型")
    private String cdOperation;

    @CnName("同步时间")
    private Date cdTime;

    @CnName("批次号")
    private String cdBatch;

    @CnName("深圳市事项编码")
    private String itemCode;

    @CnName("短申办流水号")
    private String sblshShort;

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

    public String getSblsh() {
        return sblsh;
    }

    public void setSblsh(String sblsh) {
        this.sblsh = sblsh;
    }

    public String getCatalogCode() {
        return catalogCode;
    }

    public void setCatalogCode(String catalogCode) {
        this.catalogCode = catalogCode;
    }

    public String getLocalCatalogCode() {
        return localCatalogCode;
    }

    public void setLocalCatalogCode(String localCatalogCode) {
        this.localCatalogCode = localCatalogCode;
    }

    public String getTaskCode() {
        return taskCode;
    }

    public void setTaskCode(String taskCode) {
        this.taskCode = taskCode;
    }

    public String getLocalTaskCode() {
        return localTaskCode;
    }

    public void setLocalTaskCode(String localTaskCode) {
        this.localTaskCode = localTaskCode;
    }

    public String getTaskHandleItem() {
        return taskHandleItem;
    }

    public void setTaskHandleItem(String taskHandleItem) {
        this.taskHandleItem = taskHandleItem;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getApplyerName() {
        return applyerName;
    }

    public void setApplyerName(String applyerName) {
        this.applyerName = applyerName;
    }

    public String getApplyerType() {
        return applyerType;
    }

    public void setApplyerType(String applyerType) {
        this.applyerType = applyerType;
    }

    public String getApplyerPageType() {
        return applyerPageType;
    }

    public void setApplyerPageType(String applyerPageType) {
        this.applyerPageType = applyerPageType;
    }

    public String getApplyerPageCode() {
        return applyerPageCode;
    }

    public void setApplyerPageCode(String applyerPageCode) {
        this.applyerPageCode = applyerPageCode;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(String orgCode) {
        this.orgCode = orgCode;
    }

    public String getSlztdm() {
        return slztdm;
    }

    public void setSlztdm(String slztdm) {
        this.slztdm = slztdm;
    }

    public String getHandleusername() {
        return handleusername;
    }

    public void setHandleusername(String handleusername) {
        this.handleusername = handleusername;
    }

    public Date getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
    }

    public Date getPromisedate() {
        return promisedate;
    }

    public void setPromisedate(Date promisedate) {
        this.promisedate = promisedate;
    }

    public String getApplyForm() {
        return applyForm;
    }

    public void setApplyForm(String applyForm) {
        this.applyForm = applyForm;
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

    public String getSblshShort() {
        return sblshShort;
    }

    public void setSblshShort(String sblshShort) {
        this.sblshShort = sblshShort;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
