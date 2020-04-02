package com.xuecheng.manage_cms_client;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@SpringBootTest
@RunWith(SpringRunner.class)
public class RabbitMqTest {

    @Resource
    private Binding BINDING_QUEUE_INFORM_SMS;

    @Resource
    private Queue QUEUE_CMS_POSTPAGE;
    /**
     * 测试BINDING_QUEUE_INFORM_SMS方法中 @Qualifier注解是否能用
     */
    @Test
    public void testQualifier() {
        System.out.println("BINDING_QUEUE_INFORM_SMS对象: " +  BINDING_QUEUE_INFORM_SMS.toString());
    }
}
