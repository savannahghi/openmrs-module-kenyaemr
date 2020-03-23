/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.kenyaemr.calculation.library.hiv.art;

import org.openmrs.Obs;
import org.openmrs.calculation.patient.PatientCalculationContext;
import org.openmrs.calculation.result.CalculationResultMap;
import org.openmrs.module.kenyacore.calculation.AbstractPatientCalculation;
import org.openmrs.module.kenyacore.calculation.BooleanResult;
import org.openmrs.module.kenyacore.calculation.PatientFlagCalculation;
import org.openmrs.module.kenyaemr.calculation.EmrCalculationUtils;

import java.util.Collection;
import java.util.Map;

/**
 * Created by codehub on 12/3/15.
 */
public class PatientsWithPositiveCovid19TestCalculation extends AbstractPatientCalculation implements PatientFlagCalculation {

    @Override
    public CalculationResultMap evaluate(Collection<Integer> cohort, Map<String, Object> parameterValues, PatientCalculationContext context) {
        CalculationResultMap ret = new CalculationResultMap();

        CalculationResultMap covidResults = calculate(new LastCovidTestCalculation(), cohort, context);
        boolean positive = false;
        for(Integer ptId:cohort){
            Obs covidObs = EmrCalculationUtils.obsResultForPatient(covidResults, ptId);
            if(covidObs != null && covidObs.getValueCoded() != null && covidObs.getValueCoded().getConceptId().equals(703)){
                positive = true;
            }
            ret.put(ptId, new BooleanResult(positive, this));
        }
        return ret;
    }

    @Override
    public String getFlagMessage() {
        return "Positive Covid-19 Test";
    }
}
