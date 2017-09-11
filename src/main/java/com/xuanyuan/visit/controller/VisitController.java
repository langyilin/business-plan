package com.xuanyuan.visit.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;//谷歌Maps对象
import com.xuanyuan.auth.cache.CacheUserManager;
import com.xuanyuan.auth.user.entity.LogonEntity;
import com.xuanyuan.auth.user.entity.UserEntity;
import com.xuanyuan.auth.user.service.UserServiceI;
import com.xuanyuan.auth.util.SessionUtil;//Session工具类，获取用户信息
import com.xuanyuan.common.hibernate.qbc.PageList;//分页对象
import com.xuanyuan.common.web.BaseController;//基础控制器
import com.xuanyuan.config.cache.CachePageModuleManager;
import com.xuanyuan.config.entity.CfgModuleEntity;
import com.xuanyuan.config.entity.CfgModuleTypeEntity;
import com.xuanyuan.config.entity.CfgPageEntity;
import com.xuanyuan.utils.StringUtils;//StringUtils工具类
import com.xuanyuan.visit.entity.VisitEntity;//Visit对象
import com.xuanyuan.visit.service.VisitServiceI;//信件接口
import com.xuanyuan.workflow.application.service.ProcessServiceI;
import com.xuanyuan.workflow.application.service.TaskServiceI;
import com.xuanyuan.workflow.utils.BeanUtils;
import com.xuanyuan.workflow.utils.FormUtils;

