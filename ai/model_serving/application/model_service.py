from dependency_injector.wiring import inject
from model_serving.domain.repository.model_repo import IModelRepository


class ModelService:
    @inject
    def __init__(
            self,
            model_repo: IModelRepository
    ):
        self.model_repo = model_repo

    def custom_location_list(
            self,
            preferred_location: dict[str, int],
            page: int,
    ) -> list[int]:
        locations = self.model_repo.custom_location_list(preferred_location=preferred_location, page=page)
        return locations

    def similar_content(
            self,
            contentId: int,
            page: int,
    ) -> list[int]:
        locations = self.model_repo.similar_content(contentId=contentId)
        return locations

