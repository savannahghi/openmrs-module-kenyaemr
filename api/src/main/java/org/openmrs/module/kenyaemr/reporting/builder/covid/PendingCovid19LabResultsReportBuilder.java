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
import org.openmrs.PersonAttributeType;
import org.openmrs.module.kenyacore.report.HybridReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportUtils;
import org.openmrs.module.kenyacore.report.builder.AbstractHybridReportBuilder;
import org.openmrs.module.kenyacore.report.builder.Builds;
import org.openmrs.module.kenyacore.report.data.patient.definition.CalculationDataDefinition;
import org.openmrs.module.kenyaemr.calculation.library.covid.PersonAddressCountyCalculation;
import org.openmrs.module.kenyaemr.calculation.library.covid.PersonAddressNationalityCalculation;
import org.openmrs.module.kenyaemr.calculation.library.covid.PersonAddressSubCountyCalculation;
import org.openmrs.module.kenyaemr.calculation.library.covid.PersonAddressVillageEstateCalculation;
import org.openmrs.module.kenyaemr.calculation.library.covid.PersonAddressWardCalculation;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.module.kenyaemr.reporting.calculation.converter.RDQACalculationResultConverter;
import org.openmrs.module.kenyaemr.reporting.cohort.definition.covid.PendingCovid19LabResultsCohortDefinition;
import org.openmrs.module.kenyaemr.reporting.data.converter.IdentifierConverter;
import org.openmrs.module.kenyaemr.reporting.data.converter.definition.covid.*;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.module.reporting.cohort.definition.CohortDefinition;
import org.openmrs.module.reporting.data.DataDefinition;
import org.openmrs.module.reporting.data.converter.BirthdateConverter;
import org.openmrs.module.reporting.data.converter.DataConverter;
import org.openmrs.module.reporting.data.converter.DateConverter;
import org.openmrs.module.reporting.data.converter.ObjectFormatter;
import org.openmrs.module.reporting.data.patient.definition.ConvertedPatientDataDefinition;
import org.openmrs.module.reporting.data.patient.definition.PatientIdentifierDataDefinition;
import org.openmrs.module.reporting.data.person.definition.*;
import org.openmrs.module.reporting.dataset.definition.DataSetDefinition;
import org.openmrs.module.reporting.dataset.definition.PatientDataSetDefinition;
import org.openmrs.module.reporting.evaluation.parameter.Mapped;
import org.openmrs.module.reporting.evaluation.parameter.Parameter;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Component
@Builds({"kenyaemr.common.report.pendingLabResults"})
public class PendingCovid19LabResultsReportBuilder extends AbstractHybridReportBuilder {
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    @Override
    protected Mapped<CohortDefinition> buildCohort(HybridReportDescriptor descriptor, PatientDataSetDefinition dsd) {
        return covidPendingLabResultsPatientsCohort();
    }

    protected Mapped<CohortDefinition> covidPendingLabResultsPatientsCohort() {
        CohortDefinition cd = new PendingCovid19LabResultsCohortDefinition();
        cd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        cd.addParameter(new Parameter("endDate", "End Date", Date.class));
        cd.setName("CovidPendingLabResultsPatients");
        return ReportUtils.map(cd, "startDate=${startDate},endDate=${endDate}");
    }

