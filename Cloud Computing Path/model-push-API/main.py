import os
import uvicorn
import traceback
import tensorflow as tf
import numpy as np
from fastapi import FastAPI, Response, UploadFile
from fastapi.responses import JSONResponse
from PIL import Image
import io
from classify import KlasifikasiGambar

# Inisialisasi aplikasi FastAPI
aplikasi = FastAPI()

# Daftar label kelas secara berurutan
# LABEL_KELAS = ["corn_blig","corn_common_rust","corn_gray_leaf","papaya_bacterical","papaya_curl","papaya_ringspot","potato_Alternaria","potato_phytopthora_infestans","potato_virus","tomato_yellow leaf curl virus","tomato_late_blight","Tomato_septoria_leaf_spot"]

# URL penyimpanan cloud untuk gambar terkait
URL_GAMBAR = {
    0: "https://storage.googleapis.com/green_model/test_corn_blight(1).jpg",  
    1: "https://storage.googleapis.com/green_model/test_corn_common_rust.jpg",   
    2: "https://storage.googleapis.com/green_model/test_corn_gray_leaf_spot.jpg", 
    3: "https://storage.googleapis.com/green_model/test_papaya_bacterical.jpg",
    4: "https://storage.googleapis.com/green_model/test_papaya_curl.jpg",
    5: "https://storage.googleapis.com/green_model/test_papaya_ringspot.jpg",   
    6: "https://storage.googleapis.com/green_model/test_potato_Alternaria.jpg", 
    7: "https://storage.googleapis.com/green_model/test_potato_phytopthora_infestans.jpeg",
    8: "https://storage.googleapis.com/green_model/test_potato_virus.jpg",
    9: "https://storage.googleapis.com/green_model/test_tomato_%20yellow%20leaf%20curl%20virus.jpg",   
    10: "https://storage.googleapis.com/green_model/test_tomato_late_blight.jpg", 
    11: "https://storage.googleapis.com/green_model/test_Tomato_septoria_leaf_spot.jpg",
}

JENIS_PENYAKIT = {
    0: "corn_blight",
    1: "corn_common_rust",
    2: "corn_gray_leaf",
    3: "papaya_bacterical",
    4: "papaya_curl",
    5: "papaya_ringspot",
    6: "potato_Alternaria",
    7: "potato_phytopthora_infestans",
    8: "potato_virus",
    9: "tomato_yellow leaf curl virus",
    10: "tomato_late_blight",
    11: "Tomato_septoria_leaf_spot",
}


