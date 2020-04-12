/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.kenyaemr.calculation.library;

import org.openmrs.Program;
import org.openmrs.api.context.Context;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.patient.PatientCalculationService;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.calculation.result.SimpleResult;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.CalculationUtils;
import org.openmrs.module.kenyaemr.Metadata;
import org.openmrs.module.reporting.cohort.EvaluatedCohort;
import org.openmrs.module.reporting.cohort.definition.InProgramCohortDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dev on 9/24/16.
 */
public class PatientProgramCalculation extends AbstractPatientCalculation {
    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> map, PatientCalculationContext context) {
        CalculationResultMap ret = new CalculationResultMap();
        InProgramCohortDefinition cd = new InProgramCohortDefinition();
        List<Program> programs = new ArrayList<Program>();
        programs.add(Context.getProgramWorkflowService().getProgramByUuid(Metadata.Program.COVID));
        programs.add(Context.getProgramWorkflowService().getProgramByUuid(Metadata.Program.COVID_QUARANTINE));
        cd.setPrograms(programs);

        EvaluatedCohort patientProgramCohort = CalculationUtils.evaluateWithReporting(cd, cohort, null, context);

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("program", programs.get(0));
        CalculationResultMap covidProgram = new InProgramCalculation().evaluate(cohort, params, Context.getService(PatientCalculationService.class).createCalculationContext());

        params.put("program", programs.get(1));
        CalculationResultMap quarantineProgram = new InProgramCalculation().evaluate(cohort, params, Context.getService(PatientCalculationService.class).createCalculationContext());


        for(Integer ptId: cohort){
            String patientProgram = "";
            if(patientProgramCohort.contains(ptId)) {
                if ((Boolean)covidProgram.get(ptId).getValue()) {
                    patientProgram = "COVID-19 Case Investigation";
                }

                if ((Boolean)quarantineProgram.get(ptId).getValue()) {
                    patientProgram = patientProgram.equalsIgnoreCase("")? "COVID-19 Quarantine Program" : patientProgram+",COVID-19 Quarantine Program";
                }

            }
            ret.put(ptId, new SimpleResult(patientProgram, this));
        }
        return ret;
    }
}
