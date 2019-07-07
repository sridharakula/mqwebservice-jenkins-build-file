package com.mq.domain;

import com.google.gson.Gson;

import java.util.LinkedHashMap;
import java.util.Vector;

/*
@author Sridhar Akula
This class is to handle Transaction attributes,
parse message and validate message.

 */
public class Transaction {

    private String requestType="";
    private String txnId="";
    private String name="";
    private String formatType="";
    private String amount="";
    private String currency="";
    private String service="";
    private String sourceCountryCode="";
    private String referenceNumbers="";
    private String executionDate="";
    private String forexRate="";


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



    public void parse(String str){

        //Transaction txn=new Transaction();

        System.out.println("Message from Active MQ: "+str);

        if (str!=null) {
            String[] mq_message = str.split("\\n");

            mq_message[0] = mq_message[0].trim();
            mq_message[1] = mq_message[1].trim();
            mq_message[2] = mq_message[2].trim();
            mq_message[3] = mq_message[3].trim();

            System.out.println("mq_message[0] :" + mq_message[0]);
            System.out.println("mq_message[1] :" + mq_message[1]);
            System.out.println("mq_message[2] :" + mq_message[2]);
            System.out.println("mq_message[3] :" + mq_message[3]);

            setRequestType(mq_message[0].substring(0, 1));
            System.out.println("requestType:" + mq_message[0].substring(0, 1));

            setTxnId(mq_message[0].substring(1, 23).trim());
            System.out.println("txnId:" + mq_message[0].substring(1, 23).trim());


            setName(mq_message[0].substring(23, 43).trim());
            System.out.println("Name:" + mq_message[0].substring(23, 43).trim());

            //nameList.contains(name);

            setFormatType(mq_message[0].substring(43, 44).trim());
            System.out.println("formatType:" + mq_message[0].substring(43, 44).trim());

            setAmount(mq_message[0].substring(44, 63).trim());
            System.out.println("Amount:" + mq_message[0].substring(44, 63).trim());

            setCurrency(mq_message[0].substring(63, 66).trim());
            System.out.println("currency:" + mq_message[0].substring(63, 66).trim());

            setService(mq_message[0].substring(66, 69).trim());
            System.out.println("Service:" + mq_message[0].substring(66, 69).trim());

            setSourceCountryCode(mq_message[0].substring(69, 71).trim());
            System.out.println("sourceCountryCode:" + mq_message[0].substring(69, 71).trim());

            setReferenceNumbers(mq_message[1]);
            System.out.println("referenceNumbers||" + mq_message[1]);

            setExecutionDate(mq_message[2]);
            System.out.println("executionDate||" + mq_message[2]);

            setForexRate(mq_message[3]);
            System.out.println("forexRate||" + mq_message[3]);

            //System.out.println("Txn Details:"+txn);
            //return txn;
        }
    }


    public String validate(){

        Vector<String> nameList = new Vector<String>();
        nameList.add("Mark Imaginary");
        nameList.add("Govind Real");
        nameList.add("Shakil Maybe");
        nameList.add("Chang Imagine");
        nameList.add("Roy Kumar");

        Vector<String> ccode = new Vector<String>();
        //ccode.addElement("DE");
        //ccode.addElement("GB");
        ccode.add("AT");

        Vector<String> serviceCode = new Vector<String>();
        serviceCode.add("ATZ");
        //serviceCode.add("AUZ");
        //serviceCode.add("ATC");

        String checkWarningMessage="Ship dual FERT chem";
        String restMessage=new String("Nothing found, all okay");
        String shipmentMsg="Suspicious shipment";
        String delim=",";
        String delim1="DELIM";

        boolean bName=false;
        boolean bExecDate=false;
        boolean bCcode=false;
        boolean bScode=false;

        bName= nameList.contains(getName());
        System.out.println("bName :"+bName);

        bCcode = ccode.contains(getSourceCountryCode());
        System.out.println("bCcode :"+bCcode);

        bScode=serviceCode.contains(getService());
        System.out.println("bScode :"+bScode);

        if (getExecutionDate().contains(checkWarningMessage)){
            bExecDate=true;
        }

        System.out.println("bExecDate :"+bExecDate);

        LinkedHashMap <String, String> msg_data =  new LinkedHashMap<String, String>();

        if (bName && bCcode && bScode && bExecDate){
            StringBuffer buildMsg=new StringBuffer();
            buildMsg.append(shipmentMsg).append(delim).append(getSourceCountryCode())
                    .append(delim).append(getService()).append(delim).append(getTxnId());

            restMessage=buildMsg.toString();
            msg_data.put("message",restMessage);
       }
        else {
            msg_data.put("message",restMessage);
        }

        com.google.gson.Gson gson = new Gson();
        String json = gson.toJson(msg_data);
        //String json = gson.toJson();

        System.out.println("json: "+json);

        return restMessage;
    }
}
