/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.kenyaemr.page.controller;

import org.apache.commons.lang.StringUtils;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.PersonName;
import org.openmrs.User;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.ConceptService;
import org.openmrs.api.EncounterService;
import org.openmrs.api.ObsService;
import org.openmrs.api.PatientService;
import org.openmrs.api.PersonService;
import org.openmrs.api.context.Context;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.module.kenyaemr.EmrConstants;
import org.openmrs.module.kenyaemr.EmrWebConstants;
import org.openmrs.module.kenyaemr.IPAccessSecurity;
import org.openmrs.module.kenyaemr.api.KenyaEmrService;
import org.openmrs.module.kenyaemr.metadata.CommonMetadata;
import org.openmrs.module.kenyaui.KenyaUiUtils;
import org.openmrs.module.kenyaui.annotation.PublicPage;
import org.openmrs.module.metadatadeploy.MetadataUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.util.PrivilegeConstants;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * provides a form for self registration
 */
@PublicPage
public class SelfRegistrationPageController {

    PersonService personService = Context.getPersonService();
    PatientService patientService = Context.getPatientService();
    ObsService obsService = Context.getObsService();
	ConceptService conceptService = Context.getConceptService();


	public String controller(PageModel model,
						   @RequestParam(value = "firstName", required = false) String firstName,
						   @RequestParam(value = "middleName", required = false) String middleName,
						   @RequestParam(value = "lastName", required = false) String lastName,
						   @RequestParam(value = "countryOfResidence", required = false) String countryOfResidence,
						   @RequestParam(value = "sex", required = false) String sex,
						   @RequestParam(value = "birthDate", required = false) String birthDate,
						   @RequestParam(value = "physicalAddress", required = false) String physicalAddress,
						   @RequestParam(value = "phoneContact", required = false) String phoneContact,
						   @RequestParam(value = "personalEmail", required = false) String personalEmail,
						   @RequestParam(value = "arrivalDate", required = false) String arrivalDate,
						   @RequestParam(value = "vesel", required = false) String vesel,
						   @RequestParam(value = "veselNumber", required = false) String veselNumber,
						   @RequestParam(value = "seatNumber", required = false) String seatNumber,
						   @RequestParam(value = "destination", required = false) String destination,
						   @RequestParam(value = "contactPerson", required = false) String contactPerson,
						   @RequestParam(value = "contactPersonTelephone", required = false) String contactPersonTelephone,
						   @RequestParam(value = "villageHouseHotel", required = false) String villageHouseHotel,
						   @RequestParam(value = "subLocationEstate", required = false) String subLocationEstate,
						   @RequestParam(value = "county", required = false) String county,
						   @RequestParam(value = "postalAddress", required = false) String postalAddress,
						   @RequestParam(value = "alternativePhoneNumber", required = false) String alternativePhoneNumber,
						   @RequestParam(value = "emailAddress", required = false) String emailAddress,
						   @RequestParam(value = "temperature", required = false) String temperature,
						   @RequestParam(value = "cough", required = false) String cough,
						   @RequestParam(value = "difficultyBreathing", required = false) String difficultyBreathing,
						   @RequestParam(value = "uniqueIdentifier", required = false) String uniqueIdentifier,
						   @SpringBean KenyaUiUtils kenyaUi,
						   HttpServletRequest request) {

		model.addAttribute("firstName", firstName);

		if (!StringUtils.isEmpty(firstName)) {
			return handleSubmission(
					model,
					firstName,
					middleName,
					lastName,
					countryOfResidence,
					sex,
					birthDate,
					physicalAddress,
					phoneContact,
					personalEmail,
					arrivalDate,
					vesel,
					veselNumber,
					seatNumber,
					destination,
					contactPerson,
					contactPersonTelephone,
					villageHouseHotel,
					subLocationEstate,
					county,
					postalAddress,
					alternativePhoneNumber,
					emailAddress,
					temperature,
					cough,
					difficultyBreathing,
					uniqueIdentifier,
					request, kenyaUi);
}

		return null;
				}

