import { useParams } from 'react-router-dom';
import PlannerData from '../../data/PlannerData';

const PlannerDetails = () => {
  const { id } = useParams();
  const plan = PlannerData.find((p) => p.id === parseInt(id));

  return (
    <div className="planner-details">
      <h1>{plan.title}</h1>
      <p>기간: {plan.period}</p>
      <h2>여행지:</h2>
      <ul>
        {plan.destinations.map((destination, index) => (
          <li key={index}>{destination}</li>
        ))}
      </ul>
    </div>
  );
};

export default PlannerDetails;
