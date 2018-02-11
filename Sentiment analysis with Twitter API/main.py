import nltk
import tweepy
from nltk.corpus import stopwords
import requests
from pymongo import MongoClient
import re
import matplotlib.pyplot as plt
from collections import Counter
import numpy as np
from tqdm import tqdm
import time
import math
#nltk.download('stopwords')
# Consumer keys and access tokens, used for OAuth
consumer_key = 'm0c5PKS3ZkMOzRKpLRU3HfFxS'
consumer_secret = 'iG7gbwFXvFnbxREMvPQwkrCwNnUPVrPZ9svZht8ChslVFJMT95'
access_token = '72530556-lciXhI3MNNDqVWMIrMedkSJiy1KJbEExK7rmGb3Uq'
access_token_secret = 'x2IHPJbxDGdnHPboxCZiFD6EKfuFlwUMDsjq8cwiDv1qe'

US_WOEID=23424977
wordsBeforeStopwords = {}
wordsAfterStopwords  = {}


#The top 5 only
def getTrends(api):
    data= api.trends_place(US_WOEID)[0]
    trends=data['trends']
    return [trend['name'] for trend in trends][:5]

def AnalyzeText(text,trend):
    text=text.lower()
    text= re.sub(r"http\S+", "", text) #remove websites

    text=text.replace(trend.lower(),"")#remove the trend
    text=re.sub(r'[^\w]', ' ', text) #remove symbols
    text=''.join([i for i in text if not i.isdigit()]) #remove numbers
    # Tokenization
    words = nltk.word_tokenize(text)
    # Normalization
    words = [word.lower() for word in words if word.isalpha()]
    # Remove Stop Words
    Frequency(words,0)
    filtered_words = [word for word in words if word not in stopwords.words('english')]
    Frequency(filtered_words, 1)
    # Merge the words
    return ' '.join(filtered_words)



def Frequency(words,i):
    if(i==0):
        for word in words:
            count = wordsBeforeStopwords .get(word, 0)
            wordsBeforeStopwords[word] = count + 1
    if(i==1):
        for word in words:
            count = wordsAfterStopwords.get(word, 0)
            wordsAfterStopwords[word] = count + 1

def zipf(trend):
    plt.gcf().clear()
    plt.figure(figsize=(18, 4))
    plt.title("Zipf for " + trend)
    counts = Counter(wordsBeforeStopwords)
    labels, values = zip(*counts.items())
    # sort your values in descending order
    indSort = np.argsort(values)[::-1]
    # rearrange your data
    values = np.array(values)[indSort[:]]
    values= [math.log10(v) for v in values] #log10
    labels = range(1, len(labels) + 1)
    labels = [math.log10(l) for l in labels]
    values = [(v - min(values)) / (max(values) - min(values)) for v in values] #normalization
    labels = [(v - min(labels)) / (max(labels) - min(labels)) for v in labels] #normalization
    plt.plot(labels, values, marker=".")
    plt.xlabel("Frequency rank of token")
    plt.ylabel("Absolute frequency of token")
    plt.savefig("Zipf" + "_" + trend, bbox_inches='tight')
    plt.gcf().clear()

#Top 50
def FrequencyHistogram(i,imagename,trend):
        plt.gcf().clear()
        plt.figure(figsize=(18, 4))
        if i==0:
            plt.title("Frequency Histogram Before Stopwords for "+trend)
            counts = Counter(wordsBeforeStopwords)
            wordsBeforeStopwords.clear()
        else:
            plt.title("Frequency Histogram After Stopwords for "+trend)
            counts = Counter(wordsAfterStopwords)
            wordsAfterStopwords.clear()

        labels, values = zip(*counts.items())

        # sort your values in descending order
        indSort = np.argsort(values)[::-1]

        # rearrange your data
        labels = np.array(labels)[indSort[:50]] #Only the first 50
        values = np.array(values)[indSort[:50]] #Only the first 50

        indexes = np.arange(len(labels))
        plt.xlabel("Word")
        plt.ylabel("Frequency")
        bar_width = 0.25
        plt.bar(indexes, values)
        # add labels
        plt.xticks(indexes + bar_width, labels,rotation=80)
        plt.savefig(imagename, bbox_inches='tight')
        plt.gcf().clear()

