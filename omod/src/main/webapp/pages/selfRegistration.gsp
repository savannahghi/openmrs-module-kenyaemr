<%
    ui.decorateWith("kenyaemr", "standardPage")
%>
<script>
    var countryNames = "Afghanistan, Äland Islands, Albania, Algeria, American Samoa, Andorra, Angola, Anguilla, Antarctica, Antigua and Barbuda, Argentina, Armenia, Aruba, Australia, Austria, Azerbaijan, Bahamas, Bahrain, Bangladesh, Barbados, Belarus, Belgium, Belize, Benin, Bermuda, Bhutan, Bolivia, Sint Eustatius and Saba Bonaire, Bosnia and Herzegovina, Botswana, Bouvet Island, Brazil, British Indian Ocean Territory, Brunei Darussalam, Bulgaria, Burkina Faso, Burundi, Cabo Verde, Cambodia, Cameroon, Canada, Cayman Islands, Central African Republic, Chad, Chile, China, Christmas Island, Cocos (Keeling) Islands, Colombia, Comoros, Congo, Congo, Democratic Republic of the, Cook Islands, Costa Rica, C√¥te d'Ivoire, Croatia, Cuba, Cura√ßao, Cyprus, Czechia, Denmark, Djibouti, Dominica, Dominican Republic, Ecuador, Egypt, El Salvador, Equatorial Guinea, Eritrea, Estonia, Eswatini, Ethiopia, Falkland Islands (Malvinas), Faroe Islands, Fiji, Finland, France, French Guiana, French Polynesia, French Southern Territories, Gabon, Gambia, Georgia, Germany, Ghana, Gibraltar, Greece, Greenland, Grenada, Guadeloupe, Guam, Guatemala, Guernsey, Guinea, Guinea-Bissau, Guyana, Haiti, Heard Island and McDonald Islands, Holy See, Honduras, Hong Kong, Hungary, Iceland, India, Indonesia, Iran, Iraq, Ireland, Isle of Man, Israel, Italy, Jamaica, Japan, Jersey, Jordan, Kazakhstan, Kenya, Kiribati, Democratic People's Republic of Korea, Republic of Korea, Kuwait, Kyrgyzstan, Lao People's Democratic Republic, Latvia, Lebanon, Lesotho, Liberia, Libya, Liechtenstein, Lithuania, Luxembourg, Macao, Madagascar, Malawi, Malaysia, Maldives, Mali, Malta, Marshall Islands, Martinique, Mauritania, Mauritius, Mayotte, Mexico, Micronesia, Moldova, Monaco, Mongolia, Montenegro, Montserrat, Morocco, Mozambique, Myanmar, Namibia, Nauru, Nepal, Netherlands, New Caledonia, New Zealand, Nicaragua, Niger, Nigeria, Niue, Norfolk Island, North Macedonia, Northern Mariana Islands, Norway, Oman, Pakistan, Palau, Palestine, Panama, Papua New Guinea, Paraguay, Peru, Philippines, Pitcairn, Poland, Portugal, Puerto Rico, Qatar, Réunion, Romania, Russian Federation, Rwanda, Saint Barthélemy, Saint Helena, Ascension and Tristan da Cunha, Saint Kitts and Nevis, Saint Lucia, Saint Martin, Saint Pierre and Miquelon, Saint Vincent and the Grenadines, Samoa, San Marino, Sao Tome and Principe, Saudi Arabia, Senegal, Serbia, Seychelles, Sierra Leone, Singapore, Sint Maarten, Slovakia, Slovenia, Solomon Islands, Somalia, South Africa, South Georgia and the South Sandwich Islands, South Sudan, Spain, Sri Lanka, Sudan, Suriname, Svalbard and Jan Mayen, Sweden, Switzerland, Syrian Arab Republic, Taiwan, Tajikistan, Tanzania, Thailand, Timor-Leste, Togo, Tokelau, Tonga, Trinidad and Tobago, Tunisia, Turkey, Turkmenistan, Turks and Caicos Islands, Tuvalu, Uganda, Ukraine, United Arab Emirates, United Kingdom of Great Britain and Northern Ireland, United States of America, United States Minor Outlying Islands, Uruguay, Uzbekistan, Vanuatu, Venezuela, Viet Nam, Virgin Islands (British), Virgin Islands (U.S.), Wallis and Futuna, Western Sahara, Yemen, Zambia, Zimbabwe"

    jq(function () {

    });
</script>
<style type="text/css">

label {
    font-size: 0.9em;
}

