import { useState, useEffect } from 'react';
import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';
import './PlannerModal.css';

const PlannerModal = ({ isOpen, mode, title, plan, onClose, onSave }) => {
  // 초기 상태를 빈 값으로 설정
  const [currentPlan, setCurrentPlan] = useState({ title: '', period: '' });
  const [selectedDates, setSelectedDates] = useState([null, null]);
  const [startDate, endDate] = selectedDates;

  useEffect(() => {
    if (mode === 'edit' && plan) {
      setCurrentPlan({
        title: plan.scheduleName || '', // plan의 값이 없을 때 기본값 설정
        period: plan.period || '', // period 값도 기본값 설정
      });

      if (plan.startTime && plan.endTime) {
        setSelectedDates([new Date(plan.startTime), new Date(plan.endTime)]);
      }
    } else if (mode === 'add') {
      // 추가 모드에서는 초기 값을 빈 상태로 설정
      setCurrentPlan({ title: '', period: '' });
      setSelectedDates([null, null]);
    }
  }, [mode, plan]);

  const formatDate = (date) => {
    return date
      ? date.toLocaleDateString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit' })
          .replace(/\./g, '')
          .replace(/\s/g, '.')
      : '';
  };

  const handleSave = () => {
    if (startDate && endDate) {
      onSave({ 
        ...currentPlan, 
        startTime: startDate.toISOString().split('T')[0], 
        endTime: endDate.toISOString().split('T')[0], 
      });
      onClose();
    }
  };

  return (
    isOpen && (
      <div className="modal-overlay">
        <div className="modal-content">
          <div className="modal-header">
            <h2>{title}</h2>
          </div>
          <div className="modal-body">
            <div className="modal-input-group">
              <label>일정 이름</label>
              <input
                type="text"
                name="title"
                value={currentPlan.title || ''} // title이 없을 경우 빈 문자열 설정
                onChange={(e) => setCurrentPlan({ ...currentPlan, title: e.target.value })}
              />
            </div>
            <div className="modal-input-group">
              <label>일정 기간</label>
              <input
                type="text"
                name="period"
                value={
                  startDate && endDate
                    ? `${formatDate(startDate)} ~ ${formatDate(endDate)}`
                    : ''
                }
                readOnly
              />
            </div>
            <div className="calendar-container">
              <DatePicker
                selected={startDate}
                onChange={(dates) => setSelectedDates(dates)}
                startDate={startDate}
                endDate={endDate}
                selectsRange
                inline
                dateFormat="yyyy.MM.dd"
              />
            </div>
          </div>
          <div className="modal-footer">
            <button className="save-button" onClick={handleSave}>
              저장
            </button>
            <button className="cancel-button" onClick={onClose}>
              취소
            </button>
          </div>
        </div>
      </div>
    )
  );
};

export default PlannerModal;
