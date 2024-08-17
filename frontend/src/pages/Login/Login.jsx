import "./Login.css"
import Left from '../../assets/login-left.png'
import Right from '../../assets/login-right.png'
import KakaoBtn from '../../assets/kakao-login.png'
import axios from 'axios'
import {useEffect, useState} from "react";

// axios 적용을 위해 비동기 함수 선언
const Login = () => {
    const [kakaoLink, setKakaoLink] = useState("")

    // baseurl을 따로 설정해주는 것을 추천합니다.
    // https://blog.beomi.net/api-url-feat-react-and-axios
    useEffect(() => {
        async function fetchKakaoLink() {
            const link = await axios.get("http://localhost:8080/auth/KAKAO/link")
                .then((response) => response.data["oAuthUri"])
            setKakaoLink(link);
        }

        if (!kakaoLink) {
            fetchKakaoLink();
        }
    }, [])

    return (
      <section className="login-page">
        <div className="login-left">
          <img src={Left}/>
        </div>
        <div className="kakao-login">
          <a style={{width: "100px", height: "100px"}} href={kakaoLink}/>
          <img src={KakaoBtn} />
        </div>
        <div className="login-right">
          <img src={Right} />
        </div>

      </section>
    );
};
  
export default Login;