import './CheckImg.css';

const CheckImg = ({ src, isSelected, onClick }) => {
  return (
    <div className="profile-imgs">
      <img 
        src={src} 
        alt="checkable" 
        onClick={onClick} 
        className="profile-img"
      />
      {isSelected && (
        <div className="profile-checkmark">
          ✔️
        </div>
      )}
    </div>
  );
};

export default CheckImg;