/**
 * 信件 控制层
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
@Controller
@RequestMapping(value = "visit/visit")
public class VisitController extends BaseController {

	@Autowired
	private VisitServiceI visitService;//信件接口
	
	@Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskServiceI taskService;
    
    @Autowired
	private TaskService actTaskService;
    
    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private ProcessServiceI processService;
    
    @Autowired
    private UserServiceI userService;
    
	
	/**
	 * get 方法
	 * @descript：TODO 通用获取对象，方法中加入VisitEntity visit 会调用此方法
	 * @param  id
	 * @return VisitEntity
	 * @author 何阳
	 * @date 2017年04月06日 09:36:58
	 */
	@ModelAttribute
	public VisitEntity get(@RequestParam(required=false) String id) {
		VisitEntity entity = null;
		if (StringUtils.isNotBlank(id)){
			entity = visitService.get(VisitEntity.class, id);
		}
		if (entity == null){
			entity = new VisitEntity();
		}
		return entity;
	}
	
	/**
	 * list 方法
	 * @descript：TODO (跳转到列表页面)
	 * @return String
	 * @author 何阳
	 * @date 2017年04月06日 09:36:58
	 */
	@RequestMapping(value = {"list", ""})
	public String list() {
		return "page/visit/visitList";
	}
	
	/**
	 * list 方法
	 * @descript：TODO (跳转到列表页面)
	 * @return String
	 * @author 何阳
	 * @date 2017年04月06日 09:36:58
	 */
	@RequestMapping(value = {"listDb", ""})
	public String listDb() {
		return "page/visit/myVisitList";
	}
	
	
	/**
	 * ajaxList 方法
	 * @descript：TODO (列表页异步分页查询)
	 * @param VisitEntity visit
	 * @param pageNo  页码
	 * @param pageSize  分页大小
	 * @return String 返回结果集(json形式)
	 * @author 何阳
	 * @date 2017年04月06日 09:36:58
	 */
	@ResponseBody
	@RequestMapping(value = {"ajaxList"}, method = RequestMethod.POST)
	public String ajaxList(VisitEntity visit, @RequestParam(value = "pageNo", defaultValue = "1") int pageNo, 
	                       @RequestParam(value = "pageSize", defaultValue = "15") int pageSize, HttpServletResponse response) {
	    //跟进pageNo与pageSize分页查询信件列表数据
	    PageList<VisitEntity> page = visitService.findPage(visit, pageNo, pageSize); 
	    return renderString(response, page);
	}
	
	@ResponseBody
	@RequestMapping(value = {"ajaxDbList"}, method = RequestMethod.POST)
	public String ajaxDbList(VisitEntity visit, @RequestParam(value = "pageNo", defaultValue = "1") int pageNo, 
	                       @RequestParam(value = "pageSize", defaultValue = "15") int pageSize, HttpServletResponse response, HttpServletRequest request) {
	    //跟进pageNo与pageSize分页查询信件列表数据
		visit.setAssigneeName(SessionUtil.getLogon(request.getSession()).getUserEntity().getUserid());
	    PageList<VisitEntity> page = visitService.findPage(visit, pageNo, pageSize); 
	    return renderString(response, page);
	}
	
	/**
	 * 查询个人待办
	 * ajaxMyVisitList 方法
	 * @descript：TODO
	 * @param task
	 * @param pageNo
	 * @param pageSize
	 * @param response
	 * @param request
	 * @return
	 * @return PageList<Map<String,Object>>
	 * @author 何阳
	 * @date 2017年4月12日-下午2:52:55
	 */
 	@ResponseBody
    @RequestMapping(value = {"ajaxMyVisitList"})
    public PageList<Map<String,Object>> ajaxMyVisitList(TaskEntity task, @RequestParam(value = "pageNo", defaultValue = "1") int pageNo, 
                           @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, HttpServletResponse response, HttpServletRequest request) {
        LogonEntity logon = SessionUtil.getLogon(request.getSession());
        if(logon==null)return null;
        PageList<Map<String,Object>> page =taskService.userTaskListByPage(task, logon.getUserid(), pageNo, pageSize);
        return page;
    }

	/**
	 * form 方法
	 * @descript：TODO (进入新增、查看或者修改信件页面)
	 * @return String
	 * @author 何阳
	 * @date   2017年04月06日 09:36:58
	 */
	@RequestMapping(value = "form")
	public String form() {
		return "page/visit/visitForm";
	}
	
	/**
	 * initForm 方法
	 * @descript：TODO (初始化表单和列表需要的对象、数据字典等信息)
	 * @param VisitEntity visit
	 * @return String
	 * @author 何阳
	 * @date 2017年04月06日 09:36:58
	 */
	@ResponseBody
	@RequestMapping(value = "initForm", method = RequestMethod.POST) 
	public String initForm(VisitEntity visit, HttpServletResponse response,HttpServletRequest request){
		Map<String, Object> map = Maps.newHashMap();
		if(visit!=null&&visit.getId()!=null){
			String pageCode = "VISIT_JBRBL";
			String uid = SessionUtil.getLogon(request.getSession()).getUserid();
			if(visit.getProcessInstanceId()!=null){
				List<Task> tasks = taskService.findTasksByProcessInstanceId(visit.getProcessInstanceId());
				Task  task = null;
				for(Task t:tasks){
					if(uid.equals(t.getAssignee())){
						task = t;
						break;
					}
				}
				if(task!=null){
			        map.put("task", BeanUtils.convertTaskEntity(task, actTaskService));
			        pageCode = taskService.findTaskFormKey(task.getId());
			        //获得页面配置
			        CfgPageEntity page = CachePageModuleManager.getPageEntityByCode(pageCode);
				}else{
					pageCode="";
				}
			}
	        
	        //获得页面配置
	        CfgPageEntity page = CachePageModuleManager.getPageEntityByCode(pageCode);
	        if(page!=null){
	        //获得页面模板
	        CfgModuleTypeEntity moduleType = CachePageModuleManager.getModuleTypeEntity(page.getModuleTypeId());
	        List<CfgModuleEntity> moduleList = CachePageModuleManager.getModuleList(moduleType.getModuleTypeId());
	        map.put("moduleType", moduleType);
		    map.put("moduleList", moduleList);
	        }
		    map.put("pageCode", pageCode);
	        map.put("page", page);
			map.put("visit", visit);//初始化数据
		}else{
			//获得页面配置
	        CfgPageEntity page = CachePageModuleManager.getPageEntityByCode("VISIT_JBRBL");
	        //获得页面模板
	        CfgModuleTypeEntity moduleType = CachePageModuleManager.getModuleTypeEntity(page.getModuleTypeId());
	        List<CfgModuleEntity> moduleList = CachePageModuleManager.getModuleList(moduleType.getModuleTypeId());
	        map.put("moduleType", moduleType);
	        map.put("moduleList", moduleList);
	        visit = new VisitEntity();
	        visit.setCreateDate(new Date());
	        map.put("visit", visit);//初始化数据
		}
		Map<String,String> userIdToNameMap = CacheUserManager.getAllUserIdNameMap();
		 if(visit.getCreateBy() != null&&visit.getCreateBy().length()==32){
			 visit.setCreatebyName(userIdToNameMap.get(visit.getCreateBy()));
         }else{
        	 visit.setCreatebyName(visit.getCreateBy());
         }
		return renderString(response, map);
	}

   /**
	 * save 方法
	 * @descript：TODO (新增或者修改信件[Visit]对象)
	 * @param  Visit 
	 * @param  response
	 * @return String
	 * @author 何阳
	 * @date   2017年04月06日 09:36:58
	 */
	@ResponseBody
	@RequestMapping(value = "save", method = RequestMethod.POST)
	public String save(@RequestBody JSONObject object, HttpServletRequest request, HttpServletResponse response) {
		String result = "failed";
		Map<String, Object> map = Maps.newHashMap();
		try{
			String flag = object.getString("flag");
			String taskId = object.getString("taskId");
			VisitEntity visit = JSONObject.parseObject(object.toJSONString(), VisitEntity.class);
			// 保存前进行服务端数据验证
			if (!beanValidator(map, visit)){
			    return renderString(response, map.get("message"));
			}
			
			if(StringUtils.isBlank(visit.getId())) {
	            visit.preInsert();
	            visit.setCreateBy(SessionUtil.getLogon(request.getSession()).getUserid());
	        }else {
	            visit.preUpdate();
	            visit.setUpdateBy(SessionUtil.getLogon(request.getSession()).getUserid());
	        }
			//暂存
			if("6".equals(flag)){
				visit.setStatus("1");
				visitService.saveOrUpdate(visit);//新增或者修改Visit对象
				result = "success";
				return renderString(response, result);//操作成功
			}
			Map<String, Object> param = FormUtils.convertTaskFormProperties(request);
			param.put("userid", SessionUtil.getLogon(request.getSession()).getUserid());
			param.put("flag",flag);
			param.put("cldw", "4028b0945b617d69015b618219720006");
			param.put("jbr",visit.getCreateBy());
			UserEntity user = new UserEntity();
			user.setOrgid(visit.getCldwid());
			List<UserEntity> users = userService.findList(user);
			StringBuffer cldws = new StringBuffer();
			List<String> assigneeList=new ArrayList<String>(); //分配任务的人员
			for(UserEntity u:users){
				//cldws.append(u.getUserid()).append(",");
				assigneeList.add(u.getUserid());
			}
			param.put("assigneeList", assigneeList);
			Task task = null;
			 LogonEntity logon = SessionUtil.getLogon(request.getSession());

			if(StringUtils.isEmpty(visit.getProcessInstanceId())){
				visitService.saveOrUpdate(visit);//新增或者修改Visit对象
				Authentication.setAuthenticatedUserId(logon.getUserid());
				//启动工作流(key，业务Id，参数)
				ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("ZLC_XF",visit.getId(), param);
	            //初始化流程实例的名称(流程定义名称+业务标题)
	            runtimeService.setProcessInstanceName(processInstance.getId(), visit.getTitle());
	            //更新最新任务(下个任务)的发起人为当前任务处理人
	            List<Task> tasks = taskService.findTasksByProcessInstanceId(processInstance.getId());
	            for(Task t:tasks){
	            	 task = t;
	            	actTaskService.addComment(t.getId(), null, "");
	            }
	            ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();  
	            List<FormProperty> list = processEngine.getFormService().getTaskFormData(task.getId()).getFormProperties();  
		        if(list!=null && list.size()>0){  
		            for(FormProperty formProperty:list){
		            	if("status".equals(formProperty.getId())){
		            		//更新信件状态
		            		visit.setStatus(formProperty.getValue());
		            		break;
		            	}
		            }  
		        }
	            visit.setStatus(task.getName());
			}else{
				 List<Task> tasks = taskService.findTasksByProcessInstanceId(visit.getProcessInstanceId());
				 
				 //流程提交(任务id,执行用户id,参数)
				 taskService.doTask(taskId,logon.getUserid(),param);
				 
				 
			     tasks = taskService.findTasksByProcessInstanceId(visit.getProcessInstanceId());
			     for(Task t:tasks){
					if(logon.getUserid().equals(t.getAssignee())){
						task = t;
						break;
					}
			     }
			}
			if(task==null){
				
			}else{
	            visit.setAssigneeName(task.getAssignee());
	            visit.setTaskId(task.getId());
	            visit.setUpdateDate(task.getCreateTime());
				//业务表绑定流程实例 
				visit.setProcessInstanceId(task.getProcessInstanceId());
		        visit.setStatus(task.getName());
			}
			
			
			visitService.saveOrUpdate(visit);//新增或者修改Visit对象
			result = "success";
		}catch(Exception e){
			e.printStackTrace();
		}
		return renderString(response, result);//操作成功
	}
	
	/**
	 * 我的已办
	 * myHistoryVisitList 方法
	 * @descript：TODO
	 * @param task
	 * @param pageNo
	 * @param pageSize
	 * @param request
	 * @return
	 * @return PageList<Map<String,Object>>
	 * @author 何阳
	 * @date 2017年4月7日-下午4:01:00
	 */
 	@ResponseBody
    @RequestMapping(value = {"myHistoryVisitList"})
    public PageList<Map<String,Object>> myHistoryVisitList(TaskEntity task, @RequestParam(value = "pageNo", defaultValue = "1") int pageNo, 
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,HttpServletRequest request) {
        LogonEntity logon = SessionUtil.getLogon(request.getSession());
        if(logon==null)return null;
        return taskService.userHistoryTaskList(task, logon.getUserid(), pageNo, pageSize);
    }
 	
 	/**
 	 * 查询组待办
 	 * ajaxGoupVisitList 方法
 	 * @descript：TODO
 	 * @param task
 	 * @param pageNo
 	 * @param pageSize
 	 * @param response
 	 * @param request
 	 * @return
 	 * @return PageList<Map<String,Object>>
 	 * @author 何阳
 	 * @date 2017年4月12日-下午2:53:21
 	 */
 	@ResponseBody
    @RequestMapping(value = {"ajaxGoupVisitList"})
    public PageList<Map<String,Object>> ajaxGoupVisitList(TaskEntity task, @RequestParam(value = "pageNo", defaultValue = "1") int pageNo, 
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize, HttpServletResponse response, HttpServletRequest request) {
        List<String> roleIdList = SessionUtil.getLogonRoleIds(request.getSession());
        if(roleIdList==null)return null;
        PageList<Map<String,Object>> page =taskService.groupsTaskListByPage(task, roleIdList, pageNo, pageSize);
        return page;
    }
	
	/**
	 * 
	 * delete 方法
	 * @descript：TODO (删除信件[Visit]对象)
	 * @param  Visit
	 * @param  response
	 * @return String
	 * @author 何阳
	 * @date   2017年04月06日 09:36:58
	 */
	@ResponseBody
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	public String delete(VisitEntity visit, HttpServletResponse response) {
		String result = "failed";
		try{
			visitService.updateDelFlag(visit.getId(), null); //删除Visit对象
			result = "success";
		}catch(Exception e){
			e.printStackTrace();
		}
		return renderString(response, result);//操作成功
	}
	
	
    @ResponseBody
    @RequestMapping(value = {"startFlow"})
    public String startFlow(@RequestParam(value = "processDefinitionId", defaultValue = "") String processDefinitionId,HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> param = FormUtils.convertTaskFormProperties(request);
		param.put("userid", SessionUtil.getLogon(request.getSession()).getUserid());
		//启动工作流
		return processService.startFlow(processDefinitionId, param);
   }

}