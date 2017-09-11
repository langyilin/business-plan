package com.xuanyuan.visit.lestener;


import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.ExecutionListener;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

import com.xuanyuan.auth.user.service.UserServiceI;
import com.xuanyuan.visit.entity.VisitEntity;
import com.xuanyuan.visit.service.VisitServiceI;
import com.xuanyuan.workflow.application.service.ProcessServiceI;
import com.xuanyuan.workflow.application.service.TaskServiceI;

public class VisitWfeLitener implements TaskListener,JavaDelegate   {

	@Autowired
	private VisitServiceI visitService;//信件接口
	
	@Autowired
    private RepositoryService repositoryService;

    @Autowired
    private TaskServiceI taskService;
    
    @Autowired
    private RuntimeService runtimeService;
    
    @Autowired
    private ProcessServiceI processService;
    
    @Autowired
    private UserServiceI userService;
    
	@Override
	public void notify(DelegateTask delegateTask) {
		delegateTask.setVariable("blr","4028b0945b617d69015b618219720006,4028b0945b8a5863015b8a5ac3730001");
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		//delegateTask.setAssignee("4028b0945b617d69015b617f88890001");
		//delegateTask.setCategory("4028b0945b617d69015b617f88890001");
		System.out.println(delegateTask.getName()+",任务监听器启动,信访id："+delegateTask.getExecution().getProcessBusinessKey());
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		execution.setVariable("blr","4028b0945b617d69015b618219720006,4028b0945b8a5863015b8a5ac3730001");

	}

}
