/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.kenyaemr.reporting.builder.covid;

import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyaemr.reporting.ColumnParameters;
import org.openmrs.module.kenyaemr.reporting.EmrReportingUtils;
import org.openmrs.module.kenyaemr.reporting.library.covid.CovidDashboardCohortIndicators;
import org.openmrs.module.kenyaemr.reporting.library.shared.common.CommonDimensionLibrary;
import org.openmrs.module.reporting.dataset.definition.CohortIndicatorDataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Report builder for Covid-19 Indicatorrs
 */
@Component
@Builds({"kenyaemr.common.report.covidDashboardIndicators"})
public class Covid19DashboardReportBuilder extends AbstractReportBuilder {
    @Autowired
    private CommonDimensionLibrary commonDimensions;

    @Autowired
    private CovidDashboardCohortIndicators covidIndicators;

    public static final String DATE_FORMAT = "yyyy-MM-dd";

    ColumnParameters m_0_to_9 = new ColumnParameters(null, "0-9, Male", "gender=M|age=0-9");
    ColumnParameters f_0_to_9 = new ColumnParameters(null, "0-9, Female", "gender=F|age=0-9");
    ColumnParameters m_10_to_19 = new ColumnParameters(null, "10-19, Male", "gender=M|age=10-19");
    ColumnParameters f_10_to_19 = new ColumnParameters(null, "10-19, Female", "gender=F|age=10-19");
    ColumnParameters m_20_to_29 = new ColumnParameters(null, "20-29, Male", "gender=M|age=20-29");
    ColumnParameters f_20_to_29 = new ColumnParameters(null, "20-29, Female", "gender=F|age=20-29");
    ColumnParameters m_30_to_39 = new ColumnParameters(null, "30-39, Male", "gender=M|age=30-39");
    ColumnParameters f_30_to_39 = new ColumnParameters(null, "30-39, Female", "gender=F|age=30-39");
    ColumnParameters m_40_to_49 = new ColumnParameters(null, "40-49, Male", "gender=M|age=40-49");
    ColumnParameters f_40_to_49 = new ColumnParameters(null, "40-49, Female", "gender=F|age=40-49");
    ColumnParameters m_50_to_59 = new ColumnParameters(null, "50-59, Male", "gender=M|age=50-59");
    ColumnParameters f_50_to_59 = new ColumnParameters(null, "50-59, Female", "gender=F|age=50-59");
    ColumnParameters m_60_to_69 = new ColumnParameters(null, "60-69, Male", "gender=M|age=60-69");
    ColumnParameters f_60_to_69 = new ColumnParameters(null, "60-69, Female", "gender=F|age=60-69");
    ColumnParameters m_70_and_above = new ColumnParameters(null, "70+, Male", "gender=M|age=70+");
    ColumnParameters f_70_and_above = new ColumnParameters(null, "70+, Female", "gender=F|age=70+");

    ColumnParameters colTotal = new ColumnParameters(null, "Total", "");

    List<ColumnParameters> covidDisaggregationAgeAndSex = Arrays.asList(m_0_to_9, f_0_to_9, m_10_to_19, f_10_to_19, m_20_to_29,
            f_20_to_29, m_30_to_39, f_30_to_39, m_40_to_49, f_40_to_49, m_50_to_59, f_50_to_59, m_60_to_69, f_60_to_69, m_70_and_above,
            f_70_and_above, colTotal);

    @Override
    protected List<Parameter> getParameters(ReportDescriptor reportDescriptor) {
        return Arrays.asList(
                new Parameter("startDate", "Start Date", Date.class),
                new Parameter("endDate", "End Date", Date.class),
                new Parameter("dateBasedReporting", "", String.class)
        );
    }

    @Override
    protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor reportDescriptor, ReportDefinition reportDefinition) {
        return Arrays.asList(
                ReportUtils.map(covid19DatasetDefinition(), "startDate=${startDate},endDate=${endDate}")
        );
    }

    /**
     * Creates the dataset for section Covid-19 indicators
     *
     * @return the dataset
     */
    protected DataSetDefinition covid19DatasetDefinition() {
        CohortIndicatorDataSetDefinition cohortDsd = new CohortIndicatorDataSetDefinition();
        cohortDsd.setName("Covid-19");
        cohortDsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cohortDsd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cohortDsd.addDimension("age", ReportUtils.map(commonDimensions.covid19AgeGroups(), "onDate=${endDate}"));
        cohortDsd.addDimension("gender", ReportUtils.map(commonDimensions.gender()));
        String indParams = "startDate=${startDate},endDate=${endDate}";

        //Covid-19 Indicators
        EmrReportingUtils.addRow(cohortDsd, "Newly Diagnosed", "Newly Diagnosed", ReportUtils.map(covidIndicators.newlyDiagnosed(), indParams), covidDisaggregationAgeAndSex, Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17"));
        EmrReportingUtils.addRow(cohortDsd, "Confirmed Cases", "Confirmed Cases", ReportUtils.map(covidIndicators.totalConfirmedCases(), indParams), covidDisaggregationAgeAndSex, Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17"));
       // EmrReportingUtils.addRow(cohortDsd, "Active Cases", "Active Cases", ReportUtils.map(covidIndicators.activeCases(), indParams), covidDisaggregationAgeAndSex, Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17"));
        EmrReportingUtils.addRow(cohortDsd, "Deceased", "Deceased", ReportUtils.map(covidIndicators.deceased(), indParams), covidDisaggregationAgeAndSex, Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17"));
        EmrReportingUtils.addRow(cohortDsd, "Discharged", "Discharged", ReportUtils.map(covidIndicators.discharged(), indParams), covidDisaggregationAgeAndSex, Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17"));
       // EmrReportingUtils.addRow(cohortDsd, "Persons under investigation", "Persons under investigation", ReportUtils.map(covidIndicators.personsUnderInvestigation(), indParams), covidDisaggregationAgeAndSex, Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17"));
        EmrReportingUtils.addRow(cohortDsd, "Persons tested", "Persons tested", ReportUtils.map(covidIndicators.personsTested(), indParams), covidDisaggregationAgeAndSex, Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17"));
        EmrReportingUtils.addRow(cohortDsd, "Persons positive", "Persons positive", ReportUtils.map(covidIndicators.personsPositive(), indParams), covidDisaggregationAgeAndSex, Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17"));
        EmrReportingUtils.addRow(cohortDsd, "Persons negative", "Persons negative", ReportUtils.map(covidIndicators.personsNegative(), indParams), covidDisaggregationAgeAndSex, Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17"));
        EmrReportingUtils.addRow(cohortDsd, "Contacts listed", "Contacts listed", ReportUtils.map(covidIndicators.contactsListed(), indParams), covidDisaggregationAgeAndSex, Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17"));
        //EmrReportingUtils.addRow(cohortDsd, "Contacts under follow-up", "Contacts under follow-up", ReportUtils.map(covidIndicators.contactsUnderFollowup(), indParams), covidDisaggregationAgeAndSex, Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17"));
        EmrReportingUtils.addRow(cohortDsd, "Contacts reached", "Contacts reached", ReportUtils.map(covidIndicators.contactsReached(), indParams), covidDisaggregationAgeAndSex, Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17"));

        return cohortDsd;

    }
}
