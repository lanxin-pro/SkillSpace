package cn.iocoder.educate.module.system.service.sensitiveword;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.hash.Hash;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.educate.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.educate.framework.common.pojo.PageResult;
import cn.iocoder.educate.module.system.controller.admin.sensitiveword.vo.SensitiveWordCreateReqVO;
import cn.iocoder.educate.module.system.controller.admin.sensitiveword.vo.SensitiveWordExportReqVO;
import cn.iocoder.educate.module.system.controller.admin.sensitiveword.vo.SensitiveWordPageReqVO;
import cn.iocoder.educate.module.system.controller.admin.sensitiveword.vo.SensitiveWordUpdateReqVO;
import cn.iocoder.educate.module.system.convert.sensitiveword.SensitiveWordConvert;
import cn.iocoder.educate.module.system.dal.dataobject.sensitiveword.SensitiveWordDO;
import cn.iocoder.educate.module.system.dal.mysql.sensitiveword.SensitiveWordMapper;
import cn.iocoder.educate.module.system.enums.ErrorCodeConstants;
import cn.iocoder.educate.module.system.mq.producer.sensitiveword.SensitiveWordProducer;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: j-sentinel
 * @Date: 2023/8/21 8:05
 */
@Service
@Slf4j
@Validated
public class SensitiveWordServiceImpl implements SensitiveWordService {

    @Resource
    private SensitiveWordMapper sensitiveWordMapper;

    @Resource
    private SensitiveWordProducer sensitiveWordProducer;

    /**
     * 敏感词标签缓存
     * key：敏感词编号 {@link SensitiveWordDO#getId()}
     * <p>
     * 这里声明 volatile 修饰的原因是，每次刷新时，直接修改指向
     */
    @Getter
    private volatile Set<String> sensitiveWordTagsCache = Collections.emptySet();

    @Override
    @PostConstruct
    public void initLocalCache() {
        // 第一步：查询数据
        List<SensitiveWordDO> sensitiveWords = sensitiveWordMapper.selectList(new LambdaQueryWrapper<>());
        log.info("[initLocalCache][缓存敏感词，数量为:{}]", sensitiveWords.size());

        // 第二步：构建缓存
        // 写入 sensitiveWordTagsCache 缓存
        Set<String> tags = new HashSet<>();
        sensitiveWords.forEach(word -> {
            tags.addAll(word.getTags());
        });
        sensitiveWordTagsCache = tags;
    }

    @Override
    public Long createSensitiveWord(SensitiveWordCreateReqVO sensitiveWordCreateReqVO) {
        validateSensitiveWordNameUnique(null,sensitiveWordCreateReqVO.getName());
        SensitiveWordDO convert = SensitiveWordConvert.INSTANCE.convert(sensitiveWordCreateReqVO);
        sensitiveWordMapper.insert(convert);
        // 发送消息，刷新缓存
        sensitiveWordProducer.sendSensitiveWordRefreshMessage();
        return convert.getId();
    }

    @Override
    public void updateSensitiveWord(SensitiveWordUpdateReqVO sensitiveWordUpdateReqVO) {
        // 校验唯一性
        validateSensitiveWordExists(sensitiveWordUpdateReqVO.getId());
        validateSensitiveWordNameUnique(sensitiveWordUpdateReqVO.getId(), sensitiveWordUpdateReqVO.getName());

        // 更新
        SensitiveWordDO updateObj = SensitiveWordConvert.INSTANCE.convert(sensitiveWordUpdateReqVO);
        sensitiveWordMapper.updateById(updateObj);
        // 发送消息，刷新缓存
        sensitiveWordProducer.sendSensitiveWordRefreshMessage();
    }

    @Override
    public void deleteSensitiveWord(Long id) {
        // 校验存在
        validateSensitiveWordExists(id);
        // 删除
        sensitiveWordMapper.deleteById(id);
        // 发送消息，刷新缓存
        sensitiveWordProducer.sendSensitiveWordRefreshMessage();
    }

    @Override
    public SensitiveWordDO getSensitiveWord(Long id) {
        return sensitiveWordMapper.selectById(id);
    }

    @Override
    public List<SensitiveWordDO> getSensitiveWordList() {
        return sensitiveWordMapper.selectList(new LambdaQueryWrapper<>());
    }

    @Override
    public PageResult<SensitiveWordDO> getSensitiveWordPage(SensitiveWordPageReqVO sensitiveWordPageReqVO) {
        return sensitiveWordMapper.selectPage(sensitiveWordPageReqVO);
    }

