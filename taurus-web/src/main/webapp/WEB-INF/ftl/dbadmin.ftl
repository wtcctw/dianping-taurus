<!DOCTYPE html >
<html >
<head>
	
	<title>后台管理系统</title>
	<#include "segment/html_header.ftl">
	<script type="text/javascript" src="${rc.contextPath}/js/jquery-ui.min.js"></script>
	<script type="text/javascript" src="${rc.contextPath}/resource/js/lib/Chart.js"></script>
	<script type="text/javascript" src="${rc.contextPath}/lib/ace/js/jquery.flot.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/lib/ace/js/jquery.flot.pie.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/lib/ace/js/bootstrap-datepicker.min.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/lib/ace/js/daterangepicker.min.js"></script>
    <script src="${rc.contextPath}/lib/ace/js/jquery.dataTables.min.js"></script>
    <script src="${rc.contextPath}/lib/ace/js/jquery.dataTables.bootstrap.js"></script>
    <script type="text/javascript" src="${rc.contextPath}/static/js/dbadmin.js"></script>
	<#include "segment/html_header2.ftl">
	<link rel="stylesheet" href="${rc.contextPath}/css/jquery-ui.min.css"/>
    <link rel="stylesheet" href="${rc.contextPath}/resource/css/monitor-center.css">


</head>
<body>
<#include "segment/header.ftl">
<#include "segment/left.ftl">

