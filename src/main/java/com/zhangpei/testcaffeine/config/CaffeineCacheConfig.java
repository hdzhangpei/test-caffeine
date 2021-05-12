package com.zhangpei.testcaffeine.config;

import ch.qos.logback.classic.gaffer.PropertyUtil;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangpei
 * @description: TODO
 * @link https://www.cnblogs.com/fnlingnzb-learner/p/11025565.html
 * @created 2021/5/11
 */
@EnableCaching
@Configuration
public class CaffeineCacheConfig {

    /**
     * 创建基于Caffeine的Cache Manager
     *
     * @return
     */
    @Primary
    @Bean("caffeineCacheManager1")
    public CacheManager CaffeineCacheManager1() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        cacheManager.setCaffeine(Caffeine.newBuilder().recordStats()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .maximumSize(100)
//                .initialCapacity() // 初始的缓存空间大小
//                .maximumWeight() //缓存的最大权重
//                .expireAfterAccess() // 最后一次写入或访问后经过固定时间过期
//                .refreshAfterWrite() // 最后一次写入后经过固定时间过期
//                .weakKeys() //打开key的弱引用
//                .weakValues()// 打开value的弱引用
//                .softValues() // 打开value的软引用
//                .recordStats() //打开统计功能
        );
        /**
         * expireAfterWrite和expireAfterAccess同事存在时，以expireAfterWrite为准。
         * maximumSize和maximumWeight不可以同时使用
         * weakValues和softValues不可以同时使用
         */
        cacheManager.setCacheNames(Collections.singletonList("container1"));

        return cacheManager;
    }

    /**
     * 创建基于Caffeine的Cache Manager
     *
     * @return
     */
    @Bean("caffeineCacheManager2")
    public CacheManager CaffeineCacheManager2() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();

        cacheManager.setCaffeine(Caffeine.newBuilder().recordStats()
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .maximumSize(100));
        cacheManager.setCacheNames(Collections.singletonList("container2"));

        return cacheManager;
    }
}
