package com.idit.designer.common.config;

import com.idit.designer.common.constants.DesignerServiceConstants;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Queue;

//@Configuration
//@EnableJms
public class ActiveMQConfig {

//
//    @Value("${activemq.broker.url}")
//    private String brokerUrl;
//
//    @Bean
//    public Queue queue() {
//        return new ActiveMQQueue(DesignerServiceConstants.IDIT_DESIGN_TIME_QUEUE);
//    }
//
//    @Bean
//    public ActiveMQConnectionFactory activeMQConnectionFactory() {
//        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
//        activeMQConnectionFactory.setBrokerURL(brokerUrl);
//        return activeMQConnectionFactory;
//    }
//
//    @Bean
//    public JmsTemplate jmsTemplate() {
//        return new JmsTemplate(activeMQConnectionFactory());
//    }

}

