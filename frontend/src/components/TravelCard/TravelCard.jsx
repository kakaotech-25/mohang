import './TravelCard.css';

const TravelCard = ({ image, tags = [], title, location, isSelected, onClick }) => {  // 기본값 추가
  const formatLocation = (location) => {
    const parts = location.split(' ');
    return parts.slice(0, 2).join(' '); // 주소에서 구까지만
  };

  return (
    <div className={`travel-card ${isSelected ? 'selected' : ''}`} onClick={onClick}>
      <img src={image} alt={title} className="travel-card-image" />
      {isSelected && <div className="interested-checkmark">✔️</div>}
      <div className="travel-card-content">
        <div className="travel-card-tags">
          {tags.slice(0, 2).map((tag, index) => ( // 태그 2개만
            <span key={index} className="travel-card-tag">
              #{tag}
            </span>
          ))}
        </div>
        <div className="travel-card-title">{title}</div>
        <div className="travel-card-location">{formatLocation(location)}</div>
      </div>
    </div>
  );
};

export default TravelCard;
