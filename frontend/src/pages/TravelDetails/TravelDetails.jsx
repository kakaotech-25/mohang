import './TravelDetails.css';
import { useState } from 'react';
import { useParams } from 'react-router-dom';
import TravelData from '../../data/TravelData';
import plannerIcon from '../../assets/plannericon.png';
import addIcon from '../../assets/editicon.png';
import TravelCarousel from '../../components/TravelCarousel/TravelCarousel';
import PlannerData from '../../data/PlannerData';
import PlannerModal from '../../components/PlannerModal/PlannerModal';

const TravelDetails = () => {
  const { id } = useParams();
  const card = TravelData.find(item => item.contentId === parseInt(id)); // contentId로 수정

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isAddPlanModalOpen, setIsAddPlanModalOpen] = useState(false);

  const handleAddToPlanner = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const handleAddPlan = () => {
    setIsAddPlanModalOpen(true);
  };

  const closeAddPlanModal = () => {
    setIsAddPlanModalOpen(false);
  };

  // 현재 여행지와 비슷한 여행지 필터링 (예: 같은 키워드가 있는 여행지)
  const similarCards = TravelData.filter(item =>
    item.keywords.some(keyword => card.keywords.includes(keyword)) && item.contentId !== card.contentId
  );

  return (
    <div className="travel-details-container">
      <div className="details-upper">
        <div className="travel-details-left">
          <div className="title-and-button">
            <div>
              <h1>{card.name}</h1> {/* name으로 수정 */}
              <p className="location">{card.placeName}</p> {/* placeName으로 수정 */}
            </div>
            <div className="add-to-planner">
              <button onClick={handleAddToPlanner} className="planner-button">
                <img src={plannerIcon} alt="플래너 아이콘" />
              </button>
              <span>플래너에 담기</span>
            </div>
          </div>
          <div className="tags">
            {card.keywords.map((keyword, index) => (
              <span key={index} className="tag">#{keyword} </span>
            ))}
          </div>
          <p className={`description ${card.description.length > 560 ? 'scrollable-description' : ''}`}>
            {card.description}
          </p>
        </div>
        <div className="travel-details-right">
          <div className="travel-image-container">
            <img src={card.tripImageUrl} alt={card.name} className="travel-image" /> {/* tripImageUrl로 수정 */}
          </div>
        </div>
      </div>

      {isModalOpen && (
        <div className="td-modal-overlay">
          <div className="td-modal-content">
            <div className="td-modal-header">
              <h2>여행 일정</h2>
            </div>
            <div className="td-modal-body">
              <ul className="td-planner-list">
                {PlannerData.map((item) => (
                  <li key={item.id} className="td-planner-list-item">
                    <span className="td-planner-title">{item.title}</span>
                    <input type="checkbox" className="td-planner-checkbox" />
                  </li>
                ))}
              </ul>
            </div>
            <div className="td-modal-footer">
              <button onClick={handleAddPlan} className="td-add-plan-button">
                <img src={addIcon} alt="추가 아이콘" className="add-icon" />
                일정 추가
              </button>
              <div className="td-save-cancel-group">
                <button className="td-modal-save-button">저장</button>
                <button onClick={closeModal} className="td-modal-cancel-button">취소</button>
              </div>
            </div>
          </div>
        </div>
      )}

      {isAddPlanModalOpen && (
        <PlannerModal
          isOpen={isAddPlanModalOpen}
          mode="add"
          title="새 일정 추가"
          onClose={closeAddPlanModal}
          onSave={(newPlan) => {
            PlannerData.push(newPlan);
            closeAddPlanModal();
          }}
        />
      )}

      <section className="similar-destinations">
        <h2 className='similar-place'>
          <span className="highlight">{card.name}</span>와(과) 비슷한 여행지 {/* name으로 수정 */}
        </h2>
        <TravelCarousel cards={similarCards} />
      </section>
    </div>
  );
};

export default TravelDetails;
