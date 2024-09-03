import { useState } from 'react';
import Button from "../../components/Button/Button";
import './UserForm.css';
import axiosInstance from "../../pages/Login/axiosInstance";

const UserForm = ({ input, onChange }) => {
  const [isNameValid, setIsNameValid] = useState(null); // 중복 검사 결과 상태
  const [loading, setLoading] = useState(false); // 중복 검사 요청 상태

  const checkNickname = async () => {
    if (!input.name) {
      alert("닉네임을 입력해주세요.");
      return;
    }

    setLoading(true);
    try {
      const response = await axiosInstance.post("/member/check/nickname", {
        nickname: input.name,
      });

      if (response.data.message === "사용 가능한 닉네임입니다.") {
        setIsNameValid(true);
        alert("사용 가능한 닉네임입니다.");
      } else {
        setIsNameValid(false);
        alert("이미 사용 중인 닉네임입니다.");
      }
    } catch (error) {
      setIsNameValid(false);
      console.error("닉네임 중복 검사 실패:", error);
      alert("닉네임 중복 검사 중 오류가 발생했습니다.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="user-form">
      <section className="nickname">
        <div>닉네임</div>
        <input
          name="name"
          value={input.name}
          onChange={onChange}
          placeholder="닉네임을 입력하세요."
        />
        <div className="nickname-button">
          <Button
            text={loading ? "검사 중..." : "중복 검사"}
            onClick={checkNickname}
            disabled={loading} // 검사 중에는 버튼 비활성화
          />
        </div>
      </section>

      <section className="birth">
        <div>생년월일</div>
        <input
          name="birth"
          value={input.birth}
          onChange={onChange}
          type="date"
        />
      </section>

      <section className="gender">
        <div>성별</div>
        <label>
          <input
            type="radio"
            name="gender"
            value="MEN"
            checked={input.gender === "MEN"}
            onChange={onChange}
          />
          남성
        </label>
        <label>
          <input
            type="radio"
            name="gender"
            value="WOMEN"
            checked={input.gender === "WOMEN"}
            onChange={onChange}
          />
          여성
        </label>
      </section>
    </div>
  );
};

export default UserForm;
