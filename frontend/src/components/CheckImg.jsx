import './CheckImg.css';

const CheckImg = ({ src, isSelected, onClick }) => {
  return (
    <div className="profileimgs">
      <img 
        src={src} 
        alt="checkable" 
        onClick={onClick} 
        className="profileimg"
      />
      {isSelected && (
        <div className="profilecheckmark">
          ✔️
        </div>
      )}
    </div>
  );
};

export default CheckImg;