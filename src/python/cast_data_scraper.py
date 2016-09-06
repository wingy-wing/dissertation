from bs4 import BeautifulSoup
import urllib2
import json
import os

url = "https://artcastingproject.net/map/casts.php?perPage=200"
page = urllib2.urlopen(url).read()
soup = BeautifulSoup(page, "lxml")

data = {}
i = 0

for tr in soup.findAll('tr')[1:]:
    tds = tr.findAll('td')
    data ['cast' + `i`] = {
    'created' : tds[0].text,
    'name' : tds[1].text,
    'artwork' : tds[2].text,
    'dest_lat' : float(tds[3].text),
    'dest_lon' : float(tds[4].text),
    'explanation' : tds[5].text,
    'recast' : 1 == int(tds[6].text),
    'year' : int(tds[7].text),
    'month' : int(tds[8].text),
    'day' : int(tds[9].text)
    }
    i+=1
s = json.dumps(data)

filepath = os.path.join(os.path.expanduser('~'), 'workspace', 'Dissertation', 'resources', 'casts.json')
with open(filepath,'w') as f:
    f.write(s)
