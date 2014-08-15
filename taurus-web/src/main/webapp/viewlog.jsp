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
<body data-spy="scroll"><title>Taurus作业调度平台</title>
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
<div class="spanm">
    <ul class="error-tag">
        <li><a>错误信息<span class="label label-important">STDERR</span></a></li>
    </ul>
    <div data-spy="scroll" data-offset="0" style="height: 510px; overflow: auto;" class="terminal terminal-like " id="errolog">



    </div>
</div>
<div class="spann">
    <ul class="run-tag">
        <li><a>日志信息<span class="label label-info">STDOUT</span></a></li>
    </ul>
    <div data-spy="scroll" data-offset="0" style="height: 510px; line-height: 20px; overflow: auto;" class="terminal terminal-like " id="strout">




</div>
</div>

</body>
</html>