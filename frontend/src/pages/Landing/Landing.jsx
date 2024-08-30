import './Landing.css'
import MainImg from "../../assets/landing-img.png";

const Landing = () => {
    return (
      <div className="landing-container">
        <img src={MainImg} alt="Landing" className="landing-image" />
      </div>
    );
  };
  
  export default Landing;