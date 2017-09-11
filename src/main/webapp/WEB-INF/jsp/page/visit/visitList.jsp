<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- MAIN CONTENT -->
<div id="content">
	<!-- widget grid -->
	<section widget-grid id="visitList">
		<div class="row">
			<article class="col-sm-12">
				<div jarvis-widget id="standard-datatable-widget" data-widget-editbutton="true">
					<header>
						<span class="widget-icon"><i class="fa fa-table"></i></span>
						<h2>信件</h2>
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
												<th>来信时间</th>
												<th>类型</th>
												<th>创建人</th>
												<th>状态</th>
												<th>操作</th>
											</tr>
										</thead>
										<tbody>
											<tr ng-repeat="visit in visitList">
												<td><span class="badge bg-color-blueLight" >{{$index+1 +(paginationConf.currentPage-1)*paginationConf.itemsPerPage}}</span></td>
												<td><a data-ui-sref="app.visit.form({id:visit.id})">
													{{visit.title}}
												</a></td>
												<td>
													{{visit.createDate | date:'yyyy-MM-dd HH:mm:ss'}}
												</td>
												<td>
													{{visit.type|xyFilter:'VISIT_TYPE'}}
												</td>
												<td>
													{{visit.createbyName}}
												</td>
												<td>
													{{visit.status}}
												</td>
												<td>
													<a class="btn btn-info btn-xs" data-ui-sref="app.visit.form({id:visit.id})"><i class="glyphicon glyphicon-edit"></i>&nbsp;修改</a>
														<a class="btn btn-info btn-xs" ng-show="visit.processInstanceId" ng-click="viewHistory(visit.processInstanceId)"><i class="fa  fa-sitemap "></i>&nbsp;流程图</a>
													<a class="btn btn-danger btn-xs" ng-click="del(visit.id)"><i class="glyphicon glyphicon-trash "></i>&nbsp;删除</a>
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