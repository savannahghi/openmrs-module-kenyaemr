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

import org.apache.commons.lang3.StringUtils;
import org.openmrs.Encounter;
import org.openmrs.EncounterType;
import org.openmrs.Form;
import org.openmrs.Obs;
import org.openmrs.Patient;
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
		
		Encounter enc = EmrUtils.lastEncounter(patient, encComorbidityHistory, formComorbidityHistory);
		SimpleObject simplifiedEncData = null;
		if (enc != null) {
			simplifiedEncData = buildEncounterData(enc.getObs(), enc);
		}

		model.addAttribute("patient", patient);
		model.addAttribute("conditions", simplifiedEncData);
	}
	
	public static SimpleObject buildEncounterData(Set<Obs> obsList, Encounter e) {


		int pregnancyConcept = 5272;
		int trimesterConcept = 160665;
		int firstTrimesterConcept = 1721;
		int secondTrimesterConcept = 1722;
		int thirdTrimesterConcept = 1723;
		int cardiovascularDiseaseConcept = 119270;
		int chronicNeurologicalDiseaseConcept = 165646;
		int immunodeficiencyConcept = 117277;
		int postpartumConcept = 129317;
		int chronicLungDiseaseConcept = 155569;
		int liverDiseaseConcept = 6032;
		int malignancyConcept = 116031;
		int renalConcept = 6033;
		int diabetesConcept = 119481;
		int yesConcept = 1065;

		boolean pregnant = false;
		String trimester = null;

		List<String> comorbidites = new ArrayList<String>();
		
		for (Obs obs : obsList) {
			
			if (obs.getConcept().getConceptId().equals(pregnancyConcept) && obs.getValueCoded().getConceptId().equals(yesConcept)) {
				pregnant = true;
			} else if (obs.getConcept().getConceptId().equals(trimesterConcept)) {
				if (obs.getValueCoded().getConceptId().equals(firstTrimesterConcept)) {
					trimester = "First Trimester";
				} else if (obs.getValueCoded().getConceptId().equals(secondTrimesterConcept)) {
					trimester = "Second Trimester";
				} else if (obs.getValueCoded().getConceptId().equals(thirdTrimesterConcept)) {
					trimester = "Third Trimester";
				}
			} else if (obs.getConcept().getConceptId().equals(liverDiseaseConcept) && obs.getValueCoded().getConceptId().equals(yesConcept)) {
				comorbidites.add("Liver Disease");
			} else if (obs.getConcept().getConceptId().equals(chronicNeurologicalDiseaseConcept) && obs.getValueCoded().getConceptId().equals(yesConcept)) {
				comorbidites.add("Chronic neurological or neuromascular disease");
			}  else if (obs.getConcept().getConceptId().equals(renalConcept) && obs.getValueCoded().getConceptId().equals(yesConcept)) {
				comorbidites.add("Renal Disease");
			} else if (obs.getConcept().getConceptId().equals(chronicLungDiseaseConcept) && obs.getValueCoded().getConceptId().equals(yesConcept)) {
				comorbidites.add("Chronic lung disease");
			} else if (obs.getConcept().getConceptId().equals(malignancyConcept) && obs.getValueCoded().getConceptId().equals(yesConcept)) {
				comorbidites.add("Malignancy");
			} else if (obs.getConcept().getConceptId().equals(diabetesConcept) && obs.getValueCoded().getConceptId().equals(yesConcept)) {
				comorbidites.add("Diabetes");
			}  else if (obs.getConcept().getConceptId().equals(immunodeficiencyConcept) && obs.getValueCoded().getConceptId().equals(yesConcept)) {
				comorbidites.add("Immunodeficiency");
			}  else if (obs.getConcept().getConceptId().equals(postpartumConcept) && obs.getValueCoded().getConceptId().equals(yesConcept)) {
				comorbidites.add("Post-partum less than 6 weeks");
			}  else if (obs.getConcept().getConceptId().equals(cardiovascularDiseaseConcept) && obs.getValueCoded().getConceptId().equals(yesConcept)) {
				comorbidites.add("Cardiovascular disease including hypertension");
			}
		}

		String pregnancy = "";

		if (pregnant && trimester != null) {
			pregnancy = "Pregnant".concat("(").concat(trimester).concat(")");
			comorbidites.add(pregnancy);
		} else if (pregnant) {
			pregnancy = "Pregnant";
			comorbidites.add(pregnancy);
		}
		
		return SimpleObject.create("conditions", comorbidites
		
		);
	}
	
}
