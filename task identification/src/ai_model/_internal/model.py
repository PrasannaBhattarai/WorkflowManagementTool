# Importing libraries
import pandas as pd
from sklearn.naive_bayes import MultinomialNB
from sklearn.feature_extraction.text import TfidfVectorizer

# Reading data from the CSV file
df = pd.read_csv('prepared_dataset.csv')

# Dropping rows with missing values in the text column
df = df.dropna()

# Converting text column to strings
df['comment_full_text'] = df['comment_full_text'].astype(str)

# Splitting the data into features (X) and labels (Y)
X = df['comment_full_text']
Y = df['task']

# Creating and fitting the TF-IDF vectorizer on the training data
tfidf_vectorizer = TfidfVectorizer()
X_tfidf = tfidf_vectorizer.fit_transform(X)

# Initializing the MultinomialNB model
model = MultinomialNB(alpha=1)

# Training the model on the complete dataset
model.fit(X_tfidf, Y)

# Continuous loop to predict text entered in the terminal
while True:
    # Getting prompt from the user
    user_input = input("Enter text: ")

    # Transforming the input text using the TF-IDF vectorizer
    input_tfidf = tfidf_vectorizer.transform([user_input])

    # Making predictions using the trained model
    prediction = model.predict(input_tfidf)

    # Printing the prediction
    print("Prediction:", prediction[0])
