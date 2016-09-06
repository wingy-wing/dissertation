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
    cast_no = 'cast' + `int(argv[0])`
    filepath = os.path.join(os.path.expanduser('~'), 'workspace', 'Dissertation', 'resources', 'casts.json')
    with open(filepath) as f:
        all_casts = yaml.safe_load(f.read().encode('utf-8'))
        
    stories = get_stories(all_casts)
    story_syns = get_syns(stories)
    jc_sims = normalise_values(jiang_conrath(story_syns, cast_no))
    
    orig_stdout = sys.stdout
    save_path = os.path.join(os.path.expanduser('~'), 'workspace', 'Dissertation', 'resources', 'text_sims.csv')
    with open(save_path, "w") as g:
        wr = csv.writer(g)
        wr.writerow(jc_sims.values())
    sys.stdout = orig_stdout

def get_syns(story_dict):
    syn_dict = OrderedDict()
    i = 0
    while i<len(story_dict):
        key = 'cast' + `i`
        syns = []
        story = []
        story = remove_stop_words((tokenise(story_dict[key])))
        for word in story:
            syns.append(adapted_lesk(story_dict[key], word))
        syn_dict[key] = syns        
        i+=1
    return syn_dict

def normalise_values(similarities_dict):
    max_val = similarities_dict[max(similarities_dict, key=lambda i: similarities_dict[i])]
    j=0
    while j < len(similarities_dict):
        key = 'cast' + `j`
        similarities_dict[key] = similarities_dict[key]/max_val
        j+=1
    return similarities_dict

def jiang_conrath(syn_dict, cast_no):
    this_cast_syns = syn_dict[cast_no]
    brown_ic = wordnet_ic.ic('ic-brown.dat')
    jc_sims = OrderedDict()
    i = 0
    while i < len(syn_dict):
        total_sim = 0.0
        key = 'cast' + `i`
        no_of_comparisons = 0.0
        for original_syn in this_cast_syns:
            if len(this_cast_syns) is not 0 and original_syn is not None:
                for comparison_syn in syn_dict[key]:
                    if len(syn_dict[key]) is not 0 and comparison_syn is not None and original_syn.pos()==comparison_syn.pos() and ((original_syn.pos() == "n") or (original_syn.pos() == "v")):
                        sim = original_syn.jcn_similarity(comparison_syn, brown_ic)
                        total_sim = total_sim + sim
                        no_of_comparisons+=1        
        i+=1
        if no_of_comparisons is not 0.0:
            jc_sims[key] = total_sim/no_of_comparisons
        else:
            jc_sims[key] = total_sim
    return jc_sims 

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
