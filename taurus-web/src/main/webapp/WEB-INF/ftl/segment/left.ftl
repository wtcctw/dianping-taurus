<div class="sidebar " id="sidebar">
    <script type="text/javascript">
        try{ace.settings.check('sidebar', 'fixed')}catch(e){}
    </script>
    <ul class="nav nav-list">
        <li id="index">
            <a href="#" class="dropdown-toggle">
                <i class="icon-dashboard"></i>
                <span class="menu-text" id="userrolechange"><#if isAdmin == true>监控中心<#else>我的任务</#if></span>
                <b class="icon-angle-down"></b>
            </a>
            <ul class="submenu">
                <li  id="monitor_center">
                    <a href="${rc.contextPath}/mvc/index">
                        <i class="menu-icon icon-caret-right"></i>
                        我的任务
                    </a>
                </li>
                <li id="task_center">
                    <a href="${rc.contextPath}/mvc/task_center">
                        <i class="menu-icon icon-caret-right"></i>
                        所有任务
                    </a>
                </li>
                <li id="host_center">
                    <a href="${rc.contextPath}/mvc/host_center">
                        <i class="menu-icon icon-caret-right"></i>
                        主机负载
                    </a>
                </li>
            </ul>

        </li>

        <li id="task">
            <a href="${rc.contextPath}/mvc/task" target="_self">
                <i class="icon-edit"></i>
                <span class="menu-text">新建任务 </span>
            </a>
        </li>
        <li id="schedule">
            <a href="${rc.contextPath}/mvc/schedule" target="_self">
                <i class="icon-tasks"></i>
                <span class="menu-text"> 调度中心 </span>
            </a>
        </li>
        <li id="monitor">
            <a href="${rc.contextPath}/mvc/monitor" target="_self">
                <i class="icon-trello"></i>
                <span class="menu-text"> 任务监控 </span>
            </a>
        </li>
        <li id="host">
            <a href="${rc.contextPath}/mvc/hosts" target="_self">
                <i class="icon-desktop"></i>
                <span class="menu-text"> 主机监控 </span>
            </a>
        </li>
        <li id="cron">
            <a href="${rc.contextPath}/mvc/cronbuilder" target="_self">
                <i class="icon-indent-right"></i>
                <span class="menu-text"> Cron 生成器</span>
            </a>
        </li>
        <li id="user">
            <a href="${rc.contextPath}/mvc/user" target="_self">
                <i class="icon-user"></i>
                <span class="menu-text"> 用户设置 </span>
            </a>
        </li>
        <li id="resign">
            <a href="${rc.contextPath}/mvc/resign" target="_self">
                <i class="icon-retweet"></i>
                <span class="menu-text"> 任务交接 </span>
            </a>
        </li>
        <li id="feedback">
            <a href="${rc.contextPath}/mvc/feedback" target="_self">
                <i class="icon-comments"></i>
                <span class="menu-text"> 我要反馈 </span>
            </a>
        </li>
        <li id="update">
            <a href="${rc.contextPath}/mvc/update" target="_self">
                <i class="icon-tag"></i>
                <span class="menu-text"> 更新日志 </span>
            </a>
        </li>
        <li id="about">
            <a href="${rc.contextPath}/mvc/about" target="_self">
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