    @Override
    public List<SensitiveWordDO> getSensitiveWordList(SensitiveWordExportReqVO sensitiveWordExportReqVO) {
        return null;
    }

    @Override
    public Set<String> getSensitiveWordTagSet() {
        return sensitiveWordTagsCache;
    }

    @Override
    public List<String> validateText(String text, List<String> tags) {
        return null;
    }

    @Override
    public boolean isTextValid(String text, List<String> tags) {
        return false;
    }

    @Override
    public void deleteBatchSensitiveWord(String batchIds) {
        String[] split = batchIds.split(",");
        List<SensitiveWordDO> collect = Arrays.stream(split).map(id -> {
            // 校验存在
            validateSensitiveBatchWordExists(id);
            SensitiveWordDO sensitiveWordDO = new SensitiveWordDO();
            sensitiveWordDO.setId(new Long(id));
            return sensitiveWordDO;
        }).collect(Collectors.toList());
        sensitiveWordMapper.deleteBatchIds(collect);
    }

    @Override
    public Long createBatchSensitiveWord(SensitiveWordCreateReqVO sensitiveWordCreateReqVO) {
        // 去除全部的空白字符
        String deleteWhitespace = StringUtils.deleteWhitespace(sensitiveWordCreateReqVO.getName());
        String[] split = deleteWhitespace.split(",");
        // TODO 项目经理-蓝欣 uat发现这里存在性能隐患
        HashSet<String> repeatName = new HashSet<>();
        List<SensitiveWordDO> sensitiveWordDOS = Arrays.stream(split).map(name -> {
            // 校验存在
            validateSensitiveBatchWordNameUnique(name,repeatName);
            // 对象的创建放到外面,不想创建非常多的对象
            SensitiveWordDO sensitiveWordDO = new SensitiveWordDO();
            sensitiveWordDO.setName(name);
            sensitiveWordDO.setDescription(sensitiveWordCreateReqVO.getDescription());
            sensitiveWordDO.setStatus(sensitiveWordCreateReqVO.getStatus());
            sensitiveWordDO.setTags(sensitiveWordCreateReqVO.getTags());
            return sensitiveWordDO;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        // 重复的名称
        if(CollectionUtil.isNotEmpty(repeatName)){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SENSITIVE_WORD_REPEAT,repeatName);
        }
        sensitiveWordMapper.insertBatch(sensitiveWordDOS);
        return null;
    }

    private HashSet<String> validateSensitiveBatchWordNameUnique(String name,HashSet<String> repeatName) {
//        List<SensitiveWordDO> sensitiveWordList = getSensitiveWordList();
//        Map<String, SensitiveWordDO> sensitiveWordDOMap = sensitiveWordList.stream()
//                .collect(Collectors.toMap(SensitiveWordDO::getName, Function.identity(), (v1, v2) -> v1));
//        SensitiveWordDO sensitiveWordDO = sensitiveWordDOMap.get(name);
        SensitiveWordDO sensitiveWordDO = sensitiveWordMapper.selectByName(name);
        // 我不可能拿一个对象就去输出一下，不太现实
        if(sensitiveWordDO != null){
            repeatName.add(sensitiveWordDO.getName());
        }
        return repeatName;
    }

    private void validateSensitiveBatchWordExists(String id) {
//        List<SensitiveWordDO> sensitiveWordList = getSensitiveWordList();
//        Map<Long, SensitiveWordDO> sensitiveWordDOMap = sensitiveWordList.stream()
//                .collect(Collectors.toMap(SensitiveWordDO::getId, Function.identity(), (v1, v2) -> v1));
//        SensitiveWordDO sensitiveWordDO = sensitiveWordDOMap.get(Long.valueOf(id));
        SensitiveWordDO sensitiveWordDO = sensitiveWordMapper.selectById(id);
        if(sensitiveWordDO == null){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SENSITIVE_WORD_EXISTS);
        }
    }

    private void validateSensitiveWordNameUnique(Long id, String name) {
        SensitiveWordDO word = sensitiveWordMapper.selectByName(name);
        if (word == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的敏感词
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SENSITIVE_WORD_EXISTS);
        }
        if (!word.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SENSITIVE_WORD_EXISTS);
        }
    }

    private void validateSensitiveWordExists(Long id) {
        if (sensitiveWordMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.SENSITIVE_WORD_NOT_EXISTS);
        }
    }

}
