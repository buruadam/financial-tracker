import { TrendingUp, TrendingDown, DollarSign, Calendar } from 'lucide-react';
import { useAuth } from '../../context/useAuth';

export default function DashboardPage() {
  const { user } = useAuth();

  return (
    <div className="space-y-8">

      <div>
        <h1 className="text-3xl font-bold text-zinc-950 tracking-tight">Hello, {user?.username}!</h1>
        <p className="text-zinc-500 mt-1">Here is a financial overview of your accounts and expenses.</p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">

        <div className="bg-white p-6 rounded-2xl border border-zinc-200 shadow-sm flex items-center justify-between">
          <div className="space-y-1">
            <span className="text-xs font-semibold uppercase tracking-wider text-zinc-400">Total Balance</span>
            <h3 className="text-2xl font-bold text-zinc-950">$12,450.00</h3>
          </div>
          <div className="p-3 bg-zinc-50 text-zinc-700 rounded-xl">
            <DollarSign size={24} />
          </div>
        </div>

        <div className="bg-white p-6 rounded-2xl border border-zinc-200 shadow-sm flex items-center justify-between">
          <div className="space-y-1">
            <span className="text-xs font-semibold uppercase tracking-wider text-zinc-400">Monthly Income</span>
            <h3 className="text-2xl font-bold text-emerald-600">+$4,200.00</h3>
          </div>
          <div className="p-3 bg-emerald-50 text-emerald-600 rounded-xl">
            <TrendingUp size={24} />
          </div>
        </div>

        <div className="bg-white p-6 rounded-2xl border border-zinc-200 shadow-sm flex items-center justify-between">
          <div className="space-y-1">
            <span className="text-xs font-semibold uppercase tracking-wider text-zinc-400">Monthly Expenses</span>
            <h3 className="text-2xl font-bold text-red-600">-$1,850.00</h3>
          </div>
          <div className="p-3 bg-red-50 text-red-600 rounded-xl">
            <TrendingDown size={24} />
          </div>
        </div>

      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">

        <div className="lg:col-span-2 bg-white p-6 rounded-2xl border border-zinc-200 shadow-sm h-80 flex flex-col justify-between">
          <div className="flex justify-between items-center">
            <h4 className="font-bold text-zinc-900">Financial Analytics</h4>
            <span className="text-xs text-zinc-400 flex items-center gap-1"><Calendar size={14} /> Last month</span>
          </div>
          <div className="flex-1 flex items-center justify-center border border-dashed border-zinc-200 rounded-xl mt-4 bg-zinc-50/50">
            <span className="text-sm text-zinc-400 font-medium">Chart placeholder</span>
          </div>
        </div>

        <div className="bg-white p-6 rounded-2xl border border-zinc-200 shadow-sm h-80 flex flex-col justify-between">
          <h4 className="font-bold text-zinc-900">Expenses by Category</h4>
          <div className="flex-1 flex items-center justify-center border border-dashed border-zinc-200 rounded-xl mt-4 bg-zinc-50/50">
            <span className="text-sm text-zinc-400 font-medium">Donut chart placeholder</span>
          </div>
        </div>

      </div>

    </div>
  );
}
