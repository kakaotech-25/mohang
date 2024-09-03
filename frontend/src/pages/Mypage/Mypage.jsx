import { useState } from 'react';
import './Mypage.css';
import LivingBtn from '../../components/LivingBtn/LivingBtn';
import Button from '../../components/Button/Button';
import UserForm from "../../components/UserForm/UserForm";
import profileimg from "../../assets/profileimg.png";
import changebtn from "../../assets/mypage-change-img.png";

const Mypage = () => {
  const [activeTab, setActiveTab] = useState('tab1');
  const [profileImage, setProfileImage] = useState(profileimg);
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

  const handleTabClick = (tab) => {
    setActiveTab(tab);
  };

  const handleLivingSelectionChange = (selectedOptions) => {
    console.log("Selected options:", selectedOptions);
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onload = () => {
        setProfileImage(reader.result);
      };
      reader.readAsDataURL(file);
    }
  };

  return (
    <div className="my-page">
      <section className="tabs">
        <button
          className={activeTab === 'tab1' ? 'tab-button active' : 'tab-button'}
          onClick={() => handleTabClick('tab1')}
        >
          프로필
        </button>
        <button
          className={activeTab === 'tab2' ? 'tab-button active' : 'tab-button'}
          onClick={() => handleTabClick('tab2')}
        >
          생활
        </button>
      </section>

      <section className="tab-content">
        {activeTab === 'tab1' && (
          <div id="tab1">
            <section className="mypage-img">
              <img src={profileImage} className="user-img" />
              <label htmlFor="image-upload">
                <img src={changebtn} className="change-btn" />
              </label>
              <input
                id="image-upload"
                type="file"
                accept="image/*"
                onChange={handleImageChange}
                style={{ display: 'none' }}
              />
            </section>
            <UserForm input={input} onChange={onChange} />
            <section className="mypage-btn">
              <Button text={"저장"} />
            </section>
          </div>
        )}
        {activeTab === 'tab2' && (
          <div id="tab2" className='mypage-living'>
            <LivingBtn onChangeSelection={handleLivingSelectionChange} />
            <section>
              <Button text={"저장"} />
            </section>
          </div>
        )}
      </section>
    </div>
  );
}

export default Mypage;
