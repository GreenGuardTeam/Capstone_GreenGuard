### **README.md: GreenGuard**

#### **1. Project Title**
**GreenGuard: Plant Disease Detection and Treatment Recommendation**

---

#### **2. Introduction**
GreenGuard is a mobile application designed to help beginner gardeners identify plant diseases and receive actionable treatment recommendations. By combining machine learning, intuitive mobile development, and cloud-based infrastructure, GreenGuard provides a seamless and reliable gardening assistant.

---

#### **3. Features**
- **Disease Classification**: Identifies plant diseases with high accuracy using a CNN-based model.
- **Treatment Recommendations**: Provides detailed advice for organic and conventional treatments.
- **History Management**: Tracks and saves user submissions locally for future reference.
- **Scalable Infrastructure**: Cloud-hosted APIs ensure secure and reliable service delivery.
- **User-Friendly Design**: Tailored for beginner gardeners with an intuitive and simple interface.

---

#### **4. Project Architecture**
The project consists of three main components:
1. **Machine Learning**: Builds and trains a CNN model for disease detection.
2. **Mobile Development**: Implements the user interface and interaction for disease detection and management.
3. **Cloud Computing**: Develops and hosts APIs for model inference and data storage.

---

#### **5. Machine Learning Documentation**
- **Dataset**:
  - Includes tropical Indonesian plant diseases (Corn, Papaya, Potato, Tomato).
  - Augmentation techniques (rotation, shift, zoom, flipping) enhance robustness.
  
- **Model**:
  - Algorithm: Convolutional Neural Networks (CNN).
  - Architecture: 5 Convolutional Blocks, Batch Normalization, Global Average Pooling, and L2-regularized Dense Layers.
  - Metrics: Accuracy **93.02%**, Precision **0.93**, Recall **0.93**, F1 Score **0.93**.

- **Training Insights**:
  - Plots: Accuracy and loss during training indicate minimal overfitting.
  - Confusion Matrix: Highlights strong classification performance across all classes.

- **Deployment**:
  - Trained model integrated into the cloud backend using Flask APIs.

---

#### **6. Cloud Computing Documentation**
- **Overview**:
  - GreenGuard uses API-based architecture to process and classify plant diseases.
  - The system is designed for simplicity and scalability.

- **Architecture**:
  - Mobile Application: Allows users to upload images for analysis.
  - API Gateway: Routes requests to the Flask-based backend.
  - Flask API: Processes images and performs inference using the trained model.
  - Cloud Storage: Hosts the model and stores uploaded images.
  - Cloud Run: Executes the containerized application in a serverless environment.

- **Key Features**:
  - Supports classification for potatoes, corn, tomatoes, and papayas.
  - Validates file size (up to 10 MB) and formats (JPEG/PNG).
  
- **Technical Highlights**:
  - The API processes requests efficiently, ensuring low latency for users.
  - Secure communication with HTTPS for all endpoints.
  - The backend is designed to handle concurrent requests seamlessly.

- **Scalability**:
  - Built with Google Cloud’s autoscaling capabilities to manage varying workloads.
  - Ensures high availability and fault tolerance for production-level demands.

- **Notes**:
  - The model file (`final_model.h5`) is hosted securely in Cloud Storage.
  - Cloud Run ensures cost-effective operation, billing only for actual usage.

---

#### **7. Mobile Development Documentation**

- **Tools**:
  - **Development Environment**: Android Studio
  - **Language**: Kotlin and XML

- **Key Features**:
  - Upload plant leaf images for disease detection.
  - Display disease classification, impact, symptoms, tips and tricks, and treatment recommendations.
  - Save history in the app's local database using Room.

- **Architecture**:
  - **MVVM Architecture**: Designed to separate concerns for better maintainability and scalability:
    - **Model**: Manages the data and business logic of the application.
    - **View**: Represents the UI elements and user interactions.
    - **ViewModel**: Acts as a mediator between the Model and the View, handling presentation logic and preparing data for display in the UI.

- **Future Features**:
  - Authentication and login functionality for personalized experiences.
  - Push notifications for disease management tips.

- **UI/UX**:
  - Designed the app UI and prototyped it using Figma.
  - Focused on creating a clean and intuitive interface to ensure ease of use for beginner gardeners.

---

#### **8. Project Timeline**
- **Months 1-2**: Planning and dataset research.
- **Months 2-3**: Machine learning model development and evaluation.
- **Months 2-4**: Mobile app development and cloud infrastructure setup.
- **Months 5-6**: Testing, refinement, and final deployment.

---

#### **9. Results**
- **Final Application**:
  - High-accuracy classification of plant diseases.
  - Detailed treatment recommendations for both organic and conventional methods.
  - Seamless user experience with offline history management.
- **Demo**:  
  - [YouTube Demo](https://youtu.be/VuQTxuD7fTW)

---

#### **10. Contributors**
- **Laras Ayodya Sari** - M297B4KX2253  
- **Reyhanssan Islamey** - M179B4KY3775  
- **Rolly Dhea Venesia Sibuea** - M297B4KX3959  
- **Dimas Mario** - C487B4KY1146  
- **Sela Dwi Apriliyana** - C627B4KX4095  
- **Yessica Thipandona** - A008B4KX4531  
- **Ririth Coshy Yah** - A008B4KX3876