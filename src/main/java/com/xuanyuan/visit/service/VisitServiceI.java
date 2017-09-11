package com.xuanyuan.visit.service;

import java.util.List;

import com.xuanyuan.common.hibernate.qbc.PageList;
import com.xuanyuan.common.service.ICommonService;
import com.xuanyuan.visit.entity.VisitEntity;

/**
 * 信件 接口
 * <ul>
 * <li>项目名：基础架构平台V2.0</li>
 * <li>版本信息：2017-04-06</li>
 * <li>日期：2017年04月06日 09:36:58</li>
 * <li>版权所有(C)2016广东轩辕网络科技股份有限公司-版权所有</li>
 * <li>创建人: 何阳</li>
 * <li>创建时间：2017年04月06日 09:36:58</li>
 * <li>修改人1：</li>
 * <li>修改时间1：</li>
 * <li>修改内容1：</li>
 * </ul>
 */
public interface VisitServiceI extends ICommonService {

  	/**
	 * findList 方法
	 * @descript：TODO(根据VisitEntity查询列表数据，返回List<VisitEntity>)
	 * @param  Visit 
	 * @return List<VisitEntity>
	 * @author 何阳
	 * @date   2017年04月06日 09:36:58
	 */ 
	public List<VisitEntity> findList(VisitEntity visit);
	
	/**
	 * findPage 方法
	 * @descript：TODO(分页查询VisitEntity对象，返回PageList<VisitEntity>)
	 * @param  Visit
	 * @param  pageNo   当前页码
	 * @param  pageSize 每页显示数量
	 * @return PageList<VisitEntity>
	 * @author 何阳
	 * @date   2017年04月06日 09:36:58
	 */
	public PageList<VisitEntity> findPage(VisitEntity visit, Integer pageNo, Integer pageSize);
	
	/**
     * 更新删除标识
     * updateDelFlag 方法
     * @descript：逻辑删除数据
     * @param id
     * @param delFlag 删除标记（0：正常；1：删除；2：审核）
     * @return
     * @author 何阳
     * @date   2017年04月06日 09:36:58
     */
	public void updateDelFlag(String id, String delFlag);
	
	
}