    @Override
    protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report) {

        PatientDataSetDefinition covidPatients = covidPendinLabResultsDataSetDefinition();
        covidPatients.addRowFilter(covidPendingLabResultsPatientsCohort());
        DataSetDefinition covidPatientsDSD = covidPatients;

        return Arrays.asList(
                ReportUtils.map(covidPatientsDSD, "startDate=${startDate},endDate=${endDate}")
        );
    }

    protected PatientDataSetDefinition covidPendinLabResultsDataSetDefinition() {

        PatientIdentifierType nationalIdType = MetadataUtils.existing(PatientIdentifierType.class, CommonMetadata._PatientIdentifierType.NATIONAL_ID);
        PatientIdentifierType passportNumberType = MetadataUtils.existing(PatientIdentifierType.class, CommonMetadata._PatientIdentifierType.PASSPORT_NUMBER);
        DataDefinition natIdDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(nationalIdType.getName(), nationalIdType), new IdentifierConverter());
        DataDefinition passportDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(passportNumberType.getName(), passportNumberType), new IdentifierConverter());
        PersonAttributeType phoneNumber = MetadataUtils.existing(PersonAttributeType.class, CommonMetadata._PersonAttributeType.TELEPHONE_CONTACT);
        String DATE_FORMAT = "dd/MM/yyyy";
        PatientDataSetDefinition dsd = new PatientDataSetDefinition("CovidLinelist");
        dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        dsd.addParameter(new Parameter("endDate", "End Date", Date.class));
        dsd.addParameter(new Parameter("startDate", "Start Date", Date.class));
        String defParam = "startDate=${startDate}";

        DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName} {middleName}");
        DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);
        dsd.addColumn("id", new PersonIdDataDefinition(), "");

        dsd.addColumn("Name", nameDef, "");
        dsd.addColumn("National ID", natIdDef, "");
        dsd.addColumn("Passport Number", passportDef, "");
        dsd.addColumn("Date of Birth", new BirthdateDataDefinition(), "", new BirthdateConverter(DATE_FORMAT));
        dsd.addColumn("Age", new AgeDataDefinition(), "");
        dsd.addColumn("Sex", new GenderDataDefinition(), "");
        dsd.addColumn("Occupation", new OccupationDataDefinition(), "");
        dsd.addColumn("Phone Number", new PersonAttributeDataDefinition(phoneNumber), "");
        dsd.addColumn("Nationality", new CalculationDataDefinition("Nationality", new PersonAddressNationalityCalculation()), "", new RDQACalculationResultConverter());
        dsd.addColumn("County", new CalculationDataDefinition("County", new PersonAddressCountyCalculation()), "", new RDQACalculationResultConverter());
        dsd.addColumn("Sub-County", new CalculationDataDefinition("Sub-County", new PersonAddressSubCountyCalculation()), "", new RDQACalculationResultConverter());
        dsd.addColumn("Ward", new CalculationDataDefinition("Ward", new PersonAddressWardCalculation()), "", new RDQACalculationResultConverter());
        dsd.addColumn("Village/Estate", new CalculationDataDefinition("Village/Estate", new PersonAddressVillageEstateCalculation()), "", new RDQACalculationResultConverter());
        dsd.addColumn("Reporting County",new CountyDataDefinition(), "");
        dsd.addColumn("Reporting Sub-County",new SubcountyDataDefinition(), "");
        dsd.addColumn("Travel History(Last 14 days)",new RecentTravelDataDefinition(), "");
        dsd.addColumn("History of Contact with Covid-19 case",new RecentContactWithCovidCaseDataDefinition(), "");
        dsd.addColumn("Symptoms(Yes/No)",new SymptomsDataDefinition(), "");
        dsd.addColumn("Classification(Meet case Definition/Do not meet case definition)",new ClassificationDataDefinition(), "");
        dsd.addColumn("Date of onset of symptoms",new SymptomsOnsetDateDataDefinition(), "");
        dsd.addColumn("Cough",new CoughDataDefinition(), "");
        dsd.addColumn("Fever",new FeverDataDefinition(), "");
        dsd.addColumn("Difficulty in breathing",new DifficultyBreathingDataDefinition(), "");
        dsd.addColumn("Others",new OtherSymptomsDataDefinition(), "");
        dsd.addColumn("Visit Health facility",new VisitedHFDataDefinition(), "");
        dsd.addColumn("Health facility Name",new HealthFacilityNameDataDefinition(), "");
        dsd.addColumn("Date of Visit(Hospital)",new HospitalVisitDateDataDefinition(), "");
        dsd.addColumn("Hospitalized",new HospitalizedDataDefinition(), "");
        dsd.addColumn("Date of Admission",new DateOfAdmissionDataDefinition(), "");
        dsd.addColumn("Specimen ID",new SpecimenIdDataDefinition(), "");
        dsd.addColumn("Date sample collected", new OrderDateDataDefinition(), "",new DateConverter(DATE_FORMAT));
        dsd.addColumn("Order Reason", new OrderReasonDataDefinition(), "");
        dsd.addColumn("Sample Type", new SampleTypeDataDefinition(), "");
        dsd.addColumn("Lab Name", new LabNameDataDefinition(), "");

        return dsd;
    }

}

