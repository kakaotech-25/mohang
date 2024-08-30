import { DragDropContext, Droppable, Draggable } from 'react-beautiful-dnd';
import { useParams, useNavigate } from 'react-router-dom';
import PlannerData from '../../data/PlannerData';
import './PlannerDetails.css';
import { useState, useEffect } from 'react';
import deleteIcon from '../../assets/delete-icon.png';
import backIcon from '../../assets/back-icon.png';
import editIcon from '../../assets/editicon.png';
import PlannerModal from '../../components/PlannerModal/PlannerModal';

const PlannerDetails = () => {
  const { id } = useParams();
  const plan = PlannerData.find((p) => p.id === parseInt(id));
  const [destinations, setDestinations] = useState(plan.destinations);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedDestination, setSelectedDestination] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    loadKakaoMapScript(() => initializeMap(destinations, setSelectedDestination));
  }, [destinations]);

  const loadKakaoMapScript = (callback) => {
    const script = document.createElement('script');
    script.src = `https://dapi.kakao.com/v2/maps/sdk.js?appkey=1068045215b2426f764f379d3ed6c315&autoload=false`;
    script.async = true;
    script.onload = () => window.kakao.maps.load(callback);
    document.head.appendChild(script);

    return () => {
      document.head.removeChild(script);
    };
  };

  const initializeMap = (destinations, setSelectedDestination) => {
    const container = document.getElementById('map'); 
    const options = {
      center: new window.kakao.maps.LatLng(33.450701, 126.570667),
      level: 3,
    };
    const map = new window.kakao.maps.Map(container, options);

    const bounds = new window.kakao.maps.LatLngBounds();
    destinations.forEach(destination => {
      addMarkerToMap(map, destination, bounds, setSelectedDestination);
    });

    map.setBounds(bounds);
  };

  const addMarkerToMap = (map, destination, bounds, setSelectedDestination) => {
    const markerPosition = new window.kakao.maps.LatLng(destination.y, destination.x);
    const marker = new window.kakao.maps.Marker({
      position: markerPosition,
      map: map,
    });

    window.kakao.maps.event.addListener(marker, 'click', () => {
      setSelectedDestination(destination.name);
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

  const handleDelete = (index) => {
    const updatedDestinations = destinations.filter((_, i) => i !== index);
    setDestinations(updatedDestinations);
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
            <h1 className="planner-details-title">{plan.title}</h1>
            <p className="planner-details-period">{plan.period}</p>
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
                    <Draggable key={destination.name} draggableId={destination.name} index={index}>
                      {(provided) => (
                        <li
                          className={`planner-destination-item ${selectedDestination === destination.name ? 'selected' : ''}`}
                          ref={provided.innerRef}
                          {...provided.draggableProps}
                        >
                          <span
                            className="hamburger-button"
                            {...provided.dragHandleProps}
                          >
                            ☰
                          </span>
                          <span className="destination-name">{destination.name}</span>
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
          plan={{ title: plan.title, period: plan.period }}
          onClose={() => setIsModalOpen(false)}
          onSave={handleSave}
        />
      )}
    </div>
  );
};

export default PlannerDetails;
