package com.hmdp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hmdp.dto.Result;
import com.hmdp.entity.Follow;

/**
 * @className: IFollowService
 * @Description: TODO
 * @version: v1.８.0
 * @author: chz
 * @date: 2024/4/12 3:23
 **/

public interface IFollowService extends IService<Follow> {


    Result follow(Long followUserId, Boolean isFollow);

    Result isFollow(Long followUserId);
}
