<!DOCTYPE html >
<html >
<head>
	
	<title>Taurus</title>
	<#include "segment/html_header.ftl">
	<#include "segment/html_header2.ftl">
	<link href="${rc.contextPath}/css/index.css" rel="stylesheet" type="text/css">
</head>
<body>
<#include "segment/header.ftl">
<#include "segment/left.ftl">

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
                <a href="${rc.contextPath}/index">HOME</a>
            </li>
            <li class="active">
                <a href="${rc.contextPath}/update">更新日志</a>
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
                                <td>部署PPE环境，gitpub同步支持job上线PPE环境 环境地址<a href="http://ppe.taurus.dp/">ppe.taurus.dp/</a></td>
                                <td>2015-01-30</td>
                            </tr>
                            <tr>
                                <td>新增数据库状态监控告警，数据库连接异常，执行sql异常会及时告警到我和监控运维</td>
                                <td>2015-01-26</td>
                            </tr>
                            <tr>
                                <td>新增部署job后智能更新jar版本号</td>
                                <td>2015-01-25</td>
                            </tr>
                            <tr>
                                <td>新增任务拥堵告警</td>
                                <td>2015-01-20</td>
                            </tr>
                            <tr>
                                <td>上线job 命令行提示到文件名</td>
                                <td>2015-01-09</td>
                            </tr>
                            <tr>
                                <td>在调度历史中，新增 重跑任务 功能</td>
                                <td>2014-12-19</td>
                            </tr>
                            <tr>
                                <td>在调度中心中，新增 任务最后执行结果</td>
                                <td>2014-12-19</td>
                            </tr>
                            <tr>
                                <td>在我的任务/监控中心中的我的任务执行情况和我组的任务执行情况中添加 查看详情</td>
                                <td>2014-12-19</td>
                            </tr>
                            <tr>
                                <td>接入微信告警</td>
                                <td>2014-12-18</td>
                            </tr>
                            <tr>
                                <td>在我的任务/监控中心中新增显示所有任务成功率</td>
                                <td>2014-12-17</td>
                            </tr>
                            <tr>
                                <td>在任务监控和历史调度中新增 【我要报错】 功能 </td>
                                <td>2014-12-10</td>
                            </tr>
                            <tr>
                                <td>新增 <a href="${rc.contextPath}/feedback">我要反馈</a> 功能 </td>
                                <td>2014-12-9</td>
                            </tr>
                            <tr>
                                <td>新增 收到执行任务，对存在正在执行的任务提示的功能</td>
                                <td>2014-12-4</td>
                            </tr>
                            <tr>
                                <td>细化我的任务与我的组任务显示项</td>
                                <td>2014-12-1</td>
                            </tr>
                            <tr>
                                <td>新增 <a href="${rc.contextPath}/resign"> 任务交接 </a>功能</td>
                                <td>2014-11-21</td>
                            </tr>
                            <tr>
                                <td>优化界面访问速度</td>
                                <td>2014-11-06</td>
                            </tr>
                            <tr>
                                <td>新增查看历史数据</td>
                                <td>2014-11-06</td>
                            </tr>
                            <tr>
                                <td>新增定期和手工清理zookeeper节点功能</td>
                                <td>2014-11-05</td>
                            </tr>
                            <tr>
                                <td>新增 <a href="${rc.contextPath}/cronbuilder">cron 生成器</a></td>
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
                                <td>job host监控细化，<a href="${rc.contextPath}/hosts">主机监控</a></td>
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
                                <td>新增任务监控<a href="${rc.contextPath}/monitor">任务监控</a>页面</td>
                                <td>2014-08-03</td>
                            </tr>
							<tr>
								<td>重新组织了<a href="${rc.contextPath}/about">帮助</a>页面</td>
								<td>2013-09-27</td>
							</tr>
							<tr>
								<td>增加了<a href="${rc.contextPath}/user">用户设置</a>页面</td>
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
								<td>支持自动杀死Timeout作业。具体细节请看<a href="${rc.contextPath}/about#config">帮助</a></td>
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
<div class="feedTool">

    <a target="_blank" style="color: white;" href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img border="0" src="${rc.contextPath}/img/qq.png"  width="80" height="80" color="white" alt="点我报错" title="点我报错"/></a>
    <a target="_blank" style="float:right; padding-right:16px;color: white;" href="javascript:close_tool()"><img border="0" src="${rc.contextPath}/img/x_alt.png"  width="20" height="20" color="white" alt="关闭挂件" title="关闭挂件"/></a>
    <p style="text-align: center; padding-right:32px;color: firebrick">点我报错</p>
</div>
<script>
    $(document).ready(function() {
        $('li[id="update"]').addClass("active");
        $('#menu-toggler').on(ace.click_event, function() {
            $('#sidebar2').toggleClass('display');
            $(this).toggleClass('display');
            return false;
        });

    });
</script>

</body>
</html>