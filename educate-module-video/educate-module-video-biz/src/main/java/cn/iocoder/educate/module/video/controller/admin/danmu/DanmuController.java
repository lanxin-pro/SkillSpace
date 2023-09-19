package cn.iocoder.educate.module.video.controller.admin.danmu;

import cn.iocoder.educate.framework.common.pojo.CommonResult;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.framework.operatelog.core.annotations.OperateLog;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminPageReqVO;
import cn.iocoder.educate.module.video.controller.admin.videoadmin.vo.VideoAdminRespVo;
import cn.iocoder.educate.module.video.covert.videoadmin.VideoAdminConvert;
import cn.iocoder.educate.module.video.dal.dataobject.VideoAdminDO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: j-sentinel
 * @Date: 2023/9/19 16:47
 */
@Tag(name = "管理后台 - 视频弹幕接口")
@RestController
@RequestMapping("/video/danmu")
@Validated
public class DanmuController {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @GetMapping("/v3/")
    @Operation(summary = "获得视频详情分页")
    @PermitAll
    public String getVideoPage(String id) throws Exception {
        System.out.println(id);
        Map map = new HashMap();
        List data = new ArrayList();
        data = DPlayerConstants.barrage_init(data);
        map.put("code", DPlayerConstants.DPLAYER_SUCCESS_CODE);
        map.put("data",data);
        return obj2json(map);
    }

    public static String obj2json(Object obj) throws Exception {
        return objectMapper.writeValueAsString(obj);
    }

    @PostMapping("/v3/")
    @Operation(summary = "发送视频弹幕")
    @PermitAll
    public String postv3(@RequestBody Map<String,String> param) throws Exception {
        Map map = new HashMap();
        System.out.println(param);
        map.put("code",DPlayerConstants.DPLAYER_SUCCESS_CODE);
        map.put("data",param);
        return obj2json(map);
    }

}
