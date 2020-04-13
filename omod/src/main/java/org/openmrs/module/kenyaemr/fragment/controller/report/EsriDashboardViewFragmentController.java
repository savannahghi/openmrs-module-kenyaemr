/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.kenyaemr.fragment.controller.report;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import org.openmrs.GlobalProperty;
import org.openmrs.Location;
import org.openmrs.api.AdministrationService;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.kenyacore.report.ReportDescriptor;
import org.openmrs.module.kenyacore.report.ReportManager;
import org.openmrs.module.kenyaemr.util.EmrUtils;
import org.openmrs.module.kenyaemr.wrapper.Facility;
import org.openmrs.module.kenyaui.KenyaUiUtils;
import org.openmrs.module.reporting.dataset.DataSet;
import org.openmrs.module.reporting.dataset.DataSetColumn;
import org.openmrs.module.reporting.dataset.DataSetRow;
import org.openmrs.module.reporting.report.ReportData;
import org.openmrs.module.reporting.report.ReportRequest;
import org.openmrs.module.reporting.report.definition.ReportDefinition;
import org.openmrs.module.reporting.report.service.ReportService;
import org.openmrs.ui.framework.SimpleObject;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.openmrs.ui.framework.page.PageRequest;
import org.openmrs.util.OpenmrsUtil;
import org.openmrs.util.PrivilegeConstants;
import org.springframework.web.bind.annotation.RequestParam;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Page for generating ESRIE dashbaord indicators
 */
public class EsriDashboardViewFragmentController {

    private AdministrationService administrationService;
    protected final Log log = LogFactory.getLog(getClass());

    DateFormat isoDateTimeFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZ");
    DateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    public void get(@RequestParam("request") ReportRequest reportRequest,
                    @RequestParam("returnUrl") String returnUrl,
                    PageRequest pageRequest,
                    PageModel model,
                    @SpringBean ReportManager reportManager,
                    @SpringBean KenyaUiUtils kenyaUi,
                    @SpringBean ReportService reportService) throws Exception {

        ReportDefinition definition = reportRequest.getReportDefinition().getParameterizable();
        ReportDescriptor report = reportManager.getReportDescriptor(definition);
        administrationService = Context.getAdministrationService();

        //CoreUtils.checkAccess(report, kenyaUi.getCurrentApp(pageRequest));

        ReportData reportData = reportService.loadReportData(reportRequest);
       /* ByteArrayOutputStream outputStream = (ByteArrayOutputStream) buildXmlDocument(reportData);
        postAdxToIL(outputStream);*/

        Date reportStartDate = (Date) reportData.getContext().getParameterValue("startDate");
        Date reportEndDate = (Date) reportData.getContext().getParameterValue("endDate");

        String serverAddress = administrationService.getGlobalProperty("ilServer.address");
        model.addAttribute("endDate", isoDateFormat.format(reportEndDate));
        model.addAttribute("startDate", isoDateFormat.format(reportStartDate));
        model.addAttribute("reportRequest", reportRequest);
        model.addAttribute("adx", render(reportData));
        model.addAttribute("reportName", definition.getName());
        model.addAttribute("returnUrl", returnUrl);
    }

