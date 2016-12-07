from django.conf.urls import url
from django.contrib import admin
from app import views as app_view

urlpatterns = [
    url(r'^login/$', app_view.index),
    url(r'^dashboards$',app_view.dashboard),
    url(r'^form$',app_view.form),
    url(r'^mail$',app_view.mail),
    url(r'^loginform$',app_view.loginform),
    url(r'^admin/', admin.site.urls),
]