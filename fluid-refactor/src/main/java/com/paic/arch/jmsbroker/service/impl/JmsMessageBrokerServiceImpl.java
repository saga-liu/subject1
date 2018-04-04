package com.paic.arch.jmsbroker.service.impl;

import static org.slf4j.LoggerFactory.getLogger;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.broker.region.DestinationStatistics;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import com.paic.arch.jmsbroker.factory.JmsMessageBrokerFactory;
import com.paic.arch.jmsbroker.service.JmsMessageBrokerService;

/**
 * 
 * @author saga
 * @description:activemq connection管理类信息
 * @project_name:com.paic.arch.jmsbroker.service.impl
 * @file_name:JmsMessageBrokerServiceImpl.java
 * @date:2018年4月4日 下午5:49:20
 */
@Service
public class JmsMessageBrokerServiceImpl implements JmsMessageBrokerService {
	private static final Logger logger = getLogger(JmsMessageBrokerServiceImpl.class);

	private JmsMessageBrokerFactory jmsBroker;

	private Connection connection;

	private Session session;

	public JmsMessageBrokerServiceImpl(JmsMessageBrokerFactory jmsBroker) {
		this.jmsBroker = jmsBroker;
	}

	// 初始化连接
	private void init() {
		try {
			connection = jmsBroker.getConnectionFactory().createConnection();
			connection.start();
		} catch (JMSException jmse) {
			logger.error("failed to create connection to {}", jmsBroker.getBrokerUrl());
			throw new IllegalStateException(jmse);
		}

		try {
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		} catch (JMSException jmse) {
			logger.error("Failed to create session on connection {}", connection);
			throw new IllegalStateException(jmse);
		}
	}

	// 关闭连接
	private void close() {
		if (session != null) {
			try {
				session.close();
			} catch (JMSException jmse) {
				logger.warn("Failed to close session");
				throw new IllegalStateException(jmse);
			}
		}
		if (connection != null) {
			try {
				connection.close();
			} catch (JMSException jmse) {
				logger.warn("Failed to close connection to broker");
				throw new IllegalStateException(jmse);
			}
		}
	}

	public void bindToBrokerAtUrl(String aBrokerUrl) {
		jmsBroker.setBrokerUrl(aBrokerUrl);
	}

	public String getBrokerUrl() {
		return jmsBroker.getBrokerUrl();
	}

	public void sendMessage(String aDestinationName, String aMessage) {
		init();
		try {
			// 创建队列
			Queue queue = session.createQueue(aDestinationName);
			MessageProducer producer = session.createProducer(queue);
			// 发送消息
			producer.send(session.createTextMessage(aMessage));
			producer.close();
		} catch (Exception e) {
			logger.warn("Failed to send message");
			throw new IllegalStateException(e.getMessage());
		} finally {
			close();
		}
	}

	public String getMessage(String aDestinationName, long aTime) {
		init();
		String msg;
		try {
			Queue queue = session.createQueue(aDestinationName);
			MessageConsumer consumer = session.createConsumer(queue);
			Message message = consumer.receive(aTime);
			consumer.close();
			if (message != null) {
				msg = ((TextMessage) message).getText();
			} else {
				throw new NoMessageReceivedException(
						String.format("No messages received from the broker within the %d timeout", aTime));
			}
		} catch (JMSException jmse) {
			logger.warn("Failed to receive message");
			throw new IllegalStateException(jmse);
		} finally {
			close();
		}
		return msg;
	}

	public long getMessageCount(String aDestinationName) {
		long count = 0;
		try {
			count = getDestinationStatisticsFor(aDestinationName).getMessages().getCount();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count;
	}

	public void startBroker() {
		try {
			jmsBroker.setBrokerService(new BrokerService());
			jmsBroker.getBrokerService().setPersistent(false);
			jmsBroker.getBrokerService().addConnector(jmsBroker.getBrokerUrl());
			jmsBroker.getBrokerService().start();
		} catch (Exception e) {
			logger.warn("Failed to add Connector to broker");
			e.printStackTrace();
		}
	}

	public void stopBroker() {
		if (jmsBroker.getBrokerService() != null) {
			try {
				jmsBroker.getBrokerService().stop();
				jmsBroker.getBrokerService().waitUntilStopped();
			} catch (Exception e) {
				logger.warn("annot stop the broker");
				throw new IllegalStateException(e.getMessage());
			}
		}
	}

	@SuppressWarnings("serial")
	public class NoMessageReceivedException extends RuntimeException {
		public NoMessageReceivedException(String reason) {
			super(reason);
		}
	}

	private DestinationStatistics getDestinationStatisticsFor(String aDestinationName) throws Exception {
		Broker regionBroker = jmsBroker.getBrokerService().getRegionBroker();
		for (org.apache.activemq.broker.region.Destination destination : regionBroker.getDestinationMap().values()) {
			if (destination.getName().equals(aDestinationName)) {
				return destination.getDestinationStatistics();
			}
		}
		throw new IllegalStateException(String.format("Destination %s does not exist on broker at %s", aDestinationName,
				jmsBroker.getBrokerUrl()));
	}

	public boolean isEmptyQueueAt(String aDestinationName) {
		long count = 0;
		try {
			count = getEnqueuedMessageCountAt(aDestinationName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return count == 0;
	}

	public long getEnqueuedMessageCountAt(String aDestinationName) throws Exception {
		return getDestinationStatisticsFor(aDestinationName).getMessages().getCount();
	}
}