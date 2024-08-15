import "./LivingBtn.css";

const LivingBtn = ({ src, text, isSelected, onClick }) => {
  return (
    <div
      className={`livingbtn ${isSelected ? "selected" : ""}`}
      onClick={onClick}
    >
      <img src={src} className="livingimg" alt={text} />
      <div className="livingtext">{text}</div>
      {isSelected && <div className="livingcheckmark">✔️</div>}
    </div>
  );
};

export default LivingBtn;
