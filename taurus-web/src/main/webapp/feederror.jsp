<%@ page contentType="text/html;charset=utf-8" %>
<%@ include file="jsp/common-nav.jsp" %>


<%
    String attemptId = request.getParameter("id");
    String taskName = request.getParameter("taskName");
    String taskId = request.getParameter("taskId");
    String ip = request.getParameter("ip");
    String state = request.getParameter("status");
    String feedType = request.getParameter("feedtype");

%>
<div class="modal-dialog " style="width: 900px">
    <div class="modal-content">
        <div id="modal-confirm" class="modal fade" role="dialog"
             aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <a href="#" class="close">&times;</a>

                        <h3>报错成功</h3>
                    </div>
                    <div class="modal-body">
                        <p>您的报错我们已经收到！</p>
                    </div>
                    <div class="modal-footer">
                        <button id="confirmBtn" class="btn btn-primary"
                                onClick="window.location.reload();">确定
                        </button>
                    </div>
                </div>
            </div>
        </div>
        <div class="modal-header" style="height: 50px">
            <button type="button" class="close smaller" data-dismiss="modal">x</button>
            <h4 id="myModalLabel" class="smaller">Task运行报错</h4>
        </div>
        <div class="modal-body">
            <%if(feedType.equals("mail")){%>
            <form id="form_<%=attemptId%>" class="form-horizontal task-form">
                <fieldset>


                    <div class="control-group col-sm-12">
                        <label class="label label-lg label-info arrowed-right col-sm-2" for="taskName">任务名称*</label>

                        <div class="controls">
                            <input type="text" class="input-big" id="taskName"
                                   name="taskName" value="<%=taskName%>" readonly>
                        </div>
                    </div>
                    <br>

                    <div id="host" class="control-group col-sm-12">
                        <label class="label label-lg label-info arrowed-right col-sm-2" for="host">部署的机器*</label>

                        <div class="controls">
                            <input type="text" id="hostname" name="hostname"
                                   class="input-big field" value="<%=ip%>" disabled>
                        </div>
                    </div>

                    <br>

                    <div class="control-group col-sm-12">
                        <label class="label label-lg label-info arrowed-right col-sm-2" for="state">任务状态</label>

                        <div class="controls">
                            <input type="text" class="input field " id="state"
                                   name="state" value="<%=state%>" disabled>
                        </div>
                    </div>

                    <br>

                    <div class="control-group col-sm-12">
                        <label class="label label-lg label-info arrowed-right col-sm-4"
                               for="alertUser">选择报错接收人(逗号分隔)</label>

                        <div class="controls">
                            <input type="text" class="" id="alertUser"
                                   name="alertUser" value="kirin.li"
                                    >
                        </div>
                    </div>
                    <br>

                    <input type="text" class="field" style="display: none" id="creator"
                           name="creator"
                           value="<%=currentUser%>">
                </fieldset>
            </form>
            <%}else{%>
            <h3><i class="icon-info red">请把下面内容复制,然后点击【报错】按钮，在弹出的QQ对话框中粘贴报错内容</i>
            </h3>
            <hr>
                <p>任务名称：<%=taskName%></p>
                <p>任务ID：<%=taskId%></p>
                <p>AttemptId :<%=attemptId%></p>
                <p>任务状态：<%=state%></p>
            <%
                String domain ="";
                try {
                    domain = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.deploy.weburl");
                } catch (LionException e) {
                    domain="taurus.dp";
                    e.printStackTrace();
                }
                String logUrl =  "http://"
                        + domain
                        + "/viewlog.jsp?id="
                        + attemptId
                        + "&status="
                        + state;
            %>
                <p>任务日志: <%=logUrl%></p>
            <%}%>
        </div>
        <div class="modal-footer">
            <button id="feedErrorBtn" class="btn btn-primary"
                    onClick="action_feed()"
                    data-loading-text='正在报错..'>报错
            </button>
            <button class="btn" data-dismiss="modal">关闭</button>
        </div>


    </div>
</div>
<script type="text/javascript">
    var userList = "", groupList = "";
    <%for(UserDTO user:users) {%>
    userList = userList + ",<%=user.getName()%>";
    <%}%>

    userList = userList.substr(1);
    var attemptId = "<%=attemptId%>";
    var taskName = "<%=taskName%>";
    var taskId = "<%=taskId%>"
    var user = "<%=currentUser%>";
    var status = "<%=state%>";
    var ip = "<%=ip%>";
    var feedtype = "<%=feedType%>"


    function action_feed() {
        if(feedtype == "mail"){
            var mailTo = $("#alertUser").val();

            var feedResult = "";
            $.ajax({
                url: "/feedback",
                data: {
                    action: "feederror",
                    taskId: taskId,
                    attemptId: attemptId,
                    taskName: taskName,
                    user: user,
                    status: status,
                    ip: ip,
                    mailTo: mailTo
                },
                timeout: 1000,
                type: 'POST',
                async: false,
                error: function () {
                    feedResult = "error";
                },
                success: function (response) {
                    feedResult = response
                }
            });

            if (feedResult == "success") {
                bootbox.confirm("报错成功<hr><i class='icon-info green'>你的报错我们已经收到，我们会及时处理~</i>", function (result) {
                    if (result) {
                        window.location.reload();
                    }
                });
            } else {
                bootbox.confirm("报错失败<hr><i class='icon-info red'>你的报错失败，请再尝试一次~</i>", function (result) {
                    if (result) {
                        ;
                    }
                });
            }
        }else{
            window.location.href="http://wpa.qq.com/msgrd?v=3&uin=767762405&site=qq&menu=yes";
        }


    }

</script>


