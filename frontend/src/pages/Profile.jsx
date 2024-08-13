import ProfileInfo from "../components/ProfileInfo";
import ProgressBar from "../components/ProgressBar";
import "./Profile.css"
import profileimg1 from "../assets/profileimg1.png"
import profileimg2 from "../assets/profileimg2.png"
import profileimg3 from "../assets/profileimg3.png"

const Profile = () => {
  return (
    <section className="profilepage">
      <ProfileInfo text={"회원가입을 위해 프로필 정보를 입력해주세요."} />
      <ProgressBar />
      <section className='profile1'>
        <section className="profileimgs">
          <img src={profileimg1} className='profleimg1' />
          <img src={profileimg2} className='profleimg2' />
          <img src={profileimg3} className='profleimg3' />
        </section>

      </section>

    </section>
  );
};

export default Profile;