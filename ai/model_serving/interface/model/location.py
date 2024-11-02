from pydantic import BaseModel


class PreferredLocation(BaseModel):
    contentId: int
    clicked: int
