
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
	<#assign dtos = hHelper.getDtos(host, dto.name) >
	<li class="text-left <#if dto.name == RequestParameters.hostName>active</#if>" id="host_${dto.name!}">
		<a class="atip tooltip-info" data-original-title="状态：<#if !dto.isOnline()><#assign color = "gray">已下线<#elseif dto.isConnected()><#assign color = "green">已连接<#else><#assign color = "red">已失去联系，请联系运维重启agent</#if>|版本：<#if dtos.info?exists>${dtos.info.agentVersion!}<#else>异常</#if>" href="${rc.contextPath}/hosts?hostName=${dto.name!}">
			<font color="${color!}"><strong>${dto.name!}</strong></font>
		</a>
	</li>
</#list>
</ul>

</div>
