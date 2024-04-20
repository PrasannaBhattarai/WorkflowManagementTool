from django.http import JsonResponse
import json
from django.db import connection
from django.core.mail import send_mail
from .model import model
from django.views.decorators.csrf import csrf_exempt
from django.shortcuts import render
from sklearn.feature_extraction.text import TfidfVectorizer
import pandas as pd
from sklearn.naive_bayes import MultinomialNB

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


def send_email(request, user_email):
    try:
        send_mail(
            'Workflow Management Tool Welcomes You!',
            'Thank You for signing up to Workflow Management Tool, your account has now been verified and you can start working with us starting now!',
            'workflowmanagementtool@gmail.com',  
            [user_email],
            fail_silently=False,
        )
        return JsonResponse({'success': True, 'message': 'Email sent successfully'})
    except Exception as e:
        return JsonResponse({'success': False, 'message': str(e)})
    
    
def send_rejection_email(request, user_email):
    try:
        send_mail(
            'Workflow Management Tool account Request Rejected.',
            'We are sorry to inform you but your registration was failed due to invalid credentials. Please re-fill the form with valid details.',
            'workflowmanagementtool@gmail.com',  
            [user_email],  
            fail_silently=False,
        )
        return JsonResponse({'success': True, 'message': 'Rejection email sent successfully'})
    except Exception as e:
        return JsonResponse({'success': False, 'message': str(e)})
    


# Load and preprocess the dataset
df = pd.read_csv('prepared_dataset.csv')
df = df.dropna()
df['comment_full_text'] = df['comment_full_text'].astype(str)

# Split the dataset into features (X) and labels (Y)
X = df['comment_full_text']
Y = df['task']

# Initialize and fit the TF-IDF vectorizer
tfidf_vectorizer = TfidfVectorizer()
X_tfidf = tfidf_vectorizer.fit_transform(X)

# Initialize and train the Multinomial Naive Bayes classifier
model = MultinomialNB(alpha=1)
model.fit(X_tfidf, Y)



@csrf_exempt
def classify_text(request):
    if request.method == 'POST':
        # Get the JSON data from the request body
        try:
            data = json.loads(request.body.decode('utf-8'))
            text = data.get('text')
        except json.JSONDecodeError:
            return JsonResponse({'error': 'Invalid JSON data in request body'}, status=400)

        if text is None:
            return JsonResponse({'error': 'Text data is missing from request'}, status=400)

        # Transform the input text using the TF-IDF vectorizer
        input_tfidf = tfidf_vectorizer.transform([text])

        # Make predictions using the trained model
        prediction = model.predict(input_tfidf)

        # Return the prediction result as JSON response
        return JsonResponse({'is_task': bool(prediction[0])})
    else:
        # Return an error if the request method is not POST
        return JsonResponse({'error': 'Only POST requests are allowed.'}, status=400)
