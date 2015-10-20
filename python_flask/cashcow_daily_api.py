'''
Created on Oct 17, 2015

@author: Brandon Sheffield
@date:   10/17/15

Description: Runs on server to fetch mongo data 
and show in json format upon certain route 
i.e. bsheffield.com/cashcow/daily_data
'''

from flask import Flask, jsonify
from flask_restful import Resource
from pymongo.mongo_client import MongoClient
from pymongo import DESCENDING

app = Flask(__name__)  

@app.route('/cashcow/daily_data',methods=['GET'])
def get():
    client = MongoClient('localhost',27017) #explicit default 
    db = client['cashcow']
    collec = db['daily_reports']
    
    #grab most recent insertion...shaky logic i know
    cursor = collec.find().sort('_id',DESCENDING).limit(1)
    doc = cursor[0]
    doc.pop('_id') #get rid of id. causes problems w/ jsonify
    return jsonify({'Daily Data':doc})
    
if __name__ == '__main__':
    app.run(debug=True)
