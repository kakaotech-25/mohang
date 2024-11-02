from fastapi import APIRouter, Depends
from pydantic import BaseModel
from dependency_injector.wiring import inject, Provide
from model_serving.application.model_service import ModelService
from containers import Container
from typing import List
from model_serving.interface.model.location import PreferredLocation


router = APIRouter(prefix='/travel')


class CustomTravelListRequest(BaseModel):
    preferredLocation: List[PreferredLocation]


class CustomTravelListResponse(BaseModel):
    contentIds: list[int]


@router.post('/custom/model')
@inject
def custom_travel_list(
        page: int,
        req_body: CustomTravelListRequest,
        model_service: ModelService = Depends(Provide[Container.model_service]),
) -> CustomTravelListResponse:
    locations = model_service.custom_location_list(
        preferred_location=req_body.preferredLocation,
        page=page,
    )

    return CustomTravelListResponse(contentIds=locations)


class SimilarTravelListResponse(BaseModel):
    contentIds: list[int]


@router.get('/similar/{contentId}')
@inject
def similar_content(
        contentId: int,
        page: int,
        model_service: ModelService = Depends(Provide[Container.model_service]),
):
    locations = model_service.similar_content(contentId=contentId, page=page)
    return SimilarTravelListResponse(contentIds=locations)