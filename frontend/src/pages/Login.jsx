import "./Login.css"
import Left from '../assets/login-left.png'
import Right from '../assets/login-right.png'
import KakaoBtn from '../assets/kakao-login.png'


const Login = () => {
    return (
      <section className="loginpage">
        <div className="login_left">
          <img src={Left}/>
        </div>
        <div className="kakaologin">
          <img src={KakaoBtn} />
        </div>
        <div className="login_right">
          <img src={Right} />
        </div>

      </section>
    );
  };
  
  export default Login;