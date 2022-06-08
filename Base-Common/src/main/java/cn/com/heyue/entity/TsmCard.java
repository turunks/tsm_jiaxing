package cn.com.heyue.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 作者：sunwei
 * <p>
 * 时间：2022/5/28 17:11
 * <p>
 * 注释：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TsmCard {
    /**
     * 城市代码
     */
    private String cityCode;
    /**
     * 城市名称
     */
    private String areaName;

    private static final long serialVersionUID = 1L;
}
