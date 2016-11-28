from __future__ import unicode_literals

from django.db import models
from datetime import datetime

# Create your models here.

class StudentDetails(models.Model):
    studentId = models.IntegerField(primary_key=True, null=False)
    firstName = models.CharField(max_length=200, null=False)
    lastName = models.CharField(max_length=200, null=False)
    macAddress = models.CharField(max_length=20, unique=True, null=False)

class AttendanceDetails(models.Model):
    attandance_id = models.AutoField(primary_key=True)
    date = models.DateTimeField(default=datetime.now, null=False)
    studentMacAddress = models.CharField(max_length=20, null=False)
    raspPieMacAddress = models.CharField(max_length=20, null=False)
    attendanceStatus = models.CharField(max_length=200, null=False)
    classId = models.CharField(max_length=200, null=False)

class ProfessorDetails(models.Model):
    professorId = models.IntegerField(primary_key=True, null=False)
    firstName = models.CharField(max_length=200, null=False)
    lastName = models.CharField(max_length=200, null=False)
    password = models.CharField(max_length=200, null=False)


class ClassDetails(models.Model):
    classId = models.CharField(max_length=200, primary_key=True, null=False)
    className = models.CharField(max_length=200, null=False)
    classStartTime = models.DateTimeField(null=False)
    classEndTime = models.DateTimeField(null=False)

class ClassProfessorMapping(models.Model):
    id = models.AutoField(primary_key=True, null=False)
    classId = models.CharField(max_length=200, null=False)
    professorId = models.IntegerField(null=False)

class StudentClassMapping(models.Model):
    id = models.AutoField(primary_key=True, null=False)
    studentId = models.IntegerField(null=False)
    classId = models.CharField(max_length=200, null=False)
