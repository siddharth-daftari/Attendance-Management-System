from django.shortcuts import render,redirect
from models import AttendanceDetails,StudentDetails,ProfessorDetails
from django.conf import settings
from django.core.mail import send_mail
import datetime


def index(request):
    return render(request, 'index.html')

def dashboard(request):
    detail=getAttendance(datetime.datetime.now().date())
    return render(request,'dashboard1.html',detail)

def form(request):
    if request.method == 'POST':
        search_id = request.POST.get('datepicker', None)
        s=datetime.datetime.strptime(search_id, "%Y-%m-%d").date()
        if s <= datetime.datetime.now().date():
            detail=getAttendance(s)
            return render(request,'form.html',detail)
        
    ls=["CMPE273"]
    proffName=["Professor Sithu"]
    detail={"classId":ls,"proffesorName":proffName}
    return render(request, 'form.html',detail)

def loginform(request):
     if request.method == 'POST':
         userid = request.POST.get('userid', None)
         passwd = request.POST.get('passwd', None)

         obj=ProfessorDetails.objects.filter(professorId=userid)
         for o in obj:
             if o.password == passwd:
                return redirect('/app/dashboards') 

         return render(request,'index.html')


    #recievers list to be fetched from database
    # email_recievers_list=['siddharthrajesh.daftari@sjsu.edu','seema.rohilla@sjsu.com','madhur.khandelwal@sjsu.edu','akshay.sonvane@sjsu.edu']
    # send_mail('****Reminder For CMPE 273****', 'Please Turn on your bluetooth as the class starts in 5 mins', settings.EMAIL_HOST_USER,
    # email_recievers_list, fail_silently=False)
def getAttendance(search_id):
    studentList=StudentDetails.objects.all()
    total_student=studentList.count()
    resultList=[]
    totalPresent=0
    for obj2 in studentList:
        obj=AttendanceDetails.objects.filter(studentMacAddress=obj2.macAddress,date__icontains=search_id,classId__icontains='CMPE273')
        if(obj.count()==0):
            rs={} 
            rs["student"]=obj2
            rs["status"]='absent'
            rs["date"]=search_id
            resultList.append(rs)
        else:
            rs={} 
            rs["student"]=obj2
            rs["status"]="present"
            rs["date"]=search_id
            totalPresent=totalPresent+1
            resultList.append(rs)
    ls=["CMPE273"]
    proffName=["Professor Sithu"]
    totalPresent_list=[totalPresent]
    total_student_list=[total_student]
    total_absent=[total_student-totalPresent]
    
    detail={"detail":resultList,"classId":ls,"proffesorName":proffName,"totalPresent":totalPresent_list,"total_student_list":total_student_list,"total_absent":total_absent}        
    return detail  

def mail(request):
    ls=["CMPE273"]
    proffName=["Professor Sithu"]
    if request.method == 'POST':
        subject_content = request.POST.get('subject', None)
        body_content = request.POST.get('mailbody', None)
        #recievers list fetched from database
        studentList=StudentDetails.objects.all()
        email_recievers_list=[]
        for student in studentList:
            email_recievers_list.append(student.email)
            
        send_mail(subject_content,body_content, settings.EMAIL_HOST_USER,
        email_recievers_list, fail_silently=False)
        response=["Message succesfully sent !"]
        detail={"classId":ls,"proffesorName":proffName,"response":response}
        return render(request,'mailing.html',detail)    

    detail={"classId":ls,"proffesorName":proffName}
    return render(request,'mailing.html',detail)    





  
     



        


