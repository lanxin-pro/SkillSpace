package cn.iocoder.educate.framework.excel.core.covert;

import cn.iocoder.educate.framework.dict.core.util.DictFrameworkUtils;
import cn.iocoder.educate.framework.excel.core.annotations.DictFormat;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;

/**
 * Excel 数据字典转换器
 *
 * @author j-sentinel
 * @date 2024/1/20 12:24
 */
@Slf4j
public class DictConvert implements Converter<Object> {

    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    private static String getType(ExcelContentProperty contentProperty) {
        return contentProperty.getField().getAnnotation(DictFormat.class).value();
    }

    /**
     * 这个属性暂时无用
     *
     * @param cellData Excel单元格数据
     * @param contentProperty 内容属性
     * @param globalConfiguration 全局配置
     * @return 要放入Java对象中的数据
     * @throws Exception UnsupportedOperationException
     */
    @Override
    public Object convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty,
                                    GlobalConfiguration globalConfiguration) throws Exception {
        return Converter.super.convertToJavaData(cellData, contentProperty, globalConfiguration);
    }

    /**
     *
     * @param object javaData
     * @param contentProperty 内容属性
     * @param globalConfiguration 全局配置
     * @return 要放入Java对象中的数据
     * @throws Exception UnsupportedOperationException
     */
    @Override
    public WriteCellData<String> convertToExcelData(Object object, ExcelContentProperty contentProperty,
                                                    GlobalConfiguration globalConfiguration) {
        // 空时，返回空
        if (object == null) {
            return new WriteCellData<>("");
        }
        // 使用字典格式化
        String type = getType(contentProperty);
        String value = String.valueOf(object);
        String label = DictFrameworkUtils.getDictDataLabel(type, value);
        // 找不到的抛出
        if (label == null) {
            log.error("[convertToExcelData][type({}) 转换不了 label({})]", type, value);
            return new WriteCellData<>();
        }

        // 生成 Excel 小表格
        return new WriteCellData<>(label);
    }

}
