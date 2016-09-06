from nltk.tokenize import RegexpTokenizer
from nltk.corpus import stopwords
from nltk.stem.porter import PorterStemmer
from gensim import corpora, models
import gensim
import yaml
import os
import string
from collections import OrderedDict
from gensim.similarities import MatrixSimilarity, Similarity

def get_stories(casts):
    stories = OrderedDict()
    i = 0
    while i < len(casts):
        key = 'cast' + `i`
        stories[key] = (casts[key]["name"] + " " + casts[key]["explanation"]).lower().translate(string.maketrans("", ""), string.punctuation)
        i+=1
    return stories



tokenizer = RegexpTokenizer(r'\w+')

# create English stop words list
en_stop = stopwords.words('english')

# Create p_stemmer of class PorterStemmer
p_stemmer = PorterStemmer()

# create sample documents
filepath = os.path.join(os.path.expanduser('~'), 'workspace', 'Dissertation', 'resources', 'casts.json')
with open(filepath) as f:
    all_casts = yaml.safe_load(f.read().encode('utf-8'))
stories = get_stories(all_casts)

# compile sample documents into a list
doc_set = stories.values()

# list for tokenized documents in loop
texts = []

# loop through document list
for i in doc_set:

    # clean and tokenize document string
    raw = i.lower()
    tokens = tokenizer.tokenize(raw)

    # remove stop words from tokens
    stopped_tokens = [i for i in tokens if not i in en_stop]

    # stem tokens
    stemmed_tokens = [p_stemmer.stem(i) for i in stopped_tokens]

    # add tokens to list
    texts.append(stemmed_tokens)

# turn our tokenized documents into a id <-> term dictionary
dictionary = corpora.Dictionary(texts)

# convert tokenized documents into a document-term matrix
corpus = [dictionary.doc2bow(text) for text in texts]

# generate LDA model
ldamodel = gensim.models.ldamodel.LdaModel(corpus, num_topics=30, id2word = dictionary, passes=20)

index = MatrixSimilarity(ldamodel[corpus])
index.save("simIndex.index")

print(ldamodel.print_topics(num_topics=30, num_words=2))

doc = stories['cast56']
vec_bow = dictionary.doc2bow(doc.lower().split())
vec_lda = ldamodel[vec_bow]

sims = index[vec_lda]
sims = sorted(enumerate(sims), key=lambda item: -item[1])
print sims
