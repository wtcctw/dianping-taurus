<!DOCTYPE html>
<%@ page contentType="text/html;charset=utf-8"%>
<html lang="en">
<head>
    <title>Taurus</title>
    <meta charset="utf-8">
    <meta name="description" content=""/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <%@ include file="jsp/common-nav.jsp"%>
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
    <script type="text/javascript" src="js/login.js"></script>
    <!-- page specific plugin styles -->

    <!-- fonts -->
    <script src="lib/ace/js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="lib/ace/css/ace-fonts.css"/>

    <!-- ace styles -->

    <link rel="stylesheet" href="lib/ace/css/ace.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-rtl.min.css"/>
    <link rel="stylesheet" href="lib/ace/css/ace-skins.min.css"/>

	<link href="css/index.css" rel="stylesheet" type="text/css">
</head>
<body >

<div class="common-header" id="common-header">

</div>
<div class="main-content">
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
                <a href="update.jsp">更新日志</a>
            </li>
        </ul>
    </div>

	<div class="page-content ">

			<div class="row ">
                <div class="col-sm-12 padding-14">
                <div class="page-header">
                    <h1>服务器更新日志</h1>
                </div>
					<table class="table table-striped table-bordered table-condensed ">
						<tbody>

							<tr class="text-success">
								<th width="90%">最新发布功能描述</th>
								<th width="10%">发布时间</th>
							</tr>
                            <tr>
                                <td>新增 <a href="cronbuilder.jsp">cron 生成器</a></td>
                                <td>2014-10-28</td>
                            </tr>
                            <tr>
                                <td>新增job主机负载内存监控</td>
                                <td>2014-10-20</td>
                            </tr>
                            <tr>
                                <td>新增组任务情况监控</td>
                                <td>2014-10-15</td>
                            </tr>
                            <tr>
                                <td>新版Taurus界面，新增我的任务</td>
                                <td>2014-10-09</td>
                            </tr>
                            <tr>
                                <td>job host监控细化，<a href="hosts.jsp">主机监控</a></td>
                                <td>2014-09-05</td>
                            </tr>
                            <tr>
                                <td>agent war包化，可以通过button部署</td>
                                <td>2014-09-01</td>
                            </tr>
                            <tr>
                                <td>新增实时查看正在运行的任务日志</td>
                                <td>2014-08-05</td>
                            </tr>
                            <tr>
                                <td>新增任务监控<a href="monitor.jsp">任务监控</a>页面</td>
                                <td>2014-08-03</td>
                            </tr>
							<tr>
								<td>重新组织了<a href="about.jsp">帮助</a>页面</td>
								<td>2013-09-27</td>
							</tr>
							<tr>
								<td>增加了<a href="user.jsp">用户设置</a>页面</td>
								<td>2013-09-27</td>
							</tr>
							<tr>
								<td>用户可以看到同组的所有人的作业</td>
								<td>2013-09-27</td>
							</tr>
							<tr>
								<td>修复了description显示为null和修改告警人的bug</td>
								<td>2013-09-27</td>
							</tr>
							<tr>
								<td>支持自动杀死Timeout作业。具体细节请看<a href="about.jsp#config">帮助</a></td>
								<td>2013-08-20</td>
							</tr>
							<tr>
								<td>修正Bugs。对杀死的作业设置返回值为-1。</td>
								<td>2013-08-20</td>
							</tr>
							<tr>
								<td>增加Cat打点</td>
								<td>2013-08-20</td>
							</tr>
						</tbody>
					</table>
                <div class="page-header padding-14">
                    <h1>开发者</h1>
                </div>
					<table class="table table-striped table-bordered table-condensed ">
						<tbody>
                            <tr>
                                <td>李明</td>
                                <td>kirin.li@dianping.com</td>
                                <td>13661871541</td>
                            </tr>
						</tbody>
					</table>

			</div>
	</div>
    </div>
    </div>
<script>
    $(document).ready(function() {
        $.ajax({
            type: "get",
            url: "jsp/common-header.jsp",
            error: function () {
            },
            success: function (response, textStatus) {
                $("#common-header").html(response);
                $('li[id="update"]').addClass("active");
            }


        });
    });
</script>
</body>
</html>
