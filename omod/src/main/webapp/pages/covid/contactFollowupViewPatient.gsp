<%
    ui.decorateWith("kenyaemr", "standardPage", [patient: currentPatient])

    def onEncounterClick = { encounter ->
        """kenyaemr.openEncounterDialog('${currentApp.id}', ${encounter.id});"""
    }
%>

<div class="ke-page-content">


    <!--<div class="ke-tab" data-tabid="overview">-->
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td width="30%" valign="top">
                <div class="ke-panel-frame">
                    <div class="ke-panel-heading">Information</div>
                <div class="ke-panel-content" style="background-color: #F3F9FF">
                ${ui.includeFragment("kenyaemr", "patient/patientSummary", [patient: currentPatient])}
                </div>
                </div>
                <div class="ke-panel-frame" style="padding-top: 10px">
                    <div class="ke-panel-heading">Relationships</div>
                    <div class="ke-panel-content" style="background-color: #F3F9FF">
                ${ui.includeFragment("kenyaemr", "patient/patientRelationships", [patient: currentPatient])}
                    </div>
                </div>

            </td>
            <td width="70%" valign="top" style="padding-left: 5px">
                <% if (hasNoCovidEnrollment) { %>
                <div class="ke-panel-frame">
                    <div class="ke-panel-heading">Contact Followup History</div>

                    <div class="ke-panel-content" style="background-color: #F3F9FF">

                        <div align="center">

                            ${ui.includeFragment("kenyaui", "widget/button", [
                                    label       : "Add contact followup information",
                                    extra       : "",
                                    iconProvider: "kenyaui",
                                    icon        : "buttons/visit_retrospective.png",
                                    href        : ui.pageLink("kenyaemr", "enterForm", [appId: currentApp.id, patientId: currentPatient, formUuid: contactFollowupformUuid, returnUrl: ui.thisUrl()])
                            ])}
                        </div>
                        <br/>
                        ${ui.includeFragment("kenyaemr", "widget/encounterStack", [encounters: followupEncounters, onEncounterClick: onEncounterClick])}

                    </div>
                </div>
                <% }  %>
            </td>
        </tr>
    </table>
</div>