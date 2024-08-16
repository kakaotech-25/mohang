from abc import ABCMeta, abstractmethod


class IModelRepository(metaclass=ABCMeta):
    @abstractmethod
    def custom_location_list(
            self,
            preferred_location: dict[str, int],
            page: int
    ) -> list[str]:
        raise NotImplementedError
