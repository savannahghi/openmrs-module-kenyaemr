<%
	def appId = "kenyaemr.medicalEncounter"
%>
<div class="ke-panel-content">
	<div class="ke-stack-item">

		<% if(encounters) { %>
		<table>
			<tr>

				<th>Date</th>
				<% if(gender =="F") { %>
				<th>Pregnant</th>
				<% } %>
				<th>Liver disease</th>
				<th>Renal disease</th>
				<th>Diabetes</th>
			</tr>

		<% encounters.each { %>
			<tr>
				<td width="80px">${it.encDate}</td>
			    <% if(gender =="F") { %>
				<td>${it.pregnant}</td>
			    <% } %>
				<td>${it.liverDisease}</td>
				<td>${it.renalDisease}</td>
				<td>${it.diabetes}</td>
			</tr>
		<% } %>
		</table>
		<% } else { %>

		No information
		<% } %>
	</div>
</div>
