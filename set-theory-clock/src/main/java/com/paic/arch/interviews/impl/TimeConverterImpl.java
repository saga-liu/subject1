package com.paic.arch.interviews.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.paic.arch.interviews.TimeConverter;

/**
 * 
 * @author saga
 * @description:
 * @project_name:com.paic.arch.interviews.impl
 * @file_name:TimeConverterImpl.java
 * @date:2018年4月4日 下午3:48:08
 */
public class TimeConverterImpl implements TimeConverter {
	// 自定义一个时间对象访问
	private static Calendar datetime = Calendar.getInstance();
	// 自定义一个时间对象访问
	private static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	// 第一行长度
	private static final int FIRST_ROW_LENGTH = 4;
	// 第二行长度
	private static final int SECOND_ROW_LENGTH = 4;
	// 第三行长度
	private static final int THIRD_ROW_LENGTH = 11;
	// 第四行长度
	private static final int FOURTH_ROW_LENGTH = 4;
	// 空白符
	private static final String BLANK = "O";
	// 红色标识
	private static final String RED_SYMBOL = "R";
	// 黄色标识
	private static final String YELLOW_SYMBOL = "Y";
	// 换行符
	private static final String DISPLAY_SYBOL = "\n";

	public String convertTime(String aTime) {
		StringBuffer result = new StringBuffer();
		// 读取第一行每隔2秒闪烁一次的灯
		result.append(getHeadDisplay());
		result.append(DISPLAY_SYBOL);
		// 读取第二行5个小时基值的计数值
		result.append(getFirstRowDisplay());
		result.append(DISPLAY_SYBOL);
		// 读取第三行1小时基值的计数值
		result.append(getSecondRowDisplay());
		result.append(DISPLAY_SYBOL);
		// 读取第四行5秒基值的计数值
		result.append(getThirdRowDisplay());
		result.append(DISPLAY_SYBOL);
		// 读取第五行1秒基值的计数值
		result.append(getFourthRowDisplay());
		System.out.println(result);
		return result.toString();
	}

	public static void main(String[] args) {
		// 自定义一个时间对象访问
		TimeConverterImpl converterImpl = new TimeConverterImpl();
		converterImpl.convertTime(sdf.format(datetime.getTime()));
	}

	/**
	 * 
	 * @author saga
	 * @description:
	 * @return
	 */
	public String getHeadDisplay() {
		int seconds = datetime.get(Calendar.SECOND);
		return seconds % 2 > 0 ? RED_SYMBOL : BLANK;
	}

	/**
	 * 
	 * @author saga
	 * @description:
	 * @return
	 */
	public String getFirstRowDisplay() {
		return generateRow(Calendar.HOUR_OF_DAY, false, RED_SYMBOL, FIRST_ROW_LENGTH);
	}

	/**
	 * 
	 * @author saga
	 * @description:
	 * @return
	 */
	public String getSecondRowDisplay() {
		return generateRow(Calendar.HOUR_OF_DAY, true, RED_SYMBOL, SECOND_ROW_LENGTH);
	}

	/**
	 * 
	 * @author saga
	 * @description:
	 * @return
	 */
	public String getThirdRowDisplay() {
		return generateRow(Calendar.MINUTE, false, YELLOW_SYMBOL, THIRD_ROW_LENGTH, RED_SYMBOL, 3);
	}

	/**
	 * 
	 * @author saga
	 * @description:
	 * @return
	 */
	public String getFourthRowDisplay() {
		return generateRow(Calendar.MINUTE, true, YELLOW_SYMBOL, FOURTH_ROW_LENGTH);
	}

	/**
	 * 
	 * @author saga
	 * @description:
	 * @param calendarField
	 *            calendar对应的时间单位类型(例如:时,分,秒)
	 * @param isNeedHex
	 *            取整运算或取模设置(1.每5分钟/小时显示+1 2.不满5取模显示实际的分钟/小时计数)
	 * @param rowSymbol
	 *            是否需要变色处理
	 * @param rowLength
	 *            行数对应总长度,补O使用
	 * @return
	 */
	private String generateRow(int calendarField, boolean isNeedHex, String rowSymbol, int rowLength) {
		return generateRow(calendarField, isNeedHex, rowSymbol, rowLength, null, -1);
	}

	/**
	 * 
	 * @author saga
	 * @description:
	 * @param calendarField
	 * @param isNeedHex
	 * @param rowSymbol
	 * @param rowLength
	 * @param changeSymbol
	 *            第三行需要变色标识颜色
	 * @param step
	 *            需要处理变色对应的步长
	 * @return
	 */
	private String generateRow(int calendarField, boolean isNeedHex, String rowSymbol, int rowLength,
			String changeSymbol, int step) {
		StringBuffer row = new StringBuffer();
		// 获取时间单位值
		int unitTime = datetime.get(calendarField);
		// 判断是否需要进制运算,取整运算或取模设置,(1.每5分钟/小时显示+1 2.不满5取模显示实际的分钟/小时计数)
		unitTime = isNeedHex ? unitTime % 5 : unitTime / 5;
		// 判断是否是第三行的变色处理
		if (changeSymbol == null || step <= 0) {
			for (int i = 0; i < unitTime; i++) {
				row.append(rowSymbol);
			}
		} else {
			// 处理步长为step进行变色处理
			for (int i = 0; i < unitTime; i++) {
				if (i % step == 0) {
					row.append(changeSymbol);
				} else {
					row.append(rowSymbol);
				}

			}
		}
		// 根据长度不足的进行不为O操作
		for (int i = row.length(); i < rowLength; i++) {
			row.append(BLANK);
		}
		return row.toString();
	}

}
