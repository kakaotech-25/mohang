import numpy as np
import random
import pandas as pd

# Load the CSV files
# keyword_extraction_df = pd.read_csv('/content/keyword_extraction_with_contentid.csv')
# combined_detail_df = pd.read_csv('/content/combined_detailfile.csv')

# Merge the two dataframes on 'contentid'
# merged_df = pd.merge(keyword_extraction_df, combined_detail_df[['contentid', 'areacode', 'sigungucode']], on='contentid', how='inner')

df = pd.read_csv('model/data/merged_final_data.csv')

df[['areacode', 'sigungucode', 'None']] = df['area_sigungu_combined'].str.split(' ', expand=True)
df = df.drop(columns=['None'])


# 200개 데이터만
# df = df.iloc[-200:]
# df.head()


# Define a simple proximity function for area codes (this can be replaced with a more complex mapping)
'''
def calculate_area_similarity(target_area, target_sigungu, area, sigungu):
    if target_area == area:
        # Same areacode gives a high score
        return 1.0
    elif target_sigungu == sigungu:
        # Same sigungucode gives a slightly lower score
        return 0.8
    else:
        # Different area and sigungu gets a lower score
        return 0.5
        '''

# Function to scale data using Z-score standardization
def z_score_standardize(values):
    mean_val = np.mean(values)
    std_val = np.std(values)
    if std_val == 0:
        return [0] * len(values)
    return [(x - mean_val) / std_val for x in values]

# Function to recommend items based on scaled scores
def recommend_items_with_z_score_standardized_scores(content_id, df, click_data, top_n=5, random_n=3):
    # Retrieve the labels, scores, and keywords for the given content_id
    target_item = df[df['contentid'] == content_id].iloc[0]
    target_labels = eval(target_item['labels'])
    target_scores = eval(target_item['scores'])
    target_keywords = set(target_item['unique_nouns_cleaned'].split(', '))
    target_areacode = target_item['areacode']
    target_sigungucode = target_item['sigungucode']

    # Sort click data by recency (assuming it's already sorted by time, but if not, sort by recency here)
    sorted_click_data = list(click_data.items())  # Click data is assumed to be sorted by recency by default

    # Apply weights to clicks based on recency (latest clicks have more weight)
    weighted_click_data = {}
    for index, (content_id, click_count) in enumerate(sorted_click_data):
        weight = 4 - (3 * (index / (len(sorted_click_data) - 1))) if len(sorted_click_data) > 1 else 4
        weighted_click_data[content_id] = click_count * weight

    # Z-score standardize weighted click data
    click_counts = np.array(list(weighted_click_data.values()))
    standardized_click_data = dict(zip(weighted_click_data.keys(), z_score_standardize(click_counts)))

    # Initialize a list to store recommendations with scores
    recommendations = []
    seen_content_ids = set(weighted_click_data.keys())  # Include already clicked content in the seen set

    # Z-score standardize areacode and sigungucode by assigning numerical values
    area_codes = pd.factorize(df['areacode'])[0]
    sigungu_codes = pd.factorize(df['sigungucode'])[0]
    df['areacode_standardized'] = z_score_standardize(area_codes)
    df['sigungucode_standardized'] = z_score_standardize(sigungu_codes)
    df['score_standardized'] = z_score_standardize(df['scores'].apply(eval).apply(np.mean))

    for idx, row in df.iterrows():
        if row['contentid'] == content_id:
            continue

        # Skip if this content ID is already in the seen set
        if row['contentid'] in seen_content_ids:
            continue

        # Compare labels
        current_labels = eval(row['labels'])
        current_scores = eval(row['scores'])
        label_score = sum(min(target_scores[target_labels.index(label)], current_scores[current_labels.index(label)])
                          for label in set(target_labels) & set(current_labels))

        # Compare keywords
        current_keywords = set(row['unique_nouns_cleaned'].split(', '))
        keyword_score = len(target_keywords & current_keywords)

        # Get standardized scores for area code, sigungu code, and cosine similarity
        area_standardized = row['areacode_standardized']
        sigungu_standardized = row['sigungucode_standardized']
        score_standardized = row['score_standardized']

        # Get standardized click count for the current item, default to 0 if not found
        standardized_click_score = standardized_click_data.get(row['contentid'], 0)

        # Calculate area similarity
        # area_similarity = calculate_area_similarity(target_areacode, target_sigungucode, row['areacode'], row['sigungucode'])

        # Combine scores with area similarity weighted
        # total_score = (area_standardized + sigungu_standardized + score_standardized +
        #                standardized_click_score + label_score + keyword_score) * area_similarity

        total_score = (score_standardized +
                       standardized_click_score + label_score + keyword_score)

        # Add content to recommendations if not already seen
        recommendations.append((row['contentid'], total_score))
        seen_content_ids.add(row['contentid'])  # Track seen content to avoid duplicates

    # Sort recommendations based on the total score
    recommendations = sorted(recommendations, key=lambda x: x[1], reverse=True)

    # Take the top 20 items from the sorted recommendations
    top_20_recommendations = recommendations[:20]

    # Select n items randomly from the top 20 recommendations
    random_recommended_content_ids = random.sample([item[0] for item in top_20_recommendations], min(random_n, len(top_20_recommendations)))

    return random_recommended_content_ids

