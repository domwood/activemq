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
class ActiveMQTopicResourceTest {

    @RegisterExtension
    public ActiveMQTopicPublisherResource publisherResource = new ActiveMQTopicPublisherResource("embeddedTestTopic");

    @RegisterExtension
    public ActiveMQTopicSubscriberResource subscriberResource = new ActiveMQTopicSubscriberResource("embeddedTestTopic");

    @Test
    void testSubscriberDestinationIsTopic() {
        assertTrue(subscriberResource.destination.isTopic());
    }

    @Test
    void testPublisherDestinationIsTopic() {
        assertTrue(publisherResource.destination.isTopic());
    }

    @Test
    void testSendAndReceive() throws JMSException {
        String testMessage = "Test Message";
        publisherResource.sendMessage(testMessage);
        TextMessage textMessage = subscriberResource.receiveTextMessage();
        assertEquals(testMessage, textMessage.getText());
    }

    @RepeatedTest(3)
    void testSendAndReceiveIsRepeatable(RepetitionInfo repetitionInfo) throws JMSException {
        String testMessage = String.format("Test Message %d", repetitionInfo.getCurrentRepetition());
        publisherResource.sendMessage(testMessage);
        TextMessage textMessage = subscriberResource.receiveTextMessage();
        assertEquals(testMessage, textMessage.getText());
    }

}
