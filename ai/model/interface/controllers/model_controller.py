from fastapi import APIRouter, Depends
from pydantic import BaseModel
from dependency_injector.wiring import inject, Provide
from model.application.model_service import ModelService
from containers import Container
router = APIRouter(prefix='/model')


class CustomTravelListRequest(BaseModel):
    preferredLocation: dict[str, int]


class CustomTravelListResponse(BaseModel):
    contentIds: list[str]


@router.post('/customTravelList')
@inject
def custom_travel_list(
        page: int,
        req_body: CustomTravelListRequest,
        model_service: ModelService = Depends(Provide[Container.model_service]),
) -> CustomTravelListResponse:
    # TODO: 디버깅!!!
    locations = model_service.custom_location_list(
        preferred_location=req_body.preferredLocation,
        page=page,
    )

    return {
        'contentIds': locations
    }
