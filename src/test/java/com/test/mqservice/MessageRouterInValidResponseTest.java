package com.test.mqservice;

import com.mq.launcher.route.MessageRouter;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.springframework.context.annotation.Description;
import org.springframework.core.annotation.Order;

public class MessageRouterInValidResponseTest extends CamelTestSupport {



    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new MessageRouter();

    }

    String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
            + ":20:TR234565,Zu8765Z,Bhj876t\n"
            + ":32A:180123\n"
            + ":36:12";

    String fraudulentMessage = "GZXFRTJ675FTRHJJJ87zyxtRoy Kumar           U000000000000017.450EURATZAT\n"
            + ":20:TR234565,Zu8765Z,Bhj876t\n"
            + ":32A:180123,Ship dual FERT chem\n"
            + ":36:12\n";

    String expectedFraudMessage = "Suspicious shipment";
    //String response ="";

    @Test
    @Order(1)
    @Description("This checks fraud response for fraud message")
    public void checkExpectedResponse() throws InterruptedException {

        System.out.println("checkExpectedResponse");
        template.sendBody("activemq:queue:testQueue", fraudulentMessage);
        String response = consumer.receiveBody("activemq:queue:myQueue", String.class);
        System.out.println("The response in checkExpectedResponse: " + response);
        assertStringContains(response, expectedFraudMessage);
        // assertEquals(expected, response);
        log.info("**************************THIS IS LOGGER*********");
    }

    @Test
    @Order(2)
    @Description("This is for negative test case: Send validMessage and check for response. Make sure the responses (expected and actual)are not equal.")

    public void checkUnexpectedResponse() throws InterruptedException {

        System.out.println("checkUnexpectedExpectedResponse");
        template.sendBody("activemq:queue:testQueue", validMQMessage);
        String response = consumer.receiveBody("activemq:queue:myQueue", String.class);
        System.out.println("checkUnexpectedResponse The response is : " + response);
        assertNotEquals(expectedFraudMessage, response);
    }

    @Test
    @Order(3)
    @Description("This checks for Null message")
    public void notNullCheckForResponse() throws InterruptedException {

        System.out.println("notNullCheckForResponse");
        template.sendBody("activemq:queue:testQueue", fraudulentMessage);
        System.out.println("after template.sendBody ");
        String response = consumer.receiveBody("activemq:queue:myQueue", String.class);
        System.out.println("The response in notNullCheckForResponse : " + response);
        assertNotNull(response);
    }


}
