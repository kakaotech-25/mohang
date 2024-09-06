from abc import ABCMeta, abstractmethod
from typing import List
from model_serving.interface.model.location import PreferredLocation


class IModelRepository(metaclass=ABCMeta):
    @abstractmethod
    def custom_location_list(
            self,
            preferred_location: List[PreferredLocation],
            page: int
    ) -> list[int]:
        raise NotImplementedError

    @abstractmethod
    def similar_content(
            self,
            contentId: int,
    ):
        raise NotImplementedError

