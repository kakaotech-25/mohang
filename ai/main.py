import uvicorn
from fastapi import FastAPI
from model_serving.interface.controllers.model_controller import router as model_router
from model_serving.documentation.model_controller_docs import router as docs_router
from containers import Container
from fastapi.middleware.cors import CORSMiddleware


app = FastAPI()
app.container = Container()
app.include_router(model_router)
#app.include_router(docs_router)

origins = [
    "http://localhost:8080",
]

app.add_middleware(
    CORSMiddleware,
    allow_origins=origins,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


if __name__ == "__main__":
    uvicorn.run("main:app", host="127.0.0.1", reload=True)