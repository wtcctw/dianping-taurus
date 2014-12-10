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

    <link href="css/docs.css" rel="stylesheet">
    <style>
        .scrollup {
            opacity: 0.3;
            position: fixed;
            bottom: 150px;
            right: 100px;
            display: none;
        }
    </style>
</head>

<body data-spy="scroll" data-target=".bs-docs-sidebar">
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

        <!-- /.navbar-header -->
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
            <a href="index.jsp">
                <i class="icon-dashboard"></i>
                <span class="menu-text" id="userrolechange">监控中心</span>
            </a>
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


    </ul>
    <!-- /.nav-list -->

    <div class="sidebar-collapse" id="sidebar-collapse">
        <i class="icon-double-angle-left" data-icon1="icon-double-angle-left"
           data-icon2="icon-double-angle-right"></i>
    </div>
    <script type="text/javascript">
        try {
            ace.settings.check('sidebar', 'collapsed')
        } catch (e) {
        }
    </script>

</div>

<script>
    var isAdmin = <%=isAdmin%>;
    if(!isAdmin){
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
            <a href="about.jsp">使用帮助</a>
        </li>
    </ul>
</div>

<div class="page-content ">
<div class="row">
<div class="col-sm-12">
    <ul class="nav nav-tabs ">
        <li><a href="#q1"><i class="icon-chevron-down"></i>
            taurus能干什么？</a></li>
        <li><a href="#q2"><i class="icon-chevron-down"></i>
            创建一个普通任务？</a></li>
        <li><a href="#q3"><i class="icon-chevron-down"></i>
            创建一个hadoop任务？</a></li>
        <li><a href="#q4"><i class="icon-chevron-down"></i>
            创建一个一次性脚本？</a></li>
        <li><a href="#config"><i
                class="icon-chevron-down"></i> 配置</a></li>
        <li><a href="#status"><i class="icon-chevron-down"></i>
            状态</a></li>
        <li><a href="#crontab"><i class="icon-chevron-down"></i>
            定时器</a></li>
        <li><a href="#agent"><i class="icon-chevron-down"></i>
            安装agent</a></li>
    </ul>
</div>
<br>

<div class="col-sm-12 padding-14">
<section id="new">
    <div class="page-header">
        <h1>最新功能:我要报错&&我要反馈</h1>
        <hr>
<h2>我要报错</h2>
        <img src="img/qq.png" style="float:left;width: 300px;height: 300px"/>
        <img src="img/feedQQ.png" style="float:right;width: 720px;height: 300px"/>

        <img src="img/feederror.png" style="width: 1020px;height: 250px"/>
        <hr>
        <h2>我要反馈</h2>
        <div>
        <img src="img/feedback.png" style="width: 720px;height: 560px"/>
</div>
        <hr>

        <p style="font-size: 14px; color:#ff7f50;" ><h1>快来体验！！！</h1></p>
        <hr>
        <h1>入门导引</h1>
    </div>
    <div id="login">
        <h3>一、Login</h3>
        <hr style="padding:15px 0px">
        <h4>Taurus 各环境入口</h4>
        <ul>
            <li>
                <a href="http://alpha.taurus.dp/">Alpha 环境:alpha.taurus.dp/</a>
            </li>
            <li>
                <a href="http://beta.taurus.dp/">Beta 环境:beta.taurus.dp/</a>
            </li>
            <li>
                <a href="http://taurus.dp/">线上环境:taurus.dp/</a>
            </li>

        </ul>
        <h4>Taurus 登陆</h4>
        Taurus 登陆采用域账户登录，如下图所示：
        <img src="img/login.png" style="width: 800px;height: 500px"/>
    </div>
    <div id="dashboad">
        <h3>二、导航栏</h3>
        <hr style="padding:15px 0px">
        新版Taurus Web 导航栏改到左侧栏，各功能说明如下表：
        <table class="table table-striped table-bordered table-condensed">
            <tr>
                <th align="left" width="12%">导航项</th>
                <th align="left" width="88%">说明</th>
            </tr>
            <tr>
                <td align="left">监控中心</td>
                <td align="left">查看当前taurus job机器状态，负载情况，任务执行情况；
                </td>
            </tr>
            <tr>
                <td align="left">新建任务</td>
                <td align="left">创建新的任务，在创建任务时执行身份必须为nobody
                </td>
            </tr>
            <tr>
                <td align="left">调度中心</td>
                <td align="left">对自己的任务进行【删除】、【暂停】、【执行】、【修改】操作，查看调度历史及7天内日志
                </td>
            </tr>
            <tr>
                <td align="left">任务监控</td>
                <td align="left">主要是监控当前taurus 任务状态：【RUNNING】、【FAILED】、【SUBMIT FAILED】、【DEPENDENCY_TIMEOUT】、【TIMEOUT】
                </td>
            </tr>
            <tr>
                <td align="left">主机监控</td>
                <td align="left">对job机器的状态，日志，任务进行详细的监控
                </td>
            </tr>
            <tr>
                <td align="left">用户设置</td>
                <td align="left">设置用户组,E-mail,手机号
                </td>
            </tr>
            <tr>
                <td align="left">更新日志</td>
                <td align="left">Taurus 升级新特性日志说明
                </td>
            </tr>
            <tr>
                <td align="left">使用帮助</td>
                <td align="left">你所在的页面，帮助你更好的使用taurus
                </td>
            </tr>
        </table>
    </div>
    <br>

    <div id="qa">
        <h3>Q&A</h3>
        <hr style="padding:15px 0px">
        <div id="q1">
            <h4><font color="#a52a2a">Q1.我能用taurus干什么？</font></h4>

            <h4>答：</h4><br>

            <div class="well">
                <font color="#5f9ea0">taurus可以帮你创建定时任务，按照crontab表达式来调度你的任务。<br>
                    你可以使用taurus做以下事情：
                    <br>
                    &nbsp;&nbsp;&nbsp;
                    <ul>
                        <li>
                            创建crontab表达式任务，任务类型default，请用ci上传job
                        </li>
                        <li>
                            创建hadoop任务，在创建时任务类型选择hadoop，运行job机器要确认为hadoop
                            job机器，另外把任务需要的keytab文件上传到job机器的/data/home/hadoop用户名/.keytab
                        </li>
                        <li>
                            执行一次性脚本，一般是用了初始化任务环境，比如创建任务需要的目录，文件，等等。在ci上传脚本，在taurus上设置命令选择执行任务，执行完成后，回到调度中心，删除该任务。
                        </li>
                        <li>
                            还有更多等待你去挖掘。。。
                        </li>
                    </ul>
                </font>
            </div>
        </div>
        <br><br><br><br>

        <div id="q2">
            <h4><font color="#a52a2a">Q2.我要创建一个普通任务，怎么操作？</font></h4>
            <h4>答：</h4><br>

            <div class="well">
                <font color="#5f9ea0">taurus是和ci一起使用的，你需要在[code.dianpingoa.com]上你的项目点ci 如下图<br>
                    <img src="img/citask.png" style="width: 950px;height: 580px"/>
                    <br>
                    <br>
                    <br>
                    job发布到job机器后配置，跳转到taurus.dp上来,如下图：
                    <br>
                    <br>
                    <img src="img/task1.png" style="width: 950px;height: 400px"/>
                    <br>
                    <br>
                    点击【下一步】后进行相关设置，注意按照自己任务需求，填写crontab表达式和命令，如下图所示：
                    <br>
                    <br>
                    <img src="img/task2.png" style="width: 980px;height: 450px"/>
                    <br><br>
                    这时你可以选择直接提交，或者点击下一步进行可选项设置，如下图：
                    <br><br>
                    <img src="img/task3.png" style="width: 980px;height: 550px"/>
                    <br><br>
                    设置完毕后，点击提交。此时任务就创建完毕，taurus会按照你设置的crontab对你的任务进行调度执行。
                </font>
            </div>
        </div>
        <br><br><br><br>

        <div id="q3">
            <h4><font color="#a52a2a">Q3.我要创建一个Hadoop任务，怎么操作？</font></h4>
            <h4>答：</h4><br>

            <div class="well">
                <font color="#5f9ea0">taurus是和ci一起使用的，你需要在[code.dianpingoa.com]上你的项目点ci 如下图<br>
                    <img src="img/citask.png" style="width: 950px;height: 580px"/>
                    <br>
                    <br>
                    <br>
                    job发布到job机器后配置，跳转到taurus.dp上来,注意作业类型选择[hadoop],并且保证keytab文件已经上传到job机器的/data/home/hadoop用户名/.keytab上。如下图：
                    <br>
                    <br>
                    <img src="img/task4.png" style="width: 950px;height: 420px"/>
                    <br>
                    <br>
                    点击【下一步】后进行相关设置，注意按照自己任务需求，填写crontab表达式和命令;执行身份如无特别要求请填nobody,如果有特别需求请联系运维，如下图所示：
                    <br>
                    <br>
                    <img src="img/task5.png" style="width: 980px;height: 450px"/>
                    <br><br>
                    这时你可以选择直接提交，或者点击下一步进行可选项设置，如下图：
                    <br><br>
                    <img src="img/task3.png" style="width: 980px;height: 550px"/>
                    <br><br>
                    设置完毕后，点击提交。此时任务就创建完毕，taurus会按照你设置的crontab对你的任务进行调度执行。
                </font>
            </div>
        </div>
        <div id="q4">
            <h4><font color="#a52a2a">Q4.我要创建一个一次性脚本，怎么操作？</font></h4>
            <h4>答：</h4><br>

            <div class="well">
                <font color="#5f9ea0">脚本程序确保你的项目有以下文件zipfile.json 如下图<br>
                    <img src="img/zipfile.jpg" style="width: 800px;height: 340px"/>
                    <br>
                    <font color="#5f9ea0">php程序确保你的项目有以下文件config.json 如下图<br>
                        <img src="img/config.png" style="width: 800px;height: 340px"/>
                    <br>
                    剩余操作和Q1相同，如有疑问请联系运维。
                </font>
            </div>
        </div>
    </div>
</section>
<br><br><br><br><br><br><br><br>
<section id="config">
    <div class="page-header">
        <h1>配置说明</h1>
    </div>
    <div id="content">

        <table class="table table-striped table-bordered table-condensed">
            <tr>
                <th align="left" width="12%">配置项</th>
                <th align="left" width="88%">说明</th>
            </tr>
            <tr>
                <td align="left">作业类型</td>
                <td align="left">hadoop:
                    需要访问hadoop的作业。这种类型的作业，taurus会管理作业的hadoop ticket的申请和销毁。<br/>
                    spring: 在spring容器中运行的作业，较特殊，一般用不到。<br/> default:
                    上述两种类型以外所有类型。
                </td>
            </tr>
            <tr>
                <td align="left">hadoop用户名</td>
                <td align="left">hadoop类型的作业，需要提供一个用于访问hadoop的principle
                    name。<br/>
                    为此，taurus需要读取这个principle的keytab文件，一般情况下这个keytab已经放到相应的目录。<br/>
                    如果你不确定这一点，请联系我们。
                </td>
            </tr>
            <tr>
                <td align="left">最长执行时间</td>
                <td align="left">作业正常情况下预计最长的执行时间，超过这个时间，作业的状态会变为TIMEOUT，但是仍然会继续执行。<br/>
                    有相应告警设置的作业人会收到报警。
                </td>
            </tr>
            <tr>
                <td align="left">依赖</td>
                <td align="left">用于配置依赖的作业，形式为依赖表达式：[作业名][作业的倒数第几次执行][作业返回值]。<br/>
                    比如：[A][1][0]表示当前作业在被触发的时间点上，依赖作业名为A的作业的前一次执行，并且这一次返回值为0。<br/>
                    可以用&或者|来连接依赖表达式，例如：[A][1][0] & [B][1][0] & [C][1][0]。
                </td>
            </tr>
            <tr>
                <td align="left">最长等待时间</td>
                <td align="left">与依赖联合起来使用，表示对依赖的作业最长的等待时间。<br/>
                    超过这个时间，作业的状态变为DEPENDENCY_TIMEOUT，相关人员会收到报警。<br/>
                    但是作业会继续等待，一旦依赖的作业完成，该作业会立即执行。<br/> 对于没有依赖的作业，该选项无效。
                </td>
            </tr>
            <tr>
                <td align="left">重试次数</td>
                <td align="left">作业在执行失败的情况下，重新执行的次数。<br/> 默认为0，表示不重试。
                </td>
            </tr>
            <tr>
                <td align="left">自动kill timeout实例</td>
                <td align="left">系统中同一个作业在同一时间只能有一个实例执行。因此当有实例执行超时时，后续的实例是无法执行的。<br/>
                    选择是，如果有实例执行超时了，并且已经有新的实例在等待执行，那么系统将自动将这个Timeout的实例杀死。
                </td>
            </tr>
            <tr>
                <td align="left">组名</td>
                <td align="left">你所属的组的名字。<br/>
                    如果提示中没有合适的选项，你可以填写一个合适的组名，这个组名请尽可能地细化。<br/>
                    这个选项的重要性在于，你可以在作业的通知选项中，选择通知一个组的人。<br/> 并且你可以看到并操作同组人员的作业。
                </td>
            </tr>
        </table>
    </div>
    <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/>
    <br/>
</section>
<section id="status">
    <div class="page-header">
        <h1>状态说明</h1>
    </div>
    <table class="table table-striped table-bordered table-condensed">
        <tr>
            <th align="left">状态名</th>
            <th align="left">解释</th>
        </tr>
        <tr>
            <td align="left">RUNNING</td>
            <td align="left">这个实例正在运行</td>
        </tr>
        <tr>
            <td align="left">DEPENDENCY_PASS</td>
            <td align="left">这个实例等待被调度运行</td>
        </tr>
        <tr>
            <td align="left">DEPENDENCY_TIMEOUT</td>
            <td align="left">实例等待被调度超时，但它依然可被调度。只要当它依赖的作业完成，或者它的前一次实例完成，即可被调度执行</td>
        </tr>
        <tr>
            <td align="left">SUCCEEDED</td>
            <td align="left">这个实例已经成功执行</td>
        </tr>
        <tr>
            <td align="left">FAILED</td>
            <td align="left">这个实例因为返回值不为0而被系统认为执行失败</td>
        </tr>
        <tr>
            <td align="left">KILLED</td>
            <td align="left">这个实例被杀死</td>
        </tr>
        <tr>
            <td align="left">TIMEOUT</td>
            <td align="left">当运行的实例执行时间超过配置的最大执行时间时，将被认为超时，但这个实例依然继续运行</td>
        </tr>
    </table>
    <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/>
    <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/>
    <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/> <br/>
    <br/>
</section>

<section id="crontab">
<div class="page-header">
    <h1>Crontab表达式</h1>
</div>
<h4>
    Taurus的crontab表达式使用的是Quartz的格式。但是需要注意的是，我们不支持秒级的调度，所以只保留了Quartz格式的后五位。
    所以填写crontab表达式的时候<span style="color: red">只填写后五位，去掉秒位</span>。<br/>
</h4>
<h4>以下是quartz表达式的规范文档:</h4>

<p>
    Quartz cron 表达式的格式十分类似于 UNIX cron 格式，但还是有少许明显的区别。区别之一就是 Quartz
    的格式向下支持到秒级别的计划，而 UNIX cron 计划仅支持至分钟级。<br> 在 UNIX cron
    里，要执行的作业（或者说命令）是存放在 cron 表达式中的，在第六个域位置上。Quartz 用 cron
    表达式存放执行计划。引用了 cron 表达式的<span style="color: #800080">CronTrigger</span>
    在计划的时间里会与 job 关联上。<br> 另一个与 UNIX cron
    表达式的不同点是在表达式中支持域的数目。UNIX 给出五个域(<span style="color: #800080">分、时、日、月和周</span>)，Quartz
    提供七个域。表 5.1 列出了 Quartz cron 表达式支持的七个域。<br>
</p>
<table border="0" width="650">
    <caption>
        <strong>表 5.1. Quartz Cron 表达式支持到七个域</strong>
    </caption>
    <caption></caption>
    <tbody>
    <tr>
        <td><strong>名称</strong></td>
        <td><strong>是否必须</strong></td>
        <td><strong>允许值</strong></td>
        <td><strong><span style="color: #800080">特殊字符</span></strong></td>
    </tr>
    <tr>
        <td>秒</td>
        <td>是</td>
        <td>0-59</td>
        <td><span style="color: #800080">, - * /</span></td>
    </tr>
    <tr>
        <td>分</td>
        <td>是</td>
        <td>0-59</td>
        <td><span style="color: #800080">, - * /</span></td>
    </tr>
    <tr>
        <td>时</td>
        <td>是</td>
        <td>0-23</td>
        <td><span style="color: #800080">, - * /</span></td>
    </tr>
    <tr>
        <td>日</td>
        <td>是</td>
        <td>1-31</td>
        <td><span style="color: #800080">, - * ? / L W C</span></td>
    </tr>
    <tr>
        <td>月</td>
        <td>是</td>
        <td>1-12 或 JAN-DEC</td>
        <td><span style="color: #800080">, - * /</span></td>
    </tr>
    <tr>
        <td>周</td>
        <td>是</td>
        <td>1-7 或 SUN-SAT</td>
        <td><span style="color: #800080">, - * ? / L C #</span></td>
    </tr>
    <tr>
        <td>年</td>
        <td>否</td>
        <td>空 或 1970-2099</td>
        <td><span style="color: #800080">, - * /</span></td>
    </tr>
    </tbody>
</table>
<p>
    <br> 月份和星期的名称是不区分大小写的。<span style="color: #800080">FRI</span>
    和 <span style="color: #800080"> fri</span> 是一样的。<br> <br>
    域之间有空格分隔，这和 UNIX cron 一样。无可争辩的，我们能写的最简单的表达式看起来就是这个了：<br> <br>
						<span style="color: #800080">* * * ? * *<br>
						</span><br> 这个表达会每秒钟(每分种的、每小时的、每天的)激发一个部署的 job。<br> <br> <strong>·理解特殊字符</strong><br>
    <br> 同 UNIX cron 一样，Quartz cron
    表达式支持用特殊字符来创建更为复杂的执行计划。然而，Quartz 在特殊字符的支持上比标准 UNIX cron 表达式更丰富了。<br>
    <br> <strong><span style="color: #800080">* </span>星号</strong><br>
    <br> 使用星号(*) 指示着你想在这个域上包含所有合法的值。例如，在月份域上使用星号意味着每个月都会触发这个
    trigger。<br> <br> 表达式样例：<br> <br> <span
        style="color: #800080">0 * 17 * * ?<br>
						</span><br> 意义：每天从下午5点到下午5:59中的每分钟激发一次 trigger。它停在下午 5:59 是因为值 17
    在小时域上，在下午 6 点时，小时变为 18 了，也就不再理会这个 trigger，直到下一天的下午5点。<br> <br>
    在你希望 trigger 在该域的所有有效值上被激发时使用 <span style="color: #800080">*</span>
    字符。<br> <br> <strong><span
        style="color: #800080">? </span>问号</strong><br> <br> <span
        style="color: #800080">?</span> 号只能用在<span style="color: #800080">日</span>和<span
        style="color: #800080">周域</span>上，但是不能在这两个域上同时使用。你可以认为<span
        style="color: #800080">?</span> 字符是 "我并不关心在该域上是什么值。"
    这不同于星号，星号是指示着该域上的每一个值。? 是说不为该域指定值。<br> <br>
    不能同时这两个域上指定值的理由是难以解释甚至是难以理解的。基本上，假定同时指定值的话，意义就会变得含混不清了：考虑一下，如果一个表达式在<span
        style="color: #800080">日</span>域上有值11，同时在<span
        style="color: #800080">周</span>域上指定了<span style="color: #800080">WED</span>。那么是要
    trigger
    仅在每个月的11号，且正好又是星期三那天被激发？还是在每个星期三的11号被激发呢？要去除这种不明确性的办法就是不能同时在这两个域上指定值。<br>
    <br> 只要记住，假如你为这两域的其中一个指定了值，那就必须在另一个字值上放一个 <span
        style="color: #800080">?</span>。<br> <br> 表达式样例：<br>
    <br> <span style="color: #800080">0 10,44 14 ? 3 WEB</span><br>
    <br> 意义：在三月中的每个星期三的下午 2:10 和 下午 2:44 被触发。<br> <br>
    <strong><span style="color: #800080">,</span> 逗号</strong><br>
    <br> 逗号 (<span style="color: #800080">,</span>)
    是用来在给某个域上指定一个值列表的。例如，使用值 0,15,30,45 在秒域上意味着每15秒触发一个 trigger。<br>
    <br> 表达式样例：<br> <br> <span style="color: #800080">0
							0,15,30,45 * * * ?</span><br> <br> 意义：每刻钟触发一次 trigger。<br>
    <br> <strong><span style="color: #800080">/</span>
    斜杠</strong><br> <br> 斜杠 (<span style="color: #800080">/</span>)
    是用于时间表的递增的。我们刚刚用了逗号来表示每15分钟的递增，但是我们也能写成这样<span
        style="color: #800080">0/15</span>。<br> <br> 表达式样例：<br>
    <br> <span style="color: #800080">0/15 0/30 * * * ?<br>
						</span><br> 意义：在整点和半点时每15秒触发 trigger。<br> <br> <strong>-
    中划线</strong><br> <br> 中划线 (<span style="color: #800080">-</span>)
    用于指定一个范围。例如，在小时域上的 3-8 意味着 "3,4,5,6,7 和 8 点。" 域的值不允许回卷，所以像 50-10
    这样的值是不允许的。<br> <br> 表达式样例：<br> <br> <span
        style="color: #800080">0 45 3-8 ? * *<br>
						</span><br> 意义：在上午的3点至上午的8点的45分时触发 trigger。<br> <br> <strong><span
        style="color: #800080">L</span> 字母<br> </strong><br> <span
        style="color: #800080">L</span> 说明了某域上允许的最后一个值。它仅被<span
        style="color: #800080">日</span>和<span style="color: #800080">周</span>域支持。当用在日域上，表示的是在<span
        style="color: #800080">月</span>域上指定的月份的最后一天。例如，当月域上指定了<span
        style="color: #800080">JAN</span> 时，在<span style="color: #800080">日</span>域上的<span
        style="color: #800080">L</span> 会促使 trigger 在1月31号被触发。假如<span
        style="color: #800080">月</span>域上是<span style="color: #800080">SEP</span>，那么
    L 会预示着在9月30号触发。换句话说，就是不管指定了哪个月，都是在相应月份的时最后一天触发 trigger。<br> <br>
    表达式 <span style="color: #800080">0 0 8 L * ?</span> 意义是在每个月最后一天的上午
    8:00 触发 trigger。在<span style="color: #800080">月</span>域上的 * 说明是
    "每个月"。<br> <br> 当 <span style="color: #800080">L</span>
    字母用于周域上，指示着周的最后一天，就是星期六 (或者数字7)。所以如果你需要在每个月的最后一个星期六下午的 11:59 触发
    trigger，你可以用这样的表达式<span style="color: #800080">0 59 23 ? *
							L</span>。<br> <br> 当使用于<span style="color: #800080">周</span>域上，你可以用一个数字与
    <span style="color: #800080"> L</span> 连起来表示月份的最后一个星期 X。例如，表达式 <span
        style="color: #800080">0 0 12 ? * 2L</span> 说的是在每个月的最后一个星期一触发
    trigger。<br>
</p>
<table border="1" width="70%" align="center"
       style="border-right-width: 1px; border-top-width: 1px; border-bottom-width: 1px; border-left-width: 1px">
    <tbody>
    <tr>
        <td><strong>不要让范围和列表值与 L 连用</strong><br> <br>
            虽然你能用星期数(<span style="color: #800080">1-7</span>)与 L
            连用，但是不允许你用一个范围值和列表值与 L 连用。这会产生不可预知的结果。
        </td>
    </tr>
    </tbody>
</table>
<p>
    <br> <strong><span style="color: #800080">W</span>
    字母</strong><br> <br> <span style="color: #800080">W</span>
    字符代表着平日 (<span style="color: #800080">Mon-Fri</span>)，并且仅能用于日域中。它用来指定离指定日的最近的一个平日。大部分的商业处理都是基于工作周的，所以
    W 字符可能是非常重要的。例如，日域中的<span style="color: #800080">15W</span> 意味着
    "离该月15号的最近一个平日。" 假如15号是星期六，那么 trigger
    会在14号(星期五)触发，因为星期四比星期一（这个例子中是17号）离15号更近。<span
        style="color: #0000ff">（译者Unmi注：不会在17号触发的，如果是<span
        style="color: #800080">15W</span>，可能会是在14号(15号是星期六)或者15号(15号是星期天)触发，也就是只能出现在邻近的一天，如果15号当天为平日直接就会当日执行）
						</span>。<span style="color: #800080">W</span> 只能用在指定的<span
        style="color: #800080">日</span>域为单天，不能是范围或列表值。<br> <br>
    <strong><span style="color: #800080">#</span> 井号</strong><br>
    <br> <span style="color: #800080">#</span> 字符仅能用于<span
        style="color: #800080">周</span>域中。它用于指定月份中的第几周的哪一天。例如，如果你指定周域的值为<span
        style="color: #800080">6#3</span>，它意思是某月的第三个周五 (<span
        style="color: #800080">6</span>=星期五，<span style="color: #800080">#3</span>意味着月份中的第三周)。另一个例子<span
        style="color: #800080">2#1</span> 意思是某月的第一个星期一 (<span
        style="color: #800080">2</span>=星期一，<span style="color: #800080">#1</span>意味着月份中的第一周)。注意，假如你指定<span
        style="color: #800080">#5</span>，然而月份中没有第 5 周，那么该月不会触发。
</p>

<p></p>

<p>
    此处的 Cron 表达式 cookbook
    旨在为常用的执行需求提供方案。尽管不可能列举出所有的表达式，但下面的应该为满足你的业务需求提供了足够的例子。<br> <br>
    <strong>·分钟的 Cron 表达式</strong><br> <br>
</p>
<table border="0" cellspacing="5" width="600">
    <caption>
        <strong>表 5.1. 包括了分钟频度的任务计划 Cron 表达式</strong>
    </caption>
    <tbody>
    <tr>
        <td><strong>用法</strong></td>
        <td width="150"><strong>表达式</strong></td>
    </tr>
    <tr>
        <td>每天的从 5:00 PM 至 5:59 PM 中的每分钟触发</td>
        <td><span style="color: #800080">0 * 17 * * ?</span></td>
    </tr>
    <tr>
        <td>每天的从 11:00 PM 至 11:55 PM 中的每五分钟触发</td>
        <td><span style="color: #800080">0 0/5 23 * * ?<br>
								</span></td>
    </tr>
    <tr>
        <td>每天的从 3:00 至 3:55 PM 和 6:00 PM 至 6:55 PM 之中的每五分钟触发</td>
        <td><span style="color: #800080">0 0/5 15,18 * * ?<br>
								</span></td>
    </tr>
    <tr>
        <td>每天的从 5:00 AM 至 5:05 AM 中的每分钟触发</td>
        <td><span style="color: #800080">0 0-5 5 * * ?</span></td>
    </tr>
    </tbody>
</table>
<br> <strong>·日上的 Cron 表达式</strong><br>
<table border="0" cellspacing="5" width="600">
    <caption>
        <strong><br> 表 5.2. 基于日的频度上任务计划的 Cron 表达式</strong>
    </caption>
    <tbody>
    <tr>
        <td><strong>用法</strong></td>
        <td width="150"><strong>表达式</strong></td>
    </tr>
    <tr>
        <td>每天的 3:00 AM</td>
        <td><span style="color: #800080">0 0 3 * * ?</span></td>
    </tr>
    <tr>
        <td>每天的 3:00 AM (另一种写法)</td>
        <td><span style="color: #800080">0 0 3 ? * *</span></td>
    </tr>
    <tr>
        <td>每天的 12:00 PM (中午)</td>
        <td><span style="color: #800080">0 0 12 * * ?</span></td>
    </tr>
    <tr>
        <td>在 2005 中每天的 10:15 AM</td>
        <td><span style="color: #800080">0 15 10 * * ? 2005</span></td>
    </tr>
    </tbody>
</table>
<br> <strong>·周和月的 Cron 表达式</strong><br> <br>
<table border="0" cellspacing="5" width="600">
    <caption>
        <strong>表 5.3. 基于周和/或月的频度上任务计划的 Cron 表达式</strong>
    </caption>
    <tbody>
    <tr>
        <td><strong>用法</strong></td>
        <td width="150"><strong>表达式</strong></td>
    </tr>
    <tr>
        <td>在每个周一,二, 三和周四的 10:15 AM</td>
        <td><span style="color: #800080">0 15 10 ? * MON-FRI</span></td>
    </tr>
    <tr>
        <td>每月15号的 10:15 AM</td>
        <td><span style="color: #800080">0 15 10 15 * ?</span></td>
    </tr>
    <tr>
        <td>每月最后一天的 10:15 AM</td>
        <td><span style="color: #800080">0 15 10 L * ?</span></td>
    </tr>
    <tr>
        <td>每月最后一个周五的 10:15 AM</td>
        <td><span style="color: #800080">0 15 10 ? * 6L</span></td>
    </tr>
    <tr>
        <td>在 2002, 2003, 2004, 和 2005 年中的每月最后一个周五的 10:15 AM</td>
        <td><span style="color: #800080">0 15 10 ? * 6L
										2002-2005</span></td>
    </tr>
    <tr>
        <td>每月第三个周五的 10:15 AM</td>
        <td><span style="color: #800080">0 15 10 ? * 6#3</span></td>
    </tr>
    <tr>
        <td>每月从第一天算起每五天的 12:00 PM (中午)</td>
        <td><span style="color: #800080">0 0 12 1/5 * ?</span></td>
    </tr>
    <tr>
        <td>每一个 11 月 11 号的 11:11 AM</td>
        <td><span style="color: #800080">0 11 11 11 11 ?</span></td>
    </tr>
    <tr>
        <td>三月份每个周三的 2:10 PM 和 2:44 PM</td>
        <td><span style="color: #800080">0 10,44 14 ? 3 WED</span></td>
    </tr>
    </tbody>
</table>
<br>
</section>
<section id="agent">
    <div class="page-header">
        <h1>安装agent</h1>
    </div>
    Taurus agent已经升级到0.5.0版本，这版的agent需要部署在tomcat下，所以安装前请确认job机器有tomcat。<br/>
    <hr style="padding:15px 0px">

    如果是第一次安装，以root身份执行如下代码：
    <div class="well">
        <em>wget http://10.1.1.171/install-new-agent-war.sh; <br/>
            chmod 700 ./install-new-agent-war.sh; <br/> ./install-new-agent-war.sh;
        </em>
    </div>
    以上会初始化taurus agent的运行环境。
    <hr style="padding:15px 0px">
<h4>运维操作</h4>
    在老cmdb中首先把服务器加入服务器列表，点deploy manage 然后输入taurus-agnet按回车,如下图所示
    <br><br>
    <img src="img/cmdb1.png" style="width: 876px;height: 455px"/>
    <br><br>
    添加服务器 如下图所示：
    <br><br>
    <img src="img/cmdb2.jpg" style="width: 876px;height: 455px"/>
    <br><br>
    添加完之后点下面的提交按钮，然后点旁边灰度定义按钮，进入后选择新增灰度组，然后默认组名为应用名，然后主机列表勾选刚刚加入的应用服务器。
    <br><br>
    <img src="img/cmdb3.jpg" style="width: 876px;height: 455px"/>
    <br><br>
    <hr style="padding:15px 0px">
    如果希望在该agent上，以多种身份运行作业<br/>
    修改配置文件/data/webapps/taurus-agent/current/taurus-agent.war/WEB-INF/classes/agentConf.properties
    <div class="well">
        <em> needSudoAuthority=true </em>
    </div>
    运维需要以root身份启动tomcat
    <hr style="padding:15px 0px">
    如果需要使用agent申请hadoop权限<br/>先修改配置文件/data/webapps/taurus-agent/current/taurus-agent.war/WEB-INF/classes/agentConf.properties
    <div class="well">
        <em> needHadoopAuthority=true
        </em>
    </div>
    keytab文件存放规则为
    <div class="well">
        <em> /data/home/${hadoop-user-name}/.keytab </em>
    </div>
    /data/home/是keytab文件的根路径,如果不存在请联系运维创建。<br/>
    ${hadoop-user-name}是hadoop中的用户身份。<br/>
    例如，以wwwcron的身份访问hadoop<br/>
    可以把wwwcron的keytab文件放在/data/home/wwwcron/.keytab<br/>
    在/data/webapps/taurus-agent/current/taurus-agent.war/WEB-INF/classes/agentConf.properties中配置
    <div class="well">
        <em> needHadoopAuthority=true<br/>
        </em>
    </div>
    最后启动agent时，需要保证执行agent的用户是taurus-agent目录的所有者。
</section>
</div>

</div>
</div>

<a href="#" class="scrollup" style="display: inline;">
    <img src="img/betop.png" width="66" height="67">
</a>
</div>

<script>
    $('li[id="about"]').addClass("active");
    $('#menu-toggler').on(ace.click_event, function() {
        $('#sidebar').toggleClass('display');
        $(this).toggleClass('display');
        return false;
    });

    $(window).scroll(function () {
        if ($(this).scrollTop() > 100) {
            $('.scrollup').fadeIn();
        } else {
            $('.scrollup').fadeOut();
        }
    });

    $('.scrollup').click(function () {
        $("html, body").scrollTop(0);
        return false;
    });

</script>
</body>
</html>