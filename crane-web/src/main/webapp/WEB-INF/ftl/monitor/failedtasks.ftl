<#if attempts??>
<#list attempts as dto>
<#if dto.status == "FAILED">
	<tr id="${dto.attemptID!}">
	<#list tasks as task>
	<#if task.taskid == dto.taskID>
		<td>${dto.taskID!}</td>
		<td>${task.name!}</td>
		<td>${(dto.startTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
		<td>${(dto.endTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
		<td>${(dto.scheduleTime?string("yyyy-MM-dd HH:mm"))!"NULL"}</td>
		<td>${dto.execHost!"NULL"}</td>
		<td><a taget="_blank" href="${rc.contextPath}/viewlog?id=${dto.attemptID!}&status=${dto.status}">日志</a></td>
		<td><a id='failedFeedBtn' class='feedBtn' href="${rc.contextPath}/feederror?id=${dto.attemptID!}&status=${dto.status!}&taskName=${task.name!}&ip=${dto.execHost!}&taskId=${dto.taskID!}&feedtype=wechat&from=monitor"><img border='0' src='${rc.contextPath}/img/wechat.png'  width='20' height='20' color='blue' alt='点我报错' title='点我报错'/></a> |<a id ='failedFeedQQBtn' class='feedBtn'  href='${rc.contextPath}/feederror?id=${dto.attemptID!}&status=${dto.status!}&taskName=${task.name!}&ip=${dto.execHost!}&taskId=${dto.taskID!}&feedtype=qq&from=monitor'><img border='0' src='${rc.contextPath}/img/qq.png'  width='20' height='20' color='blue' alt='点我报错' title='点我报错'/></a></td>
	<#break>
	</#if>
	</#list>
	</tr>
</#if>
</#list>
</#if>