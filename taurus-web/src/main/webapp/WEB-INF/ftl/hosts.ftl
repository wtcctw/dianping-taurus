<!DOCTYPE html >
<html >
<head>
	
	<title>Taurus</title>
	<#include "segment/html_header.ftl">
	<script type="text/javascript" src="${rc.contextPath}/resource/js/lib/Chart.js"></script>
	<#include "segment/html_header2.ftl">
	<style>
        .spann {
            width: 95%;
        }
    </style>
    <link rel="stylesheet" type="text/css" href="${rc.contextPath}/css/DT_bootstrap.css">
    <link href="${rc.contextPath}/css/viewlog.css" rel="stylesheet" type="text/css">
</head>
<body data-spy="scroll">
<#include "segment/header.ftl">
<#include "segment/left.ftl">

<div class="mid-div col-sm-12">
<div class="page-content col-sm-12">
<div class="sidebar col-sm-2 no-padding-left">
<#include "hostList.ftl">
</div>

<div class="main-content">
<div class="page-content col-sm-12">
<#if statusCode! == "200">
<div id="alertContainer" class="container">
    <div id="alertContainerSuccess" class="alert alert-success">
        <button type="button" class="close" data-dismiss="alert">×</button>
        ${opChs!}成功
    </div>
</div>
<#elseif statusCode! == "500">
<div id="alertContainer" class="container">
    <div id="alertContainerError" class="alert alert-danger">
        <button type="button" class="close" data-dismiss="alert">×</button>
        ${opChs!}失败
    </div>
</div>
</#if>
<#if dto?exists>

<ul class="nav nav-tabs">
    <li class="active"><a href="#state" data-toggle="tab">运行状态</a></li>
    <li class=""><a href="#taskmonitor" data-toggle="tab">任务监控</a></li>
    <li class=""><a href="#log" data-toggle="tab">日志</a></li>
    <li class=""><a href="#statistics" data-toggle="tab">统计</a></li>
</ul>

<#-- agent机器详情 start -->
<div class="tab-content col-sm-12">

<#-- 运行状态标签 start -->
<div class="tab-pane active in" id="state">
    <table class="table table-striped table-bordered table-hover " id="host_state">
        <thead>
        <tr>
            <th>#</th>
            <th>属性</th>
            <th>值</th>
            <#if isAdmin><th>操作</th></#if>
        </tr>
        </thead>
        <tbody>
        <tr class="success">
            <td>1</td>
            <td>机器IP</td>
            <td>${hostName!}</td>
            <#if isAdmin><td></td></#if>
        </tr>
        <tr class="error">
            <td>2</td>
            <td>机器状态</td>
        <#if dto.isOnline()>
            <td>在线</td>
            <#if isAdmin>
            <td><a id="down" title="这台agent将不在监控范围内，agent进程是否被kill并不能确定。" class="btn  btn-primary btn-minier"
                   href="${rc.contextPath}/updateHost?hostName=${hostName!}&op=down">下线</a></td>
            </#if>
        <#else>
            <td>下线</td>
            <#if isAdmin>
            <td><a id="up" title="这台agent将被纳入监控范围内，agent需要手动启动。" class="btn btn-primary btn-minier"
                   href="${rc.contextPath}/updateHost?hostName=${hostName!}&op=up">上线</a></td>
            </#if>
        </#if>
        </tr>
        <tr class="warning">
            <td>3</td>
            <td>心跳状态</td>
        <#if dto.isConnected()>
            <td>正常</td>
            <#if isAdmin>
            <td><a id="restart" class="btn  btn-primary btn-minier"
                   href="${rc.contextPath}/updateHost?hostName=${hostName!}&op=restart">重启</a></td>
            </#if>
        <#else>
            <td>异常</td>
            <#if isAdmin>
            <td>无法重启</td>
            </#if>
        </#if>
        </tr>
        <tr class="info">
            <td>4</td>
            <td>版本</td>
            <td><#if dto.info?exists>${dto.info.agentVersion!}<#else>异常，无法获得版本号</#if></td>
        <#if dto.isConnected()>
            <#if isAdmin>
            <td><a id="update" class="btn btn-primary btn-minier"
                   href="http://code.dianpingoa.com/arch/taurus/rollout_branches">升级</a></td>
            </#if>
        <#else>
            <#if isAdmin>
            <td><a id="update" class="btn btn-primary btn-minier"
                   href="http://code.dianpingoa.com/arch/taurus/rollout_branches">升级</a></td>
            </#if>
        </#if>
        </tr>

        <tr class="info">
            <td>5</td>
            <td>主机任务执行历史</td>
            <td><a id="history" class="btn btn-primary btn-minier"
                   href="${rc.contextPath}/host_history?ip=${hostName!}">查看</a></td>
        </tr>
        </tbody>
    </table>
