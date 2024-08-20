import "./LivingBtn.css";

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

export default LivingBtn;