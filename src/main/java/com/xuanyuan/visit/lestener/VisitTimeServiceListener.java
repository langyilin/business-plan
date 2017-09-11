package com.xuanyuan.visit.lestener;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class VisitTimeServiceListener implements JavaDelegate{
	

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("信件："+execution.getProcessBusinessKey()+execution.getCurrentActivityName());
		
	}

}
