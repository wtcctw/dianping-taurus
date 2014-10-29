<!DOCTYPE html>
<html>
<head>
    <title>后台管理系统</title>
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
    <script type="text/javascript" src="js/jquery-ui.min.js"></script>
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
    <script type="text/javascript" src="js/dbadmin.js"></script>
    <!-- page specific plugin styles -->

    <!-- fonts -->
    <script src="lib/ace/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="lib/ace/css/ace-fonts.css"/>

    <!-- ace styles -->

    <link rel="stylesheet" href="lib/ace/css/ace.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-skins.min.css"/>
    <link rel="stylesheet" href="css/jquery-ui.min.css"/>
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
            <div id="user" class="col-sm-12">
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
<script type="text/javascript">

    $.ajax({

        type: "get",
        url: "jsp/common-header.jsp",
        error: function () {
        },
        success: function (response, textStatus) {
            $("#common-header").html(response);
            $('li[id="index"]').addClass("active");
        }


    });

    var isAdmin = <%=isAdmin%>;
    var username = "<%=currentUser%>";
</script>
</body>
</html>