/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.kenyaemr.reporting.library.covid;

import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.cohort.definition.SqlCohortDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by dev on 20200408.
 */

/**
 * Library of cohort queries used for covid-19 indicators
 */
@Component

public class CovidDashboardCohortQueries {

    /*Newly diagnosed cases*/
    public CohortDefinition newlyDiagnosed(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select patient_id\n" +
                "from (\n" +
                "select pp.patient_id,prev_pos.patient_id as prevPos\n" +
                "from kenyaemr_etl.etl_patient_program pp\n" +
                "inner join kenyaemr_etl.etl_laboratory_extract l on l.patient_id=pp.patient_id and l.result = 703\n" +
                "left outer join (select l.patient_id\n" +
                "from kenyaemr_etl.etl_patient_program pp\n" +
                "inner join kenyaemr_etl.etl_laboratory_extract l on l.patient_id=pp.patient_id and l.result = 703 and l.order_date < date(:endDate)\n" +
                "where pp.program='COVID-19 Case Investigation' and pp.date_completed is null) prev_pos on prev_pos.patient_id = l.patient_id\n" +
                "where pp.program='COVID-19 Case Investigation' and pp.date_completed is null and l.order_date = date(:endDate)\n" +
                ") t where prevPos is null;;";
        cd.setName("newlyDiagnosed");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Newly diagnosed Covid19");

        return cd;
    }

    /*Total confirmed cases*/
    public CohortDefinition totalConfirmedCases(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select pp.patient_id\n" +
                "from kenyaemr_etl.etl_patient_program pp\n" +
                "inner join kenyaemr_etl.etl_laboratory_extract l on l.patient_id=pp.patient_id and l.result = 703\n" +
                "where pp.program='COVID-19 Case Investigation'";
        cd.setName("totalConfirmedCases");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Total confirmed Covid19 cases");

        return cd;
    }

    /*Active cases*/
    public CohortDefinition activeCases(){
        SqlCohortDefinition cd = new SqlCohortDefinition();

        String sqlQuery = "select pp.patient_id\n" +
                "from kenyaemr_etl.etl_patient_program pp\n" +
                "inner join kenyaemr_etl.etl_laboratory_extract l on l.patient_id=pp.patient_id and l.result = 703\n" +
                "where pp.program='COVID-19 Case Investigation' and (pp.date_completed is null or pp.date_completed > date(:endDate))";

        cd.setName("activeCases");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Active Covid19 cases");

        return cd;
    }

    /*Deceased*/
    public CohortDefinition deceased(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select pd.patient_id\n" +
                "from kenyaemr_etl.etl_patient_program_discontinuation pd\n" +
                "where pd.program_name='COVID-19 Case Investigation' and pd.discontinuation_reason = 160034  and date(pd.encounter_date) between date(:startDate) and date(:endDate)\n" +
                "group by pd.patient_id;\n";
        cd.setName("deceased");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Deceased");

        return cd;
    }

    /*Discharged*/
    public CohortDefinition discharged(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select pd.patient_id\n" +
                "from kenyaemr_etl.etl_patient_program_discontinuation pd\n" +
                "where pd.program_name='COVID-19 Case Investigation' and pd.discontinuation_reason != 160034  and date(pd.encounter_date) between date(:startDate) and date(:endDate)\n" +
                "group by pd.patient_id;";
        cd.setName("discharged");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Discharged");

        return cd;
    }

    /*Persons under investigation*/
    public CohortDefinition personsUnderInvestigation(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select patient_id from\n" +
                "  kenyaemr_etl.etl_covid_19_enrolment where voided=0 and date(visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("personsUnderInvestigation");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Persons Under Investigation");

