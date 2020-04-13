package com.lark.spider.start;

import com.lark.spider.core.download.IDownload;
import com.lark.spider.core.download.impl.DownloadImpl;
import com.lark.spider.core.parser.IUserParser;
import com.lark.spider.core.repository.impl.UserRepository;
import com.lark.spider.core.store.IUserStore;
import com.lark.spider.utils.ApplicationContextProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.concurrent.LinkedBlockingQueue;

public class StartTest {

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

    public void test(){
        iSpider.setDownload(userDownload);
        iSpider.setParser(userParser);
        iSpider.setStore(userStore);

        //产生待爬取的url队列
        LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<>();
        UserRepository userRepository = ApplicationContextProvider.getBean("userRepository", UserRepository.class);
        userRepository.setUserBlockingQueue(queue);
        Thread thread = new Thread(userRepository);
        thread.start();

        //弹出队顶元素，没有元素的话返回null
        String poll = queue.poll();




    }

}
