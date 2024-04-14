package com.hmdp.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.CACHE_SHOP_TYPE_KEY;
import static com.hmdp.utils.RedisConstants.CACHE_SHOP_TYPE_TTL;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result queryTypeList() {
        String key = CACHE_SHOP_TYPE_KEY + "shoptypelist";
        List<String> typeJson = stringRedisTemplate.opsForList().range(key, 0, 9);
        log.debug("==> Redis get: " + typeJson);
        if (!typeJson.isEmpty()) {
            List<ShopType> typeList = new ArrayList<>();
            for (String s : typeJson) {
                ShopType shopType = JSONUtil.toBean(s, ShopType.class);
                typeList.add(shopType);
            }
            return Result.ok(typeList, (long) typeList.size());
        }
        List<ShopType> typeList = query().orderByAsc("sort").list().subList(0,10);
        if (typeList.isEmpty()) {
            return Result.fail("SHOPTYPE null");
        }

        for (ShopType shopType : typeList) {
            String s = JSONUtil.toJsonStr(shopType);
            typeJson.add(s);
        }

        stringRedisTemplate.opsForList().rightPushAll(key, typeJson);
        stringRedisTemplate.expire(key,CACHE_SHOP_TYPE_TTL, TimeUnit.MINUTES);
        log.debug("<== Redis set: " + key + " ," + CACHE_SHOP_TYPE_TTL);
        return Result.ok(typeList, (long) typeList.size());
    }
}
