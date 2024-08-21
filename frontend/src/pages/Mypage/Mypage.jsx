import { useState } from 'react';
import './Mypage.css';
import LivingBtn from '../../components/LivingBtn/LivingBtn';
import Button from '../../components/Button/Button';
import UserForm from "../../components/UserForm/UserForm";
import profileimg1 from "../../assets/profileimg1.png";

const Mypage = () => {
  const [activeTab, setActiveTab] = useState('tab1');

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
            {/* 프로필 이미지 추가 */}
            <section className="mypage-img">
              <img src={profileimg1} alt="프로필 이미지" className="user-img" />
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
