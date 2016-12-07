from models import *
from datetime import datetime, date

def checkIfAttendanceMarkedService(jsonVar):
    """service to check if student has marked attendance or not"""
    try:
        studentMacAddressVar = jsonVar['macAddress']
        classIdVar = jsonVar['classId']
        attendanceStatusVar = 'Marked'
        returnVar = {}
        print date.today()
        if AttendanceDetails.objects.filter(date__contains=datetime.now().date(),
                                         attendanceStatus=attendanceStatusVar,
                                         studentMacAddress__iexact=studentMacAddressVar,
                                         classId=classIdVar).count() == 0:

            returnVar['data'] = False
            returnVar['message'] = "Attendance not marked yet"
            returnVar['result'] = True

        else:
            returnVar['data'] = True
            returnVar['message'] = "Attendance already marked"
            returnVar['result'] = True

        return returnVar
    except Exception as ex:
        returnVar['data'] = ""
        returnVar['message'] = ex.message
        returnVar['result'] = False

        print ex
        return returnVar


def registerStudentService(jsonVar):
    """service to register a student"""
    try:
        firstNameVar = jsonVar['firstName']
        lastNameVar = jsonVar['lastName']
        studentIdVar = jsonVar['studentId']
        macAddressVar = jsonVar['macAddress']
        classIdVar = jsonVar['classId']
        emailIdVar = jsonVar['emailId']
        returnVar = {}

        if (StudentClassMapping.objects.filter(studentId=studentIdVar, classId=classIdVar).count() == 0) and (ClassDetails.objects.filter(classId=classIdVar).count() != 0):

            if (StudentDetails.objects.filter(studentId=studentIdVar).count() == 0):
                studentDetailsVar = StudentDetails(firstName=firstNameVar,
                                               lastName=lastNameVar,
                                               studentId=studentIdVar,
                                               macAddress=macAddressVar,
                                                   email=emailIdVar)
                studentDetailsVar.save()

            #if StudentClassMapping.objects.filter(studentId=studentIdVar, classId=classIdVar).count() == 0:
            studentClassMapping = StudentClassMapping(studentId=studentIdVar, classId=classIdVar)
            studentClassMapping.save()


            returnVar['data'] = ""
            returnVar['message'] = "Student Registered"
            returnVar['result'] = True

            return returnVar
        else:
            raise Exception("student already registered.")
    except Exception as ex:
        returnVar['data'] = ""
        returnVar['message'] = ex.message
        returnVar['result'] = False

        print ex
        return returnVar


def markAttendanceService(jsonVar):
    """service to mark an attendance"""
    try:
        dateVar = datetime.now()
        studentMacAddressVar = jsonVar['studentMacAddress']
        raspPieMacAddressVar = jsonVar['raspPieMacAddress']
        attendanceStatusVar = 'Marked'
        classIdVar = jsonVar['classId']
        print "classIdVar : ", classIdVar
        returnVar = {}

        if AttendanceDetails.objects.filter(date__contains=datetime.now().date(),
                                            attendanceStatus=attendanceStatusVar,
                                            studentMacAddress=studentMacAddressVar,
                                            classId=classIdVar).count() == 0:
            attendanceDetails = AttendanceDetails(date=datetime.now(),
                                                  studentMacAddress=studentMacAddressVar,
                                                  raspPieMacAddress=raspPieMacAddressVar,
                                                  attendanceStatus=attendanceStatusVar,
                                                  classId=classIdVar)
            attendanceDetails.save()
            returnVar['data'] = ""
            returnVar['message'] = "Attendance marked"
            returnVar['result'] = True

            return returnVar
        else:
            raise Exception("Attendance already marked.")
    except Exception as ex:
        returnVar['data'] = ""
        returnVar['message'] = ex.message
        returnVar['result'] = False

        print ex
        return returnVar

def getClassDetailsService():
    """service to get all class details"""
    try:

        returnVar = {}

        #classDetails = ClassDetails.objects.filter(classDay=2,classStartTime__gt=datetime.now().time())
        classDetails = ClassDetails.objects.filter(classDay=datetime.now().weekday(), classEndTime__gt=datetime.now().time())

        if len(classDetails) != 0:
            nextClass = classDetails[0]
            studentIds = list()
            macAddresses = list()

            studentIdsQuerySet = StudentClassMapping.objects.filter(classId=nextClass.classId).only('studentId')
            for e in studentIdsQuerySet:
                studentIds.append(e.studentId)

            macAddressesQuerySet = StudentDetails.objects.filter(studentId__in=studentIds).only('macAddress')
            for e in macAddressesQuerySet:
                macAddresses.append(e.macAddress)

            classInfo = {}
            classInfo['startTime'] = datetime.now().date().__format__("%m-%d-%y") + " " + nextClass.classStartTime.__format__("%H:%M:%S")
            classInfo['endTime'] = datetime.now().date().__format__("%m-%d-%y") + " " + nextClass.classEndTime.__format__("%H:%M:%S")
            classInfo['enrolledStudents'] = macAddresses

            returnDataVar = {}
            returnDataVar[nextClass.classId] = classInfo

        else:
            raise Exception("No classes now.")

        returnVar['data'] = returnDataVar
        returnVar['message'] = "Requested class info available in data field"
        returnVar['result'] = True

        return returnVar
    except Exception as ex:
        returnVar['data'] = ""
        returnVar['message'] = ex.message
        returnVar['result'] = False

        print ex
        return returnVar