# might we wish to do stemming? don't think the stemmer is working
from collections import OrderedDict
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.metrics.pairwise import linear_kernel
from nltk.stem.porter import *
import csv
import nltk
import os
import string
import sys
import yaml
import os

stemmer = PorterStemmer()

def main(argv):
    cast_no1 = int(argv[0])
    cast_no2 = int(argv[1])
    filepath = os.path.join(os.path.expanduser('~'), 'workspace', 'Dissertation', 'resources', 'casts.json')
    with open(filepath) as f:
        all_casts = yaml.safe_load(f.read().encode('utf-8'))
    stories = get_stories(all_casts)

    vect = TfidfVectorizer(tokenizer = tokenise, stop_words='english')
    tfidf = vect.fit_transform(stories.values())
    feature_names = vect.get_feature_names()

    cosine_similarities = linear_kernel(tfidf[cast_no1:cast_no1+1], tfidf).flatten()
    sim_list = []
    for el in cosine_similarities:
        sim_list.append(el)

    save_path = os.path.join(os.path.expanduser('~'), 'workspace', 'Dissertation', 'resources', 'results', 'Survey Cast Similarities', 'tfidf.txt')
    orig_stdout = sys.stdout
    with open(save_path, "a") as g:
        g.write('cast' + `cast_no1` + 'cast' + `cast_no2` + ":" + `sim_list[cast_no2]`+",")
    sys.stdout = orig_stdout

def get_stories(casts):
    stories = OrderedDict()
    i = 0
    while i < len(casts):
        key = 'cast' + `i`
        stories[key] = (casts[key]["name"] + " " + casts[key]["explanation"]).lower().translate(string.maketrans("", ""), string.punctuation)
        i+=1
    return stories

def stem_tokens(tokens, stemmer):
    stemmed = []
    for item in tokens:
        stemmed.append(stemmer.stem(item))
    return stemmed

def tokenise(text):
    tokens = nltk.word_tokenize(text)
    stems = stem_tokens(tokens, stemmer)
    return stems

if __name__ == "__main__":
    main(sys.argv[1:])
