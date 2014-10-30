<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <title>Taurus</title>
    <meta charset="utf-8">
    <meta name="description" content="overview &amp; stats" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
    <%@ include file="jsp/common-nav.jsp" %>

    <!-- bootstrap & fontawesome -->
    <!-- basic styles -->
    <script type="text/javascript" src="resource/js/lib/jquery-1.9.1.min.js"></script>
    <link href="lib/ace/css/bootstrap.min.css" rel="stylesheet"/>
    <script src="lib/ace/js/ace-extra.min.js"></script>
    <link rel="stylesheet" href="lib/ace/css/font-awesome.min.css"/>
    <script src="lib/ace/js/ace-elements.min.js"></script>
    <script src="lib/ace/js/ace.min.js"></script>
    <script src="lib/ace/js/bootbox.min.js"></script>
    <script type="text/javascript" src="resource/js/lib/raphael.2.1.0.min.js"></script>
    <script type="text/javascript" src="resource/js/lib/justgage.1.0.1.min.js"></script>
    <script type="text/javascript" src="resource/js/lib/Chart.js"></script>
    <script type="text/javascript" src="lib/ace/js/jquery.flot.min.js"></script>
    <script type="text/javascript" src="lib/ace/js/jquery.flot.pie.min.js"></script>
    <script type="text/javascript" src="lib/ace/js/bootstrap-datepicker.min.js"></script>
    <script type="text/javascript" src="lib/ace/js/daterangepicker.min.js"></script>
    <script src="lib/ace/js/jquery.dataTables.min.js"></script>
    <script src="lib/ace/js/jquery.dataTables.bootstrap.js"></script>
    <script type="text/javascript" src="js/login.js"></script>
    <script type="text/javascript" src="js/index.js"></script>
    <!-- page specific plugin styles -->

    <!-- fonts -->
    <script src="lib/ace/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="lib/ace/css/ace-fonts.css"/>

    <!-- ace styles -->

    <link rel="stylesheet" href="lib/ace/css/ace.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-skins.min.css"/>

    <link rel="stylesheet" href="resource/css/monitor-center.css">

</head>
<body>

<div class="common-header" id="common-header">

</div>


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
            <a href="index.jsp">HOME</a>
        </li>
    </ul>
