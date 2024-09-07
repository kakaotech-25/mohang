import uvicorn
from fastapi import FastAPI
from model_serving.interface.controllers.model_controller import router as model_router
from containers import Container


app = FastAPI()
app.container = Container()
app.include_router(model_router)


if __name__ == "__main__":
    uvicorn.run("main:app", host="127.0.0.1", reload=True)