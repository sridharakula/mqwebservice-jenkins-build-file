package com.mq.route;

import com.mq.domain.ParseException;
import com.mq.domain.ValidationException;
import com.mq.processor.ProcessBean;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;


/*
    @author Sridhar Akula
    This class is to build the Route to process the message from JMS Queue.
*/

public class MessageRouter extends RouteBuilder {

    /*
    This configure method core function of Route Builder
    to add the different Camel routes.

    For our POC we are reading message from Active MQ,
    then routing to Processor bean end point to parse the message
    and validate message.

     Based on the outcome of the validation message, we will route the message
     to the webservice endpoint.

     */



    public void configure() throws Exception {
        log.info("--------Started the route.----------");

        onException(ParseException.class)
                .log(LoggingLevel.ERROR,"Parsed exception");
        onException(ValidationException.class)
                .log(LoggingLevel.ERROR,"Validation exception");
        onException(RuntimeException.class)
                .log(LoggingLevel.ERROR,"Runtime exception");
        onException(Exception.class)
                .log(LoggingLevel.ERROR,"Generic Exception ");

        from("activemq:queue:testQueue")
                .to("log:?level=INFO&showBody=true")
                .choice()
                .when(simple("${body} != null"))
                .process(new ProcessBean())
                .to("log:?level=INFO&showBody=true")
                .to("activemq:queue:myQueue")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setHeader(Exchange.HTTP_URI, simple("http://localhost:9090/message/webapi/processmessage/${body}"))
                .choice()
                .when(body().contains("Suspicious shipment"))
                .to("http://localhost:9090/message/webapi/processmessage/${body}")
                .log("Web Service Response: ${body}") .end()
                .end()
                .end();

/*
        from("direct:restCall")
                .to("log:?level=INFO&showBody=true")
                .setHeader(Exchange.HTTP_METHOD, constant("GET"))
                .setHeader(Exchange.HTTP_URI, simple("https://restcountries.eu/rest/v2/alpha/${body}"))
                //.setHeader(Exchange.HTTP_URI, simple("https://restcountries.eu/rest/v2/alpha/USA"))
                .to("https://restcountries.eu/rest/v2/alpha/${body}")
                //.to("https://restcountries.eu/rest/v2/alpha/USA")
                .to("log:?level=INFO&showBody=true");
*/
        log.info("--------Ended the route.----------");
    }
}