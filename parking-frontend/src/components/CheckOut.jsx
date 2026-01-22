import React, { useState } from 'react';
import { Search, Receipt, Clock, Calendar, CheckCircle, ArrowLeft, Car, Bike, CreditCard } from 'lucide-react';
import { ticketsAPI } from '../services/api';

<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
/**
 * Check-out Component - Chỉ hiển thị nội dung
 * Sidebar được quản lý bởi Layout component
 */
<<<<<<< HEAD
=======
>>>>>>> master
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
export default function CheckOut() {
  const [step, setStep] = useState(1); // 1: Input, 2: Bill
  const [query, setQuery] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [billData, setBillData] = useState(null);

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!query) return;

    setIsLoading(true);
    
    try {
<<<<<<< HEAD
=======
<<<<<<< HEAD
      // Tìm vé theo biển số hoặc mã vé
      const response = await ticketsAPI.getByPlateNumber(query);
      
      if (response.data && response.data.length > 0) {
        const ticket = response.data[0];
        
        // Map dữ liệu từ API sang billData
        const checkInTime = new Date(ticket.checkInTime);
        const checkOutTime = new Date();
        const duration = calculateDuration(checkInTime, checkOutTime);
        
        setBillData({
          plateNumber: ticket.plateNumber,
          vehicleType: ticket.vehicleType.toLowerCase(), // MOTORBIKE -> motorbike
          ticketCode: ticket.ticketCode,
          checkInTime: checkInTime,
          checkOutTime: checkOutTime,
          duration: duration,
          amount: ticket.fee || 0,
          ticketId: ticket.id
        });
        
        setStep(2);
      } else {
        alert('Không tìm thấy vé xe với thông tin này.');
      }
=======
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
      const trimmed = query.trim();
      const isIdLookup = /^\d+$/.test(trimmed);
      const response = isIdLookup
        ? await ticketsAPI.getById(trimmed)
        : await ticketsAPI.getByPlateNumber(trimmed);
      const ticket = response.data?.data;

      if (!ticket || ticket.status !== 'ACTIVE') {
        alert('Không tìm thấy vé đang hoạt động cho thông tin này.');
        return;
      }

      const checkInTime = new Date(ticket.entryTime);
      const checkOutTime = new Date();
      const duration = calculateDuration(checkInTime, checkOutTime);

      setBillData({
        plateNumber: ticket.licensePlate,
        vehicleType: (ticket.vehicleType || 'MOTORBIKE').toLowerCase(),
        ticketCode: `T-${ticket.id.toString().padStart(6, '0')}`,
        checkInTime,
        checkOutTime,
        duration,
        amount: estimateAmount(checkInTime, checkOutTime, ticket.vehicleType),
        ticketId: ticket.id
      });
      setStep(2);
<<<<<<< HEAD
=======
>>>>>>> master
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
    } catch (error) {
      console.error('Search ticket error:', error);
      alert(error.response?.data?.message || 'Không tìm thấy vé xe. Vui lòng kiểm tra lại.');
    } finally {
      setIsLoading(false);
    }
  };

  const calculateDuration = (checkIn, checkOut) => {
    const diff = checkOut - checkIn;
    const hours = Math.floor(diff / (1000 * 60 * 60));
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
    return `${hours} giờ ${minutes} phút`;
  };

  const handlePayment = async () => {
    if (!billData) return;

    try {
<<<<<<< HEAD
      const response = await ticketsAPI.checkOut(billData.ticketId);
      const paidAmount = response.data?.data?.totalAmount ?? billData.amount;
      alert(`Thanh toán thành công!\nSố tiền: ${paidAmount.toLocaleString('vi-VN')} VNĐ\nCổng đang mở...`);
=======
<<<<<<< HEAD
      // Gọi API checkout
      const response = await ticketsAPI.checkOut(billData.ticketId);
      
      alert(`Thanh toán thành công!\nSố tiền: ${billData.amount.toLocaleString('vi-VN')} VNĐ\nCổng đang mở...`);
=======
      const response = await ticketsAPI.checkOut(billData.ticketId);
      const paidAmount = response.data?.data?.totalAmount ?? billData.amount;
      alert(`Thanh toán thành công!\nSố tiền: ${paidAmount.toLocaleString('vi-VN')} VNĐ\nCổng đang mở...`);
>>>>>>> master
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
      resetProcess();
    } catch (error) {
      console.error('Checkout error:', error);
      alert(error.response?.data?.message || 'Có lỗi xảy ra khi thanh toán. Vui lòng thử lại.');
    }
  };

  const resetProcess = () => {
    setStep(1);
    setQuery('');
    setBillData(null);
  };

  // Format tiền tệ
  const formatCurrency = (amount) => {
    return new Intl.NumberFormat('vi-VN', { style: 'currency', currency: 'VND' }).format(amount);
  };

