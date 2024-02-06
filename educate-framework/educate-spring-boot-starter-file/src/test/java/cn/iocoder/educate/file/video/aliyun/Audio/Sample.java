package cn.iocoder.educate.file.video.aliyun.Audio;

import cn.iocoder.educate.file.video.aliyun.InitObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;

import java.util.List;

/**
 * @author j-sentinel
 * @date 2024/2/6 17:41
 */
public class Sample {

    public static void main(String[] args_) throws Exception {
        java.util.List<String> args = java.util.Arrays.asList(args_);
        // 请确保代码运行环境设置了环境变量 ALIBABA_CLOUD_ACCESS_KEY_ID 和 ALIBABA_CLOUD_ACCESS_KEY_SECRET。
        DefaultAcsClient defaultAcsClient = InitObject.initVodClient();
        GetPlayInfoRequest getPlayInfoRequest = new GetPlayInfoRequest();
        GetPlayInfoResponse getPlayInfoResponse = new GetPlayInfoResponse();
        getPlayInfoRequest.setVideoId("");
        getPlayInfoResponse = defaultAcsClient.getAcsResponse(getPlayInfoRequest);

        List<GetPlayInfoResponse.PlayInfo> playInfoList = getPlayInfoResponse.getPlayInfoList();
        // 播放地址
        for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
            System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
        }
        // Base信息
        System.out.print("VideoBase.Title = " + getPlayInfoResponse.getVideoBase().getTitle() + "\n");
    }
}
