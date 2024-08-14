import uvicorn
from fastapi import FastAPI
from pydantic import BaseModel
from model.interface.controllers.model_controller import router as model_router
from containers import Container
app = FastAPI()
app.container = Container()
app.include_router(model_router)


# 유저가 방문한 여행지
class Visited(BaseModel):
    pass


# 유저 맞춤 추천 여행지
@app.get("/recommendationList")
def get_recommendation_list(visited: Visited):
    return {"Hello": "FastAPI"}


if __name__ == "__main__":
    uvicorn.run("main:app", host="127.0.0.1", reload=True)