"""
This is an example of using the k-nearest-neighbors (KNN) algorithm for face recognition.

When should I use this example?
This example is useful when you wish to recognize a large set of known people,
and make a prediction for an unknown person in a feasible computation time.

Algorithm Description:
The knn classifier is first trained on a set of labeled (known) faces and can then predict the person
in an unknown image by finding the k most similar faces (images with closet face-features under euclidean distance)
in its training set, and performing a majority vote (possibly weighted) on their label.

For example, if k=3, and the three closest face images to the given image in the training set are one image of Biden
and two images of Obama, The result would be 'Obama'.

* This implementation uses a weighted vote, such that the votes of closer-neighbors are weighted more heavily.

Usage:

1. Prepare a set of images of the known people you want to recognize. Organize the images in a single directory
   with a sub-directory for each known person.

2. Then, call the 'train' function with the appropriate parameters. Make sure to pass in the 'model_save_path' if you
   want to save the model to disk so you can re-use the model without having to re-train it.

3. Call 'predict' and pass in your trained model to recognize the people in an unknown image.

NOTE: This example requires scikit-learn to be installed! You can install it with pip:

$ pip3 install scikit-learn

"""

import math
import os
import os.path
import pickle
import shutil
from pprint import pformat
from datetime import datetime
import json

import face_recognition
import numpy as np
import pandas as pd
from face_recognition.face_recognition_cli import image_files_in_folder
from matplotlib import pyplot as plt
from numpy import asarray, loadtxt, savetxt
from sklearn import datasets, neighbors, preprocessing
from sklearn.metrics import (accuracy_score, classification_report,
                             confusion_matrix)
from sklearn.model_selection import cross_val_score, train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn.model_selection import GridSearchCV, train_test_split
from sklearn.neural_network import MLPClassifier
# import logging
import time

from sklearn.metrics import accuracy_score, balanced_accuracy_score, classification_report, \
    precision_score
ALLOWED_EXTENSIONS = {'png', 'jpg', 'jpeg'}
# log = logging.getLogger()


def train(train_dir, verbose=False):
    """
    Trains a k-nearest neighbors classifier for face recognition.

    :param train_dir: directory that contains a sub-directory for each known person, with its name.
    :param model_save_path: (optional) path to save model on disk
    :param n_neighbors: (optional) number of neighbors to weigh in classification. Chosen automatically if not specified
    :param knn_algo: (optional) underlying data structure to support knn.default is ball_tree
    :param verbose: verbosity of training
    :return: returns knn classifier that was trained on the given data.
    """
    # X = []
    # y = []
    X = loadtxt("/home/ubuntu/s03p31b107/face_classifier/output2.csv",
                delimiter=',').tolist()
    y = loadtxt(
        "/home/ubuntu/s03p31b107/face_classifier/output3.csv", dtype=str).tolist()

    # Loop through each person in the training set
    for class_dir in os.listdir(train_dir):
        if not os.path.isdir(os.path.join(train_dir, class_dir)):
            continue

        # Loop through each training image for the current person
        for img_path in image_files_in_folder(os.path.join(train_dir, class_dir)):
            image = face_recognition.load_image_file(img_path)
            face_bounding_boxes = face_recognition.face_locations(image)

            if len(face_bounding_boxes) != 1:
                pass
            else:
                # Add face encoding for current image to the training set
                X.append(face_recognition.face_encodings(
                    image, known_face_locations=face_bounding_boxes)[0])
                y.append(class_dir)

    # shutil.rmtree(
    #     "C:/Users/multicampus/Desktop/project3/s03p31b107/face_classifier/knn_examples/train")
    # os.mkdir(
    #     "C:/Users/multicampus/Desktop/project3/s03p31b107/face_classifier/knn_examples/train")

    # Determine how many neighbors to use for weighting in the KNN classifier


    pointX = asarray(X)
    d = np.array(y)
    pointY = d.reshape(d.shape[0], -1)

    savetxt('output2.csv', pointX, delimiter=',')
    savetxt('output3.csv', pointY, fmt='%s')

    
    start_time = time.time()
    model_path = "/home/ubuntu/s03p31b107/face_classifier/"

    dump_dataset(X, y, os.path.join(model_path))
    X_train, x_test, Y_train, y_test = train_test_split(X, y, test_size=0.25)
    classifier = MLPClassifier(max_iter=250)

    # Hyperparameter of the neural network (MLP) to tune
    # Faces are encoded using 128 points

    parameter_space = {
        'hidden_layer_sizes': [(128,), (200,), (200, 128,), ],
        'activation': ['identity', 'tanh', 'relu'],
        'solver': ['adam'],
        'learning_rate': ['constant', 'adaptive'],
    }
    # print("tuning | Parameter -> {}".format(pformat(parameter_space)))
    grid = GridSearchCV(classifier, parameter_space,
                        cv=2, scoring='accuracy', verbose=20, n_jobs=6)
    # print("++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++")
    grid.fit(X_train, Y_train)
    # print("TUNING COMPLETE | DUMPING DATA!")
    # print('Best parameters found: ', grid.best_params_)

    # print(time.time()-start_time, "++++++++++++++++++++++++++++++++++++++++++++++++++++++")
    y_pred = grid.predict(x_test)

    # print('Results on the test set: ', grid.score(x_test, y_test))

    # verify_performance(y_test, y_pred)
    return dump_model(params=grid.best_params_,
                      classifier=grid.best_estimator_), time.time() - start_time


