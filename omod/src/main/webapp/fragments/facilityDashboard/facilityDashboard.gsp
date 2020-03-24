<%
    ui.includeCss("kenyaemr", "referenceapplication.css", 100)
    ui.includeJavascript("kenyaemr", "highcharts.js")
    ui.includeJavascript("kenyaemr", "highcharts-grouped-categories.js")
%>
<style>
.alignLeft {
    text-align: left;
}
</style>
<script>
    jQuery(function () {
        jQuery('#care_and_treatment_chart').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: ''
            },
            subtitle: {
                text: ''
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: 'Number of Patients'
                }

            },
            legend: {
                enabled: false
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                        format: '{point.y:.0f}'
                    }
                }
            },

            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.0f}</b><br/>'
            },

            series: [{
                name: 'Statistics',
                colorByPoint: true,
                data: [{

                    name: 'Total Patients',
                    y:${allPatients},

                }, {
                    name: 'Total enrolled in HIV',
                    y: ${cumulativeEnrolledInHiv},

                }, {
                    name: 'Current in Care',
                    y: ${inCare},

                }, {
                    name: 'Current on ART',
                    y: ${onArt},

                }, {
                    name: 'Newly Enrolled',
                    y: ${newlyEnrolledInHiv},

                }, {
                    name: 'New on ART',
                    y: ${newOnArt},
                }]
            }],
        });
    });

    jQuery(function () {
        jQuery('#viral_load_tracker').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: ''
            },
            subtitle: {
                text: ''
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: 'Number of Patients'
                }

            },
            legend: {
                enabled: false
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                        format: '{point.y:.0f}'
                    }
                }
            },

            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.0f}</b><br/>'
            },

            series: [{
                name: 'Statistics',
                colorByPoint: true,
                data: [{

                    name: 'Total clients with viral loads',
                    y:${vlResults},

                }, {
                    name: 'Total Unsuppressed',
                    y: ${vlResults - suppressedVl},

                }, {
                    name: 'Total Suppressed',
                    y: ${suppressedVl},

                }]
            }],
        });
    });

    jQuery(function () {
        jQuery('#hts_tracker').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: ''
            },
            subtitle: {
                text: ''
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: 'Number of Patients'
                }

            },
            legend: {
                enabled: false
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                        format: '{point.y:.0f}'
                    }
                }
            },

            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.0f}</b><br/>'
            },

            series: [{
                name: 'Statistics',
                colorByPoint: true,
                data: [{

                    name: 'Total Tested',
                    y:${htsTested},

                }, {
                    name: 'Total Positive',
                    y: ${htsPositive},

                }, {
                    name: 'Total Enrolled to Care',
                    y: ${htsLinked},

                }]
            }],
        });
    });

    jQuery(function () {
        jQuery('#hts_contacts_tracker').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: ''
            },
            subtitle: {
                text: ''
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: 'Number of Patient Contacts'
                }

            },
            legend: {
                enabled: false
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                        format: '{point.y:.0f}'
                    }
                }
            },

            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.0f}</b><br/>'
            },

            series: [{
                data: [{y:${htsTestedFamily},color:"#7cb5ec"}, {y:${htsPositiveFamily},color:"#434348"},{y:${htsUnknownStatusFamily},color:"#90ed7d"}, {y:${htsLinkedFamily},color:"#f7a35c"},
                    {y:${htsTestedPartners},color:"#7cb5ec"}, {y:${htsPositivePartner},color:"#434348"},{y:${htsUnknownStatusPartner},color:"#90ed7d"}, {y:${htsLinkedPartner},color:"#f7a35c"},
                    {y:${htsTestedIDU},color:"#7cb5ec"}, {y:${htsPositiveIDU},color:"#434348"},{y:${htsUnknownStatusIDU},color:"#90ed7d"}, {y:${htsLinkedIDU},color:"#f7a35c"}]
            }],
            xAxis: {
                categories: [{
                    name: "Family contacts",
                    categories: ["Total Tested", "HIV+","Unknown status","Linked"]
                }, {
                    name: "Sex partners",
                    categories: ["Total Tested", "HIV+","Unknown status","Linked"]
                }, {
                    name: "Injectable Drug Users",
                    categories: ["Total Tested", "HIV+","Unknown status","Linked"]
                },
                ]
            },
        });
    });


    jQuery(function () {
        jQuery('#differentiated_care_tracker_stable').highcharts({
            plotOptions: {
                series: {
                    colorByPoint: true
                }
            },
            chart: {
                type: 'column'
            },
            title: {
                text: ''
            },
            subtitle: {
                text: ''
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: 'Documented Stable patients'
                }

            },
            legend: {
                enabled: false
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                        format: '{point.y:.0f}'
                    }
                }
            },

            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.0f}</b><br/>'
            },
            series: [{
                data: [{y:${stableUnder4mtcaBelow15},color:"#7cb5ec"}, {y:${stableOver4mtcaBelow15},color:"#434348"},{y:${stableUnder4mtcaOver15F},color:"#90ed7d"}, {y:${stableUnder4mtcaOver15M},color:"#f7a35c"},  {y:${stableOver4mtcaOver15F},color:"#8085e9"},
                    {y:${stableOver4mtcaOver15M},color:"#f15c80"}]
            }],
            xAxis: {
                categories: [{
                    name: "<15 Years",
                    categories: ["Below 4 months prescription", "4+ months prescription"]
                }, {
                    name: "15+ Years",
                    categories: ["Females below 4 months prescription", " Males below 4 months prescription", "Females 4+ months prescription", "Males 4+ months prescription"]
                },
                ]
            }

        });
    });


    jQuery(function () {
        jQuery('#differentiated_care_tracker_unstable').highcharts({
            chart: {
                type: 'column'
            },
            title: {
                text: ''
            },
            subtitle: {
                text: ''
            },
            xAxis: {
                type: 'category'
            },
            yAxis: {
                title: {
                    text: 'Documented Unstable patients'
                }

            },
            legend: {
                enabled: false
            },
            plotOptions: {
                series: {
                    borderWidth: 0,
                    dataLabels: {
                        enabled: true,
                        format: '{point.y:.0f}'
                    }
                }
            },

            tooltip: {
                headerFormat: '<span style="font-size:11px">{series.name}</span><br>',
                pointFormat: '<span style="color:{point.color}">{point.name}</span>: <b>{point.y:.0f}</b><br/>'
            },



            series: [{
            data: [{y:${unstableUnder15},color:"#7cb5ec"}, {y:${unstableFemales15Plus},color:"#434348"},{y:${unstableMales15Plus},color:"#90ed7d"}]
        }],
            xAxis: {
            categories: [{
                name: "<15 Years",
                categories: ["Males and Females"]

            }, {
                name: "15+ Years",
                categories: ["Females", " Males"]
            },
            ]
            }
        });
    });