	private String handleSubmission(
			PageModel model,
			String firstName,
			String middleName,
			String lastName,
			String countryOfResidence,
			String sex,
			String birthDate,
			String physicalAddress,
			String phoneContact,
			String personalEmail,
			String arrivalDate,
			String vesel,
			String veselNumber,
			String seatNumber,
			String destination,
			String contactPerson,
			String contactPersonTelephone,
			String villageHouseHotel,
			String subLocationEstate,
			String county,
			String postalAddress,
			String alternativePhoneNumber,
			String emailAddress,
			String temperature,
			String cough,
			String difficultyBreathing,
			String uniqueIdentifier,
			HttpServletRequest request, KenyaUiUtils kenyaUi) {

		Context.addProxyPrivilege(PrivilegeConstants.GET_IDENTIFIER_TYPES);
		Context.addProxyPrivilege(PrivilegeConstants.ADD_PATIENTS);
		Context.addProxyPrivilege(PrivilegeConstants.EDIT_PATIENTS);
		Context.addProxyPrivilege(PrivilegeConstants.ADD_PERSONS);
		Context.addProxyPrivilege(PrivilegeConstants.EDIT_PERSONS);
		Context.addProxyPrivilege(PrivilegeConstants.ADD_PATIENT_IDENTIFIERS);
		Context.addProxyPrivilege(PrivilegeConstants.EDIT_PATIENT_IDENTIFIERS);
        try {

			Patient patient;

			AdministrationService administrationService;
			EncounterService encounterService;
			HttpSession httpSession = request.getSession();

			patient = createPatient(firstName, middleName, lastName, birthDate, "ESTIMATED", sex);

			PatientIdentifierType upnType = MetadataUtils.existing(PatientIdentifierType.class, CommonMetadata._PatientIdentifierType.NATIONAL_ID);

			PatientIdentifier upn = new PatientIdentifier();
			upn.setIdentifierType(upnType);
			upn.setIdentifier(uniqueIdentifier);
			upn.setPreferred(true);
			upn.setLocation(Context.getService(KenyaEmrService.class).getDefaultLocation());
			patient.addIdentifier(upn);
			// Make sure everyone gets an OpenMRS ID
			/*PatientIdentifierType openmrsIdType = MetadataUtils.existing(PatientIdentifierType.class, CommonMetadata._PatientIdentifierType.OPENMRS_ID);
			PatientIdentifier openmrsId = patient.getPatientIdentifier(openmrsIdType);

			if (openmrsId == null) {
				String generated = Context.getService(IdentifierSourceService.class).generateIdentifier(openmrsIdType, "Registration");
				openmrsId = new PatientIdentifier(generated, openmrsIdType, Context.getService(KenyaEmrService.class).getDefaultLocation());
				patient.addIdentifier(openmrsId);

				if (!patient.getPatientIdentifier().isPreferred()) {
					openmrsId.setPreferred(true);

				}
			}*/

			patientService.savePatient(patient);

		Context.removeProxyPrivilege(PrivilegeConstants.GET_IDENTIFIER_TYPES);
		Context.removeProxyPrivilege(PrivilegeConstants.ADD_PATIENTS);
		Context.removeProxyPrivilege(PrivilegeConstants.ADD_PERSONS);
		Context.removeProxyPrivilege(PrivilegeConstants.ADD_PATIENT_IDENTIFIERS);
			System.out.println("FirstName: " + firstName);
			System.out.println("LastName: " + lastName);
			System.out.println("DOB: " + birthDate);
			System.out.println("Arrival Date: " + arrivalDate);
			System.out.println("Fever: " + temperature);
		} catch (Exception e) {
        	e.printStackTrace();
		} finally {
			Context.closeSession();
		}
		// User already has a reset password... go change it...
		/*if (httpSession.getAttribute(EmrWebConstants.SESSION_ATTR_RESET_PASSWORD) != null) {
			return "redirect:/" + EmrConstants.MODULE_ID + "/successfulSelfRegistration.page";
		}

		String ipAddress = request.getRemoteAddr();

		if (IPAccessSecurity.isLockedOut(ipAddress)) {
			kenyaUi.notifyError(httpSession, "auth.forgotPassword.tooManyAttempts");
		} else {
			if (StringUtils.isEmpty(secretAnswer)) {
				// if they are seeing this page for the first time

				User user = null;

				try {
					Context.addProxyPrivilege(PrivilegeConstants.VIEW_USERS);

					// Only search if they actually put in a username
					if (!StringUtils.isEmpty(username)) {
						user = Context.getUserService().getUserByUsername(username);
					}
				}
				finally {
					Context.removeProxyPrivilege(PrivilegeConstants.VIEW_USERS);
				}

				if (user == null) {
					// Client might be trying to guess a username
					IPAccessSecurity.registerFailedAccess(ipAddress);
					kenyaUi.notifyError(httpSession, "auth.question.empty");
				} else if (StringUtils.isEmpty(user.getSecretQuestion())) {
					kenyaUi.notifyError(httpSession, "auth.question.empty");
				} else {
					model.addAttribute("secretQuestion", user.getSecretQuestion());
				}

			} else {
				// if they've filled in the username and entered their secret answer

				User user = null;

				try {
					Context.addProxyPrivilege(PrivilegeConstants.VIEW_USERS);
					user = Context.getUserService().getUserByUsername(username);
				}
				finally {
					Context.removeProxyPrivilege(PrivilegeConstants.VIEW_USERS);
				}

				// Check the secret question again in case the user got here "illegally"
				if (user == null || StringUtils.isEmpty(user.getSecretQuestion())) {
					kenyaUi.notifyError(httpSession, "auth.question.empty");
				} else if (user.getSecretQuestion() != null && Context.getUserService().isSecretAnswer(user, secretAnswer)) {

					String randomPassword = randomPassword();

					try {
						Context.addProxyPrivilege(PrivilegeConstants.EDIT_USER_PASSWORDS);
						Context.getUserService().changePassword(user, randomPassword);
						Context.refreshAuthenticatedUser();
					}
					finally {
						Context.removeProxyPrivilege(PrivilegeConstants.EDIT_USER_PASSWORDS);
					}

					IPAccessSecurity.registerSuccessfulAccess(ipAddress);
					httpSession.setAttribute(EmrWebConstants.SESSION_ATTR_RESET_PASSWORD, randomPassword);
					kenyaUi.notifySuccess(httpSession, "auth.password.reset");
					Context.authenticate(username, randomPassword);
					httpSession.setAttribute("loginAttempts", 0);
					return "redirect:/" + EmrConstants.MODULE_ID + "/profile.page";
				} else {
					kenyaUi.notifyError(httpSession, "auth.answer.invalid");
					model.addAttribute("secretQuestion", user.getSecretQuestion());
					IPAccessSecurity.registerFailedAccess(ipAddress);
				}
			}
		}*/

		//return null;
		return "redirect:/" + EmrConstants.MODULE_ID + "/successfulSelfRegistration.page";

	}


