import React, { useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axios from 'axios';

const Callback = () => {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchTokens = async () => {
      const code = new URLSearchParams(location.search).get('code');

      if (code) {
        try {
          // 카카오 로그인 인증 코드를 백엔드에 전송하여 인증 처리
          await axios.post("http://localhost:8080/api/auth/KAKAO/login", { "code": code });
          
          navigate('/'); 
        } catch (error) {
          console.error('Failed to login with Kakao:', error);
          navigate('/login?error=oauth');
        }
      } else {
        console.error('Authorization code is missing');
        navigate('/login?error=code_missing');
      }
    };

    fetchTokens();
  }, [location, navigate]);

  return (
    <div>
      <p>로그인 처리 중입니다. 잠시만 기다려주세요...</p>
    </div>
  );
};

export default Callback;
