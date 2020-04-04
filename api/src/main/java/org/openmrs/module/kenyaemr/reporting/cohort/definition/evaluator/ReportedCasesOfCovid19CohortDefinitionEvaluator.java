/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.kenyaemr.reporting.cohort.definition.evaluator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.module.kenyaemr.reporting.cohort.definition.ReportedCasesOfCovid19CohortDefinition;
import org.openmrs.module.kenyaemr.util.EmrUtils;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Evaluator for all reported cases. those enrolled in covid-19 program
 */
@Handler(supports = {ReportedCasesOfCovid19CohortDefinition.class})
public class ReportedCasesOfCovid19CohortDefinitionEvaluator implements CohortDefinitionEvaluator {

    private final Log log = LogFactory.getLog(this.getClass());
	@Autowired
	EvaluationService evaluationService;

    @Override
    public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

		ReportedCasesOfCovid19CohortDefinition definition = (ReportedCasesOfCovid19CohortDefinition) cohortDefinition;

        if (definition == null)
            return null;

		Cohort newCohort = new Cohort();
		SqlQueryBuilder builder = new SqlQueryBuilder();


		String qry = "";

		qry = "select pp.patient_id from patient_program pp \n" +
				"inner join (select program_id from program where uuid='e7ee7548-6958-4361-bed9-ee2614423947') p on pp.program_id = p.program_id\n" +
				"inner join kenyaemr_etl.etl_covid_19_enrolment e on e.patient_id = pp.patient_id \n" +
				"where pp.voided=0";

		/*if (EmrUtils.getUserCounty() != null) {
			qry = "select pp.patient_id from patient_program pp \n" +
					"inner join (select program_id from program where uuid='e7ee7548-6958-4361-bed9-ee2614423947') p on pp.program_id = p.program_id\n" +
					"inner join kenyaemr_etl.etl_covid_19_enrolment e on e.patient_id = pp.patient_id \n" +
					"where pp.voided=0 and e.county=:userCounty;";
			builder.addParameter("userCounty", EmrUtils.getUserCounty());

		} else {
			qry = "select pp.patient_id from patient_program pp \n" +
					"inner join (select program_id from program where uuid='e7ee7548-6958-4361-bed9-ee2614423947') p on pp.program_id = p.program_id\n" +
					"inner join kenyaemr_etl.etl_covid_19_enrolment e on e.patient_id = pp.patient_id \n" +
					"where pp.voided=0";
		}*/

		builder.append(qry);
		Date endDate = (Date)context.getParameterValue("endDate");
		builder.addParameter("endDate", endDate);
		List<Integer> ptIds = evaluationService.evaluateToList(builder, Integer.class, context);

		newCohort.setMemberIds(new HashSet<Integer>(ptIds));


        return new EvaluatedCohort(newCohort, definition, context);
    }

}
