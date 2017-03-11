# Consumer loyalty program 

DESCRIPTION TODO

# Server

## Install

First try:

```bash
cd server
npm install
cp .env.example .env
npm start
```

and then visit:
[http://localhost:3000/]()


## Query language

To query things in endpoints please lookinto
[json-server](https://github.com/typicode/json-server)

## Frontend (first try)

[http://localhost:3000/assets/index.html]()

## Posting product for tests

```bash
nodemon -d 1 -w ./ -e js, -x 'curl localhost:3000/user-reviews/  -H "Content-Type: application/json"  -d \'{"data":"LEtter - see what happens"}\''
```

## Sentiments: get all good german ones for product 1 

```bash
curl 'http://localhost:3000/sentiments?lang=de&sentiment=good&productId=1&_expand=product' -s | jq
```

## Extracting user network tweets social media

```bash
curl 'localhost:3000/user-tweets?twitter=syzer3'
```

## `/purchases` API

```bash
curl 'localhost:3000/purchases?_expand=user'
 ```

with discounts
```bash
curl 'localhost:3000/purchases?_expand=product&_expand=user'
``` 

## Voice recognition
Tested on Python 2.7

virtualenv venv
source venv/bin/activate
pip install -r requirements.txt

Do the 
```bash
heroku create
```
Add your key to the path
```bash
heroku config:set --app damp-chamber-61545 BING_VOICE_API_KEY="super secret key here"
```

To set up Bing voice API key use
```bash
heroku config:set --app secure-basin-20797 BING_VOICE_API_KEY="111keepdreamingecab33b12"
```

Deploy to heroku (for heroku)
```bash
git subtree push --prefix ml heroku master
```
Make sure only one instance is running (for heroku)
```bash 
heroku ps:scale web=1
``` 

Give it a spin
```bash
curl localhost:5000/transcript-voice -F "file=@english.wav"
```

