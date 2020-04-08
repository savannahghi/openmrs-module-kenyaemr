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

import org.openmrs.module.reporting.indicator.CohortIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.openmrs.module.kenyacore.report.ReportUtils.map;
import static org.openmrs.module.kenyaemr.reporting.EmrReportingUtils.cohortIndicator;

/**
 * Created by dev on 20200408.
 */

/**
 * Library for Covid-19 indicators
 */
@Component
public class CovidDashboardCohortIndicators {
    @Autowired
    private CovidDashboardCohortQueries covidCohorts;

    // Green card additions

    /**
     * Newly diagnosed
     * covers indicators CV19-01
     *
     * @return indicator
     */
    public CohortIndicator newlyDiagnosed() {
        return cohortIndicator("Newly Diagnosed", map(covidCohorts.newlyDiagnosed(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Total confirmed
     * covers indicators  CV19-02
     *
     * @return indicator
     */
    public CohortIndicator totalConfirmedCases() {
        return cohortIndicator("Total Cases", map(covidCohorts.totalConfirmedCases(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Active cases
     * covers indicators  CV19-03
     *
     * @return indicator
     */
    public CohortIndicator activeCases() {
        return cohortIndicator("Active Cases", map(covidCohorts.activeCases(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Deceased
     * covers indicators  CV19-04
     *
     * @return indicator
     */
    public CohortIndicator deceased() {
        return cohortIndicator("Deceased", map(covidCohorts.deceased(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Discharged
     * covers indicators  CV19-05
     *
     * @return indicator
     */
    public CohortIndicator discharged() {
        return cohortIndicator("Repeat tests", map(covidCohorts.discharged(), "startDate=${startDate},endDate=${endDate}"));
    }
    /**
     * Persons under investigation
     * covers indicators CV19-06
     *
     * @return indicator
     */
    public CohortIndicator personsUnderInvestigation() {
        return cohortIndicator("Repeat tests", map(covidCohorts.personsUnderInvestigation(), "startDate=${startDate},endDate=${endDate}"));
    }
    /**
     * Persons Tested
     * covers indicators  CV19-07
     *
     * @return indicator
     */
    public CohortIndicator personsTested() {
        return cohortIndicator("Repeat tests", map(covidCohorts.personsTested(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Tested positive
     * covers indicators  CV19-08
     *
     * @return indicator
     */
    public CohortIndicator personsPositive() {
        return cohortIndicator("Repeat tests", map(covidCohorts.personsPositive(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Tested negative
     * covers indicators  CV19-09
     *
     * @return indicator
     */
    public CohortIndicator personsNegative() {
        return cohortIndicator("Repeat tests", map(covidCohorts.personsNegative(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Contacts  listed
     * covers indicators  CV19-10
     *
     * @return indicator
     */
    public CohortIndicator contactsListed() {
        return cohortIndicator("Repeat tests", map(covidCohorts.contactsListed(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Contacts reached
     * covers indicators  CV19-11
     *
     * @return indicator
     */
    public CohortIndicator contactsReached() {
        return cohortIndicator("Repeat tests", map(covidCohorts.contactsReached(), "startDate=${startDate},endDate=${endDate}"));
    }

    /**
     * Contacts under followup
     * covers indicators  CV19-12
     *
     * @return indicator
     */
    public CohortIndicator contactsUnderFollowup() {
        return cohortIndicator("Repeat tests", map(covidCohorts.contactsUnderFollowup(), "startDate=${startDate},endDate=${endDate}"));
    }
}


