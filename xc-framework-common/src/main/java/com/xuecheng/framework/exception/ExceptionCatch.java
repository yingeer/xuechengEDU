package com.xuecheng.framework.exception;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.model.response.ResultCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionCatch {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionCatch.class);

    // 建立一个异常和代号的映射
    private static ImmutableMap<Class<? extends Runnable>, ResultCode> EXCEPTIONS;

    private static ImmutableMap.Builder<Class<? extends Runnable>, ResultCode> builder = ImmutableMap.builder();


    /**
     * 捕获自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseResult customException(CustomException e) {
        LOGGER.error("catch exception: {}\r\nexception: ", e.getMessage(), e);
        ResponseResult responseResult = new ResponseResult(e.getResultCode());
        return responseResult;
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public ResponseResult exceptionOfOther(Exception e) {
        //记录日志
        LOGGER.error("catch exception:{}", e.getMessage());

        if (EXCEPTIONS == null) {
            EXCEPTIONS = builder.build();
        }
        final ResultCode resultCode = EXCEPTIONS.get(e.getClass());
        if (resultCode != null) {
            return new ResponseResult(resultCode);
        }
        return new ResponseResult(CommonCode.SERVER_ERROR);
    }
}
