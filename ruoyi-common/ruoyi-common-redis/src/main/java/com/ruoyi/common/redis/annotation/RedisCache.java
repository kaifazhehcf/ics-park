package com.ruoyi.common.redis.annotation;

import java.lang.annotation.*;

/**
 * Redis缓存
 *
 * @author jack
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisCache {
    /**
     * 键名
     *
     * @return
     */
    String key() default "";

    /**
     * 主键
     *
     * @return
     * @author zmr
     */
    String fieldKey();

    /**
     * 过期时间
     *
     * @return
     */
    long expired() default 3600;

    /**
     * 是否为查询操作
     * 如果为写入数据库的操作，该值需置为 false
     *
     * @return
     */
    boolean read() default true;
}