def dump_dataset(X, y, path):
    """
    :param X:
    :param Y:
    :param path:
    :return:
    """
    # log = logging.getLogger()
    dataset = {
        'X': X,
        'y': y
    }
    # print("dump_dataset | Dumping dataset int {}".format(path))
    if not os.path.exists(path):
        os.makedirs(path)
        # print("dump_dataset | Path {} exist".format(path))
        dataset_name = os.path.join(path, "model.dat")
        with open(dataset_name, 'wb') as f:
            pickle.dump(dataset, f)
    # else:
        # log.error(
        #     "dump_dataset | Path {} ALREADY EXIST exist, avoiding to overwrite".format(path))


def dump_model(classifier, params=None, path=None):
    """
    Dump the model to the given path, file
    :param params:
    :param timestamp:
    :param classifier:
    :param path:
    """
    # print("dump_model | Dumping model ...")

    model_path = "/home/ubuntu/s03p31b107/face_classifier/"
    if path is None:
        if model_path is not None:
            if os.path.exists(model_path) and os.path.isdir(model_path):
                path = model_path
    config = {'classifier_file': os.path.join("model.pkl"),
              'params': params}

    # print(config)

    classifier_folder = os.path.join(path)
    classifier_file = os.path.join(classifier_folder, "model")

    # print("dump_model | Dumping model ... | Path: {}".format(
    #     path))
    if not os.path.exists(classifier_folder):
        os.makedirs(classifier_folder)

    with open(classifier_file + ".pkl", 'wb') as f:
        pickle.dump(classifier, f)
        # log.info('dump_model | Model saved to {0}.pkl'.format(
        #     classifier_file))

    with open(classifier_file + ".json", 'w') as f:
        json.dump(config, f)
        # log.info('dump_model | Configuration saved to {0}.json'.format(
        #     classifier_file))

    return config


def verify_performance(y_test, y_pred):
    print("###########################################################################################")
    print("verify_performance | Analyzing performance ...")
    print((classification_report(y_test, y_pred)))
    print(balanced_accuracy_score(y_test, y_pred))
    print("accuracy_score: {}", accuracy_score(y_test, y_pred))
    print("precision_score: {}", (precision_score(
        y_test, y_pred, average='weighted')))
    print("###########################################################################################")


def predict(X_img_path, knn_clf=None, model_path=None, distance_threshold=0.4):
    """
    Recognizes faces in given image using a trained KNN classifier

    :param X_img_path: path to image to be recognized
    :param knn_clf: (optional) a knn classifier object. if not specified, model_save_path must be specified.
    :param model_path: (optional) path to a pickled knn classifier. if not specified, model_save_path must be knn_clf.
    :param distance_threshold: (optional) distance threshold for face classification. the larger it is, the more chance
           of mis-classifying an unknown person as a known one.
    :return: a list of names and face locations for the recognized faces in the image: [(name, bounding box), ...].
        For faces of unrecognized persons, the name 'unknown' will be returned.
    """
    # print(X_img_path, " hiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii")
    if not os.path.isfile(X_img_path) or os.path.splitext(X_img_path)[1][1:] not in ALLOWED_EXTENSIONS:
        raise Exception("Invalid image path: {}".format(X_img_path))

    if knn_clf is None and model_path is None:
        raise Exception(
            "Must supply knn classifier either thourgh knn_clf or model_path")

    # Load a trained KNN model (if one was passed in)
    if knn_clf is None:
        with open(model_path, 'rb') as f:
            knn_clf = pickle.load(f)

    # Load image file and find face locations
    X_img = face_recognition.load_image_file(X_img_path)
    X_face_locations = face_recognition.face_locations(X_img)

    # If no faces are found in the image, return an empty result.
    if len(X_face_locations) == 0:
        return []

    # Find encodings for faces in the test iamge
    faces_encodings = face_recognition.face_encodings(
        X_img, known_face_locations=X_face_locations)

    # Use the KNN model to find the best matches for the test face
    closest_distances = knn_clf.kneighbors(faces_encodings, n_neighbors=5)
    are_matches = [closest_distances[0][i][0] <=
                   distance_threshold for i in range(len(X_face_locations))]

    # Predict classes and remove classifications that aren't within the threshold
    return [(pred, loc) if rec else ("unknown", loc) for pred, loc, rec in zip(knn_clf.predict(faces_encodings), X_face_locations, are_matches)]


