package com.paic.arch.jmsbroker.factory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;

/**
 * 
 * @author saga
 * @description:broker连接池工厂
 * @project_name:com.paic.arch.jmsbroker.factory
 * @file_name:JmsMessageBroker.java
 * @date:2018年4月4日 下午5:45:17
 */
public class JmsMessageBrokerFactory {
	
	private String brokerUrl;
	private BrokerService brokerService;
	private ActiveMQConnectionFactory connectionFactory;
	
	public JmsMessageBrokerFactory(String brokerUrl) {
		super();
		this.brokerUrl = brokerUrl;
		connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
	}
	
	public String getBrokerUrl() {
		return brokerUrl;
	}
	public void setBrokerUrl(String brokerUrl) {
		this.brokerUrl = brokerUrl;
	}
	public BrokerService getBrokerService() {
		return brokerService;
	}
	public void setBrokerService(BrokerService brokerService) {
		this.brokerService = brokerService;
	}
	public ActiveMQConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}
	public void setConnectionFactory(ActiveMQConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	

}