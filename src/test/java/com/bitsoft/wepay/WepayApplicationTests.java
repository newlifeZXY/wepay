package com.bitsoft.wepay;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.management.Notification;

@SpringBootTest
@Slf4j
class WepayApplicationTests {

    @Test
    void contextLoads() throws JsonProcessingException {
        log.info("开始发送测试报文");

        UseEventNotifyReq.NotifyData notifyData = new UseEventNotifyReq.NotifyData();
        notifyData.setAlgorithm("AEAD_AES_256_GCM");
        notifyData.setNonce("lj2xCofleb24");
        notifyData.setOriginal_type("coupon");
        notifyData.setCiphertext("hnO6hIZQ23rKHZhAH3SU46de5HvHSIN55o2bLaIWOlZEJMT8Ev5RJPVdMFtQDG/OLxxjZmIO3bKlptF17nel9Ztx5sh/mvM3DMGRcJi/z4hrUQw+uFBhwiUlZOov0sc8iTwGurPaVF+5XGuoTACFpjU57IraOTf5OZtCwypjcROnM/lg1mjWwtH4B40UBIUHeCXDEuXqBXsNYrT9NxLBXggncIDFEILo8EFtxpfNmFmDSGbF11xuwnKBgxaLf8KHcHJsJAmImlKsjcFJIm3r8qI6g/293FD6YpXqgBvtE4zEVDVu26qlz/HuB+urYmSc8FdCZbXtKvT4Zwk7rJAow/drvRASk94bYCRyX5PoNZAjZzoCyWQyRU+nmJj41wxyZZBUl+DAe/YQ4BMatcMromz+5b8qPrUXCjpWwlhd6lBPIberolH6wXXjUP/4MXmBwYCdGyO3V1GJsbLRmS3+FW48EkO6fOijZr2FMradeG4NmpbXc6KoodHB7UHKaV+Uy01c8TloqbFlwTexMqyEnwiuo10rba6Xm3jrN1hg22YmGRP+jtI9iTRoUYiLv7FjQlbIYqS7D0RgNGs2eVUK8+8SBwpZ1irSyptD9EtYqQJWIJp9aqNLrzDf/PWidGfO+Gv+P+XZ67CAxQ9c+I8pNFjbaGR6ecT3rHhuht/gTVhpkWGcCM02N5CQ9+e4+JsKjN0+kxt7XZadEjopmubz7QLQti++C92kRrD+KmWvnjTE6BSzxZTbxGHJ8cjnKyZvERv3nN7zWiZFst2XCtDS");
        notifyData.setAssociated_data("coupon");

        UseEventNotifyReq req = new UseEventNotifyReq();
        req.setId("8b213d38-0654-5e3c-8133-abaffa2d68d9");
        req.setCreate_time("2020-10-10T21:13:08+08:00");
        req.setResource_type("encrypt-resource");
        req.setEvent_type("COUPON.USE");
        req.setSummary("\\344\\273\\243\\351\\207\\221\\345\\210\\270\\346\\240\\270\\351\\224\\200\\351\\200\\232\\347\\237\\245\\");
        req.setResource(notifyData);

        ObjectMapper objectMapper = new ObjectMapper();
        String data = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(req);
        log.info("data:{}", data);
        String url = "https://sme.test.webank.com/pmc-front/cashCoupon/useEventNotify.json";
        HttpsUtil.doPost(url, data);
    }


}