def predictionss(X_img_path: str, detection_model: str, jitters: int, encoding_models: str,
                 distance_threshold: int = 0.45):
    """
    Recognizes faces in given image using a trained KNN classifier
    :param detection_model: can be 'hog' (CPU) or 'cnn' (GPU)
    :param jitters: augmentation data (jitters=20 -> 20x time)
    :param X_img_path: path of the image to be recognized
    :param distance_threshold: (optional) distance threshold for face classification. the larger it is,
    the more chance of mis-classifying an unknown person as a known one.
    :return: a list of names and face locations for the recognized faces in the image: [(name, bounding box), ...].
                                    For faces of unrecognized persons, the name 'unknown' will be returned.
    """
    classifier = MLPClassifier()
    with open("/home/ubuntu/s03p31b107/face_classifier/model.pkl", 'rb') as f:
        clf_from_pickle = pickle.load(f)

    faces_encodings, X_face_locations = None, None
    # Resize image if necessary for avoid cuda-malloc error (important for low gpu)
    # In case of error, will be returned back an integer.
    # FIXME: manage gpu memory unload in case of None
    ratio = 2
    ########################################################################
    # faces_encodings =[]
    pred = []
    face_prediction = []

    _predictions = []
    scores = []
    # Load image file and find face locations
    for img_path in image_files_in_folder(X_img_path):
        # print("이미지 : ", img_path)
        X_img = face_recognition.load_image_file(img_path)
        X_face_locations = face_recognition.face_locations(X_img)

    # If no faces are found in the image, return an empty result.
        if len(X_face_locations) == 0:
            return []
    # Find encodings for faces in the test iamge
        # faces_encodings.append(face_recognition.face_encodings(
        #     X_img, known_face_locations=X_face_locations))
        faces_encodings = (face_recognition.face_encodings(
            X_img, known_face_locations=X_face_locations))

        # Use the MLP model to find the best matches for the face(s)
        # print("predict | Understanding peoples recognized from NN ...")
        closest_distances = clf_from_pickle.predict(faces_encodings)

        # print("predict | Persons recognized: ", closest_distances)
        # print("predict | Asking to the neural network for probability ...")
        predictions = clf_from_pickle.predict_proba(faces_encodings)
        # print(predictions)

        # print(predictions[0])
        predictions= (sorted(predictions[0]))
        predictions.reverse()
        # print(img_path)
        if(predictions[0]*100 < 50):
            print("Not Correct.")
        else:
            print("Correct Face : ",  closest_distances, " Accuracy : ", predictions[0]*100)
            
        # print(predictions[0]*100)
        # for prediction in predictions:
        #     pred.append(dict([v for v in sorted(zip(prediction),
        #                                         key=lambda c: c[1], reverse=True)[:len(closest_distances)]]))
        # print("predict | Predict proba -> {}".format(pred))
        # print("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!")
        # os.remove(img_path)

    return {"predictions": _predictions, "scores": scores}


def __init__(self):
    self.classifier = MLPClassifier()


if __name__ == "__main__":
    # STEP 1: Train the KNN classifier and save it to disk
    # Once the model is trained and saved, you can skip this step next time.

    classifier = train("/home/ubuntu/s03p31b107/face_classifier/train")

    # STEP 2: Using the trained classifier, make predictions for unknown images
    model_path = "/home/ubuntu/s03p31b107/face_classifier/"

    classifier_folder = os.path.join(model_path)
    classifier_file = os.path.join(classifier_folder, "config")

    CONFIG_FILE = "/home/ubuntu/s03p31b107/face_classifier/config.json"
    with open(CONFIG_FILE) as f:
        CFG = json.load(f)

    detection_model = CFG["PyRecognizer"]["detection_model"].lower()
    # Use data augmentation when retrieving face encodings
    jitter = CFG["PyRecognizer"]["jitter"]
    # Use 68 or 5 points
    encoding_models = CFG["PyRecognizer"]["encoding_models"].lower()

    full_path = "/home/ubuntu/s03p31b107/face_classifier/test/"
    predictionssis = predictionss(
        full_path, detection_model, jitter, encoding_models, 45)