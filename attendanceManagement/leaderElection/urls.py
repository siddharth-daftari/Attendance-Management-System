from django.conf.urls import url
from django.contrib import admin
from leaderElection import views

urlpatterns = [
    url(r'^getLeader/$', views.getLeader),
    url(r'^setLeader/$', views.setLeader),
    url(r'^admin/', admin.site.urls),
]
