import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import PlannerData from '../../data/PlannerData';
import seemoreIcon from '../../assets/seemore.png';
import PlannerModal from '../../components/PlannerModal/PlannerModal';
import addIcon from '../../assets/editicon.png';
import './Planner.css';

const Planner = () => {
  const [activeDropdown, setActiveDropdown] = useState(null);
  const [sortCriteria, setSortCriteria] = useState('newest'); // 기본 정렬 기준은 최신순
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedPlan, setSelectedPlan] = useState(null);
  const [modalMode, setModalMode] = useState('add'); // 기본 모드는 추가 모드
  const navigate = useNavigate();

  const toggleDropdown = (id) => {
    setActiveDropdown(activeDropdown === id ? null : id);
  };

  const handleEdit = (id) => {
    const planToEdit = PlannerData.find((plan) => plan.id === id);
    setSelectedPlan(planToEdit);
    setModalMode('edit');
    setIsModalOpen(true);
  };

  const handleAdd = () => {
    setSelectedPlan(null);
    setModalMode('add');
    setIsModalOpen(true);
  };

  const handleDelete = (id) => {
    // 삭제하기 로직
    console.log(`삭제하기 클릭: ${id}`);
  };

  const handleSave = (updatedPlan) => {
    if (modalMode === 'edit') {
      console.log('수정된 플랜:', updatedPlan);
    } else if (modalMode === 'add') {
      console.log('새로 추가된 플랜:', updatedPlan);
    }
    setIsModalOpen(false);
  };

  const handleSort = (criteria) => {
    setSortCriteria(criteria);
  };

  const sortedPlans = [...PlannerData].sort((a, b) => {
    if (sortCriteria === 'newest') { // id를 기준으로 최신순 정렬 (연동 전 임시로 id를 사용)
      return b.id - a.id;
    } else if (sortCriteria === 'name') { // 이름순 정렬 (가나다순)
      return a.title.localeCompare(b.title);
    } else if (sortCriteria === 'date') { // 날짜순 정렬 (여행기간이 가장 가까운 순)
      const dateA = new Date(a.period.split(' ~ ')[0]);
      const dateB = new Date(b.period.split(' ~ ')[0]);
      return dateA - dateB;
    }
    return 0;
  });

  const handleItemClick = (id) => {
    if (activeDropdown !== id) {
      navigate(`/plannerdetails/${id}`);
    }
  };

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
        {sortedPlans.map((plan) => (
          <div
            key={plan.id}
            className="planner-item"
            onClick={() => handleItemClick(plan.id)}
          >
            <div className="planner-info">
              <Link
                to={`/plannerdetails/${plan.id}`}
                className="planner-link"
                onClick={(e) => e.stopPropagation()} // 링크 클릭 시 이벤트 중단
              >
                <h2 className="planner-title-text">{plan.title}</h2>
              </Link>
              <div className="planner-period">{plan.period}</div>
            </div>
            <div
              className="planner-actions"
              onClick={(e) => e.stopPropagation()} // 드롭다운 클릭 시 이벤트 중단
            >
              <img
                src={seemoreIcon}
                alt="더보기"
                onClick={() => toggleDropdown(plan.id)}
                className="seemore-icon"
              />
              {activeDropdown === plan.id && (
                <div className="dropdown-menu">
                  <button onClick={() => handleEdit(plan.id)}>수정하기</button>
                  <button onClick={() => handleDelete(plan.id)}>삭제하기</button>
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
