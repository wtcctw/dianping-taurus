<!DOCTYPE html >
<html >
<head>
	
	<title>Taurus</title>
	<#include "segment/html_header.ftl">
	<link rel="stylesheet" href="${rc.contextPath}/css/jquery-ui.min.css"/>
	<script type="text/javascript" src="${rc.contextPath}/js/jquery-ui.min.js"></script>
	<script type="text/javascript" charset="utf-8" language="javascript" src="${rc.contextPath}/js/jquery.dataTables.js"></script>
    <script type="text/javascript" charset="utf-8" language="javascript" src="${rc.contextPath}/js/DT_bootstrap.js"></script>
	<#include "segment/html_header2.ftl">
	<link href="${rc.contextPath}/css/bwizard.min.css" rel="stylesheet"/>
	<style>
        label.error {
            margin-left: 10px;
            color: red;
        }

        label.success {
            margin-left: 10px;
            color: green;
        }

        .creatorbtn {
            float: left;
        }
    </style>
</head>
<body>
<#include "segment/header.ftl">
<#include "segment/left.ftl">


<div class="main-content ">

<div class="breadcrumbs" id="breadcrumbs">
    <script type="text/javascript">
        try {
            ace.settings.check('breadcrumbs', 'fixed')
        } catch (e) {
        }
    </script>
    <ul class="breadcrumb">
        <li>
            <i class="icon-home home-icon"></i>
            <a href="${rc.contextPath}/mvc/index">HOME</a>
        </li>
        <li class="active">
            <a href="${rc.contextPath}/mvc/resign">离职交接</a>
        </li>
    </ul>
</div>



<div class="page-content">
    


<#-- 此处原 for和if 开始 -->
		<#if user.group?exists == false || user.group == "">
		<#assign userGroup = "null">
		<#-- 未设置分组不能交接提示 start -->
				<div id="alertContainer" class="container col-sm-12">
				    <div id="alertContainer" class="alert alert-danger">
				        <button type="button" class="close" data-dismiss="alert">×</button>
				        你未设分组，不能交接任务，请到<a href="${rc.contextPath}/mvc/user">用户设置</a>中设置自己的分组~
				    </div>
				</div>
		<#else>
		<#assign userGroup = user.group>
				<div id="alertContainer" class="container col-sm-12"></div>
		</#if>
		<#-- 未设置分组不能交接提示 end -->



<div class="container " style="margin-top: 10px">
<#-- row start -->
<div class="row">

<div class="col-sm-12 padding-8">

	<#if userGroup != "null">
        	<div class=" creatorbtn col-sm-12"></div>
	</#if>
            <br/>

<div class="col-sm-12">

<#if tasks?exists == false || tasks?size == 0 >
        <div class="align-center col-sm-7">
            <i class='icon-info-sign icon-large red '>你没有创建任何任务～</i>
        </div>
