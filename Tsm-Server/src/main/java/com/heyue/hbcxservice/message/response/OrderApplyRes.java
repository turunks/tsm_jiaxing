package com.heyue.hbcxservice.message.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderApplyRes {
	/**
	 * 业务订单号（由tsm生成与维护）
	 */
	private String serviceOrderId;

	/**
	 * 支付参数
	 */
	private String payparm;

	/**
	 * 订单生成时间,格式yyyyMMddHHmmss
	 */
	private String orderTime;

	/**
	 * 可退金额（以分为单位）
	 * 当前字段只适合于好卡删卡
	 */
	private String amount;
}
