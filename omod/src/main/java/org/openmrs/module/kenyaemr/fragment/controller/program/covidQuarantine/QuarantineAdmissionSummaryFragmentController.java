/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.kenyaemr.fragment.controller.program.covidQuarantine;

import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.PatientProgram;
import org.openmrs.module.kenyaemr.Dictionary;
import org.openmrs.module.kenyaemr.wrapper.EncounterWrapper;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.fragment.FragmentModel;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Patient program enrollment fragment
 */
public class QuarantineAdmissionSummaryFragmentController {

	String QUARANTINE_CENTER_CONCEPT = "162724AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	String TYPE_OF_ADMISSION_CONCEPT = "161641AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
	String TRANSFER_FACILITY_CONCEPT = "161550AAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";

	public String controller(@FragmentParam("patientProgram") PatientProgram patientProgram,
							 @FragmentParam(value = "encounter", required = false) Encounter encounter,
							 @FragmentParam("showClinicalData") boolean showClinicalData, FragmentModel model) {

		Map<String, Object> dataPoints = new LinkedHashMap<String, Object>();
		dataPoints.put("Date of Admission", patientProgram.getDateEnrolled());


		if (encounter != null) {
			EncounterWrapper wrapper = new EncounterWrapper(encounter);

			Obs quarantineCenter = wrapper.firstObs(Dictionary.getConcept(QUARANTINE_CENTER_CONCEPT));
			Obs typeOfAdmission = wrapper.firstObs(Dictionary.getConcept(TYPE_OF_ADMISSION_CONCEPT));
			Obs tranferFacility = wrapper.firstObs(Dictionary.getConcept(TRANSFER_FACILITY_CONCEPT));

			if (quarantineCenter != null) {
				dataPoints.put("Name of quarantine center", quarantineCenter.getValueText());
			}

			if (typeOfAdmission != null) {
				dataPoints.put("Type of admission", typeOfAdmission.getValueCoded());
			}

			if (tranferFacility != null) {
				dataPoints.put("Facility tranferred from", tranferFacility.getValueText());
			}

		}
		model.put("dataPoints", dataPoints);
		return "view/dataPoints";
	}
}
