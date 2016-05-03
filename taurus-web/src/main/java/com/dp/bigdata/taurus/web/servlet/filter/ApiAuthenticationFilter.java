package com.dp.bigdata.taurus.web.servlet.filter;

import com.dp.bigdata.taurus.lion.LionDynamicConfig;
import com.dp.bigdata.taurus.utils.APIAuthorizationUtils;
import com.sankuai.meituan.config.MtConfigClient;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * Author   mingdongli
 * 16/4/25  下午4:18.
 */
public class ApiAuthenticationFilter implements Filter {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private MtConfigClient mtConfigClient;

    private LionDynamicConfig lionConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        boolean hasAuth;
        try {
            hasAuth = doAuthorization(request);
        } catch (NoSuchAlgorithmException e) {
            logger.error("BA验证异常-NoSuchAlgorithmException", e);
            hasAuth = false;
        } catch (InvalidKeyException e) {
            logger.error("BA验证异常-InvalidKeyException", e);
            hasAuth = false;
        } catch (RuntimeException e) {
            logger.error("BA验证异常-RuntimeException", e);
            hasAuth = false;
        }
        if (!hasAuth) {
            response.setContentType("text/json;charset=utf-8");
            response.setCharacterEncoding("UTF-8");
            response.getOutputStream().write(constructFailResponseJson().getBytes());
            return;
        }
        chain.doFilter(request, response);
    }

    private String constructFailResponseJson() {
        JSONObject result = new JSONObject();
        HashMap resultMap = new HashMap();

        try {
            resultMap.put("status", 2);
            resultMap.put("message", "BA权限验证失败");
            result.put("error", resultMap);
            return result.toString();
        } catch (JSONException var4) {
            logger.error("construct general response json error!", var4);
            return null;
        }
    }

    private Boolean doAuthorization(ServletRequest request) throws NoSuchAlgorithmException, InvalidKeyException, RuntimeException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String authorization = httpServletRequest.getHeader(APIAuthorizationUtils.HTTP_HEADER_AUTHORIZATION);
        String date = httpServletRequest.getHeader(APIAuthorizationUtils.HTTP_HEADER_DATE);
        logger.info("Header info Date:" + date + "Authorization:" + authorization);
        // 验证信息不为空
        if (StringUtils.isNotBlank(authorization) && StringUtils.isNotBlank(date)) {
            if (authorization.startsWith(APIAuthorizationUtils.MEITUAN_AUTH_METHOD)) {
                // 去除前面的MWS
                String[] clientSignature = authorization.substring(4).split(":");
                String clientId = clientSignature[0];
                String requestSignature = clientSignature[1];
                logger.info("clientId:" + clientId + "requestSignature:" + requestSignature);
                String clientSecret = "";
                if (StringUtils.isNotBlank(clientId)) {
                    clientSecret = mtConfigClient.getValue(clientId);
                }
                boolean passed = doAuthorization0(request, requestSignature, clientId, clientSecret, date);
                if (!passed) {
                    clientSecret = lionConfig.get(clientId);  //throw RuntimeException
                    passed = doAuthorization0(request, requestSignature, clientId, clientSecret, date);
                }

                return passed;
            }
        }
        return false;
    }

    private boolean doAuthorization0(ServletRequest request, String requestSignature, String clientId,
                                     String clientSecret, String date) throws NoSuchAlgorithmException, InvalidKeyException {

        if (StringUtils.isNotBlank(clientSecret)) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            String stringToSign = httpServletRequest.getMethod().toUpperCase() + " " + httpServletRequest.getRequestURI() + "\n" + date;
            if (StringUtils.equals(requestSignature, APIAuthorizationUtils.getSignature(stringToSign.getBytes(), clientSecret.getBytes()))) {
                String[] jobLineAndGroup = clientId.split("_");
                if (null != jobLineAndGroup && jobLineAndGroup.length == 2) {
                    request.setAttribute(APIAuthorizationUtils.BA_REQUST_ATTRIBUTE_CLIENTID, clientId);
                    return true;
                }
            }
        }

        return true;
    }

    @Override
    public void destroy() {

    }

    public void setMtConfigClient(MtConfigClient mtConfigClient) {
        this.mtConfigClient = mtConfigClient;
    }

    public void setLionConfig(LionDynamicConfig lionConfig) {
        this.lionConfig = lionConfig;
    }

}
