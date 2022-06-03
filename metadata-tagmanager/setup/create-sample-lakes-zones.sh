#!/bin/bash

project_id=${1}

echo "Creating Dataplex lakes in project ${project_id}"


`gcloud dataplex lakes create "core-crm"  --project="${project_id}" --display-name="Core - CRM" --labels=domain_type="source" --location="us-central1" --description="Customer master data domain is an unified central data store for all customer data. It contains list of customer specific data products for all the line of buisness"`

`gcloud dataplex lakes create "customer-analytics"  --project="${project_id}" --display-name="Customer Analytics" --labels=domain_type="consumer" --location="us-central1" --description="Customer analytics data. Contains loyalty, ltv, demographics, customer behaviour etc.."`
  
`gcloud dataplex lakes create "core-financial-products"  --project="${project_id}" --display-name="Core - Financial Products" --labels=domain_type="source" --location="us-central1" --description="Core financial products is the authorized data source for all the financial products provided by our organization"`

`gcloud dataplex lakes create "core-banking-credit-cards"  --project="${project_id}" --display-name="Consumer Banking - Credit Cards" --labels=domain_type="source" --location="us-central1" --description="The Credit card processing domain (PCI – Payment Card Industry) covers debit, credit, prepaid, e-purse, ATM, and POS cards, and associated businesses. Zones within these are - cards_identifier,cards_payments, cards_transactions, offline_sales, online_sales, returns, support integration"`
  
`gcloud dataplex lakes create "core-reference-data"  --project="${project_id}" --display-name="Core - Reference Data" --labels=domain_type="source" --location="us-central1" --description="Contains  system of truth for reference data" `
  
`gcloud dataplex lakes create "core-banking-deposits"  --project="${project_id}" --display-name="Consumer Banking - Deposits" --labels=domain_type="source" --location="us-central1" --description="Domain for deposits that are made to deposit accounts such as savings accounts, checking accounts, and money market accountts. Includes Current(Demand Deposit) Account, Savings Accounts, call deposit accounts, Certificates of Deposit/Time deposit Accounts and Special Considerations" `
  
`gcloud dataplex lakes create "core-banking-student-loans"  --project="${project_id}" --display-name="Consumer Banking - Student Loans" --labels=domain_type="source" --location="us-central1" --description="A student loan is money borrowed from the government or a private lender in order to pay for college. The loan has to be paid back later, along with interest that builds up over time. The money can usually be used for tuition, room and board, books or other fees. This domain provided Student Loans portfolio - Origination to servicing " `
  
`gcloud dataplex lakes create "core-banking-home-equity-loans"  --project="${project_id}" --display-name="Consumer Banking - Home Equity Loans" --labels=domain_type="source" --location="us-central1" --description="A home equity loan—also known as an equity loan, home equity installment loan, or second mortgage—is a type of consumer debt. Home equity loans allow homeowners to borrow against the equity in their homes. The loan amount is based on the difference between the home’s current market value and the homeowner’s mortgage balance due. Home equity loans tend to be fixed-rate, while the typical alternative, home equity lines of credit (HELOCs), generally have variable rates" `
  
`gcloud dataplex lakes create "sales-customer-acquisition"  --project="${project_id}" --display-name="Sales - Customer Acquisition" --labels=domain_type="consumer" --location="us-central1" --description="Sales customer acquisition domain containing all the sales data for the new customers/prospects organization" `
  
`gcloud dataplex lakes create "sales-portfolio-management"  --project="${project_id}" --display-name="Sales - Portfolio Management " --labels=domain_type="consumer" --location="us-central1" --description="Sales customer portfolio management domain containing all the sales data for the existing customer base" `
  
`gcloud dataplex lakes create "sales-data"  --project="${project_id}" --display-name="Sales - CRM " --labels=domain_type="source" --location="us-central1" --description="Authorized data source for all the CRM customer data including all the customer interactions " `
  
`gcloud dataplex lakes create "sales-analytics"  --project="${project_id}" --display-name="Sales - CRM Analytics" --labels=domain_type="consumer" --location="us-central1" --description="Authorized data source for all the CRM customer data including all the customer interactions " `
  
`gcloud dataplex lakes create "marketing-product-development"  --project="${project_id}" --display-name="Marketing - Product Development" --labels=domain_type="consumer" --location="us-central1" --description="Domain is responsible to attract and retain customers and merchants by developing new programs, features and benefits and market them through variety of chanannels. Through the development of a large prospect databasesm use of credit bureau data and use of a csutomer contact strategy and management system, we continously develop our modeling and customer engagement capabilities which helps optimize products, pricing and channel selection " `
  
