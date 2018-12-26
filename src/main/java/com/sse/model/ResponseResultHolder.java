package com.sse.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ZHAOPENGCHENG
 * @email
 * @date 2018-12-13 20:48
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseResultHolder<T> {
    T result;
    private ResponseError error;

    public static class ResponseError {
        private Integer code;
        private String message;

        public ResponseError(Integer code, String message) {
            this.code = code;
            this.message = message;
        }

        public ResponseError() {
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return "Error[" +
                    "code=" + code +
                    ", message='" + message + '\'' +
                    ']';
        }
    }

}
