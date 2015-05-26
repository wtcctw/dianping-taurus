	<script type="text/javascript">
		$(document).ready(function() {
			$('li[id="#host_${RequestParameters.hostName!}"]').addClass("active");
		});
	</script>
	
    <div class="well sidebar-nav"  >
	    <ul class="nav nav-list">
	        <li class='nav-header'>  <h5>当前机器:</h5></li>
	        <li><font color=rgb(0,0,255)><strong>${RequestParameters.hostName!}</strong></font></li>
	    </ul>
    </div>
	<div class="well sidebar-nav">

	<ul class="nav nav-list">
	<li class='nav-header'><h4>所有机器</h4></li>
	<#list hosts as dto>
	<#assign dtos = >

	</#list>
	  <%
		for (HostDTO dto : hosts) {
	        cr = new ClientResource(host + "host/" + dto.getName());
	        IHostResource hostResource = cr.wrap(IHostResource.class);
	        cr.accept(MediaType.APPLICATION_XML);
	        HostDTO dtos = hostResource.retrieve();
	%>

			<%if(!dto.isOnline()){ %>
	  <li class="text-left" id="host_<%=dto.getName()%>"><a class="atip tooltip-info" data-original-title="状态：已下线|版本<% if (dtos.getInfo() != null) {%>  <%=dtos.getInfo().getAgentVersion()%> <%}else{%>异常<%}%>" href="hosts.jsp?hostName=<%=dto.getName()%> ">
			<font color=grey><strong><%=dto.getName()%></strong></font>
			<%}else if(dto.isConnected()){ %>
	      <li class="text-left" id="host_<%=dto.getName()%>"><a class="atip tooltip-info"   data-original-title="状态：已连接|版本：<% if (dtos.getInfo() != null) {%> <%=dtos.getInfo().getAgentVersion()%> <%}else{%>异常<%}%>" href="hosts.jsp?hostName=<%=dto.getName()%> ">

	      <font color=green><strong><%=dto.getName()%></strong></font>
			<%} else{ %>
	          <li class="text-left" id="host_<%=dto.getName()%>"><a class="atip tooltip-info"   data-original-title="状态：已失去联系，请联系运维重启agent|版本：<% if (dtos.getInfo() != null) {%> <%=dtos.getInfo().getAgentVersion()%> <%}else{%>异常<%}%>" href="hosts.jsp?hostName=<%=dto.getName()%> ">

	          <font color=red><strong><%=dto.getName()%></strong></font>
			<%} %>
			</a></li>
	<%
		}
	%>

	</ul>
	</div>
