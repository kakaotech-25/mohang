import { useState } from 'react';
import Button from "../../components/Button/Button";
import './UserForm.css';
import axiosInstance from "../../pages/Login/axiosInstance";

const UserForm = ({ input, onChange }) => {
  const [isNameValid, setIsNameValid] = useState(null); // 닉네임 중복 검사 결과
  const [loading, setLoading] = useState(false); // 중복 검사 요청 상태

  const checkNickname = async () => {
    if (!input.name) {
      alert("닉네임을 입력해주세요.");
      return;
    }

    if (input.name.length > 50) {
      alert("닉네임은 1자 이상 50자 이하만 허용됩니다.");
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

  const validateBirthDate = (birthDate) => {
    const today = new Date();
    const selectedDate = new Date(birthDate);

    // 생년월일이 현재 날짜보다 이후일 경우
    if (selectedDate > today) {
      alert("생년월일은 현재 날짜보다 이후일 수 없습니다.");
      return false; // 유효하지 않음
    } else {
      return true; // 유효함
    }
  };

  const handleBirthChange = (e) => {
    const { value } = e.target;

    // 생년월일이 유효한 경우에만 상태를 업데이트
    if (validateBirthDate(value)) {
      onChange(e); // 생년월일이 유효할 때만 상태를 업데이트
    }
  };

  // 닉네임 입력 시 글자 수 제한 및 유효성 검사
  const handleNameChange = (e) => {
    const { value } = e.target;

    if (value.length > 50) {
      alert("닉네임은 1자 이상 50자 이하만 허용됩니다.");
      return; // 50자를 초과하면 입력 중단
    }

    onChange(e); // 유효한 입력일 경우 상태 업데이트
  };

  return (
    <div className="user-form">
      <section className="nickname">
        <div>닉네임</div>
        <input
          name="name"
          value={input.name}
          onChange={handleNameChange} // 닉네임 입력 시 글자 수 체크
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
          onChange={handleBirthChange}
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
