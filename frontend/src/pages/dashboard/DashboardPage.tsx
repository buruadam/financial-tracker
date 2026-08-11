import { useEffect, useState } from 'react';
import { TrendingUp, TrendingDown, DollarSign, Calendar } from 'lucide-react';
import { useAuth } from '../../context/useAuth';
import { transactionService } from '../../api/transaction/transactionService';
import type { TransactionResponse } from '../../api/transaction/types';

export default function DashboardPage() {
  const { user } = useAuth();
  const [transactions, setTransactions] = useState<TransactionResponse[]>([]);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        setLoading(true);
        setError(null);
        const data = await transactionService.getAllTransactions();
        setTransactions(data);
      } catch (err: any) {
        setError('Failed to load dashboard analytics.');
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();
  }, []);

  const totalIncome = transactions
    .filter((t) => t.transactionType === 'INCOME')
    .reduce((sum, t) => sum + t.amount, 0);

  const totalExpense = transactions
    .filter((t) => t.transactionType === 'EXPENSE')
    .reduce((sum, t) => sum + t.amount, 0);

  const totalBalance = totalIncome - totalExpense;

  const formatCurrency = (amount: number) => {
    return amount.toLocaleString('hu-HU', { minimumFractionDigits: 2 }) + ' Ft';
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center py-24">
        <div className="h-8 w-8 animate-spin rounded-full border-4 border-zinc-950 border-t-transparent"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="p-4 bg-red-50 border border-red-200 text-red-600 rounded-2xl text-sm flex items-center gap-2">
        <span className="w-2 h-2 rounded-full bg-red-500 animate-pulse" />
        {error}
      </div>
    );
  }

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
            <h3 className={`text-2xl font-bold ${totalBalance >= 0 ? 'text-zinc-950' : 'text-red-500'}`}>
              {formatCurrency(totalBalance)}
            </h3>
          </div>
          <div className="p-3 bg-zinc-50 text-zinc-700 rounded-xl">
            <DollarSign size={24} />
          </div>
        </div>

        <div className="bg-white p-6 rounded-2xl border border-zinc-200 shadow-sm flex items-center justify-between">
          <div className="space-y-1">
            <span className="text-xs font-semibold uppercase tracking-wider text-zinc-400">Total Income</span>
            <h3 className="text-2xl font-bold text-emerald-600">
              +{formatCurrency(totalIncome)}
            </h3>
          </div>
          <div className="p-3 bg-emerald-50 text-emerald-600 rounded-xl">
            <TrendingUp size={24} />
          </div>
        </div>

        <div className="bg-white p-6 rounded-2xl border border-zinc-200 shadow-sm flex items-center justify-between">
          <div className="space-y-1">
            <span className="text-xs font-semibold uppercase tracking-wider text-zinc-400">Total Expenses</span>
            <h3 className="text-2xl font-bold text-red-500">
              -{formatCurrency(totalExpense)}
            </h3>
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
            <span className="text-xs text-zinc-400 flex items-center gap-1">
              <Calendar size={14} /> Overall
            </span>
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
