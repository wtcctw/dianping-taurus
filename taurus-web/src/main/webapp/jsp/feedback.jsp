<%@ page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
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
    <link rel="stylesheet" href="css/jquery-ui.min.css"/>
    <script src="lib/ace/js/ace-elements.min.js"></script>
    <script src="lib/ace/js/ace.min.js"></script>
    <script src="lib/ace/js/bootbox.min.js"></script>
    <script type="text/javascript" src="resource/js/lib/raphael.2.1.0.min.js"></script>
    <script type="text/javascript" src="resource/js/lib/justgage.1.0.1.min.js"></script>
    <script type="text/javascript" src="js/jquery-ui.min.js"></script>
    <script type="text/javascript" src="js/login.js"></script>
    <!-- page specific plugin styles -->

    <!-- fonts -->
    <script src="lib/ace/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="lib/ace/css/ace-fonts.css"/>

    <!-- ace styles -->

    <link rel="stylesheet" href="lib/ace/css/ace.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-skins.min.css"/>
    <link href="css/bwizard.min.css" rel="stylesheet"/>
    <link rel="stylesheet" href="css/common.css">
    <style>
        label.error {
            margin-left: 10px;
            color: red;
        }

        label.success {
            margin-left: 10px;
            color: green;
        }

        .tips {
            background: #ebf4f8;
            color: #888;
            border-top: 1px #f6f1dc solid;
            padding: 11px 26px 11px 5px;
        }

        .tip-title {
            padding-top: 20px;
            color: #888;
            font-weight: normal;
            font-size: 13px;
        }

        #contentnum {
            color: red;
        }
    </style>
</head>
<body>


<div class="navbar navbar-default" id="navbar">
    <script type="text/javascript">
        try {
            ace.settings.check('navbar', 'fixed')
        } catch (e) {
        }
    </script>

    <div class="navbar-container" id="navbar-container" style="height: 30px">
        <div class="navbar-header pull-left">

            <a href="index.jsp" class="navbar-brand">
                <i class="icon-tasks"></i>
                Taurus
            </a>
            <!-- /.brand -->
        </div>
        <!-- /.navbar-header -->
        <div class="navbar-header">
            <span style="margin:10px;font-size: 16px" class="label label-transparent">任务调度系统</span>
        </div>

        <!-- /.navbar-header -->
        <button type="button" class="navbar-toggle pull-left" id="menu-toggler">
            <span class="sr-only">Toggle sidebar</span>

            <span class="icon-bar"></span>

            <span class="icon-bar"></span>

            <span class="icon-bar"></span>
        </button>
        <div class="navbar-header pull-right" role="navigation">
            <ul class="nav ace-nav">
                <li class="light-blue">
                    <a data-toggle="dropdown" href="#" target="_self" class="dropdown-toggle">
                        <img class="nav-user-photo" src="lib/ace/avatars/user.jpg" alt="Jason's Photo"/>
            <span class="user-info">
                                    <small>欢迎,</small>
                                    <div id="username"><%=currentUser%>
                                    </div>
                                </span>

                        <i class="icon-caret-down"></i>
                    </a>

                    <ul class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
                        <li>
                            <a href="user.jsp">
                                <i class="icon-cogs"></i>
                                设置
                            </a>
                        </li>
                        <li>
                            <a href="javascript:logout('<%=currentUser%>')">
                                <i class="icon-off"></i>
                                退出
                            </a>
                        </li>
                    </ul>
                </li>
            </ul>
            <!-- /.ace-nav -->
        </div>
        <div class="pull-right" style="margin:10px;color: white;">
            <a target="_blank" style="margin:10px;color: white;"  href="http://shang.qq.com/wpa/qunwpa?idkey=6a730c052b1b42ce027179ba1f1568d0e5e598c456ccb6798be582b9a9c931f7"><img border="0" src="img/group.png" width="20" height="20" alt="Taurus后援团" title="Taurus后援团">点我加入Taurus后援团 155326270</a>
        </div>

        <div class="pull-right ng-binding" style="margin:10px;color: white;" ng-bind="monitorMessage"><i class="icon-user-md">开发者：李明  <a target="_blank" style="margin:10px;color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img border="0" src="img/qq.png"  width="20" height="20" color="white" alt="点我报错" title="点我报错"/>点我报错</a></i> <i class="icon-phone">: 13661871541</i></div>

    </div>
    <!-- /.container -->
