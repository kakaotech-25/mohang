import pytest
from model_serving.application.model_service import ModelService
from model_serving.interface.controllers import model_controller


@pytest.fixture
def model_interface_dependencies(mocker):
    model_service_mock = mocker.Mock(spec=ModelService)

    return model_service_mock


def test_custom_travel_list(model_interface_dependencies):
    model_service_mock = model_interface_dependencies

    model_service_mock.custom_location_list.return_value = {
        "contentIds": ["123456"]
    }

    model_controller.custom_travel_list(
        page=1,
        req_body=model_controller.CustomTravelListRequest(
            preferredLocation={"123123": 1}
        ),
        model_service=model_service_mock
    )

    model_service_mock.custom_location_list.assert_called_once_with(
        preferred_location={"123123": 1},
        page=1
    )