</div>
<#-- 运行状态标签 end -->

<#-- 任务监控标签 start -->
<div class="tab-pane" id="taskmonitor">

<#-- 正在运行的任务 start -->
<ul class="run-tag col-sm-12">
    <li><a>正在运行的任务<span class="label label-info">RUNNING</span></a></li>
</ul>
<ul class="breadcrumb col-sm-12">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover" id="running">
        <thead>
        <tr>
            <th>任务ID</th>
            <th>任务名称</th>
            <th>实际启动时间</th>
            <th>实际结束时间</th>
            <th>预计调度时间</th>
            <!-- <th>IP</th> -->
            <th>查看日志</th>
        </tr>
        </thead>
        <tbody>
<#if attempts?exists>
<#list attempts as hostDto>

<#if hostName?? && hostName == hostDto.execHost>
    <#if tasks?exists && hostDto.startTime?exists && hostDto.status == "RUNNING" &&
     (hostDto.startTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") lte nowTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") && (hostDto.endTime?? == false || hostDto.endTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") gte nowTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") )) >
        <#list tasks as task>
		<#if task.taskid == hostDto.taskID>
        <tr id="${hostDto.attemptID!}">
            <td>${hostDto.taskID!}</td>
            <td>${task.name!}</td>
            <td>${(hostDto.startTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
            <td>${(hostDto.endTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
            <td>${(hostDto.scheduleTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
            <td>
                <a target="_blank"
                   href="${rc.contextPath}/viewlog?id=${hostDto.attemptID!}&status=${hostDto.status!}">日志</a>
            </td>

        </tr>
    	<#break>
		</#if>
		</#list>
	</#if>
</#if>

</#list>
</#if>
        </tbody>
    </table>
</ul>
<#-- 正在运行的任务 end -->

<#-- 提交失败的任务 start -->
<ul class="submit-fail-tag col-sm-12">
    <li><a>提交失败的任务 <span class="label label-important">SUBMIT_FAIL</span></a></li>
</ul>
<ul class="breadcrumb col-sm-12">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover " id="submitfail">
        <thead>
        <tr>
            <th>任务ID</th>
            <th>任务名称</th>
            <th>实际启动时间</th>
            <th>实际结束时间</th>
            <th>预计调度时间</th>
            <!-- <th>IP</th> -->
            <th>查看日志</th>
        </tr>
        </thead>
        <tbody>

<#if submitFailAttempts?exists>
<#list submitFailAttempts as hostDto>
	<#list tasks as task>
	<#if task.taskid == hostDto.taskID>

    <#if hostName?exists && hostName == hostDto.execHost>
        <#if taskTime?exists>
            <#if hostDto.startTime?exists && hostDto.status == "SUBMIT_FAIL" && 
            (hostDto.startTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") gte taskDateTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") || hostDto.endTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") gte taskDateTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") )> 

	        <tr id="${hostDto.attemptID!}">
	        	<td>${hostDto.taskID!}</td>
	            <td>${task.name!}</td>
	            <td>${(hostDto.startTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>${(hostDto.endTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>${(hostDto.scheduleTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>

	            <td>
	                <a target="_blank"
	                   href="${rc.contextPath}/viewlog?id=${hostDto.attemptID!}&status=${hostDto.status!}">日志</a>
	            </td>

	        </tr>
        	</#if>
        <#else>
        	<#if hostDto.status == "SUBMIT_FAIL">

	        <tr id="${hostDto.attemptID!}">
	        	<td>${hostDto.taskID!}</td>
	            <td>${task.name!}</td>
	            <td>${(hostDto.startTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>${(hostDto.endTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>${(hostDto.scheduleTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>
	                <a target="_blank"
	                   href="${rc.contextPath}/viewlog?id=${hostDto.attemptID!}&status=${hostDto.status!}">日志</a>
	            </td>

	        </tr>
    		</#if>
        </#if>
    </#if>

    <#break>
	</#if>
	</#list>
</#list>
</#if>
        </tbody>
    </table>
</ul>
<#-- 提交失败的任务 end -->

<#-- 失败的任务 start -->
<ul class="fail-tag col-sm-12">
    <li><a>失败的任务 <span class="label label-important">FAILED</span></a></li>
</ul>
<ul class="breadcrumb col-sm-12">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover " id="fail">
        <thead>
        <tr>
            <th>任务ID</th>
            <th>任务名称</th>
            <th>实际启动时间</th>
            <th>实际结束时间</th>
            <th>预计调度时间</th>
            <!-- <th>IP</th> -->
            <th>查看日志</th>
        </tr>
        </thead>
        <tbody>

<#if failAttempts?exists>
<#list failAttempts as hostDto>
	<#list tasks as task>
	<#if task.taskid == hostDto.taskID>

	<#if hostName?exists && hostName == hostDto.execHost>
		<#if taskTime?exists>
			<#if hostDto.startTime?exists && hostDto.status == "FAILED" && (hostDto.startTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") gte taskDateTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") || hostDto.endTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") gte taskDateTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") )> 
        	<tr id="${hostDto.attemptID!}">
	        	<td>${hostDto.taskID!}</td>
	            <td>${task.name!}</td>
	            <td>${(hostDto.startTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>${(hostDto.endTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>${(hostDto.scheduleTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>
	                <a target="_blank"
	                   href="${rc.contextPath}/viewlog?id=${hostDto.attemptID!}&status=${hostDto.status!}">日志</a>
	            </td>

	        </tr>
    		</#if>
    	<#else>
    		<#if hostDto.status == "FAILED">
        	<tr id="${hostDto.attemptID!}">
	        	<td>${hostDto.taskID!}</td>
	            <td>${task.name!}</td>
	            <td>${(hostDto.startTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>${(hostDto.endTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>${(hostDto.scheduleTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>
	                <a target="_blank"
	                   href="${rc.contextPath}/viewlog?id=${hostDto.attemptID!}&status=${hostDto.status!}">日志</a>
	            </td>

	        </tr>
    		</#if>
    	</#if>

	</#if>

	<#break>
	</#if>
	</#list>
</#list>
</#if>

        </tbody>
    </table>
</ul>
<#-- 失败的任务 end -->

<#-- 依赖超时的任务 start -->
<ul class="dependency-timeout-tag col-sm-12">
    <li><a>依赖超时的任务<span class="label label-important">DEPENDENCY_TIMEOUT</span></a></li>
</ul>
<ul class="breadcrumb col-sm-12">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover " id="dependency-timeout">
        <thead>
        <tr>
            <th>任务ID</th>
            <th>任务名称</th>
            <th>实际启动时间</th>
            <th>实际结束时间</th>
            <th>预计调度时间</th>
            <!-- <th>IP</th> -->
            <th>查看日志</th>

        </tr>
        </thead>
        <tbody>
            
<#if dependencyTimeOutAttempts?exists>
<#list dependencyTimeOutAttempts as hostDto>
	<#list tasks as task>
	<#if task.taskid == hostDto.taskID>

	<#if hostName?exists && hostName == hostDto.execHost>
		<#if taskTime?exists>
			<#if hostDto.startTime?exists && hostDto.status == "DEPENDENCY_TIMEOUT" && (hostDto.startTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") gte taskDateTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") || hostDto.endTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") gte taskDateTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") )> 
			<tr id="${hostDto.attemptID!}">
	        	<td>${hostDto.taskID!}</td>
	            <td>${task.name!}</td>
	            <td>${(hostDto.startTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>${(hostDto.endTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>${(hostDto.scheduleTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>
	                <a target="_blank"
	                   href="${rc.contextPath}/viewlog?id=${hostDto.attemptID!}&status=${hostDto.status!}">日志</a>
	            </td>

	        </tr>
    		</#if>
    	<#else>
    		<#if hostDto.status == "DEPENDENCY_TIMEOUT"> 
			<tr id="${hostDto.attemptID!}">
	        	<td>${hostDto.taskID!}</td>
	            <td>${task.name!}</td>
	            <td>${(hostDto.startTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>${(hostDto.endTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>${(hostDto.scheduleTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>
	                <a target="_blank"
	                   href="${rc.contextPath}/viewlog?id=${hostDto.attemptID!}&status=${hostDto.status!}">日志</a>
	            </td>

	        </tr>
    		</#if>
        </#if>
	</#if>

	<#break>
	</#if>
	</#list>
</#list>
</#if>
        </tbody>
    </table>
</ul>
<#-- 依赖超时的任务 end -->

<#-- 超时的任务 start -->
<ul class="timeout-tag col-sm-12">
    <li><a>超时的任务<span class="label label-important">TIMEOUT</span></a></li>
</ul>
<ul class="breadcrumb col-sm-12">
    <table cellpadding="0" cellspacing="0" border="0"
           class="table table-striped table-format table-hover " id="timeout">
        <thead>
        <tr>
            <th>任务ID</th>
            <th>任务名称</th>
            <th>实际启动时间</th>
            <th>实际结束时间</th>
            <th>预计调度时间</th>
            <!-- <th>IP</th> -->
            <th>查看日志</th>

        </tr>
        </thead>
        <tbody>
            
<#if timeOutAttempts?exists>
<#list timeOutAttempts as hostDto>
	<#list tasks as task>
	<#if task.taskid == hostDto.taskID>

	<#if hostName?exists && hostName == hostDto.execHost>
		<#if taskTime?exists>
			<#if hostDto.startTime?exists && hostDto.status == "TIMEOUT" && (hostDto.startTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") gte taskDateTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") || hostDto.endTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") gte taskDateTime?string("yyyy-MM-dd HH:mm")?date("yyyy-MM-dd HH:mm") )> 
			<tr id="${hostDto.attemptID!}">
	        	<td>${hostDto.taskID!}</td>
	            <td>${task.name!}</td>
	            <td>${(hostDto.startTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>${(hostDto.endTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>${(hostDto.scheduleTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>
	                <a target="_blank"
	                   href="${rc.contextPath}/viewlog?id=${hostDto.attemptID!}&status=${hostDto.status!}">日志</a>
	            </td>

	        </tr>
    		</#if>
    	<#else>
    		<#if hostDto.status == "TIMEOUT"> 
			<tr id="${hostDto.attemptID!}">
	        	<td>${hostDto.taskID!}</td>
	            <td>${task.name!}</td>
	            <td>${(hostDto.startTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>${(hostDto.endTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>${(hostDto.scheduleTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
	            <td>
	                <a target="_blank"
	                   href="${rc.contextPath}/viewlog?id=${hostDto.attemptID!}&status=${hostDto.status!}">日志</a>
	            </td>

	        </tr>
    		</#if>
		</#if>
	</#if>

	<#break>
	</#if>
	</#list>
</#list>
</#if>
        </tbody>
    </table>
</ul>
<#-- 超时的任务 end -->

</div>
<#-- 任务监控标签 end -->

<#-- 日志标签 start -->
<div class="tab-pane" id="log">
    <div id="log-panel">
        <div class="spann" id="spann">
            <ul class="run-tag">
                <li><a>日志信息<span class="label label-info">STDOUT</span></a></li>
            </ul>
            <div data-spy="scroll" data-offset="0" style="height: 510px; line-height: 20px; overflow: auto;"
                 class="terminal terminal-like col-sm-12" id="strout">
            </div>
        </div>
    </div>
</div>
<#-- 日志标签 end -->

<#-- 统计标签 start -->
<div class="tab-pane" id="statistics">
</div>
<#-- 统计标签 end -->

</div>
<#-- agent机器详情 end -->

</#if><#-- dto判空结束 -->


</div>
</div>
</div>
</div>

<div class="feedTool">
    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img border="0" src="${rc.contextPath}/img/qq.png"  width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img border="0" src="${rc.contextPath}/img/x_alt.png"  width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>
    <p style="text-align: center; padding-right:32px;color: firebrick">点我报错</p>
</div>

<script type="text/javascript">
    $(".atip").tooltip();
</script>
<script src="${rc.contextPath}/static/js/hosts.js" type="text/javascript"></script>

</body>
</html>