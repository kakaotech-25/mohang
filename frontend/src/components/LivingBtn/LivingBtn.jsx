import React, { useState, useEffect } from "react";
import "./LivingBtn.css";
import livingimg1 from "../../assets/physical.png";
import livingimg2 from "../../assets/visual.png";
import livingimg3 from "../../assets/hearing.png";
import livingimg4 from "../../assets/infants.png";
import livingimg5 from "../../assets/senior.png";
import livingimg6 from "../../assets/nothing.png";

// LivingBtn 컴포넌트
const LivingBtn = ({ onChangeSelection, selectedOptions = ["해당없음"] }) => {
  const [selectedLivingOptions, setSelectedLivingOptions] = useState(selectedOptions);

  const livingOptions = [
    { img: livingimg1, text: "지체장애" },
    { img: livingimg2, text: "시각장애" },
    { img: livingimg3, text: "청각장애" },
    { img: livingimg4, text: "영유아가족" },
    { img: livingimg5, text: "고령인" },
    { img: livingimg6, text: "해당없음" },
  ];

  const handleSelect = (text) => {
    if (text === "해당없음") {
      setSelectedLivingOptions(["해당없음"]);
    } else {
      setSelectedLivingOptions((prevOptions) => {
        const updatedOptions = prevOptions.filter((option) => option !== "해당없음");
        if (updatedOptions.includes(text)) {
          return updatedOptions.filter((option) => option !== text);
        } else {
          return [...updatedOptions, text];
        }
      });
    }
  };

  // 부모 컴포넌트로 선택된 옵션 전달
  useEffect(() => {
    if (selectedLivingOptions.length === 0) {
      setSelectedLivingOptions(["해당없음"]);
    }
    onChangeSelection(selectedLivingOptions);
  }, [selectedLivingOptions, onChangeSelection]);

  return (
    <section className="livingbtns">
      {livingOptions.map((option) => (
        <div
          key={option.text}
          className={`living-btn ${selectedLivingOptions.includes(option.text) ? "selected" : ""}`}
          onClick={() => handleSelect(option.text)}
        >
          <img src={option.img} className="living-img" alt={option.text} />
          <div className="living-text">{option.text}</div>
          {selectedLivingOptions.includes(option.text) && <div className="living-checkmark">✔️</div>}
        </div>
      ))}
    </section>
  );
};

export default LivingBtn;
