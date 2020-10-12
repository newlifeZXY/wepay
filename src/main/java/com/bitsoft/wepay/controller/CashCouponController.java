package com.bitsoft.wepay.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/cashCoupon")
public class CashCouponController {
    private Logger log = LoggerFactory.getLogger(CashCouponController.class);

    @PostMapping(value = "/useEventNotify.json")
    public String useEventNotify(HttpServletRequest httpRequest, HttpServletResponse httpResponse,
                                 @Valid @RequestBody UseEventNotifyReq req) {
        ObjectMapper mapper = new ObjectMapper();
        String body = "";
        try {
            body = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(req);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //应答随机串
        String nonce = httpRequest.getHeader("Wechatpay-Nonce");
        //应答时间戳
        String timestamp = httpRequest.getHeader("Wechatpay-Timestamp");
        //平台证书序列号
        String certSerial = httpRequest.getHeader("Wechatpay-Serial");
        //签名值
        String signature = httpRequest.getHeader("Wechatpay-Signature");
        log.info("请求对象:{},Wechatpay-Nonce:{},Wechatpay-Timestamp:{},Wechatpay-Serial:{},Wechatpay-Signature:{}", body, nonce, timestamp, certSerial,
                signature);
        return "SUCCESS";

    }
}
