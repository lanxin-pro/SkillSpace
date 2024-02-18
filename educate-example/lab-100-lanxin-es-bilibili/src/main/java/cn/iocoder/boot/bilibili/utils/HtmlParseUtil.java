package cn.iocoder.boot.bilibili.utils;

import cn.iocoder.boot.bilibili.pojo.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author j-sentinel
 * @date 2024/2/16 19:11
 */
public class HtmlParseUtil {

    public static void main(String[] args) throws IOException {
        ArrayList<Content> a = extracted("玫瑰花");
        a.forEach(System.out::println);
    }

    public static ArrayList<Content> extracted(String keyword) throws IOException {
        // 1.获取请求
        String url = "https://search.jd.com/Search?keyword=" + keyword + "&enc=utf-8";
        HashMap<String, String> map = new HashMap<>();
        map.put("thor", "74FEECCCE207C9391474B55B2A19B95C6C6734DC64048F62BFD63CD794ACD5CCBA48285190559FEE3F220E4F5DB814079F9C266682DBC0D64000B716B50D04B353CFB8EA3082215DEF2C49BE86790D90AE64C60B30329EF9415BABDCD72F08E3E29C2E96B1A4350FE77599E3DFBBDFDA380B955E172A21C3DA5A8A06E1A68F608F2B9F3B4BAC7ED064F1583993F829D2; flash=2_qV5jrRnGgKsmrjdSycS1YCln4DM5DGVfHstFMFkmp-1EPQqrRvUifiYzrfjy00RxJnmFIcAOvx8hWA2pN5fYRp1TYz4nAqZJehJmBCtqaJj*; _tp=k7kFCZX7hgaxJXmVWKTQ3w%3D%3D; pinId=YG7B5-pO47Nst8anfbGW8Q; jsavif=1; jsavif=1; rkv=1.0; avif=1; qrsc=3; 3AB9D23F7A4B3CSS=jdd032Z33EGFDIAQFSGPY4S64ZQKNU6YJFTKHOR6VMXO3M5IAQDG3ZK32CM3HJQNNTILVZQFKO3GMAEYWKYU5QDGQX6C45QAAAAMNWG2EMVIAAAAADT47EX7UTUXPLYX; _gia_d=1;");
        Document doc = Jsoup.connect(url).cookies(map).get();
        Element j_goodsList = doc.getElementById("J_goodsList");
        Elements elementsByTag = j_goodsList.getElementsByTag("li");
        ArrayList<Content> goodsList = new ArrayList<>();
        for(Element el : elementsByTag) {
            String img = el.getElementsByTag("img").eq(0).attr("data-lazy-img");
            String price = el.getElementsByClass("p-price").eq(0).text();
            String name = el.getElementsByClass("p-name").eq(0).text();
            Content content = new Content();
            content.setTitle(name);
            content.setPrice(price);
            content.setImg(img);
            goodsList.add(content);
        }
        return goodsList;
    }

}
