package com.bfxx.controller;

import com.bfxx.common.CommonResult;
import com.bfxx.common.ResultCode;
import com.bfxx.pojo.EndNode;
import com.bfxx.services.EndpointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RequestMapping("/endnode")
@RestController
public class EndpointController {

    @Autowired
    EndpointService endpointService;

    @GetMapping("/all")
    public  CommonResult<ArrayList<EndNode>> getALLEndPointStatus(){
        return CommonResult.success(endpointService.getAllEndpoint());

    }

    @GetMapping(value = "/{id}")
    public  CommonResult<EndNode> getEndPointStatus(@PathVariable("id") String id){

        EndNode endpoint = endpointService.getEndpoint(id);
        if(endpoint == null){
            return CommonResult.success(endpoint,"此节点不存在");
        }
        if(!endpoint.getOnlineStatus()){
            return CommonResult.success(endpoint,"此节点未上线");
        }
        return CommonResult.success(endpoint,"此节点存在并上线");

    }


    @RestControllerAdvice
    public class AdviceController {

        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ExceptionHandler(value = Exception.class)
        public CommonResult exceptionHandler(Exception ex) {
            if (ex instanceof Exception) {
                return CommonResult.failed(ResultCode.BADREQUEST);
            }
            return CommonResult.failed();
        }
    }

//    @RestControllerAdvice
//    public class AdviceController2 {
//
//        @ResponseStatus(HttpStatus.NOT_FOUND)
//        @ExceptionHandler(value = Exception.class)
//        public CommonResult exceptionHandler(Exception ex) {
//            if (ex instanceof Exception) {
//                return CommonResult.failed(ResultCode.VALIDATE_FAILED);
//            }
//            return CommonResult.failed();
//        }
//    }






}
