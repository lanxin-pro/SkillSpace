package cn.iocoder.educate.file.video.aliyun.Audio;

import cn.iocoder.educate.file.video.aliyun.InitObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoRequest;
import com.aliyuncs.vod.model.v20170321.GetPlayInfoResponse;

import java.util.List;

/**
 * 获取阿里云视频点播的播放地址
 *
 * @author j-sentinel
 * @date 2024/3/24 22:31
 */
public class GetPlayAddress {

    // 阿里云视频点播获取播放凭证
    public static void main(String[] args) throws ClientException {
        // 创建客户端
        DefaultAcsClient client = InitObject.initVodClient();
        // 获取播放凭证
        GetPlayInfoRequest request = new GetPlayInfoRequest();
        GetPlayInfoResponse response = new GetPlayInfoResponse();
        try {
            request.setVideoId("a073fdbac4d371ee864d4531949c0102");
            response = client.getAcsResponse(request);
            List<GetPlayInfoResponse.PlayInfo> playInfoList = response.getPlayInfoList();

            // 播放地址
            for (GetPlayInfoResponse.PlayInfo playInfo : playInfoList) {
                System.out.print("PlayInfo.PlayURL = " + playInfo.getPlayURL() + "\n");
            }
            // Base信息
            System.out.print("VideoBase.Title = " + response.getVideoBase().getTitle() + "\n");
        } catch (Exception e) {
            System.out.print("ErrorMessage = " + e.getLocalizedMessage());
        }
        System.out.print("RequestId = " + response.getRequestId() + "\n");
    }

}
