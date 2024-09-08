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
  const [selectedLivingInfo, setSelectedLivingInfo] = useState([]); // 선택된 생활정보 저장
  const [loading, setLoading] = useState(true);
  const [livingLoading, setLivingLoading] = useState(true); // 생활정보 로딩 상태

  useEffect(() => {
    const fetchUserData = async () => {
      try {
        const response = await axiosInstance.get('/member/me'); // 사용자 정보 호출
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

  // 생활정보 불러오기
  useEffect(() => {
    const fetchLivingInfo = async () => {
      try {
        const response = await axiosInstance.get('/live/info/member'); // 생활정보 API 호출
        const livingInfoData = response.data.liveInfoResponses;

        // 선택된 생활정보 필터링
        const selectedInfo = livingInfoData
          .filter(info => info.contain) // 선택된 정보만 필터링
          .map(info => info.name); // 이름만 추출하여 저장

        setSelectedLivingInfo(selectedInfo.length > 0 ? selectedInfo : ["해당없음"]); // 선택된 정보가 없으면 "해당없음"
        setLivingLoading(false);
      } catch (error) {
        console.error('생활정보 불러오기 실패:', error);
        setLivingLoading(false);
      }
    };

    fetchLivingInfo();
  }, []);

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
    setSelectedLivingInfo(selectedOptions); // 선택된 생활정보 변경
  };

  if (loading || livingLoading) {
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
            <LivingBtn 
              onChangeSelection={handleLivingSelectionChange} 
              selectedOptions={selectedLivingInfo} // 선택된 생활정보 전달
            />
            <section>
              <Button text={"저장"} />
            </section>
          </div>
        )}
      </section>
    </div>
  );
};

export default Mypage;
