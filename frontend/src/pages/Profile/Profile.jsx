import { useState } from "react";
import "./Profile.css";
import ProfileInfo from "../../components/ProfileInfo/ProfileInfo";
import progressbar from "../../assets/progressbar1.png";
import Button from "../../components/Button/Button";
import { useNavigate } from "react-router-dom";
import UserForm from "../../components/UserForm/UserForm"

const Profile = () => {
  const navigate = useNavigate();
  const [input, setInput] = useState({
    name: "",
    birth: "",
  });

  const onChange = (e) => {
    setInput({
      ...input,
      [e.target.name]: e.target.value,
    });
  };



  return (
    <section className="profile-page">
      <ProfileInfo text={"회원가입을 위해 프로필 정보를 입력해주세요."} src={progressbar} />

      <section className="profile-body">
        <UserForm input={input} onChange={onChange} />

        <section className="profile-btn">
          <Button
            text={"다음"}
            onClick={() => {
              navigate(`/signup/livinginfo`);
            }}
          />
        </section>
      </section>
    </section>
  );
};

export default Profile;
