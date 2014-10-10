<!DOCTYPE html>
<html>
<head>
    <title>Taurus</title>
    <meta charset="utf-8">
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <%@ include file="jsp/common-nav.jsp" %>
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


<div class="main-content" style="opacity: 1;">
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
                    <div id="piechart-placeholder"></div>

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
    <div class="col-sm-6">
        <div class="widget-box">
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
    <div class="col-sm-6">
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
<script type="text/javascript">

</script>
</body>
</html>