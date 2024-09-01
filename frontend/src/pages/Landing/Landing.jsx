import { useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import './Landing.css';

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
    </div>
  );
};

export default Landing
