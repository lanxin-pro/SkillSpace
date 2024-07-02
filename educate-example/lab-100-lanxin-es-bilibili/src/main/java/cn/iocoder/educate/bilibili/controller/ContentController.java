package cn.iocoder.educate.bilibili.controller;

import cn.iocoder.educate.bilibili.pojo.Content;
import cn.iocoder.educate.bilibili.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/2/16 20:11
 */
@RestController
public class ContentController {

    @Autowired
    private ContentService contentService;

    @GetMapping("/parse/{keyword}")
    public Boolean parse(@PathVariable("keyword") String keyword) throws IOException {
        return contentService.parseContent(keyword);
    }

    @GetMapping("/search/{keyword}/{pageNo}/{pageSize}")
    public List<Content> page(@PathVariable("keyword") String keyword,
                              @PathVariable("pageNo") int pageNo,
                              @PathVariable("pageSize") int pageSize) throws IOException {
        return contentService.searchPage(keyword, pageNo, pageSize);
    }
}
