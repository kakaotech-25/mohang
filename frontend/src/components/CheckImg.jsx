import './CheckImg.css';

const CheckImg = ({ src, isSelected, onClick }) => {
  return (
    <div className="profileimgs">
      <img 
        src={src} 
        alt="checkable" 
        onClick={onClick} 
        className="profileimg"
        onMouseEnter={e => e.currentTarget.classList.add('darken')}
        onMouseLeave={e => e.currentTarget.classList.remove('darken')}
      />
      {isSelected && (
        <div className="checkmark">
          ✔️
        </div>
      )}
    </div>
  );
};

export default CheckImg;