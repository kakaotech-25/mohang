import { useState } from 'react';
import TravelCarousel from '../../components/TravelCarousel/TravelCarousel';
import TravelData from '../../data/TravelData';
import './Home.css';

const Home = () => {
  const [filteredCards, setFilteredCards] = useState(TravelData);
  const [selectedKeywords, setSelectedKeywords] = useState([]);

  const uniqueKeywords = Array.from(new Set(TravelData.flatMap(card => card.tags)));

  const handleKeywordClick = (keyword) => {
    let updatedKeywords;
    if (selectedKeywords.includes(keyword)) {
      updatedKeywords = selectedKeywords.filter(k => k !== keyword);
    } else {
      updatedKeywords = [...selectedKeywords, keyword];
    }

    setSelectedKeywords(updatedKeywords);

    if (updatedKeywords.length === 0) {
      setFilteredCards(TravelData);
    } else {
      setFilteredCards(
        TravelData.filter(card => 
          updatedKeywords.every(keyword => card.tags.includes(keyword))
        )
      );
    }
  };

  return (
    <div>
      <section className='ai-recommendation'>
        <div className='carousel-description'>
          <span style={{ color: '#845EC2' }}>모행 AI</span>를 이용한 맞춤 여행지
        </div>
        <TravelCarousel cards={TravelData} />
      </section>

      <section className='keyword-recommendation'>
        <div className='carousel-description'>
          <span style={{ color: '#845EC2' }}>#키워드</span> 추천 여행지
        </div>

        <div className='keyword-buttons'>
          {uniqueKeywords.map((keyword, index) => (
            <button
              key={index}
              className={`keyword-button ${selectedKeywords.includes(keyword) ? 'selected' : ''}`}
              onClick={() => handleKeywordClick(keyword)}
            >
              #{keyword}
            </button>
          ))}
        </div>

        <TravelCarousel cards={filteredCards} />
      </section>
    </div>
  );
};

export default Home;
