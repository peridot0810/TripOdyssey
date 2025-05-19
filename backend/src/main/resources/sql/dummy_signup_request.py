import requests
import json
import random

url = "http://localhost:8080/auth/signup"
headers = {
    "Content-Type": "application/json"
}

nicknames = [
    "지훈", "민지", "현우", "수빈", "도윤", "예은", "시우", "하은", "건우",
    "서연", "준서", "윤아", "지후", "예진", "하람", "태윤", "소율", "민재", "유진", "현서",
    "서준", "아린", "세진", "예린", "지아", "도현", "채원", "하진", "승우", "나윤"
]
genders = ["male", "female"]

for i in range(1, 21):
    user_id = f"user{i:02d}"
    data = {
        "id": user_id,
        "email": f"{user_id}@example.com",
        "pw": "1111",
        "nickname": random.choice(nicknames),
        "gender": random.choice(genders),
        "age": random.randint(15, 40)
    }

    response = requests.post(url, headers=headers, data=json.dumps(data))
    print(f"[{user_id}] Status Code: {response.status_code} | Response: {response.text}")