<<<<<<< HEAD
=======
<<<<<<< HEAD
=======
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
  const estimateAmount = (entry, exit, vehicleType) => {
    const diffHours = Math.max(1, Math.ceil((exit - entry) / (1000 * 60 * 60)));
    const type = vehicleType?.toUpperCase() === 'CAR' ? 'CAR' : 'MOTORBIKE';
    const firstBlock = 2;
    const firstPrice = type === 'MOTORBIKE' ? 5000 : 10000;
    const extraPrice = type === 'MOTORBIKE' ? 2000 : 5000;
    if (diffHours <= firstBlock) {
      return firstPrice;
    }
    return firstPrice + (diffHours - firstBlock) * extraPrice;
  };

<<<<<<< HEAD
=======
>>>>>>> master
>>>>>>> 8d97af04eff0ac055fbeed2838c3472f501c1be5
  // Format ngày giờ
  const formatDateTime = (date) => {
    return date.toLocaleString('vi-VN', { 
      hour: '2-digit', minute: '2-digit', 
      day: '2-digit', month: '2-digit', year: 'numeric' 
    });
  };

  return (
    <div className="min-h-screen bg-slate-100 flex items-center justify-center p-4 font-sans">
      
      {/* Bước 1: Nhập mã vé / Biển số */}
      {step === 1 && (
        <div className="w-full max-w-md bg-white rounded-2xl shadow-xl overflow-hidden animate-fade-in-up">
          <div className="bg-slate-800 p-6 text-center">
            <h2 className="text-2xl font-bold text-white uppercase tracking-wide">Kiểm Soát Xe Ra</h2>
            <p className="text-slate-400 text-sm mt-1">Quét mã vé hoặc nhập biển số</p>
          </div>
          
          <div className="p-8">
            <form onSubmit={handleSearch} className="space-y-6">
              <div className="space-y-2">
                <label className="text-sm font-semibold text-slate-600 uppercase">Mã vé / Biển số xe</label>
                <div className="relative">
                  <input
                    type="text"
                    value={query}
                    onChange={(e) => setQuery(e.target.value.toUpperCase())}
                    className="block w-full h-16 pl-12 pr-4 text-3xl font-bold text-slate-800 bg-slate-50 border-2 border-slate-300 rounded-xl focus:outline-none focus:border-blue-500 focus:ring-4 focus:ring-blue-500/10 transition-all uppercase placeholder:text-2xl placeholder:font-normal placeholder:text-slate-300"
                    placeholder="VD: 59X112345"
                    autoFocus
                  />
                  <Search className="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400" size={24} />
                </div>
              </div>

              <button
                type="submit"
                disabled={!query || isLoading}
                className="w-full h-14 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-xl shadow-lg shadow-blue-500/30 flex items-center justify-center gap-2 transition-all active:scale-[0.98] disabled:opacity-70 disabled:cursor-not-allowed"
              >
                {isLoading ? (
                  <>
                    <div className="w-5 h-5 border-2 border-white border-t-transparent rounded-full animate-spin"></div>
                    <span>Đang kiểm tra...</span>
                  </>
                ) : (
                  <>
                    <Search size={20} />
                    <span>Kiểm Tra Thông Tin</span>
                  </>
                )}
              </button>
            </form>

            <div className="mt-8 pt-6 border-t border-slate-100 text-center">
              <p className="text-xs text-slate-400">Hệ thống tự động tính toán phí gửi xe theo quy định.</p>
            </div>
          </div>
        </div>
      )}

      {/* Bước 2: Hóa đơn thanh toán */}
      {step === 2 && billData && (
        <div className="w-full max-w-lg bg-white rounded-2xl shadow-2xl overflow-hidden animate-zoom-in relative">
          
          {/* Nút Quay lại */}
          <button 
            onClick={resetProcess}
            className="absolute top-4 left-4 p-2 bg-white/20 hover:bg-white/30 rounded-full text-white transition-colors z-10"
            title="Quay lại"
          >
            <ArrowLeft size={20} />
          </button>

          {/* Header Hóa đơn */}
          <div className="bg-gradient-to-r from-orange-600 to-red-600 p-8 pt-10 text-white relative overflow-hidden">
            <div className="relative z-10 flex flex-col items-center">
              <div className="w-16 h-16 bg-white/20 rounded-full flex items-center justify-center mb-4 backdrop-blur-sm">
                <Receipt size={32} className="text-white" />
              </div>
              <h2 className="text-3xl font-bold uppercase tracking-widest">Hóa Đơn</h2>
              <p className="opacity-90 mt-1 font-mono text-sm">{billData.ticketCode}</p>
            </div>
            
            {/* Pattern trang trí */}
            <div className="absolute top-0 left-0 w-full h-full opacity-10 bg-[radial-gradient(circle_at_center,_var(--tw-gradient-stops))] from-white via-transparent to-transparent"></div>
          </div>

          {/* Nội dung chi tiết */}
          <div className="p-8 space-y-6">
            
            {/* Thông tin xe */}
            <div className="flex items-center justify-between pb-6 border-b border-dashed border-slate-300">
              <div>
                <p className="text-sm text-slate-500 mb-1">Biển số xe</p>
                <p className="text-2xl font-black text-slate-800 uppercase tracking-wider">{billData.plateNumber}</p>
              </div>
              <div className={`px-4 py-2 rounded-lg flex items-center gap-2 font-bold uppercase text-sm
                ${billData.vehicleType === 'motorbike' ? 'bg-blue-100 text-blue-700' : 'bg-orange-100 text-orange-700'}
              `}>
                {billData.vehicleType === 'motorbike' ? <Bike size={18} /> : <Car size={18} />}
                {billData.vehicleType === 'motorbike' ? 'Xe Máy' : 'Ô Tô'}
              </div>
            </div>

            {/* Chi tiết thời gian */}
            <div className="space-y-4">
              <div className="flex justify-between items-center">
                <div className="flex items-center gap-3 text-slate-600">
                  <Calendar size={18} className="text-slate-400" />
                  <span className="text-sm">Thời gian vào</span>
                </div>
                <span className="font-semibold text-slate-800">{formatDateTime(billData.checkInTime)}</span>
              </div>
              
              <div className="flex justify-between items-center">
                <div className="flex items-center gap-3 text-slate-600">
                  <Clock size={18} className="text-slate-400" />
                  <span className="text-sm">Thời gian ra</span>
                </div>
                <span className="font-semibold text-slate-800">{formatDateTime(billData.checkOutTime)}</span>
              </div>

              <div className="flex justify-between items-center bg-slate-50 p-3 rounded-lg border border-slate-100">
                <span className="text-sm font-medium text-slate-500">Tổng thời gian gửi</span>
                <span className="font-bold text-slate-800">{billData.duration}</span>
              </div>
            </div>

            {/* Tổng tiền - Highlight */}
            <div className="bg-red-50 rounded-xl p-6 border-2 border-red-100 text-center space-y-1">
              <p className="text-red-600 font-semibold uppercase text-xs tracking-widest">Tổng tiền thanh toán</p>
              <p className="text-4xl font-black text-red-600 tracking-tight">
                {formatCurrency(billData.amount)}
              </p>
            </div>

            {/* Nút hành động */}
            <button
              onClick={handlePayment}
              className="w-full py-4 bg-emerald-600 hover:bg-emerald-700 text-white rounded-xl shadow-lg shadow-emerald-500/30 font-bold text-lg flex items-center justify-center gap-3 transition-transform hover:scale-[1.02] active:scale-[0.98]"
            >
              <CheckCircle size={24} />
              Xác nhận Thu Tiền & Mở Cổng
            </button>
            
          </div>
          
          {/* Răng cưa hóa đơn (Decorative) */}
          <div className="h-4 bg-slate-100 relative -mt-2" style={{
            backgroundImage: 'linear-gradient(45deg, transparent 50%, #f1f5f9 50%), linear-gradient(-45deg, transparent 50%, #f1f5f9 50%)',
            backgroundSize: '20px 20px',
            backgroundPosition: '0 10px'
          }}></div>
        </div>
      )}

      <style>{`
        @keyframes fadeInUp {
          from { opacity: 0; transform: translateY(20px); }
          to { opacity: 1; transform: translateY(0); }
        }
        @keyframes zoomIn {
          from { opacity: 0; transform: scale(0.95); }
          to { opacity: 1; transform: scale(1); }
        }
        .animate-fade-in-up {
          animation: fadeInUp 0.4s ease-out;
        }
        .animate-zoom-in {
          animation: zoomIn 0.3s ease-out;
        }
      `}</style>
    </div>
  );
}

