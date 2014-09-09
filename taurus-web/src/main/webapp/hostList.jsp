	<%@ page contentType="text/html;charset=utf-8"%>
	<%@page import="org.restlet.resource.ClientResource"%>
	<%@page import="com.dp.bigdata.taurus.restlet.resource.IHostsResource"%>
	<%@page import="com.dp.bigdata.taurus.restlet.shared.HostDTO"%>
	<%@page import="java.util.ArrayList"%>
	<%@page import="org.restlet.data.MediaType"%>
	<%@page import="java.text.SimpleDateFormat"%>
	<script type="text/javascript">
		$(document).ready(function() {
			$('li[id="#host_<%=request.getParameter("hostName")%>"]').addClass("active");
		});

	</script>
    <div class="well sidebar-nav" >
    <ul class="nav nav-list">
        <li class='nav-header'>  <h5>当前机器:</h5></li>
        <li><font color=rgb(0,0,255)><strong><%=request.getParameter("hostName")%></strong></font></li>
    </ul>
        </div>
		 <div class="well sidebar-nav" >

          <ul class="nav nav-list">
	       <li class='nav-header'><h4>所有机器</h4></li>

              <%
	    		for (HostDTO dto : hosts) {
                    cr = new ClientResource(host + "host/" + dto.getName());
                    IHostResource hostResource = cr.wrap(IHostResource.class);
                    cr.accept(MediaType.APPLICATION_XML);
                    HostDTO dtos = hostResource.retrieve();
			%>

					<%if(!dto.isOnline()){ %>
              <li class="text-right" id="host_<%=dto.getName()%>"><a class="atip"  data-toggle="tooltip" data-placement="right" data-original-title="状态：已下线|版本<% if (dtos.getInfo() != null) {%>  <%=dtos.getInfo().getAgentVersion()%> <%}else{%>异常<%}%>" href="hosts.jsp?hostName=<%=dto.getName()%> ">
					<font color=grey><strong><%=dto.getName()%></strong></font>
					<%}else if(dto.isConnected()){ %>
                  <li class="text-right" id="host_<%=dto.getName()%>"><a class="atip"  data-toggle="tooltip" data-placement="right" data-original-title="状态：已连接|版本：<% if (dtos.getInfo() != null) {%> <%=dtos.getInfo().getAgentVersion()%> <%}else{%>异常<%}%>" href="hosts.jsp?hostName=<%=dto.getName()%> ">

                  <font color=green><strong><%=dto.getName()%></strong></font>
					<%} else{ %>
                      <li class="text-right" id="host_<%=dto.getName()%>"><a class="atip"  data-toggle="tooltip" data-placement="right" data-original-title="状态：已失去联系，请联系运维重启agent|版本：<% if (dtos.getInfo() != null) {%> <%=dtos.getInfo().getAgentVersion()%> <%}else{%>异常<%}%>" href="hosts.jsp?hostName=<%=dto.getName()%> ">

                      <font color=red><strong><%=dto.getName()%></strong></font>
					<%} %>
					</a></li>
			<%
	    		}
			%>
	     
          </ul>
		 </div>
	  
	<style>
		.nav-list  li  a{
			padding:2px 15px;
		}
		.nav li  +.nav-header{
			margin-top:2px;
		}
		.nav-header{
			padding:5px 3px;
		}
		.row-fluid .span2{
			width:12%;
		}
	</style>

	  	