#Find the sentiment for all the trends
def SentimentTrends(trends,db):
    fig = plt.figure(figsize=(10, 9))

    fig.canvas.set_window_title("Trends sentiment")
    fig.subplots_adjust(top=0.95)
    plt.subplots_adjust(hspace=1.5)
    figure = {trends[0]: 321, trends[1]: 322, trends[2]: 323, trends[3]: 324, trends[4]: 325}
    pbar = tqdm(total=5)
    pbar.set_description("Text processing for sentiment")
    for trend in trends:
        positive = 0
        negative = 0
        neutral = 0
        data = db[trend]
        for item in data.find():
            if(item['proces-text']!=""):
                r = requests.post("http://text-processing.com/api/sentiment/", data={'text': item['proces-text']})
                rjson = r.json()
                positive+=rjson['probability']['pos']
                negative+=rjson['probability']['neg']
                neutral+=rjson['probability']['neutral']
                data.update({'_id': item['_id']}, {'$set': {'label': rjson['label']}}, True)
                data.update({'_id': item['_id']}, {'$set' : {'positive' : rjson['probability']['pos'] }}, True)
                data.update({'_id': item['_id']}, {'$set': {'negative': rjson['probability']['neg']}}, True)
                data.update({'_id': item['_id']}, {'$set': {'neutral': rjson['probability']['neutral']}}, True)

        ax=plt.subplot(figure[trend])
        ax.set_title(trend)
        # Data to plot
        labels = 'Positive', 'Negative', 'Neutral'
        sizes = [positive, negative, neutral]
        colors = ['gold', 'yellowgreen', 'lightcoral']
        explode = (0, 0, 0)  # explode 1st slice
        # Plot
        ax.pie(sizes, explode=explode, labels=labels, colors=colors,autopct='%1.1f%%', shadow=True, startangle=140)
        ax.axis('equal')
        pbar.update(1)
    pbar.close()
    plt.savefig('sentiment.png', bbox_inches='tight')


def SentimentUser(trends,db): #For the average sentiment of each user
    users = {}
    for trend in trends:
        data = db[trend]
        for item in data.find():
            key = item['user']['id_str']
            if not (key in users):
                users[key]=[]
                users[key].append(item["positive"])
                users[key].append(item["negative"])
                users[key].append(item["neutral"])
                users[key].append(1)
            else:
                users[key][0]= float(users[key][0]) + float(item["positive"])
                users[key][1] = float(users[key][1]) +float(item["negative"])
                users[key][2] = float(users[key][2])+float(item["neutral"])
                users[key][3] += 1

        text_file = open("sent_" + trend + ".txt", "w")
        for k, value in users.items():
            positive = value[0]/value[3]
            negative = value[1] / value[3]
            neutral =value[2] / value[3]
            label=max(positive,negative,neutral)
            sentiment=""
            for i in range(0,3):
                if label == positive and label != neutral and label != negative:
                    sentiment="positive"
                elif label == negative and label != neutral and label != positive:
                    sentiment="negative"
                elif label == neutral and label != negative and label != positive:
                    sentiment="neutral"
                else:
                    sentiment="undefined"


            text_file.write(k+": "+sentiment+"\n")
        users.clear()
        text_file.close()
		
#Cumulative Distribution Function
def CDF(trends,db):
    users = {}
    for trend in trends:
        data = db[trend]
        for item in data.find():
            key = item['user']['id_str']
            if (not (key in users)):
                if float(item['user']['friends_count'])!=0:
                    users[key] = float(item['user']['followers_count'])/float(item['user']['friends_count'])
                else:
                    users[key]= 0
    plt.gcf().clear()
    plt.figure(figsize=(18, 4))
    plt.title("Cumulative Distribution Function" )
    counts = Counter(users)
    labels, values = zip(*counts.items())

    # sort your values in descending order
    indSort = np.argsort(values)[::]
    # rearrange your data
    values = np.array(values)[indSort[::-1]]

    values = [math.exp(-v ** 2) for v in values]
    plt.plot(values, 'r--')
    plt.savefig("CDF", bbox_inches='tight')
    plt.gcf().clear()

def limit_handled(cursor):
    while True:
        try:
            yield cursor.next() #like return, except the function will return a generator
        except tweepy.TweepError:
            print("\n Limit Error from Twitter! Please, wait 15 minutes.")
            time.sleep(15*61)

def main():
    auth = tweepy.OAuthHandler(consumer_key,consumer_secret)
    auth.set_access_token(access_token, access_token_secret)

    api = tweepy.API(auth)
    trends = getTrends(api)

    client = MongoClient('localhost',27017)

    client.drop_database('OurDatabase')

    # Use OurDatabase database. If it doesn't exist, it will be created.
    db = client["OurDatabase"]
    for trend in trends:
        count = 0
        pbar = tqdm(total=1500)
        pbar.set_description("Collect tweets for " + trend)
        collection = db[trend]
        for tweet in limit_handled(tweepy.Cursor(api.search, q=trend).items()):
            if count == 1500:
                break
            if not tweet.text.startswith("RT @"):
                # Text Pre-processing
                tweet._json["proces-text"] = AnalyzeText(tweet.text, trend)
                if (len(tweet._json["proces-text"].replace(" ", "")) != 0 and tweet._json["lang"]) == "en":
                    count = count + 1
                    collection.insert(tweet._json)   #Add in MongoDB
                    pbar.update(1)
        pbar.close()
        zipf(trend)
        print("The zipf for "+trend+" saved!")
        FrequencyHistogram(0, "BeforeStopwords_"+trend,trend)
        print("The image with the histogram before stopwords for "+trend+" saved!")
        FrequencyHistogram(1, "AfterStopwords_"+trend.strip(), trend)
        print("The image with the histogram after stopwords for "+trend+" saved!")
        time.sleep(1)

    SentimentTrends(trends, db)
    print("The image with the sentiment saved!")
    SentimentUser(trends,db)
    print("The files with average sentiment are ready!")
    CDF(trends,db)

if __name__ == "__main__":
    main()