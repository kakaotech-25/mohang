import { useState } from 'react';
import './TravelCard.css';

const TravelCard = ({ image, tags, title, location }) => {
  const [isSelected, setIsSelected] = useState(false);

  const handleClick = () => {
    setIsSelected(!isSelected);
  };

  return (
    <div className={`travel-card ${isSelected ? 'selected' : ''}`} onClick={handleClick}>
      <img src={image} alt={title} className="travel-card-image" />
      {isSelected && <div className="interested-checkmark">✔️</div>}
      <div className="travel-card-content">
        <div className="travel-card-tags">
          {tags.map((tag, index) => (
            <span key={index} className="travel-card-tag">
              #{tag}
            </span>
          ))}
        </div>
        <div className="travel-card-title">{title}</div>
        <div className="travel-card-location">{location}</div>
      </div>
    </div>
  );
};

export default TravelCard;
