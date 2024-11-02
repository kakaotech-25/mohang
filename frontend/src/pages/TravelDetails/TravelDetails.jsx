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

  const [card, setCard] = useState(null);
  const [plannerData, setPlannerData] = useState([]);
  const [similarCards, setSimilarCards] = useState([]);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isAddPlanModalOpen, setIsAddPlanModalOpen] = useState(false);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedSchedules, setSelectedSchedules] = useState([]);

  const travelDataAPI = '/trip/find';
  const plannerDataAPI = '/planner/name';

  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true);
        setError(null);
        const [travelResponse, plannerResponse] = await Promise.all([
          axiosInstance.get(`${travelDataAPI}/${id}`),
          axiosInstance.get(plannerDataAPI)
        ]);

        const travelItem = travelResponse.data;
        setCard(travelItem.findTripResponse);
        setSimilarCards(travelItem.similarTripResponses.findTripResponses);
        setPlannerData(plannerResponse.data.tripScheduleResponses);
      } catch (error) {
        setError('데이터를 불러오는 중 오류가 발생했습니다.');
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id]);

  // 플래너에 담기 버튼 클릭 시 모달 열기
  const handleAddToPlanner = () => setIsModalOpen(true);
  const closeModal = () => setIsModalOpen(false);

  const handleAddPlan = () => setIsAddPlanModalOpen(true);
  const closeAddPlanModal = () => setIsAddPlanModalOpen(false);

  const handleCheckboxChange = (scheduleId, isChecked) => {
    if (isChecked) {
      setSelectedSchedules((prev) => [...prev, scheduleId]);
    } else {
      setSelectedSchedules((prev) => prev.filter((id) => id !== scheduleId));
    }
  };

  const handleSave = async () => {
    console.log("Saving schedules:", selectedSchedules); // 디버깅용 로그
    try {
      const response = await axiosInstance.post(`/schedule/trip/${card.tripId}`, {
        scheduleIds: selectedSchedules,
      });

      if (response && response.status === 204) {
        alert('여행지가 일정에 성공적으로 추가되었습니다.');
        closeModal();
      } else {
        alert('일정 추가에 실패했습니다.');
      }
    } catch (error) {
      console.error('Error saving schedules:', error);
      alert('일정 추가 중 오류가 발생했습니다.');
    }
  };

  // 새로운 일정 생성 핸들러
  const handleNewPlanSave = async (newPlan) => {
    try {
      const response = await axiosInstance.post('/schedule', {
        scheduleName: newPlan.title,
        startDate: newPlan.startTime,
        endDate: newPlan.endTime,
      });

      if (response.status === 204) {
        console.log("New schedule created successfully, fetching updated planner data...");

        // 일정이 생성되었으므로, 잠시 지연 후 데이터를 다시 불러옵니다.
        setTimeout(async () => {
          try {
            const updatedPlannerResponse = await axiosInstance.get(plannerDataAPI);
            const updatedPlannerData = updatedPlannerResponse.data.tripScheduleResponses;

            // 새로 추가된 일정 찾기 (단순화된 조건: 이름만으로 찾기)
            const newPlannerItem = updatedPlannerData.find(
              (item) => item.scheduleName === newPlan.title
            );

            if (newPlannerItem && newPlannerItem.scheduleId) {
              // plannerData에 새로운 일정 추가 및 selectedSchedules에 자동으로 추가
              setPlannerData(updatedPlannerData);
              setSelectedSchedules((prevSelected) => [...prevSelected, newPlannerItem.scheduleId]);

              console.log("New planner data added and selected:", newPlannerItem);
              closeAddPlanModal();
            } else {
              console.error("Failed to find the newly created schedule in the updated planner data.");
              alert("일정 생성 후 새로 생성된 일정을 찾지 못했습니다.");
            }
          } catch (fetchError) {
            console.error("Failed to fetch updated planner data:", fetchError);
            alert("일정 생성 후 일정을 다시 불러오는 중 오류가 발생했습니다.");
          }
        });

      } else {
        console.error("Failed to create a new schedule.");
        alert("일정 생성에 실패했습니다.");
      }
    } catch (error) {
      console.error('일정 생성 실패:', error);
      if (error.response && error.response.data && error.response.data.message) {
        alert(error.response.data.message); // 서버에서 전송된 오류 메시지 표시
      } else {
        alert('일정 생성 중 오류가 발생했습니다.');
      }
    }
  };

  useEffect(() => {
    // 모달을 열 때 선택한 일정 초기화
    if (isModalOpen && plannerData.length > 0) {
      setSelectedSchedules([]); 
    }
  }, [isModalOpen, plannerData]);

  // 선택된 체크박스 상태를 반영
  useEffect(() => {
    console.log("Selected schedules updated:", selectedSchedules); // 디버깅용 로그
  }, [selectedSchedules]);

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
                {plannerData.map((item, index) => (
                  <li key={index} className="td-planner-list-item">
                    {/* 일정명 출력 */}
                    <span className="td-planner-title">{item.scheduleName}</span>
                    <input
                      type="checkbox"
                      className="td-planner-checkbox"
                      onChange={(e) => handleCheckboxChange(item.scheduleId, e.target.checked)}
                      checked={selectedSchedules.includes(item.scheduleId)} // 선택된 상태 반영
                    />
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
                <button onClick={handleSave} className="td-modal-save-button">저장</button>
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
          onSave={handleNewPlanSave} // 새로운 일정 저장 핸들러
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
