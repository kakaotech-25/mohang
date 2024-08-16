import logo from '../../assets/logo.png'
import "./Footer.css"

const Footer = () => {
  return (
    <section className='footer'>

      <section className='main-contents'>
        <section>
          <img src={logo} className='logo-img' />
          <div>moheng</div>
        </section>
        <section className='content'>
          <div className='v-line'></div>
          <section className='title'>
            <div>이용약관</div>
            <div>개인정보처리방침</div>
          </section>
        </section>
        <section className='content'>
          <div className='v-line'></div>
          <section className='title'>
            <div>문의하기 moheng@kakao.com</div>
            <div>FAQ 자주묻는질문</div>
          </section>
        </section>
      </section>

      <section className='copyright'>
        Copyright © 2024 모행 - All rights reserved.
      </section>

    </section>
  );
};
  
export default Footer;