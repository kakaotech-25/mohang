from dependency_injector.wiring import inject
from model_serving.domain.repository.model_repo import IModelRepository
from typing import List

from model_serving.interface.model.location import PreferredLocation


# from model_serving.interface.controllers.model_controller import PreferredLocation


class ModelService:
    @inject
    def __init__(
            self,
            model_repo: IModelRepository
    ):
        self.model_repo = model_repo

    def custom_location_list(
            self,
            preferred_location: List[PreferredLocation],
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

