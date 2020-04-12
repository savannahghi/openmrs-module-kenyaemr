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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Cohort;
import org.openmrs.annotation.Handler;
import org.openmrs.module.kenyaemr.reporting.cohort.definition.covid.DeadCovid19CasesCohortDefinition;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.evaluator.CohortDefinitionEvaluator;
import org.openmrs.module.reporting.common.ObjectUtil;
import org.openmrs.module.reporting.evaluation.EvaluationContext;
import org.openmrs.module.reporting.evaluation.EvaluationException;
import org.openmrs.module.reporting.evaluation.querybuilder.SqlQueryBuilder;
import org.openmrs.module.reporting.evaluation.service.EvaluationService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * Evaluator for Covid dead cases
 */
@Handler(supports = {DeadCovid19CasesCohortDefinition.class})
public class DeadCovid19CasesCohortDefinitionEvaluator implements CohortDefinitionEvaluator {

    private final Log log = LogFactory.getLog(this.getClass());
    @Autowired
    EvaluationService evaluationService;

    @Override
    public EvaluatedCohort evaluate(CohortDefinition cohortDefinition, EvaluationContext context) throws EvaluationException {

        DeadCovid19CasesCohortDefinition definition = (DeadCovid19CasesCohortDefinition) cohortDefinition;

        if (definition == null)
            return null;

        Cohort newCohort = new Cohort();

        context = ObjectUtil.nvl(context, new EvaluationContext());

        String qry = "";
        SqlQueryBuilder builder = new SqlQueryBuilder();

        qry = "select pd.patient_id from kenyaemr_etl.etl_patient_program_discontinuation pd\n" +
                "where pd.program_name='COVID-19 Case Investigation' and pd.discontinuation_reason = 160034\n" +
                "group by pd.patient_id;";


        builder.append(qry);
        Date startDate = (Date) context.getParameterValue("startDate");
        Date endDate = (Date) context.getParameterValue("endDate");
        builder.addParameter("endDate", endDate);
        builder.addParameter("startDate", startDate);

        List<Integer> ptIds = evaluationService.evaluateToList(builder, Integer.class, context);
        newCohort.setMemberIds(new HashSet<Integer>(ptIds));

        return new EvaluatedCohort(newCohort, definition, context);
    }

}
