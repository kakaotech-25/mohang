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

  const formatLocation = (location) => {
    // 주소에서 구까지만 남기기 위한 정규식 사용
    const parts = location.split(' ');
    return parts.slice(0, 2).join(' ');
  };

  const formattedCards = TravelData.map(card => ({ // 위쪽 여행지캐러셀
    ...card,
    tags: card.tags.slice(0, 2), // 태그는 2개만 보이기
    location: formatLocation(card.location) // 주소도 구까지만 남기기
  }));

  const formattedFilteredCards = filteredCards.map(card => ({ //아래쪽 여행지캐러셀
    ...card,
    tags: card.tags.slice(0, 2),
    location: formatLocation(card.location)
  }));

  return (
    <div>
      <section className='ai-recommendation'>
        <div className='carousel-description'>
          <span style={{ color: '#845EC2' }}>모행 AI</span>를 이용한 맞춤 여행지
        </div>
        <TravelCarousel cards={formattedCards} />
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

        <TravelCarousel cards={formattedFilteredCards} />
      </section>
    </div>
  );
};

export default Home;
