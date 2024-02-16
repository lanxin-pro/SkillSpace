package cn.iocoder.educate.file.video.aliyun.Audio;

import cn.iocoder.educate.file.video.aliyun.InitObject;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.CreateUploadImageRequest;
import com.aliyuncs.vod.model.v20170321.CreateUploadImageResponse;

/**
 * @author j-sentinel
 * @date 2024/2/6 17:41
 */
public class Sample {

    /**
     * 获取图片上传地址和凭证
     * @param client 发送请求客户端
     * @return CreateUploadImageResponse 获取图片上传地址和凭证响应数据
     * @throws Exception
     */
    public static CreateUploadImageResponse createUploadImage(DefaultAcsClient client) throws Exception {
        CreateUploadImageRequest request = new CreateUploadImageRequest();
        // 设置图片类型
        request.setImageType("default");
        // 设置图片扩展名
        request.setImageExt("gif");
        // 设置图片标题
        request.setTitle("this is a sample");

        // UserData，用户自定义设置参数，用户需要单独回调URL及数据透传时设置(非必须)
        JSONObject userData = new JSONObject();

        // UserData回调部分设置
        // 消息回调设置，指定时以此为准，否则以全局设置的事件通知为准
        JSONObject messageCallback = new JSONObject();
        // 设置回调地址
        messageCallback.put("CallbackURL", "http://192.168.0.0/16");
        // 设置回调方式，默认为http
        messageCallback.put("CallbackType", "http");
        userData.put("MessageCallback", messageCallback.toJSONString());

        JSONObject extend = new JSONObject();
        extend.put("MyId", "user-defined-id");
        userData.put("Extend", extend.toJSONString());

        request.setUserData(userData.toJSONString());

        return client.getAcsResponse(request);
    }


    public static void main(String[] args_) throws Exception {
        try {
            DefaultAcsClient client = InitObject.initVodClient();
            CreateUploadImageResponse response = createUploadImage(client);
            System.out.print("ImageId = " + response.getImageId() + "\n");
            System.out.print("ImageURL = " + response.getImageURL() + "\n");
            System.out.print("UploadAddress = " + response.getUploadAddress() + "\n");
            System.out.print("UploadAuth = " + response.getUploadAuth() + "\n");
            System.out.print("RequestId = " + response.getRequestId() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
    }
}
