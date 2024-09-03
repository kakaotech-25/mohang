from abc import ABCMeta, abstractmethod


class IModelRepository(metaclass=ABCMeta):
    @abstractmethod
    def custom_location_list(
            self,
            preferred_location: dict[int, int],
            page: int
    ) -> list[int]:
        raise NotImplementedError

    @abstractmethod
    def similar_content(
            self,
            contentId: int,
    ):
        raise NotImplementedError

