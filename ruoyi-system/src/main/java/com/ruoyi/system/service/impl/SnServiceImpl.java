package com.ruoyi.system.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.Sn;
import com.ruoyi.system.mapper.SnMapper;
import com.ruoyi.system.service.ISnService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Date;

/**
 * 序列号Service业务层处理
 *
 * @author ruoyi
 * @date 2020-05-12
 */
@Service
public class SnServiceImpl implements ISnService, InitializingBean {

    /**
     * 商品编号生成器
     */
    private HiloOptimizer repairHiloOptimizer;

    @Autowired
    private SnMapper snMapper;

    @Value("${sn.repair.prefix}")
    private String repairPrefix;
    @Value("${sn.repair.maxLo}")
    private int repairMaxLo;


    /**
     * 初始化
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        repairHiloOptimizer = new HiloOptimizer(Sn.Type.REPAIR, repairPrefix, repairMaxLo);
    }

    /**
     * 生成序列号
     *
     * @param type 类型
     * @return 序列号
     */
    @Override
    public String generate(Sn.Type type) {
        Assert.notNull(type, "[Assertion failed] - type is required; it must not be null");

        switch (type) {
            case REPAIR:
                return repairHiloOptimizer.generate();
        }
        return null;
    }


    /**
     * 高低位算法生成器
     */
    private class HiloOptimizer {

        /**
         * 类型
         */
        private Sn.Type type;

        /**
         * 前缀
         */
        private String prefix;

        /**
         * 最大低位值
         */
        private int maxLo;

        /**
         * 低位值
         */
        private int lo;

        /**
         * 高位值
         */
        private long hi;

        /**
         * 末值
         */
        private long lastValue;

        /**
         * 构造方法
         *
         * @param type   类型
         * @param prefix 前缀
         * @param maxLo  最大低位值
         */
        HiloOptimizer(Sn.Type type, String prefix, int maxLo) {
            this.type = type;
            this.prefix = prefix != null ? prefix.replace("{", "${") : StringUtils.EMPTY;
            this.maxLo = maxLo;
            this.lo = maxLo + 1;
        }

        /**
         * 生成序列号
         *
         * @return 序列号
         */
        public synchronized String generate() {
            if (lo > maxLo) {
                lastValue = getLastValue(type);
                lo = lastValue == 0 ? 1 : 0;
                hi = lastValue * (maxLo + 1);
            }
            return DateUtil.format(new Date(), prefix) + (hi + lo++);
        }
    }

    /**
     * 获取末值
     *
     * @param type 类型
     * @return 末值
     */
    private long getLastValue(Sn.Type type) {
        Sn sn = snMapper.selectSnByType(type);
        long lastValue = sn.getLastValue();
        sn.setLastValue(lastValue + 1);
        sn.setUpdateTime(DateUtils.getNowDate());
        snMapper.updateSn(sn);
        return lastValue;
    }

}
