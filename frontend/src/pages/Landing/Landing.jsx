import React, { useRef } from 'react';
import './Landing.css';
import MainImg from "../../assets/landing-img.png";

const Landing = () => {
  const secondSectionRef = useRef(null);

  const handleScroll = () => {
    secondSectionRef.current.scrollIntoView({ behavior: 'smooth' });
  };

  return (
    <div>
      <div className="landing-container">
        <div className="landing-content">
          <h1 className="landing-title">모두가 행복한 여행지로 안내합니다.</h1>
          <button className="landing-button" onClick={handleScroll}>
            데모 영상 보기
          </button>
        </div>
      </div>
      <div ref={secondSectionRef} className="second-section">
        <h2>영상 공간</h2>
      </div>
    </div>
  );
};

export default Landing;
