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

import org.openmrs.module.kenyaui.KenyaUiUtils;
import org.openmrs.module.kenyaui.annotation.PublicPage;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;

import javax.servlet.http.HttpServletRequest;

/**
 * provides a form for self registration
 */
@PublicPage
public class SuccessfulSelfRegistrationPageController {

    public String controller(PageModel model,
                             @SpringBean KenyaUiUtils kenyaUi,
                             HttpServletRequest request) {

        model.addAttribute("message", "Your registration was submitted successfully");
        return null;
    }


}