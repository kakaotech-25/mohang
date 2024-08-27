import './TravelDetails.css';
import { useParams } from 'react-router-dom';
import TravelData from '../../data/TravelData';

const TravelDetails = () => {
  const { id } = useParams();
  const card = TravelData.find(item => item.id === parseInt(id));

  return (
    <div className="travel-details-container">
      <div className="travel-details-left">
        <h1>{card.title}</h1>
        <p className="location">{card.location}</p>
        <div className="tags">
          {card.tags.map((tag, index) => (
            <span key={index} className="tag">#{tag} </span>
          ))}
        </div>
        <p className="description">{card.description}</p>
      </div>
      <div className="travel-details-right">
        <img src={card.image} alt={card.title} className="travel-image" />
      </div>
    </div>
  );
};

export default TravelDetails;
