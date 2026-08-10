import { Outlet, Link, useLocation } from 'react-router-dom';
import { Wallet, LayoutDashboard, LogOut, User } from 'lucide-react';
import { useAuth } from '../context/useAuth';

export default function DashboardLayout() {
  const { user, logout } = useAuth();
  const location = useLocation();

  const isActive = (path: string) => location.pathname === path;

  return (
    <div className="flex h-screen bg-zinc-50 text-zinc-950 font-sans">

      <aside className="w-64 bg-white border-r border-zinc-200 flex flex-col justify-between p-6">
        <div className="space-y-8">
          <div className="flex items-center gap-3 px-2">
            <div className="p-2 bg-emerald-50 text-emerald-600 rounded-lg">
              <Wallet size={24} />
            </div>
            <span className="font-bold text-lg tracking-tight">FinanceTracker</span>
          </div>

          <nav className="space-y-1">
            <Link
              to="/dashboard"
              className={`flex items-center gap-3 px-3 py-2.5 rounded-xl font-medium text-sm transition-all ${isActive('/dashboard')
                ? 'bg-emerald-50 text-emerald-700'
                : 'text-zinc-600 hover:bg-zinc-50 hover:text-zinc-900'
                }`}
            >
              <LayoutDashboard size={18} />
              Dashboard
            </Link>
          </nav>
        </div>

        <div className="border-t border-zinc-100 pt-4 space-y-4">
          <div className="flex items-center gap-3 px-2">
            <div className="p-2 bg-zinc-100 text-zinc-600 rounded-full">
              <User size={18} />
            </div>
            <div className="flex flex-col min-w-0">
              <span className="text-sm font-semibold truncate text-zinc-900">{user?.username}</span>
              <span className="text-xs text-zinc-400 truncate">{user?.email}</span>
            </div>
          </div>

          <button
            onClick={logout}
            className="w-full flex items-center gap-3 px-3 py-2.5 rounded-xl font-medium text-sm text-red-600 hover:bg-red-50 transition-all focus:outline-none"
          >
            <LogOut size={18} />
            Sign Out
          </button>
        </div>
      </aside>

      <main className="flex-1 overflow-y-auto p-10">
        <div className="max-w-5xl mx-auto">
          <Outlet />
        </div>
      </main>

    </div>
  );
}
