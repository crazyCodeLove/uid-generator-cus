package com.sse.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-13 20:45
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestParamHolder<T> {
    private T param;
}
