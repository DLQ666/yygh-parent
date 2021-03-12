package com.dlq.yygh.hosp.controller.api;

import com.dlq.yygh.common.exception.YyghException;
import com.dlq.yygh.common.result.Result;
import com.dlq.yygh.common.result.ResultCodeEnum;
import com.dlq.yygh.helper.HttpRequestHelper;
import com.dlq.yygh.hosp.service.HospitalService;
import com.dlq.yygh.hosp.service.HospitalSetService;
import com.dlq.yygh.model.hosp.HospitalSet;
import com.dlq.yygh.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 *@program: yygh-parent
 *@description:
 *@author: Hasee
 *@create: 2021-03-12 17:10
 */
@RestController
@RequestMapping("/api/hosp")
public class ApiController {

    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private HospitalSetService hospitalSetService;

    /**
     * 上传医院接口
     */
    @PostMapping("/saveHospital")
    public Result saveHosp(HttpServletRequest request){
        //获取到传递过来的医院信息
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(requestMap);

        //获取医院传过来的签名，签名进行了MD5加密
        String hospSign = (String) paramMap.get("sign");

        String hoscode = (String) paramMap.get("hoscode");
        String signKey = hospitalSetService.getSignKey(hoscode);

        String signKeyMD5 = MD5.encrypt(signKey);
        if (signKey.equals(signKeyMD5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        hospitalService.save(paramMap);
        return Result.ok();
    }
}
