from django.http import JsonResponse
from django.db import connection

#project data analytics
def chart_data(request):
    with connection.cursor() as cursor:
        cursor.execute("""
            SELECT p.project_name, COUNT(pu.user_id) as user_count
            FROM project_user pu
            JOIN project p ON p.project_id = pu.project_id
            GROUP BY p.project_name
            ORDER BY user_count DESC
            LIMIT 5;
        """)
        rows = cursor.fetchall()

    labels = []
    data = []

    for row in rows:
        labels.append(row[0])
        data.append(row[1])

    chart_data = {
        'labels': labels,
        'data': data,
    }

    return JsonResponse(chart_data)


def project_type_data(request):
    with connection.cursor() as cursor:
        cursor.execute("""
            SELECT project_type, COUNT(*) as count
            FROM project
            GROUP BY project_type;
        """)
        rows = cursor.fetchall()

    labels = []
    data = []

    for row in rows:
        labels.append(row[0])
        data.append(row[1])

    chart_data = {
        'labels': labels,
        'data': data,
    }

    return JsonResponse(chart_data)



def project_status_data(request):
    with connection.cursor() as cursor:
        cursor.execute("""
            SELECT project_status, COUNT(*) as count
            FROM project
            GROUP BY project_status;
        """)
        rows = cursor.fetchall()

    labels = []
    data = []

    for row in rows:
        labels.append(row[0])
        data.append(row[1])

    chart_data = {
        'labels': labels,
        'data': data,
    }

    return JsonResponse(chart_data)


def project_count(request):
    with connection.cursor() as cursor:
        cursor.execute("SELECT COUNT(project_id) FROM project;")
        row = cursor.fetchone()
        project_count = row[0] if row else 0

    return JsonResponse({'project_count': project_count})


# user data
def project_monthly_data(request):
    with connection.cursor() as cursor:
        cursor.execute("""
            SELECT MONTHNAME(start_date) AS month, COUNT(*) AS num_records
            FROM project
            GROUP BY MONTH(start_date);
        """)
        rows = cursor.fetchall()

    data = [{'month': row[0], 'num_records': row[1]} for row in rows]

    return JsonResponse({'data': data})


def project_roles_data(request):
    with connection.cursor() as cursor:
        cursor.execute("""
            SELECT project_role, COUNT(project_role)
            FROM project_user
            GROUP BY project_role;
        """)
        rows = cursor.fetchall()

    data = [{'project_role': row[0], 'count': row[1]} for row in rows]

    return JsonResponse({'data': data})


def top_users_data(request):
    with connection.cursor() as cursor:
        cursor.execute("""
            SELECT CONCAT(first_name, ' ', last_name) AS name, user_ratings
            FROM user
            ORDER BY user_ratings DESC
            LIMIT 5;
        """)
        rows = cursor.fetchall()

    data = [{'name': row[0], 'user_ratings': row[1]} for row in rows]

    return JsonResponse({'data': data})

def user_count(request):
    with connection.cursor() as cursor:
        cursor.execute("SELECT COUNT(user_id) FROM user;")
        row = cursor.fetchone()
        user_count = row[0] if row else 0

    return JsonResponse({'user_count': user_count})