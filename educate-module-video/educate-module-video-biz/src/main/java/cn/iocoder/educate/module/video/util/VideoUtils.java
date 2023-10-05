package cn.iocoder.educate.module.video.util;

import lombok.extern.slf4j.Slf4j;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * FFmpeg 来获取视频截取指定帧图片
 *
 * @Author: j-sentinel
 * @Date: 2023/9/29 13:23
 */
@Slf4j
public class VideoUtils {

    /**
     * 获取指定视频的帧并保存为图片
     * @param file  源视频文件地址
     * @param framefile  截取帧的图片存放地址
     * @throws Exception
     */
    public static void fetchPic(byte[] file, String framefile) throws Exception {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file);
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(byteArrayInputStream);
        ff.start();
        int lenght = ff.getLengthInFrames();

        File targetFile = new File(framefile);
        int i = 0;
        Frame frame = null;
        while (i < lenght) {
            // 过滤前5帧，避免出现全黑的图片，依自己情况而定

            frame = ff.grabFrame();

            if ((i > 5) && (frame.image != null)) {
                break;
            }
            i++;
        }

        String imgSuffix = "jpg";
        if(framefile.indexOf('.') != -1){
            String[] arr = framefile.split("\\.");
            if(arr.length>=2){
                imgSuffix = arr[1];
            }
        }

        Java2DFrameConverter converter =new Java2DFrameConverter();
        BufferedImage srcBi = converter.getBufferedImage(frame);
        int owidth = srcBi.getWidth();
        int oheight = srcBi.getHeight();
        // 对截取的帧进行等比例缩放
        int width = 800;
        int height = (int) (((double) width / owidth) * oheight);
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        bi.getGraphics().drawImage(srcBi.getScaledInstance(width, height, Image.SCALE_SMOOTH),0, 0, null);

        try {
            ImageIO.write(bi, imgSuffix, targetFile);
        }catch (Exception e) {
            e.printStackTrace();
        }
        ff.stop();
    }

    /**
     * 获取指定视频byte[]流的帧并保存为图片
     *
     * @param file 源视频
     * @return 视频封面的byte[]
     * @throws Exception
     */
    public static byte[] fetchUrl(byte[] file) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file);
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(byteArrayInputStream);
        byte[] bytes = null;
        try {
            ff.start();
            int lenght = ff.getLengthInFrames();

            int i = 0;
            Frame frame = null;
            while (i < lenght) {
                // 过滤前5帧，避免出现全黑的图片，依自己情况而定

                frame = ff.grabFrame();

                if ((i > 5) && (frame.image != null)) {
                    break;
                }
                i++;
            }
            String imgSuffix = "jpg";

            Java2DFrameConverter converter =new Java2DFrameConverter();
            BufferedImage srcBi = converter.getBufferedImage(frame);
            int owidth = srcBi.getWidth();
            int oheight = srcBi.getHeight();
            // 对截取的帧进行等比例缩放
            int width = 800;
            int height = (int) (((double) width / owidth) * oheight);
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            bi.getGraphics().drawImage(srcBi.getScaledInstance(width, height, Image.SCALE_SMOOTH),0, 0, null);

            // 创建字节数组输出流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, imgSuffix, baos);
            // 获取字节数组
            bytes = baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ff.stop();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
        return bytes;
    }

    public static Map<String,Object> fetchMap(byte[] file) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file);
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(byteArrayInputStream);
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();
        byte[] bytes = null;
        Integer times;
        try {
            ff.start();
            times = (int)ff.getLengthInTime()/(1000*1000);
            int lenght = ff.getLengthInFrames();

            int i = 0;
            Frame frame = null;
            while (i < lenght) {
                // 过滤前5帧，避免出现全黑的图片，依自己情况而定

                frame = ff.grabFrame();

                if ((i > 5) && (frame.image != null)) {
                    break;
                }
                i++;
            }
            String imgSuffix = "jpg";

            Java2DFrameConverter converter =new Java2DFrameConverter();
            BufferedImage srcBi = converter.getBufferedImage(frame);
            int owidth = srcBi.getWidth();
            int oheight = srcBi.getHeight();
            // 对截取的帧进行等比例缩放
            int width = 800;
            int height = (int) (((double) width / owidth) * oheight);
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            bi.getGraphics().drawImage(srcBi.getScaledInstance(width, height, Image.SCALE_SMOOTH),0, 0, null);

            // 创建字节数组输出流
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bi, imgSuffix, baos);
            // 获取字节数组
            bytes = baos.toByteArray();
            stringObjectHashMap.put("videoCover",bytes);
            stringObjectHashMap.put("videoTimes",times);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ff.stop();
            } catch (FrameGrabber.Exception e) {
                e.printStackTrace();
            }
        }
        return stringObjectHashMap;
    }

    /**
     * 获取视频时长，单位为秒
     * @param file 即为视频地址
     * @return 时长（秒）
     */
    public static Long getVideoTime(File file){
        Long times = 0L;
        try {
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file);
            ff.start();
            times = ff.getLengthInTime()/(1000*1000);
            ff.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return times;
    }

}
