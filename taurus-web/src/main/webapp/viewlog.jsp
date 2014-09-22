<%@ page import="java.util.Date" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="org.restlet.resource.ClientResource" %>
<%@ page import="com.dp.bigdata.taurus.restlet.resource.IAttemptsResource" %>
<%@ page import="com.dp.bigdata.taurus.restlet.shared.AttemptDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="org.restlet.representation.Representation" %>
<%@ page import="org.restlet.data.MediaType" %>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
</head>
<body data-spy="scroll" ><title>Taurus作业调度平台</title>
<style>
    .run-tag {
        font-size: 130%
    }

    .error-tag {
        font-size: 130%
    }

    .spanm {

        width: 600px;
    }

    .spann {

        width: 48%;
    }

    .row-fluid .spanm {
        width: 49.914893617021278%;
        *width: 49.861702127659576%;
    }

    .row-fluid .spann {
        width: 49.81196581196582%;
        *width: 49.75877432260411%;
    }

    .flash_btn {
        float: right
    }
    .reflashtip {
        float: right
    }
</style>
<script src="js/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>
<script src="js/login.js"></script>
<script src="js/viewlog.js"></script>
<link href="css/bootstrap.min.css" rel="stylesheet" type="text/css">
<link href="css/viewlog.css" rel="stylesheet" type="text/css">
<link href="css/bootstrap-responsive.min.css" rel="stylesheet" type="text/css">
<link href="css/taurus.css" rel="stylesheet" type="text/css">
<link rel="Shortcut Icon" href="img/icon.png" type="image/x-icon">
<link href="css/index.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" type="text/css" href="css/DT_bootstrap.css">
<div class="navbar navbar-inverse navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container">
            <button type="button" class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
                <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span>
            </button>
            <a class="brand" href="./index.jsp">Taurus</a>
        </div>
    </div>
</div>
<ul class="breadcrumb">
    <li><a href="./index.jsp">首页</a> <span class="divider">/</span></li>
    <li><a href="./schedule.jsp">调度中心</a> <span class="divider">/</span></li>
    <li><a href="#" class="active">查看日志</a> <span class="divider">/</span></li>

</ul>


<div id="error-panel">
    <div class="spanm">
        <ul class="error-tag" id="error-tag">
            <li><a>错误信息<span class="label label-important">STDERR</span></a></li>
        </ul>
        <div data-spy="scroll" data-offset="0" style="height: 510px; overflow: auto;" class="terminal terminal-like "
             id="errolog">


        </div>
    </div>
</div>
<div id="log-panel">
    <div class="spann" id="spann">
        <ul class="run-tag">
            <li><a>日志信息<span class="label label-info">STDOUT</span></a>

                <div class="flash_btn" id="flash_btn">
                    <a>实时刷新</a>

                    <div class="btn-group btn-toggle">
                        <button class="btn btn-xs btn-default active">ON</button>
                        <button class="btn btn-xs btn-danger ">OFF</button>
                    </div>
                    <a class="atip" data-toggle="tooltip" data-placement="top"
                       data-original-title="当你开启实时刷新后页面会自动刷新，不需要自己动手刷新页面喽～">[提示] </a>
                </div>
                <div class="reflashtip" id="reflashtip" >

                    <a class="atip" data-toggle="tooltip" data-placemsent="left"
                       data-original-title="您的任务运行在老版本的agent上，此版本不支持实时查看日志，请等待任务完成后查看日志">[注意] </a>
                </div>
            </li>


        </ul>
        <div data-spy="scroll" data-offset="0" style="height: 510px; line-height: 20px; overflow: auto;"
             class="terminal terminal-like " id="strout">


        </div>
    </div>
</div>
</body>
</html>