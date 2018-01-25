# -*- coding: utf-8 -*-
import scrapy
from ScrapyElements.items import BoldItem
import re

class BoldSpider(scrapy.Spider):
    name = 'bold'
    allowed_domains = ['bold.dk']
    start_urls = ['https://www.bold.dk']

    #  Extracts the links of each page with css selectors and iterates over the list of links.
    #  Calls enterPage for each page

    def parse(self, response):
        listOfLinks = response.css("div.newslist a::attr(href)").extract()
        for link in listOfLinks:
            link = response.urljoin(link)
            yield scrapy.Request(link, callback=self.enterPage)

    # initializes an item and extracts url, header and words for each item/website
    def enterPage(self, response):
        listOfWords = []
        item = BoldItem()
        item["url"] = response.url
        item["header"] = response.css("div#article div.col1 h1::text").extract()
        for line in response.css("div.article_content article::text").extract():
            tempWordList = "".join(line).split(" ")
            for word in tempWordList:
                word = word.strip("‘’-»«$?.,:;–! ").lower()
                word = re.sub("\s+", "", word)
                if word is not "":
                    listOfWords.append(word)
        item["words"] = listOfWords
        return item


