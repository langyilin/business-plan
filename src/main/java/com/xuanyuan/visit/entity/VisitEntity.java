package com.xuanyuan.visit.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Transient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.xuanyuan.utils.StringUtils;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xuanyuan.common.model.DataEntity;
import java.util.Date;

/**
 * 信件 Entity
 * <ul>
 * <li>项目名：基础架构平台V2.0</li>
 * <li>版本信息：2017-04-06</li>
 * <li>日期：2017年04月06日 09:36:58</li>
 * <li>版权所有(C)2016广东轩辕网络科技股份有限公司-版权所有</li>
 * <li>创建人:何阳</li>
 * <li>创建时间：2017年04月06日 09:36:58</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
@Entity
@Table(name = "visit")
public class VisitEntity extends DataEntity<VisitEntity> implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	@Column(name = "title")
	@Length(min = 0, max = 100, message = "标题长度不能大于100")
	private String title;		// 标题	
	
	@Column(name = "contnts")
	@Length(min = 0, max = 500, message = "内容长度不能大于500")
	private String contnts;		// 内容	
	
	
	
	@Column(name = "type")
	@Length(min = 0, max = 100, message = "类型长度不能大于100")
	private String type;		// 类型	
	
	@Column(name = "processInstanceId")
	@Length(min = 0, max = 32, message = "流程实例id")
	private String processInstanceId;		// 类型	

	@Column(name = "out_date")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date outDate;		// 期限	
	
	@Column(name = "status")
	@Length(min = 0, max = 100, message = "状态长度不能大于100")
	private String status;		// 状态	
	
	@Column(name = "taskId")
	@Length(min = 0, max = 100, message = "长度不能大于32")
	private String taskId;		// 任务id
	
	@Column(name = "cldwid")
	@Length(min = 0, max = 100, message = "长度不能大于100")
	private String cldwid;		// 处理单位
	
	@Transient
	private java.lang.String createbyName;
	
	public java.lang.String getCreatebyName() {
		return createbyName;
	}


	public void setCreatebyName(java.lang.String createbyName) {
		this.createbyName = createbyName;
	}


	public String getCldwid() {
		return cldwid;
	}


	@Column(name = "assigneeName")
	@Length(min = 0, max = 100, message = "长度不能大于100")
	private String assigneeName;		// 任务id
	
	@Column(name = "cldwname")
	@Length(min = 0, max = 100, message = "长度不能大于100")
	private String cldwname;		// 处理单位
	
	public void setCldwid(String cldwid) {
		this.cldwid = cldwid;
	}

	
	public void setCldwname(String cldwname) {
		this.cldwname = cldwname;
	}

	public String getCldwname() {
		return cldwname;
	}

	
	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getAssigneeName() {
		return assigneeName;
	}

	public void setAssigneeName(String assigneeName) {
		this.assigneeName = assigneeName;
	}

	public VisitEntity() {
		super();
	}

	public VisitEntity (String id){
		super(id);
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getContnts() {
		return contnts;
	}

	public void setContnts(String contnts) {
		this.contnts = contnts;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public Date getOutDate() {
		return outDate;
	}

	public void setOutDate(Date outDate) {
		this.outDate = outDate;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}
}