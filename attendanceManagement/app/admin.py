from django.contrib import admin
from app.models import *

# Register your models here.

class StudentAdmin(admin.ModelAdmin):
    list_display = ('studentId',)
    search_fields = ['studentId']

admin.site.register(StudentDetails, StudentAdmin)

admin.site.register(AttendanceDetails)

admin.site.register(ProfessorDetails)

admin.site.register(ClassDetails)

admin.site.register(ClassProfessorMapping)
