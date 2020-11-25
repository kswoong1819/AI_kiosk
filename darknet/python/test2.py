import requests 
import argparse
import time

print("c")

ap = argparse.ArgumentParser()
ap.add_argument("st")
# ap.add_argument("ed")
args = ap.parse_args()

# for i in range(int(args.st)):
    # time.sleep(3)
URL = 'http://localhost:8080/tmp/test' 
requests.get(URL+ "?uid=" + args.st)
# response = requests.get(URL+ "?uid=" + str(i)) 
# response.status_code 
# response.text
