# app.py
import os
from flask import Flask, request, jsonify, render_template
from model_info import DiseaseDatabase
from model_predict import PlantDiseasePredictor

# Setup Flask App
app = Flask(__name__)
app.config['UPLOAD_FOLDER'] = 'static/uploads'
app.config['ALLOWED_EXTENSIONS'] = {'png', 'jpg', 'jpeg', 'gif'}

# Initialize classes
disease_db = DiseaseDatabase()
predictor = PlantDiseasePredictor(model_path="/model/model_89_38_newdataset.h5")

# Route to display the main page (index.html)
@app.route("/")
def home():
    return render_template('index.html')

# Route for image upload and prediction
@app.route('/predict', methods=['POST'])
def predict():
    if 'file' not in request.files:
        return jsonify({'error': 'No image file uploaded.'}), 400
    
    file = request.files['file']
    if file.filename == '':
        return jsonify({'error': 'No file name provided.'}), 400
    
    if file and predictor.allowed_file(file.filename):
        filepath = predictor.save_file(file)
        try:
            # Predict the uploaded image
            crop_name, disease_name, confidence_score = predictor.predict_image(filepath)

            # Retrieve disease information from DiseaseDatabase
            disease_key = f"{crop_name}_{disease_name}"
            disease_info = disease_db.get_disease_info(disease_key)

            if disease_info:
                # Return prediction results along with disease details
                return jsonify({
                    'crop_name': crop_name,
                    'disease_name': disease_name,
                    'confidence': confidence_score,
                    'description': disease_info.description,
                    'solution': disease_info.solution,
                    'tips': disease_info.tips
                })
            else:
                return jsonify({
                    'error': 'Disease information not found.'
                }), 404
        except ValueError as e:
            return jsonify({'error': str(e)}), 400
        except Exception as e:
            return jsonify({'error': f'Failed to process the image. {str(e)}'}), 500
    else:
        return jsonify({'error': 'Invalid file format.'}), 400

# Run the application
if __name__ == '__main__':
    app.run(debug=True)
