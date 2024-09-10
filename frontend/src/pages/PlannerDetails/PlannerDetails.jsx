import { DragDropContext, Droppable, Draggable } from "react-beautiful-dnd";
import { useParams, useNavigate } from "react-router-dom";
import { useState, useEffect, useLayoutEffect } from "react";
import deleteIcon from "../../assets/delete-icon.png";
import backIcon from "../../assets/back-icon.png";
import editIcon from "../../assets/editicon.png";
import PlannerModal from "../../components/PlannerModal/PlannerModal";
import axiosInstance from "../login/axiosInstance";
import "./PlannerDetails.css";

const PlannerDetails = () => {
  const { id } = useParams();
  const [scheduleName, setScheduleName] = useState("");
  const [schedulePeriod, setSchedulePeriod] = useState("");
  const [destinations, setDestinations] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedDestination, setSelectedDestination] = useState(null);
  const navigate = useNavigate();

  // eslint-disable-next-line no-undef
  useLayoutEffect(() => {
    const fetchPlannerData = async () => {
      try {
        const response = await axiosInstance.get(`/schedule/trips/${id}`);
        const tripData = response.data;
        // const tripData = {
        //   tripScheduleResponse: {
        //     scheduleId: null,
        //     scheduleName: "제주도 여행",
        //     startTime: "2024-03-01",
        //     endTime: "2030-01-10",
        //   },
        //   findTripsOnSchedules: [
        //     {
        //       tripId: null,
        //       placeName: "롯데월드2",
        //       coordinateX: 128.576,
        //       coordinateY: 35.87,
        //     },
        //     {
        //       tripId: null,
        //       placeName: "롯데월드3",
        //       coordinateX: 129.224,
        //       coordinateY: 35.839,
        //     },
        //   ],
        // };
        setScheduleName(tripData.tripScheduleResponse.scheduleName);
        setSchedulePeriod(
          `${tripData.tripScheduleResponse.startTime} - ${tripData.tripScheduleResponse.endTime}`
        );
        setDestinations(tripData.findTripsOnSchedules);
      } catch (error) {
        console.error("Failed to fetch planner data:", error);
      }
    };

    fetchPlannerData();
  }, [id]);

  useEffect(() => {
    if (destinations.length > 0) {
      loadKakaoMapScript(() =>
        initializeMap(destinations, setSelectedDestination)
      );
    }
  }, [destinations]);

  const loadKakaoMapScript = (callback) => {
    const kakaoMapApiKey = import.meta.env.VITE_KAKAO_MAP_API_KEY;

    if (!kakaoMapApiKey) {
      console.error("Kakao Map API key is missing");
      return;
    }

    const script = document.createElement("script");
    script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=${kakaoMapApiKey}&autoload=false`;
    script.async = true;
    script.onload = () => window.kakao.maps.load(callback);
    document.head.appendChild(script);

    return () => {
      document.head.removeChild(script);
    };
  };

  const initializeMap = (destinations, setSelectedDestination) => {
    const container = document.getElementById("map");
    const options = {
      center: new window.kakao.maps.LatLng(33.450701, 126.570667),
      level: 3,
    };
    const map = new window.kakao.maps.Map(container, options);

    const bounds = new window.kakao.maps.LatLngBounds();
    destinations.forEach((destination) => {
      addMarkerToMap(map, destination, bounds, setSelectedDestination);
    });

    map.setBounds(bounds);
  };

  const addMarkerToMap = (map, destination, bounds, setSelectedDestination) => {
    const markerPosition = new window.kakao.maps.LatLng(
      destination.coordinateY,
      destination.coordinateX
    );
    const marker = new window.kakao.maps.Marker({
      position: markerPosition,
      map: map,
    });

    window.kakao.maps.event.addListener(marker, "click", () => {
      setSelectedDestination(destination.placeName);
    });

    bounds.extend(markerPosition);
  };

  const handleDragEnd = (result) => {
    if (!result.destination) return;

    const updatedDestinations = Array.from(destinations);
    const [reorderedItem] = updatedDestinations.splice(result.source.index, 1);
    updatedDestinations.splice(result.destination.index, 0, reorderedItem);

    setDestinations(updatedDestinations);
  };

  const handleDelete = async (index) => {
    const updatedDestinations = destinations.filter((_, i) => i !== index);
    try {
      await axiosInstance.delete(`planner/schedule/${id}`);
      setDestinations(updatedDestinations);
    } catch (e) {
      console.error("Failed to delete destination:", e);
    }
  };

  const handleEditClick = () => {
    setIsModalOpen(true);
  };

  const handleSave = (updatedPlan) => {
    // 일정 수정 로직 추가 필요
    setIsModalOpen(false);
  };

  return (
    <div className="planner-details-page">
      <button className="back-button" onClick={() => navigate(-1)}>
        <img src={backIcon} alt="뒤로가기 아이콘" className="back-icon" />
        뒤로가기
      </button>

      <div className="planner-details-container">
        <div className="planner-details-list">
          <button className="edit-plan-button" onClick={handleEditClick}>
            <img src={editIcon} alt="수정 아이콘" className="edit-icon" />
            일정 수정
          </button>
          <div className="planner-details-header">
            <h1 className="planner-details-title">{scheduleName}</h1>
            <p className="planner-details-period">{schedulePeriod}</p>
          </div>
          <DragDropContext onDragEnd={handleDragEnd}>
            <Droppable droppableId="destinations">
              {(provided) => (
                <ul
                  className="planner-destinations-list"
                  {...provided.droppableProps}
                  ref={provided.innerRef}
                >
                  {destinations.map((destination, index) => (
                    <Draggable
                      key={destination.placeName}
                      draggableId={destination.placeName}
                      index={index}
                    >
                      {(provided) => (
                        <li
                          className={`planner-destination-item ${
                            selectedDestination === destination.placeName
                              ? "selected"
                              : ""
                          }`}
                          ref={provided.innerRef}
                          {...provided.draggableProps}
                        >
                          <span
                            className="hamburger-button"
                            {...provided.dragHandleProps}
                          >
                            ☰
                          </span>
                          <span className="destination-name">
                            {destination.placeName}
                          </span>
                          <button
                            className="delete-button"
                            onClick={() => handleDelete(index)}
                          >
                            <img src={deleteIcon} alt="삭제" />
                          </button>
                        </li>
                      )}
                    </Draggable>
                  ))}
                  {provided.placeholder}
                </ul>
              )}
            </Droppable>
          </DragDropContext>
        </div>
        <div className="planner-details-map" id="map">
          {/* 지도가 렌더링될 공간 */}
        </div>
      </div>

      {isModalOpen && (
        <PlannerModal
          isOpen={isModalOpen}
          mode="edit"
          title="일정 수정"
          plan={{ title: scheduleName, period: schedulePeriod }}
          onClose={() => setIsModalOpen(false)}
          onSave={handleSave}
        />
      )}
    </div>
  );
};

export default PlannerDetails;
