import { useState } from 'react';
import PlannerData from '../../data/PlannerData';
import seemoreIcon from '../../assets/seemore.png';
import './Planner.css';

const Planner = () => {
  const [activeDropdown, setActiveDropdown] = useState(null);

  const toggleDropdown = (id) => {
    if (activeDropdown === id) {
      setActiveDropdown(null);
    } else {
      setActiveDropdown(id);
    }
  };

  const handleEdit = (id) => {
    // 수정하기 로직
    console.log(`수정하기 클릭: ${id}`);
  };

  const handleDelete = (id) => {
    // 삭제하기 로직
    console.log(`삭제하기 클릭: ${id}`);
  };

  return (
    <div className="planner-page">
      <div className="planner-header">
        <h1 className="planner-title">여행 플래너</h1>
      </div>
      <div className="planner-list">
        {PlannerData.map((plan) => (
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
    </div>
  );
};

export default Planner;
