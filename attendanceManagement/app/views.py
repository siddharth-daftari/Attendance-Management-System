from django.shortcuts import render, redirect
from models import AttendanceDetails, StudentDetails, ProfessorDetails

def index(request):
    return render(request, 'index.html')


def dashboard(request):
    print "rendering dashboard :"
    studentList = StudentDetails.objects.all()

    resultList = []

    for obj2 in studentList:

        obj = AttendanceDetails.objects.filter(studentMacAddress=obj2.macAddress)
        print obj
        if (AttendanceDetails.objects.filter(studentMacAddress=obj2.macAddress).count() == 1):
            rs = {}
            rs["student"] = obj2
            rs["status"] = obj.attendanceStatus
            rs["date"] = obj.date
            resultList.append(rs)
        else:
            for o in obj:
                rs = {}
                rs["student"] = obj2
                rs["status"] = o.attendanceStatus
                rs["date"] = o.date
                resultList.append(rs)

    ls = []
    ls.append("273")
    proffName = ["Sithu"]

    detail = {"detail": resultList, "classId": ls, "proffesorName": proffName}

    return render(request, 'dashboard1.html', detail)


def form(request):
    ls = ["273"]
    proffName = ["Sithu"]
    detail = {"classId": ls, "proffesorName": proffName}
    return render(request, 'form.html', detail)


def loginform(request):
    if request.method == 'POST':
        userid = request.POST.get('userid', None)
        passwd = request.POST.get('passwd', None)

        obj = ProfessorDetails.objects.filter(professorId=userid)
        for o in obj:
            if o.password == passwd:
                return redirect('/app/dashboards')

        return render(request, 'index.html')


def search(request):
    if request.method == 'POST':
        search_id = request.POST.get('datepicker', None)
        studentList = []
        resultList = []
        print "date :", search_id
        obj = AttendanceDetails.objects.filter(date__icontains=search_id)
        for o in obj:
            rs = {}
            rs["status"] = o.attendanceStatus
            rs["student"] = StudentDetails.objects.get(macAddress=o.studentMacAddress)
            resultList.append(rs)
        ls = []
        ls.append("273")
        proffName = ["Sithu"]

        detail = {"detail": resultList, "classId": ls, "proffesorName": proffName}
        return render(request, 'form.html', detail)

    else:
        return render(request, 'form.html')