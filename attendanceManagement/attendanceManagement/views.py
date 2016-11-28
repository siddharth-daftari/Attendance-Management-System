from django.shortcuts import render, HttpResponse
from app.services import *
import json


def index(request):
    return render(request, 'index.html')


def register(request):
    jsonVar = json.loads(request.body)
    returnVar = registerStudentService(jsonVar)
    if not returnVar['result']:
        return HttpResponse(json.dumps(returnVar), status=500, content_type='application/json')
    else:
        return HttpResponse(json.dumps(returnVar), status=200, content_type='application/json')


def markAttendance(request):
    jsonVar = json.loads(request.body)
    returnVar = markAttendanceService(jsonVar)
    if not returnVar['result']:
        return HttpResponse(json.dumps(returnVar), status=403, content_type='application/json')
    else:
        return HttpResponse(json.dumps(returnVar), status=200, content_type='application/json')
