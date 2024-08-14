const LivingBtn = ({ src, text }) => {
  const containerStyle = {
    width: '120px',
    height: '120px',
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
    justifyContent: 'center',
    border: '3px solid',
    borderRadius: '10px',
    boxSizing: 'border-box',
  };

  const imageStyle = {
    width: '70px',
    height: '70px',
    objectFit: 'cover',
  };

  const textStyle = {
    marginTop: '10px',
    textAlign: 'center',
    fontSize: '14px',
  };

  return (
    <div style={containerStyle}>
      <img src={src} style={imageStyle} />
      <div style={textStyle}>{text}</div>
    </div>
  );
};

export default LivingBtn;
