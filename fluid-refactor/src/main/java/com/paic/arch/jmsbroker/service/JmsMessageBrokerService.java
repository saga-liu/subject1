package com.paic.arch.jmsbroker.service;

/**
 * 
 * @author saga
 * @description:自定义接口
 * @project_name:com.paic.arch.jmsbroker.service
 * @file_name:JmsMessageBrokerService.java
 * @date:2018年4月4日 下午5:46:59
 */
public interface JmsMessageBrokerService {
	/**
	 * 
	 * @author saga
	 * @description:绑定broker
	 * @param aBrokerUrl
	 */
	public void bindToBrokerAtUrl(String aBrokerUrl);
	/**
	 * 
	 * @author saga
	 * @description:获取url
	 * @return
	 */
	public String getBrokerUrl();
	/**
	 * 
	 * @author saga
	 * @description:消息发送
	 * @param aDestinationName
	 * @param aMessage
	 */
	public void sendMessage(String aDestinationName,String aMessage);
	/**
	 * 
	 * @author saga
	 * @description:获取消息
	 * @param aDestinationName
	 * @param aTime
	 * @return
	 */
	public String getMessage(String aDestinationName,long aTime);
	/**
	 * 
	 * @author saga
	 * @description:获取消息条数
	 * @param aDestinationName
	 * @return
	 */
	public long getMessageCount(String aDestinationName);
	/**
	 * 
	 * @author saga
	 * @description:判断队列是否为空
	 * @param aDestinationName
	 * @return
	 */
	public boolean isEmptyQueueAt(String aDestinationName);
	/**
	 * 
	 * @author saga
	 * @description:启动broker
	 * @date 2018年4月4日 下午5:56:29
	 */
	public void startBroker();
	/**
	 * 
	 * @author saga
	 * @description:停止broker
	 * @date 2018年4月4日 下午5:56:38
	 */
	public void stopBroker();
		
}