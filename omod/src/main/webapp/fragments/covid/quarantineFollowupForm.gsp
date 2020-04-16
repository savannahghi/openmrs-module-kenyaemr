<%
	def appId = "kenyaemr.medicalEncounter"
%>
<div class="ke-panel-content">
	<div class="ke-stack-item">

		<% if(encounters) { %>
		<table>
			<tr>
				<th>Date</th>
				<th>Fever</th>
				<th>Cough</th>
				<th>Sore throat</th>
				<th>Difficulty<br/> breathing</th>
				<th>Referred <br/> to hospital</th>
				<th> </th>
			</tr>

		<% encounters.each { %>
			<tr>
				<td width="80px">${it.encDate}</td>
				<td>${it.fever}</td>
				<td>${it.cough}</td>
				<td>${it.soreThroat}</td>
				<td>${it.difficultyBreathing}</td>
				<td>${it.referredToHospital}</td>
				<td><a href="${ui.pageLink("kenyaemr", "editForm", [patientId: currentPatient.patientId, appId: appId, encounterId: it.encId, returnUrl: ui.thisUrl()])}"> Edit </a> </td>
			</tr>
		<% } %>
		</table>
		<% } else { %>

		No information
		<% } %>
	</div>
</div>
