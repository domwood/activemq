package org.apache.activemq.junit;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.RegisterExtension;

import javax.jms.JMSException;
import javax.jms.TextMessage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(EmbeddedActiveMQBroker.class)
class ActiveMQQueueResourceTest {

    @RegisterExtension
    public ActiveMQQueueSenderResource senderResource = new ActiveMQQueueSenderResource("embeddedTestQueue");

    @RegisterExtension
    public ActiveMQQueueReceiverResource receiverResource = new ActiveMQQueueReceiverResource("embeddedTestQueue");

    @Test
    void testSenderDestinationIsQueue() {
        assertTrue(senderResource.destination.isQueue());
    }

    @Test
    void testReceiverDestinationIsQueue() {
        assertTrue(receiverResource.destination.isQueue());
    }

    @Test
    void testSendAndReceive() throws JMSException {
        String testMessage = "Test Message";
        senderResource.sendMessage(testMessage);
        TextMessage textMessage = receiverResource.receiveTextMessage();
        assertEquals(testMessage, textMessage.getText());
    }

    @RepeatedTest(3)
    void testSendAndReceiveIsRepeatable(RepetitionInfo repetitionInfo) throws JMSException {
        String testMessage = String.format("Test Message %d", repetitionInfo.getCurrentRepetition());
        senderResource.sendMessage(testMessage);
        TextMessage textMessage = receiverResource.receiveTextMessage();
        assertEquals(testMessage, textMessage.getText());
    }

}
