package com.hmdp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.dto.Result;
import com.hmdp.entity.VoucherOrder;

/**
 * @className: IVoucherOrderService
 * @Description: TODO
 * @version: v1.８.0
 * @author: chz
 * @date: 2024/3/30 5:15
 **/

public interface IVoucherOrderService extends IService<VoucherOrder> {
    Result seckillVoucher(Long voucherId);

    Result createVoucher(Long voucherId);
}
