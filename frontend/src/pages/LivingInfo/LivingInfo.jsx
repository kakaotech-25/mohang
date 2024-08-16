import React, { useState } from "react";
import "./LivingInfo.css";
import ProfileInfo from "../../components/ProfileInfo/ProfileInfo";
import progressbar2 from "../../assets/progressbar2.png";
import { useNavigate } from "react-router-dom";
import Button from "../../components/Button/Button";
import livingimg1 from "../../assets/physical.png";
import livingimg2 from "../../assets/visual.png";
import livingimg3 from "../../assets/hearing.png";
import livingimg4 from "../../assets/infants.png";
import livingimg5 from "../../assets/senior.png";
import livingimg6 from "../../assets/nothing.png";

// LivingBtn 컴포넌트를 이 파일 안에 선언
const LivingBtn = ({ src, text, isSelected, onClick }) => {
  return (
    <div
      className={`living-btn ${isSelected ? "selected" : ""}`}
      onClick={onClick}
    >
      <img src={src} className="living-img" alt={text} />
      <div className="living-text">{text}</div>
      {isSelected && <div className="living-checkmark">✔️</div>}
    </div>
  );
};

const LivingInfo = () => {
    const navigate = useNavigate();

    const [selectedLivingOptions, setSelectedLivingOptions] = useState(["해당없음"]); // "해당없음"이 기본 선택

    const handleSelect = (text) => {
        if (text === "해당없음") {
            // "해당없음"이 클릭되면 나머지 선택 해제
            setSelectedLivingOptions(["해당없음"]);
        } else {
            // 다른 옵션이 선택되면 "해당없음" 해제 및 옵션 추가
            setSelectedLivingOptions((prevOptions) => {
                const updatedOptions = prevOptions.filter(option => option !== "해당없음");
                if (updatedOptions.includes(text)) {
                    return updatedOptions.filter(option => option !== text); // 이미 선택된 항목은 해제
                } else {
                    return [...updatedOptions, text]; // 선택 항목 추가
                }
            });
        }
    };

    const livingOptions = [
        { img: livingimg1, text: "지체장애" },
        { img: livingimg2, text: "시각장애" },
        { img: livingimg3, text: "청각장애" },
        { img: livingimg4, text: "영유아가족" },
        { img: livingimg5, text: "고령인" },
        { img: livingimg6, text: "해당없음" },
    ];

    // "해당없음"이 자동으로 체크되는 로직
    React.useEffect(() => {
        if (selectedLivingOptions.length === 0) {
            setSelectedLivingOptions(["해당없음"]);
        }
    }, [selectedLivingOptions]);

    return (
        <section className="living-page">
            <ProfileInfo text={"여행지 추천에 필요한 생활 정보를 선택해주세요."} src={progressbar2} />
            
            <section className='living-body'>
                <section className="livingbtns">
                    {livingOptions.map(option => (
                        <LivingBtn
                            key={option.text}
                            src={option.img}
                            text={option.text}
                            isSelected={selectedLivingOptions.includes(option.text)}
                            onClick={() => handleSelect(option.text)}
                        />
                    ))}
                </section>

                <section className="subbtn">
                    <Button text={"다음"} onClick={() => navigate(`/signup/interestedplace`)} />
                </section>
            </section>
        </section>
    );
};

export default LivingInfo;
