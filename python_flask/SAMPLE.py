"""
@author Brandon Sheffield
@date:  8/15/15

Flask application that interfaces with databse to return json output
for Chicago Employees salaries.  Currently only works
for two Get operations.

data retrieved Aug 15th

original author: https://github.com/narenaryan/Salary-API
"""

from flask import Flask, request
from flask_restful import Resource, Api
from sqlachemy import create_engine
from json import dumps

db = create_engine('sqlite:///salaries.db')

#query by specific department
class Depart_Salary(Resource):
    def get(self, depart_name):
        connection = db.connect()
        connection.execute(“SELECT * from salaries where Department=? “, (depart_name,))
        result = {'data': [dict(zip(tuple(query.keys()), i)) for i in query.cursor]}
        return result

#query whole department
class Depart_Info(Resource):
    def get(self):
        connection = db.connect()
        db_query = connection.execute("select distinct DEPARTMENT from salaries")
        return {'departments': [i[0] for i in db_query.cursor.fetchall()]}

#urls where the data can be retrieved
api.add_resource(Depart_Salary, '/dept/<string:department_name>')
api.add_resource(Depart_Info, '/departments')

if __name__ == '__main__':
    app.run()
        
