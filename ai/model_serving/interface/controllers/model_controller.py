from fastapi import APIRouter, Depends, Query
from typing import Annotated
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


@router.post(
        '/custom/model',
        description="메인 페이지에서 AI 맞춤 추천 여행지를 요청합니다.",
        summary="[메인 페이지] AI를 이용한 유저 맞춤 추천 여행지 요청"
        )
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


@router.get(
        '/similar/{contentId}',
        description="여행지 상세페이지에서 여행지와 유사한 여행지를 추천합니다.",
        summary="[여행지 상세페이지] AI를 이용한 여행지 맞춤 추천 여행지 요청"
        )
@inject
def similar_content(
        contentId: int,
        page: int,
        model_service: ModelService = Depends(Provide[Container.model_service]),
):
    locations = model_service.similar_content(contentId=contentId, page=page)
    return SimilarTravelListResponse(contentIds=locations)