from flask import Flask, request, render_template, jsonify
import numpy as np
import os
from tensorflow.keras.models import load_model
from tensorflow.keras.utils import load_img, img_to_array

app = Flask(__name__)

# Load model sekali di awal
model = load_model('model/image-class.keras')  # Ganti dengan path ke model Anda


# Mapping kelas indeks ke label
class_indices = {
    'Banana_Banana_cordana': 0, 'Banana_Banana_healthy': 1, 'Banana_Banana_pestalotiopsis': 2, 'Banana_Banana_sigatoka': 3,
    'Bean_Bean_angular_leaf_spot': 4, 'Bean_Bean_bean_rust': 5, 'Bean_Bean_healthy': 6,
    'Cassava_Cassava_cassava_bacterial_blight_cbb': 7, 'Cassava_Cassava_cassava_brown_streak_disease_cbsd': 8,
    'Cassava_Cassava_cassava_green_mottle_cgm': 9, 'Cassava_Cassava_cassava_mosaic_disease_cmd': 10,
    'Cassava_Cassava_healthy': 11, 'Cauli Flower_Cauli Flower_bacterial_spot_rot': 12,
    'Cauli Flower_Cauli Flower_downy_mildew': 13, 'Cauli Flower_Cauli Flower_no_disease': 14,
    'Corn_Corn_blight': 15, 'Corn_Corn_common_rust': 16, 'Corn_Corn_gray_leaf_spot': 17, 'Corn_Corn_healthy': 18,
    'Cucumber_Cucumber_anthracnose': 19, 'Cucumber_Cucumber_bacterial_wilt': 20, 'Cucumber_Cucumber_belly_rot': 21,
    'Cucumber_Cucumber_downy_mildew': 22, 'Cucumber_Cucumber_fresh_cucumber': 23, 'Cucumber_Cucumber_fresh_leaf': 24,
    'Cucumber_Cucumber_gummy_stem_blight': 25, 'Cucumber_Cucumber_pythium_fruit_rot': 26,
    'Mango_Mango_anthracnose': 27, 'Mango_Mango_bacterial_canker': 28, 'Mango_Mango_cutting_weevil': 29,
    'Mango_Mango_die_back': 30, 'Mango_Mango_gall_midge': 31, 'Mango_Mango_healthy': 32, 'Mango_Mango_powdery_mildew': 33,
    'Mango_Mango_sooty_mould': 34, 'Potato_Potato_alternaria_solani': 35, 'Potato_Potato_healthy': 36,
    'Potato_Potato_insect': 37, 'Potato_Potato_phytopthora_infestans': 38, 'Potato_Potato_virus': 39,
    'Pumpkin_Pumpkin_Bacterial Leaf Spot': 40, 'Pumpkin_Pumpkin_Downy Mildew': 41,
    'Pumpkin_Pumpkin_Healthy Leaf': 42, 'Pumpkin_Pumpkin_Mosaic Disease': 43, 'Pumpkin_Pumpkin_Powdery_Mildew': 44
}
class_labels = {v: k for k, v in class_indices.items()}

def predict_image(image_path, img_size):
    img = load_img(image_path, target_size=img_size)
    img_array = img_to_array(img) / 255.0
    img_array = np.expand_dims(img_array, axis=0)

    predictions = model.predict(img_array)
    predicted_class = np.argmax(predictions, axis=1)[0]

    full_label = class_labels.get(predicted_class, "Unknown")
    if '_' in full_label:
        nama_tanaman, penyakit_tanaman = full_label.split('_', 1)
    else:
        nama_tanaman, penyakit_tanaman = full_label, "Tidak diketahui"

    confidence_score = predictions[0][predicted_class] * 100
    return nama_tanaman, penyakit_tanaman, confidence_score

@app.route('/')
def home():
    return render_template('index.html')

@app.route('/predict', methods=['POST'])
def predict():
    if 'image' not in request.files:
        return render_template('index.html', error="No image file uploaded.")

    img_file = request.files['image']
    img_path = "uploaded_image.jpg"
    img_file.save(img_path)

    try:
        img_size = (224, 224)
        nama_tanaman, penyakit_tanaman, confidence_score = predict_image(img_path, img_size)
        os.remove(img_path)

        return render_template('index.html', 
                               nama_tanaman=nama_tanaman, 
                               penyakit_tanaman=penyakit_tanaman, 
                               confidence_score=f"{confidence_score:.2f}%")
    except Exception as e:
        return render_template('index.html', error=str(e))

if __name__ == '__main__':
    app.run(debug=True)