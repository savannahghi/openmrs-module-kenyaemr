/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.kenyaemr.fragment.controller.covid;

import org.openmrs.*;
import org.openmrs.module.kenyacore.form.FormManager;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.module.kenyaemr.util.EmrUtils;
import org.openmrs.module.kenyaui.KenyaUiUtils;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.FragmentParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.FragmentModel;
import org.openmrs.ui.framework.page.PageRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Patient summary fragment
 */
public class ComorbidityFormFragmentController {
	
	static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yyyy");
	
	public void controller(@FragmentParam("patient") Patient patient, @SpringBean FormManager formManager,
                           @SpringBean KenyaUiUtils kenyaUi, PageRequest pageRequest, UiUtils ui, FragmentModel model) {
		
		EncounterType encComorbidityHistory = MetadataUtils.existing(EncounterType.class, CommonMetadata._EncounterType.COVID_19_COMORBIDITY);
		Form formComorbidityHistory = MetadataUtils.existing(Form.class, CommonMetadata._Form.COVID_19_COMORBIDITY_FORM);
		
		List<Encounter> encs = EmrUtils.AllEncounters(patient, encComorbidityHistory, formComorbidityHistory);
		List<SimpleObject> simplifiedEncData = new ArrayList<SimpleObject>();
		for (Encounter e : encs) {
			SimpleObject o = buildEncounterData(e.getObs(), e);
			simplifiedEncData.add(o);
		}
		model.addAttribute("patient", patient);
		model.addAttribute("encounters", simplifiedEncData);
		model.addAttribute("gender", patient.getGender());
	}
	
	public static SimpleObject buildEncounterData(Set<Obs> obsList, Encounter e) {


		int pregnancyConcept = 5272;
		int liverDiseaseConcept = 6032;
		int renalConcept = 6033;
		int diabetesConcept = 119481;
		int yesConcept = 1065;
		int noConcept = 1066;

		String renalDisease = "";
		String liverDisease = "";
		String diabetes = "";
		String pregnant = "";

		String encDate = e != null ? DATE_FORMAT.format(e.getEncounterDatetime()) : "";
		
		for (Obs obs : obsList) {
			
			if (obs.getConcept().getConceptId().equals(pregnancyConcept) && obs.getValueCoded().getConceptId().equals(yesConcept)) {
				pregnant = "Yes";
			} else if (obs.getConcept().getConceptId().equals(pregnancyConcept) && obs.getValueCoded().getConceptId().equals(noConcept)) {
				pregnant = "No";
			} else if (obs.getConcept().getConceptId().equals(liverDiseaseConcept) && obs.getValueCoded().getConceptId().equals(yesConcept)) {
				liverDisease = "Yes";
			} else if (obs.getConcept().getConceptId().equals(liverDiseaseConcept) && obs.getValueCoded().getConceptId().equals(noConcept)) {
				liverDisease = "No";
			}  else if (obs.getConcept().getConceptId().equals(renalConcept) && obs.getValueCoded().getConceptId().equals(yesConcept)) {
				renalDisease = "Yes";
			} else if (obs.getConcept().getConceptId().equals(renalConcept) && obs.getValueCoded().getConceptId().equals(noConcept)) {
				renalDisease = "No";
			} else if (obs.getConcept().getConceptId().equals(diabetesConcept) && obs.getValueCoded().getConceptId().equals(yesConcept)) {
				diabetes = "Yes";
			} else if (obs.getConcept().getConceptId().equals(diabetesConcept) && obs.getValueCoded().getConceptId().equals(noConcept)) {
				diabetes = "No";
			}
		}
		
		return SimpleObject.create("encDate", encDate, "encId", e.getEncounterId(), "renalDisease", renalDisease != null ? renalDisease : "", "pregnant",
				pregnant != null ? pregnant : "", "liverDisease", liverDisease != null ? liverDisease
		            : "", "diabetes", diabetes != null ? diabetes : ""
		
		);
	}
	
}
