import sys
def create_inspect_template(
    project,
    info_types,
    template_id=None,
    display_name=None,
    min_likelihood=None,
    max_findings=None,
    include_quote=None,
):
    """Creates a Data Loss Prevention API inspect template.
    Args:
        project: The Google Cloud project id to use as a parent resource.
        info_types: A list of strings representing info types to look for.
            A full list of info type categories can be fetched from the API.
        template_id: The id of the template. If omitted, an id will be randomly
            generated.
        display_name: The optional display name of the template.
        min_likelihood: A string representing the minimum likelihood threshold
            that constitutes a match. One of: 'LIKELIHOOD_UNSPECIFIED',
            'VERY_UNLIKELY', 'UNLIKELY', 'POSSIBLE', 'LIKELY', 'VERY_LIKELY'.
        max_findings: The maximum number of findings to report; 0 = no maximum.
        include_quote: Boolean for whether to display a quote of the detected
            information in the results.
    Returns:
        None; the response from the API is printed to the terminal.
    """

    #pip3 install google-cloud-dlp

    # Import the client library
    import google.cloud.dlp

    # Instantiate a client.
    dlp = google.cloud.dlp_v2.DlpServiceClient()

    # Prepare info_types by converting the list of strings into a list of
    # dictionaries (protos are also accepted).
    info_types = [{"name": info_type} for info_type in info_types]

    # Construct the configuration dictionary. Keys which are None may
    # optionally be omitted entirely.
    inspect_config = {
        "info_types": info_types,
        "min_likelihood": min_likelihood,
        "include_quote": include_quote,
        "limits": {"max_findings_per_request": max_findings},
    }

    inspect_template = {
        "inspect_config": inspect_config,
        "display_name": display_name,
    }

    # Convert the project id into a full resource id.
    parent = f"projects/{project}"

    # Call the API.
    response = dlp.create_inspect_template(
        request={
            "parent": parent,
            "inspect_template": inspect_template,
            "template_id": template_id,
        }
    )

    print("Successfully created template {}".format(response.name))

if __name__ == "__main__":
    project=sys.argv[1]
    info_types=["AGE","AUTH_TOKEN","AWS_CREDENTIALS","AZURE_AUTH_TOKEN","CREDIT_CARD_NUMBER","CREDIT_CARD_TRACK_NUMBER","DATE_OF_BIRTH","DOCUMENT_TYPE/FINANCE/REGULATORY","DOCUMENT_TYPE/FINANCE/SEC_FILING","EMAIL_ADDRESS","ENCRYPTION_KEY","ETHNIC_GROUP","GCP_API_KEY","GCP_CREDENTIALS","GENDER","HTTP_COOKIE","IBAN_CODE","IMEI_HARDWARE_ID","IP_ADDRESS","JSON_WEB_TOKEN","LAST_NAME","MAC_ADDRESS","PASSPORT","PASSWORD","PERSON_NAME","PHONE_NUMBER","STORAGE_SIGNED_URL","STREET_ADDRESS","US_BANK_ROUTING_MICR","US_DEA_NUMBER","US_DRIVERS_LICENSE_NUMBER","US_EMPLOYER_IDENTIFICATION_NUMBER","US_HEALTHCARE_NPI","US_INDIVIDUAL_TAXPAYER_IDENTIFICATION_NUMBER","US_SOCIAL_SECURITY_NUMBER","WEAK_PASSWORD_HASH","VEHICLE_IDENTIFICATION_NUMBER","XSRF_TOKEN","US_STATE","US_TOLLFREE_PHONE_NUMBER","US_VEHICLE_IDENTIFICATION_NUMBER"]
    template_id=sys.argv[2]
    display_name="Enterprise DLP template for scan"
    min_likelihood="LIKELIHOOD_UNSPECIFIED"
    max_findings=0
    create_inspect_template(project,info_types,template_id,display_name,min_likelihood,max_findings,False)