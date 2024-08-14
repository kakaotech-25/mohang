import {useState} from "react";
import "./Profile.css"
import ProfileInfo from "../components/ProfileInfo";
import ProgressBar from "../components/ProgressBar";
import profileimg1 from "../assets/profileimg1.png"
import profileimg2 from "../assets/profileimg2.png"
import profileimg3 from "../assets/profileimg3.png"
import Radio from "../components/Radio"
import Button from "../components/Button"

const Profile = () => {
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
          <Button text={"다음"} />
        </section>

        


      </section>

    </section>
  );
};

export default Profile;