import ProfileInfo from "../components/ProfileInfo";
import "./Profile.css"

const Profile = () => {
    return (
      <section className="profilepage">
        <ProfileInfo text={"회원가입을 위해 프로필 정보를 입력해주세요."} />
      </section>
    );
  };
  
  export default Profile;