TIPS_and_TRICK = {
    0: "Corn Blight (Northern Corn Leaf Blight) Fungicide applications should be considered when the disease is first noticed, particularly in high-risk periods such as humid conditions. It is also beneficial to plant blight-resistant corn hybrids, as they help reduce the impact of the disease. After harvest, cleaning up the field by removing infected plant material can help eliminate sources of spores that could infect the next season’s crops",
    1: "Corn Common Rust In areas with high rust pressure, fungicide applications can help reduce the spread of the disease. Crop rotation with non-host crops is essential to reduce the buildup of rust spores in the soil. It is also crucial to closely monitor the crop during the tasseling and silking stages, as these are the periods when rust is most active",
    2: "Corn Gray Leaf Spot Fungicides should be applied when the first symptoms of gray leaf spot appear, as early intervention is key. It’s also important to avoid excessive nitrogen fertilization, as this can encourage the disease. Using corn varieties that are resistant to gray leaf spot is an effective way to manage the disease and minimize its impact",
    3: "Papaya Bacterial Spot Pruning infected leaves and branches is a good practice to help reduce the spread of bacterial spot. Copper-based bactericides are effective in managing bacterial infections, and proper field drainage should be maintained to prevent overwatering, which can contribute to bacterial proliferation",
    4: "Papaya Leaf Curl The use of insecticides to control the whitefly vector is crucial to reduce the transmission of the virus that causes leaf curl. Early detection of the disease is essential, and if symptoms are detected early, rogueing infected plants promptly can prevent further spread",
    5: "Papaya Ring Spot To avoid introducing the virus, it is vital to use certified virus-free seeds or saplings for planting. Controlling aphid populations, the primary vectors of the virus, can be achieved by using insecticides or introducing natural predators like ladybugs to reduce the spread of the disease",
    6: "Potato Alternaria Solani Fungicide applications are recommended when wet weather conditions persist, particularly during the tuber bulking stages, as this can help manage the disease. Proper spacing of potato plants is necessary to allow for air circulation, which reduces leaf wetness and the risk of infection. Additionally, removing infected plant debris after harvest is essential to prevent the overwintering of the pathogen",
    7: "Potato Phytophthora Infestans (Late Blight) To manage late blight, spray with systemic fungicides, especially during rainy seasons when the disease is most prevalent. Using certified disease-free seed potatoes reduces the risk of initial infections. Regular scouting of fields, particularly after rainy periods, helps with the early detection of the disease",
    8: "Potato Virus Planting certified virus-free seed potatoes is critical in preventing virus introduction into the crop. Removing weeds that may harbor aphids or other insect vectors helps reduce the spread of the virus. Aphid management through the use of insecticides is necessary to control aphid populations that transmit potato viruses",
    9: "Tomato Late Blight Fungicides should be applied at the first sign of infection, particularly during wet conditions, to limit the spread of late blight. Infected leaves or plants should be routinely removed to minimize the diseases impact. Growing late blight-resistant tomato varieties when possible can significantly reduce the risk of infection",
    10: "Tomato Septoria Leaf Spot To reduce the risk of fungal infection, water at the base of tomato plants to avoid wetting the foliage. Fungicides should be applied as soon as symptoms appear to help limit the spread. Removing infected plant debris and practicing crop rotation can also prevent future infections",
    11: "Tomato Yellow Leaf Curl Virus Insecticides or organic alternatives like neem oil should be used to control the whitefly population, as they are the main vectors of the virus. Preventative barriers like insect-proof nets or row covers can help keep whiteflies from reaching tomato plants. If infected plants are detected, rogueing them promptly can help prevent further spread of the virus",
    12: "General Tips for All Diseases: Early detection is critical in managing most plant diseases, so regular monitoring for symptoms is essential. Maintaining soil health through proper fertilization, rotation, and organic matter improves crop resilience and disease resistance. Employing good cultural practices such as appropriate planting densities, maintaining good field hygiene, and avoiding practices that promote excessive moisture retention (like overhead irrigation) are also vital for reducing disease risks",
}

SYMTOMS_PENYAKIT = {
    0: "Corn plants affected by blight show elongated, cigar-shaped lesions on the leaves, which start as gray-green and later turn tan or brown. In severe infections, the lesions can merge, causing extensive leaf damage and reducing the plant's ability to photosynthesize.",
    1: "Common rust appears as small, oval or elongated reddish-brown pustules on both sides of the leaves. Over time, these pustules can turn black. In severe infections, pustules may coalesce, causing leaf tissue to die.",
    2: "Gray Leaf Spot manifests as rectangular, gray to tan lesions on corn leaves, often with a yellow halo. Lesions can coalesce, leading to large areas of dead tissue.",
    3: "Symptoms include water-soaked spots on leaves, which later turn brown with yellow halos. On fruits, small, raised, water-soaked spots develop, which may turn brown and crack.",
    4: "Papaya Leaf Curl disease causes curling and distortion of leaves, yellowing, stunted growth, and reduced leaf size. The leaves may become thickened with an upward or downward curl.",
    5: "Papaya Ring Spot disease causes yellowing and mottling of the leaves, narrowing and distortion of leaf blades, and distinctive concentric rings or spots on the fruit. Stems and petioles may also develop streaks or rings.",
    6: "Alternaria solani causes early blight in potatoes. Symptoms typically start as small, dark, concentric lesions on older leaves, which eventually expand and form irregular spots with a yellow halo. These lesions may coalesce, causing large areas of the leaf to die.",
    7: "Phytophthora infestans starts as small, dark, water-soaked lesions on the older leaves, which expand and turn brown or black with a yellow halo. These lesions often have a characteristic oily appearance.",
    8: "Potatoes can be affected by several types of viruses, including Potato virus Y (PVY), Potato leafroll virus (PLRV), and Potato virus X (PVX). Virus-infected plants often exhibit stunted growth, yellowing of the leaves (chlorosis), and leaf curling.",
    9: "Early symptoms include upward curling and crinkling of young leaves, followed by interveinal yellowing. Infected plants may produce few or no fruits, and the fruits that do form are often small and misshapen.",
    10: "Early symptoms include greenish-gray or brown spots on leaves that quickly expand, often with water-soaked edges. On fruits, the disease causes large, brown, rough-textured lesions, rendering them inedible.",
    11: "Early symptoms appear as small, circular spots with dark brown edges and lighter gray centers, often containing tiny black fruiting bodies (pycnidia) of the fungus. As the infection progresses, the spots increase in number, leading to yellowing and eventual leaf drop."
}


