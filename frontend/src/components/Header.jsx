import logo from '../assets/logo.png'
import "./Header.css"
import {Link} from 'react-router-dom'

const Header = () => {
  return (
    <section className='header'>
      <Link to ='/'>
        <img src={logo} className='logo-img' />
      </Link>
      <div className='moheng'>모행</div>

      <div className='login'>로그인</div>

    </section>
  );
};
  
export default Header;