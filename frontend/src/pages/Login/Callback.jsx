import React, { useEffect } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import axiosInstance from './axiosInstance';

const Callback = () => {
  const location = useLocation();
  const navigate = useNavigate();

  useEffect(() => {
    const fetchTokens = async () => {
      const code = new URLSearchParams(location.search).get('code');

      if (code) {
        try {
          // 카카오 로그인 인증 코드를 백엔드로 전송하여 인증 처리
          const response = await axiosInstance.post("/auth/KAKAO/login", { code });

          // 백엔드에서 받은 엑세스 토큰을 저장
          const accessToken = response.data.accessToken;
          if (accessToken) {
            localStorage.setItem('accessToken', accessToken);
          }

          // 백엔드에서 Set-Cookie 헤더로 리프레시 토큰을 보내는 경우, 이를 처리
          const cookies = response.headers['set-cookie'];
          if (cookies) {
            const refreshToken = cookies.find(cookie => cookie.startsWith('refresh-token'))?.split(';')[0]?.split('=')[1];
            if (refreshToken) {
              localStorage.setItem('refreshToken', refreshToken);
            }
          }

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