`gcloud dataplex lakes create "marketing-prospects"  --project="${project_id}" --display-name="Marketing - Prospects" --labels=domain_type="source" --location="us-central1" --description="Marketing  domain for future customers or prospects. Zones - campaign, loyalty program, newsletters, social platforms, surveys " `
  
`gcloud dataplex lakes create "marketing-analytics-app"  --project="${project_id}" --display-name="Marketing - Analytics" --labels=domain_type="source" --location="us-central1" --description="Marketing data for analytics apps. Zones: analytics app, analytics websites " `
  
`gcloud dataplex lakes create "marketing-ads"  --project="${project_id}" --display-name="Marketing - Ads " --labels=domain_type="source" --location="us-central1" --description="Domain responsible for sourcing all the Ads data for further analytics " `
  
`gcloud dataplex lakes create "marketing-rewards-program"  --project="${project_id}" --display-name="Marketing - Rewards Program" --labels=domain_type="consumer" --location="us-central1" --description="Our cardmembers use several card products, all with no annual fee, that allow them to earn their rewards based on their purchases, which can be redeemed in any amount at any time, in general as set forth below" `
  
`gcloud dataplex lakes create "marketing-customer-acquisition"  --project="${project_id}" --display-name="Marketing - Customer Acquisition" --labels=domain_type="consumer" --location="us-central1" --description="Domain reponsible for acquiring new customers.  Zones - campaign, loyalty program, newsletters, social platforms, surveys " `
  
`gcloud dataplex lakes create "marketing-portfolio-management"  --project="${project_id}" --display-name="Marketing - Portfolio Management" --labels=domain_type="consumer" --location="us-central1" --description="The revolving nature of our credit card loans requires that we regularly assess the credit risk exposure of such accounts. This assessment uses the individual’s account performance information as well as information from credit bureaus. We utilize statistical evaluation models to support the measurement and management of credit risk. At the individual customer level, we use custom risk models together with more generally available industry models as an integral part of the credit decision-making process. Depending on the duration of the customer’s account, risk profile and other performance metrics, the account may be subject to a range of account management treatments, including transaction authorization limits and increases or decreases on credit limits." `
  
`gcloud dataplex lakes create "marketing-brand-advertisement"  --project="${project_id}" --display-name="Marketing - Brand Advertisement" --labels=domain_type="consumer" --location="us-central1" --description="a full-service marketing department charged with delivering integrated mass and direct communications to foster customer engagement with our products and services. We also leverage strategic partnerships and sponsorship properties such as the NHL and the Big Ten Conference to help drive loan growth. Our brand team utilizes consumer insights and market intelligence to define our mass communication strategy, create multi-channel advertising messages and develop marketing partnerships with sponsorship properties. This work is performed in house as well as with a variety of external agencies and vendors." `
  
`gcloud dataplex lakes create "risk-management-credit-card-risk"  --project="${project_id}" --display-name="risk management - Credit Card Risk" --labels=domain_type="consumer" --location="us-central1" --description="The revolving nature of our credit card loans requires that we regularly assess the credit risk exposure of such accounts. This assessment uses the individual’s account performance information as well as information from credit bureaus. We utilize statistical evaluation models to support the measurement and management of credit risk. At the individual customer level, we use custom risk models together with more generally available industry models as an integral part of the credit decision-making process. Depending on the duration of the customer’s account, risk profile and other performance metrics, the account may be subject to a range of account management treatments, including transaction authorization limits and increases or decreases on credit limits" `
  
`gcloud dataplex lakes create "fraud-management-credit-cards"  --project="${project_id}" --display-name="fraud management - Credit Cards" --labels=domain_type="consumer" --location="us-central1" --description="Domain manages fraud for credit cards" `
  
`gcloud dataplex lakes create "risk-management-aml"  --project="${project_id}" --display-name="risk management - AML" --labels=domain_type="consumer" --location="us-central1" --description="Domain for Anti-money laundering" `
  
`gcloud dataplex lakes create "operational-data"  --project="${project_id}" --display-name="Operational Data" --labels=domain_type="source" --location="us-central1" --description="Operational Domain responsible for providing operational/system data. This is a authorized  data source " `

`gcloud dataplex lakes create "core-merchants"  --project="${project_id}" --display-name="Core - Merchants" --labels=domain_type="source" --location="us-central1" --description="Source system for sourcing all the merchant data"`

`gcloud dataplex lakes create "merchants-analytics"  --project="${project_id}" --display-name="Merchant Analytics" --labels=domain_type="consumer" --location="us-central1" --description="System handling all the merchant analytics"`

`gcloud dataplex lakes create "third-party-data-source"  --project="${project_id}" --display-name="Merchant Analytics" --labels=domain_type="consumer" --location="us-central1" --description="System handling all the merchant analytics"`

echo "Successfully created Dataplex lakes in project ${project_id}" 

echo "Creating Zones"