<#else>

    <#-- 选择交接的任务 start -->
    <div class="col-sm-9 no-padding-left">
        <#-- widget-box start -->
        <div class="widget-box no-padding-left">

                <div class="widget-header header-color-green">
                    <h5 class="widget-title">
                        <i class="icon-tasks"></i>
                        <a class="atip tooltip-info" data-toggle="tooltip" data-placement="bottom"
                           data-original-title="你只能把自己组的任务指派给自己组的成员，如果你需要交接给别的组请联系 李明 【kirin.li@dianping.com】">[提示] </a>
                        选择你要交接的任务
                    </h5>

                    <div class="widget-toolbar">
                        <a href="#" data-action="collapse">
                            <i class="icon-chevron-up"></i>
                        </a>
                    </div>
                    <div class="widget-toolbar">
                        <button class="btn btn-xs btn-yellow" type="button" id="creatorbtn">
                            <i class="ace-icon fa fa-check bigger-110"></i>
                            交接任务
                        </button>
                        <a class="atip tooltip-info" data-toggle="tooltip" data-placement="bottom"
                           data-original-title="此操作为危险操作，你所做的所有操作将审计下来，如果不是你的任务，你指派给了其他人，在没有项目组的人授权情况下是违规的！！！">[警告] </a>
                    </div>
                </div>

                <#-- widget-body start -->
                <div class="widget-body">
                    <div class="widget-main " id="taskwidget">
                        <table cellpadding="0" cellspacing="0" border="0" class="table table-striped table-bordered table-hover"
                               width="100%" id="regintask">
                            <thead>
                            <tr>
                                <th class="hide">ID</th>
                                <th width=>名称</th>
                                <th>IP</th>
                                <th>调度人</th>
                                <th class="hide">调度身份</th>
                                <th class="hide">组</th>
                                <th class="hide">创建时间</th>
                                <th>Crontab</th>
                                <th>状态</th>
                            </tr>
                            </thead>
                            <tbody>

		<#list tasks as dto>
			<#assign isRunning = true>
			<#if dto.status == "SUSPEND">
				<#assign isRunning = false>
			</#if>

						<#if isRunning>
                            <tr id="${dto.taskid!}">
						<#else>
                            <tr id="${dto.taskid!}" class="error">
            			</#if>
                                <td class="hide">${dto.taskid!}</td>
                                <td class="fixLength-td"><input type="checkbox" class="field taskcheckbox"
                                                                id="${dto.creator!}" name="taskcheckbox"
                                                                value="${dto.name!}" alertUser = "${dto.getAlertRule().userid!}" alertId="${dto.getAlertRule().jobid!}" >${dto.name!}
                                </td>
                                <td>${dto.hostname!}</td>
                                <td>${dto.creator!}</td>
                                <td class="hide">${dto.proxyuser!}</td>
                                <td class="hide">arch(mock)</td>
                                <td class="hide">${(dto.addtime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
                                <td>${dto.crontab!}</td>
                                <td>
							<#if isRunning>
                                    <span class="label label-info">${dto.status!}</span>
							<#else>
                                    <span class="label label-important">${dto.status!}</span>
                            </#if>
                                </td>

                            </tr>
        </#list>



                            </tbody>
                        </table>
                     </div>
                </div><#-- widget-body end -->

        </div><#-- widget-box end -->

    </div><#-- 选择交接的任务 end -->

</#if>

<#-- 此处原for和if结束 -->



    <#-- 选择指派人 start -->
    <div class="col-sm-3" style="opacity: 0.5">
<#if userGroup == "null">
        你未设置分组！
<#else>
        <div class="widget-box">
            <div class="widget-header header-color-red3">
                <h5 class="widget-title">
                    <i class="icon-user"></i>
                    选择你要指派的人
                </h5>

                <div class="widget-toolbar">
                    <a href="#" data-action="collapse">
                        <i class="icon-chevron-up"></i>

                    </a>
                </div>
            </div>

            <div class="widget-body">
                <div class="widget-main" id="userwidget">
			<#if userGroup?contains("admin") == false> <#-- TODO 普通组成员能加入最多3个分组，查看交接的组和成员(完成) -->
                    <table class="table table-striped table-bordered table-hover reginuser" >
                        <tr>
                            <th align="left">成员</th>
                            <th align="left">组名</th>
                        </tr>
						<#if map?exists>
			                <#list map?keys as group>
			                <#if userGroup?contains(group)><#-- TODO 改为userGroup包含group的子串(完成) -->
                                <#list hHelper.getGroupUserList(map[group]) as creator>
			                        <tr>
			                            <td align="left">
			                                <input type="radio" value="${creator!}" name="creator">${creator!}
			                            </td>
                                        <td align="left">${group!}</td>
			                        </tr>
                    			</#list>
                    		</#if>
                            </#list>
			            </#if>
            <#else> <#-- admin组可以交接任务给所有的分组和成员 -->
                        <table class="table table-striped table-bordered table-hover reginuser" >
                            <tr>
                                <th align="left">成员</th>
                                <th align="left">组名</th>
                            </tr>
						<#if map?exists>
			                <#list map?keys as group>
                                <#list hHelper.getGroupUserList(map[group]) as creator>
		                            <tr>
		                                <td align="left">
		                                    <input type="radio" value="${creator!}" name="creator">${creator!}
		                                </td>
		                                <td align="left">${group!}</td>
		                            </tr>
                                </#list>
                            </#list>
			            </#if>

            </#if>
                        </table>

                </div>
                <!-- /.widget-main -->
            </div>
            <!-- /.widget-body -->
        </div>
        <!-- /.widget-box -->
</#if>
        <br/>

    </div><#-- 选择指派人 end -->



</div>
</div>


<div class="col-sm-12" id="adjustout"></div>

</div><#-- row end -->

</div>
</div>
</div>

<#-- 这处可以做一个ftl模块 -->
<a href="#" class="scrollup" style="display: inline;">
    <img src="${rc.contextPath}/img/betop.png" width="66" height="67">
</a>
<div class="feedTool">
    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img border="0" src="${rc.contextPath}/img/qq.png"  width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img border="0" src="${rc.contextPath}/img/x_alt.png"  width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>
    <p style="text-align: center; padding-right:32px;color: firebrick">点我报错</p>
</div>


<script type="text/javascript">
    $('li[id="resign"]').addClass("active");
    $('#menu-toggler').on(ace.click_event, function () {
        $('#sidebar').toggleClass('display');
        $(this).toggleClass('display');
        return false;
    });

    var userList = "", groupList = "", isAdmin;
    userList = userList
    <#if users??>
    <#list users as user>
    	+",${user.name!}"
    </#list>
    </#if>
    	;
    groupList = groupList
    <#if groups??>
    <#list groups as group>
    	+",${group.name!}"
    </#list>
    </#if>
    	;
    isAdmin = ${isAdmin?c};
    userList = userList.substr(1);
    groupList = groupList.substr(1);




</script>
<script src="${rc.contextPath}/js/jquery.validate.min.js" type="text/javascript"></script>
<script src="${rc.contextPath}/static/js/resign.js" type="text/javascript"></script>
<script>
    jQuery(function ($) {
        $('#regintask').dataTable({
            "bAutoWidth": true,
            "bPaginate": false,
            "bFilter": true,
            "bInfo": false,
            "bLengthChange": false

        });
        $(".reginuser").dataTable({
            "bAutoWidth": true,
            "bPaginate": false,
            "bFilter": true,
            "bInfo": false,
            "bLengthChange": false

        });
    });
    var currentUser = "${currentUser!}"
    var userId = ${userId!}
</script>

</body>
</html>