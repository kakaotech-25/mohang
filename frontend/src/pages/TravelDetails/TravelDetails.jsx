import './TravelDetails.css';
import { useState } from 'react';
import { useParams } from 'react-router-dom';
import TravelData from '../../data/TravelData';
import plannerIcon from '../../assets/plannericon.png'; // plannericon.png 경로에 맞게 조정

const TravelDetails = () => {
  const { id } = useParams();
  const card = TravelData.find(item => item.id === parseInt(id));

  const [isModalOpen, setIsModalOpen] = useState(false);

  const handleAddToPlanner = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  return (
    <div className="travel-details-container">
      <div className="travel-details-left">
        <div className="title-and-button">
          <div>
            <h1>{card.title}</h1>
            <p className="location">{card.location}</p>
          </div>
          <div className="add-to-planner">
            <button onClick={handleAddToPlanner} className="planner-button">
              <img src={plannerIcon} alt="플래너 아이콘" />
            </button>
            <span>플래너에 담기</span>
          </div>
        </div>
        <div className="tags">
          {card.tags.map((tag, index) => (
            <span key={index} className="tag">#{tag} </span>
          ))}
        </div>
        <p className="description">{card.description}</p>
      </div>
      <div className="travel-details-right">
        <div className="travel-image-container">
          <img src={card.image} alt={card.title} className="travel-image" />
        </div>
      </div>

      {isModalOpen && (
        <div className="modal-overlay">
          <div className="modal">
            <h2>여행 일정</h2>
            <p>----리스트 공간----</p>
            <button onClick={closeModal}>save</button>
          </div>
        </div>
      )}
    </div>
  );
};

export default TravelDetails;
