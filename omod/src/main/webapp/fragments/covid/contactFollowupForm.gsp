<%
	def appId = "kenyaemr.medicalEncounter"
%>

<script type="text/javascript">
	jq(function() {
		jq("#showAll").hide();

		jq(document).on("click", "#toggle", function () {
			if(document.getElementById("toggle").checked == true) {
				jq("#showAll").show();
				jq("#showFive").hide();
				jq("#label").hide();
			}else {
				jq("#showAll").hide();
				jq("#showFive").show();
				jq("#label").show();
			}
		});


	})

</script>
<style>
.switch {
	position: relative;
	display: inline-block;
	width: 60px;
	height: 34px;
}

.switch input {
	opacity: 0;
	width: 0;
	height: 0;
}

.slider {
	position: absolute;
	cursor: pointer;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: #ccc;
	-webkit-transition: .4s;
	transition: .4s;
}

.slider:before {
	position: absolute;
	content: "";
	height: 26px;
	width: 26px;
	left: 4px;
	bottom: 4px;
	background-color: white;
	-webkit-transition: .4s;
	transition: .4s;
}

input:checked + .slider {
	background-color: #2196F3;
}

input:focus + .slider {
	box-shadow: 0 0 1px #2196F3;
}

input:checked + .slider:before {
	-webkit-transform: translateX(26px);
	-ms-transform: translateX(26px);
	transform: translateX(26px);
}

/* Rounded sliders */
.slider.round {
	border-radius: 34px;
}

.slider.round:before {
	border-radius: 50%;
}
.toggle-label {
	padding-top: 7px;
	font-weight: bold;
}

</style>

<div class="ke-panel-content">
	<div class="ke-stack-item">
		<div id="showAll">
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

		<div id="showFive">
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
		<label class="switch">
			<input type="checkbox" id="toggle">
			<span class="slider round"></span>
		</label>
		<span class="toggle-label" id="label">
		 Show More
		</span>
	</div>
</div>
