from model_serving.interface.controllers.model_controller import CustomTravelListRequest, CustomTravelListResponse, SimilarTravelListResponse
from fastapi import APIRouter, Depends

router = APIRouter(prefix="/travel")

@router.post("/custom/model", response_model=CustomTravelListResponse)
def AI_recommendation(
    page: int,
    body: CustomTravelListRequest
    ):
    """
    메인 페이지에서 AI 맞춤 추천 여행지를 요청합니다.
    ### URL 파라미터
    |파라미터명|파라미터값|
    |---|---|
    |page|int|
    """