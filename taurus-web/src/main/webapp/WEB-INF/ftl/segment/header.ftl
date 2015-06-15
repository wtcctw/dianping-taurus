<div class="navbar navbar-default" id="navbar">
    <script type="text/javascript">
        try{ace.settings.check('navbar', 'fixed')} catch(e){}
    </script>

    <div class="navbar-container" id="navbar-container" style="height: 30px">
        <div class="navbar-header pull-left">
            <a href="${rc.contextPath}/index" class="navbar-brand">
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
                        <img class="nav-user-photo" src="${rc.contextPath}/lib/ace/avatars/user.jpg" alt="Jason's Photo"/>
            <span class="user-info">
                                    <small>欢迎,</small>
                                    <div id="username">${currentUser!}
                                    </div>
                                </span>

                        <i class="icon-caret-down"></i>
                    </a>
                    <ul class="user-menu pull-right dropdown-menu dropdown-yellow dropdown-caret dropdown-close">
                        <li>
                            <a href="${rc.contextPath}/user">
                                <i class="icon-cogs"></i>
                                设置
                            </a>
                        </li>
                        <li>
                            <a href="javascript:logout('${currentUser!}')">
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
            <a target="_blank" style="margin:10px;color: white;"
               href="http://shang.qq.com/wpa/qunwpa?idkey=6a730c052b1b42ce027179ba1f1568d0e5e598c456ccb6798be582b9a9c931f7"><img
                    border="0" src="${rc.contextPath}/img/group.png" width="20" height="20" alt="Taurus后援团" title="Taurus后援团">点我加入Taurus后援团
                155326270</a>
        </div>

        <div class="pull-right ng-binding" style="margin:10px;color: white;" ng-bind="monitorMessage"><i
                class="icon-user-md">开发者：李明 <a target="_blank" style="margin:10px;color: white;"
                                               href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes"><img
                border="0" src="${rc.contextPath}/img/qq.png" width="20" height="20" color="white" alt="点我报错" title="点我报错"/>点我报错</a></i>
            <i class="icon-phone">: 13661871541</i>
        </div>

    </div>
    <!-- /.container -->
</div>