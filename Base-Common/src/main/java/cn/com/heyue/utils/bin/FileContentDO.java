package cn.com.heyue.utils.bin;

import java.io.Serializable;
import java.util.Date;


/**
 * 文件编码存储
 * 
 * @author chglee
 * @email 1992lcg@163.com
 * @date 2020-11-07 16:38:51
 */
public class FileContentDO implements Serializable {
	private static final long serialVersionUID = 1L;
	//文件详情id
	private Integer fileDetailId;
	//文件id
	private Integer fileId;
	//卡片序列号
	private String seqNo;
	//发卡方标识
	private String cardSign;
	//应用类型标识
	private String appTypeSign;
	//发卡方应用版本
	private String cardAppVersion;
	//应用序列号
	private String appSerialNo;
	//应用启用日期
	private String appStartDate;
	//生效日期
	private String appValidDate;
	//发卡方自定义 FCI 数据
	private String cardCustomFci;
	//卡类型标识
	private String cardTypeSign;
	//本行职工标识
	private String employeeSign;
	//持卡人姓名
	private String carderName;
	//持卡人证件号码
	private String carderIdNo;
	//持卡人证件类型
	private String carderIdType;
	//国际代码
	private String internateCode;
	//省际代码
	private String provinceCode;
	//城市代码
	private String cityCode;
	//互通卡种   0000：非互通卡   0001：互通卡
	private String contactCardType;
	//卡种类型 01 普通卡
	private String cardType;
	//00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000  预留
	private String reserve;

	//国际密钥【用户卡主控密钥，用户卡主控密钥,用户卡维护密钥,用户卡应用主控密钥,用户卡应用维护密钥,消费密钥,充值密钥,TAC 密钥,用户卡应用维护密钥（应用锁定）,PIN 密钥,互通记录保护密钥-电子现金,互通记录保护密钥（现金备用）,用户卡应用维护密钥（应用解锁）,预留密钥 1,预留密钥 2,充值密钥 2（国际） 密文（3DES）+KCV（明文）。TK 加密  ***通过加密机还原出来的值4907494927122D2CEAFBDC29AC9DDE01】
	private String internationalKey;
	//国内密钥
	private String domesticKey;
	//卡片状态（入库、出库）【1入库2出库】
	private String cardStatus;
	//入库时间
	private Date inDepositTime;
	//出库时间
	private Date outDepositTime;

	public Integer getFileDetailId() {
		return fileDetailId;
	}

	public void setFileDetailId(Integer fileDetailId) {
		this.fileDetailId = fileDetailId;
	}

