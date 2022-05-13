package com.heyue.hbcxservice.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PayOrderRes {
	/**
	 * 业务订单号（由tsm生成与维护）
	 */
	private String serviceOrderId;

	/**
	 * 订单类型: 00：支付+开卡  01：支付+开卡+圈存  02：支付+圈存
	 */
	private String orderType;

	/**
	 * 卡应用序列号
	 */
	private String cardNo;

	/**
	 * 交易金额（以分为单位）
	 */
	private String amount;

	/**
	 * 支付结果: 01：支付成功 02：支付失败  03：支付中
	 */
	private String payRet;

	/**
	 * 订单生成时间,格式yyyyMMddHHmmss
	 */
	private String creatTime;

	/**
	 * 订单支付时间,格式yyyyMMddHHmmss
	 */
	private String payDate;
}
