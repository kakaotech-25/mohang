import { useState } from "react";
import "./Profile.css";
import ProfileInfo from "../../components/ProfileInfo/ProfileInfo";
import progressbar from "../../assets/progressbar1.png";
import Button from "../../components/Button/Button";
import { useNavigate } from "react-router-dom";
import UserForm from "../../components/UserForm/UserForm";
import axiosInstance from "../Login/axiosInstance";

const Profile = () => {
  const navigate = useNavigate();
  const [input, setInput] = useState({
    name: "",
    birth: "",
    gender: "",
  });

  const [isNameValid, setIsNameValid] = useState(null); // 닉네임 중복 검사 상태
  const [loading, setLoading] = useState(false); // 로딩 상태

  // 현재 날짜를 구함
  const today = new Date().toISOString().split("T")[0]; // YYYY-MM-DD 형식으로 변환

  // 입력 값 변경 처리 함수
  const onChange = (e) => {
    const { name, value } = e.target;

    // 닉네임 길이 검사 (50글자 이하만 허용)
    if (name === "name" && value.length > 50) {
      alert("닉네임은 1자 이상 50자 이하만 허용됩니다.");
      return; // 50자를 넘으면 입력을 막음
    }

    // 생년월일 유효성 검사
    if (name === "birth" && value > today) {
      alert("생년월일은 현재 날짜보다 이후일 수 없습니다.");
      return; // 잘못된 입력일 경우 처리 중단
    }

    setInput({
      ...input,
      [name]: value,
    });
  };

  const handleNextClick = async () => {
    // 닉네임 중복 검사 여부 확인
    if (isNameValid === false) {
      alert("닉네임이 중복되었습니다. 다른 닉네임을 입력하세요.");
      return;
    }

    if (isNameValid === null) {
      alert("닉네임 중복 검사를 해주세요.");
      return;
    }

    // 생년월일 및 성별 입력 여부 확인
    if (!input.birth || !input.gender) {
      alert("생년월일과 성별을 입력해주세요.");
      return;
    }

    // API 요청을 통해 프로필 정보를 서버에 전송
    setLoading(true);
    try {
      const response = await axiosInstance.post("/member/signup/profile", {
        nickname: input.name,
        birthday: input.birth,
        genderType: input.gender,
        profileImageUrl: "https://profile-image.com", // 프로필 이미지 경로는 필요에 따라 수정필요
      });

      // 응답이 성공적이면 생활정보 입력 페이지로 이동
      if (response.status === 204) {
        navigate(`/signup/livinginfo`);
      }
    } catch (error) {
      // 오류 처리
      if (error.response.status === 403) {
        alert("이미 가입된 회원입니다. 메인 페이지로 이동합니다.");
        navigate('/');
      } else if (error.response.status === 404) {
        alert("존재하지 않는 회원입니다.");
      } else if (error.response.status === 400) {
        alert("잘못된 요청입니다. 입력 내용을 다시 확인해주세요.");
      } else {
        alert("서버에 오류가 발생했습니다. 다시 시도해주세요.");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <section className="profile-page">
      <ProfileInfo text={"회원가입을 위해 프로필 정보를 입력해주세요."} src={progressbar} />

      <section className="profile-body">
        <UserForm input={input} onChange={onChange} setIsNameValid={setIsNameValid} />

        <section className="profile-btn">
          <Button
            text={loading ? "저장 중..." : "다음"}
            onClick={handleNextClick}
            disabled={loading} // 저장 중에는 버튼 비활성화
          />
        </section>
      </section>
    </section>
  );
};

export default Profile;
