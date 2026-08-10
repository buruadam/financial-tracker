import { Outlet } from 'react-router-dom';
import { Wallet } from 'lucide-react';

export default function AuthLayout() {
  return (
    <div className="min-h-screen flex items-center justify-center bg-zinc-50 px-4">
      <div className="max-w-md w-full bg-white rounded-2xl shadow-xl p-8 border border-zinc-100">
        
        <div className="flex flex-col items-center mb-8">
          <div className="p-3 bg-emerald-50 text-emerald-600 rounded-xl mb-3">
            <Wallet size={32} />
          </div>
          <span className="font-bold text-xl tracking-tight text-zinc-950">Finance Tracker</span>
        </div>

        <Outlet />

      </div>
    </div>
  );
}
