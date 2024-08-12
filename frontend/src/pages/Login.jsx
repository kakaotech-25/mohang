import "./Login.css"
import Left1 from '../assets/login-left-1.png'
import Left2 from '../assets/login-left-2.png'
import Left3 from '../assets/login-left-3.png'
import Right1 from '../assets/login-right-1.png'
import Right2 from '../assets/login-right-2.png'
import Right3 from '../assets/login-right-3.png'


const Login = () => {
    return (
      <section className="loginpage">
        <div className="login_left">
          <img src={Left3} className='left3img' />
          <img src={Left2} className='left2img' />
          <img src={Left1} className='left1img' />
        </div>
        <div className="kakaologin">

        </div>
        <div className="login_right">
          <img src={Right1} className='right1img' />
          <img src={Right2} className='right2img' />
          <img src={Right3} className='right3img' />
        </div>

      </section>
    );
  };
  
  export default Login;