package com.test.mqservice;

import com.mq.launcher.route.MessageRouter;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import org.springframework.core.annotation.Order;

public class MessageRouterValidResponseTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new MessageRouter();

    }

    String validMQMessage = "GZXFRTJ675FTRHJJJ87zyxtBig Kumar           U000000000000017.450EURATCGB\n"
            + ":20:TR234565,Zu8765Z,Bhj876t\n"
            + ":32A:180123\n"
            + ":36:12";

    String inValidMQMessage = "GZXFRTJ675FTRHJJJ87zyxtRoy Kumar           U000000000000017.450EURATZAT\n"
            + ":20:TR234565,Zu8765Z,Bhj876t\n"
            + ":32A:180123,Ship dual FERT chem\n"
            + ":36:12\n";

    String expected = "Nothing found, all okay";
    //String response ="";

    @Test
    @Order(1)
    public void checkExpectedResponse() throws InterruptedException {

        System.out.println("checkExpectedResponse");
        template.sendBody("activemq:queue:testQueue", validMQMessage);
        String response = consumer.receiveBody("activemq:queue:myQueue", String.class);
        System.out.println("The response in checkExpectedResponse: " + response);
        assertEquals(expected, response);
    }

    @Test
    @Order(2)
    public void checkUnexpectedExpectedResponse() throws InterruptedException {

        System.out.println("checkUnexpectedExpectedResponse");
        template.sendBody("activemq:queue:testQueue", inValidMQMessage);
        String response = consumer.receiveBody("activemq:queue:myQueue", String.class);
        System.out.println("The response in checkUnexpectedExpectedResponse : " + response);
        assertNotEquals(expected, response);
    }

    @Test
    @Order(3)
    public void notNullCheckForResponse() throws InterruptedException {

        System.out.println("notNullCheckForResponse");
        template.sendBody("activemq:queue:testQueue", validMQMessage);
        System.out.println("after template.sendBody ");
        String response = consumer.receiveBody("activemq:queue:myQueue", String.class);
        System.out.println("The response in notNullCheckForResponse : " + response);
        assertNotNull(response);
    }



}