	/**
	 * 设置：文件id
	 */
	public void setFileId(Integer fileId) {
		this.fileId = fileId;
	}
	/**
	 * 获取：文件id
	 */
	public Integer getFileId() {
		return fileId;
	}
	/**
	 * 设置：卡片序列号
	 */
	public void setSeqNo(String seqNo) {
		this.seqNo = seqNo;
	}
	/**
	 * 获取：卡片序列号
	 */
	public String getSeqNo() {
		return seqNo;
	}
	/**
	 * 设置：发卡方标识
	 */
	public void setCardSign(String cardSign) {
		this.cardSign = cardSign;
	}
	/**
	 * 获取：发卡方标识
	 */
	public String getCardSign() {
		return cardSign;
	}
	/**
	 * 设置：应用类型标识
	 */
	public void setAppTypeSign(String appTypeSign) {
		this.appTypeSign = appTypeSign;
	}
	/**
	 * 获取：应用类型标识
	 */
	public String getAppTypeSign() {
		return appTypeSign;
	}
	/**
	 * 设置：发卡方应用版本
	 */
	public void setCardAppVersion(String cardAppVersion) {
		this.cardAppVersion = cardAppVersion;
	}
	/**
	 * 获取：发卡方应用版本
	 */
	public String getCardAppVersion() {
		return cardAppVersion;
	}
	/**
	 * 设置：应用序列号
	 */
	public void setAppSerialNo(String appSerialNo) {
		this.appSerialNo = appSerialNo;
	}
	/**
	 * 获取：应用序列号
	 */
	public String getAppSerialNo() {
		return appSerialNo;
	}
	/**
	 * 设置：应用启用日期
	 */
	public void setAppStartDate(String appStartDate) {
		this.appStartDate = appStartDate;
	}
	/**
	 * 获取：应用启用日期
	 */
	public String getAppStartDate() {
		return appStartDate;
	}
	/**
	 * 设置：
	 */
	public void setAppValidDate(String appValidDate) {
		this.appValidDate = appValidDate;
	}
	/**
	 * 获取：
	 */
	public String getAppValidDate() {
		return appValidDate;
	}
	/**
	 * 设置：发卡方自定义 FCI 数据
	 */
	public void setCardCustomFci(String cardCustomFci) {
		this.cardCustomFci = cardCustomFci;
	}
	/**
	 * 获取：发卡方自定义 FCI 数据
	 */
	public String getCardCustomFci() {
		return cardCustomFci;
	}
	/**
	 * 设置：卡类型标识
	 */
	public void setCardTypeSign(String cardTypeSign) {
		this.cardTypeSign = cardTypeSign;
	}
	/**
	 * 获取：卡类型标识
	 */
	public String getCardTypeSign() {
		return cardTypeSign;
	}
	/**
	 * 设置：本行职工标识
	 */
	public void setEmployeeSign(String employeeSign) {
		this.employeeSign = employeeSign;
	}
	/**
	 * 获取：本行职工标识
	 */
	public String getEmployeeSign() {
		return employeeSign;
	}
	/**
	 * 设置：持卡人姓名
	 */
	public void setCarderName(String carderName) {
		this.carderName = carderName;
	}
	/**
	 * 获取：持卡人姓名
	 */
	public String getCarderName() {
		return carderName;
	}
	/**
	 * 设置：持卡人证件号码
	 */
	public void setCarderIdNo(String carderIdNo) {
		this.carderIdNo = carderIdNo;
	}
	/**
	 * 获取：持卡人证件号码
	 */
	public String getCarderIdNo() {
		return carderIdNo;
	}
	/**
	 * 设置：持卡人证件类型
	 */
	public void setCarderIdType(String carderIdType) {
		this.carderIdType = carderIdType;
	}
	/**
	 * 获取：持卡人证件类型
	 */
	public String getCarderIdType() {
		return carderIdType;
	}
	/**
	 * 设置：国际代码
	 */
	public void setInternateCode(String internateCode) {
		this.internateCode = internateCode;
	}
	/**
	 * 获取：国际代码
	 */
	public String getInternateCode() {
		return internateCode;
	}
	/**
	 * 设置：省际代码
	 */
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	/**
	 * 获取：省际代码
	 */
	public String getProvinceCode() {
		return provinceCode;
	}
	/**
	 * 设置：城市代码
	 */
	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}
	/**
	 * 获取：城市代码
	 */
	public String getCityCode() {
		return cityCode;
	}
	/**
	 * 设置：互通卡种   0000：非互通卡   0001：互通卡
	 */
	public void setContactCardType(String contactCardType) {
		this.contactCardType = contactCardType;
	}
	/**
	 * 获取：互通卡种   0000：非互通卡   0001：互通卡
	 */
	public String getContactCardType() {
		return contactCardType;
	}
	/**
	 * 设置：卡种类型 01 普通卡
	 */
	public void setCardType(String cardType) {
		this.cardType = cardType;
	}
	/**
	 * 获取：卡种类型 01 普通卡
	 */
	public String getCardType() {
		return cardType;
	}
	/**
	 * 设置：国际密钥【用户卡主控密钥，用户卡主控密钥,用户卡维护密钥,用户卡应用主控密钥,用户卡应用维护密钥,消费密钥,充值密钥,TAC 密钥,用户卡应用维护密钥（应用锁定）,PIN 密钥,互通记录保护密钥-电子现金,互通记录保护密钥（现金备用）,用户卡应用维护密钥（应用解锁）,预留密钥 1,预留密钥 2,充值密钥 2（国际） 密文（3DES）+KCV（明文）。TK 加密  ***通过加密机还原出来的值4907494927122D2CEAFBDC29AC9DDE01】
	 */
	public void setInternationalKey(String internationalKey) {
		this.internationalKey = internationalKey;
	}
	/**
	 * 获取：国际密钥【用户卡主控密钥，用户卡主控密钥,用户卡维护密钥,用户卡应用主控密钥,用户卡应用维护密钥,消费密钥,充值密钥,TAC 密钥,用户卡应用维护密钥（应用锁定）,PIN 密钥,互通记录保护密钥-电子现金,互通记录保护密钥（现金备用）,用户卡应用维护密钥（应用解锁）,预留密钥 1,预留密钥 2,充值密钥 2（国际） 密文（3DES）+KCV（明文）。TK 加密  ***通过加密机还原出来的值4907494927122D2CEAFBDC29AC9DDE01】
	 */
	public String getInternationalKey() {
		return internationalKey;
	}
	/**
	 * 设置：国内密钥
	 */
	public void setDomesticKey(String domesticKey) {
		this.domesticKey = domesticKey;
	}
	/**
	 * 获取：国内密钥
	 */
	public String getDomesticKey() {
		return domesticKey;
	}
	/**
	 * 设置：卡片状态（入库、出库）【1入库2出库】
	 */
	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}
	/**
	 * 获取：卡片状态（入库、出库）【1入库2出库】
	 */
	public String getCardStatus() {
		return cardStatus;
	}
	/**
	 * 设置：入库时间
	 */
	public void setInDepositTime(Date inDepositTime) {
		this.inDepositTime = inDepositTime;
	}
	/**
	 * 获取：入库时间
	 */
	public Date getInDepositTime() {
		return inDepositTime;
	}
	/**
	 * 设置：出库时间
	 */
	public void setOutDepositTime(Date outDepositTime) {
		this.outDepositTime = outDepositTime;
	}
	/**
	 * 获取：出库时间
	 */
	public Date getOutDepositTime() {
		return outDepositTime;
	}
	//预留
	public String getReserve() {
		return reserve;
	}
	//预留
	public void setReserve(String reserve) {
		this.reserve = reserve;
	}
}
