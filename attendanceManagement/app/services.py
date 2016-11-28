from models import *
from datetime import datetime,tzinfo

import pytz
#mytz = pytz.timezone('US/Pacific')
#currDate = pytz.utc.localize(datetime.utcnow(), is_dst=None).astimezone(mytz)


def registerStudentService(jsonVar):
    """service to register a student"""
    try:
        firstNameVar = jsonVar['firstName']
        lastNameVar = jsonVar['lastName']
        studentIdVar = jsonVar['studentId']
        macAddressVar = jsonVar['macAddress']
        classIdVar = jsonVar['classId']
        returnVar = {}

        if (StudentDetails.objects.filter(studentId=studentIdVar).count() == 0) and (StudentClassMapping.objects.filter(studentId=studentIdVar, classId=classIdVar).count() == 0) and (ClassDetails.objects.filter(classId=classIdVar).count() != 0):
            studentDetailsVar = StudentDetails(firstName=firstNameVar,
                                               lastName=lastNameVar,
                                               studentId=studentIdVar,
                                               macAddress=macAddressVar)

            if StudentClassMapping.objects.filter(studentId=studentIdVar, classId=classIdVar).count() == 0:
                studentClassMapping = StudentClassMapping(studentId=studentIdVar, classId=classIdVar)

                studentDetailsVar.save()
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
        returnVar = {}

        if AttendanceDetails.objects.filter(date=datetime.now(),
                                            attendanceStatus=attendanceStatusVar,
                                            studentMacAddress=studentMacAddressVar,
                                            classId=classIdVar).count() == 0:
            attendanceDetails = AttendanceDetails(date=dateVar,
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


        classDetails = ClassDetails.objects.filter(classDay=datetime.now().weekday(), classStartTime__gt=datetime.now().time())

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