user_data = pd.read_pickle('model/data/modified_preference_list.pkl')
user_data.head()
print(type(user_data['preference_list'][1]))
user_data['preference_list'][1]

# 유저 성향을 반영한 함수 (content_id 없이 클릭 데이터만을 바탕으로 추천)
def recommend_by_user_preference(click_data, df, top_n=5, random_n=3):
    # 클릭 데이터를 최신순으로 정렬 (이미 정렬되어 있다고 가정)
    print(click_data)
    sorted_click_data = list(click_data.items())

    # 최신 클릭 데이터에 가중치를 적용
    weighted_click_data = {}
    for index, (content_id, click_count) in enumerate(sorted_click_data):
        weight = 4 - (3 * (index / (len(sorted_click_data) - 1))) if len(sorted_click_data) > 1 else 4
        weighted_click_data[content_id] = click_count * weight

    # 클릭 데이터를 Z-score로 표준화
    click_counts = np.array(list(weighted_click_data.values()))
    standardized_click_data = dict(zip(weighted_click_data.keys(), z_score_standardize(click_counts)))

    # 추천 목록을 저장할 리스트와 이미 본 콘텐츠 ID들을 저장할 집합
    recommendations = []
    seen_content_ids = set(weighted_click_data.keys())

    for idx, row in df.iterrows():
        if row['contentid'] in seen_content_ids:
            continue

        # 콘텐츠의 라벨 및 키워드 비교
        current_labels = eval(row['labels'])
        current_keywords = set(row['unique_nouns_cleaned'].split(', '))

        # 클릭 데이터에 따라 점수 부여
        standardized_click_score = standardized_click_data.get(row['contentid'], 0)

        # 콘텐츠의 지역 코드 유사성 계산
        # area_similarity = calculate_area_similarity(
        #     target_area=None, target_sigungu=None,
        #     area=row['areacode'], sigungu=row['sigungucode']
        # )

        # 총점 계산
        total_score = standardized_click_score # * area_similarity
        recommendations.append((row['contentid'], total_score))

    # 점수에 따라 추천 목록 정렬
    recommendations = sorted(recommendations, key=lambda x: x[1], reverse=True)

    # 상위 top_n개의 추천 항목 선택
    top_recommendations = recommendations[:top_n]

    # 상위 추천 항목 중 랜덤으로 random_n개 선택
    random_recommended_content_ids = random.sample([item[0] for item in top_recommendations], min(random_n, len(top_recommendations)))

    return random_recommended_content_ids

