# Green Guard - Plant Classification API

Green Guard is an API-based application that uses a machine learning model to detect and classify plant diseases, such as those affecting potatoes, corn, tomatoes, and others, based on images uploaded by users. The project leverages Google Cloud Platform (GCP) services, including Cloud Run, Cloud Storage, and the Flask framework.

---

## 📐 Architecture
- **Mobile Application**: A user-facing app for uploading images to the API.
- **API Gateway**: Routes requests from the app to the Flask backend.
- **Flask API**: Processes images and executes the machine learning model.
- **Cloud Storage**: Stores the model file and uploaded images.
- **Cloud Run**: Runs the containerized application in a serverless environment.

---

## ✨ Features
- Supports classification for plants such as **potatoes**, **corn**, **tomatoes**, and **papayas**.
- Validates a maximum file size of **10 MB**.
- Supports image formats: **JPEG** and **PNG**.

---

## 📂 Directory Structure
.
├── classify.py          # Script to load and use the machine learning model
├── main.py              # Main file for the Flask API
├── Dockerfile           # Docker configuration for Cloud Run
├── final_model.h5       # Machine learning model file
├── requirements.txt     # Python dependencies
├── README.md            # Project documentation






---

## 🚀 Installation and Configuration

### 1. Clone this repository:
copy code
git clone https://github.com/GreenGuardTeam/Capstone_GreenGuard/new/main/Cloud%20Computing%20Path
cd repository
2. Install dependencies:
bash
copy code
pip install -r requirements.txt
3. Run the application locally:
copy code
python main.py
4. Build Docker image:
docker build -t green-guard .
5. Deploy to Cloud Run:
copy code
gcloud run deploy green-guard \
  --source . \
  --region asia-southeast2 \
  --memory 512Mi



📡 API Endpoints
1. Root Endpoint
URL: /
Method: GET
Response:
copy code
{
  "message": "Plant Classification API (supports potato, papaya, corn, tomato) is active"
}

3. Image Classification
URL: /predict
Method: POST
Headers:
Content-Type: multipart/form-data
Body:
uploaded_file: An image file (JPEG/PNG).
Success Response:
json
copy code
{
  "error": false,
  "prediction": "Plant disease name",
  "message": "Prediction successful."
}


Error Responses:
Invalid file format:
json
Copy code
{
  "error": true,
  "message": "Uploaded file is not a valid image format"
}

File too large:
json
Copy code
{
  "error": true,
  "message": "File size exceeds 10 MB. Please upload a smaller image."
}



📝 Technical Notes
Memory Issues on Cloud Run
If you encounter an error such as Memory limit exceeded with 516 MiB used, increase the Cloud Run memory limit to 1 GiB using the following command:
copy code
gcloud run services update green-guard --memory=1Gi

Machine Learning Model
The final_model.h5 model is trained to detect plant diseases using a pre-processed image dataset.

👥 Contributors
Cloud Computing
Green Guard

