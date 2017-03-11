# Consumer loyalty program 



# Server

## Install

First try:

```bash
cd server
npm install
npm start
cp .env.sample .env
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
