import React, { useState, useEffect } from 'react';
import logo from '../../assets/logo.png';
import "./Header.css";
import { Link } from 'react-router-dom';
import profileimg from "../../assets/profileimg.png" //예시로 넣어 볼 프로필 이미지

const Header = () => {
  // 로그인 상태와 사용자 정보를 관리할 상태 변수
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [isFirstLogin, setIsFirstLogin] = useState(true); // 최초 로그인 여부
  const [profileImg, setProfileImg] = useState(null); // 프로필 이미지 경로

  useEffect(() => {
    // 1: 로그인 전
    setIsLoggedIn(false);
    setIsFirstLogin(true);

    // 2: 최초 로그인 (회원가입 후 프로필 설정 단계) /signup/profile , /signup/livinginfo, /signup/interestedplace 페이지에서의 헤더
    // setIsLoggedIn(true);
    // setIsFirstLogin(true);

    // 3: 로그인 후 (프로필 설정 완료)
    // setIsLoggedIn(true);
    // setIsFirstLogin(false);
    // setProfileImg(profileimg);
  }, []); // 빈 배열을 넣어 useEffect가 컴포넌트 마운트 시 한 번만 실행되도록 함

  return (
    <section className='header'>
      <Link to='/'>
        <img src={logo} className='logo-img' />
      </Link>
      <div className='moheng'>모행</div>

      {isLoggedIn ? (
        isFirstLogin ? null : (
          <div className="profile-menu">
            <img src={profileImg} className='header-profile' alt="Profile" />
            <div className="dropdown-content">
              <Link to="/mypage">마이페이지</Link>
              <Link to="/planner">플래너</Link>
              <div className="logout-menu-item">로그아웃</div>
            </div>
          </div>
        )
      ) : (
        <Link to='/login' className='login'>
          로그인
        </Link>
      )}
    </section>
  );
};

export default Header;
