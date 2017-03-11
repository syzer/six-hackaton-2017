import os
from os import path

import speech_recognition as sr
import json

from flask import Flask
from flask import make_response
from flask import request, abort

from flask.ext import restful
from flask.ext.restful import reqparse

import sys

app = Flask(__name__)

def output_json(data, code, headers=None):
	resp = make_response(json.dumps(data), code)
	resp.headers.extend(headers or {})
	return resp

DEFAULT_PRESENTATIONS = {'applicaiton/json': output_json}
api = restful.Api(app)
api.representations = DEFAULT_PRESENTATIONS

from py_rest_service import app, api 

def transcript_audio(audio_recording):
	r = sr.Recognizer()
	with sr.AudioFile(audio_recording) as source:
	    audio = r.record(source) # read the entire audio file

	BING_KEY = "332b029a26854d4896f80c7ebac22d86" # Microsoft Bing Voice Recognition API keys 32-character lowercase hexadecimal strings
	try:
	    return (r.recognize_bing(audio, key=BING_KEY)).encode("utf-8")
	except sr.UnknownValueError:
	    return ("Microsoft Bing Voice Recognition could not understand audio").encode("utf-8")
	except sr.RequestError as e:
	    return("Could not request results from Microsoft Bing Voice Recognition service; {0}".format(e)).encode("utf-8")


class TranscriptAudio(restful.Resource):
	def post(self):

		print('I am in....')

		if 'file' not in request.files:
			flash('No file part')
			return redirect(request.url)
		
		file = request.files['file']

		return ({"transcript": transcript_audio(file)})


class Root(restful.Resource):
    def get(self):
    	print("ok, it is root")
        return {
            'status': 'OK'
        }


api.add_resource(TranscriptAudio, '/transcript-voice')

api.add_resource(Root, '/')




