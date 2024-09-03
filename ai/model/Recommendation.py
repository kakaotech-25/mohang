from pydantic import BaseModel
import pandas as pd
import numpy as np
import torch
from sklearn.metrics.pairwise import cosine_similarity
from typing import List

data = pd.read_pickle('model/data/data.pkl')

# 사용자 데이터 모델 정의
class UserPreferences(BaseModel):
    preferred_destinations: List[int]  # contentid 리스트

# 사용자 임베딩 생성 함수
def get_user_embedding(preferred_destinations, destination_data):
    embeddings = destination_data[destination_data['contentid'].isin(preferred_destinations)]['embedding']
    print(embeddings)
    return torch.tensor(np.vstack(embeddings)).mean(dim=0).cpu().numpy()

# 추천 시스템 함수
def recommend_destinations(user_embedding, destination_data, top_k=10):
    embeddings = np.vstack(destination_data['embedding'])
    similarities = cosine_similarity([user_embedding], embeddings)[0]

    sorted_indices = similarities.argsort()[::-1]
    recommended_destinations = destination_data.iloc[sorted_indices][:top_k]
    return recommended_destinations

def personalized_recommendation(user_prefs: UserPreferences):
    user_embedding = get_user_embedding(user_prefs.preferred_destinations, data)
    recommended_destinations = recommend_destinations(user_embedding, data)
    t = recommended_destinations[['contentid']].values.tolist()
    return [item for sublist in t for item in sublist]