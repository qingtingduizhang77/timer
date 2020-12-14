package com.timer.common.entity;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import swallow.framework.jpaquery.repository.annotations.CnName;

import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
public class BaseSimpleEntity extends OnlyIdEntity {
	@ApiModelProperty(value="创建时间",name="created",example="")
	@CnName("创建时间")
    @CreatedDate
    private Date created;
	 
    @ApiModelProperty(value="最后修改时间",name="lastmodi",example="")
    @CnName("最后修改时间")
    @LastModifiedDate
    private Date lastmodi;
    
    
    
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getLastmodi() {
		return lastmodi;
	}
	public void setLastmodi(Date lastmodi) {
		this.lastmodi = lastmodi;
	}
    
    
    
}
