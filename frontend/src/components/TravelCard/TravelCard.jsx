import './TravelCard.css';

const TravelCard = ({ image, tags, title, location }) => {
  return (
    <div className="travel-card">
      <img src={image} alt={title} className="travel-card-image" />
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
