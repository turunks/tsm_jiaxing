package com.heyue.card.message.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 作者：sunwei
 * <p>
 * 时间：2022/5/28 17:01
 * <p>
 * 注释：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardDetailReq {

    private String cityCode; // 城市代码

    private String areaName; // 城市名称

}
