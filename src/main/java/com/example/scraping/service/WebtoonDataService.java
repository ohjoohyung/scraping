package com.example.scraping.service;

import com.example.scraping.model.Webtoon;
import com.example.scraping.repository.WebtoonMapper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebtoonDataService {

    //스프링부트 폴더 구조 중요..
    //밖에 service 패키지 만들었다가 몇시간 동안 고생함
    private static String WEBTOON_URL = "https://comic.naver.com/webtoon/weekday.nhn";
    private static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
    private String[] days = {"mon","tue","wed","thu","fri","sat","sun"};


    @Autowired
    private WebtoonMapper webtoonMapper;

    public Map<String, List<Webtoon>> getWebtoonList() {
        Map<String, List<Webtoon>> map = new LinkedHashMap<>();
        for(int i=0; i<7; i++) {
            map.put(days[i], webtoonMapper.getWebtoonListByDay(i));
        }
        return map;
    }

    @PostConstruct
    public void getWebtoonDatas() {

        Document doc = null;
        Elements elemList = new Elements();
        String[] dateArr = {"h4.mon","h4.tue","h4.wed","h4.thu","h4.fri","h4.sat","h4.sun"};
        try {
            Connection connection = Jsoup.connect(WEBTOON_URL)
                    .userAgent(USER_AGENT)
                    .header("Accept","text/html")
                    .header("Accept-Encoding","gzip,deflate")
                    .header("Accept-Language","it-IT,en;q=0.8,en-US;q=0.6,de;q=0.4,it;q=0.2,es;q=0.2")
                    .header("Connection","keep-alive")
                    .ignoreContentType(true)
                    .timeout(30000);
            doc = connection.get();
            elemList = doc.select(".col_inner");


            if(elemList.size() > 0) {
                for(int i=0; i<elemList.size(); i++) {
                    Elements elements = elemList.get(i).children();
                    if(elements.size() > 0) {
                        for(int j=0; j<dateArr.length; j++) {
                            Elements elemTitle = elements.select(dateArr[j]);
                            Elements elemUl = new Elements();

                            if(elemTitle != null && !elemTitle.isEmpty()) {
                                System.out.println("-------------------------");
                                System.out.println(elemTitle.text());
                                System.out.println("-------------------------");
                                elemUl = elements.select("ul > li");

                                if(elemUl.size() > 0) {
                                    for(int k = 0; k < elemUl.size(); k++) {
                                        Elements elemLi = elemUl.get(k).children();
                                        if(webtoonMapper.getCntWebtoonByTitle(elemLi.text()) < 1) {
                                            Elements elemHref = elemLi.select(".thumb > a");
                                            Elements elemImg = elemLi.select(".thumb > a > img");
                                            System.out.println(elemLi.text());
                                            System.out.println(elemHref);
                                            System.out.println(elemImg.attr("src"));

                                            webtoonMapper.insertWebtoon(Webtoon.builder()
                                                    .day(j)
                                                    .title(elemLi.text())
                                                    .url(elemHref.attr("href"))
                                                    .thumb(elemImg.attr("src"))
                                                    .build()
                                            );

                                        }

                                    }

                                }
                            }
                        }
                    }
                }

            }

        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
