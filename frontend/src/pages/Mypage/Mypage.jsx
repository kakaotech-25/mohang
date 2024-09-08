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
  const [livingInfoIds, setLivingInfoIds] = useState([]); // 생활정보의 id 저장
  const [loading, setLoading] = useState(true);
  const [livingLoading, setLivingLoading] = useState(true); // 생활정보 로딩 상태

  // livingInfoData를 전역 상태로 정의
  const [livingInfoData, setLivingInfoData] = useState([]);

  // API 호출로 사용자 정보 가져오기
  useEffect(() => {
    const fetchUserData = async () => {
      try {
        console.log('사용자 정보 불러오기 시작'); // 콘솔 로그 추가
        const response = await axiosInstance.get('/member/me'); // 사용자 정보 호출
        const userData = response.data;
        setInput({
          name: userData.nickname,
          birth: userData.birthday,
          gender: userData.genderType,
          profileImageUrl: userData.profileImageUrl
        });
        setLoading(false);
        console.log('사용자 정보 불러오기 완료:', userData); // 콘솔 로그 추가
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
      console.log('fetchLivingInfo 함수 실행');
      try {
        const response = await axiosInstance.get('/live/info/member'); // 생활정보 API 호출
        console.log('생활정보 응답:', response.data); // 콘솔 로그 추가
        const livingInfoData = response.data.liveInfoResponses;
        
        // 선택된 생활정보 필터링
        const selectedInfo = livingInfoData
          .filter(info => info.contain) // 선택된 정보만 필터링
          .map(info => info.name); // 이름만 추출하여 저장

        const selectedInfoIds = livingInfoData
          .filter(info => info.contain)
          .map(info => info.liveInfoId); // 선택된 정보의 id 저장

        setLivingInfoData(livingInfoData); // 전역 상태로 저장
        setSelectedLivingInfo(selectedInfo.length > 0 ? selectedInfo : ["해당없음"]); // 선택된 정보가 없으면 "해당없음"
        setLivingInfoIds(selectedInfoIds); // 선택된 id 값 저장
        setLivingLoading(false);
      } catch (error) {
        console.error('생활정보 불러오기 실패:', error);
        setLivingLoading(false);
      }
    };

    fetchLivingInfo();
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
    setSelectedLivingInfo(selectedOptions); // 선택된 생활정보 변경
    // 선택된 옵션에 따라 id 값도 업데이트
    const updatedLivingIds = livingInfoData
      .filter(info => selectedOptions.includes(info.name))
      .map(info => info.liveInfoId);
    setLivingInfoIds(updatedLivingIds);
  };

  // 생활정보 수정 (저장) 요청
  const handleSaveClick = async () => {
    try {
      const payload = {
        liveInfoIds: livingInfoIds.includes("해당없음") ? [] : livingInfoIds, // "해당없음" 선택 시 빈 배열 전송
      };
      console.log("저장할 데이터:", payload);

      const response = await axiosInstance.put('/live/info/member', payload);

      if (response.status === 204) {
        console.log("생활정보 수정 성공");
        alert("생활정보가 성공적으로 저장되었습니다.");
      } else {
        console.error("응답 실패:", response);
      }
    } catch (error) {
      console.error("생활정보 수정 요청 실패:", error);
      alert("저장 중 문제가 발생했습니다.");
    }
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
              <Button text={"저장"} onClick={handleSaveClick} />
            </section>
          </div>
        )}
      </section>
    </div>
  );
};

export default Mypage;
