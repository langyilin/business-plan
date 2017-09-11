<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- MAIN CONTENT -->
<div id="content">
	<!-- widget grid -->
	<section widget-grid id="visitList">
		<div class="row">
			<article class="col-sm-12">
				<div jarvis-widget id="standard-datatable-widget" data-widget-editbutton="true">
					<header>
						<ul class="nav nav-tabs">
	                        <li ng-class="{active: activeTab == 1}"><a href="javascript:;" ng-click="switchTab(1)"><i class="fa {{crrentMenu.smallicon}}"></i>&nbsp;待处理</a></li>
	    					<li ng-class="{active: activeTab == 2}"><a href="javascript:;" ng-click="switchTab(2)"><i class="fa {{crrentMenu.smallicon}}"></i>&nbsp;待签收</a></li>
	    					<li ng-class="{active: activeTab == 3}"><a href="javascript:;" ng-click="switchTab(3)"><i class="fa {{crrentMenu.smallicon}}"></i>&nbsp;已办</a></li>
	    					<li ng-class="{active: activeTab == 4}"><a href="javascript:;" ng-click="switchTab(4)"><i class="fa {{crrentMenu.smallicon}}"></i>&nbsp;超期</a></li>
	  					</ul>
					</header>
					<div>
						<div class="widget-body">
							<div class="dataTables_wrapper form-inline no-footer">
								<form id="visitForm" ng-submit="search();">							
									<label><input type="search" class="form-control" placeholder="标题" ng-model="visit.title"></label>
									<label>
										<button type="submit" class="btn btn-default"><i class="fa fa-search"></i>&nbsp;查询 </button>
									</label>
									<label>
										<button type="button" class="btn btn-primary" data-ui-sref="app.visit.form({id:visit.id})"><i class="fa fa-plus-circle"></i>&nbsp;新增 </button>
									</label>
									
									<table class="table table-striped table-bordered table-hover " width="100%">
										<thead>
											<tr>
												<th>序号</th>
												 <th>标题</th>
												<th>操作环节</th>
												<th>完成时间</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>
											<tr ng-repeat="visit in visitList">
												<td><span class="badge bg-color-blueLight" >{{$index+1 +(paginationConf.currentPage-1)*paginationConf.itemsPerPage}}</span></td>
												<td>
												<a data-ui-sref="app.visit.form({id:visit.businessKey,taskId:visit.id})">
													{{visit.businessName}}
													</a>
												</td>
												<td>
													{{visit.name}}
												</td>
												
												<td>
													{{visit.createTime | date:'yyyy-MM-dd HH:mm:ss'}}
												</td>
												<td>
														<a ng-show="activeTab==2" class="btn btn-info btn-xs" ng-click="claim(visit.id)"><i class="glyphicon glyphicon-edit"></i>&nbsp;签收</a>
														<a class="btn btn-info btn-xs" ng-click="viewHistory(visit.processInstanceId)"><i class="glyphicon glyphicon-edit"></i>&nbsp;流程图</a>
												</td>
											</tr>
										</tbody>
									</table>
									
									<xy-pagination conf="paginationConf"></xy-pagination>
								</form>
							</div>
						</div>
					</div>
				</div>
			</article>
		</div>
	</section>
</div>