.formHeader {
    text-align: center;
    font-size: 2.4em;
    font-weight: bold;
    margin: 12px 4px 4px 6px;
    background-color: whitesmoke;
}

.formIntro {
    text-align: center;
    font-size: 1.2em;
    background-color: whitesmoke;

}

.formBody {
    background-color: white;
    display: block;
    text-align: center;
}
form {
    display: inline-block;
    text-align: left;
    left: auto;
    right: auto;
}
    .ke-field-label {
        font-size: 1em;
    }
</style>

<div class="formHeader">
    2019 NOVEL CORONAVIRUS <br/>
    TRAVELERS HEALTH SURVEILLANCE FORM
</div>

<div class="formIntro">
    Providing the following information to port health officer is required under the Public Health Act Cap 242 of the laws of Kenya,
    and is being collected as part of public health response to a new coronavirus identified in China.

</div>
<div class="formBody">
    <form method="post" action="${ui.pageLink("kenyaemr", "selfRegistration")}" >

        <div class="ke-panel-content">

            <div class="ke-form-globalerrors" style="display: none"></div>

            <div class="ke-form-instructions">
                <strong style="color: red;">*</strong> indicates a required field
            </div>

            <fieldset>
                <legend>Demographics</legend>
                <table>
                    <tr>
                        <td valign="top">
                            <label for="field8736" class="ke-field-label">
                                First Name
                            </label>
                            <span class="ke-field-content">
                                <input id="fr8982" type="text" name="firstName" style="width: 260px" value=""/>
                                <span id="fr8982-error" class="error" style="display: none"></span>
                            </span>
                        </td>

                        <td valign="top">
                            <label for="field2381" class="ke-field-label">
                                Middle Name
                            </label>
                            <span class="ke-field-content">

                                <input id="fr4498" type="text" name="middleName" style="width: 260px" value=""/>
                                <span id="fr4498-error" class="error" style="display: none"></span>

                            </span>
                        </td>

                        <td valign="top">
                            <label for="field7143" class="ke-field-label">
                                Last Name
                            </label>
                            <span class="ke-field-content">

                                <input id="fr3563" type="text" name="lastName" style="width: 260px" value=""/>

                                <span id="fr3563-error" class="error" style="display: none"></span>

                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <label class="ke-field-label">
                                Country of residence
                            </label>
                            <span class="ke-field-content">

                                <input id="countryOfResidence" type="text" name="countryOfResidence" style="width: 260px" value=""/>

                                <span id="countryOfResidence-error" class="error" style="display: none"></span>

                            </span>
                        </td>
                        <td valign="top">
                            <label class="ke-field-label">
                                Unique Identifier
                            </label>
                            <span class="ke-field-content">

                                <input id="uniqueIdentifier" type="text" name="uniqueIdentifier" style="width: 260px" value=""/>

                                <span id="uniqueIdentifier-error" class="error" style="display: none"></span>

                            </span>
                        </td>
                    </tr>
                </table>

                <table>
                    <tr>
                        <td valign="top">
                            <label class="ke-field-label">Sex *</label>
                            <span class="ke-field-content">
                                <input type="radio" name="sex" value="F"
                                       id="gender-F"/> Female
                                <input type="radio" name="sex" value="M"
                                       id="gender-M"/> Male
                                <span id="gender-F-error" class="error" style="display: none"></span>
                                <span id="gender-M-error" class="error" style="display: none"></span>
                            </span>
                        </td>
                        <td valign="top"></td>
                        <td valign="top">
                            <label class="ke-field-label">Date of Birth *</label>
                            <span class="ke-field-content">

                                <script type="text/javascript">
                                    jq(function () {
                                        jq('#patient-birthdate_date').datepicker({
                                            dateFormat: 'dd-M-yy',
                                            changeMonth: true,
                                            changeYear: true,
                                            showButtonPanel: true,
                                            yearRange: '-120:+5',
                                            autoSize: true


                                        });

                                        jq('#patient-birthdate_date, #patient-birthdate_hour, #patient-birthdate_minute').change(function () {
                                            kenyaui.updateDateTimeFromDisplay('patient-birthdate', false);
                                        });
                                    });
                                </script>
                                <input id="patient-birthdate" type="hidden" name="birthDate"/>
                                <input id="patient-birthdate_date" type="text"/>

                                <span id="patient-birthdate-error" class="error" style="display: none"></span>



                                <span id="from-age-button-placeholder"></span>
                            </span>
                        </td>
                        <td valign="top"></td>
                        <td valign="top">
                        <td style="width: 140px">

                        </td>
                    </tr>
                </table>

            </fieldset>

            <fieldset>
                <legend>Contact</legend>

                <table>
                    <tr>
                        <td valign="top">
                            <label for="field8541" class="ke-field-label">
                                Physical Address/Landmark
                            </label>
                            <span class="ke-field-content">

                                <input id="fr6770" type="text" name="physicalAddress" style="width: 260px" value=""/>

                                <span id="fr6770-error" class="error" style="display: none"></span>

                            </span>
                        </td>

                        <td valign="top">
                            <label for="field8451" class="ke-field-label">
                                Phone No.
                            </label>
                            <span class="ke-field-content">

                                <input id="fr4948" type="text" name="phoneContact" style="width: 260px" value=""/>


                                <span id="fr4948-error" class="error" style="display: none"></span>

                            </span>
                        </td>

                        <td valign="top">
                            <label class="ke-field-label">
                                Email Address
                            </label>
                            <span class="ke-field-content">

                                <input id="personalEmail" type="text" name="personalEmail" style="width: 260px" value=""/>


                                <span id="personalEmail-error" class="error" style="display: none"></span>

                            </span>
                        </td>


                    </tr>
                </table>

            </fieldset>

            <fieldset>
                <legend>Travel Information</legend>

                <table>
                    <tr>
                        <td valign="top">
                            <label class="ke-field-label">Date of arrival in Kenya</label>
                            <span class="ke-field-content">

                                <script type="text/javascript">
                                    jq(function () {
                                        jq('#traveler-arrival_date').datepicker({
                                            dateFormat: 'dd-M-yy',
                                            changeMonth: true,
                                            changeYear: true,
                                            showButtonPanel: true,
                                            yearRange: '-120:+5',
                                            autoSize: true


                                        });

                                        jq('#traveler-arrival_date, #traveler-arrival_hour, #traveler-arrival_minute').change(function () {
                                            kenyaui.updateDateTimeFromDisplay('arrival-date', false);
                                        });
                                    });
                                </script>
                                <input id="arrival-date" type="hidden" name="arrivalDate"/>
                                <input id="traveler-arrival_date" type="text"/>

                                <span id="arrival-date-error" class="error" style="display: none"></span>



                                <span></span>
                            </span>
                        </td>

                        <td valign="top">

                        </td>


                    </tr>
                    <tr>
                        <td valign="top">
                            <label class="ke-field-label">
                                Airline/Bus
                            </label>
                            <span class="ke-field-content">

                                <input id="vesel" type="text" name="vesel" style="width: 260px" value=""/>


                                <span id="vesel-error" class="error" style="display: none"></span>

                            </span>
                        </td>
                        <td valign="top">
                            <label class="ke-field-label">
                                Flight/Bus Number
                            </label>
                            <span class="ke-field-content">

                                <input id="veselNumber" type="text" name="veselNumber" style="width: 260px" value=""/>


                                <span id="veselNumber-error" class="error" style="display: none"></span>

                            </span>
                        </td>

                        <td valign="top">
                            <label class="ke-field-label">
                                Seat Number
                            </label>
                            <span class="ke-field-content">

                                <input id="seatNumber" type="text" name="seatNumber" style="width: 260px" value=""/>


                                <span id="seatNumber-error" class="error" style="display: none"></span>

                            </span>
                        </td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <label class="ke-field-label">
                                Destination in Kenya
                            </label>
                            <span class="ke-field-content">

                                <input id="destination" type="text" name="destination" style="width: 260px" value=""/>


                                <span id="destination-error" class="error" style="display: none"></span>

                            </span>
                        </td>
                    </tr>
                </table>

            </fieldset>

            <fieldset>
                <legend>Local Contact information</legend>

                <div>
                    <em></e><i>Please provide your contact information if you plan to reside in Kenya for a duration exceeding 1 hour</i>
                    </em>
                </div>

                <table>
                    <tr>
                        <td valign="top">
                            <label class="ke-field-label">
                                Name of your contact person (if not yourself)
                            </label>
                            <span class="ke-field-content">

                                <input id="contactPerson" type="text" name="contactPerson" style="width: 260px"
                                       value=""/>

                                <span id="contactPerson-error" class="error" style="display: none"></span>

                            </span>
                        </td>

                        <td valign="top">
                            <label class="ke-field-label">
                                Telephone No of your contact person (if any)
                            </label>
                            <span class="ke-field-content">

                                <input id="contactPersonTelephone" type="text" name="contactPersonTelephone"
                                       style="width: 260px" value=""/>


                                <span id="contactPersonTelephone-error" class="error" style="display: none"></span>

                            </span>
                        </td>

                    </tr>
                    <tr>
                        <td valign="top">
                            <label class="ke-field-label">
                                Village/House number/Hotel
                            </label>
                            <span class="ke-field-content">

                                <input id="villageHouseHotel" type="text" name="villageHouseHotel" style="width: 260px"
                                       value=""/>

                                <span id="villageHouseHotel-error" class="error" style="display: none"></span>

                            </span>
                        </td>

                        <td valign="top"></td>
                    </tr>

                    <tr>
                        <td valign="top">
                            <label class="ke-field-label">
                                Sublocation/Estate
                            </label>
                            <span class="ke-field-content">

                                <input id="subLocationEstate" type="text" name="subLocationEstate" style="width: 260px"
                                       value=""/>

                                <span id="subLocationEstate-error" class="error" style="display: none"></span>

                            </span>
                        </td>

                        <td valign="top"></td>
                    </tr>
                    <tr>
                        <td valign="top">
                            <label class="ke-field-label">
                                County
                            </label>
                            <span class="ke-field-content">

                                <input id="county" type="text" name="county" style="width: 260px" value=""/>

                                <span id="county-error" class="error" style="display: none"></span>

                            </span>
                        </td>

                        <td valign="top">
                            <label class="ke-field-label">
                                Postal Address
                            </label>
                            <span class="ke-field-content">

                                <input id="postalAddress" type="text" name="postalAddress" style="width: 260px"
                                       value=""/>


                                <span id="postalAddress-error" class="error" style="display: none"></span>

                            </span>
                        </td>

                    </tr>
                    <tr>
                        <td valign="top">
                            <label class="ke-field-label">
                                Telephone No you plan to use while in Kenya
                            </label>
                            <span class="ke-field-content">

                                <input id="alternativePhoneNumber" type="text" name="alternativePhoneNumber"
                                       style="width: 260px" value=""/>

                                <span id="alternativePhoneNumber-error" class="error" style="display: none"></span>

                            </span>
                        </td>

                        <td valign="top">
                            <label class="ke-field-label">
                                Email Address
                            </label>
                            <span class="ke-field-content">

                                <input id="emailAddress" type="text" name="emailAddress" style="width: 260px" value=""/>


                                <span id="emailAddress-error" class="error" style="display: none"></span>

                            </span>
                        </td>

                    </tr>
                </table>

            </fieldset>

            <fieldset>
                <legend>Examination</legend>

                <div>
                    TODAY OR IN THE PAST 24 HOURS, HAVE YOU HAD ANY OF THE FOLLOWING SYMPTOMS?
                </div>
                <table>
                    <tr>
                        <td>
                            <label class="ke-field-label">
                                Fever (38°C or higher), felt feverish, or had chills?
                            </label>
                        </td>
                        <td>
                            <span class="ke-field-content">

                                <input type="radio" name="temperature" value="1065" />Yes
                                <input type="radio" name="temperature" value="1066" />No


                                <span id="temperature-error" class="error" style="display: none"></span>

                            </span>
                        </td>

                        <td>
                        </td>

                    </tr>
                    <tr>
                        <td>
                            <label class="ke-field-label">
                                Cough?
                            </label>
                        </td>
                        <td>
                            <span class="ke-field-content">

                                <input type="radio" name="cough" value="1065" />Yes
                                <input type="radio" name="cough" value="1066" />No


                                <span id="cough-error" class="error" style="display: none"></span>

                            </span>
                        </td>

                        <td valign="top">
                        </td>

                    </tr>

                    <tr>
                        <td>
                            <label class="ke-field-label">
                                Difficulty breathing?
                            </label>
                        </td>
                        <td>
                            <span class="ke-field-content">

                                <input type="radio" name="difficultyBreathing" value="1065" />Yes
                                <input type="radio" name="difficultyBreathing" value="1066" />No


                                <span id="difficultyBreathing-error" class="error" style="display: none"></span>

                            </span>
                        </td>

                        <td valign="top">
                        </td>

                    </tr>
                </table>

            </fieldset>

            <br/>
            <br/>

            <div class="ke-panel-footer">
                <button type="submit">
                    <img src="/openmrs/ms/uiframework/resource/kenyaui/images/glyphs/ok.png"/> Submit Form
                </button>

                <button type="button" class="cancel-button"><img
                        src="/openmrs/ms/uiframework/resource/kenyaui/images/glyphs/cancel.png"/> Cancel</button>

            </div>

        </div>

    </form>
</div>
