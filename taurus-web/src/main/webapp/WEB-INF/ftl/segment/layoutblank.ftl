<#macro page title bodystyle>
<!DOCTYPE html >
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" /> 
	<meta name="apple-mobile-web-app-capable" content="yes" /> 
	<meta name="viewport" content="width=device-width, initial-scale=1" />
	<title>${title?html}</title>
</head>
<body class="${bodystyle?html}">
	<#nested/>
</body>
</html>
</#macro>