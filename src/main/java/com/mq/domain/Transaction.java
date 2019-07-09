package com.mq.domain;

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/*
@author Sridhar Akula
This class is to handle Transaction attributes,
parse message and validate message.

 */
public class Transaction implements Parser, Validator {

    private String msg;

    private String requestType;
    private String txnId;
    private String name;
    private String formatType;
    private String amount;
    private String currency;
    private String service;
    private String sourceCountryCode;
    private String referenceNumbers;
    private String executionDate;
    private String forexRate;

    private String restMessage = "Nothing found, all okay";
    private String jsonData = "";

    private boolean refNumberWithValidFormat;
    private boolean executionDateWithValidFormat;
    private boolean forexRateWithValidFormat;

    public String getRequestType() {
        return requestType;
    }

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public String getTxnId() {
        return txnId;
    }

    public void setTxnId(String txnId) {
        this.txnId = txnId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSourceCountryCode() {
        return sourceCountryCode;
    }

    public void setSourceCountryCode(String sourceCountryCode) {
        this.sourceCountryCode = sourceCountryCode;
    }

    public String getReferenceNumbers() {
        return referenceNumbers;
    }

    public void setReferenceNumbers(String referenceNumbers) {
        this.referenceNumbers = referenceNumbers;
    }

    public String getExecutionDate() {
        return executionDate;
    }

    public void setExecutionDate(String executionDate) {
        this.executionDate = executionDate;
    }

    public String getForexRate() {
        return forexRate;
    }

    public void setForexRate(String forexRate) {
        this.forexRate = forexRate;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) { this.jsonData = jsonData;}


    public String getRestMessage () {return restMessage;}

    public void setRestMessage (String restMessage){ this.restMessage = restMessage; }

    public boolean isRefNumberWithValidFormat() {
        return refNumberWithValidFormat;
    }

    public void setRefNumberWithValidFormat(boolean refNumberWithValidFormat) {
        this.refNumberWithValidFormat = refNumberWithValidFormat;
    }

    public boolean isExecutionDateWithValidFormat() {
        return executionDateWithValidFormat;
    }

    public void setExecutionDateWithValidFormat(boolean executionDateWithValidFormat) {
        this.executionDateWithValidFormat = executionDateWithValidFormat;
    }

    public boolean isForexRateWithValidFormat() {
        return forexRateWithValidFormat;
    }

    public void setForexRateWithValidFormat(boolean forexRateWithValidFormat) {
        this.forexRateWithValidFormat = forexRateWithValidFormat;
    }



    public Transaction(String msg) {
        this.msg = msg;
        System.out.print("Transaction:Constructor: Message from Queue :" + msg);

    }

    /*
        Parses the message as per the business requirement.
     */

    public void parse () throws ParseException {

        System.out.println("Transaction:parse:Message from Queue :" + msg);

        String[] mq_message = msg.split("\\n");

        try {

            mq_message[0] = mq_message[0].trim();
            mq_message[1] = mq_message[1].trim();
            mq_message[2] = mq_message[2].trim();
            mq_message[3] = mq_message[3].trim();
/*
            setRequestType(mq_message[0].substring(0, 1));
            setTxnId(mq_message[0].substring(1, 23).trim());
            setName(mq_message[0].substring(23, 43).trim());
            setFormatType(mq_message[0].substring(43, 44).trim());
            setAmount(mq_message[0].substring(44, 63).trim());
            setCurrency(mq_message[0].substring(63, 66).trim());
            setService(mq_message[0].substring(66, 69).trim());
            setSourceCountryCode(mq_message[0].substring(69, 71).trim());
*/

            setRequestType(mq_message[0].substring(0, 1));
            setTxnId(mq_message[0].substring(1, 23));
            setName(mq_message[0].substring(23, 43));
            setFormatType(mq_message[0].substring(43, 44));
            setAmount(mq_message[0].substring(44, 63));
            setCurrency(mq_message[0].substring(63, 66));
            setService(mq_message[0].substring(66, 69));
            setSourceCountryCode(mq_message[0].substring(69, 71));

            String msg1Delim = ":20:";
            String msg2Delim = ":32A:";
            String msg3Delim = ":36:";

            if (!mq_message[1].startsWith(msg1Delim)){
                throw new ParseException("Invalid Delimiter for ReferencesNumbers in Main Message");
            }

            if (!mq_message[2].startsWith(msg2Delim)){
                throw new ParseException("Invalid Delimiter for ExecutionDate in Main Message");
            }

            if (!mq_message[3].startsWith(msg3Delim)){
                throw new ParseException("Invalid Delimiter for Forex rate Main Message");
            }

            setReferenceNumbers(mq_message[1].replace(msg1Delim, ""));
            setExecutionDate(mq_message[2].replace(msg2Delim, ""));
            setForexRate(mq_message[3].replace(msg3Delim, ""));

            System.out.println("Transaction:parse:After Replacement :"+this);

        } catch (ParseException exp) {
            throw exp;
        } catch (Exception exp) {

            exp.printStackTrace();
            throw new ParseException(exp.getMessage(), exp);
        }

        //System.out.println("Transaction:parseBean:Txn Details:" + this);


    }


    /*
    Validates the message as per business requirement.
    This message will be sent to Webservice and also logged.
     */

    public void validate ()  throws ValidationException {
        System.out.println("Transaction:validate: Message after parse :" + this);
        if (getRequestType().isEmpty()) {
            throw new ValidationException("Mandatory field RequestType missing");
        }
        if (getTxnId().isEmpty()) {
            throw new ValidationException("Mandatory field TransactionId missing");
        }
        if (getName().isEmpty()) {
            throw new ValidationException("Mandatory field Name missing");
        }
        if (getFormatType().isEmpty()) {
            throw new ValidationException("Mandatory field FormatType missing");
        }
        if (getAmount().isEmpty()) {
            throw new ValidationException("Mandatory field Amount missing");
        }
        if (getCurrency().isEmpty()) {
            throw new ValidationException("Mandatory field Currency missing");
        }
        if (getService().isEmpty()) {
            throw new ValidationException("Mandatory field Service missing");
        }
        if (getSourceCountryCode().isEmpty()) {
            throw new ValidationException("Mandatory field SourceCountryCode missing");
        }

        if (getReferenceNumbers().isEmpty() && getExecutionDate().isEmpty()) {
            throw new ValidationException("Main Message is missing");
        }

        if (getReferenceNumbers().isEmpty()) {
            throw new ValidationException("Mandatory field ReferencesNumbers in Main Message missing");
        }

        if (getExecutionDate().isEmpty()) {
            throw new ValidationException("Mandatory field ExecutionDate in Main Message missing");
        }

        if (!getRequestType().equals("G")) {
            throw new ValidationException("Invalid Request Type");
        }


        Set<String> nameList = new HashSet<String>();
        nameList.add("Mark Imaginary");
        nameList.add("Govind Real");
        nameList.add("Shakil Maybe");
        nameList.add("Chang Imagine");
        nameList.add("Roy Kumar");

        Set<String> ccode = new HashSet<String>();
        ccode.add("AT");

        Set<String> serviceCode = new HashSet<String>();
        serviceCode.add("ATZ");

        String checkWarningMessage = "Ship dual FERT chem";

        String shipmentMsg = "Suspicious shipment";
        String delim = ",";
        String delim1 = "DELIM";

        boolean bName = nameList.contains(getName().trim());
        boolean bCcode = ccode.contains(getSourceCountryCode());
        boolean bScode = serviceCode.contains(getService());
        boolean bExecDate = getExecutionDate().contains(checkWarningMessage);


        System.out.println("bName :"+bName+ " Name:"+getName().trim()+":");
        System.out.println("bCcode :"+bCcode+" Country Code:"+getSourceCountryCode());
        System.out.println("bScode :"+bScode+ "Source Country Code:"+getService());
        System.out.println("bExecDate :"+bExecDate+" Execution Date:"+getExecutionDate());

        Map<String, String> msg_data = new HashMap<String, String>();

        if (bName && bCcode && bScode && bExecDate) {
            StringBuffer buildMsg = new StringBuffer();
            buildMsg.append(shipmentMsg).append(delim).append(getSourceCountryCode())
                    .append(delim).append(getService()).append(delim).append(getTxnId());

            restMessage = buildMsg.toString();
            //throw new ValidationException(restMessage);
            msg_data.put("message", restMessage);
        } else {
            msg_data.put("message", restMessage);
        }

        com.google.gson.Gson gson = new Gson();
        jsonData = gson.toJson(msg_data);


        System.out.println("Transaction:validate:json :" + jsonData);

    }

    private void populateData(String values, Set<String> list) {
        String[] tokens = values.split(",");
        for (String value : tokens) {
            list.add(value);
        }


    }

    @Override
    public String toString () {
        return "requestType: " + requestType + ", txnId: " + txnId + ", name: " + name + ", formatType: " + formatType + ", amount: " + amount
                + ", currency: " + currency + ", service: " + service + ", sourceCountryCode: " + sourceCountryCode + ", referenceNumbers: " + referenceNumbers
                + ", executionDate: " + executionDate + ", forexRate: " + forexRate;
    }
}
