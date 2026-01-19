import React, { useState, useEffect } from 'react';
import { 
  LayoutDashboard, 
  Map, 
  LogIn, 
  LogOut, 
  Users, 
  LogOut as SignOutIcon, 
  Menu, 
  Car,
  TrendingUp
} from 'lucide-react';
import { useNavigate, useLocation } from 'react-router-dom';
import { authAPI } from '../services/api';

/**
 * Layout Component - Sidebar chung cho toàn bộ ứng dụng
 * Giữ sidebar luôn hiển thị khi chuyển trang
 */
export default function Layout({ children }) {
  const navigate = useNavigate();
  const location = useLocation();
  const [isSidebarOpen, setIsSidebarOpen] = useState(true);
  const [user, setUser] = useState(null);

  // Load thông tin user từ localStorage
  useEffect(() => {
    const userData = localStorage.getItem('user');
    if (userData) {
      setUser(JSON.parse(userData));
    }
  }, []);

  // Danh sách menu cơ bản (cho tất cả user)
  const baseMenuItems = [
    { name: 'Tổng quan', icon: <LayoutDashboard size={20} />, path: '/dashboard' },
    { name: 'Sơ đồ bãi xe', icon: <Map size={20} />, path: '/parking-map' },
    { name: 'Xe vào (Check-in)', icon: <LogIn size={20} />, path: '/check-in' },
    { name: 'Xe ra (Check-out)', icon: <LogOut size={20} />, path: '/check-out' },
  ];

  // Menu items chỉ dành cho ADMIN
  const adminMenuItems = [
    { name: 'Báo cáo doanh thu', icon: <TrendingUp size={20} />, path: '/reports' },
    { name: 'Quản lý nhân viên', icon: <Users size={20} />, path: '/employees' },
  ];

  // Kết hợp menu items dựa trên role
  const menuItems = user?.role === 'ADMIN' 
    ? [...baseMenuItems, ...adminMenuItems]
    : baseMenuItems;

  const handleMenuClick = (path) => {
    navigate(path);
  };

  const handleLogout = () => {
    if (window.confirm('Bạn có chắc chắn muốn đăng xuất?')) {
      authAPI.logout();
      navigate('/login');
    }
  };

  return (
    <div className="min-h-screen bg-slate-50 flex font-sans">
      
      {/* Sidebar - Fixed */}
      <aside 
        className={`${
          isSidebarOpen ? 'w-64' : 'w-20'
        } bg-slate-900 text-white transition-all duration-300 ease-in-out flex flex-col fixed h-full z-20 shadow-xl`}
      >
        {/* Logo Section */}
        <div className="h-16 flex items-center justify-center border-b border-slate-700">
          <div className="flex items-center gap-2 font-bold text-xl tracking-wider">
            <div className="bg-blue-600 p-1.5 rounded-lg">
              <Car size={20} className="text-white" />
            </div>
            {isSidebarOpen && <span className="animate-fade-in">PMS Admin</span>}
          </div>
        </div>

        {/* Navigation Links */}
        <nav className="flex-1 py-6 px-3 space-y-2">
          {menuItems.map((item, index) => {
            const isActive = location.pathname === item.path;
            return (
              <button
                key={index}
                onClick={() => handleMenuClick(item.path)}
                className={`w-full flex items-center p-3 rounded-lg transition-colors duration-200 group relative
                  ${isActive 
                    ? 'bg-blue-600 text-white shadow-lg shadow-blue-500/30' 
                    : 'text-slate-400 hover:bg-slate-800 hover:text-white'
                  }
                `}
              >
                <span className={`${isSidebarOpen ? 'mr-3' : 'mx-auto'}`}>
                  {item.icon}
                </span>
                {isSidebarOpen && <span className="font-medium text-sm">{item.name}</span>}
                
                {/* Tooltip khi sidebar đóng */}
                {!isSidebarOpen && (
                  <div className="absolute left-full ml-2 px-3 py-2 bg-slate-800 text-white text-sm rounded-lg opacity-0 group-hover:opacity-100 transition-opacity whitespace-nowrap pointer-events-none shadow-lg">
                    {item.name}
                  </div>
                )}
              </button>
            );
          })}
        </nav>

        {/* User Info & Logout */}
        <div className="border-t border-slate-700 p-4">
          {isSidebarOpen ? (
            <div className="space-y-3">
              <div className="flex items-center gap-3 p-2 bg-slate-800 rounded-lg">
                <div className="w-10 h-10 bg-blue-600 rounded-full flex items-center justify-center text-white font-bold text-sm">
                  {user?.username?.charAt(0).toUpperCase() || 'A'}
                </div>
                <div className="flex-1 min-w-0">
                  <p className="text-sm font-semibold text-white truncate">{user?.username || 'Admin'}</p>
                  <p className="text-xs text-slate-400 truncate">{user?.role || 'ADMIN'}</p>
                </div>
              </div>
              <button
                onClick={handleLogout}
                className="w-full flex items-center justify-center gap-2 p-2.5 bg-red-600 hover:bg-red-700 text-white rounded-lg transition-colors text-sm font-medium"
              >
                <SignOutIcon size={18} />
                <span>Đăng xuất</span>
              </button>
            </div>
          ) : (
            <button
              onClick={handleLogout}
              className="w-full flex items-center justify-center p-2.5 bg-red-600 hover:bg-red-700 text-white rounded-lg transition-colors"
            >
              <SignOutIcon size={18} />
            </button>
          )}
        </div>

        {/* Toggle Sidebar Button */}
        <button
          onClick={() => setIsSidebarOpen(!isSidebarOpen)}
          className="absolute -right-3 top-20 bg-slate-800 hover:bg-slate-700 text-white p-1.5 rounded-full shadow-lg transition-colors border-2 border-slate-700"
        >
          <Menu size={16} />
        </button>
      </aside>

      {/* Main Content Area */}
      <main className={`flex-1 transition-all duration-300 ${isSidebarOpen ? 'ml-64' : 'ml-20'}`}>
        {children}
      </main>
    </div>
  );
}
