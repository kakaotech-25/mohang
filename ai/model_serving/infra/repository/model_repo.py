from model_serving.domain.repository.model_repo import IModelRepository
from model.Recommendation import personalized_recommendation, UserPreferences
from typing import List


class ModelRepository(IModelRepository):
    def custom_location_list(
            self,
            preferred_location: List,
            page: int,
    ) -> list[int]:
        # TODO: 방문 컨텐츠 리스트와 방문 수를 반영한 추천 모듈 실행
        # return personalized_recommendation(UserPreferences(preferred_destinations=list([preferred_location[0].contentId])))\
        return list([preferred_location[0].contentId])


    def similar_content(
            self,
            contentId: int,
    ) -> list[int]:
        # TODO: 단순히 해당 컨텐츠와 관련된 여행지를 추천
        # return personalized_recommendation(UserPreferences(preferred_destinations=[location]))
        return [contentId]