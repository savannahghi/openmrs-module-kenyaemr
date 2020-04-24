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
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.module.kenyaemr.reporting.cohort.definition.covid.ConfirmedCovid19PositivesCohortDefinition;
import org.openmrs.module.kenyaemr.reporting.cohort.definition.covid.Covid19FollowupCasesCohortDefinition;
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
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Builds({"kenyaemr.common.report.contactTracing"})
public class Covid19FollowupCasesReportBuilder extends AbstractHybridReportBuilder {
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    @Override
    protected Mapped<CohortDefinition> buildCohort(HybridReportDescriptor descriptor, PatientDataSetDefinition dsd) {
        return covidContactsFollowupCohort();
    }

    protected Mapped<CohortDefinition> covidContactsFollowupCohort() {
        CohortDefinition cd = new Covid19FollowupCasesCohortDefinition();
        cd.setName("covidCases");
        return ReportUtils.map(cd, "");
    }

    @Override
    protected List<Mapped<DataSetDefinition>> buildDataSets(ReportDescriptor descriptor, ReportDefinition report) {

        PatientDataSetDefinition covidCases = covidContactsDataSetDefinition();
        covidCases.addRowFilter(covidContactsFollowupCohort());
        DataSetDefinition covidCasesDSD = covidCases;

        return Arrays.asList(
                ReportUtils.map(covidCasesDSD, "")
        );
    }

    protected PatientDataSetDefinition covidContactsDataSetDefinition() {

        PatientIdentifierType nationalIdType = MetadataUtils.existing(PatientIdentifierType.class, CommonMetadata._PatientIdentifierType.NATIONAL_ID);
        PatientIdentifierType passportNumberType = MetadataUtils.existing(PatientIdentifierType.class, CommonMetadata._PatientIdentifierType.PASSPORT_NUMBER);
        DataDefinition natIdDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(nationalIdType.getName(), nationalIdType), new IdentifierConverter());
        DataDefinition passportDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(passportNumberType.getName(), passportNumberType), new IdentifierConverter());
        PersonAttributeType phoneNumber = MetadataUtils.existing(PersonAttributeType.class, CommonMetadata._PersonAttributeType.TELEPHONE_CONTACT);
        String DATE_FORMAT = "dd/MM/yyyy";
        PatientDataSetDefinition dsd = new PatientDataSetDefinition("covidCases");

        PatientIdentifierType upn = MetadataUtils.existing(PatientIdentifierType.class, CommonMetadata._PatientIdentifierType.NATIONAL_ID);
        DataConverter identifierFormatter = new ObjectFormatter("{identifier}");
        DataDefinition identifierDef = new ConvertedPatientDataDefinition("identifier", new PatientIdentifierDataDefinition(upn.getName(), upn), identifierFormatter);

        DataConverter nameFormatter = new ObjectFormatter("{familyName}, {givenName} {middleName}");
        DataDefinition nameDef = new ConvertedPersonDataDefinition("name", new PreferredNameDataDefinition(), nameFormatter);
        dsd.addColumn("id", new PersonIdDataDefinition(), "");

        dsd.addColumn("Case Name", nameDef, "");
        //dsd.addColumn("Case", nameDef, "");
        dsd.addColumn("Contacts identified",new ContactsIdentifiedDataDefinition(), "");
        dsd.addColumn("Contacts converted to cases",new ContactsConvertedToCasesDataDefinition(), "");
        dsd.addColumn("Contacts on followup",new ContactsOnFollowUpDataDefinition(), "");
        dsd.addColumn("Contacted today",new NumberContactedTodayDataDefinition(), "");
        dsd.addColumn("Not contacted today",new NumberNotContactedTodayDataDefinition(), "");
        //dsd.addColumn("Response rate",new ResponseRateDataDefinition(), "");
        dsd.addColumn("Developed signs and symptoms",new ContactsDevelopedSignsAndSymptomsDataDefinition(), "");
        dsd.addColumn("Positive lab results",new ContactsWithPositiveLabResultsDataDefinition(), "");
        dsd.addColumn("County",new CountyDataDefinition(), "");
        //dsd.addColumn("Status of case Contact tracing",new CountyDataDefinition(), "");
        return dsd;
    }

}

