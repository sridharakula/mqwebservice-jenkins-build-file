package com.test.mqservice;

import com.mq.domain.ParseException;
import com.mq.domain.Transaction;
import com.mq.domain.ValidationException;
import com.mq.route.MessageRouter;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.springframework.context.annotation.Description;

import java.math.BigDecimal;
import java.util.StringTokenizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TransactionTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new MessageRouter();

    }

    @Test
    @Description("Check for Valid Message")
    public void checkValidMsg(){
        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123\n"
                + ":36:12";

        Transaction t = new Transaction(validMQMessage);
        t.parse();
        String expected="{\"message\":\"Nothing found, all okay\"}";
        t.validate();
        String actual=t.getJsonData();
        assertEquals(expected, actual);
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    @Description("Check for Warning Message - With Suspicious shipment")
    public void checkWarningMsg(){
        /*String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtRoy Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123,Ship dual FERT chem\n"
                + ":36:12";

         */

        String inValidMQMessage = "GZXFRTJ675FTRHJJJ87zyxtRoy Kumar           U000000000000017.450EURATZAT\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123,Ship dual FERT chem\n"
                + ":36:12\n";


        Transaction t = new Transaction(inValidMQMessage);
        t.parse();

        t.validate();
        String expected="Suspicious shipment,AT,ATZ,ZXFRTJ675FTRHJJJ87zyxt";
        String actual=t.getRestMessage();
        assertEquals(expected,actual);
    }

    @Test
    @Description("Check for message with Valid RequestType is - G")
    public void checkValidRequestType(){
        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtRoy Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123\n"
                + ":36:12";
        Transaction t = new Transaction(validMQMessage);

        String expected="G";
        t.parse();
        String actual=t.getRequestType();
        System.out.println("Expected :"+expected+" || actual :"+actual);
        Assert.assertEquals(expected,actual);
    }

    @Test
    @Description("Check for message with InValid Request Type other than G")
    public void checkInvalidRequestType(){
        String inValidMQMessage = "BZXFRTJ675FTRHJJJ87zyxtRoy Kumar           U000000000000017.450EURATZAT\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123,Ship dual FERT chem\n"
                + ":36:12\n";
        Transaction t = new Transaction(inValidMQMessage);

        String expected="G";
        t.parse();
        String actual=t.getRequestType();
        System.out.println("Expected :"+expected+" || actual :"+actual);
        Assert.assertNotEquals(expected,actual);
    }

    @Test
    @Description("Check for Valid Transaction Id length is 22 Chars")
    public void checkForTxnLength(){

        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123\n"
                + ":36:12";

        Transaction t = new Transaction(validMQMessage);

        t.parse();
        t.validate();
        int expected=22;
        int actual=t.getTxnId().length();
        System.out.println("Expected :"+expected+" || actual :"+actual);
        assertEquals(expected,actual);
    }

    @Test
    @Description("Check for Valid Name length is 20 Chars")
    public void checkForNameLength(){

        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123\n"
                + ":36:12";

        Transaction t = new Transaction(validMQMessage);

        t.parse();
        t.validate();
        int expected=20;
        int actual=t.getName().length();
        System.out.println("Expected :"+expected+" || actual :"+actual);
        assertEquals(expected,actual);
    }

    @Test
    @Description("Check for Valid FormatType - U")
    public void checkForValidFormatType(){

        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123\n"
                + ":36:12";

        Transaction t = new Transaction(validMQMessage);

        t.parse();
        t.validate();
        String expected="U";
        String actual=t.getFormatType();
        System.out.println("Expected :"+expected+" || actual :"+actual);
        assertEquals(expected,actual);
    }

    @Test
    @Description("Check for InValid FormatType other than - U")
    public void checkForInValidFormatType(){

        String inValidMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           M000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123\n"
                + ":36:12";

        Transaction t = new Transaction(inValidMQMessage);

        t.parse();
        t.validate();
        String expected="U";
        String actual=t.getFormatType();
        System.out.println("Expected :"+expected+" || actual :"+actual);
        assertNotEquals(expected,actual);
    }

    @Test
    @Description("Check for Amount with 3 decimals")
    public void checkForAmountWithThreeDecimals(){

        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123\n"
                + ":36:12";

        Transaction t = new Transaction(validMQMessage);

        t.parse();
        t.validate();
        int expected=3;
        String actualAmount=t.getAmount();
        StringTokenizer st=new StringTokenizer(actualAmount,".");
        String t1=st.nextToken();
        String t2=st.nextToken();
        int n1=t1.length();
        int actual=t2.length();

        //System.out.println("Actual decimal value :"+actual);
        System.out.println("Actual Amount:"+actualAmount);
        System.out.println("Expected :"+expected+" || actual :"+actual);
        assertEquals(expected,actual);
    }


    @Test
    @Description("Check for Currency Length is 3")
    public void checkCurrencyLength(){

        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123\n"
                + ":36:12";

        Transaction t = new Transaction(validMQMessage);

        t.parse();
        t.validate();
        long expected=3;
        int actual=t.getCurrency().length();

        System.out.println("Expected :"+expected+" || actual :"+actual);
        assertEquals(expected,actual);
    }

    @Test
    @Description("Check for Service Length is 3")
    public void checkServiceLength(){

        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123\n"
                + ":36:12";

        Transaction t = new Transaction(validMQMessage);

        t.parse();
        t.validate();
        long expected=3;
        int actual=t.getService().length();

        System.out.println("Expected :"+expected+" || actual :"+actual);
        assertEquals(expected,actual);
    }


    @Test
    @Description("Check for Service values are ATZ, AUZ, ATC")
    public void checkServiceValues(){

        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123\n"
                + ":36:12";
        String expectedService="ATZ,AUZ,ATC";
        Transaction t = new Transaction(validMQMessage);

        t.parse();
        t.validate();

        String actualService=t.getService();

        boolean expected=true;
        boolean actual=expectedService.contains(actualService);
        System.out.println("Expected :"+expected+" || actual :"+actual);
        assertEquals(expected,actual);
    }



    @Test
    @Description("Check for SourceCountryCode Length is 2")
    public void checkSourceCountryCodeLength(){

        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123\n"
                + ":36:12";

        Transaction t = new Transaction(validMQMessage);

        t.parse();
        t.validate();
        long expected=2;
        int actual=t.getSourceCountryCode().length();

        System.out.println("Expected :"+expected+" || actual :"+actual);
        assertEquals(expected,actual);
    }

   /*
    @Test
    @Description("Check for Valid Reference Type starts with :20: Begining")
    public void checkForValidReferenceNumerFormat(){
        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123\n"
                + ":36:12";

        Transaction t = new Transaction(validMQMessage);
        t.parse();
        boolean expected=true;
        boolean actual=t.isRefNumberWithValidFormat();
        System.out.println("Expected :"+expected+" || actual :"+actual);
        assertEquals(expected,actual);
    }
*/
    @Test
    @Description("Check for In Valid Reference Type prefix with OTHER than :20: ")
    public void checkForInValidReferenceNumerPrefix(){
        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
                + ":10:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123\n"
                + ":36:12";

        Transaction t = new Transaction(validMQMessage);
        thrown.expect(ParseException.class);
        thrown.expectMessage("Invalid Delimiter for ReferencesNumbers in Main Message");
        t.parse();
        t.validate();
    }

    /*
    @Test
    @Description("Check for For Valid Date Prefix starts with :32A: Begining")
    public void checkForValidDatePrefix(){
        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123\n"
                + ":36:12";

        Transaction t = new Transaction(validMQMessage);
        t.parse();
        boolean expected=true;
        boolean actual=t.isExecutionDateWithValidFormat();
        System.out.println("Expected :"+expected+" || actual :"+actual);
        assertEquals(expected,actual);
    }
*/

    @Test
    @Description("Check for InValid Date Type starts with OTHER than :32A: ")
    public void checkForInValidDatePrefix(){
        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32B:180123\n"
                + ":36:12";

        Transaction t = new Transaction(validMQMessage);

        thrown.expect(ParseException.class);
        thrown.expectMessage("Invalid Delimiter for ExecutionDate in Main Message");
        t.parse();
        t.validate();
    }
/*
    @Test
    @Description("Check for For Valid Forex Prefix starts with :36:")
    public void checkForValidDForexPrefix(){
        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123\n"
                + ":36:12";

        Transaction t = new Transaction(validMQMessage);
        t.parse();
        boolean expected=true;
        boolean actual=t.isForexRateWithValidFormat();
        System.out.println("Expected :"+expected+" || actual :"+actual);
        assertEquals(expected,actual);
    }
 */
    @Test
    @Description("Check for InValid Forex prefix with OTHER than :36: ")
    public void checkForInValidDForexPrefix(){
        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32A:180123\n"
                + ":31:12";
        Transaction t = new Transaction(validMQMessage);
        thrown.expect(ParseException.class);
        thrown.expectMessage("Invalid Delimiter for Forex rate Main Message");
        t.parse();
        t.validate();
    }

    /*
    @Test
    @Description("Check for Forex rate is positive ")
    public void checkForForexRatePositive(){
        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32B:180123\n"
                + ":36:12";

        Transaction t = new Transaction(validMQMessage);

        t.parse();
        t.validate();
        boolean expected=true;
        String forexRate=t.getForexRate();

        int forexValue=Integer.parseInt(forexRate);
        boolean actual=false;

        if (forexValue>0){actual=true;}

        System.out.println("Expected :"+expected+" || actual :"+actual);
        assertEquals(expected,actual);

    }

    @Test
    @Description("Check for For Forex rate is not positive ")
    public void checkForForexRateNegative(){
        String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
                + ":20:TR234565,Zu8765Z,Bhj876t\n"
                + ":32B:180123\n"
                + ":36:-2";

        Transaction t = new Transaction(validMQMessage);

        t.parse();
        t.validate();
        boolean expected=false;
        boolean actual=true;
        String forexRate=t.getForexRate();

        int forexValue=Integer.parseInt(forexRate);


        if (forexValue<=0){actual=false;}

        System.out.println("Expected :"+expected+" || actual :"+actual);
        assertEquals(expected,actual);

    }


 */

}

