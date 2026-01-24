import React, { useState, useEffect } from 'react';
import { 
  LayoutDashboard, 
  Car, 
  CreditCard,
  AlertCircle,
  ParkingSquare
} from 'lucide-react';
import { reportsAPI, zonesAPI } from '../services/api';

/**
 * Dashboard Component - Chỉ hiển thị nội dung
 * Sidebar được quản lý bởi Layout component
 */
export default function Dashboard() {
  const [stats, setStats] = useState({
    totalSlots: 0,
    occupiedSlots: 0,
    availableSlots: 0,
    dailyRevenue: '0đ'
  });
  const [lastUpdated, setLastUpdated] = useState(null);

  // Hàm tiện ích: lấy ngày theo múi giờ local dạng yyyy-MM-dd
  const getTodayLocalDateString = () => {
    const now = new Date();
    const year = now.getFullYear();
    const month = String(now.getMonth() + 1).padStart(2, '0');
    const day = String(now.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
  };

  // Load thống kê từ API
  useEffect(() => {
    const loadStats = async () => {
      try {
        const today = getTodayLocalDateString();

        // Gọi song song nhưng xử lý lỗi từng API để tránh "đổ vỡ dây chuyền"
        const [zonesResult, revenueResult] = await Promise.allSettled([
          zonesAPI.getAll(),
          reportsAPI.getDailyRevenue(today)
        ]);

        // Xử lý dữ liệu khu vực & slot
        let zonesData = [];
        if (zonesResult.status === 'fulfilled') {
          const rawZones = zonesResult.value.data.data || zonesResult.value.data;
          if (Array.isArray(rawZones)) {
            zonesData = rawZones;
          } else {
            console.error('Zones data is not an array:', rawZones);
          }
        } else {
          console.error('Error loading zones:', zonesResult.reason);
        }

        const allSlots = zonesData.flatMap(zone => Array.isArray(zone.slots) ? zone.slots : []);
        const totalSlots = allSlots.length;
        const occupiedSlots = allSlots.filter(s => s.status === 'OCCUPIED').length;
        const availableSlots = allSlots.filter(s => s.status === 'AVAILABLE').length;

        // Xử lý dữ liệu doanh thu (có thể lỗi nhưng không chặn hiển thị slot)
        let revenueValue = 0;
        if (revenueResult.status === 'fulfilled') {
          const revenueData = revenueResult.value.data.data || revenueResult.value.data;
          revenueValue = revenueData.revenue || 0;
        } else {
          console.error('Error loading daily revenue:', revenueResult.reason);
        }

        setStats({
          totalSlots,
          occupiedSlots,
          availableSlots,
          dailyRevenue: `${revenueValue.toLocaleString('vi-VN')}đ`
        });
        setLastUpdated(new Date());
      } catch (error) {
        console.error('Error loading stats:', error);
        setStats({
          totalSlots: 0,
          occupiedSlots: 0,
          availableSlots: 0,
          dailyRevenue: '0đ'
        });
      }
    };

    loadStats();
    // Refresh stats mỗi 30 giây
    const interval = setInterval(loadStats, 30000);
    return () => clearInterval(interval);
  }, []);

  // Dữ liệu thống kê hiển thị
  const statsDisplay = [
    { 
      title: 'Tổng vị trí', 
      value: stats.totalSlots.toString(), 
      icon: <ParkingSquare size={28} />, 
      color: 'bg-blue-600', 
      textColor: 'text-blue-600',
      bgColor: 'bg-blue-100'
    },
    { 
      title: 'Số xe đang gửi', 
      value: stats.occupiedSlots.toString(), 
      icon: <Car size={28} />, 
      color: 'bg-yellow-500', 
      textColor: 'text-yellow-600',
      bgColor: 'bg-yellow-100'
    },
    { 
      title: 'Vị trí trống', 
      value: stats.availableSlots.toString(), 
      icon: <AlertCircle size={28} />, 
      color: 'bg-green-500', 
      textColor: 'text-green-600',
      bgColor: 'bg-green-100'
    },
    { 
      title: 'Doanh thu hôm nay', 
      value: stats.dailyRevenue, 
      icon: <CreditCard size={28} />, 
      color: 'bg-purple-600', 
      textColor: 'text-purple-600',
      bgColor: 'bg-purple-100'
    },
  ];

  return (
    <div className="p-6">
      {/* Header */}
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-slate-800">Tổng quan hệ thống</h1>
        <p className="text-sm text-slate-500 mt-1">
          {lastUpdated
            ? `Cập nhật lần cuối: ${lastUpdated.toLocaleTimeString('vi-VN')}`
            : 'Đang tải dữ liệu thống kê...'}
        </p>
      </div>

      {/* Stats Cards Grid */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
        {statsDisplay.map((stat, index) => (
          <div 
            key={index} 
            className="bg-white rounded-xl shadow-sm border border-slate-100 p-6 flex items-start justify-between hover:shadow-md transition-shadow duration-200 cursor-pointer"
          >
            <div>
              <p className="text-sm font-medium text-slate-500 mb-1">{stat.title}</p>
              <h3 className="text-2xl font-bold text-slate-800">{stat.value}</h3>
            </div>
            <div className={`p-3 rounded-lg ${stat.bgColor} ${stat.textColor}`}>
              {stat.icon}
            </div>
          </div>
        ))}
      </div>

      {/* Placeholder for future content (Charts/Tables) */}
      <div className="bg-white rounded-xl shadow-sm border border-slate-100 p-8 h-96 flex flex-col items-center justify-center text-slate-400 border-dashed border-2 border-slate-200">
        <div className="bg-slate-50 p-4 rounded-full mb-4">
          <LayoutDashboard size={40} className="text-slate-300" />
        </div>
        <p className="font-medium">Khu vực biểu đồ và danh sách xe mới nhất</p>
        <p className="text-sm mt-2">Sẽ được thêm vào trong các bước tiếp theo...</p>
      </div>
    </div>
  );
}

