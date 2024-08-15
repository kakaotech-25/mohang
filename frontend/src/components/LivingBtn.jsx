import "./LivingBtn.css"

const LivingBtn = ({ src, text }) => {
  return (
    <div className="livingbtn">
      <img src={src} className="livingimg" />
      <div className="livingtext">{text}</div>
    </div>
  );
};

export default LivingBtn;
