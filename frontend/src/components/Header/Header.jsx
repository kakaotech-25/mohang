import React, { useState, useEffect } from 'react';
import axiosInstance from '../../pages/Login/axiosInstance'; // axiosInstance를 import
import logo from '../../assets/logo.png';
import "./Header.css";
import { Link } from 'react-router-dom';
import profileimg from "../../assets/profileimg.png"; // 예시로 넣어 볼 프로필 이미지

const Header = () => {
  const [isLoggedIn, setIsLoggedIn] = useState(false); // 로그인 여부 상태
  const [isFirstLogin, setIsFirstLogin] = useState(false); // 최초 로그인 여부 상태
  const [profileImg, setProfileImg] = useState(profileimg); // 기본 프로필 이미지로 설정
  const [loading, setLoading] = useState(true); // 로딩 상태

  useEffect(() => {
    const fetchUserProfile = async () => {
      try {
        // API 호출
        const response = await axiosInstance.get('/member/authority/profile');
        const { authority, profileImageUrl } = response.data;

        if (authority === 'INIT_MEMBER') {
          setIsLoggedIn(true);
          setIsFirstLogin(true); // 최초 로그인 상태로 설정
        } else if (authority === 'REGULAR_MEMBER') {
          setIsLoggedIn(true);
          setIsFirstLogin(false); // 정규 회원 상태
          setProfileImg(profileImageUrl || profileimg); // 프로필 이미지 설정 (없으면 기본 이미지)
        }
      } catch (error) {
        if (error.response && error.response.status === 401) {
          setIsLoggedIn(false); // 비회원 상태
        } else {
          console.error('API 호출 중 오류 발생:', error);
        }
      } finally {
        setLoading(false); // 로딩 상태 종료
      }
    };

    fetchUserProfile();
  }, []);

  if (loading) {
    return null; // 로딩 중일 때는 아무것도 렌더링하지 않음
  }

  return (
    <section className='header'>
      <Link to='/'>
        <img src={logo} className='logo-img' alt="Logo" />
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
