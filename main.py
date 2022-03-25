from recognize import *
from connect import *
from Adafruit_IO import Client, Data, MQTTClient
from firebase import save_record
from device import readSerial, writeSerial




def connect():
    # aio = Client(AIO_USERNAME, AIO_KEY)
    # client = MQTTClient(AIO_USERNAME, AIO_KEY)
    # client.on_connect = connected
    # client.on_disconnect = disconnected
    # client.on_message = message
    # client.on_subscribe = subscribe
    #
    # client.connect()
    # client.loop_background()

    print('Publishing a new message every 10 seconds (press Ctrl-C to quit)...')
    facenet_model = load_model('model/facenet_keras.h5')
    # lcd_scr_data = Data(value="Loaded successfully...")
    # client.publish(fr_screen, value=lcd_scr_data.value)
    print("Loaded successfully...")
    # lcd_scr_data = Data(value="Welcome")
    # client.publish(fr_screen, value=lcd_scr_data.value)
    writeSerial("Touch the button")
    while True:
        ret = readSerial()
        ret = True
        img_val = None
        if ret:
            # client.publish(fr_button2, 1)
            # lcd_scr_data = Data(value="Capturing...")
            # client.publish(fr_screen, value=lcd_scr_data.value)
            writeSerial("Capturing...")
            print("Capturing...")
            img_val = get_image_pil()
            # lcd_scr_data = Data(value="Captured")
            # client.publish(fr_screen, value=lcd_scr_data.value)
            writeSerial("Captured")
            print("Captured")

        if img_val:
            svm_model = load('svm_model.joblib')
            try:
                screen, name, prob = predict('opencv_frame_0.png', svm_model, facenet_model)
                # lcd_scr_data = Data(value=screen)
                # client.publish(fr_screen, value=lcd_scr_data.value)
                writeSerial(screen)
                save_record(name)
                ret = readSerial()
                # if name != "Stranger":
                #     client.publish(fr_button, value=0)
            except:
                # lcd_scr_data = Data(value="No face detected !")
                # client.publish(fr_screen, value=lcd_scr_data.value)
                writeSerial("No face detected!")
                print("No face detected !")
                ret = readSerial()

        # client.publish(fr_button2, 0)





def main():
    connect()


if __name__ == "__main__":
    main()
    # #In case you want to train the svm model again #
    # facenet_model = load_model('model/facenet_keras.h5')
    # svm_model = train_svm_model(facenet_model)
