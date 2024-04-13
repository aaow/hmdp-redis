package com.hmdp.dto;

import lombok.Data;

import java.util.List;

/**
 * @className: ScrollResult
 * @Description: TODO
 * @version: v1.８.0
 * @author: chz
 * @date: 2024/4/13 4:45
 **/
@Data
public class ScrollResult {
    private List<?> list;
    private Long minTime;
    private Integer offset;
}
