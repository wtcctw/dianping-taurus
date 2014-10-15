<%@page import="org.restlet.resource.ClientResource" %>
<%@page import="com.dp.bigdata.taurus.restlet.resource.IUsersResource" %>
<%@page import="com.dp.bigdata.taurus.restlet.shared.UserDTO" %>
<%@page import="java.util.ArrayList" %>
<%@page import="org.restlet.data.MediaType" %>
<%@ page import="com.dianping.lion.client.ConfigCache" %>
<%@ page import="com.dianping.lion.EnvZooKeeperConfig" %>
<%@ page import="com.dianping.lion.client.LionException" %>
<%
    String currentUser = (String) session.getAttribute(com.dp.bigdata.taurus.web.servlet.LoginServlet.USER_NAME);
    if (currentUser != null) {
%>
<%} else {%>
<%}%>
<!-- Global variable -->
<%
    String host;
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
    for (UserDTO user : users) {
        if (user.getName().equals(currentUser)) {
            if ("admin".equals(user.getGroup()) || "monitor".equals(user.getGroup()) || "OP".equals(user.getGroup())) {
                isAdmin = true;
            } else {
                isAdmin = false;
            }

        }
    }
%>

