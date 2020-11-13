package com.example.scraping.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Builder
@Getter
public class Webtoon {
    private int no; //번호
    private int day; //요일
    private String title; //제목
    private String url; //웹툰 url
    private String thumb; //썸네일 url

}
