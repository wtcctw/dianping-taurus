<#if attempts??>
<#list attempts as dto>
<#if dto.status == "RUNNING">
	<tr id="${dto.attemptID!}">
		<td>${dto.taskID!}</td>
		<td><#list tasks as task><#if task.taskid == dto.taskID>${task.name!}<#break></#if></#list></td>
		<td>${(dto.startTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
		<td>${(dto.endTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
		<td>${(dto.scheduleTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
		<td>${dto.execHost!"NULL"}</td>
		<td><#if mHelper.isViewLog(dto.execHost) == false><a taget="_blank" href="${rc.contextPath}/mvc/viewlog?id=${dto.attemptID!}&status=${dto.status}">日志</a><#else>Job机负载过高，无法查看实时日志</#if></td>
	</tr>
</#if>
</#list>
</#if>