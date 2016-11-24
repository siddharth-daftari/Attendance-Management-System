from __future__ import unicode_literals

from django.db import models
from datetime import datetime

# Create your models here.

class StudentDetails(models.Model):
    firstName = models.CharField(max_length=200)
    lastName = models.CharField(max_length=200)
    studentId = models.IntegerField(primary_key=True)
    macAddress = models.CharField(max_length=200)

class AttendanceDetails(models.Model):
    attandance_id = models.AutoField(primary_key=True)
    date = models.DateTimeField(default=datetime.now)
    studentMacAddress = models.CharField(max_length=200)
    raspPieMacAddress = models.CharField(max_length=200)
    attendanceStatus = models.CharField(max_length=200)
    classId = models.IntegerField()

class ProfessorDetails(models.Model):
    firstName = models.CharField(max_length=200)
    lastName = models.CharField(max_length=200)
    professorId = models.IntegerField(primary_key=True)

class ClassDetails(models.Model):
    className = models.CharField(max_length=200)
    classId = models.IntegerField(primary_key=True)

class ClassProfessorMapping(models.Model):
    id = models.AutoField(primary_key=True)
    classId = models.IntegerField()
    professorId = models.IntegerField()
