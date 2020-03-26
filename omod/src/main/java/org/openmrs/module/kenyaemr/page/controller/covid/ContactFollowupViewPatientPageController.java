/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.kenyaemr.page.controller.covid;

import org.openmrs.*;
import org.openmrs.api.ProgramWorkflowService;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyaemr.EmrConstants;
import org.openmrs.module.kenyaemr.EmrWebConstants;
import org.openmrs.module.kenyaemr.metadata.COVIDMetadata;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.module.kenyaemr.wrapper.PatientWrapper;
import org.openmrs.module.kenyaui.annotation.AppPage;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.page.PageModel;

import java.util.Collections;
import java.util.List;

/**
 * View patient page for tracing app
 */
@AppPage(EmrConstants.APP_DEFAULTER_TRACING)
public class ContactFollowupViewPatientPageController {
	
	public void controller(PageModel model,
						   UiUtils ui) {

		Patient patient = (Patient) model.getAttribute(EmrWebConstants.MODEL_ATTR_CURRENT_PATIENT);
		PatientWrapper patientWrapper = new PatientWrapper(patient);
		Form contactFollowupForm = MetadataUtils.existing(Form.class, CommonMetadata._Form.COVID_19_CONTACT_TRACING_FORM);

		List<Encounter> contactFollowupEncounters = patientWrapper.allEncounters(contactFollowupForm);
		Collections.reverse(contactFollowupEncounters);

		boolean notEnrolledInCovid = false;


		// check if a patient has covid enrollments
		ProgramWorkflowService programWorkflowService = Context.getProgramWorkflowService();
		List<PatientProgram> covidProgramEnrollments = programWorkflowService.getPatientPrograms(patient, programWorkflowService.getProgramByUuid(COVIDMetadata._Program.COVID), null, null, null, null, false );

		if (covidProgramEnrollments.size() == 0) {
			notEnrolledInCovid = true;
		}

		model.put("followupEncounters", contactFollowupEncounters);
		model.put("contactFollowupformUuid", CommonMetadata._Form.COVID_19_CONTACT_TRACING_FORM);


	}
}