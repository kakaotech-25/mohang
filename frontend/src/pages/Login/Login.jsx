import "./Login.css"
import Left from '../../assets/login-left.png'
import Right from '../../assets/login-right.png'
import KakaoBtn from '../../assets/kakao-login.png'


const Login = () => {
    return (
      <section className="login-page">
        <div className="login-left">
          <img src={Left}/>
        </div>
        <div className="kakao-login">
          <img src={KakaoBtn} />
        </div>
        <div className="login-right">
          <img src={Right} />
        </div>

      </section>
    );
  };
  
  export default Login;