</div>
<div class="page-content">
<div class="row">
<div id="user">
    <div class="col-sm-12">
        <div class="col-sm-6">
            <div class="widget-box">
                <div class="widget-header widget-header-flat widget-header-small">
                    <h5 class="widget-title">
                        <i class="icon-signal"></i>
                        我的任务成功率
                    </h5>

                    <div class="widget-toolbar">
                        <a href="#" data-action="collapse">
                            <i class="icon-chevron-up"></i>
                        </a>
                    </div>
                </div>

                <div class="widget-body">
                    <div class="widget-main" id="user-widget-main">

                        <div id="mytasks"><i class="icon-spinner icon-spin icon-large"></i></div>

                        <div class="hr hr8 hr-double"></div>

                        <div class="clearfix">
                            <div class="col-sm-6">
														<span class="grey">
															<i class="icon-lightbulb green"></i>
															&nbsp; 成功
														</span>
                                <h4 class="bigger pull-right" id="succtask"></h4>
                            </div>

                            <div class="col-sm-6">
														<span class="grey">
															<i class="icon-lightbulb red"></i>
															&nbsp; 失败
														</span>
                                <h4 class="bigger pull-right " id="failtask"></h4>
                            </div>


                            <!-- /section:custom/extra.grid -->
                        </div>
                    </div>
                    <!-- /.widget-main -->
                </div>
                <!-- /.widget-body -->
            </div>
            <!-- /.widget-box -->

        </div>
        <div class="col-sm-6">
            <div class="widget-box">
                <div class="widget-header widget-header-flat widget-header-small">
                    <h5 class="widget-title">
                        <i class="icon-signal"></i>
                        我的组的任务成功率
                    </h5>

                    <div class="widget-toolbar">
                        <a href="#" data-action="collapse">
                            <i class="icon-chevron-up"></i>
                        </a>
                    </div>
                </div>

                <div class="widget-body">
                    <div class="widget-main" id="group-widget-main">

                        <div id="grouptasks"><i class="icon-spinner icon-spin icon-large"></i></div>

                        <div class="hr hr8 hr-double"></div>

                        <div class="clearfix">
                            <div class="col-sm-6">
														<span class="grey">
															<i class="icon-lightbulb green"></i>
															&nbsp; 成功
														</span>
                                <h4 class="bigger pull-right" id="groupsucctask"></h4>
                            </div>

                            <div class="col-sm-6">
														<span class="grey">
															<i class="icon-lightbulb red"></i>
															&nbsp; 失败
														</span>
                                <h4 class="bigger pull-right " id="groupfailtask"></h4>
                            </div>


                            <!-- /section:custom/extra.grid -->
                        </div>
                    </div>
                    <!-- /.widget-main -->
                </div>
                <!-- /.widget-body -->
            </div>
            <!-- /.widget-box -->

        </div>
    </div>
    <div class="col-sm-12">
        <div class="col-sm-6">
            <div class="widget-box">
                <div class="widget-header widget-header-flat widget-header-small">
                    <h5 class="widget-title">
                        <i class=" icon-signal"></i>
                        我的任务执行详情
                    </h5>

                    <div class="widget-toolbar">
                        <a href="#" data-action="collapse">
                            <i class="icon-chevron-up"></i>
                        </a>
                    </div>
                </div>

                <div class="widget-body">

                    <div class="widget-main align-center" id="mytasklist">
                        <i class="icon-spinner icon-spin icon-large"></i>
                    </div>
                    <!-- /.widget-main -->
                </div>
                <!-- /.widget-body -->
            </div>
            <!-- /.widget-box -->

        </div>
        <div class="col-sm-6">
            <div class="widget-box">
                <div class="widget-header widget-header-flat widget-header-small">
                    <h5 class="widget-title">
                        <i class=" icon-signal"></i>
                        我所在组的任务执行详情
                    </h5>

                    <div class="widget-toolbar">

                        <a href="#" data-action="collapse">
                            <i class="icon-chevron-up"></i>
                        </a>
                    </div>
                </div>

                <div class="widget-body">

                    <div class="widget-main align-center" id="grouptasklist">
                        <i class="icon-spinner icon-spin icon-large"></i>
                    </div>
                    <!-- /.widget-main -->
                </div>
                <!-- /.widget-body -->
            </div>
            <!-- /.widget-box -->

        </div>
    </div>
</div>
<div id="admin">
<div class="col-sm-12">
    <div class="col-sm-6">
        <div class="widget-box">
            <div class="widget-header widget-header-flat widget-header-small">
                <h5 class="widget-title">
                    <i class="icon-signal"></i>
                    Job机器状态
                </h5>

                <div class="widget-toolbar">
                    <a href="#" data-action="collapse">
                        <i class="icon-chevron-up"></i>
                    </a>
                </div>
            </div>

            <div class="widget-body">
                <div class="widget-main">
                    <div id="piechart-placeholder"><i class="icon-spinner icon-spin icon-large"></i></div>

                    <div class="hr hr8 hr-double"></div>

                    <div class="clearfix">
                        <div class="col-sm-6">
														<span class="grey">
															<i class="icon-lightbulb green"></i>
															&nbsp; 正常
														</span>
                            <h4 class="bigger pull-right" id="onlineNums"></h4>
                        </div>

                        <div class="col-sm-6">
														<span class="grey">
															<i class="icon-lightbulb red"></i>
															&nbsp; 失联
														</span>
                            <h4 class="bigger pull-right " id="exceptionNums"></h4>
                        </div>


                        <!-- /section:custom/extra.grid -->
                    </div>
                </div>
                <!-- /.widget-main -->
            </div>
            <!-- /.widget-body -->
        </div>
        <!-- /.widget-box -->

    </div>
    <div class="col-sm-6">
        <div class="widget-box">
            <div class="widget-header widget-header-flat widget-header-small">
                <h5 class="widget-title">
                    <i class="icon-signal"></i>
                    Job异常机器列表
                </h5>

                <div class="widget-toolbar">
                    <a href="#" data-action="collapse">
                        <i class="icon-chevron-up"></i>
                    </a>
                </div>
            </div>

            <div class="widget-body">
                <div class="widget-main" id="exceptionJob">
                    <i class="icon-spinner icon-spin icon-large"></i>
                </div>
                <!-- /.widget-main -->
            </div>
            <!-- /.widget-body -->
        </div>
        <!-- /.widget-box -->

    </div>
