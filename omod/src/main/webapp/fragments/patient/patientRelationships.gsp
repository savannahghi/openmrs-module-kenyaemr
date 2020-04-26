<%
	ui.includeCss("kenyaemr", "kenyaemr.css")
	def totalRelationships = relationships.size()
	/*<button type="button" onclick="ui.navigate('${ ui.pageLink("kenyaemr", "registration/editRelationship", [ patientId: patient.id, appId: currentApp.id, returnUrl: ui.thisUrl() ])}')">
			<img src="${ ui.resourceLink("kenyaui", "images/glyphs/add.png") }" /> Add Relationship
	</button>*/

%>
<script type="text/javascript">
	function onVoidRelationship(relId) {
		kenyaui.openConfirmDialog({
			heading: 'Void Relationship',
			message: '${ ui.message("kenyaemr.confirmVoidRelationship") }',
			okCallback: function() { doRelationshipVoid(relId); }
		});
	}

	function doRelationshipVoid(relId) {
		ui.getFragmentActionAsJson('kenyaemr', 'emrUtils', 'voidRelationship', { relationshipId: relId, reason: 'Data entry error' }, function() {
			ui.reloadPage();
		});
	}

	jq(function() {
		jq("#allRelationships").hide();

		jq(document).on("click", "#relationship-toggle", function () {
			if(document.getElementById("relationship-toggle").checked == true) {
				jq("#allRelationships").show();
				jq("#first-two-relationships").hide();
				jq("#show-more-relationships").hide();
			}else {
				jq("#allRelationships").hide();
				jq("#first-two-relationships").show();
				jq("#show-more-relationships").show();
			}
		});


	})
</script>

<div class="ke-panel-content">

<% if (relationships) { %>
<div  id="allRelationships">

	<% relationships.each { rel -> %>
	<div class="ke-stack-item">
		<button type="button" class="ke-compact" onclick="onVoidRelationship(${ rel.relationshipId })"><img src="${ ui.resourceLink("kenyaui", "images/glyphs/void.png") }" /></button>

		<button type="button" class="ke-compact" onclick="ui.navigate('${ ui.pageLink("kenyaemr", "registration/editRelationship", [ patientId: patient.id, relationshipId: rel.relationshipId, appId: currentApp.id, returnUrl: ui.thisUrl() ]) }')">
			<img src="${ ui.resourceLink("kenyaui", "images/glyphs/edit.png") }" />
		</button>

		${ ui.includeFragment("kenyaui", "widget/dataPoint", [ label: ui.format(rel.type), value: rel.personLink ]) }
		<% if (rel.startDate) { %>
		${ ui.includeFragment("kenyaui", "widget/dataPoint", [ label: "Started", value: rel.startDate ]) }
		<% } %>
		<% if (rel.endDate) { %>
		${ ui.includeFragment("kenyaui", "widget/dataPoint", [ label: "Ended", value: rel.endDate ]) }
		<% } %>

		<div style="clear: both"></div>
	</div>
	<% } %>
</div>
<% } %>


<% if (firstTwoRelationships) { %>
<div id="first-two-relationships">

	<% firstTwoRelationships.each { rel -> %>
	<div class="ke-stack-item">
		<button type="button" class="ke-compact" onclick="onVoidRelationship(${ rel.relationshipId })"><img src="${ ui.resourceLink("kenyaui", "images/glyphs/void.png") }" /></button>

		<button type="button" class="ke-compact" onclick="ui.navigate('${ ui.pageLink("kenyaemr", "registration/editRelationship", [ patientId: patient.id, relationshipId: rel.relationshipId, appId: currentApp.id, returnUrl: ui.thisUrl() ]) }')">
			<img src="${ ui.resourceLink("kenyaui", "images/glyphs/edit.png") }" />
		</button>

		${ ui.includeFragment("kenyaui", "widget/dataPoint", [ label: ui.format(rel.type), value: rel.personLink ]) }
		<% if (rel.startDate) { %>
		${ ui.includeFragment("kenyaui", "widget/dataPoint", [ label: "Started", value: rel.startDate ]) }
		<% } %>
		<% if (rel.endDate) { %>
		${ ui.includeFragment("kenyaui", "widget/dataPoint", [ label: "Ended", value: rel.endDate ]) }
		<% } %>

		<div style="clear: both"></div>
	</div>
	<% } %>
</div>
<% } %>

<div>
	<% if(totalRelationships >2) { %>
	<label class="switch-three">
		<input type="checkbox" id="relationship-toggle">
		<span class="slider round"></span>
	</label>
	<span class="toggle-label" id="show-more-relationships">
		Show More
	</span>
	<% }%>
</div>
</div>

<div class="ke-panel-footer">

</div>