from model_serving.domain.repository.model_repo import IModelRepository
from model.Recommendation import recommend_by_user_preference, recommend_by_content_similarity, df
from typing import List
from model_serving.interface.model.location import PreferredLocation


class ModelRepository(IModelRepository):
    def custom_location_list(
            self,
            preferred_location: List[PreferredLocation],
            page: int,
    ) -> list[int]:
        # TODO: 방문 컨텐츠 리스트와 방문 수를 반영한 추천 모듈 실행
        pl = dict()
        for e in preferred_location:
            pl[e.contentId] = e.clicked
        recommended_ids_preference = recommend_by_user_preference(pl, df, top_n=20, random_n=20)
        return recommended_ids_preference


    def similar_content(
            self,
            contentId: int,
    ) -> list[int]:
        # TODO: 단순히 해당 컨텐츠와 관련된 여행지를 추천
        recommended_ids_similarity = recommend_by_content_similarity(contentId, df, top_n=20, random_n=20)
        return recommended_ids_similarity