# 콘텐츠 유사성만을 고려한 함수
def recommend_by_content_similarity(content_id, df, top_n=5, random_n=3):
    target_item = df[df['contentid'] == content_id].iloc[0]
    target_labels = eval(target_item['labels'])
    target_scores = eval(target_item['scores'])
    target_keywords = set(target_item['unique_nouns_cleaned'].split(', '))
    #target_areacode = target_item['areacode']
    #target_sigungucode = target_item['sigungucode']

    recommendations = []

    for idx, row in df.iterrows():
        if row['contentid'] == content_id:
            continue

        current_labels = eval(row['labels'])
        current_scores = eval(row['scores'])
        label_score = sum(min(target_scores[target_labels.index(label)], current_scores[current_labels.index(label)])
                          for label in set(target_labels) & set(current_labels))

        current_keywords = set(row['unique_nouns_cleaned'].split(', '))
        keyword_score = len(target_keywords & current_keywords)

        # area_similarity = calculate_area_similarity(target_areacode, target_sigungucode, row['areacode'], row['sigungucode'])

        total_score = (label_score + keyword_score) # * area_similarity
        recommendations.append((row['contentid'], total_score))

    recommendations = sorted(recommendations, key=lambda x: x[1], reverse=True)
    top_20_recommendations = recommendations[:20]
    random_recommended_content_ids = random.sample([item[0] for item in top_20_recommendations], min(random_n, len(top_20_recommendations)))

    return random_recommended_content_ids

from sklearn.metrics import precision_score, recall_score, f1_score

def evaluate_recommendations(recommended_ids, actual_ids):
    """
    Evaluate the performance of the recommendation system using Precision, Recall, and F1-Score.

    Parameters:
    - recommended_ids (list): List of recommended content IDs.
    - actual_ids (list): List of actual relevant content IDs (preference_list).

    Returns:
    - precision (float): Precision of the recommendations.
    - recall (float): Recall of the recommendations.
    - f1 (float): F1-Score of the recommendations.
    """
    actual_ids_set = set(actual_ids)
    true_positives = len([id for id in recommended_ids if id in actual_ids_set])
    false_positives = len(recommended_ids) - true_positives
    false_negatives = len(actual_ids) - true_positives

    print(f"True Positives: {true_positives}, False Positives: {false_positives}, False Negatives: {false_negatives}")

    precision = true_positives / (true_positives + false_positives) if (true_positives + false_positives) > 0 else 0
    recall = true_positives / (true_positives + false_negatives) if (true_positives + false_negatives) > 0 else 0
    f1 = (2 * precision * recall) / (precision + recall) if (precision + recall) > 0 else 0

    return precision, recall, f1

'''
# Pick one user's data from the pickle file (for example, the first user)
for user_index in range(len(user_data)):
    preference_list = user_data.iloc[user_index]['preference_list']  # Ground truth (actual IDs)
    click_data = user_data.iloc[user_index]['click']  # Click data for recommendations

    # 추천 함수 실행 및 평가
    print("\nEvaluating user preference-based recommendations:")
    recommended_ids_preference = recommend_by_user_preference(click_data, df, top_n=20, random_n=20)

    # 추천해 준 리스트
    print(f"recommended_ids_preference: {recommended_ids_preference}")

    # 실제 선호 목록
    print(f"preference_list: {preference_list}")

    # 추천된 ID와 실제 선호 목록을 평가
    precision, recall, f1 = evaluate_recommendations(recommended_ids_preference, preference_list)
    print(f"User Preference - Precision: {precision:.2f}, Recall: {recall:.2f}, F1-Score: {f1:.2f}")

for idx in range(2641, 2641+200):
    print("\nEvaluating content similarity-based recommendations:")
    # 콘텐츠 유사성 기반 추천 실행
    recommended_ids_similarity = recommend_by_content_similarity(df["contentid"][idx], df, top_n=20, random_n=20)

    # 추천해 준 리스트
    print(f"recommended_ids_similarity: {recommended_ids_similarity}")

    # 실제 선호 목록
    print(f"preference_list: {preference_list}")

    # 추천된 ID와 실제 선호 목록을 평가
    precision, recall, f1 = evaluate_recommendations(recommended_ids_similarity, preference_list)
    print(f"Content Similarity - Precision: {precision:.2f}, Recall: {recall:.2f}, F1-Score: {f1:.2f}")
'''