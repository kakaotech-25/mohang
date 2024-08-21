import { useState } from "react";
import "./Profile.css";
import ProfileInfo from "../../components/ProfileInfo/ProfileInfo";
import progressbar from "../../assets/progressbar1.png";
import profileimg1 from "../../assets/profileimg1.png";
import profileimg2 from "../../assets/profileimg2.png";
import profileimg3 from "../../assets/profileimg3.png";
import CheckImg from "../../components/CheckImg/CheckImg";
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

  const [selectedImageIndex, setSelectedImageIndex] = useState(0);

  const handleImageClick = (index) => {
    setSelectedImageIndex(index);
  };

  const images = [
    profileimg1,
    profileimg2,
    profileimg3,
  ];

  return (
    <section className="profile-page">
      <ProfileInfo text={"회원가입을 위해 프로필 정보를 입력해주세요."} src={progressbar} />

      <section className="profile-body">
        <section className="profile-imgs">
          {images.map((src, index) => (
            <CheckImg
              key={index}
              src={src}
              isSelected={selectedImageIndex === index}
              onClick={() => handleImageClick(index)}
            />
          ))}
        </section>

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
