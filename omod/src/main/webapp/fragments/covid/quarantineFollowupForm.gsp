<%
	def appId = "kenyaemr.medicalEncounter"
	ui.includeCss("kenyaemr", "kenyaemr.css")
	def totalEncounters = encounters.size()


%>

<script type="text/javascript">
	jq(function() {
		jq("#showAllEncounters").hide();

		jq(document).on("click", "#toggle-btn", function () {
			if(document.getElementById("toggle-btn").checked == true) {
				jq("#showAllEncounters").show();
				jq("#showLastFiveEncounters").hide();
				jq("#ShowLabel").hide();
			}else {
				jq("#showAllEncounters").hide();
				jq("#showLastFiveEncounters").show();
				jq("#ShowLabel").show();
			}
		});


	})

</script>


<div class="ke-panel-content">
	<div class="ke-stack-item">
		<div id="showAllEncounters">
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

		<div id="showLastFiveEncounters">
			<% if(lastFiveEncounters) { %>
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

				<% lastFiveEncounters.each { %>
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
	<div>
		<% if(totalEncounters >5) { %>
		<label class="switch-two">
			<input type="checkbox" id="toggle-btn">
			<span class="slider round"></span>
		</label>
		<span class="toggle-label" id="ShowLabel">
			Show More
		</span>
		<% }%>
	</div>
</div>
