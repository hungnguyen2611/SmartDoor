import serial.tools.list_ports
import  sys
# from  Adafruit_IO import  MQTTClient

AIO_FEED_ID = "fr-button"
AIO_USERNAME = "hungak01"
AIO_KEY = "aio_Nrsd87hRDxU3HWNPQzvuQLQaDNxt"

def  connected(client):
    print("Ket noi thanh cong...")
    client.subscribe(AIO_FEED_ID)

def  subscribe(client , userdata , mid , granted_qos):
    print("Subcribe thanh cong...")

def  disconnected(client):
    print("Ngat ket noi...")
    sys.exit (1)

def  message(client , feed_id , payload):
    print("Nhan du lieu: " + payload)
    ser.write((str(payload) + "#").encode())

# client = MQTTClient(AIO_USERNAME , AIO_KEY)
# client.on_connect = connected
# client.on_disconnect = disconnected
# client.on_message = message
# client.on_subscribe = subscribe
# client.connect()
# client.loop_background()

def getPort():
    ports = serial.tools.list_ports.comports()
    N = len(ports)
    commPort = "None"
    for i in range(0, N):
        port = ports[i]
        strPort = str(port)
        if "USB Serial Device" in strPort:
            splitPort = strPort.split(" ")
            commPort = (splitPort[0])
    return commPort

ser = serial.Serial( port=getPort(), baudrate=115200)

mess = ""
def processData(data):
    data = data.replace("!", "")
    data = data.replace("#", "")
    splitData = data.split(":")
    print(splitData)
    if splitData[0] == "BUTTON2":
        if splitData[1] == "1":
            return True
        else:
            return False
    return False

def readSerial():
    bytesToRead = ser.inWaiting()
    ret = False
    if (bytesToRead > 0):
        mess = ser.read(bytesToRead).decode("UTF-8")
        if ("#" in mess) and ("!" in mess):
            start = mess.find("!")
            end = mess.find("#")
            ret = processData(mess[start:end + 1])
    return ret
def writeSerial(str):
    ser.write((str+"#").encode())

