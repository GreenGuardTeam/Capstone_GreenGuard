# model_predict.py
import os
import numpy as np
import tensorflow as tf
from tensorflow.keras.preprocessing.image import load_img, img_to_array
from werkzeug.utils import secure_filename
from PIL import UnidentifiedImageError

class PlantDiseasePredictor:
    def __init__(self, model_path, upload_folder='static/uploads', allowed_extensions=None):
        if allowed_extensions is None:
            allowed_extensions = {'png', 'jpg', 'jpeg', 'gif'}
        
        self.model = tf.keras.models.load_model(model_path)
        self.upload_folder = upload_folder
        self.allowed_extensions = allowed_extensions
        self.class_indices = {
            'Corn_blight': 0, 'Corn_common_rust': 1, 'Corn_gray_leaf_spot': 2,
            'Papaya_BacterialSpot': 3, 'Papaya_Curl': 4, 'Papaya_RingSpot': 5,
            'Potato_alternaria_solani': 6, 'Potato_phytopthora_infestans': 7,
            'Potato_virus': 8, 'Tomato_late_blight': 9, 'Tomato_septoria_leaf_spot': 10,
            'Tomato_yellow_leaf_curl_virus': 11
        }

    def allowed_file(self, filename):
        return '.' in filename and filename.rsplit('.', 1)[1].lower() in self.allowed_extensions

    def predict_image(self, image_path, img_size=(224, 224)):
        try:
            img = load_img(image_path, target_size=img_size)
        except UnidentifiedImageError:
            raise ValueError("The uploaded file is not a valid image.")

        img_array = img_to_array(img) / 255.0
        img_array = np.expand_dims(img_array, axis=0)

        predictions = self.model.predict(img_array)
        predicted_class = np.argmax(predictions, axis=1)[0]
        class_labels = {v: k for k, v in self.class_indices.items()}
        full_label = class_labels.get(predicted_class, "Unknown")

        if '_' in full_label:
            crop_name, disease_name = full_label.split('_', 1)
        else:
            crop_name, disease_name = full_label, "Unknown"

        confidence_score = float(predictions[0][predicted_class] * 100)
        return crop_name, disease_name, confidence_score

    def save_file(self, file):
        if not os.path.exists(self.upload_folder):
            os.makedirs(self.upload_folder)
        filename = secure_filename(file.filename)
        filepath = os.path.join(self.upload_folder, filename)
        file.save(filepath)
        return filepath
