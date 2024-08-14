import React from 'react';

const ImageWithCheck = ({ src, isSelected, onClick }) => {
  return (
    <div style={{ position: 'relative', display: 'inline-block' }}>
      <img 
        src={src} 
        alt="checkable" 
        onClick={onClick} 
        style={{
          width: '150px',
          height: '150px',
          cursor: 'pointer',
          transition: 'filter 0.3s ease', // 필터 적용에 부드러운 전환 추가
        }}
        // 마우스가 이미지 위에 올라갔을 때 어둡게 만들기
        onMouseEnter={e => e.currentTarget.style.filter = 'brightness(50%)'}
        onMouseLeave={e => e.currentTarget.style.filter = 'brightness(100%)'}
      />
      {isSelected && (
        <div style={{
          position: 'absolute',
          top: '10px',
          right: '10px',
          backgroundColor: 'white',
          borderRadius: '50%',
          padding: '5px',
        }}>
          ✔️
        </div>
      )}
    </div>
  );
};

export default ImageWithCheck;
