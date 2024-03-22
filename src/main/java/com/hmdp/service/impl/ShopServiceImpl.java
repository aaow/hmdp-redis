package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hmdp.dto.Result;
import com.hmdp.entity.Shop;
import com.hmdp.mapper.ShopMapper;
import com.hmdp.service.IShopService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

import static com.hmdp.utils.RedisConstants.CACHE_SHOP_KEY;
import static com.hmdp.utils.RedisConstants.CACHE_SHOP_TTL;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopServiceImpl extends ServiceImpl<ShopMapper, Shop> implements IShopService {


    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public Result queryById(Long id) {
        String key = CACHE_SHOP_KEY + id;
        long ttl = CACHE_SHOP_TTL + (int)System.currentTimeMillis()%10;

        String shopJson = stringRedisTemplate.opsForValue().get(key);
        log.debug("==> Redis get: " + shopJson);
        if (StrUtil.isNotBlank(shopJson)) {
            Shop shop = JSONUtil.toBean(shopJson, Shop.class);
            return Result.ok(shop);
        }
        if (shopJson != null) {
            return Result.fail("shop 为空");
        }
        Shop shop = getById(id);
        if (shop == null) {
            stringRedisTemplate.opsForValue().set(key,"",ttl, TimeUnit.MINUTES);
            log.debug("<== Redis set: " + key + ttl);
            return Result.fail("shop null");
        }
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(shop),ttl, TimeUnit.MINUTES);
        log.debug("<== Redis set: " + key + ttl);
        return Result.ok(shop);
    }

    @Override
    public Result updateShopById(Shop shop) {
        Long id = shop.getId();
        if (id == null) {
            return Result.fail("shopid null");
        }
        updateById(shop);
        stringRedisTemplate.delete(CACHE_SHOP_KEY + id);
        log.debug("==> Redis del: " + CACHE_SHOP_KEY + id);
        return Result.ok();
    }
}
