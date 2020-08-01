/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.kenyaemr.reporting.data.converter.definition.evaluator.covid;

import org.openmrs.annotation.Handler;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.covid.FeverDataDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.covid.NumberNotContactedTodayDataDefinition;
import org.openmrs.module.reporting.data.person.EvaluatedPersonData;
import org.openmrs.module.reporting.data.person.definition.PersonDataDefinition;
import org.openmrs.module.reporting.data.person.evaluator.PersonDataEvaluator;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;

/**
 * Evaluates a Number Not Contacted Today DataDefinition
 */
@Handler(supports= NumberNotContactedTodayDataDefinition.class, order=50)
public class NumberNotContactedTodayDataEvaluator implements PersonDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedPersonData evaluate(PersonDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedPersonData c = new EvaluatedPersonData(definition, context);

        String qry = "select case_patient, count(*) from (\n" +
                "select patient_id, case_patient, coalesce(first_gov_quarantine_date, first_gov_quarantine_date) as followupStartDate, coalesce(last_gov_quarantine_date, last_gov_quarantine_date) as followupEndDate from (\n" +
                "select c.patient_id, c.patient_related_to case_patient, min(self_q.visit_date) first_self_q_followup_date, min(gov_q.visit_date) first_gov_quarantine_date, max(self_q.visit_date) last_self_q_followup_date, max(gov_q.visit_date) last_gov_quarantine_date\n" +
                "      from kenyaemr_hiv_testing_patient_contact c\n" +
                "             left join kenyaemr_etl.etl_contact_tracing_followup self_q on self_q.patient_id = c.patient_id\n" +
                "             left join kenyaemr_etl.etl_covid_quarantine_followup gov_q on gov_q.patient_id = c.patient_id\n" +
                "      where c.voided=0 and c.patient_id is not null\n" +
                "      group by c.patient_id\n" +
                "      ) f \n" +
                "having datediff(followupEndDate,followupStartDate) < 14 and date(followupEndDate) < curdate()) a\n" +
                "group by case_patient;";

        SqlQueryBuilder queryBuilder = new SqlQueryBuilder();
        Date startDate = (Date)context.getParameterValue("startDate");
        Date endDate = (Date)context.getParameterValue("endDate");
        queryBuilder.addParameter("endDate", endDate);
        queryBuilder.addParameter("startDate", startDate);
        queryBuilder.append(qry);
        Map<Integer, Object> data = evaluationService.evaluateToMap(queryBuilder, Integer.class, Object.class, context);
        c.setData(data);
        return c;
    }
}
