import Button from "../../components/Button/Button";
import Radio from "../../components/Radio";
import './UserForm.css'

const UserForm = ({ input, onChange }) => {
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
          <Button text={"중복 검사"} />
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
        <Radio name="contact" value="MAN" defaultChecked>
          남성
        </Radio>
        <Radio name="contact" value="WOMAN">
          여성
        </Radio>
      </section>
    </div>
  );
};

export default UserForm;
