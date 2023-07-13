package cn.iocoder.educate.framework.file.core.utils;

import com.alibaba.ttl.TransmittableThreadLocal;
import org.apache.tika.Tika;

/**
 * 文件类型 Utils
 *
 * @Author: j-sentinel
 * @Date: 2023/7/13 12:33
 */
public class FileTypeUtils {

    private static final ThreadLocal<Tika> TIKA = TransmittableThreadLocal.withInitial(Tika::new);

    /**
     * 在拥有文件和数据的情况下，最好使用此方法，最为准确
     *
     * @param data 文件内容
     * @param name 文件名
     * @return mineType 无法识别时会返回“application/octet-stream”
     */
    public static String getMineType(byte[] data, String name) {
        return TIKA.get().detect(data, name);
    }
}