<div class="main-content  " style="opacity: 1;">
    <div class="breadcrumbs" id="breadcrumbs">
        <script type="text/javascript">
            try {
                ace.settings.check('breadcrumbs', 'fixed')
            } catch (e) {
            }
        </script>
        <ul class="breadcrumb">
            <li class="active">
                <i class="icon-home home-icon"></i>
                <a href="${rc.contextPath}/mvc/index">HOME</a>
            </li>
        </ul>
    </div>
    <div class="page-content">
        <div class="row">
            <div id="cleardependence" class="col-sm-12">
                <div class="col-sm-6">
                    <div class="widget-box">
                        <div class="widget-header widget-header-flat widget-header-small">
                            <h5 class="widget-title">
                                <i class="icon-signal"></i>
                                清理操作
                            </h5>

                            <div class="widget-toolbar">
                                <a href="#" data-action="collapse">
                                    <i class="icon-chevron-up"></i>
                                </a>
                            </div>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main" id="user-widget-main" style="height: 150px">


                                <div class="col-sm-12">
                                    <div class="col-sm-2">
                                        <label class="label label-lg label-info arrowed-right">taskId:</label>
                                    </div>
                                    <div class="col-sm-8">
                                        <input id="sqlinput" type="text" class="form-control">
                                    </div>


                                </div>
                                <div class="col-sm-12">
                                    <div class="col-sm-2">
                                        <label class="label label-lg label-info arrowed-right">status:</label>
                                    </div>
                                    <div class="col-sm-8">
                                       <input
                                                id="status" name="value" type="text" class="ui-spinner-input"
                                                >
                                    </div>


                                </div>

                                <div class="col-sm-12">


                                    <div class="col-sm-8">

                                    </div>
                                    <div class="col-sm-4">
                                        <button class="btn btn-info" type="button" id="querybtn">
                                            <i class="ace-icon fa fa-check bigger-110"></i>
                                            执行
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- /.widget-main -->
                    </div>
                    <!-- /.widget-body -->
                </div>
                <!-- /.widget-box -->
                <div class="col-sm-6">
                    <div class="widget-box">
                        <div class="widget-header widget-header-flat widget-header-small">
                            <h5 class="widget-title">
                                <i class="icon-signal"></i>
                                查询结果
                            </h5>

                            <div class="widget-toolbar">
                                <a href="#" data-action="collapse">
                                    <i class="icon-chevron-up"></i>
                                </a>
                            </div>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main" id="group-widget-main">


                                <div id="sqloutput"><i class="icon-spinner icon-spin icon-large"></i></div>

                                <!-- /section:custom/extra.grid -->
                            </div>
                        </div>
                        <!-- /.widget-main -->
                    </div>
                    <!-- /.widget-body -->
                    <!-- /.widget-box -->

                </div>
            </div>
            <div id="clearzookeeper" class="col-sm-12">
                <div class="col-sm-6">
                    <div class="widget-box">
                        <div class="widget-header widget-header-flat widget-header-small">
                            <h5 class="widget-title">
                                <i class="icon-signal"></i>
                                清理Zookeeper节点操作
                            </h5>

                            <div class="widget-toolbar">
                                <a href="#" data-action="collapse">
                                    <i class="icon-chevron-up"></i>
                                </a>
                            </div>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main" id="clear-widget-main" style="height: 150px">


                                <div class="col-sm-12">
                                    <div class="col-sm-3">
                                        <label class="label label-lg label-info arrowed-right">start(负数):</label>
                                    </div>
                                    <div class="col-sm-8">
                                        <input id="start" type="text" class="form-control">
                                    </div>


                                </div>
                                <div class="col-sm-12">
                                    <div class="col-sm-3">
                                        <label class="label label-lg label-info arrowed-right">end(负数):</label>
                                    </div>
                                    <div class="col-sm-8">
                                        <input
                                                id="end" name="value" type="text" class="ui-spinner-input"
                                                >
                                    </div>


                                </div>

                                <div class="col-sm-12">


                                    <div class="col-sm-8">

                                    </div>
                                    <div class="col-sm-4">
                                        <button class="btn btn-info" type="button" id="clearbtn">
                                            <i class="ace-icon fa fa-check bigger-110"></i>
                                            执行
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- /.widget-main -->
                    </div>
                    <!-- /.widget-body -->
                </div>
                <!-- /.widget-box -->
                <div class="col-sm-6">
                    <div class="widget-box">
                        <div class="widget-header widget-header-flat widget-header-small">
                            <h5 class="widget-title">
                                <i class="icon-signal"></i>
                                执行结果
                            </h5>

                            <div class="widget-toolbar">
                                <a href="#" data-action="collapse">
                                    <i class="icon-chevron-up"></i>
                                </a>
                            </div>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main" id="clearoupt-widget-main">


                                <div id="clearoutput"><i class="icon-spinner icon-spin icon-large"></i></div>

                                <!-- /section:custom/extra.grid -->
                            </div>
                        </div>
                        <!-- /.widget-main -->
                    </div>
                    <!-- /.widget-body -->
                    <!-- /.widget-box -->

                </div>
            </div>
            <div id="adjustcreator" class="col-sm-12">
                <div class="col-sm-6">
                    <div class="widget-box">
                        <div class="widget-header widget-header-flat widget-header-small">
                            <h5 class="widget-title">
                                <i class="icon-signal"></i>
                                迁移job 修改调度人操作
                            </h5>

                            <div class="widget-toolbar">
                                <a href="#" data-action="collapse">
                                    <i class="icon-chevron-up"></i>
                                </a>
                            </div>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main" id="adjustcreator-widget-main" style="height: 150px">


                                <div class="col-sm-12">
                                    <div class="col-sm-3">
                                        <label class="label label-lg label-info arrowed-right">TaskName:</label>
                                    </div>
                                    <div class="col-sm-9">
                                        <input id="taskName" type="text" class="form-control">
                                    </div>


                                </div>
                                <div class="col-sm-12">
                                    <div class="col-sm-3">
                                        <label class="label label-lg label-info arrowed-right">新的调度人</label>
                                    </div>
                                    <div class="col-sm-9">
                                        <input
                                                id="creator" name="value" type="text" class="ui-spinner-input"
                                                >
                                    </div>


                                </div>

                                <div class="col-sm-12">


                                    <div class="col-sm-8">

                                    </div>
                                    <div class="col-sm-4">
                                        <button class="btn btn-info" type="button" id="creatorbtn">
                                            <i class="ace-icon fa fa-check bigger-110"></i>
                                            执行
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- /.widget-main -->
                    </div>
                    <!-- /.widget-body -->
                </div>
                <!-- /.widget-box -->
                <div class="col-sm-6">
                    <div class="widget-box">
                        <div class="widget-header widget-header-flat widget-header-small">
                            <h5 class="widget-title">
                                <i class="icon-signal"></i>
                                执行结果
                            </h5>

                            <div class="widget-toolbar">
                                <a href="#" data-action="collapse">
                                    <i class="icon-chevron-up"></i>
                                </a>
                            </div>
                        </div>

                        <div class="widget-body">
                            <div class="widget-main" id="adjustout-widget-main">


                                <div id="adjustout"><i class="icon-spinner icon-spin icon-large"></i></div>

                                <!-- /section:custom/extra.grid -->
                            </div>
                        </div>
                        <!-- /.widget-main -->
                    </div>
                    <!-- /.widget-body -->
                    <!-- /.widget-box -->

                </div>
            </div>
        </div>
    </div>


</div>

<div id="confirm" class="hide">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3 id="id_header"></h3>
    </div>
    <div class="modal-body">
        <p id="id_body"></p>
    </div>
    <div class="modal-footer">
        <a href="#" class="btn" data-dismiss="modal" aria-hidden="true">取消</a>
        <a href="#" class="btn btn-danger" onClick="action_ok()">确定</a>
    </div>
</div>
<div class="feedTool">
    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img border="0" src="${rc.contextPath}/img/qq.png"  width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img border="0" src="${rc.contextPath}/img/x_alt.png"  width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>
    <p style="text-align: center; padding-right:32px;color: firebrick">点我报错</p>
</div>
<script type="text/javascript">

    $('li[id="index"]').addClass("active");
    $('#menu-toggler').on(ace.click_event, function() {
        $('#sidebar').toggleClass('display');
        $(this).toggleClass('display');
        return false;
    });

    var isAdmin = ${isAdmin?c};
    var username = "${currentUser!}";
</script>

</body>
</html>