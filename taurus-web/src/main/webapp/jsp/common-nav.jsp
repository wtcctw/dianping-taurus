<%@page import="org.restlet.resource.ClientResource" %>
<%@page import="com.dp.bigdata.taurus.restlet.resource.IUsersResource" %>
<%@page import="com.dp.bigdata.taurus.restlet.shared.UserDTO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="org.restlet.data.MediaType" %>
<%@ page import="com.dianping.lion.client.ConfigCache" %>
<%@ page import="com.dianping.lion.EnvZooKeeperConfig" %>
<%@ page import="com.dianping.lion.client.LionException" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.dp.bigdata.taurus.web.servlet.LoginServlet" %>
<%@ page import="static com.dp.bigdata.taurus.web.servlet.LoginServlet.*" %>
<%@ page import="org.springframework.web.util.WebUtils" %>
<%@ page import="sun.misc.BASE64Decoder" %>
<%@ page import="sun.misc.BASE64Encoder" %>
<%
    BASE64Encoder base64Encoder = new BASE64Encoder();
    String cookieName = "cookie_user_jsessionid";
    Cookie cookie = WebUtils.getCookie(request, cookieName);
    String currentUser = null;
    BASE64Decoder base64Decoder = new BASE64Decoder();
    if (cookie != null){
        byte[] userNameByte = base64Decoder.decodeBuffer(cookie.getValue());
        currentUser = new String(userNameByte);
    }
   //(String) session.getAttribute(com.dp.bigdata.taurus.web.servlet.LoginServlet.USER_NAME);
    if (currentUser != null) {
%>
<%} else {%>
<%}%>
<!-- Global variable -->
<%
    String host;
    int userId = -1;
    try {
        host = ConfigCache.getInstance(EnvZooKeeperConfig.getZKAddress()).getProperty("taurus.web.restlet.url");
    } catch (LionException e) {
        host = config.getServletContext().getInitParameter("RESTLET_SERVER");
        e.printStackTrace();
    }

    boolean isAdmin = false;
    ClientResource cr = new ClientResource(host + "user");
    IUsersResource userResource = cr.wrap(IUsersResource.class);
    cr.accept(MediaType.APPLICATION_XML);
    ArrayList<UserDTO> users = userResource.retrieve();
    HashMap<String, UserDTO>  userMap = new HashMap<String, UserDTO>();
    for (UserDTO user : users) {
        userMap.put(user.getName(),user);
        if (user.getName().equals(currentUser)) {

            userId = user.getId();
            if ("admin".equals(user.getGroup()) || "monitor".equals(user.getGroup()) || "OP".equals(user.getGroup())) {
                isAdmin = true;
            } else {
                isAdmin = false;
            }

        }
    }
%>

