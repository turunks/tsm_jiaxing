package com.heyue.hbcxservice.message.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 作者：sunwei
 * <p>
 * 时间：2022/5/28 19:39
 * <p>
 * 注释：
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardNumRes {
    /**
     * 卡库存
     */
    private Integer count ;
}
