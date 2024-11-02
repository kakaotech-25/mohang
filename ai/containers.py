from dependency_injector import containers, providers
from model_serving.application.model_service import ModelService
from model_serving.infra.repository.model_repo import ModelRepository


class Container(containers.DeclarativeContainer):
    wiring_config = containers.WiringConfiguration(
        packages=["model_serving"]
    )

    model_repo = providers.Factory(ModelRepository)
    model_service = providers.Factory(ModelService, model_repo=model_repo)
