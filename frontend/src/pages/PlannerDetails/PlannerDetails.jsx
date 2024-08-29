import { useParams } from 'react-router-dom';
import PlannerData from '../../data/PlannerData';
import './PlannerDetails.css';

const PlannerDetails = () => {
  const { id } = useParams();
  const plan = PlannerData.find((p) => p.id === parseInt(id));

  return (
    <div className="planner-details-container">
      <div className="planner-details-list">
        <div className="planner-details-header">
          <h1 className="planner-details-title">{plan.title}</h1>
          <p className="planner-details-period">{plan.period}</p>
        </div>
        <ul className="planner-destinations-list">
          {plan.destinations.map((destination, index) => (
            <li key={index} className="planner-destination-item">
              <button className="hamburger-button">☰</button>
              <span className="destination-name">{destination}</span>
              <button className="delete-button">삭제</button>
            </li>
          ))}
        </ul>
      </div>
      <div className="planner-details-map">
        <p>지도 공간</p>
      </div>
    </div>
  );
};

export default PlannerDetails;
