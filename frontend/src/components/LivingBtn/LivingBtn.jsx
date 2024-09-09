import React, { useState, useEffect } from "react";
import "./LivingBtn.css";
import physical_img from "../../assets/physical.png";
import visual_img from "../../assets/visual.png";
import hearing_img from "../../assets/hearing.png";
import infants_img from "../../assets/infants.png";
import senior_img from "../../assets/senior.png";
import nothing_img from "../../assets/nothing.png";

// LivingBtn 컴포넌트
const LivingBtn = ({ onChangeSelection, selectedOptions }) => {
  const [selectedLivingOptions, setSelectedLivingOptions] = useState(
    selectedOptions.length > 0 ? selectedOptions : ["해당없음"]
  );

  const livingOptions = [
    { img: physical_img, text: "지체장애" },
    { img: visual_img, text: "시각장애" },
    { img: hearing_img, text: "청각장애" },
    { img: infants_img, text: "영유아가족" },
    { img: senior_img, text: "고령자" },
    { img: nothing_img, text: "해당없음" },
  ];

  // 선택 핸들러
  const handleSelect = (text) => {
    if (text === "해당없음") {
      setSelectedLivingOptions(["해당없음"]);
    } else {
      setSelectedLivingOptions((prevOptions) => {
        const updatedOptions = prevOptions.filter(
          (option) => option !== "해당없음"
        );
        if (updatedOptions.includes(text)) {
          return updatedOptions.filter((option) => option !== text);
        } else {
          return [...updatedOptions, text];
        }
      });
    }
  };

  // 최초 렌더링 시 selectedOptions로 초기화
  useEffect(() => {
    if (selectedOptions && selectedOptions.length > 0) {
      setSelectedLivingOptions(selectedOptions);
    }
  }, [selectedOptions]);

  // 부모 컴포넌트로 선택된 옵션 전달
  useEffect(() => {
    if (onChangeSelection) {
      onChangeSelection(selectedLivingOptions);
    }
  }, [selectedLivingOptions]);  // 의존성 배열에 onChangeSelection을 포함하지 않음

  return (
    <section className="livingbtns">
      {livingOptions.map((option) => (
        <div
          key={option.text}
          className={`living-btn ${
            selectedLivingOptions.includes(option.text) ? "selected" : ""
          }`}
          onClick={() => handleSelect(option.text)}
        >
          <img src={option.img} className="living-img" alt={option.text} />
          <div className="living-text">{option.text}</div>
          {selectedLivingOptions.includes(option.text) && (
            <div className="living-checkmark">✔️</div>
          )}
        </div>
      ))}
    </section>
  );
};

export default LivingBtn;
