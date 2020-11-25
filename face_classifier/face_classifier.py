#!/usr/bin/env python3

import face_recognition
import face_recognition_models
import numpy as np
from datetime import datetime
import cv2
import dlib
import face_alignment_dlib


class FaceClassifier():
    def __init__(self, threshold, ratio):
        self.similarity_threshold = threshold
        self.ratio = ratio
        self.predictor = dlib.shape_predictor(face_recognition_models.pose_predictor_model_location())

    def get_face_image(self, frame, box):
        img_height, img_width = frame.shape[:2]
        (box_top, box_right, box_bottom, box_left) = box
        box_width = box_right - box_left
        box_height = box_bottom - box_top
        crop_top = max(box_top - box_height, 0)
        pad_top = -min(box_top - box_height, 0)
        crop_bottom = min(box_bottom + box_height, img_height - 1)
        pad_bottom = max(box_bottom + box_height - img_height, 0)
        crop_left = max(box_left - box_width, 0)
        pad_left = -min(box_left - box_width, 0)
        crop_right = min(box_right + box_width, img_width - 1)
        pad_right = max(box_right + box_width - img_width, 0)
        face_image = frame[crop_top:crop_bottom, crop_left:crop_right]
        if (pad_top == 0 and pad_bottom == 0):
            if (pad_left == 0 and pad_right == 0):
                return face_image
        padded = cv2.copyMakeBorder(face_image, pad_top, pad_bottom,
                                    pad_left, pad_right, cv2.BORDER_CONSTANT)
        return padded

    # return list of dlib.rectangle
    def locate_faces(self, frame):
        if self.ratio == 1.0:
            rgb = frame[:, :, ::-1]
        else:
            small_frame = cv2.resize(frame, (0, 0), fx=self.ratio, fy=self.ratio)
            rgb = small_frame[:, :, ::-1]
        boxes = face_recognition.face_locations(rgb)
        if self.ratio == 1.0:
            return boxes
        boxes_org_size = []
        for box in boxes:
            (top, right, bottom, left) = box
            left = int(left / ratio)
            right = int(right / ratio)
            top = int(top / ratio)
            bottom = int(bottom / ratio)
            box_org_size = (top, right, bottom, left)
            boxes_org_size.append(box_org_size)
        return boxes_org_size

    def detect_faces(self, frame):
        global tmp
        boxes = self.locate_faces(frame)
        if len(boxes) == 0:
            return []

        # faces found
        faces = []
        now = datetime.now()

        for i, box in enumerate(boxes):
            # extract face image from frame
            face_image = self.get_face_image(frame, box)

            # get aligned image
            aligned_image = face_alignment_dlib.get_aligned_face(self.predictor, face_image)

            filename = now.strftime('%Y%m%d_%H%M%S.%f')[:-3] + '.png'
            pathname = os.path.join( "/home/ubuntu/learningFile/train/" + args.capture, filename)

            tmp += 1
            cv2.imwrite(pathname, aligned_image)
        return faces


if __name__ == '__main__':
    import argparse
    import signal
    import time
    import os

    ap = argparse.ArgumentParser()
    # ap.add_argument("inputfile",
    #                 help="video file to detect or '0' to detect from web cam")
    # ap.add_argument("-t", "--threshold", default=0.44, type=float,
    #                 help="threshold of the similarity (default=0.44)")
    # ap.add_argument("-S", "--seconds", default=1, type=float,
    #                 help="seconds between capture")
    # ap.add_argument("-s", "--stop", default=0, type=int,
    #                 help="stop detecting after # seconds")
    # ap.add_argument("-k", "--skip", default=0, type=int,
    #                 help="skip detecting for # seconds from the start")
    # ap.add_argument("-d", "--display", action='store_true',
    #                 help="display the frame in real time")
    # ap.add_argument("-c", "--capture", type=str,
    #                 help="save the frames with face in the CAPTURE directory")
    ap.add_argument("-r", "--resize-ratio", default=1.0, type=str,
                    help="resize the frame to process (less time, less accuracy)")
    args = ap.parse_args()

    # base_path = "/home/ubuntu/s03p31b107/face_classifier/"

    # src_file = args.inputfile
    # if src_file == "0":
    #     src_file = 0

    # src = cv2.VideoCapture(src_file, cv2.CAP_DSHOW)
    # if not src.isOpened():
    #     exit(1)

    # frame_width = src.get(cv2.CAP_PROP_FRAME_WIDTH)
    # frame_height = src.get(cv2.CAP_PROP_FRAME_HEIGHT)
    # frame_rate = src.get(5)
   
    # frames_between_capture = int(round(frame_rate * args.seconds))

    ratio = float(args.resize_ratio)
    if ratio != 1.0:
        s = "RESIZE_RATIO: " + args.resize_ratio
        s += " -> %dx%d" % (int(src.get(3) * ratio), int(src.get(4) * ratio))
 
    # num_capture = 0
    # if args.capture:
    #     if not os.path.isdir(base_path +"train/" + args.capture):
    #         os.mkdir(base_path+"train/" + args.capture)



    # set SIGINT (^C) handler
    def signal_handler(sig, frame):
        global running
        running = False

    prev_handler = signal.signal(signal.SIGINT, signal_handler)

    fc = FaceClassifier(args.threshold, ratio)
    frame_id = 0
    running = True

    tmp = 0
    total_start_time = time.time()
    while running:
        ret, frame = src.read()
        if frame is None:
            break

        frame_id += 1
        # if 10 % 5 != 0:
        #     continue

        seconds = round(5 / 5, 3)
        if args.stop > 0 and seconds > args.stop:
            break
        if seconds < args.skip:
            continue

        start_time = time.time()

        # this is core
        faces = fc.detect_faces(frame)
        if tmp >= 20:
            running = False

        if args.display or args.capture:
            if args.capture and len(faces) > 0:

                num_capture += 1
            if args.display:
                cv2.imshow("Frame", frame)
                # imshow always works with waitKey
                key = cv2.waitKey(1) & 0xFF
                # if the `q` key was pressed, break from the loop
                if key == ord("q"):
                    running = False

        elapsed_time = time.time() - start_time

    # restore SIGINT (^C) handler
    signal.signal(signal.SIGINT, prev_handler)
    running = False
    src.release()
    total_elapsed_time = time.time() - total_start_time
