import scrapy
from random import randint
from ScrapyElements.items import NYTItem

class NYTimesSpider(scrapy.Spider):
    name = 'nytimes'
    allowed_domains = ['www.nytimes.com', 'spiderbites.nytimes.com']
    start_urls = ['http://spiderbites.nytimes.com/free_2017/index.html']

    # Extracts the links from the main page using css selector and puts them into a list
    # Iterates over the list of links and calls the enterSubpage method
    def parse(self, response):
        listOfLinks = response.css("body div.monthsRow a::attr(href)").extract()
        for link in listOfLinks:
            link = response.urljoin(link)
            yield scrapy.Request(link, callback=self.enterSubpage)

    # Extracts the links of all articles in the subpage using css selector and adds them to a list
    # Iterates over the list of links and calls the enterArticle method
    def enterSubpage(self, response):
        listOfPages = response.css("body a::attr(href)").extract()
        for link in listOfPages:
            if randint(1, 5) == 1:  # To avoid too big a database
                link = response.urljoin(link)
                yield scrapy.Request(link, callback=self.enterArticle)

    # Initializes the item object (a website). Adds url, header and a list of words by using css selectors
    # Returns the item, which is printed to a text file in pipelines.py
    def enterArticle(self, response):
        listOfWords = []
        item = NYTItem()
        item["url"] = response.url
        item["header"] = response.css("div.page h1::text").extract()
        tempListOfWords = "".join(response.css("div.story-body-supplemental p::text").extract()).split(" ")
        i = 0
        while i < 100:  # adding only the first 100 words per article
            word = tempListOfWords[i]
            word = word.strip("“”“‘’'-»«$?.,,:;–! ").lower()  # Removes symbols
            word = word.replace("advertisement", "")
            if word is not "":
                listOfWords.append(word)
            i += 1
        item["words"] = listOfWords
        return item
