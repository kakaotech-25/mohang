import { useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import './Landing.css';
import TravelCarousel from '../../components/TravelCarousel/TravelCarousel';
import TravelData from '../../data/TravelData';

const Landing = () => {
  const secondSectionRef = useRef(null);
  const navigate = useNavigate();

  const handleScroll = () => {
    secondSectionRef.current.scrollIntoView({ behavior: 'smooth' });
  };

  const handleLoginRedirect = () => {
    navigate('/login');
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
      <div ref={secondSectionRef} className="second-section">
        <h2>영상 공간</h2>
      </div>

      <section className='keyword-recommendation'>
        <div className='carousel-description'>
          <span className="keyword-highlight">#키워드</span>로 추천하는 여행지
        </div>
        <TravelCarousel cards={TravelData} />

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
