import logo from '../assets/logo.png'
import "./Header.css"

const Header = () => {
  return (
    <section className='header'>
      <img src={logo} className='logoimg' />
      <div className='moheng'>모행</div>

      <div className='login'>로그인</div>

    </section>
  );
};
  
export default Header;