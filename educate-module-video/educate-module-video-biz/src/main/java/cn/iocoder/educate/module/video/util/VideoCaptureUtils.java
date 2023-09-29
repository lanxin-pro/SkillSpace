package cn.iocoder.educate.module.video.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.validation.constraints.Size;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * 视频截取指定帧图片
 *
 * @Author: j-sentinel
 * @Date: 2023/9/29 13:23
 */
@Slf4j
public class VideoCaptureUtils {

    private static InputStream getVideoInputStream(String videoUrl)
            throws IOException {
        //下载网络文件
        URL url = new URL(videoUrl);
        //获取链接
        URLConnection conn = url.openConnection();
        //输入流
        InputStream inStream = conn.getInputStream();
        return inStream;
    }

    public static void getVideoCoverByUrl(String url) {
        try {
            InputStream videoInputStream = getVideoInputStream(url);
            FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoInputStream);
            ff.start();
            int ffLength = ff.getLengthInFrames();
            Frame f;
            int i = 0;
            while (i < ffLength) {
                f = ff.grabFrame();
                //截取第6帧
                if ((i > 1) && (f.image != null)) {
                    executeFrame(f);
                    break;
                }
                i++;
            }
            ff.stop();
        } catch (Exception e) {
            log.error("getVideoCoverByUrl error：{}", ExceptionUtils.getMessage(e));
        }
    }

    private static void executeFrame(Frame frame) {
        OutputStream output = null;
        ByteArrayInputStream input = null;
        try {
            String imageSuffix = "png";
            if (null == frame || null == frame.image) {
                return;
            }
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage bi = converter.getBufferedImage(frame);
            output = new FileOutputStream("D:/cover-test.png");
            ImageIO.write(bi, imageSuffix, output);
        } catch (Exception e) {
            log.error("executeFrame error：{}", ExceptionUtils.getMessage(e));
        } finally {
            try {
                if (output != null) {
                    output.close();
                }
                if (input != null) {
                    input.close();
                }
            } catch (Exception e) {
                log.error("executeFrame error：{}", ExceptionUtils.getMessage(e));
            }
        }
    }

}
