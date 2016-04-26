package com.dp.bigdata.taurus.springmvc.controller.api;

import com.dp.bigdata.taurus.utils.APIAuthorizationUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Created by mingdongli on 16/4/26.
 */
public abstract class AbstractAuthenticationController {

    protected String jobLine;

    protected String jobGroup;

    protected String clientId;

    @Autowired
    private HttpServletRequest request;

    protected void refreshBAinfo(){
        BAInfoModel baInfoModel = (BAInfoModel) request.getAttribute(APIAuthorizationUtils.BA_REQUST_ATTRIBUTE_CLIENTID);
        jobLine = baInfoModel.getJobLine();
        jobGroup = baInfoModel.getJobGroup();
        clientId = baInfoModel.getClientId();
    }

    public static class BAInfoModel implements Serializable {

        private String clientId;

        private String jobLine;

        private String jobGroup;

        public String getClientId() {
            return clientId;
        }

        public String getJobLine() {
            return jobLine;
        }

        public String getJobGroup() {
            return jobGroup;
        }

        public void setClientId(String clientId) {
            this.clientId = clientId;
        }

        public void setJobLine(String jobLine) {
            this.jobLine = jobLine;
        }

        public void setJobGroup(String jobGroup) {
            this.jobGroup = jobGroup;
        }

    }

    protected enum ErrorCodeEnum{

        //define error code
        OPERATION_SUCCESS(0, "操作成功"),
        OPERATION_FAILED(1, "操作失败"),

        OPERATION_ADD_FAILED(101, "添加失败"),
        OPERATION_ADD_FAILED_UNIQUE_CODE_REPEAT(102, "job唯一码已存在"),
        OPERATION_ADD_FAILED_CRON_NOT_VALID(103, "cron表达式配置错误"),
        OPERATION_ADD_FAILED_PORT_NOT_VALID(104, "通信端口必须在8410-8430,默认8383"),

        OPERATION_MODIFY_FAILED(201, "修改失败"),
        OPERATION_MODIFY_FAILED_UNIQUE_CODE_REPEAT(202, "job唯一码已存在"),
        OPERATION_MODIFY_FAILED_CRON_NOT_VALID(203, "cron表达式配置错误"),
        OPERATION_MODIFY_FAILED_JOB_IS_START(204, "您本次修改了job的关键信息，请先停掉再编辑"),

        OPERATION_DELETE_FAILED(301, "删除失败"),
        OPERATION_DELETE_FAILED_JOB_IS_START(302, "删除失败,处于启动状态的job不允许删除"),
        OPERATION_DELETE_FAILED_NOT_JOB_OWNER(303,"删除失败，您不是任务所有者不能删除该任务"),

        OPERATION_FAILED_PARAM_NULL(401,"必填参数为空"),
        OPERATION_FAILED_PARAM_EXPRESSIONTYPE(402,"expressiontype必须为 cron "),
        OPERATION_FAILED_PARAM_BAERROR(403,"jobLine和jobGroup的组合信息与clientId不符"),
        OPERATION_FAILED_PARAM_TYPE_ERROR(404,"参数类型错误"),
        OPERATION_FAILED_PARAM_JOB_NOFOUND(405,"未找到相应的jobInfo信息"),
        OPERATION_FAILED_PARAM_MIS_NOFOUND(406,"未找到相应的mis信息"),
        OPERATION_FAILED_PARAM_PORT_ERROR(407,"taskAcceptorPort must in 8410~8430,include 8410 and " +
                "8430,default 8383"),
        OPERATION_FAILED_PARAM_IP_ERROR(408,"指定机器IP地址有误"),
        OPERATION_FAILED_PARAM_IP_TASKNODE_NULL(409, "指定IP模式，任务节点需要配置"),
        OPERATION_FAILED_PARAM_SHARDSTRATEGY_SUBTASK_NULL(410,"任务策略为分片任务时，任务分片数必须为正整数");

        private Integer code;

        private String field;

        ErrorCodeEnum(Integer code, String field) {
            this.code = code;
            this.field = field;
        }

        public Integer getCode() {
            return code;
        }

        public String getField() {
            return field;
        }
    }

}
