const Radio = ({ children, name, value, defaultChecked}) => {
    return (
    <label>
        <input
        type="radio"
        name={name}
        value={value}
        defaultChecked={defaultChecked}
        />
        {children}
    </label>
    );
};

export default Radio;