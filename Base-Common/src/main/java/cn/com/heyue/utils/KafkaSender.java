package cn.com.heyue.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ：yinbeng
 * @date ：Created in 2021-03-08 10:10
 * @description：xxx
 */
@Component
@Slf4j
public class KafkaSender {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    //发送消息方法
    public void send(String topic,String message) {
        log.info("+++++++++++++++++++++  KafkaSender topic = {} message = {}", topic,message);
        kafkaTemplate.send(topic, message);
    }
}
