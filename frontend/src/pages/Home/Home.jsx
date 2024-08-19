import {useState} from 'react';
import TravelCarousel from '../../components/TravelCarousel/TravelCarousel';
import travelcardimg from "../../assets/travelcard.png";
import './Home.css'

const data = [
  { id: 1, image: travelcardimg, tags: ["20대", "연인"], title: "롯데월드", location: "서울특별시 송파구" },
  { id: 2, image: travelcardimg, tags: ["가족", "아이"], title: "에버랜드", location: "경기도 용인시" },
  { id: 3, image: travelcardimg, tags: ["자연", "힐링"], title: "남이섬", location: "강원도 춘천시" },
  { id: 4, image: travelcardimg, tags: ["역사", "문화"], title: "경복궁", location: "서울특별시 종로구" },
  { id: 5, image: travelcardimg, tags: ["해변", "휴양"], title: "해운대", location: "부산광역시" },
  { id: 6, image: travelcardimg, tags: ["쇼핑", "문화"], title: "명동", location: "서울특별시 중구" },
  { id: 7, image: travelcardimg, tags: ["공원", "자연"], title: "올림픽공원", location: "서울특별시 송파구" },
  { id: 8, image: travelcardimg, tags: ["문화", "예술"], title: "인사동", location: "서울특별시 종로구" },
  { id: 9, image: travelcardimg, tags: ["휴양", "해변"], title: "제주도", location: "제주특별자치도" },
  { id: 10, image: travelcardimg, tags: ["자연", "등산"], title: "북한산", location: "서울특별시 은평구" },
  { id: 11, image: travelcardimg, tags: ["연인", "데이트"], title: "청계천", location: "서울특별시 중구" },
  { id: 12, image: travelcardimg, tags: ["역사", "문화"], title: "창덕궁", location: "서울특별시 종로구" },
  { id: 13, image: travelcardimg, tags: ["자연", "관광"], title: "동해안", location: "강원도 동해시" },
  { id: 14, image: travelcardimg, tags: ["산책", "데이트"], title: "한강공원", location: "서울특별시 마포구" },
  { id: 15, image: travelcardimg, tags: ["전통", "역사"], title: "안동하회마을", location: "경상북도 안동시" },
  { id: 16, image: travelcardimg, tags: ["공원", "자연"], title: "서울숲", location: "서울특별시 성동구" },
  { id: 17, image: travelcardimg, tags: ["문화", "예술"], title: "부산영화의전당", location: "부산광역시 해운대구" },
  { id: 18, image: travelcardimg, tags: ["해변", "휴양"], title: "광안리", location: "부산광역시 수영구" },
  { id: 19, image: travelcardimg, tags: ["산책", "자연"], title: "양재천", location: "서울특별시 서초구" },
  { id: 20, image: travelcardimg, tags: ["휴양", "온천"], title: "온천지구", location: "부산광역시 동래구" },
  { id: 21, image: travelcardimg, tags: ["공원", "역사"], title: "서울올림픽공원", location: "서울특별시 송파구" },
  { id: 22, image: travelcardimg, tags: ["산책", "데이트"], title: "석촌호수", location: "서울특별시 송파구" },
  { id: 23, image: travelcardimg, tags: ["바다", "관광"], title: "통영", location: "경상남도 통영시" },
  { id: 24, image: travelcardimg, tags: ["쇼핑", "문화"], title: "가로수길", location: "서울특별시 강남구" },
  { id: 25, image: travelcardimg, tags: ["자연", "힐링"], title: "속초", location: "강원도 속초시" },
  { id: 26, image: travelcardimg, tags: ["전통", "문화"], title: "전주한옥마을", location: "전라북도 전주시" },
  { id: 27, image: travelcardimg, tags: ["해변", "휴양"], title: "망상해변", location: "강원도 동해시" },
  { id: 28, image: travelcardimg, tags: ["산책", "공원"], title: "북서울꿈의숲", location: "서울특별시 강북구" },
  { id: 29, image: travelcardimg, tags: ["힐링", "온천"], title: "설악산온천", location: "강원도 속초시" },
  { id: 30, image: travelcardimg, tags: ["자연", "관광"], title: "설악산", location: "강원도 속초시" }
];

const Home = () => {
  const [filteredCards, setFilteredCards] = useState(data);
  const [selectedKeywords, setSelectedKeywords] = useState([]);

  // 중복 제거한 모든 키워드
  const uniqueKeywords = Array.from(new Set(data.flatMap(card => card.tags)));

  // 키워드 클릭 핸들러
  const handleKeywordClick = (keyword) => {
    let updatedKeywords;
    if (selectedKeywords.includes(keyword)) {
      // 이미 선택된 키워드를 다시 클릭하면 선택 해제
      updatedKeywords = selectedKeywords.filter(k => k !== keyword);
    } else {
      // 선택된 키워드를 추가
      updatedKeywords = [...selectedKeywords, keyword];
    }

    setSelectedKeywords(updatedKeywords);

    if (updatedKeywords.length === 0) {
      setFilteredCards(data); // 모든 키워드 선택 해제 시 전체 카드 보여줌
    } else {
      // 선택된 키워드 중 하나라도 포함된 카드들을 필터링
      setFilteredCards(
        data.filter(card => 
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
        <TravelCarousel cards={data} />
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