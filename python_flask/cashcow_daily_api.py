'''
Created on Oct 17, 2015

@author: Brandon Sheffield
@date:   10/17/15

Description: Runs on server to fetch mongo data 
and show in json format upon certain route 
i.e. bsheffield.com/cashcow/daily_data
'''

from flask import Flask, request
from flask_restful import Resource, Api
from json import dumps
from pymongo.mongo_client import MongoClient
from datetime import datetime


def daily_json():
    client = MongoClient('localhost',27017) #explicit default 
    db = client['cashcow']
    collec = db['daily_reports']
    
    today = dayHelper(datetime.weekday(datetime.now()))
    todayData = collec.find_one( {"Date":today} )
    print(todayData)
    
def dayHelper(day):
    if  (day is 0):
        return 'Mon'
    elif(day is 1):
        return 'Tue'
    elif(day is 2):
        return 'Wed'
    elif(day is 3):
        return 'Thu'
    elif(day is 4):
        return 'Fri'
    elif(day is 5): #return Friday for Saturday and Sunday since
        return 'Fri'#auction is closed on weekends
    elif(day is 6):
        return 'Fri'
    else:
        return None

if __name__ == '__main__':
    daily_json()
        