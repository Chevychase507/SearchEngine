# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html

class NYTPipeline(object):

    def open_spider(self, spider):
        self.file = open('nytimes.txt', "w")

    def close_spider(self, spider):
        self.file.close()

    def process_item(self, item, spider):
        urlLine = "*PAGE:" + str(item["url"]) + "\n"
        headerLine = str(item["header"][0]) + "\n"
        self.file.write(urlLine)
        self.file.write(headerLine)
        for word in item["words"]:
            self.file.write(word + "\n")
        return item


class BoldPipeline(object):

    def open_spider(self, spider):
        self.file = open('bold.txt', "w")

    def close_spider(self, spider):
        self.file.close()

    def process_item(self, item, spider):
        urlLine = "*PAGE:" + str(item["url"]) + "\n"
        titleLine = str(item["header"][0]) + "\n"
        self.file.write(urlLine)
        self.file.write(titleLine)
        for word in item["words"]:
            self.file.write(word + "\n")
        return item


class Version2Pipeline(object):

    def open_spider(self, spider):
        self.file = open('Version2.txt', "w")

    def close_spider(self, spider):
        self.file.close()

    def process_item(self, item, spider):
        urlLine = "*PAGE:" + str(item["url"]) + "\n"
        titleLine = str(item["header"][0]) + "\n"
        self.file.write(urlLine)
        self.file.write(titleLine)
        for word in item["words"]:
            self.file.write(word + "\n")
        return item
