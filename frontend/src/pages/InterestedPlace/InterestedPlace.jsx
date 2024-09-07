import "./InterestedPlace.css";
import ProfileInfo from "../../components/ProfileInfo/ProfileInfo";
import progressbar3 from "../../assets/progressbar3.png";
import TravelCard from "../../components/TravelCard/TravelCard";
import Button from "../../components/Button/Button";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import axiosInstance from "../Login/axiosInstance";

const InterestedPlace = () => {
  const navigate = useNavigate();
  const [locations, setLocations] = useState([]);
  const [selectedCards, setSelectedCards] = useState([]); // 선택된 여행지의 contentId 저장

  // API 호출로 여행지 데이터 불러오기
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axiosInstance.get("/trip/find/interested");
        const tripData = response.data.findTripResponses;
        // 데이터 매핑하여 state에 저장
        setLocations(
          tripData.map((location) => ({
            id: location.contentId,
            image: location.tripImageUrl,
            tags: location.keywords,
            title: location.name,
            location: location.placeName,
            description: location.description,
          }))
        );
      } catch (error) {
        console.error("데이터 가져오기 실패:", error);
      }
    };

    fetchData();
  }, []);

  // 여행지 선택 처리
  const handleCardClick = (id) => {
    setSelectedCards((prevSelected) => {
      if (prevSelected.includes(id)) {
        return prevSelected.filter((cardId) => cardId !== id); // 이미 선택된 카드 클릭 시 선택 해제
      } else {
        return [...prevSelected, id]; // 선택되지 않은 카드 클릭 시 선택 추가
      }
    });
  };

  // 완료 버튼 클릭 시 선택된 여행지 contentId를 POST 요청으로 전송
  const handleSubmit = async () => {
    const selectedCount = selectedCards.length;
    if (selectedCount < 5 || selectedCount > 11) {
      alert(
        selectedCount < 5
          ? `관심 여행지를 5개 이상 선택해주세요! (${selectedCount}/5)`
          : `관심 여행지는 최대 10개까지만 선택 가능합니다! (${selectedCount}/10)`
      );
      return;
    }

    try {
      const payload = {
        contentIds: selectedCards, // 선택된 여행지의 contentId 리스트 전송
      };

      const response = await axiosInstance.post("/member/signup/trip", payload);

      if (response.status === 204) {
        console.log("POST 요청 성공");
        navigate(`/`); // 성공 시 메인 페이지로 이동
      } else {
        console.error("응답 실패:", response);
      }
    } catch (error) {
      console.error("POST 요청 실패:", error);
    }
  };

  return (
    <section className="interested-page">
      <ProfileInfo text={"AI 맞춤 추천을 위해 관심 여행지를 5개 이상 선택해주세요."} src={progressbar3} />

      <section className="interested-body">
        <section className="travel-grid">
          {locations.map((location) => (
            <TravelCard
              key={location.id}
              image={location.image}
              tags={location.tags}
              title={location.title}
              location={location.location}
              isSelected={selectedCards.includes(location.id)} // 선택 상태 전달
              onClick={() => handleCardClick(location.id)} // 클릭 핸들러 전달
            />
          ))}
        </section>
        <section className="subbtn">
          <Button text={"완료"} onClick={handleSubmit} />
        </section>
      </section>
    </section>
  );
};

export default InterestedPlace;
