<%
	ui.decorateWith("kenyaemr", "standardPage")
%>
${ ui.includeFragment("kenyaemr", "report/esriDashboardView", [ request: reportRequest.id, returnUrl: returnUrl ]) }