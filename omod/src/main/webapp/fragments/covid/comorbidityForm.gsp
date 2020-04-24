<%
	def appId = "kenyaemr.medicalEncounter"
%>
<div class="ke-panel-content">
	<div class="ke-stack-item">

		<% if(conditions) { %>

		<% conditions.conditions.eachWithIndex {it, i -> %>
		${ ui.includeFragment("kenyaui", "widget/dataPoint", [ label: (i + 1), value: it ]) }
		<% } %>


		<% } else { %>

		No information
		<% } %>
	</div>
</div>
