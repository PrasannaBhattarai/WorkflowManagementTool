"""
URL configuration for workflowtool project.

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/5.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from .views import chart_data, project_type_data, project_status_data, project_count,  project_monthly_data, project_roles_data, top_users_data, user_count


urlpatterns = [
    path('admin/', admin.site.urls),
    path('chart-data/', chart_data, name='chart-data'),
     path('project-type-data/', project_type_data, name='project-type-data'),
     path('project-status-data/', project_status_data, name='project-status-data'),
     path('project-count/', project_count, name='project-count'),
     path('project-monthly-data/', project_monthly_data, name='project-monthly-data'),
     path('project-roles-data/', project_roles_data, name='project-roles-data'),
     path('top-users-data/', top_users_data, name='top-users-data'),
     path('user-count/', user_count, name='user-count'),
]
