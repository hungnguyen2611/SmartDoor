import os
import random
import time
from firebase_admin import credentials, initialize_app, storage

import requests
import json
# Init firebase with your credentials
databaseURL = 'https://mp212-ai-default-rtdb.asia-southeast1.firebasedatabase.app/'
cred = credentials.Certificate("key.json")
initialize_app(cred, {'storageBucket': 'mp212-ai.appspot.com'})





def save_record(name):
    # Put your local file path
    num = int(round(time.time() * 1000))
    local_time = time.ctime(time.time())
    new_name = name+"_"+str(local_time).replace(':', '_')+'.jpg'
    os.rename('opencv_frame_0.png', new_name)
    fileName = new_name
    bucket = storage.bucket()
    blob = bucket.blob(fileName)
    blob.upload_from_filename(fileName)
    # Opt : if you want to make public access from the URL
    blob.make_public()
    url = blob.public_url
    his_info = str(
        {
            f'\"{num}\":{{"fullname":\"{name}\","url":\"{url}\","date":\"{local_time}\"}}'}
    )

    # his_info = his_info.replace(".", "-")
    his_info = his_info.replace("\'", "")
    to_database = json.loads(his_info)
    requests.patch(url="https://mp212-ai-default-rtdb.asia-southeast1.firebasedatabase.app/History/.json", json=to_database)
    print(fileName)
    os.rename(new_name, 'opencv_frame_0.png')