</div>
<div class="col-sm-12">
    <div class="col-sm-6" id="cpuwidget">
        <div class="widget-box" >
            <div class="widget-header widget-header-flat widget-header-small">
                <h5 class="widget-title">
                    <i class=" icon-signal"></i>
                    主机CPU负载列表
                </h5>


                <div class="widget-toolbar">
                    <a href="#" data-action="collapse">
                        <i class="icon-chevron-up"></i>
                    </a>
                </div>
                <div class="widget-toolbar">
                    <a id='cpureeflash' title='更新数据,刷新时间间隔1分钟' onclick="reflash('reflash');" href="#cpuwidget" ><i class="icon-refresh"></i></a>
                </div>
            </div>

            <div class="widget-body">

                <div class="widget-main align-center" id="cpuload">
                    <i class="icon-spinner icon-spin icon-large"></i>
                </div>
                <!-- /.widget-main -->
            </div>
            <!-- /.widget-body -->
        </div>
        <!-- /.widget-box -->

    </div>
    <div class="col-sm-6" id="memwidget">
        <div class="widget-box">
            <div class="widget-header widget-header-flat widget-header-small">
                <h5 class="widget-title">
                    <i class=" icon-signal"></i>
                    主机内存使用率列表
                </h5>



                <div class="widget-toolbar">
                    <a href="#" data-action="collapse">
                        <i class="icon-chevron-up"></i>
                    </a>
                </div>
                <div class="widget-toolbar">
                    <a id='reflash' title='更新数据,刷新时间间隔1分钟' onclick="reflash('reflash');" href="#memwidget"> <i
                            class="icon-refresh"></i></a>
                </div>
            </div>

            <div class="widget-body">

                <div class="widget-main align-center" id="memload">
                    <i class="icon-spinner icon-spin icon-large"></i>
                </div>
                <!-- /.widget-main -->
            </div>
            <!-- /.widget-body -->
        </div>
        <!-- /.widget-box -->

    </div>
</div>

<div class="col-sm-12" style="display: none">
    <div class="widget-box">
        <div class="widget-header widget-header-flat widget-header-small">
            <h5 class="widget-title">
                <i class="icon-signal"></i>
                Job主机任务详情
            </h5>

            <div class="widget-toolbar">
                <a href="#" data-action="collapse">
                    <i class="icon-chevron-up"></i>
                </a>
            </div>
        </div>

        <div class="widget-body">
            <div class="widget-main">
                <div id="job-placeholder"><i class="icon-spinner icon-spin icon-large"></i></div>

                <div class="hr hr8 hr-double"></div>

            </div>
            <!-- /.widget-main -->
        </div>
        <!-- /.widget-body -->
    </div>
    <!-- /.widget-box -->

</div>
<div class="col-sm-12">
    <div class="col-sm-6">
        <div class="widget-box">
            <div class="widget-header widget-header-flat widget-header-small">
                <h5 class="widget-title">
                    <i class="icon-signal"></i>
                    执行任务总数排行
                </h5>

                <div class="widget-toolbar">
                    <a href="#" data-action="collapse">
                        <i class="icon-chevron-up"></i>
                    </a>
                </div>
            </div>

            <div class="widget-body">


                <div class="widget-main" id="totalJob">
                    <span class="label " style="display: none">开始时间</span>

                    <div class="input-group" style="display: none">
                        <input class="form-control date-picker ng-pristine ng-valid ng-valid-required"
                               id="totalstarttime" type="text" data-date-format="yyyy-mm-dd"
                               ng-model="startDate" required="">
                                        <span class="input-group-addon"> <i class="icon-calendar bigger-110"></i>
                                        </span>
                    </div>

                    <span class="label " style="display: none">结束时间</span>

                    <div class="input-group" style="display: none">
                        <input class="form-control date-picker ng-pristine ng-valid ng-valid-required"
                               id="totalendtime" type="text" data-date-format="yyyy-mm-dd" ng-model="startDate"
                               required="">
                                        <span class="input-group-addon"> <i class="icon-calendar bigger-110"></i>
                                        </span>
                    </div>
                </div>
                <!-- /.widget-main -->
            </div>
            <!-- /.widget-body -->
        </div>
        <!-- /.widget-box -->

    </div>
    <div class="col-sm-6">
        <div class="widget-box">
            <div class="widget-header widget-header-flat widget-header-small">
                <h5 class="widget-title">
                    <i class=" icon-signal"></i>
                    执行任务失败总数排行
                </h5>

                <div class="widget-toolbar">
                    <a href="#" data-action="collapse">
                        <i class="icon-chevron-up"></i>
                    </a>
                </div>
            </div>

            <div class="widget-body">

                <div class="widget-main " id="failedJob">
                    <div class="col-sm-6" style="display: none">
                        <span class="label ">开始时间</span>

                        <div class="input-group">
                            <input class="form-control date-picker ng-pristine ng-valid ng-valid-required"
                                   id="failstarttime" type="text" data-date-format="yyyy-mm-dd" ng-model="startDate"
                                   required="">
                                        <span class="input-group-addon"> <i class="icon-calendar bigger-110"></i>
                                        </span>
                        </div>
                    </div>

                    <div class="col-sm-6 " style="display: none">
                        <span class="label col-sm-2">结束时间</span>

                        <div class="input-group col-sm-6">

                            <input class="form-control date-picker ng-pristine ng-valid ng-valid-required"
                                   id="failendtime" type="text" data-date-format="yyyy-mm-dd" ng-model="startDate"
                                   required="">
                                        <span class="input-group-addon"> <i class="icon-calendar bigger-110"></i>
                                        </span>
                        </div>
                    </div>

                </div>
                <!-- /.widget-main -->
            </div>
            <!-- /.widget-body -->
        </div>
        <!-- /.widget-box -->

    </div>
