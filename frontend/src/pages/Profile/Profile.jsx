import {useState} from "react";
import "./Profile.css"
import ProfileInfo from "../../components/ProfileInfo";
import progressbar from "../../assets/progressbar1.png";
import profileimg1 from "../../assets/profileimg1.png"
import profileimg2 from "../../assets/profileimg2.png"
import profileimg3 from "../../assets/profileimg3.png"
import Radio from "../../components/Radio"
import Button from "../../components/Button"
import { useNavigate } from "react-router-dom";
import CheckImg from "../../components/CheckImg";

const Profile = () => {
  const navigate = useNavigate();
  const [input, setInput] = useState({
    name: "",
  });

  const onChange = (e) => {
    console.log(e.target.name + " : " + e.target.value);
    setInput({
      ...input,
      [e.target.name]:e.target.value,
    });
  };

  const [selectedImageIndex, setSelectedImageIndex] = useState(0);

  const handleImageClick = (index) => {
    setSelectedImageIndex(index);
  };

  const images = [
    profileimg1,
    profileimg2,
    profileimg3
  ];

  return (
    <section className="profile-page">
      <ProfileInfo text={"회원가입을 위해 프로필 정보를 입력해주세요."} />
      <section className="progressbar">
        <img src={progressbar} className='progressbar' />
      </section>
      
      <section className='profile-body'>
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

        <section className="nickname">
          <div>닉네임 </div>
          <input 
            name = "name"
            value = {input.name}
            onChange={onChange}
            placeholder="닉네임을 입력하세요."
          />
          <Button text={"중복 검사"} />
        </section>

        <section className="birth">
          <div>생년월일</div>
          <input
            name = "birth"
            value = {input.birth}
            onChange={onChange}
            type = "date"
          />
        </section>

        <section className="gender">
          <div>성별</div>
            <Radio name="contact" value="MAN" defaultChecked>
              남성
            </Radio>
            <Radio name="contact" value="WOMAN">
              여성
            </Radio>
        </section>

        <section className="subbtn">
          <Button text={"다음"} onClick={()=>{
            navigate(`/signup/livinginfo`)
          }} />
        </section>

        


      </section>

    </section>
  );
};

export default Profile;