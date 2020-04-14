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
import org.openmrs.module.kenyaemr.reporting.cohort.definition.ContactsUnderCovid19FollowupCohortDefinition;
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
 * Evaluator for contacts unrolled in covid-19 program
 */
@Handler(supports = {ContactsUnderCovid19FollowupCohortDefinition.class})
public class ContactsUnderCovid19FollowupCohortDefinitionEvaluator implements CohortDefinitionEvaluator {

    private final Log log = LogFactory.getLog(this.getClass());
	@Autowired
	EvaluationService evaluationService;

    @Override
    public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

		ContactsUnderCovid19FollowupCohortDefinition definition = (ContactsUnderCovid19FollowupCohortDefinition) cohortDefinition;

        if (definition == null)
            return null;

		Cohort newCohort = new Cohort();

		String qry="select patient_id\n" +
				"from (\n" +
				"select c.patient_id, self_q.patient_id selfQ, min(self_q.visit_date) first_self_q_followup_date, gov_q.patient_id govQ, min(gov_q.visit_date) first_gov_quarantine_date \n" +
				"from kenyaemr_hiv_testing_patient_contact c \n" +
				"inner join kenyaemr_etl.etl_covid_19_enrolment e on e.patient_id = c.patient_related_to\n" +
				"left join kenyaemr_etl.etl_contact_tracing_followup self_q on self_q.patient_id = c.patient_id\n" +
				"left join kenyaemr_etl.etl_covid_quarantine_enrolment gov_q on gov_q.patient_id = c.patient_id\n" +
				"where c.voided=0\n" +
				"group by c.patient_id\n" +
				") f where (selfQ is not null and datediff(curdate(),first_self_q_followup_date) < 14) or (govQ is not null and datediff(curdate(), first_gov_quarantine_date) < 14)";;

		SqlQueryBuilder builder = new SqlQueryBuilder();
		builder.append(qry);
		Date endDate = (Date)context.getParameterValue("endDate");
		builder.addParameter("endDate", endDate);
		List<Integer> ptIds = evaluationService.evaluateToList(builder, Integer.class, context);

		newCohort.setMemberIds(new HashSet<Integer>(ptIds));


        return new EvaluatedCohort(newCohort, definition, context);
    }

}
