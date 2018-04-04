package com.paic.arch.jmsbroker;

import static com.paic.arch.jmsbroker.SocketFinder.findNextAvailablePortBetween;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;

import com.paic.arch.jmsbroker.factory.JmsMessageBrokerFactory;
import com.paic.arch.jmsbroker.service.JmsMessageBrokerService;
import com.paic.arch.jmsbroker.service.impl.JmsMessageBrokerServiceImpl;

/**
 * 
 * @author saga
 * @description:设计模型值scp,一个类专注做一件事,提供面向接口服务化编程
 * @project_name:com.paic.arch.jmsbroker
 * @file_name:JmsMessageBrokerSupport.java
 * @date:2018年4月4日 下午5:52:11
 */
public class JmsMessageBrokerSupport {
	private static final Logger LOG = getLogger(JmsMessageBrokerSupport.class);
	private static final int ONE_SECOND = 1000;
	private static final int DEFAULT_RECEIVE_TIMEOUT = 10 * ONE_SECOND;
	public static final String DEFAULT_BROKER_URL_PREFIX = "tcp://localhost:";

	private JmsMessageBrokerService jmsService;

	private JmsMessageBrokerSupport(String aBrokerUrl) {
		jmsService = new JmsMessageBrokerServiceImpl(new JmsMessageBrokerFactory(aBrokerUrl));
	}

	public static JmsMessageBrokerSupport createARunningEmbeddedBrokerOnAvailablePort() throws Exception {
		return createARunningEmbeddedBrokerAt(DEFAULT_BROKER_URL_PREFIX + findNextAvailablePortBetween(41616, 50000));
	}

	public static JmsMessageBrokerSupport createARunningEmbeddedBrokerAt(String aBrokerUrl) throws Exception {
		LOG.debug("Creating a new broker at {}", aBrokerUrl);
		JmsMessageBrokerSupport broker = bindToBrokerAtUrl(aBrokerUrl);
		broker.jmsService.startBroker();
		return broker;
	}

	public static JmsMessageBrokerSupport bindToBrokerAtUrl(String aBrokerUrl) throws Exception {
		return new JmsMessageBrokerSupport(aBrokerUrl);
	}

	public void stopTheRunningBroker() throws Exception {
		jmsService.stopBroker();
	}

	public final JmsMessageBrokerSupport andThen() {
		return this;
	}

	public final String getBrokerUrl() {
		return jmsService.getBrokerUrl();
	}

	public JmsMessageBrokerSupport sendATextMessageToDestinationAt(String aDestinationName,
			final String aMessageToSend) {
		jmsService.sendMessage(aDestinationName, aMessageToSend);
		return this;
	}

	public String retrieveASingleMessageFromTheDestination(String aDestinationName) {
		return jmsService.getMessage(aDestinationName, DEFAULT_RECEIVE_TIMEOUT);
	}

	public String retrieveASingleMessageFromTheDestination(String aDestinationName, final int aTimeout) {
		return jmsService.getMessage(aDestinationName, aTimeout);
	}

	public long getEnqueuedMessageCountAt(String aDestinationName) throws Exception {
		return jmsService.getMessageCount(aDestinationName);
	}

	public boolean isEmptyQueueAt(String aDestinationName) throws Exception {
		return jmsService.isEmptyQueueAt(aDestinationName);
	}

}
