import React from "react";
import "./LivingInfo.css";
import ProfileInfo from "../../components/ProfileInfo/ProfileInfo";
import LivingBtn from "../../components/LivingBtn/LivingBtn";
import progressbar2 from "../../assets/progressbar2.png";
import { useNavigate } from "react-router-dom";
import Button from "../../components/Button/Button";

const LivingInfo = () => {
  const navigate = useNavigate();

  const handleLivingSelectionChange = (selectedOptions) => {
    console.log("Selected options:", selectedOptions);
  };

  return (
    <section className="living-page">
        <ProfileInfo text={"여행지 추천에 필요한 생활 정보를 선택해주세요."} src={progressbar2} />
        <section className="living-body">
                <LivingBtn onChangeSelection={handleLivingSelectionChange} />

            <section>
                <Button text={"다음"} onClick={() => navigate(`/signup/interestedplace`)} />
            </section>
        </section>
        
    </section>
  );
};

export default LivingInfo;
