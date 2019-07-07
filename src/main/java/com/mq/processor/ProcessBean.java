package com.mq.processor;

import com.mq.domain.Transaction;
import org.apache.camel.Exchange;

/*
The Process Bean reads the transaction message from queue and
takes care of invoking the parse and validation of the message
by using Transaction class.
 */
public class ProcessBean implements org.apache.camel.Processor{


/*
    The process method reads message using Exchange,
    creates Transaction object - invokes the parse & validation methods of Transaction.
 */
    public void process(Exchange exchange) throws Exception {
        String newBody ="";
        System.out.println("++++++++++++++Started the process++++++++++");
        String body= (String) exchange.getIn().getBody();
        System.out.println("BODY in Process Bean: "+body);
        Transaction transactionDetails=new Transaction();
        transactionDetails.parse(body);
        newBody= transactionDetails.validate();
        System.out.println("new BODY in Process Bean:"+newBody);
        exchange.getIn().setBody(newBody);
        System.out.println("++++++++++++++End the process++++++++++");
    }

}
