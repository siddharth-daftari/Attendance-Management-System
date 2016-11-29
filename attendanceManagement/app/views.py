from django.shortcuts import render,redirect
from models import AttendanceDetails,StudentDetails,ProfessorDetails


def index(request):
    return render(request, 'index.html')

def dashboard(request):
    studentList=StudentDetails.objects.all()
    resultList=[]
    for obj2 in studentList:
        rs={}
        obj=AttendanceDetails.objects.get(studentMacAddress=obj2.macAddress)
        rs["student"]=obj2
        rs["status"]=obj.attendanceStatus
        rs["date"]=obj.date
        print "Date: " ,obj.date
        resultList.append(rs)
    ls=[]
    ls.append("273")
    detail={"detail":resultList,"classId":ls}

    return render(request,'dashboard1.html',detail)

def form(request):
    return render(request, 'form.html')

def loginform(request):
     if request.method == 'POST':
         userid = request.POST.get('userid', None)
         passwd = request.POST.get('passwd', None)

         obj=ProfessorDetails.objects.filter(professorId=userid)
         for o in obj:
             if o.password == passwd:
                return redirect('/app/dashboards') 

         return render(request,'index.html')

def search(request):
    if request.method == 'POST':
        search_id = request.POST.get('datepicker', None)
        studentList=[]
        resultList=[]
        obj = AttendanceDetails.objects.filter(date__icontains=search_id)
        for o in obj:
            rs={}
            rs["status"]=o.attendanceStatus
            rs["student"]=StudentDetails.objects.get(macAddress=o.studentMacAddress)
            resultList.append(rs)
        ls=[]
        ls.append("273")
        detail={"detail":resultList,"classId":ls}
        return render(request,'form.html',detail)    
            
    else:
        return render(request,'form.html')    



        


