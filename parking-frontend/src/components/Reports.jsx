import React, { useState } from 'react';
import { 
  TrendingUp, 
  Calendar, 
  DollarSign, 
  Clock,
  ChevronRight 
} from 'lucide-react';
import { reportsAPI } from '../services/api';

/**
 * Reports Component - Báo cáo doanh thu (Chỉ dành cho ADMIN)
 * Sidebar được quản lý bởi Layout component
 */
export default function Reports() {
  const [activeTab, setActiveTab] = useState('daily'); // daily, monthly, shift
  const [dailyDate, setDailyDate] = useState(getTodayString());
  const [monthlyMonth, setMonthlyMonth] = useState(new Date().getMonth() + 1);
  const [monthlyYear, setMonthlyYear] = useState(new Date().getFullYear());
  const [shiftDate, setShiftDate] = useState(getTodayString());
  
  const [dailyData, setDailyData] = useState(null);
  const [monthlyData, setMonthlyData] = useState(null);
  const [shiftData, setShiftData] = useState(null);
  
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  // Helper: Get today's date in yyyy-MM-dd format
  function getTodayString() {
    const now = new Date();
    return now.toISOString().split('T')[0];
  }

  // Fetch Daily Revenue
  const fetchDailyRevenue = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await reportsAPI.getDailyRevenue(dailyDate);
      const data = response.data.data || response.data;
      setDailyData(data);
    } catch (err) {
      setError('Không thể tải dữ liệu doanh thu ngày');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Fetch Monthly Revenue
  const fetchMonthlyRevenue = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await reportsAPI.getMonthlyRevenue(monthlyMonth, monthlyYear);
      const data = response.data.data || response.data;
      setMonthlyData(data);
    } catch (err) {
      setError('Không thể tải dữ liệu doanh thu tháng');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Fetch Shift Revenue
  const fetchShiftRevenue = async () => {
    setLoading(true);
    setError(null);
    try {
      const response = await reportsAPI.getShiftRevenue(shiftDate);
      const data = response.data.data || response.data;
      setShiftData(data);
    } catch (err) {
      setError('Không thể tải dữ liệu doanh thu ca');
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Handle Tab Change
  const handleTabChange = (tab) => {
    setActiveTab(tab);
    setError(null);
  };

  return (
    <div className="p-6">
      {/* Header */}
      <div className="mb-6">
        <h1 className="text-2xl font-bold text-slate-800 flex items-center gap-2">
          <TrendingUp className="text-green-600" size={28} />
          Báo cáo doanh thu
        </h1>
        <p className="text-sm text-slate-500 mt-1">
          Xem báo cáo doanh thu theo ngày, tháng hoặc ca làm việc
        </p>
      </div>

      {/* Tabs */}
      <div className="bg-white rounded-xl shadow-sm border border-slate-100 mb-6">
        <div className="flex border-b border-slate-200">
          <button
            onClick={() => handleTabChange('daily')}
            className={`flex-1 py-3 px-4 text-sm font-medium transition-colors ${
              activeTab === 'daily'
                ? 'text-blue-600 border-b-2 border-blue-600 bg-blue-50'
                : 'text-slate-600 hover:text-slate-800 hover:bg-slate-50'
            }`}
          >
            <Calendar size={16} className="inline mr-2" />
            Theo ngày
          </button>
          <button
            onClick={() => handleTabChange('monthly')}
            className={`flex-1 py-3 px-4 text-sm font-medium transition-colors ${
              activeTab === 'monthly'
                ? 'text-blue-600 border-b-2 border-blue-600 bg-blue-50'
                : 'text-slate-600 hover:text-slate-800 hover:bg-slate-50'
            }`}
          >
            <DollarSign size={16} className="inline mr-2" />
            Theo tháng
          </button>
          <button
            onClick={() => handleTabChange('shift')}
            className={`flex-1 py-3 px-4 text-sm font-medium transition-colors ${
              activeTab === 'shift'
                ? 'text-blue-600 border-b-2 border-blue-600 bg-blue-50'
                : 'text-slate-600 hover:text-slate-800 hover:bg-slate-50'
            }`}
          >
            <Clock size={16} className="inline mr-2" />
            Theo ca
          </button>
        </div>
      </div>

      {/* Error Message */}
      {error && (
        <div className="bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg mb-6">
          {error}
        </div>
      )}

      {/* Daily Revenue Tab */}
      {activeTab === 'daily' && (
        <div className="bg-white rounded-xl shadow-sm border border-slate-100 p-6">
          <h3 className="text-lg font-semibold text-slate-800 mb-4">Doanh thu theo ngày</h3>
          
          <div className="flex gap-4 mb-6">
            <div className="flex-1">
              <label className="block text-sm font-medium text-slate-700 mb-2">
                Chọn ngày
              </label>
              <input
                type="date"
                value={dailyDate}
                onChange={(e) => setDailyDate(e.target.value)}
                className="w-full px-4 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              />
            </div>
            <div className="flex items-end">
              <button
                onClick={fetchDailyRevenue}
                disabled={loading}
                className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-slate-400 transition-colors flex items-center gap-2"
              >
                {loading ? 'Đang tải...' : 'Xem báo cáo'}
                <ChevronRight size={16} />
              </button>
            </div>
          </div>

          {dailyData && (
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div className="bg-gradient-to-br from-green-50 to-green-100 p-6 rounded-xl border border-green-200">
                <p className="text-sm text-green-700 font-medium mb-1">Tổng doanh thu</p>
                <p className="text-3xl font-bold text-green-800">
                  {(dailyData.revenue || 0).toLocaleString('vi-VN')}đ
                </p>
              </div>
              <div className="bg-gradient-to-br from-blue-50 to-blue-100 p-6 rounded-xl border border-blue-200">
                <p className="text-sm text-blue-700 font-medium mb-1">Số lượt xe</p>
                <p className="text-3xl font-bold text-blue-800">
                  {dailyData.ticketCount || 0}
                </p>
              </div>
              <div className="bg-gradient-to-br from-purple-50 to-purple-100 p-6 rounded-xl border border-purple-200">
                <p className="text-sm text-purple-700 font-medium mb-1">Ngày báo cáo</p>
                <p className="text-xl font-bold text-purple-800">
                  {new Date(dailyDate).toLocaleDateString('vi-VN')}
                </p>
              </div>
            </div>
          )}
        </div>
      )}

      {/* Monthly Revenue Tab */}
      {activeTab === 'monthly' && (
        <div className="bg-white rounded-xl shadow-sm border border-slate-100 p-6">
          <h3 className="text-lg font-semibold text-slate-800 mb-4">Doanh thu theo tháng</h3>
          
          <div className="flex gap-4 mb-6">
            <div className="flex-1">
              <label className="block text-sm font-medium text-slate-700 mb-2">
                Tháng
              </label>
              <select
                value={monthlyMonth}
                onChange={(e) => setMonthlyMonth(parseInt(e.target.value))}
                className="w-full px-4 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              >
                {[...Array(12)].map((_, i) => (
                  <option key={i + 1} value={i + 1}>
                    Tháng {i + 1}
                  </option>
                ))}
              </select>
            </div>
            <div className="flex-1">
              <label className="block text-sm font-medium text-slate-700 mb-2">
                Năm
              </label>
              <input
                type="number"
                value={monthlyYear}
                onChange={(e) => setMonthlyYear(parseInt(e.target.value))}
                className="w-full px-4 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              />
            </div>
            <div className="flex items-end">
              <button
                onClick={fetchMonthlyRevenue}
                disabled={loading}
                className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-slate-400 transition-colors flex items-center gap-2"
              >
                {loading ? 'Đang tải...' : 'Xem báo cáo'}
                <ChevronRight size={16} />
              </button>
            </div>
          </div>

          {monthlyData && (
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
              <div className="bg-gradient-to-br from-green-50 to-green-100 p-6 rounded-xl border border-green-200">
                <p className="text-sm text-green-700 font-medium mb-1">Tổng doanh thu</p>
                <p className="text-3xl font-bold text-green-800">
                  {(monthlyData.revenue || 0).toLocaleString('vi-VN')}đ
                </p>
              </div>
              <div className="bg-gradient-to-br from-blue-50 to-blue-100 p-6 rounded-xl border border-blue-200">
                <p className="text-sm text-blue-700 font-medium mb-1">Số lượt xe</p>
                <p className="text-3xl font-bold text-blue-800">
                  {monthlyData.ticketCount || 0}
                </p>
              </div>
              <div className="bg-gradient-to-br from-purple-50 to-purple-100 p-6 rounded-xl border border-purple-200">
                <p className="text-sm text-purple-700 font-medium mb-1">Kỳ báo cáo</p>
                <p className="text-xl font-bold text-purple-800">
                  {monthlyMonth}/{monthlyYear}
                </p>
              </div>
            </div>
          )}
        </div>
      )}

      {/* Shift Revenue Tab */}
      {activeTab === 'shift' && (
        <div className="bg-white rounded-xl shadow-sm border border-slate-100 p-6">
          <h3 className="text-lg font-semibold text-slate-800 mb-4">Doanh thu theo ca làm việc</h3>
          
          <div className="flex gap-4 mb-6">
            <div className="flex-1">
              <label className="block text-sm font-medium text-slate-700 mb-2">
                Chọn ngày
              </label>
              <input
                type="date"
                value={shiftDate}
                onChange={(e) => setShiftDate(e.target.value)}
                className="w-full px-4 py-2 border border-slate-300 rounded-lg focus:ring-2 focus:ring-blue-500 focus:border-blue-500"
              />
            </div>
            <div className="flex items-end">
              <button
                onClick={fetchShiftRevenue}
                disabled={loading}
                className="px-6 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 disabled:bg-slate-400 transition-colors flex items-center gap-2"
              >
                {loading ? 'Đang tải...' : 'Xem báo cáo'}
                <ChevronRight size={16} />
              </button>
            </div>
          </div>

          {shiftData && shiftData.shifts && (
            <div className="space-y-4">
              {shiftData.shifts.map((shift, index) => (
                <div 
                  key={index}
                  className="bg-gradient-to-r from-slate-50 to-slate-100 p-6 rounded-xl border border-slate-200 hover:shadow-md transition-shadow"
                >
                  <div className="flex justify-between items-center">
                    <div>
                      <p className="text-sm text-slate-600 mb-1">
                        {shift.shiftName || `Ca ${index + 1}`}
                      </p>
                      <p className="text-xs text-slate-500">
                        {shift.startTime} - {shift.endTime}
                      </p>
                    </div>
                    <div className="text-right">
                      <p className="text-2xl font-bold text-slate-800">
                        {(shift.revenue || 0).toLocaleString('vi-VN')}đ
                      </p>
                      <p className="text-sm text-slate-600">
                        {shift.ticketCount || 0} lượt xe
                      </p>
                    </div>
                  </div>
                </div>
              ))}
              
              {/* Total */}
              <div className="bg-gradient-to-br from-green-50 to-green-100 p-6 rounded-xl border-2 border-green-300">
                <div className="flex justify-between items-center">
                  <div>
                    <p className="text-lg font-semibold text-green-800">Tổng cộng</p>
                    <p className="text-sm text-green-700">
                      Ngày {new Date(shiftDate).toLocaleDateString('vi-VN')}
                    </p>
                  </div>
                  <div className="text-right">
                    <p className="text-3xl font-bold text-green-800">
                      {(shiftData.totalRevenue || 0).toLocaleString('vi-VN')}đ
                    </p>
                    <p className="text-sm text-green-700">
                      {shiftData.totalTickets || 0} lượt xe
                    </p>
                  </div>
                </div>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
