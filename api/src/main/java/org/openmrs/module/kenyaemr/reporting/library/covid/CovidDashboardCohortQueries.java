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
        String sqlQuery = "select e.person_id\n" +
                "                from\n" +
                "                (SELECT ob.person_id,ob.order_id,max(obs_datetime),min(obs_datetime) as results_date FROM openmrs.obs ob\n" +
                "                join openmrs.orders od on od.patient_id = ob.person_id and od.order_id =ob.order_id\n" +
                "                where ob.order_id is not null and ob.value_coded =703 group by ob.person_id\n" +
                "                 having min(od.date_activated) between date(:startDate) and date(:endDate) )e;";
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
        String sqlQuery = "select p.person_id\n" +
                "from(\n" +
                "select obs.person_id, max(obs.obs_datetime) as confirmation_date from openmrs.obs obs\n" +
                "join openmrs.person p on p.person_id=obs.person_id\n" +
                "where obs.voided=0 and obs.value_coded =703 and obs.order_id is not null\n" +
                "group by obs.person_id\n" +
                ")p;";
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
        String sqlQuery = ";";
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
                " from kenyaemr_etl.etl_patient_program_discontinuation pd\n" +
                "  inner join openmrs.person p on p.person_id=pd.patient_id\n" +
                "where pd.discontinuation_reason = 160034\n" +
                "group by pd.patient_id;";
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
        String sqlQuery = "select p.person_id\n" +
                "from(\n" +
                "    select obs.person_id, max(obs.obs_datetime) as confirmation_date from openmrs.obs obs\n" +
                "    join openmrs.person p on p.person_id=obs.person_id\n" +
                "    join  kenyaemr_etl.etl_patient_program_discontinuation d on obs.person_id = d.patient_id\n" +
                "    where obs.voided=0 and obs.value_coded =703 and obs.order_id is not null and d.program_name in ('COVID-19 Outcome','COVID-19 Quarantine Outcome')\n" +
                "    and d.discontinuation_reason !=160034\n" +
                "    group by obs.person_id\n" +
                "    )p;";
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
                "  (select cf.patient_id from kenyaemr_etl.etl_contact_tracing_followup cf\n" +
                "  inner join person p on p.person_id = cf.patient_id\n" +
                " group by cf.patient_id ) t;";
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
        String sqlQuery = "select t.person_id\n" +
                "from(\n" +
                "select obs.person_id, max(obs.obs_datetime) as confirmation_date from openmrs.obs obs\n" +
                "join openmrs.person p on p.person_id=obs.person_id\n" +
                "where obs.voided=0 and obs.order_id is not null and obs.value_coded =664 or obs.value_coded =703\n" +
                "group by obs.person_id\n" +
                ")t;";
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
        String sqlQuery = "select p.person_id\n" +
                "from(\n" +
                "select obs.person_id, max(obs.obs_datetime) as confirmation_date from openmrs.obs obs\n" +
                "join openmrs.person p on p.person_id=obs.person_id\n" +
                "where obs.voided=0 and obs.value_coded =703 and obs.order_id is not null\n" +
                "group by obs.person_id\n" +
                ")p;";
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
        String sqlQuery = "select n.person_id\n" +
                "from(\n" +
                "select obs.person_id, max(obs.obs_datetime) as confirmation_date from openmrs.obs obs\n" +
                "join openmrs.person p on p.person_id=obs.person_id\n" +
                "where obs.voided=0 and obs.value_coded =664 and obs.order_id is not null\n" +
                "group by obs.person_id\n" +
                ")n;";
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
        String sqlQuery = "select person_id from (select p.person_id from person p\n" +
                "  inner join kenyaemr_hiv_testing_patient_contact c on p.person_id = c.patient_related_to\n" +
                "group by p.person_id ) t;";
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
        String sqlQuery = "select person_id from (select p.person_id from person p\n" +
                "  inner join kenyaemr_hiv_testing_patient_contact c on p.person_id = c.patient_related_to\n" +
                "  inner join kenyaemr_hiv_testing_client_trace t on t.client_id = c.id\n" +
                "where t.status = \"Contacted\"\n" +
                "group by p.person_id ) t;";
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
        String sqlQuery = "select patient_id from\n" +
                "  (select patient_id,max(visit_date) from kenyaemr_etl.etl_contact_tracing_followup cf\n" +
                "    inner join person p on p.person_id = cf.patient_id\n" +
                "  group by cf.patient_id having round(DATEDIFF(max(cf.visit_date),now())) < 14) t;";
        cd.setName("contactsUnderFollowup");
        cd.setQuery(sqlQuery);
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setDescription("Contacts under followup");

        return cd;
    }

  }
