import logo from '../assets/logo.png'
import "./Header.css"

const Header = () => {
  return (
    <section className='header'>
      <section className='logo'>
        <img src={logo} className='logoimg' />
        <div>모행</div>
      </section>

    </section>
  );
};
  
export default Header;