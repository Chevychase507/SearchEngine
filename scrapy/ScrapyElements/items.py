# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# http://doc.scrapy.org/en/latest/topics/items.html

import scrapy

class NYTItem(scrapy.Item):
    url = scrapy.Field()
    header = scrapy.Field()
    words = scrapy.Field()

class Version2Item(scrapy.Item):
    url = scrapy.Field()
    header = scrapy.Field()
    words = scrapy.Field()

class BoldItem(scrapy.Item):
    url = scrapy.Field()
    header = scrapy.Field()
    words = scrapy.Field()

