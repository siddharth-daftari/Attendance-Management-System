from django.shortcuts import render, HttpResponse
from app.services import *
import json


def index(request):
    return render(request, 'index.html')

def checkIfAttendanceMarked(request):
    jsonVar = json.loads(request.body)

    print "request data : ", jsonVar
    returnVar = checkIfAttendanceMarkedService(jsonVar)
    print "response data : ", returnVar

    if not returnVar['result']:
        return HttpResponse(json.dumps(returnVar), status=500, content_type='application/json')
    else:
        return HttpResponse(json.dumps(returnVar), status=200, content_type='application/json')

def register(request):
    jsonVar = json.loads(request.body)

    print "request data : ", jsonVar
    returnVar = registerStudentService(jsonVar)
    print "response data : ", returnVar

    print returnVar
    if not returnVar['result']:
        return HttpResponse(json.dumps(returnVar), status=500, content_type='application/json')
    else:
        return HttpResponse(json.dumps(returnVar), status=200, content_type='application/json')


def markAttendance(request):
    jsonVar = json.loads(request.body)

    print "request data : ", jsonVar
    returnVar = markAttendanceService(jsonVar)
    print "response data : ", returnVar

    if not returnVar['result']:
        return HttpResponse(json.dumps(returnVar), status=403, content_type='application/json')
    else:
        return HttpResponse(json.dumps(returnVar), status=200, content_type='application/json')


def getClassDetails(request):
    returnVar = getClassDetailsService()
    print "response data : ", returnVar

    if not returnVar['result']:
        return HttpResponse(json.dumps(returnVar), status=403, content_type='application/json')
    else:
        return HttpResponse(json.dumps(returnVar), status=200, content_type='application/json')