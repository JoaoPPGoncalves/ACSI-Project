import cv2
import numpy as np
import time
from confluent_kafka import Producer

cap = cv2.VideoCapture('http://192.168.28.179:4747/video')

#parking_slots = [(100, 280), (220, 280), (325, 280), (435, 280), (520, 280)] -> Maquete
parking_slots = [(200, 240), (200, 90)]# -> Estacionamento real
#rect_width, rect_height = 50, 63# -> Maquete
rect_width, rect_height = 260, 100# -> Estacionamento real
color = (0,0,180)# -> Estacionamento real
thick = 3# -> Estacionamento real
threshold = 200# -> Estacionamento real
#color = (0,0,255)# -> Maquet
#thick = 2# -> Maquet
#threshold = 60# -> Maquet
last_call_time = time.time()
prevFreeslots=0

# Create a Kafka producer
producer = Producer({'bootstrap.servers': 'localhost:9092'})


def convert_grayscale(frame):
    # Convert the image to grayscale
    gray = cv2.cvtColor(frame, cv2.COLOR_BGR2GRAY)

    # Apply threshold to create a binary image
    _, binary = cv2.threshold(gray, 110, 255, cv2.THRESH_BINARY)# -> Estacionamento real
    #_, binary = cv2.threshold(gray, 180, 255, cv2.THRESH_BINARY)# -> Maquet

    # Find contours in the binary image
    contours, _ = cv2.findContours(binary, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    # okay, now create a black canvas with the same dimensions as input image
    contour_image = frame.copy()
    contour_image[:] = 0  # Fill with black

    # Draw contours on black canvas in white
    cv2.drawContours(contour_image, contours, -1, (100, 100, 100), thickness=thick)
    return contour_image

def mark_slots(frame, grayscale_frame):
    global last_call_time
    global prevFreeslots
    current_time = time.time()
    elapsed_time = current_time - last_call_time

    freeslots=0
    for x, y in parking_slots:
        x1=x+10
        x2=x+rect_width
        y1=y+4
        y2=y+rect_height
        start_point, stop_point = (x1,y1), (x2, y2)

        crop=grayscale_frame[y1:y2, x1:x2]
        gray_crop = cv2.cvtColor(crop, cv2.COLOR_BGR2GRAY)


        # Get count of non-zero pixels
        count=cv2.countNonZero(gray_crop)

        #Assign color, thickness based on threshold
        color, thick = [(0,255,0), 5] if count<threshold else [(0,0,255), 2]

        if count<threshold:
            freeslots = freeslots+1
        
        cv2.rectangle(frame, start_point, stop_point, color, thick)

    #Update the Free Slots display counter - less frequently
    current_time = time.time()
    if current_time - last_call_time >= 0.1:
        cv2.putText(frame, "Free Slots:" + str(freeslots), (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (200, 255, 255), 2)
        last_call_time = current_time
        prevFreeslots = freeslots
        

        # Send a message to a Kafka topic
        producer.produce('lugaresLivres', str(freeslots))

        
    else:
        cv2.putText(frame, "Free Slots:" + str(prevFreeslots), (10, 30), cv2.FONT_HERSHEY_SIMPLEX, 1, (200, 255, 255), 2)
        # Send a message to a Kafka topic
        #producer.produce('lugaresLivres', 'olaaaa2')
    return frame



while True:
    # Read frame from video stream
    ret, frame = cap.read()

    # Process frame using OpenCV
    framegray = convert_grayscale(frame)
    out_image = mark_slots(frame, framegray)

    # Display processed frame
    cv2.imshow("IP Webcam Stream", out_image)

    # Break loop if 'q' is pressed
    if cv2.waitKey(1) & 0xFF == ord("q"):
        break

# Release resources
cap.release()
cv2.destroyAllWindows()

# Flush the producer
producer.flush()