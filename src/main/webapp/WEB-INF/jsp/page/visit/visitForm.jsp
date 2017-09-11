<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id="content">
	<section id="widget-grid" widget-grid>
		<div class="row">
			<article class="col-sm-12 col-md-12 col-lg-12">
				<div jarvis-widget>
					<header>
						<ul class="nav nav-tabs" ng-init="activeTab=1">
	                        <li ng-class="{active: activeTab == 1}"><a href="javascript:;" ng-click="activeTab = 1"><i class="fa fa-edit"></i>&nbsp;信访信息</a></li>
	    					<li ng-show="visit.processInstanceId" ng-class="{active: activeTab == 2}"><a href="javascript:;" ng-click="activeTab = 2"><i class="fa fa-reorder"></i>&nbsp;流程图</a></li>
	    					<li ng-show="visit.processInstanceId" ng-class="{active: activeTab == 3}"><a href="javascript:;" ng-click="activeTab = 3"><i class="fa fa-reorder"></i>&nbsp;流程跟踪</a></li>
	  					</ul>
					</header>
					<div>
						<div class="widget-body no-padding">
							<form class="smart-form" name="visitForm" id="visitForm" ng-submit="save();" ng-cloak>
								<input type="hidden" name="id" ng-model="visit.id" />
								<input type="hidden" name="processDefinitionId" value="XF_HQ:103:400077"/>
								<fieldset ng-show="activeTab == 1">
									<div class="row">
										<section class="col col-4">
											<label class="label"><font color="red">标题*</font></label>
											<label class="input">
												<input type="text" id="title" name="title" ng-model="visit.title"  xy-require submit-btn-id="submit">
											</label>
										</section>
										<section class="col col-4">
											<label class="label"><font color="red">内容*</font></label>
											<label class="input">
												<input type="text" id="contnts" name="contnts" ng-model="visit.contnts"  xy-require submit-btn-id="submit">
											</label>
										</section>
										<section class="col col-4">
											<label class="label">来信时间</label>
											<label class="input"> 
												<i class="icon-append fa fa-calendar"></i>
												<input type="text" id="createDate" name="createDate" class="form-control" datepicker dateFmt="yyyy-MM-dd HH:mm:ss" ng-model="visit.createDate" />
											</label>
										</section>
									</div>
									<div class="row">
										<section class="col col-4">
											<label class="label"><font color="red">办理方式*</font></label>
											<label class="input">
												<select xy-select style="width:100%;" xy-require submit-btn-id="submit" ng-model="visit.type" data-options="{'dictCode': 'VISIT_TYPE'}" class="select2" placeholder="办理方式">
												</select>
											</label>
										</section>
											<section class="col col-4">
											<label class="label">期限</label>
											<label class="input"> 
												<i class="icon-append fa fa-calendar"></i>
												<input type="text" id="outDate" name="outDate" class="form-control" datepicker dateFmt="yyyy-MM-dd HH:mm:ss" ng-model="visit.outDate" />
											</label>
										</section>
										<section class="col col-4">
											<label class="label">状态</label>
											<label class="input">
											<input type="text" id="status" name="status" ng-model="visit.status" placeholder="状态" readonly/>
												<!-- <select xy-select style="width:100%;"  ng-model="visit.status" data-options="{'dictCode': 'VISIT_STATUS'}" class="select2" placeholder="状态">
												</select> -->
											</label>
										</section>
									</div>
									<div class="row">
										<section class="col col-4">
											<label class="label"><font color="red">处理单位*</font></label>
											<label class="input">
												<i class="icon-append fa fa-cubes" title="处理单位"></i>
												<input type="hidden" name="cldwid" ng-model="visit.cldwid" >
												<input xy-orgselect id="cldwname" name="cldwname" callback="setParent(data)" type="text" ng-model="visit.cldwname" xy-require submit-btn-id="submit" placeholder="处理单位"  />
											</label>
										</section>
										<section class="col col-4">
											<label class="label">创建人</label>
											<label class="input">
											<input type="text" id="createbyName" name="createbyName" ng-model="visit.createbyName" readonly placeholder="创建人"/>
											</label>
										</section>
									</div>
									<div class="row">
										<section class="col col-12">
											<label class="label">备注</label>
											<label class="textarea">
												<textarea rows="4" id="remarks" name="remarks" ng-model="visit.remarks" placeholder="备注" xy-max-length="100"  submit-btn-id="submit"></textarea>
											</label>
										</section>
									</div>
									<div class="row">
									<xy-formApplication ng-model="moduleList"></xy-formApplication>
									</div>
								</fieldset>
								<fieldset ng-if="activeTab == 2">
									<iframe scrolling="no" id="diagram" ng-src="{{diagramUrl}}" style="width:100%;height: 100%;" frameborder="0"></iframe>
								</fieldset>
								<fieldset ng-if="activeTab == 3">
									<iframe scrolling="no" id="diagram" ng-src="{{diagramUrl2}}" style="width:100%;height: 100%;" frameborder="0"></iframe>
								</fieldset>
								<footer>
									<button type="button" class="btn btn-danger" ng-click="backProcess();"><i class="fa fa-bomb"></i> 退回</button>
									<button type="submit" id="submit" class="btn btn-primary"><i class="fa fa-save"></i> 提交</button>
									<button type="button" class="btn btn-default" onclick="window.history.back();">返回</button>
								</footer>
							</form>
						</div>
					</div>
				</div>
			</article>
		</div>
	</section>
</div>