</div>
</div>


</div>
</div>
</div>
<script type="text/javascript">

    $.ajax({

        type: "get",
        url: "jsp/common-header.jsp",
        error: function () {
        },
        success: function (response, textStatus) {
            $("#common-header").html(response);
            $('li[id="index"]').addClass("active");
            $('#menu-toggler').on(ace.click_event, function() {
                $('#sidebar').toggleClass('display');
                $(this).toggleClass('display');
                return false;
            });
        }


    });


    var isAdmin = <%=isAdmin%>;
    var username = "<%=currentUser%>";
    if (isAdmin && username !="kirin.li") {
         var user = document.getElementById("user");
             user.style.display="none";
    } else {
        //var admin = document.getElementById("admin");
        //admin.style.display="none";
    }
    function reflash(queryType) {
        var cpuLoadBody = "";
        var memLoadBody = "";
        $("#cpuload").html(" <i class='icon-spinner icon-spin icon-large'></i>");
        $("#cpuload").addClass("align-center");
        $("#memload").html("<i class='icon-spinner icon-spin icon-large'></i>");
        $("#memload").addClass("align-center");

        $.ajax({
            async: true,
            data: {
                action: "hostload",
                queryType: queryType

            },
            type: "POST",
            url: "/monitor",
            error: function () {
                $("#cpuload").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
                $("#cpuload").addClass("align-center");
                $("#memload").html("<i class='icon-info-sign icon-large red '>后台服务器打了个盹～</i>");
                $("#memload").addClass("align-center");
            },
            success: function (response, textStatus) {
                var jsonarray = $.parseJSON(response);
                $.each(jsonarray, function (i, item) {
                    cpuLoadBody += "<tr>" +
                            "<td>" + item.hostName + "</td>" +
                            "<td>" + item.cpuLoad + "</td>"
                    "</tr>";

                    memLoadBody += "<tr>" +
                            "<td>" + item.hostName + "</td>" +
                            "<td>" + item.memLoad + "</td>"
                    "</tr>";
                });
                var topCpuLoadLists = ' <table  class="table table-striped table-bordered table-hover " id="cputable">'
                        + '<thead><tr><th>主机名</th>  <th>CPU负载(load average)</th> </tr> </thead>'
                        + '        <tbody>'
                        + cpuLoadBody
                        + '        </tbody>'
                        + '    </table>';
                $("#cpuload").html(topCpuLoadLists);
                $("#cpuload").removeClass("align-center");

                var topMemLoadLists = ' <table  class="table table-striped table-bordered table-hover " id="memtable">'
                        + '<thead><tr><th>主机名</th>  <th>内存剩余(free)</th> </tr> </thead>'
                        + '        <tbody>'
                        + memLoadBody
                        + '        </tbody>'
                        + '    </table>';
                $("#memload").html(topMemLoadLists);
                $("#memload").removeClass("align-center");
                cpuTableStyle();
                memTableStyle();
            }


        });

    }
</script>
</body>
</html>