    public String render(ReportData reportData) throws IOException {


        Date reportDate = (Date) reportData.getContext().getParameterValue("startDate");
        Date endDate = (Date) reportData.getContext().getParameterValue("endDate");

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode payloadNode = mapper.createObjectNode();
        ArrayNode counties = mapper.createArrayNode();
        for (String dsKey : reportData.getDataSets().keySet()) {

            DataSet dataset = reportData.getDataSets().get(dsKey);
            List<DataSetColumn> columns = dataset.getMetaData().getColumns();
            for (DataSetRow row : dataset) {
                for (DataSetColumn column : columns) {
                    String name = column.getName();
                    String label = column.getLabel();
                    Object value = row.getColumnValue(column);
                    payloadNode.put(formatIndicatorName(name), Integer.parseInt(value.toString()));
                }
            }
        }

        String qry = "select e.patient_id, county, recently_travelled\n" +
                "from kenyaemr_etl.etl_covid_19_enrolment e\n" +
                "inner join kenyaemr_etl.etl_laboratory_extract l on l.patient_id=e.patient_id and l.result = 703\n" +
                "where e.voided=0;";

        Map<String, ObjectNode> countyList = new HashMap<String, ObjectNode>();
        Context.addProxyPrivilege(PrivilegeConstants.SQL_LEVEL_ACCESS);

        List<List<Object>> activeList = Context.getAdministrationService().executeSQL(qry, true);
        if (!activeList.isEmpty()) {
            for (List<Object> res : activeList) {
                Integer patientId = (Integer) res.get(0);
                String county = (String) res.get(1);
                String traveled = (String) res.get(2);

                if (StringUtils.isBlank(county)) {
                    continue;
                }

                if (countyList.containsKey(county)) {
                    ObjectNode details = countyList.get(county);
                    details.put("confirmed", details.get("confirmed").getIntValue() + 1);
                    if (StringUtils.isNotBlank(traveled) && traveled.equals("Yes")) {
                        details.put("imported", details.get("imported").getIntValue() + 1);
                    } else {
                        details.put("local", details.get("local").getIntValue() + 1);
                    }
                } else {
                    ObjectNode countyDetails = mapper.createObjectNode();
                    countyDetails.put("confirmed", 1);
                    if (StringUtils.isNotBlank(traveled) && traveled.equals("Yes")) {
                        countyDetails.put("imported", 1);
                        countyDetails.put("local", 0);
                    } else {
                        countyDetails.put("imported", 0);
                        countyDetails.put("local", 1);
                    }
                    countyList.put(county, countyDetails);
                }
            }
        }

        if (!countyList.keySet().isEmpty()) {
            for (Map.Entry<String, ObjectNode> entry : countyList.entrySet()) {
                String countyName = entry.getKey();
                ObjectNode stats = entry.getValue();
                ObjectNode countyObj = mapper.createObjectNode();
                countyObj.put("name", countyName);
                countyObj.put("confirmed", stats.get("confirmed").getIntValue());
                countyObj.put("imported", stats.get("imported").getIntValue());
                countyObj.put("local", stats.get("local").getIntValue());
                counties.add(countyObj);
            }
        }
        payloadNode.put("county", counties);
        Object json = mapper.readValue(payloadNode, Object.class);
        String indented = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        Context.removeProxyPrivilege(PrivilegeConstants.SQL_LEVEL_ACCESS);

        return indented;
    }

    private String formatIndicatorName(String name) {

        String arrName[] = name.split("-"); // should come in the form of Newly Diagnosed-01
        String indName = arrName[0];
        String indDisaggregation = arrName[1];
        String resultingString = "";

        if (indDisaggregation.equals("01")) {
            resultingString = indName.trim().replace(" ", "_").concat("_male_0_to_9");
        } else if (indDisaggregation.equals("02")) {
            resultingString = indName.trim().replace(" ", "_").concat("_female_0_to_9");
        } else if (indDisaggregation.equals("03")) {
            resultingString = indName.trim().replace(" ", "_").concat("_male_10_to_19");
        } else if (indDisaggregation.equals("04")) {
            resultingString = indName.trim().replace(" ", "_").concat("_female_10_to_19");
        } else if (indDisaggregation.equals("05")) {
            resultingString = indName.trim().replace(" ", "_").concat("_male_20_to_29");
        } else if (indDisaggregation.equals("06")) {
            resultingString = indName.trim().replace(" ", "_").concat("_female_20_to_29");
        } else if (indDisaggregation.equals("07")) {
            resultingString = indName.trim().replace(" ", "_").concat("_male_30_to_39");
        } else if (indDisaggregation.equals("08")) {
            resultingString = indName.trim().replace(" ", "_").concat("_female_30_to_39");
        } else if (indDisaggregation.equals("09")) {
            resultingString = indName.trim().replace(" ", "_").concat("_male_40_to_49");
        } else if (indDisaggregation.equals("10")) {
            resultingString = indName.trim().replace(" ", "_").concat("_female_40_to_49");
        } else if (indDisaggregation.equals("11")) {
            resultingString = indName.trim().replace(" ", "_").concat("_male_50_to_59");
        } else if (indDisaggregation.equals("12")) {
            resultingString = indName.trim().replace(" ", "_").concat("_female_50_to_59");
        } else if (indDisaggregation.equals("13")) {
            resultingString = indName.trim().replace(" ", "_").concat("_male_60_to_69");
        } else if (indDisaggregation.equals("14")) {
            resultingString = indName.trim().replace(" ", "_").concat("_female_60_to_69");
        } else if (indDisaggregation.equals("15")) {
            resultingString = indName.trim().replace(" ", "_").concat("_male_70+");
        } else if (indDisaggregation.equals("16")) {
            resultingString = indName.trim().replace(" ", "_").concat("_female_70+");
        } else {
            resultingString = indName.trim().replace(" ", "_").concat("_total");
        }
        return resultingString;
    }


