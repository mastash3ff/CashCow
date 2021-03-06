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
    #client = MongoClient('localhost') #explicit default 
    #db = client['cashcow']
    #collec = db['daily_reports']
    
    #grab most recent insertion...shaky logic i know
    '''cursor = collec.find().sort('_id',DESCENDING).limit(1)
    doc = cursor[0]
    doc.pop('_id') #get rid of id. causes problems w/ jsonify
    '''
    #return jsonify({'Daily Data':doc})
    return jsonify({'Daily Data':'Hello World!'})

'''
TODO
-grab weekly data
'''

'''
TODO
Grab last weeks data and compare to this weekly
'''

'''
TODO
compare yesterday to today
'''

if __name__ == '__main__':
    app.run(debug=True, host='bsheffield.com',port=4758)