IMPACT = {
    0: "Corn Blight can cause significant reductions in yield due to premature leaf death, weakening the plant's ability to photosynthesize. In severe cases, entire plants can be killed, leading to total crop loss.",
    1: "Corn Common Rust leads to reduced photosynthesis, affecting plant growth and resulting in smaller ears and lower grain quality. Heavy infestations can lead to significant yield loss.",
    2: "Corn Gray Leaf Spot causes premature leaf senescence, reducing the photosynthetic area and leading to stunted growth and lower yield potential. This disease can be particularly damaging during hot, humid conditions.",
    3: "Papaya Bacterial Spot can lead to the loss of fruit quality and reduce crop yield. In severe cases, the disease can cause defoliation, affecting plant growth and fruit production.",
    4: "Papaya Leaf Curl reduces the plant's ability to photosynthesize, leading to stunted growth, lower fruit yield, and poor fruit quality. Infected plants often have fewer leaves and smaller fruits.",
    5: "Papaya Ring Spot causes leaf distortion and fruit deformities, significantly affecting yield and fruit marketability. The disease can also lead to premature fruit drop.",
    6: "Potato Alternaria causes early blight, resulting in premature leaf death and reduced tuber quality. Infected plants are more susceptible to secondary infections and environmental stresses, leading to a decline in yield.",
    7: "Potato Phytophthora Infestans leads to rapid decay and necrosis of leaves, stems, and tubers. This disease can cause widespread yield loss, particularly in wet conditions.",
    8: "Potato viruses often lead to stunted growth, reduced tuber size, and poor quality. Yield loss can be significant, particularly in cases of widespread viral infection.",
    9: "Tomato Yellow Leaf Curl Virus reduces photosynthesis efficiency, stunts plant growth, and reduces fruit production. Infected plants may also produce fewer and deformed fruits.",
    10: "Tomato Late Blight causes rapid decay of leaves and fruits, often leading to total crop loss in severe cases. It is especially destructive in humid, wet conditions.",
    11: "Tomato Septoria Leaf Spot causes premature leaf drop, reducing photosynthesis and weakening plant vigor. Yield can be reduced, and fruit quality may also decline."
}

SOLUTION = {
    0: "To control Corn Blight, practice crop rotation, use resistant corn varieties, and remove infected crop residues. Fungicide applications may also be necessary during periods of high humidity to prevent the spread of the disease.",
    1: "To manage Common Rust, plant resistant corn varieties, apply fungicides at early stages of infection, and rotate crops. Ensuring proper spacing between plants can help reduce humidity around the leaves and minimize infection.",
    2: "For Gray Leaf Spot, remove infected leaves, avoid excessive nitrogen fertilization, and apply fungicides to reduce disease spread. Choosing resistant corn varieties can also help manage the disease.",
    3: "Control Papaya Bacterial Spot by removing infected plant debris, treating with copper-based bactericides, and improving plant spacing for better air circulation. Avoid overhead irrigation that can spread bacteria.",
    4: "For Papaya Leaf Curl, control aphid vectors by using insecticides, removing infected plants, and improving plant health through proper fertilization and irrigation. Pruning affected leaves may also help reduce disease spread.",
    5: "To manage Papaya Ring Spot, remove infected plants and fruit, avoid using infected seedlings, and apply insecticides to control aphids, which transmit the virus. Sanitize tools and equipment to avoid cross-contamination.",
    6: "For Potato Alternaria, use resistant varieties, practice crop rotation, and apply fungicides when necessary. Remove and destroy infected leaves to prevent further spread.",
    7: "For Phytophthora Infestans, improve soil drainage, apply fungicides early in the growing season, and rotate crops. Remove infected leaves and tubers to reduce pathogen load in the field.",
    8: "For Potato Viruses, use virus-free certified seed tubers, remove infected plants, and control aphids that spread viruses. Implementing strict sanitation practices is crucial to prevent the spread.",
    9: "Control Tomato Yellow Leaf Curl Virus by removing infected plants and weeds that serve as alternative hosts. Use insecticides to control whiteflies, which transmit the virus, and consider planting resistant varieties.",
    10: "Manage Tomato Late Blight by applying fungicides during wet conditions, removing infected plant parts, and practicing crop rotation. Infected plants should be destroyed to reduce pathogen spread.",
    11: "To control Tomato Septoria Leaf Spot, remove and destroy infected leaves, apply fungicides to prevent further spread, and practice crop rotation. Improving air circulation around plants helps prevent the disease."
}

