import { useState, useEffect } from "react";
import "./LivingInfo.css";
import ProfileInfo from "../../components/ProfileInfo/ProfileInfo";
import LivingBtn from "../../components/LivingBtn/LivingBtn";
import progressbar2 from "../../assets/progressbar2.png";
import { useNavigate } from "react-router-dom";
import Button from "../../components/Button/Button";
import axiosInstance from "../Login/axiosInstance";

const LivingInfo = () => {
  const navigate = useNavigate();
  const [selectedOptions, setSelectedOptions] = useState(["해당없음"]); // 기본값으로 '해당없음'을 선택

  const handleLivingSelectionChange = (selectedOptions) => {
    setSelectedOptions(selectedOptions);
  };

  const handleNextClick = async () => {
    try {
      // "해당없음"이 선택된 경우 빈 배열로 처리
      const payload = {
        liveInfoNames: selectedOptions.includes("해당없음") ? [] : selectedOptions, // "해당없음" 선택 시 빈 배열 전송
      };

      console.log("전송 데이터:", payload);

      const response = await axiosInstance.post("/member/signup/liveinfo", payload);

      if (response.status === 204) {
        console.log("POST 요청 성공");
        navigate(`/signup/interestedplace`);
      } else {
        console.error("응답 실패:", response);
      }
    } catch (error) {
      console.error("POST 요청 실패:", error);
    }
  };

  useEffect(() => {
    console.log("Selected options:", selectedOptions);
  }, [selectedOptions]);

  return (
    <section className="living-page">
      <ProfileInfo text={"여행지 추천에 필요한 생활 정보를 선택해주세요."} src={progressbar2} />
      <section className="living-body">
        <LivingBtn onChangeSelection={handleLivingSelectionChange} />
        <section>
          <Button text={"다음"} onClick={handleNextClick} />
        </section>
      </section>
    </section>
  );
};

export default LivingInfo;