</script>

<div class="ke-page-content">
    <div style="font-size: 18px; color: #006056; font-style: normal; font-weight: bold">Dashboard</div>

    <div id="program-tabs" class="ke-tabs">
        <div class="ke-tabmenu">
            <div class="ke-tabmenu-item" data-tabid="care_and_treatment">Confirmed Cases</div>

            <div class="ke-tabmenu-item" data-tabid="hts">Cases in Lab</div>

            <div class="ke-tabmenu-item" data-tabid="appointments">Contact Tracing</div>

        </div>

        <div class="ke-tab" data-tabid="care_and_treatment">
            <table cellspacing="0" cellpadding="0" width="100%">
                <tr>
                    <td style="width: 50%; vertical-align: top">
                        <div class="ke-panel-frame">
                            <div class="ke-panel-heading">Summary of Care and Treatment Statistics</div>

                            <div class="ke-panel-content">
                                <table class="alignLeft">
                                    <tr>
                                        <td colspan="3"
                                            class="heading2"><strong>Reporting Period: ${reportPeriod}</strong>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>Total Patients</th>
                                        <th>Total enrolled in HIV</th>
                                        <th>Current in Care</th>
                                        <th>Current on ART</th>
                                        <th>Newly Enrolled</th>
                                        <th>New on ART</th>
                                    </tr>
                                    <tr>
                                        <td>${allPatients}</td>
                                        <td>${cumulativeEnrolledInHiv}</td>
                                        <td>${inCare}</td>
                                        <td>${onArt}</td>
                                        <td>${newlyEnrolledInHiv}</td>
                                        <td>${newOnArt}</td>
                                    </tr>
                                </table>
                            </div>
                        </div>

                        <div id="care_and_treatment_chart" style="min-width: 450px; height: 300px; margin: 0 auto"></div>
                    </td>
                    <td style="width: 50%; vertical-align: top; padding-left: 5px">
                        <div class="ke-panel-frame">
                            <div class="ke-panel-heading">Viral Load Tracker</div>

                            <div class="ke-panel-content">
                                <table class="alignLeft">
                                    <tr>
                                        <td colspan="3" class="heading2"><strong>Reporting Period: Today</strong></td>
                                    </tr>
                                    <tr>
                                        <th>Total clients with viral loads <br/>(in last 12 months)</th>
                                        <th>Total Unsuppressed</th>
                                        <th>Total Suppressed</th>
                                    </tr>
                                    <tr>
                                        <td>${vlResults}</td>
                                        <td>${vlResults - suppressedVl}</td>
                                        <td>${suppressedVl}</td>
                                    </tr>
                                </table>
                            </div>
                        </div>

                        <div id="viral_load_tracker" style="min-width: 450px; height: 300px; margin: 0 auto"></div>
                    </td>
                </tr>
            </table>
        </div>
        <div class="ke-tab" data-tabid="hts">
            <table cellspacing="0" cellpadding="0" width="100%">
                <tr>
                    <td style="width: 50%; vertical-align: top">
                        <div class="ke-panel-frame">
                            <div class="ke-panel-heading">HTS</div>

                            <div class="ke-panel-content">
                                <table class="alignLeft">
                                    <tr>
                                        <td colspan="3" class="heading2"><strong>Reporting Period: Today</strong></td>
                                    </tr>
                                    <tr>
                                        <th>&nbsp;</th>
                                        <th>Total Tested</th>
                                        <th>Total Positive</th>
                                        <th>Total Enrolled</th>
                                    </tr>
                                    <tr>
                                        <td><b>Total Clients</b></td>
                                        <td>${htsTested}</td>
                                        <td>${htsPositive}</td>
                                        <td>${htsLinked}</td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <div id="hts_tracker" style="min-width: 450px; height: 300px; margin: 0 auto"></div>
                    </td>
                    <td style="width: 50%; vertical-align: top; padding-left: 5px">
                        <div class="ke-panel-frame">
                            <div class="ke-panel-heading">Contact Testing</div>

                            <div class="ke-panel-content">
                                <table class="alignLeft">
                                    <tr>
                                        <td colspan="3" class="heading2"><strong>Reporting Period: Today</strong></td>
                                    </tr>
                                    <tr>
                                        <th>&nbsp;</th>
                                        <th>Total Tested</th>
                                        <th>Total Positive</th>
                                        <th>Total Unknown</th>
                                        <th>Total Enrolled</th>
                                    </tr>

                                    <tr>
                                        <td><b>Family Member(s)</b></td>
                                        <td>${htsTestedFamily}</td>
                                        <td>${htsPositiveFamily}</td>
                                        <td>${htsUnknownStatusFamily}</td>
                                        <td>${htsLinkedFamily}</td>
                                    </tr>
                                    <tr>
                                        <td><b>Sex Partner(s)</b></td>
                                        <td>${htsTestedPartners}</td>
                                        <td>${htsPositivePartner}</td>
                                        <td>${htsUnknownStatusPartner}</td>
                                        <td>${htsLinkedPartner}</td>
                                    </tr>
                                    <tr>
                                        <td><b>Injectable Drug User(s)</b></td>
                                        <td>${htsTestedIDU}</td>
                                        <td>${htsPositiveIDU}</td>
                                        <td>${htsUnknownStatusIDU}</td>
                                        <td>${htsLinkedIDU}</td>
                                    </tr>
                                </table>
                            </div>
                        </div>
                        <div id="hts_contacts_tracker" style="min-width: 700px; height: 350px; margin: 0 auto"></div>
                    </td>
                </tr>
            </table>
        </div>
        <div class="ke-tab" data-tabid="appointments">
            <table cellspacing="0" cellpadding="0" width="100%">
                <tr>
                    <td style="width: 50%; vertical-align: top">
                        <div class="ke-panel-frame">
                            <div class="ke-panel-heading">Today's Workload</div>

                            <div class="ke-panel-content">
                                <table class="alignLeft">
                                    <tr>
                                        <td colspan="3"
                                            class="heading2"><strong>Date: Today</strong>
                                        </td>
                                    </tr>
                                    <tr>
                                        <th>Number Scheduled </th>
                                        <th>Number Checked In </th>
                                        <th>Number Seen</th>
                                        <th>Unscheduled Visits</th>

                                    </tr>
                                    <tr>
                                        <td>${patientsScheduled}</td>
                                        <td>${checkedIn}</td>
                                        <td>${patientsSeen}</td>
                                        <td>${unscheduled}</td>
                                    </tr>
                                </table>
                            </div>
                        </div>

                    </td>

                </tr>
            </table>
        </div>
        <br/>
       <br/>
    </div>
   </div>
