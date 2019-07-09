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
        System.out.println("ProcessBean:process:++++++++++++++Started the process++++++++++");
        String body= (String) exchange.getIn().getBody();
        System.out.println("ProcessBean:process:BODY in Process Bean: "+body);
        if(body!=null) {
            Transaction transactionDetails = new Transaction(body);
            transactionDetails.parse();
            transactionDetails.validate();
            newBody = transactionDetails.getRestMessage();
        }
        System.out.println("ProcessBean:process:new BODY in Process Bean:"+newBody);
        exchange.getIn().setBody(newBody);
        System.out.println("ProcessBean:process:++++++++++++++End the process++++++++++");
    }

}
