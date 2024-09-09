import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import TravelCard from '../TravelCard/TravelCard';
import './TravelCarousel.css';

const TravelCarousel = ({ cards }) => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const cardsToShow = 5;
  const navigate = useNavigate();

  const handlePrevClick = () => {
    if (currentIndex === 0) {
      setCurrentIndex(cards.length - cardsToShow);
    } else {
      setCurrentIndex(currentIndex - 1);
    }
  };

  const handleNextClick = () => {
    if (currentIndex === cards.length - cardsToShow) {
      setCurrentIndex(0);
    } else {
      setCurrentIndex(currentIndex + 1);
    }
  };

  const handleCardClick = (contentId) => {  // contentId로 수정
    navigate(`/traveldetails/${contentId}`);
  };

  return (
    <div className="carousel-container">
      <button className="carousel-button prev" onClick={handlePrevClick}>
        ◀
      </button>
      <div className="carousel">
        <div
          className="carousel-track"
          style={{ transform: `translateX(-${currentIndex * (100 / cardsToShow)}%)` }}
        >
          {cards.map((card, index) => (
            <div key={index} className="carousel-card">
              <TravelCard
                image={card.tripImageUrl}
                tags={card.keywords || []}  // 기본값 설정
                title={card.name}
                location={card.placeName}
                isSelected={false}
                onClick={() => handleCardClick(card.contentId)}  // contentId로 수정
              />
            </div>
          ))}
        </div>
      </div>
      <button className="carousel-button next" onClick={handleNextClick}>
        ▶
      </button>
    </div>
  );
};

export default TravelCarousel;
