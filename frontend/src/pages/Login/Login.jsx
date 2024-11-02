import './Login.css';
import React, { useEffect, useState } from "react";
import axiosInstance from './axiosInstance';
import Left from '../../assets/login-left.png';
import Right from '../../assets/login-right.png';
import KakaoBtn from '../../assets/kakao-login.png';

const Login = () => {
  const [kakaoLink, setKakaoLink] = useState("");

  useEffect(() => {
    const fetchKakaoLink = async () => {
      try {
        const response = await axiosInstance.get("/auth/KAKAO/link");
        setKakaoLink(response.data.oAuthUri);
      } catch (error) {
        console.error("Failed to fetch Kakao login link:", error);
      }
    };

    fetchKakaoLink();
  }, []);

  return (
    <section className="login-page">
      <div className="login-left">
        <img src={Left} alt="Left" />
      </div>
      <div className="kakao-login">
        {kakaoLink ? (
          <a href={kakaoLink}>
            <img src={KakaoBtn} alt="Kakao Login" />
          </a>
        ) : (
          <p>Loading...</p>
        )}
      </div>
      <div className="login-right">
        <img src={Right} alt="Right" />
      </div>
    </section>
  );
};

export default Login;
