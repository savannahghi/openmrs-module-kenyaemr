/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.kenyaemr.reporting.cohort.definition.evaluator.covid;


import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Cohort;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyaemr.reporting.cohort.definition.covid.PatientsEnrolledOnCovidCohortDefinition;
import org.openmrs.module.kenyaemr.test.StandaloneContextSensitiveTest;
import org.openmrs.module.reporting.cohort.definition.service.CohortDefinitionService;
import org.openmrs.module.reporting.common.DateUtil;
import org.openmrs.module.reporting.evaluation.EvaluationContext;

public class PatientsEnrolledOnCovidCohortDefinitionEvaluatorTest extends StandaloneContextSensitiveTest{

    @Before
    public void initialize() throws Exception {
        
        executeDataSet("dataset/etl_data.xml");
        
    }	
	    
	/**
	 * @see PatientsEnrolledOnCovidCohortDefinitionEvaluator#evaluate(CohortDefinition,EvaluationContext)
	 * @verifies find patients in Covid program filtered by county
	 */
	@Test
	public void evaluate_shouldFindPatientsInCovidProgramFilteredByCounty() throws Exception {
		PatientsEnrolledOnCovidCohortDefinition cd = new PatientsEnrolledOnCovidCohortDefinition();
		
		Map<String, Object> parameterValues = new HashMap<String, Object>();
		parameterValues.put("startDate", DateUtil.getDateTime(2020, 4, 8));		
		parameterValues.put("endDate", DateUtil.getDateTime(2020, 4, 8));		
		parameterValues.put("county", new String("Nairobi"));		
		
		EvaluationContext evaluationContext = new EvaluationContext();
		evaluationContext.setParameterValues(parameterValues);
		
		Cohort c = Context.getService(CohortDefinitionService.class).evaluate(cd, evaluationContext);
				Assert.assertTrue(c.getSize() == 1);


	}
}