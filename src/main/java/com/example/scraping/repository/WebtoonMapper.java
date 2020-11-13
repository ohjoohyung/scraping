package com.example.scraping.repository;

import com.example.scraping.model.Webtoon;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

//인텔리제이가 Mapper 어노테이션을 인식 못하는지 Repository 어노테이션을 추가해야 서비스에서 Autowired가 가능
@Mapper
@Repository
public interface WebtoonMapper {
    List<Webtoon> getWebtoonListByDay(int day);
    int getCntWebtoonByTitle(String title);
    void insertWebtoon(Webtoon webtoon);
}
