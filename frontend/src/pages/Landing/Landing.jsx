import { useRef, useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./Landing.css";
import TravelCarousel from "../../components/TravelCarousel/TravelCarousel";
import axiosInstance from "../Login/axiosInstance"; // Axios 인스턴스 사용
// TravelData는 삭제

const Landing = () => {
  const secondSectionRef = useRef(null);
  const navigate = useNavigate();

  // 상태 관리: 키워드와 여행지 리스트
  const [keyword, setKeyword] = useState("");
  const [tripList, setTripList] = useState([]);
  const [videoUrl, setVideoUrl] = useState("");

  // 컴포넌트가 마운트될 때 API 호출
  useEffect(() => {
    const fetchTripData = async () => {
      try {
        const response = await axiosInstance.get("/keyword/random/trip");
        setKeyword(response.data.keywordName); // 키워드 설정
        setTripList(response.data.findTripResponses); // 여행지 리스트 설정
      } catch (error) {
        console.error("랜딩 페이지 데이터를 불러오는 중 오류 발생:", error);
      }
    };

    const fetchExampleVideo = async () => {
      try {
        // 비디오 URL을 서버에서 불러옴
        const response = await axiosInstance.get("/video/moheng.mp4", {
          responseType: "blob",
        });
        const videoBlob = URL.createObjectURL(response.data);
        setVideoUrl(videoBlob); // 비디오 URL을 상태로 설정
      } catch (error) {
        console.error("비디오 데이터를 불러오는 중 오류 발생:", error);
      }
    };

    fetchExampleVideo();
    fetchTripData();
  }, []);

  const handleScroll = () => {
    secondSectionRef.current.scrollIntoView({ behavior: "smooth" });
  };

  const handleLoginRedirect = () => {
    navigate("/login");
  };

  return (
    <div>
      <div className="landing-container">
        <div className="landing-content">
          <p className="small-title">Make your dream trip a reality</p>
          <p className="large-title">모두가 행복한</p>
          <p className="large-title">여행지로</p>
          <p className="large-title">안내합니다.</p>
          <div className="button-group">
            <button className="landing-button" onClick={handleLoginRedirect}>
              여행지 추천받기
            </button>
            <button className="landing-button" onClick={handleScroll}>
              데모 영상 보기
            </button>
          </div>
        </div>
      </div>
      {videoUrl && (
        <video controls width="100%">
          <source
            src={videoUrl}
            type="video/mp4"
            alt="사이트 사용법 설명 데모 영상"
          />
          Your browser does not support the video tag.
        </video>
      )}

      <section className="keyword-recommendation">
        <div className="carousel-description">
          {/* keyword 상태를 사용하여 키워드 표시 */}
          <span className="keyword-highlight">#{keyword}</span> 키워드로
          추천하는 여행지
        </div>
        {/* TravelCarousel에 tripList 데이터 전달 */}
        <TravelCarousel cards={tripList} />

        <div className="login-prompt">
          <p>로그인하고 여행지를 추천받아보세요!</p>
          <button className="login-button" onClick={handleLoginRedirect}>
            로그인
          </button>
        </div>
      </section>
    </div>
  );
};

export default Landing;
