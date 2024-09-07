import { useState, useEffect } from 'react';
import './Mypage.css';
import LivingBtn from '../../components/LivingBtn/LivingBtn';
import Button from '../../components/Button/Button';
import UserForm from "../../components/UserForm/UserForm";
import axiosInstance from '../Login/axiosInstance';

const Mypage = () => {
  const [activeTab, setActiveTab] = useState('tab1');
  const [input, setInput] = useState({
    name: "",
    birth: "",
    gender: "",
    profileImageUrl: ""
  });
  const [loading, setLoading] = useState(true);

  // API 호출로 사용자 정보 가져오기
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await axiosInstance.get('/member/me');
        const userData = response.data;
        setInput({
          name: userData.nickname,
          birth: userData.birthday,
          gender: userData.genderType,
          profileImageUrl: userData.profileImageUrl
        });
        setLoading(false);
      } catch (error) {
        console.error('사용자 정보 불러오기 실패:', error);
        setLoading(false);
      }
    };

    fetchUserData();
  }, []);

  // 입력 값 변경 처리 함수
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

  if (loading) {
    return <div>로딩 중...</div>;
  }

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
              <img src={input.profileImageUrl} className="user-img" alt="User Profile" />
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
