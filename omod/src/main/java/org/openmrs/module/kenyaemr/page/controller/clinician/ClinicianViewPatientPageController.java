/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.kenyaemr.page.controller.clinician;

import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Patient;
import org.openmrs.Visit;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyaemr.EmrConstants;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.module.kenyaemr.util.EmrUtils;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * View patient page for clinician app
 */
@AppPage(EmrConstants.APP_CLINICIAN)
public class ClinicianViewPatientPageController {

	public void controller(@RequestParam("patientId") Patient patient, PageModel model) {

		List<Visit> activeVisits = Context.getVisitService().getActiveVisitsByPatient(patient);
		EncounterType encComorbidityHistory = MetadataUtils.existing(EncounterType.class, CommonMetadata._EncounterType.COVID_19_COMORBIDITY);
		Form formCormobidityHistory = MetadataUtils.existing(Form.class, CommonMetadata._Form.COVID_19_COMORBIDITY_FORM);

		Encounter enc = EmrUtils.lastEncounter(patient, encComorbidityHistory, formCormobidityHistory);


		Visit lastVisit = null;
		if (activeVisits.size() > 0) {
			lastVisit = activeVisits.get(activeVisits.size() - 1);
		}
		model.addAttribute("visit", lastVisit);
		model.addAttribute("patient", patient);
		model.addAttribute("patient", patient);
		model.addAttribute("comorbidity", enc !=null? enc.getEncounterId():"");

	}
}