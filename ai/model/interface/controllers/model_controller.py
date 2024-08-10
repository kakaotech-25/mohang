from fastapi import APIRouter
from pydantic import BaseModel

router = APIRouter(prefix='/model')

class CustomTravelListResponse(BaseModel):
    length: int
    contentid: list[str]

@router.post('/customTravelList')
def custom_travel_list(
        keywords: list[str],
        preferredLocation: dict[str, int],
) -> CustomTravelListResponse:
    return { "length": 1, "contentid": ['123456'] }