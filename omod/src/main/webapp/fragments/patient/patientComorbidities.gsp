<%
    /*ui.includeCss("kenyaemr", "referenceapplication.css", 100)*/
%>

<div class="info-body">
                            <% if (comorbidities) { %>
                                <table>
                                    <tr>
                                        <th>&nbsp;</th>
                                        <th>Value</th>
                                    </tr>
                                    <tr>
                                        <td>Pregnant</td>
                                        <td>${comorbidities.pregnant} (${comorbidities.trimester})</td>
                                    </tr>
                                    <tr>
                                        <td>Cardiovascular disease</td>
                                        <td>${comorbidities.cardiovasculardisease}</td>
                                    </tr>
                                    <tr>
                                        <td>Diabetes</td>
                                        <td>${comorbidities.diabetes} </td>
                                    </tr>
                                    <tr>
                                        <td>Immunodeficiency</td>
                                        <td>${comorbidities.immunodeficiency}</td>
                                    </tr>
                                    <tr>
                                        <td>Renal disease</td>
                                        <td>${comorbidities.renalDisease}</td>
                                    </tr>
                                    <tr>
                                        <td>Chronic lung disease</td>
                                        <td>${comorbidities.chronicLungDisease}</td>
                                    </tr>
                                    <tr>
                                        <td>Malignancy</td>
                                        <td>${comorbidities.malignancy}</td>
                                    </tr>


                        </table>

                            <% } else { %>
                                No Comorbidities
                            <% } %>
                            <!-- <a class="view-more">Show more info ></a> //-->
                        </div>
