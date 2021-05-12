package com.zhangpei.testcaffeine.provider;

import com.zhangpei.testcaffeine.service.TestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhangpei
 * @description: TODO
 * @company 中国区销售一部
 * @created 2021/5/12
 */
@Service
public class TestProvider {
    @Resource
    private TestService testService;

    public String test(String str) {
        System.out.println("TestProvider test:" + str);

        return testService.test(str);
    }

    public String test2(String str) {
        System.out.println("TestProvider test2:" + str);

        return testService.test2(str);
    }
}
