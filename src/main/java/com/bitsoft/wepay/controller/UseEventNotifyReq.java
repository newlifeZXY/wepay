package com.bitsoft.wepay.controller;

import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 核销事件回调通知
 *
 * @Author: jameszhang
 * @Date 2020/8/27 21:16
 */
@Data
public class UseEventNotifyReq {

    @NotNull(message = "通知ID不能为空")
    private String id;
    @NotNull(message = "通知创建时间不能为空")
    private String create_time;
    @NotNull(message = "通知类型不能为空")
    private String event_type;
    @NotNull(message = "通知数据类型不能为空")
    private String resource_type;
    @NotNull(message = "回调摘要不能为空")
    private String summary;
    @NotNull(message = "通知数据不能为空")
    private NotifyData resource;

    @Data
    public static class NotifyData {

        private String algorithm;
        private String ciphertext;
        private String associated_data;
        private String nonce;
        private String original_type;
    }
}