    private Patient createPatient(String fName, String mName, String lName, String dobString, String dobPrecision, String gender) {

        Date dob = null;
        try {
            dob = new SimpleDateFormat("yyyyMMdd").parse(dobString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Patient patient = new Patient();
        patient = new Patient();
        patient.setGender(gender);
        patient.addName(new PersonName(fName, mName, lName));
        if (dob != null) {
            patient.setBirthdate(dob);
        }

        if (dobPrecision != null && dobPrecision.equals("ESTIMATED")) {
            patient.setBirthdateEstimated(true);
        } else if (dobPrecision != null && dobPrecision.equals("EXACT")) {
            patient.setBirthdateEstimated(false);
        }
        return patient;

    }

    protected List<Obs> saveRegistrationObs () {
		return null;
	}
    /**
     * Handles saving a field which is stored as an obs
     * @param patient the patient being saved
     * @param obsToSave
     * @param question
     * @param newValue
     */
    protected void addTextObs(Patient patient, List<Obs> obsToSave, Concept question, String newValue) {

            if (newValue != null) {
                Obs o = new Obs();
                o.setPerson(patient);
                o.setConcept(question);
                o.setObsDatetime(new Date());
                o.setLocation(Context.getService(KenyaEmrService.class).getDefaultLocation());
                o.setValueText(newValue);
                obsToSave.add(o);
            }
    }

    /**
     * Handles saving a field which is stored as an obs whose value is boolean
     * @param patient the patient being saved
     * @param obsToSave
     * @param obsToVoid
     * @param question
     * @param savedObs
     * @param newValue
     */
    protected void handleOncePerPatientObs(Patient patient, List<Obs> obsToSave, List<Obs> obsToVoid, Concept question,
                                           Obs savedObs, Boolean newValue) {
        if (!OpenmrsUtil.nullSafeEquals(savedObs != null ? savedObs.getValueBoolean() : null, newValue)) {
            // there was a change
            if (savedObs != null && newValue == null) {
                // treat going from a value to null as voiding all past civil status obs
                obsToVoid.addAll(Context.getObsService().getObservationsByPersonAndConcept(patient, question));
            }
            if (newValue != null) {
                Obs o = new Obs();
                o.setPerson(patient);
                o.setConcept(question);
                o.setObsDatetime(new Date());
                o.setLocation(Context.getService(KenyaEmrService.class).getDefaultLocation());
                o.setValueBoolean(newValue);
                obsToSave.add(o);
            }
        }
    }

	private void setEncounterObs(Encounter enc, Patient patient, String residentCountry, String arrivalDate, String veselNo, String seatNo) {

    	/*
    	* country of residence = 165198
    	* date of arrival = 160753
    	* mode of transport = 1375, road: 1787, flight: 1378
    	* vesel = 162612
    	* vesel number = 162612
    	* seat number = 162086
    	* destination in Kenya = 162725
    	*
    	* */
		Integer countryOfResidence = 165198;
		Integer dateOfArrival = 160753;
		Integer modeOfTransport = 1375;
		Integer veselNumber = 162612;
		Integer seatNumber = 162086;
		Integer healthProviderIdentifierConcept = 163161;

		// country of residence
		Obs o = new Obs();
		o.setConcept(conceptService.getConcept(countryOfResidence));
		o.setDateCreated(new Date());
		o.setCreator(Context.getUserService().getUser(1));
		o.setLocation(enc.getLocation());
		o.setObsDatetime(enc.getEncounterDatetime());
		o.setPerson(patient);
		o.setValueText(residentCountry);

		// date of arrival
		Obs o1 = new Obs();
		o1.setConcept(conceptService.getConcept(dateOfArrival));
		o1.setDateCreated(new Date());
		o1.setCreator(Context.getUserService().getUser(1));
		o1.setLocation(enc.getLocation());
		o1.setObsDatetime(enc.getEncounterDatetime());
		o1.setPerson(patient);
		o1.setValueDate(new Date());

		// test strategy
		Obs o2 = new Obs();
		o2.setConcept(conceptService.getConcept(veselNumber));
		o2.setDateCreated(new Date());
		o2.setCreator(Context.getUserService().getUser(1));
		o2.setLocation(enc.getLocation());
		o2.setObsDatetime(enc.getEncounterDatetime());
		o2.setPerson(patient);
		o2.setValueText(veselNo);

		// seat number

		Obs o3 = new Obs();
		o3.setConcept(conceptService.getConcept(seatNumber));
		o3.setDateCreated(new Date());
		o3.setCreator(Context.getUserService().getUser(1));
		o3.setLocation(enc.getLocation());
		o3.setObsDatetime(enc.getEncounterDatetime());
		o3.setPerson(patient);
		o3.setValueText(seatNo);




		enc.addObs(o);
		enc.addObs(o1);
		enc.addObs(o2);
		enc.addObs(o3);
		//encounterService.saveEncounter(enc);
	}

}