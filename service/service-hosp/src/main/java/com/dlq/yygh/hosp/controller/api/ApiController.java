package com.dlq.yygh.hosp.controller.api;

import com.dlq.yygh.common.exception.YyghException;
import com.dlq.yygh.common.result.Result;
import com.dlq.yygh.common.result.ResultCodeEnum;
import com.dlq.yygh.helper.HttpRequestHelper;
import com.dlq.yygh.hosp.service.DepartmentService;
import com.dlq.yygh.hosp.service.HospitalService;
import com.dlq.yygh.hosp.service.HospitalSetService;
import com.dlq.yygh.model.hosp.Department;
import com.dlq.yygh.model.hosp.Hospital;
import com.dlq.yygh.model.hosp.HospitalSet;
import com.dlq.yygh.utils.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
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
    @Autowired
    private DepartmentService departmentService;

    /**
     * 查询医院接口
     */
    @PostMapping("/hospital/show")
    public Result getHospital(HttpServletRequest request){
        Map<String, Object> paramMap = checkSignKey(request);

        String hoscode = (String) paramMap.get("hoscode");

        //调用service方法实现根据医院编号查询
        Hospital hospital = hospitalService.getByHoscode(hoscode);
        return Result.ok(hospital);
    }

    /**
     * 上传医院接口
     */
    @PostMapping("/saveHospital")
    public Result saveHosp(HttpServletRequest request){
        Map<String, Object> paramMap = checkSignKey(request);

        //传输过程中“+”转换为了“ ”，因此我们要转换回来
        String logoDataString = (String)paramMap.get("logoData");
        if(!StringUtils.isEmpty(logoDataString)) {
            String logoData = logoDataString.replaceAll(" ", "+");
            paramMap.put("logoData", logoData);
        }

        hospitalService.save(paramMap);
        return Result.ok();
    }

    /**
     * 查询科室接口
     */
    @PostMapping("/department/list")
    public Result getDepartment(HttpServletRequest request){
        List<Department> departmentList = departmentService.getDepartment(request);
        return Result.ok(departmentList);
    }

    /**
     * 上传科室接口
     */
    @PostMapping("/saveDepartment")
    public Result saveDepartment(HttpServletRequest request){
        Map<String, Object> paramMap = checkSignKey(request);

        departmentService.saveDepartment(paramMap);
        return Result.ok();
    }

    private Map<String, Object> checkSignKey(HttpServletRequest request){
        //获取到传递过来的医院信息
        Map<String, Object> paramMap = HttpRequestHelper.switchMap(request.getParameterMap());
        //获取医院传过来的签名，签名进行了MD5加密
        String hospSign = (String) paramMap.get("sign");
        if(StringUtils.isEmpty(hospSign)) {
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }

        String hoscode = (String) paramMap.get("hoscode");
        if(StringUtils.isEmpty(hoscode)) {
            throw new YyghException(ResultCodeEnum.PARAM_ERROR);
        }
        String signKey = hospitalSetService.getSignKey(hoscode);

        String signKeyMD5 = MD5.encrypt(signKey);
        //判断签名是否一致
        if (!hospSign.equals(signKeyMD5)){
            throw new YyghException(ResultCodeEnum.SIGN_ERROR);
        }
        return paramMap;
    }
}
