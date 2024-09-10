import { useState, useEffect } from 'react';
import TravelCarousel from '../../components/TravelCarousel/TravelCarousel';
import TravelData from '../../data/TravelData';
import axiosInstance from '../Login/axiosInstance'; // axiosInstance를 불러옴
import './Home.css';

const Home = () => {
  const [filteredCards, setFilteredCards] = useState([]); // 필터링된 여행지 상태
  const [selectedKeywords, setSelectedKeywords] = useState([]); // 선택된 키워드 상태
  const [uniqueKeywords, setUniqueKeywords] = useState([]); // API로부터 받은 키워드 상태
  const [loading, setLoading] = useState(true); // 로딩 상태

  // 키워드 목록과 초기 여행지 데이터를 가져오는 함수
  useEffect(() => {
    const fetchInitialData = async () => {
      try {
        // 키워드 목록을 가져옴
        const keywordResponse = await axiosInstance.get('/keyword');
        const keywords = keywordResponse.data.findAllKeywordResponses.map(keyword => ({
          id: keyword.keywordId,
          name: keyword.name
        }));
        setUniqueKeywords(keywords);

        // 초기에는 빈 배열로 여행지 데이터를 요청 (모든 추천 여행지 불러오기)
        const tripResponse = await axiosInstance.post('/keyword/trip/recommend', { keywordIds: [] });
        setFilteredCards(tripResponse.data.findTripResponses);

        setLoading(false); // 로딩 완료
      } catch (error) {
        console.error("Failed to fetch initial data:", error);
        setLoading(false);
      }
    };

    fetchInitialData(); // 컴포넌트 마운트 시 초기 데이터 가져오기
  }, []);

  // 키워드 버튼 클릭 시 필터링 처리
  const handleKeywordClick = async (keyword) => {
    let updatedKeywords;
    if (selectedKeywords.includes(keyword.id)) {
      updatedKeywords = selectedKeywords.filter(k => k !== keyword.id);
    } else {
      updatedKeywords = [...selectedKeywords, keyword.id];
    }

    setSelectedKeywords(updatedKeywords);

    try {
      // 선택된 키워드를 기반으로 여행지 필터링
      const response = await axiosInstance.post('/keyword/trip/recommend', {
        keywordIds: updatedKeywords
      });
      setFilteredCards(response.data.findTripResponses); // 필터링된 여행지 데이터 저장
    } catch (error) {
      console.error("Failed to fetch filtered trips:", error);
      setFilteredCards([]); // 에러 발생 시 빈 배열로 초기화
    }
  };

  return (
    <div>
      {/* 위의 AI 추천 캐러셀 (변동 없음) */}
      <section className='ai-recommendation'>
        <div className='carousel-description'>
          <span style={{ color: '#845EC2' }}>모행 AI</span>를 이용한 맞춤 여행지
        </div>
        <TravelCarousel cards={TravelData} />
      </section>

      {/* 아래의 키워드 필터링 캐러셀 */}
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
                  className={`keyword-button ${selectedKeywords.includes(keyword.id) ? 'selected' : ''}`}
                  onClick={() => handleKeywordClick(keyword)}
                >
                  #{keyword.name}
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
