import { useState, useEffect } from 'react';
import TravelCarousel from '../../components/TravelCarousel/TravelCarousel';
import TravelData from '../../data/TravelData';
import axiosInstance from '../Login/axiosInstance'; // axiosInstance를 불러옴
import './Home.css';

const Home = () => {
  const [filteredCards, setFilteredCards] = useState(TravelData); // 필터링된 여행지 상태
  const [selectedKeywords, setSelectedKeywords] = useState([]); // 선택된 키워드 상태
  const [uniqueKeywords, setUniqueKeywords] = useState([]); // API로부터 받은 키워드 상태
  const [loading, setLoading] = useState(true); // 로딩 상태

  // 키워드 목록을 가져오는 함수
  useEffect(() => {
    const fetchKeywords = async () => {
      try {
        const response = await axiosInstance.get('/keyword');
        const keywords = response.data.findAllKeywordResponses.map(keyword => keyword.name);
        setUniqueKeywords(keywords); // API에서 가져온 키워드로 상태 설정
        setLoading(false); // 로딩 완료
      } catch (error) {
        console.error("Failed to fetch keywords:", error);
        setLoading(false);
      }
    };

    fetchKeywords(); // 컴포넌트 마운트 시 키워드 가져오기
  }, []);

  // 키워드 버튼 클릭 시 필터링 처리
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

        {/* 로딩 중일 때는 로딩 메시지를 표시 */}
        {loading ? (
          <p>로딩 중...</p>
        ) : (
          <>
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
          </>
        )}
      </section>
    </div>
  );
};

export default Home;
