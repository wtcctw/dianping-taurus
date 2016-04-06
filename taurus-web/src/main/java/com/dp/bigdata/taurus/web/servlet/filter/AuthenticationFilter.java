package com.dp.bigdata.taurus.web.servlet.filter;

import com.dp.bigdata.taurus.restlet.shared.UserDTO;
import com.dp.bigdata.taurus.restlet.utils.LionConfigUtil;
import com.dp.bigdata.taurus.springmvc.controller.InitController;
import org.apache.commons.lang.StringUtils;
import org.restlet.resource.ClientResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * AuthenticationFilter
 *
 * @author damon.zhu
 */
public class AuthenticationFilter implements Filter {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private String userNameUnderSsoDisable;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userNameUnderSsoDisable = filterConfig.getInitParameter("userName");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String userInfoStr = req.getRemoteUser();

        String dpaccount = null;
        try {
            if (StringUtils.isBlank(userInfoStr)) {
                dpaccount = userNameUnderSsoDisable;
            } else {
                dpaccount = userInfoStr.split("\\|")[0];
            }
        } catch (Exception e) {
            log.error("get remote user error!");
        }

        HttpSession session = req.getSession(true);
        String sessionAccount = (String) session.getAttribute(InitController.USER_NAME);

        if (StringUtils.isNotBlank(sessionAccount)) {
            chain.doFilter(request, response);
            return;
        }

        if (StringUtils.isBlank(sessionAccount)) {

            if (StringUtils.isBlank(dpaccount)) {
                res.sendRedirect(req.getContextPath() + "/error");
                return;
            }

            UserDTO userDTO = setUserInfo(dpaccount);
            session.setAttribute(InitController.USER_NAME, dpaccount);

            ClientResource cr = null;
            try {
                cr = new ClientResource(LionConfigUtil.RESTLET_API_BASE + "user");
                cr.post(userDTO);//createIfNotExist
            } finally {
                cr.release();
            }

            chain.doFilter(request, response);
            return;
        }

    }

    @Override
    public void destroy() {
    }

    private UserDTO setUserInfo(String userName) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(userName);
        userDTO.setMail(userName + "@dianping.com");
        return userDTO;
    }

}