        return cd;
    }

    /*Persons Tested*/
    public CohortDefinition personsTested(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select e.patient_id \n" +
                "from kenyaemr_etl.etl_covid_19_enrolment e\n" +
                "inner join kenyaemr_etl.etl_laboratory_extract l on l.patient_id=e.patient_id and l.result is not null\n" +
                "where e.voided=0 and date(e.visit_date) between date(:startDate) and date(:endDate)";
        cd.setName("personsTested");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Persons Tested");

        return cd;
    }

    /*Persons Tested Positive*/
    public CohortDefinition personsPositive(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select e.patient_id \n" +
                "from kenyaemr_etl.etl_covid_19_enrolment e\n" +
                "inner join kenyaemr_etl.etl_laboratory_extract l on l.patient_id=e.patient_id and l.result = 703\n" +
                "where e.voided=0 and date(e.visit_date) between date(:startDate) and date(:endDate);";
        cd.setName("personsPositive");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Tested Positive");

        return cd;
    }
/*Persons Tested Negative*/
    public CohortDefinition personsNegative(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select patient_id\n" +
                "from (\n" +
                "select pp.patient_id,prev_pos.patient_id as prevPos\n" +
                "from kenyaemr_etl.etl_patient_program pp\n" +
                "inner join kenyaemr_etl.etl_laboratory_extract l on l.patient_id=pp.patient_id and l.result = 664\n" +
                "left outer join (select l.patient_id\n" +
                "from kenyaemr_etl.etl_patient_program pp\n" +
                "inner join kenyaemr_etl.etl_laboratory_extract l on l.patient_id=pp.patient_id and l.result = 703 and l.order_date <= date(:endDate)\n" +
                "where pp.program='COVID-19 Case Investigation' and pp.date_completed is null) prev_pos on prev_pos.patient_id = l.patient_id\n" +
                "where pp.program='COVID-19 Case Investigation' and pp.date_completed is null and l.order_date <= date(:endDate)\n" +
                ") t where prevPos is null;";
        cd.setName("personsNegative");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Tested Negative");

        return cd;
    }
    /*Contacts Listed*/
    public CohortDefinition contactsListed(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select c.id \n" +
                "from kenyaemr_hiv_testing_patient_contact c \n" +
                "inner join kenyaemr_etl.etl_covid_19_enrolment e on e.patient_id = c.patient_related_to\n" +
                "where c.voided=0\n" +
                          ";";
        cd.setName("contactsListed");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Contacts Listed");

        return cd;
    }

    /*Contacts reached*/
    public CohortDefinition contactsReached(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select p.person_id from person p\n" +
                " inner join kenyaemr_hiv_testing_patient_contact c on p.person_id = c.patient_id\n" +
                " where p.voided = 0;";
        cd.setName("contactsReached");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Contacts reached");

        return cd;
    }
/*Contacts under followup*/
    public CohortDefinition contactsUnderFollowup(){
        SqlCohortDefinition cd = new SqlCohortDefinition();
        String sqlQuery = "select patient_id\n" +
                "from (\n" +
                "select c.patient_id, self_q.patient_id selfQ, min(self_q.visit_date) first_self_q_followup_date, gov_q.patient_id govQ, min(gov_q.visit_date) first_gov_quarantine_date \n" +
                "from kenyaemr_hiv_testing_patient_contact c \n" +
                "inner join kenyaemr_etl.etl_covid_19_enrolment e on e.patient_id = c.patient_related_to\n" +
                "left join kenyaemr_etl.etl_contact_tracing_followup self_q on self_q.patient_id = c.patient_id\n" +
                "left join kenyaemr_etl.etl_covid_quarantine_enrolment gov_q on gov_q.patient_id = c.patient_id\n" +
                "where c.voided=0\n" +
                "group by c.patient_id\n" +
                ") f where (selfQ is not null and datediff(curdate(),first_self_q_followup_date) < 14) or (govQ is not null and datediff(curdate(), first_gov_quarantine_date) < 14)";
        cd.setName("contactsUnderFollowup");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Contacts under followup");

        return cd;
    }

  }
