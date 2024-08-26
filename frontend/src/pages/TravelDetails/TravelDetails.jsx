import './TravelDetails.css'
import { useParams } from 'react-router-dom';
import TravelData from '../../data/TravelData';

const TravelDetails = () => {
  const { id } = useParams();
  const card = TravelData.find(item => item.id === parseInt(id));

  return (
    <div>
      <h1>{card.title}</h1>
      <img src={card.image} alt={card.title} />
      <p>위치: {card.location}</p>
      <div>
        {card.tags.map((tag, index) => (
          <span key={index}>#{tag} </span>
        ))}
      </div>
    </div>
  );
};

export default TravelDetails;