</div>
<div class="sidebar " id="sidebar">
    <script type="text/javascript">
        try {
            ace.settings.check('sidebar', 'fixed')
        } catch (e) {
        }
    </script>

    <ul class="nav nav-list">

        <li id="index">
            <a href="#" class="dropdown-toggle">
                <i class="icon-dashboard"></i>
                <span class="menu-text" id="userrolechange">监控中心</span>
                <b class="icon-angle-down"></b>
            </a>
            <ul class="submenu">
                <li  id="monitor_center">
                    <a href="index.jsp">
                        <i class="menu-icon icon-caret-right"></i>
                        我的任务
                    </a>

                </li>

                <li id="task_center">
                    <a href="task_center.jsp">
                        <i class="menu-icon icon-caret-right"></i>
                        所有任务
                    </a>


                </li>
                <li id="host_center">
                    <a href="host_center.jsp">
                        <i class="menu-icon icon-caret-right"></i>
                        主机负载
                    </a>

                </li>
            </ul>

        </li>


        <li id="task">
            <a href="task.jsp" target="_self">
                <i class="icon-edit"></i>
                <span class="menu-text">新建任务 </span>
            </a>
        </li>
        <li id="schedule">
            <a href="schedule.jsp" target="_self">
                <i class="icon-tasks"></i>
                <span class="menu-text"> 调度中心 </span>
            </a>
        </li>
        <li id="monitor">
            <a href="monitor.jsp" target="_self">
                <i class="icon-trello"></i>
                <span class="menu-text"> 任务监控 </span>
            </a>
        </li>
        <li id="host">
            <a href="hosts.jsp" target="_self">
                <i class="icon-desktop"></i>
                <span class="menu-text"> 主机监控 </span>
            </a>
        </li>
        <li id="cron">
            <a href="cronbuilder.jsp" target="_self">
                <i class="icon-indent-right"></i>
                <span class="menu-text"> Cron 生成器</span>
            </a>
        </li>
        <li id="user">
            <a href="user.jsp" target="_self">
                <i class="icon-user"></i>
                <span class="menu-text"> 用户设置 </span>
            </a>
        </li>
        <li id="resign">
            <a href="resign.jsp" target="_self">
                <i class="icon-retweet"></i>
                <span class="menu-text"> 任务交接 </span>
            </a>
        </li>
        <li id="feedback">
            <a href="feedback.jsp" target="_self">
                <i class="icon-comments"></i>
                <span class="menu-text"> 我要反馈 </span>
            </a>
        </li>
        <li id="update">
            <a href="update.jsp" target="_self">
                <i class="icon-tag"></i>
                <span class="menu-text"> 更新日志 </span>
            </a>
        </li>
        <li id="about">
            <a href="about.jsp" target="_self">
                <i class="icon-question"></i>
                <span class="menu-text"> 使用帮助 </span>
            </a>
        </li>

        <li id="power">
            <a href="#" target="_self">
                <span class="menu-text" style="padding-left: 10px"> ©&nbsp;&nbsp;&nbsp;&nbsp;点评工具组 </span>
            </a>
        </li>

    </ul>
    <!-- /.nav-list -->



</div>

<script>
    var isAdmin = <%=isAdmin%>;
    if (!isAdmin) {
        $("#userrolechange").html("我的任务");
    }


</script>

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
                <a href="index.jsp">HOME</a>
            </li>
            <li class="active">
                <a href="feedback.jsp">我要反馈</a>
            </li>
        </ul>
    </div>
    <div class="page-content">
        <div class="row">
            <div class="col-sm-12">
                <div class="content-wrap">
                    <div class="col-sm-2"></div>
                    <div class="col-sm-7">

                        <div class="content-body">

                            <div class="tips col-sm-12">
                                <i class="icon-lightbulb bigger-210 blue"></i>
                                感谢你使用Taurus !请告诉我们你的意见和建议，我会及时响应你的反馈，并不断完善Taurus。<br>
                                &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                联系方式：

                                <div class="col-sm-12">
                                    <div class="col-sm-1"></div>
                                    <div class="col-sm-4"><i class="icon-user-md">&nbsp;&nbsp; 李明 </i></div>
                                </div>
                                <div class="col-sm-12">
                                    <div class="col-sm-1"></div>
                                    <div class="col-sm-4"><i class="icon-phone">&nbsp;&nbsp; 13661871541</i></div>
                                </div>
                                <div class="col-sm-12">
                                    <div class="col-sm-1"></div>
                                    <div class="col-sm-4"><a target="_blank" style="color: orangered;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img border="0" src="img/qq.png"  width="18" height="18"  alt="点我QQ在线反馈" title="点我QQ在线反馈"/>&nbsp;&nbsp;点我QQ在线反馈</a></div>
                                </div>
                                <div class="col-sm-12">
                                    <div class="col-sm-1"></div>
                                    <div class="col-sm-4"><i class="icon-envelope">&nbsp;&nbsp;&nbsp;kirin.li@dianping.com</i>
                                    </div>
                                </div>
                                <div class="col-sm-12">
                                    <div class="col-sm-1"></div>
                                    <div class="col-sm-4"><i class="icon-group"><strong>
                                        &nbsp;&nbsp;155326270</strong></i></div>
                                </div>

                            </div>

                            <div id="alertContainer" class="container"></div>
                            <h5 class="tip-title">请详细描述你遇到的问题、意见及建议:</h5>

                            <textarea id="feedback-content" class="feedback-content"
                                      style="width:100%; height:240px; resize: none; border-radius: 4px;"></textarea>
                            <br>

                            <div>
                                <div class="limit"
                                     style="float: left; margin-right: 20px; line-height: 34px; color: black;"></div>

                                <div class="btn-wrap" style="float: right">
                                    <a id="submit-btn" class="btn btn-sm btn-primary" data-loading-text="正在反馈...">反馈
                                        <i class="icon-ok bigger-110"></i>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-sm-3"></div>
                </div>
            </div>
        </div>

    </div>
</div>
<div class="feedTool">

    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img border="0" src="img/qq.png"  width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img border="0" src="img/x_alt.png"  width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>
    <p style="text-align: center; padding-right:32px;color: firebrick">点我报错</p>
</div>
<script type="text/javascript">

    $('li[id="feedback"]').addClass("active");
    $('#menu-toggler').on(ace.click_event, function () {
        $('#sidebar').toggleClass('display');
        $(this).toggleClass('display');
        return false;
    });
    var user = "<%=currentUser%>";

</script>
<script src="js/jquery.validate.min.js" type="text/javascript"></script>
<script src="js/feedback.js" type="text/javascript"></script>


</body>

</html>