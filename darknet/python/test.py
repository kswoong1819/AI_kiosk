import requests 
import argparse
import time

ap = argparse.ArgumentParser()
ap.add_argument("inputnum",
                help="person uid")
args = ap.parse_args()

num = args.inputnum

URL = 'http://localhost:8080/tracking'
response = requests.get(URL + "?tid=" + num) 
response.status_code 
response.text