from model.domain.repository.model_repo import IModelRepository


class ModelRepository(IModelRepository):
    def custom_location_list(
            self,
            preferred_location: dict[str, int],
            page: int,
    ) -> list[str]:
        # TODO: 모델 모듈을 이용해 추천 결과를 반환
        return ["123456"]
