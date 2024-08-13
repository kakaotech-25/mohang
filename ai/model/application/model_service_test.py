import pytest
from model.domain.repository.model_repo import IModelRepository


@pytest.fixture
def model_service_test(mocker):
    model_repo_mock = mocker.Mock(spec=IModelRepository)

    return model_repo_mock
