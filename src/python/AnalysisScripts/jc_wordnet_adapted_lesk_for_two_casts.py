from collections import OrderedDict
from nltk.corpus import stopwords
from pywsd.lesk import adapted_lesk
from nltk.corpus import wordnet
from nltk.corpus import brown
from nltk.corpus import wordnet_ic
from nltk.stem.porter import *
import nltk
import string
import yaml
import json
import os
import sys
import csv
import math

stemmer = PorterStemmer()

def main(argv):
    cast_no1 = 'cast' + `int(argv[0])`
    cast_no2 = 'cast' + `int(argv[1])`
    filepath = os.path.join(os.path.expanduser('~'), 'workspace', 'Dissertation', 'resources', 'casts.json')
    with open(filepath) as f:
        all_casts = yaml.safe_load(f.read().encode('utf-8'))

    stories = get_stories(all_casts)
    story_syns = get_syns(stories, cast_no1, cast_no2)
    story_sim = single_jiang_conrath(cast_no1, cast_no2, story_syns)
    print story_sim

    orig_stdout = sys.stdout
    save_path = os.path.join(os.path.expanduser('~'), 'workspace', 'Dissertation', 'resources', 'results', 'Survey Cast Similarities', 'jc_wordnet_adapted_lesk1.dat')
    with open(save_path, "a") as g:
        g.write(cast_no1 + cast_no2 + ":" + `story_sim`+",")
    sys.stdout = orig_stdout

def get_syns(story_dict, cast_no1, cast_no2):
    syn_dict = OrderedDict()
    syns1 = []
    story1 = []
    story1 = remove_stop_words((tokenise(story_dict[cast_no1])))
    for word in story1:
        syns1.append(adapted_lesk(story_dict[cast_no1], word))
    syn_dict[cast_no1] = syns1
    syns2 = []
    story2 = []
    story2 = remove_stop_words((tokenise(story_dict[cast_no1])))
    for word in story2:
        syns2.append(adapted_lesk(story_dict[cast_no2], word))
    syn_dict[cast_no2] = syns2

    return syn_dict

def normalise_values(similarities_dict):
    max_val = similarities_dict[max(similarities_dict, key=lambda i: similarities_dict[i])]
    j=0
    while j < len(similarities_dict):
        key = 'cast' + `j`
        similarities_dict[key] = similarities_dict[key]/max_val
        j+=1
    return similarities_dict

def single_jiang_conrath(cast_no1, cast_no2, syn_dict):
    brown_ic = wordnet_ic.ic('ic-brown.dat')
    synsets1 = syn_dict[cast_no1]
    synsets2 = syn_dict[cast_no2]
    total_sim = 0.0
    no_of_comparisons = 0.0
    for original_syn in synsets1:
        for syn1 in synsets1:
            if len(synsets1) is not 0 and syn1 is not None:
                for syn2 in synsets2:
                    if len(synsets2) is not 0 and syn2 is not None and syn1.pos()==syn2.pos() and ((syn1.pos() == "n") or (syn2.pos() == "v")):
                        sim = syn1.jcn_similarity(syn2, brown_ic)
                        total_sim = total_sim + sim
                        no_of_comparisons+=1
    return total_sim/no_of_comparisons

def get_stories(casts):
    stories = OrderedDict()
    i = 0
    while i < len(casts):
        key = 'cast' + `i`
        text = (casts[key]["name"] + " " + casts[key]["explanation"]).lower().translate(string.maketrans("", ""), string.punctuation)
        stories[key] = text
        i+=1
    return stories

def remove_stop_words(lst):
    no_stops = [el for el in lst if not el in stopwords.words('english')]
    return no_stops

def stem_tokens(tokens, stemmer):
    stemmed = []
    for item in tokens:
        stemmed.append(stemmer.stem(item))
    return stemmed

def tokenise(text):
    tokens = nltk.word_tokenize(text)
    return tokens

if __name__ == '__main__':
    main(sys.argv[1:])
