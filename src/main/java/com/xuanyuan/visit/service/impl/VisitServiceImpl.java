package com.xuanyuan.visit.service.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.stereotype.Service;

import com.xuanyuan.auth.cache.CacheOrgManager;
import com.xuanyuan.auth.cache.CacheUserManager;
import com.xuanyuan.auth.user.entity.UserEntity;
import com.xuanyuan.common.hibernate.qbc.CriteriaQuery;
import com.xuanyuan.common.hibernate.qbc.PageList;
import com.xuanyuan.common.model.SortDirection;
import com.xuanyuan.common.service.impl.CommonService;
import com.xuanyuan.utils.StringUtils;
import com.xuanyuan.visit.service.VisitServiceI;
import com.xuanyuan.visit.entity.VisitEntity;

/**
 * 信件 接口实现
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
@Service
@SuppressWarnings("unchecked")
public class VisitServiceImpl extends CommonService implements VisitServiceI{

    /**
	 * getCriteriaQuery 方法
	 * @descript：TODO(获取动态查询条件）
	 * @param  visit  查询对象
	 * @param  pageNo        当前页码
	 * @param  pageSize      每页显示条数
	 * @return CriteriaQuery 动态查询条件
	 * @author 何阳
	 * @date   2017年04月06日 09:36:58
	 */
	private CriteriaQuery getCriteriaQuery(VisitEntity visit, Integer pageNo, Integer pageSize) {
		CriteriaQuery cq = new CriteriaQuery(VisitEntity.class);
		if(null != pageNo && null != pageSize) {
			cq.setCurPage(pageNo);
        	cq.setPageSize(pageSize);
        }
		if(StringUtils.isNotBlank(visit.getTitle())) {
			cq.like("title", StringEscapeUtils.unescapeHtml(visit.getTitle()));
		}
		if(StringUtils.isNotBlank(visit.getStatus())) {
			cq.eq("status", StringEscapeUtils.unescapeHtml(visit.getStatus()));
		}
		if(StringUtils.isNotBlank(visit.getAssigneeName())) {
			cq.eq("assigneeName", StringEscapeUtils.unescapeHtml(visit.getAssigneeName()));
		}
		cq.eq("delFlag", visit.getDelFlag());
		cq.addOrder("updateDate", SortDirection.desc);
		cq.add();
		return cq;
	}

	/**
	 * findList 方法
	 * @descript：TODO (重写查询方法，返回 List<VisitEntity>)
	 * @param visit 实体类
	 * @return List<VisitEntity>
	 * @author 何阳
	 * @date 2017年04月06日 09:36:58
	 */
	@Override
	public List<VisitEntity> findList(VisitEntity visit) {
		return super.findListByCriteriaQuery(getCriteriaQuery(visit, null, null), false);
	}
	
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
	@Override
	public PageList<VisitEntity> findPage(VisitEntity visit, Integer pageNo, Integer pageSize) {
	   PageList pagelist = super.findPageList(getCriteriaQuery(visit, pageNo, pageSize), true);
	   List<VisitEntity> dataList = pagelist.getResultList();
	   Map<String, String> userIdToNameMap = CacheUserManager.getAllUserIdNameMap();
       for(VisitEntity v : dataList){
           if(v.getCreateBy() != null&&v.getCreateBy().length()==32){
               v.setCreatebyName(userIdToNameMap.get(v.getCreateBy()));
           }else{
               v.setCreatebyName(v.getCreateBy());
           }
       }
       pagelist.setResultList(dataList);
       return pagelist;
	}
	
	@Override
    public void updateDelFlag(String id, String delFlag){
        this.executeHql("update VisitEntity set delFlag = ? where id = ?", StringUtils.isNotBlank(delFlag) ? delFlag : "1", id);
    }
	
}