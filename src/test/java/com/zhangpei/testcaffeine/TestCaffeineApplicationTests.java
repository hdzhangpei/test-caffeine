package com.zhangpei.testcaffeine;

import com.zhangpei.testcaffeine.provider.TestProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = TestCaffeineApplication.class)
public class TestCaffeineApplicationTests {

    @Resource
    private TestProvider testProvider;

    @Autowired
    private CacheManager caffeineCacheManager1;

    @Autowired
    private CacheManager caffeineCacheManager2;

    /**
     * 测试缓存
     * @throws InterruptedException
     */
    @Test
    public void test_cache() throws InterruptedException {
        System.out.println("首次查询，不过缓存");
        System.out.println(testProvider.test("test cache"));

        System.out.println("2s内第二次查询，经过缓存");
        System.out.println(testProvider.test("test cache"));

        System.out.println("休眠3s，缓存失效，重新查询");
        TimeUnit.SECONDS.sleep(3);
        System.out.println(testProvider.test("test cache"));

    }

    /**
     * 测试不同的key，不同的缓存
     */
    @Test
    public void test_cache_by_diff_key() {
        System.out.println("key 为 test cache");
        System.out.println(testProvider.test("test cache"));

        System.out.println("key 为 test cache1");
        System.out.println(testProvider.test("test cache1"));
    }

    /**
     * 测试清除缓存
     */
    @Test
    public void test_cache_clear() {
        System.out.println("首次查询");
        System.out.println(testProvider.test("test cache"));

        Objects.requireNonNull(caffeineCacheManager1.getCache("container1")).clear();

        System.out.println("手动清除缓存后，再次查询");
        System.out.println(testProvider.test("test cache"));
    }


    /**
     * 测试缓存
     * @throws InterruptedException
     */
    @Test
    public void test_cache_diff_container() throws InterruptedException {
        System.out.println("查询caffeineCacheManager1下container1容器");
        System.out.println(testProvider.test("test cache"));
        System.out.println("过caffeineCacheManager1下container1缓存");
        System.out.println(testProvider.test("test cache"));

        System.out.println("查询caffeineCacheManager2下container2容器");
        System.out.println(testProvider.test2("test cache"));
        System.out.println("过caffeineCacheManager2下container2缓存");
        System.out.println(testProvider.test2("test cache"));

    }

}
