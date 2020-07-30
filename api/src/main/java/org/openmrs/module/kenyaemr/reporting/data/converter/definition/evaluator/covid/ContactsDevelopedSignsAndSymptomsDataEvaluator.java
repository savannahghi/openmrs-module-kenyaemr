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
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.covid.ContactsDevelopedSignsAndSymptomsDataDefinition;
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
 * Evaluates Contacts Developed Signs And Symptoms data definition
 */
@Handler(supports= ContactsDevelopedSignsAndSymptomsDataDefinition.class, order=50)
public class ContactsDevelopedSignsAndSymptomsDataEvaluator implements PersonDataEvaluator {

    @Autowired
    private EvaluationService evaluationService;

    public EvaluatedPersonData evaluate(PersonDataDefinition definition, EvaluationContext context) throws EvaluationException {
        EvaluatedPersonData c = new EvaluatedPersonData(definition, context);

        String qry = "select f.case_patient,count(f.patient_id)\n" +
                "from (\n" +
                "     select c.patient_id, self_q.patient_id selfQ,c.patient_related_to case_patient, coalesce(self_q.cough,gov_q.cough) cough, coalesce(self_q.fever, gov_q.fever) fever, coalesce(self_q.difficulty_breathing,gov_q.difficulty_breathing) difficulty_breathing,\n" +
                "            coalesce(self_q.sore_throat,gov_q.sore_throat) sorethroat,\n" +
                "            min(self_q.visit_date) first_self_q_followup_date, gov_q.patient_id govQ, min(gov_q.visit_date) first_gov_quarantine_date\n" +
                "     from kenyaemr_hiv_testing_patient_contact c\n" +
                "            inner join kenyaemr_etl.etl_covid_19_enrolment e on e.patient_id = c.patient_related_to\n" +
                "            left join kenyaemr_etl.etl_contact_tracing_followup self_q on self_q.patient_id = c.patient_id\n" +
                "            left join kenyaemr_etl.etl_covid_quarantine_followup gov_q on gov_q.patient_id = c.patient_id\n" +
                "     where c.voided=0 and e.voided = 0\n" +
                "     ) f where (f.fever = 'Yes' or f.cough = 'Yes' or f.difficulty_breathing = 'Yes' or f.sorethroat = 'Yes') and\n" +
                "    (selfQ is not null and datediff(curdate(),first_self_q_followup_date) < 14) or (govQ is not null and datediff(curdate(), first_gov_quarantine_date) < 14) group by f.case_patient;";

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
