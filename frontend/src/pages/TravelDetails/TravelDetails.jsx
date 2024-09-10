import './TravelDetails.css';
import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import axiosInstance from '../Login/axiosInstance';
import plannerIcon from '../../assets/plannericon.png';
import addIcon from '../../assets/editicon.png';
import TravelCarousel from '../../components/TravelCarousel/TravelCarousel';
import PlannerModal from '../../components/PlannerModal/PlannerModal';

const TravelDetails = () => {
  const { id } = useParams();

  // State variables for travel data and planner data
  const [card, setCard] = useState(null);
  const [plannerData, setPlannerData] = useState([]);
  const [similarCards, setSimilarCards] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isAddPlanModalOpen, setIsAddPlanModalOpen] = useState(false);
  const [loading, setLoading] = useState(true); // To handle loading state
  const [error, setError] = useState(null); // To handle error state

  // API URLs (Replace with your actual API endpoints)
  const travelDataAPI = '/trip/find';
  const plannerDataAPI = '/planner/name';

  useEffect(() => {
    // Fetch travel data and planner data on mount
    const fetchData = async () => {
      try {
        // Reset loading and error states
        setLoading(true);
        setError(null);

        // Get travel data and planner data simultaneously
        const [travelResponse, plannerResponse] = await Promise.all([
          axiosInstance.get(`${travelDataAPI}/${id}`), // Fetch specific travel data by ID
          axiosInstance.get(plannerDataAPI) // Fetch planner data
        ]);

        const travelItem = travelResponse.data;
        setCard(travelItem.findTripResponse);
        setSimilarCards(travelItem.similarTripResponses)
        setPlannerData(plannerResponse.data);

      } catch (error) {
        setError('데이터를 불러오는 중 오류가 발생했습니다.');
        console.log(error)
      } finally {
        setLoading(false); // Set loading to false when the data fetching is complete
      }
    };

    fetchData();
  }, [id]); // Runs when the component mounts or when `id` changes

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

  if (loading) {
    return <div>로딩 중...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  if (!card) {
    return <div>여행지를 찾을 수 없습니다.</div>;
  }

  return (
      <div className="travel-details-container">
        <div className="details-upper">
          <div className="travel-details-left">
            <div className="title-and-button">
              <div>
                <h1>{card.name}</h1>
                <p className="location">{card.placeName}</p>
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
              <img src={card.tripImageUrl} alt={card.name} className="travel-image" />
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
                    {plannerData.map((item) => (
                        <li key={item.scheduleId} className="td-planner-list-item">
                          <span className="td-planner-title">{item.scheduleName}</span>
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
                  setPlannerData([...plannerData, newPlan]);
                  closeAddPlanModal();
                }}
            />
        )}

        <section className="similar-destinations">
          <h2 className='similar-place'>
            <span className="highlight">{card.name}</span>와(과) 비슷한 여행지
          </h2>
          <TravelCarousel cards={similarCards} />
        </section>
      </div>
  );
};

export default TravelDetails;
