# -*- coding: utf-8 -*-
import scrapy
from ScrapyElements.items import Version2Item


class Version2Spider(scrapy.Spider):
    name = 'version2'
    allowed_domains = ['www.version2.dk']
    start_urls = ['https://www.version2.dk/it-nyheder/']

    #  extracts the links of each page with css selectors and iterates over the list of links.
    #  Calls enterPage for each page
    def parse(self, response):
        listOfLinks = response.css("h2.node-title a::attr(href)").extract()
        for link in listOfLinks:
            link = response.urljoin(link)
            yield scrapy.Request(link, callback=self.enterPage)

        # if there is a "nextpage" button, the parse method calls itself and scrapes the next page
        next_page = response.css('li.next a::attr(href)').extract_first()
        if next_page is not None:
            next_page = response.urljoin(next_page)
            yield scrapy.Request(next_page, callback=self.parse)

    # initializes an item and extracts url, header and words for each item/website
    def enterPage(self, response):
        listOfWords = []
        item = Version2Item()
        item["url"] = response.url
        item["header"] = response.css("div.node-title-pane h1::text").extract()
        for line in response.css("section.body p"):
            tempWordList = "".join(line.css("::text").extract()).split(" ")
            for word in tempWordList:
                word = word.strip("‘’-»«$?.,:;–! ").lower()
                if word is not "":
                    listOfWords.append(word)
        item["words"] = listOfWords
        return item
