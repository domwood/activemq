package org.apache.activemq.junit;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(EmbeddedActiveMQBroker.class)
class ActiveMQDynamicQueueSenderResourceTest {

    @RegisterExtension
    public ActiveMQQueueReceiverResource receiverResource = new ActiveMQQueueReceiverResource("embeddedTestQueue");

    @RegisterExtension
    public ActiveMQDynamicQueueSenderResource senderResource = new ActiveMQDynamicQueueSenderResource();

    @Test
    void testSendAndReceive() throws JMSException {
        String testMessage = "Test Message";
        senderResource.sendMessage("embeddedTestQueue", testMessage);
        TextMessage textMessage = receiverResource.receiveTextMessage();
        assertEquals(testMessage, textMessage.getText());
    }

    @RepeatedTest(3)
    void testSendAndReceiveIsRepeatable(RepetitionInfo repetitionInfo) throws JMSException {
        String testMessage = String.format("Test Message %d", repetitionInfo.getCurrentRepetition());
        senderResource.sendMessage("embeddedTestQueue", testMessage);
        TextMessage textMessage = receiverResource.receiveTextMessage();
        assertEquals(testMessage, textMessage.getText());
    }
}
