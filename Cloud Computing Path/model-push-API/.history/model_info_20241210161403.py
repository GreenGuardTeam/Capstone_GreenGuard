
class DiseaseInfo:
    def __init__(self, crop, disease, symptoms, impact, solution, tips):
        self.crop = crop
        self.disease = disease
        self.symptoms = symptoms
        self.impact = impact
        self.solution = solution
        self.tips = tips

    def get_info(self):
        return {
            "crop": self.crop,
            "disease": self.disease,
            "symptoms": self.symptoms,
            "impact": self.impact,
            "solution": self.solution,
            "tips": self.tips
        }

class DiseaseDatabase:
    def __init__(self):
        # Initialize the disease database with all crop diseases
        self.diseases = {
            'Corn_blight': DiseaseInfo(
                "Corn", "Blight", 
                "Corn plants affected by blight show elongated, cigar-shaped lesions on the leaves, which start as gray-green and later turn tan or brown. In severe infections, the lesions can merge, causing extensive leaf damage and reducing the plant's ability to photosynthesize. Blight is caused by fungal pathogens: Northern Corn Leaf Blight (NCLB) caused by Setosphaeria turcica and Southern Corn Leaf Blight (SCLB) caused by Bipolaris maydis.",
                "Reduced photosynthesis due to leaf damage. Lower grain yield and poor-quality kernels. In severe cases, early defoliation and increased susceptibility to other diseases.",
                "Use Resistant Hybrids: Plant corn varieties bred for resistance to blight pathogens to reduce susceptibility. Fungicide Application: Apply fungicides such as those containing strobilurins or triazoles at the first sign of symptoms, especially in fields with a history of the disease. Crop Rotation: Rotate corn with non-host crops to break the disease cycle and reduce fungal inoculum in the soil. Clean Farming Practices: Remove and destroy infected crop residues to limit overwintering of the fungal spores.",
                "Timely Planting: Plant early to help the crop mature before conditions become ideal for blight development. Adequate Spacing: Promote airflow to reduce leaf wetness, which can inhibit fungal growth. Monitor Weather Conditions: Blight thrives in warm, wet weather. Regularly monitor the weather and apply fungicides preventively if needed. Sanitize Equipment: Clean tools and machinery to avoid spreading the pathogen to healthy crops."
            ),
            'Corn_common_rust': DiseaseInfo(
                "Corn", "Common Rust",
                "Common rust appears as small, oval or elongated reddish-brown pustules on both sides of the leaves. Over time, these pustules can turn black. In severe infections, pustules may coalesce, causing leaf tissue to die. Caused by the fungus Puccinia sorghi.",
                "Reduced photosynthetic capacity due to damaged leaves. Decreased plant vigor and lower grain yield. Severe infections can cause premature leaf death.",
                "Use Resistant Varieties: Plant hybrid corn varieties that are resistant to common rust. Fungicide Application: Apply fungicides containing active ingredients like azoxystrobin or propiconazole at the first sign of symptoms. Monitor and Act Early: Regularly inspect fields, especially during cool, humid weather, and take action immediately when symptoms appear.",
                "Plant Early: Early planting can help the crop mature before peak conditions for rust infection. Field Sanitation: Remove infected plant debris to reduce fungal spore buildup. Ensure Proper Nutrition: Maintain balanced soil fertility to promote healthy, vigorous plants that are better able to resist infections. Space Plants Adequately: Ensure good air circulation to reduce leaf wetness, which is necessary for spore germination."
            ),
            'Corn_gray_leaf_spot': DiseaseInfo(
                "Corn", "Gray Leaf Spot",
                "Gray Leaf Spot manifests as rectangular, gray to tan lesions on corn leaves, often with a yellow halo. Lesions can coalesce, leading to large areas of dead tissue. Caused by Cercospora zeae-maydis.",
                "Reduced photosynthetic capacity due to extensive leaf damage. Decreased grain fill, resulting in reduced yields. Increased susceptibility to secondary infections.",
                "Plant Resistant Hybrids: Use corn hybrids bred for resistance to Gray Leaf Spot. Fungicide Application: Apply fungicides containing active ingredients like strobilurins or triazoles. Crop Rotation: Rotate with non-host crops to reduce fungal inoculum. Residue Management: Bury or remove infected plant debris after harvest to prevent the fungus from overwintering.",
                "Monitor Fields Regularly: Check for early symptoms, especially during warm and humid weather. Promote Air Circulation: Space plants adequately to reduce humidity around leaves. Optimize Plant Health: Use balanced fertilizers to maintain vigorous plants that can better resist infections. Plant Early: Early planting can help the crop avoid peak conditions for Gray Leaf Spot development."
            ),
            'Papaya_BacterialSpot': DiseaseInfo(
                "Papaya", "Bacterial Spot",
                "Symptoms include water-soaked spots on leaves, which later turn brown with yellow halos. On fruits, small, raised, water-soaked spots develop, which may turn brown and crack. Caused by Xanthomonas euvesicatoria pv. papayae.",
                "Decreased photosynthetic efficiency due to leaf damage. Reduced fruit quality and yield. Increased vulnerability to secondary infections.",
                "Use Copper-Based Bactericides: Apply copper-based sprays to suppress bacterial growth. Remove Infected Plant Parts: Prune and destroy affected leaves and fruits. Practice Good Sanitation: Sterilize tools and avoid working with wet plants.",
                "Improve Field Drainage: Ensure proper drainage to prevent water stagnation, which can promote disease development. Avoid Overhead Irrigation: Use drip irrigation to minimize water splashing, which spreads bacteria. Choose Resistant Varieties: Plant papaya cultivars less susceptible to bacterial spot. Rotate Crops: Avoid planting papaya in the same area to reduce bacterial buildup."
            ),
            'Papaya_Curl': DiseaseInfo(
                "Papaya", "Leaf Curl",
                "Papaya Leaf Curl disease is caused by the Papaya Leaf Curl Virus (PaLCuV), transmitted by whiteflies. Infected papaya plants show curling and distortion of leaves, yellowing, stunted growth, and reduced leaf size. The leaves may become thickened with an upward or downward curl.",
                "Poor plant growth due to disrupted photosynthesis. Reduced yield and fruit quality. Economic losses due to unmarketable fruits.",
                "Control Whiteflies: Use insecticides, sticky traps, or natural predators like ladybugs to control whitefly populations. Plant Resistant Varieties: Opt for papaya varieties resistant to Papaya Leaf Curl Virus. Remove Infected Plants: Uproot and destroy infected plants immediately.",
                "Use Healthy Planting Material: Ensure seeds and seedlings are virus-free. Maintain Field Hygiene: Remove weeds and alternate hosts that can harbor whiteflies or the virus. Monitor Fields Regularly: Inspect plants frequently for signs of whiteflies or early symptoms. Intercrop with Non-Host Plants: Plant non-host crops like maize as barriers to reduce whitefly migration."
            ),
            'Papaya_RingSpot': DiseaseInfo(
                "Papaya", "Ring Spot",
                "Papaya Ring Spot disease is caused by the Papaya Ring Spot Virus (PRSV), which belongs to the genus Potyvirus. Symptoms include yellowing and mottling of the leaves, narrowing and distortion of leaf blades, and distinctive concentric rings or spots on the fruit. Stems and petioles may also develop streaks or rings.",
                "Reduced fruit quality and yield. Stunted growth and weakened plants. Premature fruit drop and reduced marketability.",
                "Control Aphids: Use insecticides, reflective mulches, or natural predators like ladybugs to reduce aphid populations and transmission of the virus. Use Virus-Free Seeds or Plants: Plant certified virus-free seeds or seedlings to prevent initial infections. Remove Infected Plants: Uproot and destroy infected plants immediately to reduce the spread of the virus.",
                "Practice Crop Rotation: Rotate papayas with non-host crops to reduce the buildup of the virus and aphid populations. Monitor Fields Regularly: Inspect plants frequently for early symptoms and aphid activity to take quick action. Maintain Field Hygiene: Remove weeds and plant debris that can harbor aphids or serve as alternate hosts for the virus."
            ),
            'Potato_alternaria_solani': DiseaseInfo(
                "Potato", "Alternaria Solani",
                "Alternaria solani causes early blight in potatoes. Symptoms typically start as small, dark, concentric lesions on older leaves, which eventually expand and form irregular spots with a yellow halo. These lesions may coalesce, causing large areas of the leaf to die. The infection spreads to stems and tubers, leading to dark, sunken lesions. Infected tubers may develop soft rot, reducing their quality and marketability.",
                "Reduced photosynthesis due to extensive leaf damage. Decreased yield and poor tuber quality due to stem and tuber infection. Early plant death from severe infections, leading to loss of crop.",
                "Fungicide Application: Use fungicides such as chlorothalonil, mancozeb, or azoxystrobin to control the spread of Alternaria solani during periods of high humidity. Remove Infected Plant Material: Prune and destroy infected leaves, stems, and tubers to prevent the fungus from spreading. Crop Rotation: Rotate potatoes with non-host crops to reduce pathogen buildup in the soil.",
                "Improve Airflow: Space plants properly to allow for better airflow, reducing humidity around the foliage, which favors fungal growth. Regular Monitoring: Inspect potato plants regularly for early signs of leaf lesions, especially during wet weather. Maintain Soil Health: Ensure good soil drainage to prevent excess moisture around the plants, which can encourage fungal growth. Sanitation: Remove and destroy any plant debris from the field after harvest to reduce the potential for pathogen survival."
            ),
            'Potato_phytopthora_infestans': DiseaseInfo(
                "Potato", "Phytophthora Infestans",
                "Phytophthora infestans, the causative agent of late blight, starts as small, dark, water-soaked lesions on the older leaves, which expand and turn brown or black with a yellow halo. These lesions often have a characteristic oily appearance. Infected stems and tubers may also develop dark lesions that are soft and watery. As the disease progresses, the leaves shrivel and die, and the disease spreads quickly under wet conditions, particularly during periods of cool, humid weather.",
                "Reduced photosynthesis due to extensive leaf damage and defoliation. Significant yield loss from tuber infection, leading to soft rot and poor-quality potatoes. Rapid disease progression, causing rapid plant death under favorable conditions.",
                "Fungicide Application: Use systemic fungicides like mefenoxam or copper-based fungicides to manage late blight, applying during early signs of infection. Remove Infected Plant Parts: Prune and destroy infected leaves, stems, and tubers to reduce disease spread. Improve Air Circulation: Space plants adequately and avoid overhead irrigation to reduce humidity and prevent the conditions that favor fungal growth.",
                "Use Resistant Varieties: Choose potato varieties that have higher resistance to late blight. Regular Monitoring: Inspect potato plants frequently for signs of late blight, especially during wet and cool weather conditions. Field Sanitation: Remove fallen leaves and plant debris after harvest to reduce potential sources of infection. Crop Rotation: Rotate potatoes with non-host crops to reduce the buildup of pathogens in the soil. Proper Watering: Water potatoes at the base to avoid wetting the foliage and creating conditions that favor fungal growth."
            ),
            'Potato_virus': DiseaseInfo(
                "Potato", "Virus",
                "Potatoes can be affected by several types of viruses, with the most common being Potato virus Y (PVY), Potato leafroll virus (PLRV), and Potato virus X (PVX). Virus-infected plants often exhibit stunted growth, yellowing of the leaves (chlorosis), and leaf curling. Infected plants may show mosaic patterns, with dark green and light green patches on the leaves. Tubers may develop internal necrosis or streaks, reducing their marketability.",
                "Reduced yield due to poor plant growth and lower tuber quality. Reduced marketability of tubers due to internal damage or necrosis. Increased susceptibility to secondary infections and pests due to weakened plant health.",
                "Use Certified Seed: Plant only virus-free certified seed potatoes to prevent the introduction of viruses into the crop. Control Insect Vectors: Use insecticides to control aphids, which are the primary vectors for virus transmission. Remove Infected Plants: Immediately remove and destroy infected plants to reduce the spread of the virus to healthy plants. Rogue Infected Plants: Regularly inspect the crop and rogue out plants showing virus symptoms to limit the spread within the field.",
                "Crop Rotation: Rotate potatoes with non-host crops to reduce the buildup of virus-carrying insects in the soil. Field Sanitation: Remove all plant debris, particularly from infected plants, after harvest to minimize the risk of virus overwintering. Use Resistant Varieties: Plant virus-resistant potato varieties when available to reduce the impact of common potato viruses. Insect Control: Use yellow sticky traps and other methods to monitor and control aphid populations in the field."
            ),
            # Add Tomato diseases here
            'Tomato_light_blight': DiseaseInfo(
                "Tomato", "Late Blight",
                "Tomato Late Blight is a devastating disease caused by the oomycete pathogen Phytophthora infestans. It primarily affects the leaves, stems, and fruits of tomato plants. Early symptoms include greenish-gray or brown spots on leaves that quickly expand, often with water-soaked edges. In humid conditions, these spots may develop a white, powdery growth due to the pathogen's sporulation. On fruits, the disease causes large, brown, rough-textured lesions, rendering them inedible.",
                "Significant yield loss due to damaged fruits. Reduced photosynthetic ability from leaf destruction. Rapid plant decline and death in severe infections. Increased vulnerability to secondary infections.",
                "Use Fungicides: Apply fungicides such as chlorothalonil or mancozeb at the first sign of infection to control the spread. Remove Infected Plant Parts: Prune and destroy infected leaves, stems, and fruits immediately to reduce the source of spores. Improve Air Circulation: Space plants appropriately and avoid overcrowding to ensure good airflow, which reduces humidity and slows pathogen growth.",
                "Rotate Crops: Practice crop rotation with non-host plants to break the pathogen's life cycle in the soil. Choose Resistant Varieties: Plant tomato varieties bred for resistance to late blight to minimize susceptibility. Monitor Regularly: Inspect plants frequently, especially during wet and humid periods, to catch and address infections early. Field Sanitation: Clear plant debris and weeds regularly, as they can harbor the pathogen. Optimize Watering: Water plants at the base rather than overhead to keep foliage dry and less hospitable to P. infestans. Provide Balanced Nutrition: Use a balanced fertilizer to maintain plant vigor and increase resistance to diseases."
            ),

            'Tomato_septoria_leaf_spot': DiseaseInfo(
            "Tomato", "Septoria Leaf Spot",
            "Tomato Septoria Leaf Spot is caused by the fungal pathogen Septoria lycopersici. This disease primarily affects the leaves of tomato plants, usually starting on the older, lower leaves. Early symptoms appear as small, circular spots with dark brown edges and lighter gray centers, often containing tiny black fruiting bodies (pycnidia) of the fungus. As the infection progresses, the spots increase in number, leading to yellowing and eventual leaf drop.",
            "Loss of photosynthetic capacity due to extensive leaf damage. Premature leaf drop, exposing fruits to sunscald. Reduced fruit yield and quality. Weakening of the plant, making it more prone to other diseases.",
            "Use Fungicides: Apply fungicides like chlorothalonil or copper-based products at the first sign of infection to control the disease spread. Remove Infected Leaves: Prune and dispose of infected leaves to reduce fungal spore production and spread. Avoid Overhead Watering: Water at the base of the plant to prevent moisture on leaves, which facilitates fungal growth.",
            "Improve Plant Spacing: Ensure proper spacing between plants to promote airflow and reduce humidity. Practice Crop Rotation: Avoid planting tomatoes or other Solanaceae crops in the same spot year after year to prevent pathogen buildup in the soil. Inspect Regularly: Check plants frequently for early symptoms of infection, especially during humid conditions. Sanitize Tools and Equipment: Clean gardening tools after use to prevent spreading fungal spores between plants. Mulch Around Plants: Use organic mulch to reduce soil splash onto leaves, which can carry fungal spores. Choose Resistant Varieties: Opt for tomato cultivars with resistance to Septoria leaf spot when available. Maintain Healthy Plants: Provide consistent watering, adequate sunlight, and balanced fertilization to keep plants robust and more resilient to diseases."
            ),
            'Tomato_yellow_leaf_curl_virus': DiseaseInfo(
            "Tomato", "Yellow Leaf Curl Virus",
            "Tomato Yellow Leaf Curl Virus (TYLCV) is a viral disease caused by a Begomovirus, transmitted by the silverleaf whitefly (Bemisia tabaci). This disease causes severe stunting and yellowing of tomato plants. Early symptoms include upward curling and crinkling of young leaves, followed by interveinal yellowing. Infected plants may produce few or no fruits, and the fruits that do form are often small and misshapen. The virus thrives in warm, dry conditions, where whiteflies are most active.",
            "Stunted growth and weak plant development. Yellowing and curling of leaves, reducing photosynthesis. Drastically reduced fruit yield and quality. Increased susceptibility to environmental stress and secondary infections.",
            "Control Whiteflies: Use insecticides, biological controls (like predatory insects), or yellow sticky traps to reduce whitefly populations. Plant Resistant Varieties: Choose tomato varieties bred for resistance to TYLCV to minimize the impact of the virus. Use Physical Barriers: Install insect-proof nets or row covers to prevent whiteflies from accessing plants.",
            "Practice Crop Hygiene: Remove and destroy infected plants immediately to limit virus spread. Weed Management: Eliminate weeds that may serve as alternate hosts for whiteflies and the virus. Monitor Regularly: Check plants and surrounding areas frequently for signs of whitefly infestation or early symptoms of the disease. Intercrop Strategically: Plant companion crops like marigolds to repel whiteflies naturally. Maintain Healthy Soil: Use compost or organic matter to enrich soil, promoting stronger, more resilient plants. Optimize Irrigation: Provide consistent watering to reduce stress on plants, making them less susceptible to the virus. Rotate Crops: Avoid planting tomatoes in the same area year after year to disrupt the virus’s life cycle."
        )
        }

    def get_disease_info(self, disease_key):
        # Retrieve disease information by disease key
        return self.diseases.get(disease_key, None)