# Path file model (pastikan tersedia di direktori kerja container)
FILE_MODEL = "final_model (1).h5"

# Memuat model yang telah dilatih
try:
    model_dimuat = tf.keras.models.load_model(FILE_MODEL)
except Exception as error:
    raise RuntimeError(f"Gagal memuat model: {error}")

# Inisialisasi helper untuk klasifikasi gambar
pengolah_gambar = KlasifikasiGambar(model_dimuat)

@aplikasi.get("/")
def beranda():
    """
    Endpoint root API untuk memverifikasi ketersediaan layanan.
    """
    return {"pesan": "API Klasifikasi Tanaman (mendukung potato, , papaya, corn, tomat) aktif"}

@aplikasi.post("/prediksi")
def prediksi_gambar(file_diupload: UploadFile, respons: Response):
    try:
        # Validasi jenis file
        if file_diupload.content_type not in ["image/jpeg", "image/png"]:
            respons.status_code = 400
            return {"error": True, "pesan": "File yang diunggah bukan format gambar yang valid"}

        # Membaca konten file yang diunggah
        data_file = file_diupload.file.read()

        # Membatasi ukuran file maksimal 10 MB
        ukuran_maksimal = 10 * 1024 * 1024  # 10 MB
        if len(data_file) > ukuran_maksimal:
            return JSONResponse(
                status_code=413,
                content={
                    "error": True,
                    "pesan": "Ukuran file melebihi 10 MB. Silakan unggah gambar yang lebih kecil."
                }
            )

        # Membuka file gambar untuk diproses
        gambar = Image.open(io.BytesIO(data_file))

        # Melakukan prediksi
        indeks_prediksi, _ = pengolah_gambar.load_and_predict(gambar)

        # Konversi indeks prediksi menjadi integer
        indeks_prediksi = int(indeks_prediksi)

        # Mendapatkan label kelas dan URL terkait
        # label_prediksi = LABEL_KELAS[indeks_prediksi]
        jenis_penyakit = JENIS_PENYAKIT.get(indeks_prediksi)
        gejala_penyakit = SYMTOMS_PENYAKIT.get(indeks_prediksi)
        dampak_penyakit = IMPACT.get(indeks_prediksi)
        solusi_penyakit = SOLUTION.get(indeks_prediksi)
        tips_trik = TIPS_and_TRICK.get(indeks_prediksi)
        url_gambar_terkait = URL_GAMBAR.get(indeks_prediksi)

        # Menggabungkan gejala, dampak, dan solusi sebagai tips dan trik
        tips_trik_kompleks = f"Gejala: {gejala_penyakit}. Dampak: {dampak_penyakit}. Solusi: {solusi_penyakit}. {tips_trik}"

        return JSONResponse(
            status_code=200,
            content={
                "error": False,
                "pesan": "Klasifikasi gambar berhasil!",
                "hasil_prediksi": {
                    "indeks_kelas": indeks_prediksi,
                    "label_penyakit": jenis_penyakit,
                    "gejala_penyakit": gejala_penyakit,
                    "dampak_penyakit": dampak_penyakit,
                    "solusi_penyakit": solusi_penyakit,
                    "tips_dan_trick": tips_trik_kompleks,
                    "url_gambar": url_gambar_terkait,
                }
            }
        )
    except Exception as error:
        traceback.print_exc()
        respons.status_code = 500
        return {"error": True, "pesan": f"Terjadi kesalahan saat prediksi: {error}"}

# Titik masuk untuk memulai server API
if __name__ == "__main__":
    port = int(os.environ.get("PORT", 8080))
    print(f"Server berjalan di http://0.0.0.0:{port}")
    uvicorn.run(aplikasi, host="0.0.0.0", port=port)
