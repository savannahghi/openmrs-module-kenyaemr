<%
	ui.includeCss("kenyaemrorderentry", "font-awesome.css")
	ui.includeCss("kenyaemrorderentry", "font-awesome.min.css")
	ui.includeCss("kenyaemrorderentry", "font-awesome.css.map")
	ui.includeCss("kenyaemrorderentry", "fontawesome-webfont.svg")

	def contactTracingUuid ="37ef8f3c-6cd2-11ea-bc55-0242ac130003";
	def addTracingFormLink = ui.pageLink("kenyaemr", "enterForm", [patientId: currentPatient.patientId, formUuid: contactTracingUuid, appId:currentApp.id, returnUrl: ui.thisUrl()])


%>
<style>
.action-container {
	display: inline;
	float: left;
	width: 99.9%;
	margin: 0 1.04167%;
}
.action-section {
	margin-top: 2px;
	background: #7f7b72 !important; ;
	border: 1px solid #dddddd;
}
.float-left {
	float: left;
	clear: left;
	width: 97.91666%;
	background: #7f7b72 !important;
	color: white;
}

.action-section a:link {
	color: white;!important;
}

.action-section a:hover {
	color: white;
}

.action-section a:visited {
	color: white;
}
.action-section h3 {
	margin: 0;
	color: white;
	border-bottom: 1px solid white;
	margin-bottom: 5px;
	font-size: 1.5em;
	margin-top: 5px;
}
.action-section ul {
	background: #7f7b72 !important; ;
	color: white;
	padding: 5px;
}

.action-section li {
	font-size: 1.1em;
}
.action-section i {
	font-size: 1.1em;
	margin-left: 8px;
}
</style>

<div class="action-container">
	<div class="action-section">

		<ul class="float-left">
			<h3>Actions</h3>

			<li class="float-left" style="margin-top: 7px">
				<a href="${ ui.pageLink("kenyaemr", "registration/createPatient2") }" class="float-left">
					<i class="fa fa-medkit fa-2x"></i>
					Register Case
				</a>
			</li>
			<% if(hasCaseEnrollment) { %>
			<li class="float-left" style="margin-top: 7px">
				<a href="${ ui.pageLink("kenyaemrorderentry", "labOrders", [patientId: currentPatient]) }" class="float-left">
					<i class="fa fa-flask fa-2x"></i>
					Lab Orders
				</a>
			</li>
			<% } %>
		</ul>
		<ul class="float-left">
			<h3>Contacts</h3>
			<li class="float-left" style="margin-top: 7px">
				<a href="${ ui.pageLink("hivtestingservices", "patientContactList", [patientId: currentPatient.patientId]) }" class="float-left">
					<i class="fa fa-list-ul fa-2x"></i>
					Contact Listing
				</a>
			</li>
		</ul>
	</div>
</div>
<script type="text/javascript">

	//On ready
	jQuery(function () {
		//defaults


	});
</script>