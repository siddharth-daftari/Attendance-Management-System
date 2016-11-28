from models import *
from datetime import datetime

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
                                               macAddress=macAddressVar,
                                               classId=classIdVar)
            studentDetailsVar.save()

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

        print returnVar
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

        print returnVar
        return returnVar
