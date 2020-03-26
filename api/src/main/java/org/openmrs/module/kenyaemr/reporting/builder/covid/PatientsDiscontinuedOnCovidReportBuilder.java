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

import org.openmrs.PatientIdentifierType;
import org.openmrs.module.kenyacore.report.HybridReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractHybridReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyacore.report.data.patient.definition.CalculationDataDefinition;
import org.openmrs.module.kenyaemr.calculation.library.hiv.art.CurrentArtRegimenCalculation;
import org.openmrs.module.kenyaemr.calculation.library.hiv.art.ViralLoadResultCalculation;
import org.openmrs.module.kenyaemr.metadata.HivMetadata;
import org.openmrs.module.kenyaemr.reporting.calculation.converter.RDQASimpleObjectRegimenConverter;
import org.openmrs.module.kenyaemr.reporting.cohort.definition.covid.PatientsDiscontinuedOnCovidCohortDefinition;
import org.openmrs.module.kenyaemr.reporting.cohort.definition.otz.PatientsOnOTZCohortDefinition;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.*;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Builds({"kenyaemr.covid.report.discontinuedOnCovid"})
public class PatientsDiscontinuedOnCovidReportBuilder extends AbstractHybridReportBuilder {
    public static final String DATE_FORMAT = "dd/MM/yyyy";


    @Override
    protected void addColumns(HybridReportDescriptor report, PatientDataSetDefinition dsd) {
    }

    @Override
    protected Mapped<CohortDefinition> buildCohort(HybridReportDescriptor descriptor, PatientDataSetDefinition dsd) {
        return null;
    }


    protected Mapped<CohortDefinition> allPatientsCohort() {
        CohortDefinition cd = new PatientsDiscontinuedOnCovidCohortDefinition();
        cd.setName("Patients Discontinued");
        return ReportUtils.map(cd, "");
    }

    @Override
    protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report) {

        PatientDataSetDefinition allVisits = patientsDiscontinuedOnCovidDataSetDefinition("patientsDiscontinued");
        allVisits.addRowFilter(allPatientsCohort());
        DataSetDefinition allPatientsDSD = allVisits;

        return Arrays.asList(
                ReportUtils.map(allPatientsDSD, "")
        );
    }

    protected PatientDataSetDefinition patientsDiscontinuedOnCovidDataSetDefinition(String datasetName) {

        PatientDataSetDefinition dsd = new PatientDataSetDefinition(datasetName);

        DataConverter formatter = new ObjectFormatter("{familyName}, {givenName}");
        DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), formatter);
        dsd.addColumn("id", new PersonIdDataDefinition(), "");
        dsd.addColumn("Name", nameDef, "");
        dsd.addColumn("Sex", new GenderDataDefinition(), "", null);
        dsd.addColumn("Age", new AgeDataDefinition(), "", null);

        return dsd;
    }
}
