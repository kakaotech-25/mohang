import { DragDropContext, Droppable, Draggable } from 'react-beautiful-dnd';
import { useParams, useNavigate } from 'react-router-dom';
import PlannerData from '../../data/PlannerData';
import './PlannerDetails.css';
import { useState } from 'react';
import deleteIcon from '../../assets/delete-icon.png';
import backIcon from '../../assets/back-icon.png';
import editIcon from '../../assets/editicon.png';

const PlannerDetails = () => {
  const { id } = useParams();
  const plan = PlannerData.find((p) => p.id === parseInt(id));
  const [destinations, setDestinations] = useState(plan.destinations);
  const navigate = useNavigate();

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

  return (
    <div className="planner-details-page">
      <button className="back-button" onClick={() => navigate(-1)}>
        <img src={backIcon} alt="뒤로가기 아이콘" className="back-icon" />
        뒤로가기
      </button>

      <div className="planner-details-container">
        <div className="planner-details-list">
          <button className="edit-plan-button">
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
                    <Draggable key={destination} draggableId={destination} index={index}>
                      {(provided) => (
                        <li
                          className="planner-destination-item"
                          ref={provided.innerRef}
                          {...provided.draggableProps}
                        >
                          <span
                            className="hamburger-button"
                            {...provided.dragHandleProps}
                          >
                            ☰
                          </span>
                          <span className="destination-name">{destination}</span>
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
        <div className="planner-details-map">
          <p>지도 공간</p>
        </div>
      </div>
    </div>
  );
};

export default PlannerDetails;
