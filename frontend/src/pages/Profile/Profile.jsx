import { useState } from "react";
import "./Profile.css";
import ProfileInfo from "../../components/ProfileInfo/ProfileInfo";
import progressbar from "../../assets/progressbar1.png";
import Button from "../../components/Button/Button";
import { useNavigate } from "react-router-dom";
import UserForm from "../../components/UserForm/UserForm";

const Profile = () => {
  const navigate = useNavigate();
  const [input, setInput] = useState({
    name: "",
    birth: "",
    gender: "",
  });
  
  // 중복 검사 결과 상태를 Profile에서 관리
  const [isNameValid, setIsNameValid] = useState(null);

  const onChange = (e) => {
    setInput({
      ...input,
      [e.target.name]: e.target.value,
    });
  };

  const handleNextClick = () => {
    if (isNameValid === false) {
      alert("닉네임이 중복되었습니다. 다른 닉네임을 입력하세요.");
      return; // 중복된 닉네임일 때 페이지 이동을 막음
    }

    if (isNameValid === null) {
      alert("닉네임 중복 검사를 해주세요.");
      return; // 중복 검사 결과가 없으면 페이지 이동을 막음
    }

    // 닉네임이 유효하면 다음 페이지로 이동
    navigate(`/signup/livinginfo`);
  };

  return (
    <section className="profile-page">
      <ProfileInfo text={"회원가입을 위해 프로필 정보를 입력해주세요."} src={progressbar} />

      <section className="profile-body">
        {/* UserForm에 isNameValid와 setIsNameValid를 props로 전달 */}
        <UserForm input={input} onChange={onChange} setIsNameValid={setIsNameValid} />

        <section className="profile-btn">
          <Button text={"다음"} onClick={handleNextClick} />
        </section>
      </section>
    </section>
  );
};

export default Profile;
