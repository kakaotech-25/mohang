import { useState } from 'react';
import PlannerData from '../../data/PlannerData';
import seemoreIcon from '../../assets/seemore.png';
import PlannerModal from '../../components/PlannerModal/PlannerModal';
import './Planner.css';

const Planner = () => {
  const [activeDropdown, setActiveDropdown] = useState(null);
  const [sortCriteria, setSortCriteria] = useState('newest'); // 기본 정렬 기준은 최신순
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedPlan, setSelectedPlan] = useState(null);

  const toggleDropdown = (id) => {
    if (activeDropdown === id) {
      setActiveDropdown(null);
    } else {
      setActiveDropdown(id);
    }
  };

  const handleEdit = (id) => {
    const planToEdit = PlannerData.find(plan => plan.id === id);
    setSelectedPlan(planToEdit);
    setIsModalOpen(true);
  };

  const handleDelete = (id) => {
    // 삭제하기 로직
    console.log(`삭제하기 클릭: ${id}`);
  };

  const handleSave = (updatedPlan) => {
    // 이 부분은 백엔드와 연동하여 업데이트된 플래너 데이터를 저장하는 로직을 구현 필요
    console.log('저장된 플랜:', updatedPlan);
    setIsModalOpen(false);
  };

  const handleSort = (criteria) => {
    setSortCriteria(criteria);
  };

  const sortedPlans = [...PlannerData].sort((a, b) => {
    if (sortCriteria === 'newest') { // id를 기준으로 최신순 정렬 (연동전 임시로 id를 사용)
      return b.id - a.id; 
    } else if (sortCriteria === 'name') { // 이름순 정렬 (가나다순)
      return a.title.localeCompare(b.title); 
    } else if (sortCriteria === 'date') { // 날짜순 정렬 (여행기간이 가장 가까운순)
      const dateA = new Date(a.period.split(' ~ ')[0]);
      const dateB = new Date(b.period.split(' ~ ')[0]);
      return dateA - dateB; 
    }
    return 0;
  });

  return (
    <div className="planner-page">
      <h1 className="planner-title">여행 플래너</h1>
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
      <div className="planner-list">
        {sortedPlans.map((plan) => (
          <div key={plan.id} className="planner-item">
            <div className="planner-info">
              <h2 className="planner-title-text">{plan.title}</h2>
            </div>
            <div className="planner-period">{plan.period}</div>
            <div className="planner-actions">
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
        title="일정 수정하기"
        plan={selectedPlan}
        onClose={() => setIsModalOpen(false)}
        onSave={handleSave}
      />
    </div>
  );
};

export default Planner;