#Core CRM
`gcloud dataplex zones create customer-source-raw --project="${project_id}" --display-name="Customer Source Raw"  --location="us-central1" --lake=core-crm --resource-location-type=MULTI_REGION --type=RAW  --description="Raw customer data from the source system" --discovery-enabled --discovery-schedule="0 * * * *" `

`gcloud dataplex zones create customer-source-data-products  --project="${project_id}" --lake=core-crm --display-name="Customer Source Data Products" --location="us-central1" --resource-location-type=MULTI_REGION --type=CURATED --labels=data_product_type="master_data" --description="Well validated source customer data" --discovery-enabled --discovery-schedule="0 * * * *" `

#customer analytics
`gcloud dataplex zones create customer-analytics-curated --project="${project_id}" --lake=customer-analytics --display-name="Customer Analytics Data Products" --location="us-central1" --resource-location-type=MULTI_REGION --type=CURATED --labels=data_product_type="application_data" --description="aggregated/analytics data for customers. Data can only be consumed from other source domains only. Should be limited to minimum" --discovery-enabled --discovery-schedule="0 * * * *" `


`gcloud dataplex zones create customer-analytics-data-products --project="${project_id}" --lake=customer-analytics --display-name="Customer Analytics Data Products" --location="us-central1" --resource-location-type=MULTI_REGION --type=CURATED --labels=data_product_type="application_data" --description="aggregated/analytics data for customers. Data can only be consumed from other source domains only. Should be limited to minimum" --discovery-enabled --discovery-schedule="0 * * * *" `

#Transactions 
`gcloud dataplex zones create transactions-offline-source-raw  --project="${project_id}" --display-name="Raw credit card offline transactions "  --location="us-central1" --lake=core-banking-credit-cards --resource-location-type=MULTI_REGION --type=RAW  --description="Raw offline transactions data from the source system" --discovery-enabled --discovery-schedule="0 * * * *" `

`gcloud dataplex zones create transactions-online-source-raw  --project="${project_id}" --display-name="Raw credit card online transactions"  --location="us-central1" --lake=core-banking-credit-cards --resource-location-type=MULTI_REGION --type=RAW  --description="Raw online transactions data from the source system" --discovery-enabled --discovery-schedule="0 * * * *" `

`gcloud dataplex zones create credit-card-identifiers-source-raw  --project="${project_id}" --display-name="credit card identifiers"  --location="us-central1" --lake=core-banking-credit-cards --resource-location-type=MULTI_REGION --type=CURATED  --description="Identifiers for credit cards" --discovery-enabled --discovery-schedule="0 * * * *" `

`gcloud dataplex zones create transactions-offline-enriched-data-product --project="${project_id}" --display-name="Enriched data product credit card offline transactions"  --location="us-central1" --lake=core-banking-credit-cards --resource-location-type=MULTI_REGION --type=CURATED --labels=data_product_category="master_data" --description="Data products for offline transactions data from the source system" --discovery-enabled --discovery-schedule="0 * * * *" `

`gcloud dataplex zones create transactions-online-encriched-data-product --project="${project_id}" --display-name="Encriched data product credit card online transactions"  --location="us-central1" --lake=core-banking-credit-cards --resource-location-type=MULTI_REGION --type=CURATED --labels=data_product_category="master_data" --description="Data products for offline transactions data from the source system" --discovery-enabled --discovery-schedule="0 * * * *" `

#Deposits
`gcloud dataplex zones create savings-account-transactions-source-raw  --project="${project_id}" --display-name="Raw saving account transactions "  --location="us-central1" --lake=core-banking-credit-cards --resource-location-type=MULTI_REGION --type=RAW  --description="Raw offline transactions data from the source system" --discovery-enabled --discovery-schedule="0 * * * *" `

`gcloud dataplex zones create checking-account-transactions-source-raw  --project="${project_id}" --display-name="Raw checking account transactions "  --location="us-central1" --lake=core-banking-credit-cards --resource-location-type=MULTI_REGION --type=RAW  --description="Raw offline transactions data from the source system" --discovery-enabled --discovery-schedule="0 * * * *" `

`gcloud dataplex zones create savings-account-transactions-source-data-products --project="${project_id}" --display-name="Data products saving account transactions "  --location="us-central1" --lake=core-banking-credit-cards --resource-location-type=MULTI_REGION --type=RAW  --description="Raw offline transactions data from the source system" --discovery-enabled --discovery-schedule="0 * * * *" `

`gcloud dataplex zones create checking-account-transactions-source-data-products  --project="${project_id}" --display-name="Data products account transactions "  --location="us-central1" --lake=core-banking-credit-cards --resource-location-type=MULTI_REGION --type=RAW  --description="Raw offline transactions data from the source system" --discovery-enabled --discovery-schedule="0 * * * *" `