    public void saveDashboardPayload(@RequestParam("request") ReportRequest reportRequest,
                                     @RequestParam("returnUrl") String returnUrl,
                                     @SpringBean ReportService reportService) throws IOException {

        ReportData reportData = reportService.loadReportData(reportRequest);
        File appDir = new File(OpenmrsUtil.getApplicationDataDirectory());
        File dashboardPayloadFile = new File(appDir.getPath() + File.separator + "covidDashboardPayload.json");

        Context.addProxyPrivilege(PrivilegeConstants.SQL_LEVEL_ACCESS);
        Date reportDate = (Date) reportData.getContext().getParameterValue("startDate");
        Date endDate = (Date) reportData.getContext().getParameterValue("endDate");

        ObjectMapper mapper = new ObjectMapper();

        ObjectNode payloadNode = mapper.createObjectNode();
        ArrayNode counties = mapper.createArrayNode();
        for (String dsKey : reportData.getDataSets().keySet()) {

            DataSet dataset = reportData.getDataSets().get(dsKey);
            List<DataSetColumn> columns = dataset.getMetaData().getColumns();
            for (DataSetRow row : dataset) {
                for (DataSetColumn column : columns) {
                    String name = column.getName();
                    String label = column.getLabel();
                    Object value = row.getColumnValue(column);
                    payloadNode.put(formatIndicatorName(name), Integer.parseInt(value.toString()));
                }
            }
        }

        String qry = "select e.patient_id, county, recently_travelled\n" +
                "from kenyaemr_etl.etl_covid_19_enrolment e\n" +
                "inner join kenyaemr_etl.etl_laboratory_extract l on l.patient_id=e.patient_id and l.result = 703\n" +
                "where e.voided=0;";

        Map<String, ObjectNode> countyList = new HashMap<String, ObjectNode>();
        List<List<Object>> activeList = Context.getAdministrationService().executeSQL(qry, true);
        if (!activeList.isEmpty()) {
            for (List<Object> res : activeList) {
                Integer patientId = (Integer) res.get(0);
                String county = (String) res.get(1);
                String traveled = (String) res.get(2);

                if (StringUtils.isBlank(county)) {
                    continue;
                }

                if (countyList.containsKey(county)) {
                    ObjectNode details = countyList.get(county);
                    details.put("confirmed", details.get("confirmed").getIntValue() + 1);
                    if (traveled.equals("Yes")) {
                        details.put("imported", details.get("imported").getIntValue() + 1);
                    } else {
                        details.put("local", details.get("local").getIntValue() + 1);
                    }
                } else {
                    ObjectNode countyDetails = mapper.createObjectNode();
                    countyDetails.put("confirmed", 1);
                    if (StringUtils.isNotBlank(traveled) && traveled.equals("Yes")) {
                        countyDetails.put("imported", 1);
                        countyDetails.put("local", 0);
                    } else {
                        countyDetails.put("imported", 0);
                        countyDetails.put("local", 1);
                    }
                    countyList.put(county, countyDetails);
                }
            }
        }

        if (!countyList.keySet().isEmpty()) {
            for (Map.Entry<String, ObjectNode> entry : countyList.entrySet()) {
                String countyName = entry.getKey();
                ObjectNode stats = entry.getValue();
                ObjectNode countyObj = mapper.createObjectNode();
                countyObj.put("name", countyName);
                countyObj.put("confirmed", stats.get("confirmed").getIntValue());
                countyObj.put("imported", stats.get("imported").getIntValue());
                countyObj.put("local", stats.get("local").getIntValue());
                counties.add(countyObj);
            }
        }
        payloadNode.put("county", counties);

        try {
            FileWriter writer = new FileWriter(dashboardPayloadFile);
            writer.write(payloadNode.toString());
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Context.removeProxyPrivilege(PrivilegeConstants.SQL_LEVEL_ACCESS);


    }


}