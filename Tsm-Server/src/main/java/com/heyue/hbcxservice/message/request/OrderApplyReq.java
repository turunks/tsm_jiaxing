package com.heyue.hbcxservice.message.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderApplyReq {
	/**
	 * 安全模块标识
	 */
	@NotBlank(message = "安全模块标识不能为空")
	private String seid;

	/**
	 * 应用AID
	 */
	@NotBlank(message = "应用AID不能为空")
	private String appId;

	/**
	 * 商户号
	 */
	@NotBlank(message = "商户号不能为空")
	private String merchantNo;

	/**
	 * 城市代码
	 */
	@NotBlank(message = "城市代码不能为空")
	private String cityCode;

	/**
	 * 地区代码
	 */
	@NotBlank(message = "地区代码不能为空")
	private String areaCode;

	/**
	 * 卡种类型
	 */
	@NotBlank(message = "卡种类型不能为空")
	private String cardSpecies;

	/**
	 * 一卡通卡号
	 * 圈存、退款相关操作时，必填
	 */
	private String cardNo;

	/**
	 * tsm用户ID
	 */
	@NotBlank(message = "tsm用户ID不能为空")
	private String userId;

	/**
	 * 业务订单类型
	 * 00：支付+开卡
	 * 01：支付+开卡+圈存
	 * 02：支付+圈存[订单类型应与订单通知接口中一致]
	 * 03：应用迁移云备份
	 * 04：应用恢复
	 * 05：删卡+退款（好卡）
	 * 06：维修退款（坏卡）
	 */
	@NotBlank(message = "业务订单类型不能为空")
	private String serviceType;

	/**
	 * 支付渠道、渠道编号：
	 * 01：银联
	 * 02：支付宝
	 * 03：微信
	 * 04：和包支付
	 */
	@NotBlank(message = "支付渠道不能为空")
	private String payChannel;

	/**
	 * 交易总金额（单位分）。
	 */
	@NotBlank(message = "交易总金额不能为空")
	private String amount;

	/**
	 * 用户选择金额（单位分）：
	 * 1、当业务订单类型为00时，填卡当前余额。
	 * 2、当业务订单类型为02时，填用户选择充值金额。
	 * 3、当业务订单类型为06时，填卡当前余额。
	 * 4、其他业务订单类型时，无需传输。
	 */
	private String userSelCount;


	/**
	 * 单次活动金额（单位分）
	 * 1、当业务订单类型为02时，填优惠金额。
	 * 2、业务订单类型为其他时，无需传输。
	 */
	private String marketAmount;

	/**
	 * 活动编号
	 */
	private String marketNo;

	/**
	 * 优惠出资方：
	 * 00：中交金卡
	 * 01：中移金科（和包app业务活动出资方）
	 */
	private String marketOrg;

	/**
	 * 活动累计金额（单位分）：
	 * 业务订单类型为00、02时，填历史累计活动金额。
	 */
	private String cumAmount;

	/**
	 * 发卡机构码
	 */
	@NotBlank(message = "发卡机构码不能为空")
	private String cardIssCode;

	/**
	 * 充值选择金额
	 */
	@NotBlank(message = "充值选择金额不能为空")
	private String topUpAmount;

	/**
	 * 开卡费(分)
	 */
	private String cardPrice;
}
