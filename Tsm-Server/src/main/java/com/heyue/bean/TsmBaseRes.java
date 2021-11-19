package com.heyue.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TsmBaseRes<T> {
    private String status; // 状态值
    private String message;// 信息
    private String action_id;// 操作流水号
    private T data;
    private String sign;// 网关签名

    public static <T> TsmBaseRes fail() {
        return new TsmBaseRes("500", "error", null, null, null);
    }
}
