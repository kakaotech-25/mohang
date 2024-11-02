import { useState, useEffect } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import PlannerModal from '../../components/PlannerModal/PlannerModal';
import addIcon from '../../assets/editicon.png';
import seemoreIcon from '../../assets/seemore.png';
import axiosInstance from '../Login/axiosInstance';
import './Planner.css';

const Planner = () => {
  const [activeDropdown, setActiveDropdown] = useState(null);
  const [sortCriteria, setSortCriteria] = useState('newest'); // 기본 정렬 기준은 최신순
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedPlan, setSelectedPlan] = useState(null);
  const [modalMode, setModalMode] = useState('add'); // 기본 모드는 추가 모드
  const [plans, setPlans] = useState([]); // 플래너 데이터 저장
  const [loading, setLoading] = useState(true); // 로딩 상태
  const navigate = useNavigate();

  useEffect(() => {
    fetchPlans('newest'); // 페이지 로드 시 최신순 데이터를 불러옴
  }, []);

  const fetchPlans = async (criteria) => {
    setLoading(true);

    if (criteria === 'newest') return getPlans('/planner/recent');
    if (criteria === 'name') return getPlans('/planner/name');
    if (criteria === 'date') return getPlans('/planner/date');

    console.error('Unknown sort criteria:', criteria);
    setLoading(false);
  };

  const getPlans = async (url) => {
    try {
      const response = await axiosInstance.get(url);
      setPlans(response.data.tripScheduleResponses);
    } catch (error) {
      console.error('플래너 데이터를 불러오는 중 오류 발생:', error);
    } finally {
      setLoading(false);
    }
  };

  const toggleDropdown = (id) => {
    setActiveDropdown(activeDropdown === id ? null : id);
  };

  const handleEdit = (id) => {
    const planToEdit = plans.find((plan) => plan.scheduleId === id);
    setSelectedPlan(planToEdit);
    setModalMode('edit');
    setIsModalOpen(true);
  };

  const handleAdd = () => {
    setSelectedPlan(null);
    setModalMode('add');
    setIsModalOpen(true);
  };

  const handleDelete = async (id) => {
    try {
      const response = await axiosInstance.delete(`/planner/schedule/${id}`);
      if (response.status === 204) {
        console.log(`일정 삭제 성공: ${id}`);
        fetchPlans(sortCriteria);
        return;
      }
    } catch (error) {
      if (error.response && error.response.status === 404) {
        console.error('삭제 중 오류 발생: ', error.response.data.message);
        return;
      }
      console.error('일정 삭제 중 오류 발생:', error);
    }
  };

  const handleSave = async (newPlan) => {
    if (!newPlan) return;

    const payload = {
      scheduleName: newPlan.title,
      startDate: newPlan.startTime,
      endDate: newPlan.endTime,
    };

    try {
      if (modalMode === 'add') {
        const response = await axiosInstance.post('/schedule', payload);
        if (response.status === 204 || response.status === 201) {
          console.log('새 일정 추가 성공');
          fetchPlans(sortCriteria);
          return;
        }
      }

      if (modalMode === 'edit') {
        const response = await axiosInstance.put('/planner/schedule', {
          ...payload,
          scheduleId: selectedPlan.scheduleId,
        });
        if (response.status === 204) {
          console.log('일정 수정 성공');
          fetchPlans(sortCriteria);
          return;
        }
      }
    } catch (error) {
      console.error('일정 저장 중 오류 발생:', error);
    } finally {
      setIsModalOpen(false);
    }
  };

  const handleSort = (criteria) => {
    setSortCriteria(criteria);
    fetchPlans(criteria);
  };

  const handleItemClick = (id) => {
    if (activeDropdown !== id) {
      navigate(`/plannerdetails/${id}`);
    }
  };

  if (loading) {
    return <div>로딩 중...</div>;
  }

  return (
    <div className="planner-page">
      <h1 className="planner-title">여행 플래너</h1>
      <div className="header-controls">
        <div className="sort-options">
          <span
            className={`sort-option ${sortCriteria === 'newest' ? 'active' : ''}`}
            onClick={() => handleSort('newest')}
          >
            최신순
          </span>
          <span className="sort-divider">|</span>
          <span
            className={`sort-option ${sortCriteria === 'name' ? 'active' : ''}`}
            onClick={() => handleSort('name')}
          >
            이름순
          </span>
          <span className="sort-divider">|</span>
          <span
            className={`sort-option ${sortCriteria === 'date' ? 'active' : ''}`}
            onClick={() => handleSort('date')}
          >
            날짜순
          </span>
        </div>
        <button onClick={handleAdd} className="add-plan-button">
          <img src={addIcon} alt="추가 아이콘" className="add-icon" />
          일정 추가
        </button>
      </div>
      <div className="planner-list">
        {plans.map((plan) => (
          <div
            key={plan.scheduleId}
            className="planner-item"
            onClick={() => handleItemClick(plan.scheduleId)}
          >
            <div className="planner-info">
              <Link
                to={`/plannerdetails/${plan.scheduleId}`}
                className="planner-link"
                onClick={(e) => e.stopPropagation()}
              >
                <h2 className="planner-title-text">{plan.scheduleName}</h2>
              </Link>
              <div className="planner-period">{`${plan.startTime} ~ ${plan.endTime}`}</div>
            </div>
            <div
              className="planner-actions"
              onClick={(e) => e.stopPropagation()}
            >
              <img
                src={seemoreIcon}
                alt="더보기"
                onClick={() => toggleDropdown(plan.scheduleId)}
                className="seemore-icon"
              />
              {activeDropdown === plan.scheduleId && (
                <div className="dropdown-menu">
                  <button onClick={() => handleEdit(plan.scheduleId)}>수정하기</button>
                  <button onClick={() => handleDelete(plan.scheduleId)}>삭제하기</button>
                </div>
              )}
            </div>
          </div>
        ))}
      </div>

      <PlannerModal
        isOpen={isModalOpen}
        mode={modalMode}
        title={modalMode === 'edit' ? "일정 수정하기" : "일정 추가하기"}
        plan={selectedPlan}
        onClose={() => setIsModalOpen(false)}
        onSave={handleSave}
      />
    </div>
  );
};

export default Planner;
