package com.lark.spider.start;

import com.lark.spider.core.download.IDownload;
import com.lark.spider.core.parser.IUserParser;
import com.lark.spider.core.store.IUserStore;
import com.lark.spider.spiderservice.entity.UserInfo;
import com.lark.spider.spiderservice.entity.UserStat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class StartSpider {

//    public static void main(String[] args) {
//        ISpider iSpider = new ISpider();
//        iSpider.setDownload(new UserDownloadImpl());
//        iSpider.setParser(new UserParserImpl());
//        iSpider.setStore(new UserStoreImpl());
//
//        String base = "https://api.bilibili.com/x/space/acc/info?mid=";
//
//        int i = 1;
//        while (true){
//            String url = base + i;
//            String content = iSpider.download(url);
//            iSpider.parser(content);
//            iSpider.store(new ArrayList());
//            i++;
//            if (i == 10){
//                break;
//            }
//        }
//    }

//    @Scheduled(cron = "0/10 * *  * * ?")
//    public void test01(){
//        ISpider iSpider = new ISpider();
//        iSpider.setDownload(new UserDownloadImpl());
//        iSpider.setParser(new UserParserImpl());
//        iSpider.setStore(new UserStoreImpl());
//
//        String base = "https://api.bilibili.com/x/space/acc/info?mid=";
//
//        int i = 1;
//        while (true){
//            String url = base + i;
//            String content = iSpider.download(url);
//            iSpider.parser(content);
//            iSpider.store(new ArrayList());
//            i++;
//            if (i == 10){
//                break;
//            }
//        }
//    }

    @Autowired
    private ISpider iSpider;

    @Autowired
    @Qualifier("userStoreImpl")
    private IUserStore userStore;

    @Autowired
    @Qualifier("downloadImpl")
    private IDownload userDownload;

    @Autowired
    @Qualifier("userParserImpl")
    private IUserParser userParser;




    String baseUserInfo = "https://api.bilibili.com/x/space/acc/info?mid=";
    String baseRelation = "http://api.bilibili.com/x/relation/stat?vmid=";
    String baseUpStat = "https://api.bilibili.com/x/space/upstat?mid=";

    long i = 1;

    @Scheduled(cron = "0 35 12  * * ?")
    public void test03(){
        iSpider.setDownload(userDownload);
        iSpider.setParser(userParser);
        iSpider.setStore(userStore);

        UserInfo userInfo = new UserInfo();
        UserStat userStat = new UserStat();



        while (true){

            String urlUserInfo = baseUserInfo + i;
            String urlRelation = baseRelation + i;
            String urlUpStat = baseUpStat + i;

            //下载
            String relationContent = iSpider.download(urlRelation);
            iSpider.parseRelation(relationContent, userStat);
            //如果用户粉丝数小于10w直接结束本次循环，不保存该用户信息
            if (Integer.parseInt(userStat.getFansNumber()) < 100000){
                System.out.println("不处理，粉丝数是：" + userStat.getFansNumber());
                i++;
                continue;
            }

            String userInfoContent = iSpider.download(urlUserInfo);
            String upStatContent = iSpider.download(urlUpStat);

            //解析
            iSpider.parseUserInfo(userInfoContent, userInfo);
            iSpider.parseUpStat(upStatContent, userStat);
            userStat.setUid(userInfo.getUid());

            System.out.println("============================");
            System.out.println("第" + i + "次循环，用户信息：" + userInfo);
            System.out.println("第" + i + "次循环，用户信息：" + userStat);
            System.out.println("============================");

            //存储
            iSpider.storeUserInfo(userInfo);
            iSpider.StoreUserStat(userStat);

            i++;

        }

    }

}
