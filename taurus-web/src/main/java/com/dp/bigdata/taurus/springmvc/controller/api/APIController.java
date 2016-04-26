package com.dp.bigdata.taurus.springmvc.controller.api;

import com.dp.bigdata.taurus.generated.mapper.TaskAttemptMapper;
import com.dp.bigdata.taurus.generated.mapper.TaskMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Author   mingdongli
 * 16/4/25  下午5:32.
 */
@API
@Controller
public class APIController extends AbstractAuthenticationController{

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private TaskAttemptMapper taskAttemptMapper;

    @Autowired
    private TaskMapper taskMapper;

    @RequestMapping(value = "/jobList", method = {RequestMethod.GET})
    @ResponseBody
    public Result getJobList(String jobIds) {
        Result result = null;

        refreshBAinfo();
        //校验参数是否为空
        log.info("APIController  jobList begin  jobIds:" + jobIds + " ,clientId:" + clientId);
        if (StringUtils.isBlank(jobIds)) {
            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_NULL.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_NULL.getField());
            return result;
        }
        JobIdsParam jobIdsParam = new JobIdsParam();
        jobIdsParam.setIds(jobIds.split(","));
        jobIdsParam.setJobGroup(jobGroup);
        jobIdsParam.setJobLine(jobLine);

//        try {
//            List<JobDetailOutModel> list = scheduleAPIService.queryJobDetailByIds(jobIdsParam);
//            result = Result.getInstance(true, list, ErrorCodeEnum.OPERATION_SUCCESS.getCode(), ErrorCodeEnum.OPERATION_SUCCESS.getField());
//
//        } catch (MSException e) {
//            log.error("getJobList jobDetailAPIList ", e);
//            result = Result.getInstance(false, null, e.getCode(), e.getMessage());
//
//        } catch (Exception e) {
//            log.error("getJobList queryParam error ", e);
//            result = Result.getInstance(false, null, ErrorCodeEnum.OPERATION_FAILED_PARAM_TYPE_ERROR.getCode(), ErrorCodeEnum.OPERATION_FAILED_PARAM_TYPE_ERROR.getField());
//            return result;
//        }
        log.info("ScheduleAPIController  jobList end  jobIds:" + jobIds + " ,clientId:" + clientId);
        return result;

    }

    private static class Result<T>{

        private boolean ret = true;

        private Integer errCode;

        private String errMsg;

        private T data;

        public static <T> Result getSuccessInstance(T data) {
            return getInstance(true, data, null, null);
        }

        public static Result getFailedInstance(Integer errCode, String errMsg) {
            return getInstance(false, null, errCode, errMsg);
        }

        public static <T> Result getInstance(boolean ret, T data, Integer errCode, String errMsg) {
            Result<T> result = new Result<T>();
            result.setRet(ret);
            result.setData(data);
            result.setErrCode(errCode);
            result.setErrMsg(errMsg);
            return result;
        }

        public boolean isRet() {
            return ret;
        }

        public void setRet(boolean ret) {
            this.ret = ret;
        }

        public Integer getErrCode() {
            return errCode;
        }

        public void setErrCode(Integer errCode) {
            this.errCode = errCode;
        }

        public String getErrMsg() {
            return errMsg;
        }

        public void setErrMsg(String errMsg) {
            this.errMsg = errMsg;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }
    }

    private class JobIdsParam {

        private String[]  ids;//

        private String jobGroup;//任务分组

        private String jobLine;//业务线

        public String[] getIds() {
            return ids;
        }

        public void setIds(String[] ids) {
            this.ids = ids;
        }

        public String getJobGroup() {
            return jobGroup;
        }

        public void setJobGroup(String jobGroup) {
            this.jobGroup = jobGroup;
        }

        public String getJobLine() {
            return jobLine;
        }

        public void setJobLine(String jobLine) {
            this.jobLine = jobLine;
        }
    }

}
